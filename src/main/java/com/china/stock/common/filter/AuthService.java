package com.china.stock.common.filter;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jsoup.Connection.Response;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.china.stock.common.tool.base.CookieUtil;
import com.china.stock.common.tool.base.EncryptUtil;
import com.china.stock.common.tool.base.HttpUtils;
import com.china.stock.common.tool.base.RedisUtil;
import com.china.stock.common.tool.base.SignUtil;
import com.china.stock.common.tool.conf.Conf;
import com.china.stock.common.tool.entity.UserEntity;
import com.china.stock.common.tool.map.MapMaker;


@Service
public class AuthService {
private static Logger log = Logger.getLogger(AuthService.class);
	
	/**
	 * 
	* @Title: userInfoGet
	* @Description: 根据用户登录名获取用户信息Service方法
	* @param account 帐号
	* @param token 单点登录返回的token
	* @param response
	* @return
	* @throws Exception
	 */
	public boolean userInfoGet(String account,String token,HttpServletResponse response,HttpServletRequest request)throws Exception{
		UserEntity user = new UserEntity();
		user.setSa_LoginName(account);
		user.setToken(token);
		
		Response rsp=HttpUtils.doHttpPost("userUrl", "userDetailGetByCount", MapMaker.makeStrStr("method","user.userInfo.userDetailGetByCount","count",account), user);
		JSONObject userObj=((JSONObject) JSON.parse(rsp.body()));
		if(userObj.get("resultCode").toString().equals("000000")){
			JSONObject userCont=userObj.getJSONObject("content").getJSONObject("items");
			//将返回数据添加到实体类
			user.setMa_ID(userCont.getLong("mb_id"));
			user.setMb_Name(userCont.getString("mb_name"));
			user.setMb_PetName(userCont.getString("mb_petname"));
			user.setMb_County(userCont.getInteger("mb_county"));
			user.setMb_Country(userCont.getInteger("mb_country"));
			user.setMb_Province(userCont.getInteger("mb_province"));
			user.setMb_City(userCont.getInteger("mb_city"));
			user.setSa_ID(userCont.getInteger("sa_id"));
			user.setMs_ID(userCont.getLong("ms_id"));
			//将实体类对象装换为JSON对象并存入Cookie
			Object userInfo=JSON.toJSON(user);
			
			//存入session
			String usercontent = EncryptUtil.aesEncrypt(userInfo.toString(), "xw.tyfo.com");
			request.getSession().setAttribute("TyfoUserEntity", usercontent);
			CookieUtil.cookieAdd(response, "TyfoUserEntity", usercontent);	
			
			//request.setAttribute("TyfoUserEntity", userInfo.toString());
			
			//存入redis中,以保证单点登录返回的token可以在soa中使用
			RedisUtil.set(token, userInfo.toString(), 86400); //有效时间一天
			
			//将用户购物车数量存入session中
			//request.getSession().setAttribute("trolleyNum", getTrolleyNum(user).toString());
			CookieUtil.cookieAdd(response, "trolleyNum",getTrolleyNum(user).toString());
		}else{
			return false;			
		}
		return true;
	}
	
	//单点登录返回后处理
	public boolean codeDeal(Object code,String appkey,HttpServletRequest request,HttpServletResponse response) throws Exception{
		//计算sign
		String sign=SignUtil.getSign(MapMaker.makeStrStr("code",code.toString()),appkey,Conf.getValue("secret"));
		//调用单点登录接口获取token
		Response rsp=HttpUtils.singleLoginPost("userUrl","authtoken",MapMaker.makeStrStr("appkey",appkey,"code",code.toString(),"sign",sign),null);
		//解析返回数据
		JSONObject obj=((JSONObject) JSON.parse(rsp.body()));
		//判断是否请求成功
		if(obj.get("resultCode").equals("000000")){
			JSONObject cont=obj.getJSONObject("content");
			if(StringUtils.isEmpty(cont.get("account"))||StringUtils.isEmpty(cont.get("token"))){
				HttpSession session=request.getSession();
				session.setAttribute("tyfo", "Login");
				
				String redirecturl=URLEncoder.encode(getRedirecturl(request));
				
				response.sendRedirect(Conf.getValue("authcode", "urlConfig/userUrl.properties")+"?appkey="+appkey+"&redirecturl="+redirecturl);
				return false;
			}
			else{
				//调用Service层方法获取用户基本
				if(!userInfoGet(cont.get("account").toString(),cont.get("token").toString(), response,request)){
					log.error("登录拦截器获取用户详细信息失败!");
					throw new Exception("登录拦截器获取用户详细信息失败");
				}
			}
		}
		else{
			log.error("登录拦截器获取单点登录信息失败:"+obj.toJSONString());
			throw new Exception("登录拦截器获取单点登录信息失败");
		}
		return true;
			
	}
	
	/**
	 * 
	* @Title: getRedirecturl
	* @Description: 获取当前请求地址
	* @param request
	* @return
	 */
	public String getRedirecturl(HttpServletRequest request){
		StringBuffer redirecturl=request.getRequestURL();
		String query=request.getQueryString();
		if(query!=null){
			redirecturl.append("?"+query);
		}
		return redirecturl.toString();
	}
	
	/**
	 * 
	* @Description: 根据用户获取购物车数量
	* @param UserEntity uc
	* @return
	* @throws Exception
	 */
	public Integer getTrolleyNum(UserEntity uc) throws Exception {
		Integer goodsNumTotal = 0;// 总商品数量
		try {
			Response http = HttpUtils.doHttpPost("tradeUrl", "trolleyList", MapMaker.makeStrStr("method", "trade.trolley.trolleyList"), uc);
			JSONObject trolleyMap = JSON.parseObject(http.body());

			Map<String, Object> contentMap = trolleyMap.getJSONObject("content");

			if (trolleyMap.get("resultCode").equals("000000")) {// 取值失败
				// 数据处理		
				List<Map<String, Object>> itemMaps = (List<Map<String, Object>>) contentMap.get("items");
				for (Map<String, Object> mapshop : itemMaps) {
					List<Map<String, Object>> buyMaps = (List<Map<String, Object>>) mapshop.get("buy");
					for (Map<String, Object> buymap : buyMaps) {
						goodsNumTotal += Integer.parseInt(buymap.get("GoodsNum").toString());
					}
				}
			}

		} catch (Exception e) {
			log.error(e);
		}
		return goodsNumTotal;
	}
	/**
	 * 
	* @Title: getUserInfoByCount
	* @Description: 根据登录名获取用户信息
	* @param count 登录名
	* @return
	 * @throws Exception 
	 */
	public UserEntity getUserInfoByCount(UserEntity user) throws Exception{
		Response userInfo = HttpUtils.doHttpPost("userUrl","userInfoByCount", MapMaker.makeStrStr("method","user.userInfo.userDetailGetByCount","count",user.getSa_LoginName()), user);
		JSONObject userJson = JSON.parseObject(userInfo.body());
		if (userJson.get("resultCode").equals("000000")) {
			JSONObject items = userJson.getJSONObject("content").getJSONObject("items");
			user.setMs_ID(items.getLong("ms_id"));
		}
		return user;
	}
}
