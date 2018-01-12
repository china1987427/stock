package com.china.stock.common.tool.controller;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.china.stock.common.tool.base.CookieUtil;
import com.china.stock.common.tool.base.HttpUtils;
import com.china.stock.common.tool.base.ListMapUtil;
import com.china.stock.common.tool.base.RedisUtil;
import com.china.stock.common.tool.entity.MyException;
import com.china.stock.common.tool.entity.UserEntity;
import com.china.stock.common.tool.map.MapMaker;
import com.fasterxml.jackson.databind.ObjectMapper;


public class BaseController {

	

	private HttpServletRequest request;

	private HttpServletResponse response;

	public HttpServletRequest getRequest() {
		return request;
	}

	@Autowired
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	@Autowired
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * 获取当前用户
	 * 
	 * @throws Exception
	 */
	public UserEntity getUser() throws Exception, MyException {
		UserEntity uc = new UserEntity();		
		
		// 获取cookie
		Object loginString = CookieUtil.cookieGet(request, "UserEntity");//request.getSession().getAttribute("UserEntity");
		if (StringUtils.isEmpty(loginString)) {
			if(!StringUtils.isEmpty(request.getAttribute("UserEntity"))){
				loginString = request.getAttribute("UserEntity").toString();
			}			
		}
		if(!StringUtils.isEmpty(loginString)){
			// 将从Cookie获取的JSON字符串转换为实体类对象
			ObjectMapper objectMapper = new ObjectMapper();
			uc = objectMapper.readValue(loginString.toString(), UserEntity.class);
			
			if(StringUtils.isEmpty(RedisUtil.get(uc.getToken()))){
				request.getSession().removeAttribute("UserEntity");
				CookieUtil.cookieDel(response, request, "UserEntity");
	    		//request.setAttribute("UserEntity", "");
				uc = null;
			}
		}else{
			return null;
		}

		return uc;
	}

	/**
	 * 将json数据写回页面
	 * 
	 * @param jsonString
	 *            json信息
	 * @return
	 */
	protected void WriterJson(String jsonString) throws Exception {
		HttpServletResponse response = getResponse();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		//System.out.println(jsonString);
		out.write(jsonString);
		out.flush();
		out.close();
	}

	/**
	 * 组织失败的json,将数据写回页面
	 * 
	 * @param errorCode
	 *            错误码
	 * @param errorMsg
	 *            错误描述信息
	 * @return
	 * @throws Exception
	 */
	public void doPostErr(int errorCode, String errorMsg) throws Exception {
		String outJson = JSON.toJSONString(MapMaker.makeStrObj("resultCode", errorCode, "msg", errorMsg));
		WriterJson(outJson);
	}

	/**
	 * 组织成功的json,将数据写回页面
	 * 
	 * @param content
	 *            内容
	 * @return
	 * @throws Exception
	 */
	public void doPostSucc(Object content) throws Exception {
		String outJson = JSON.toJSONString(MapMaker.makeStrObj("resultCode", "000000", "msg", "执行成功", "content", content));
		WriterJson(outJson);
	}

	/**
	 * 组织成功的json,将数据写回页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public void doPostSucc() throws Exception {

		String outJson = JSON.toJSONString(MapMaker.makeStrObj("resultCode", "000000", "msg", "执行成功"));
		WriterJson(outJson);
	}

	/**
	 * 通过判断排除不必填参数为空的
	 * 
	 * @author puwc
	 * @param params
	 *            存参map
	 * @param keys
	 *            参数主键的字符串,以逗号隔开
	 * @return
	 */
	public Map<String, String> getParamMap(String keys) {
		Map<String, String> params = ListMapUtil.getEmptyMapString();
		if (!StringUtils.isEmpty(keys)) {
			for (String key : keys.split(",")) {
				if (!StringUtils.isEmpty(getRequest().getParameter(key))) {
					params.put(key, getRequest().getParameter(key));
				}
			}
		}
		return params;
	}

	public Map<String, String> getParamMap(Map<String, String> params, String keys) {
		if (!StringUtils.isEmpty(keys)) {
			for (String key : keys.split(",")) {
				if (!StringUtils.isEmpty(getRequest().getParameter(key))) {
					params.put(key, getRequest().getParameter(key));
				}
			}
		}
		return params;
	}

	/**
	 * 
	 * @Title: getRedirecturl
	 * @Description: 获取当前请求地址
	 * @param request
	 * @return
	 */
	public String getRedirecturl(HttpServletRequest request) {
		StringBuffer redirecturl = getRequest().getRequestURL();
		String query = getRequest().getQueryString();
		if (query != null) {
			redirecturl.append("?" + query);
		}
		return redirecturl.toString();
	}

	/**
	 * 
	 * @Title: getParamsMap
	 * @Description: 获取提交的所有参数
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getParameterMap() throws Exception {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = getRequest().getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = getRequest().getParameter(en);
				res.put(en, value);
			}
		}
		return res;
	}
	public void getToken(String username,String openID) {
		String token = "";
		try {
			Map<String, String> param = MapMaker.makeStrStr("method","userService.userInfo.userLoginIp");
			param.put("account", username);
			param.put("ip", "192.168.20.41");
			JSONObject infoResp=JSON.parseObject(HttpUtils.doHttpPost("yueUrl", "autoLogin", param, null).body());
			token = infoResp.getJSONObject("content").get("token").toString();
			RedisUtil.set("token/"+openID, token);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
