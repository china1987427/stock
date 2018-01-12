package com.china.stock.common.tool.base;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.china.stock.common.tool.conf.Conf;
import com.china.stock.common.tool.entity.UserEntity;
import com.china.stock.common.tool.map.MapMaker;


public class HttpUtils {
	
	private static Logger log = Logger.getLogger(HttpUtils.class);
	
	public static UserEntity getUeTest()
	{
		String ueStr = "{\"ma_ID\":2,\"mb_City\":394,\"mb_Country\":1,\"mb_County\":2701,\"mb_Name\":\"薛冰\",\"mb_PetName\":\"薛冰\",\"mb_Province\":28,\"sa_LoginName\":\"id01\",\"token\":\"F49A5B956E482AF20332A0DCFEF0B0ED\"}";
		UserEntity ue = (UserEntity)JSON.parseObject(ueStr,new TypeReference<UserEntity>(){});
		return ue;
		//System.out.println(ue.getToken());
	}
	
	public static List<Map<String,Object>> doPostGetItems(String confFileName,String apiName,String method)  throws Exception 
	{
		return doPostGetItems( confFileName, apiName, method,null,null);
	}

	public static List<Map<String,Object>> doPostGetItems(String confFileName,String apiName,String method,Map<String,String> parms,UserEntity ue)  throws Exception 
	{
		if(null==parms||parms.isEmpty())
			parms = MapMaker.makeStrStr("method",method);
		else
			parms.put("method",method);
		
		String body=HttpUtils.doHttpPostEx(
				confFileName, apiName, 
				parms,ue);
		
		String items = JSON.parseObject(body).getJSONObject("content").getString("items");
		
		return (List<Map<String,Object>>)JSON.parseObject(items,new TypeReference<List<Map<String,Object>>>(){});
	}
	
	public static String doHttpPostEx(String fileName, String methodName, Map<String, String> parms, UserEntity uc) throws Exception 
	{
		return doHttpPost(fileName,methodName,parms,uc).body();
	}
	
	public static String doHttpPostEx(String fileName, String methodName, Map<String, String> parms) throws Exception 
	{
		return doHttpPost(fileName,methodName,parms,null).body();
	}
	
	/**
	 * 请求接口方法
	 * @Title doHttpPost
	 * @Description 请求接口方法
	 * @author tianzy
	 * @param fileName 文件名  (不带后缀)
	 * @param methodName 方法名 =key
	 * @param parms 请求参数
	 * @param UserEntity 如果需要登录,则传入,否则为null
	 * @return
	 * @throws Exception
	 */
	public static Response doHttpPost(String fileName, String methodName, Map<String, String> parms, UserEntity uc) throws Exception {
		//除去map中value为空的参数
		Map<String, String> mapTemp=new HashMap<String, String>();
		for (Entry<String, String> map : parms.entrySet()) {
			if(!StringUtils.isEmpty(map.getValue()))
				mapTemp.put(map.getKey(), map.getValue());
		}
		
		Response rsp = null;
		if(uc!=null){
			rsp = Jsoup.connect(Conf.getValue(methodName, "urlConfig/"+fileName+".properties"))
					.header("token",uc.getToken()) //用户登录后返回的token
					.header("appkey", Conf.getValue("appkey")) //由soa分配,必填
					.header("source", Conf.getValue("source")) //来源,必填
					.header("seq", CodecUtil.codeGenerate()) //编码:年月日时分秒+6位随机
					.header("sign", SignUtil.getSign(mapTemp, Conf.getValue("appkey"), Conf.getValue("secret")))
					.ignoreContentType(true)
					.data(mapTemp)
					.timeout(20000)
					.execute();
		}else{
			rsp = Jsoup.connect(Conf.getValue(methodName, "urlConfig/"+fileName+".properties"))				
					.header("appkey", Conf.getValue("appkey")) //由soa分配,必填
					.header("source", Conf.getValue("source")) //来源,必填
					.header("seq", CodecUtil.codeGenerate()) //编码:年月日时分秒+6位随机
					.header("sign", SignUtil.getSign(mapTemp, Conf.getValue("appkey"), Conf.getValue("secret")))
					.ignoreContentType(true)
					.data(mapTemp)
					.timeout(20000)
					.execute();
		}		
		return rsp;
	}
	
	public static String doHttpPostString(String fileName, String methodName, Map<String, String> parms, UserEntity uc) throws Exception {
		//除去map中value为空的参数
		Map<String, String> mapTemp=new HashMap<String, String>();
		for (Entry<String, String> map : parms.entrySet()) {
			if(!StringUtils.isEmpty(map.getValue()))
				mapTemp.put(map.getKey(), map.getValue());
		}
		Document doc=Jsoup.connect(Conf.getValue(methodName, "urlConfig/"+fileName+".properties"))				
				.header("token",uc.getToken()) //用户登录后返回的token
				.header("appkey", Conf.getValue("appkey")) //由soa分配,必填
				.header("source", Conf.getValue("source")) //来源,必填
				.header("seq", CodecUtil.codeGenerate()) //编码:年月日时分秒+6位随机
				.header("sign", SignUtil.getSign(mapTemp, Conf.getValue("appkey"), Conf.getValue("secret")))
				.ignoreContentType(true)
				.data(mapTemp)
				.timeout(20000)
				.post();
		return doc.body().text();
	}
	
	
	/**
	 * 
	* @Title: singleLoginPost
	* @Description: 调用单点登录接口
	* @param fileName
	* @param methodName
	* @param parms
	* @return
	* @throws Exception
	 */
	public static Response singleLoginPost(String fileName, String methodName, Map<String, String> params,UserEntity uc)throws Exception{
		Response rsp=null;

		if(uc!=null){
			params.put("token", uc.getToken());
			rsp=Jsoup.connect(Conf.getValue(methodName, "urlConfig/"+fileName+".properties"))
					.data(params)					
					.timeout(9000)
					.execute();
		}else{
			rsp=Jsoup.connect(Conf.getValue(methodName, "urlConfig/"+fileName+".properties"))
					.data(params)
					.timeout(9000)
					.execute();
		}
		return rsp;
	}
	
	/**
	 * 
	* @Title: singleLoginPost
	* @Description: 调用单点登录接口
	* @param fileName
	* @param methodName
	* @param parms
	* @return
	* @throws Exception
	 */
	public static Response singleLoginPost(String fileName, String methodName)throws Exception{
		Response rsp=null;

		rsp=Jsoup.connect(Conf.getValue(methodName, "urlConfig/"+fileName+".properties"))
				.timeout(9000)
				.execute();
		return rsp;
	}
	
	/**
	 * 
	 * @param request 
	 * @param response
	 * @param responseMsg 要回送的消息
	 * @param stateCode 状态码 正常：200 异常：400
	 */
	public static void doPost(HttpServletRequest request, HttpServletResponse response,String responseMsg) {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			response.setHeader("Cache-Control","no-cache");//HTTP 1.1
			response.setHeader("Pragma","no-cache");//HTTP 1.0
			response.setDateHeader("Expires", 0);

			PrintWriter writer = response.getWriter();
			writer.write(responseMsg);
			writer.flush();
			writer.close();

		} catch (Exception ex) {
			log.error("Error Message------>" + ex.getMessage());
		}
	}
	/**
	 * 
	* @Title: doQrHttpPost
	* @Description: QR溯源认证请求
	* @param fileName
	* @param methodName
	* @param parms
	* @param uc
	* @return
	* @throws Exception
	 */
	public static String doQrHttpPost(String fileName, String methodName, Map<String, String> parms, UserEntity uc) throws Exception {
		//除去map中value为空的参数
		Map<String, String> mapTemp=new HashMap<String, String>();
		for (Entry<String, String> map : parms.entrySet()) {
			if(!StringUtils.isEmpty(map.getValue()))
				mapTemp.put(map.getKey(), map.getValue());
		}
		Document doc=Jsoup.connect(Conf.getValue(methodName, "urlConfig/"+fileName+".properties"))				
				.header("appkey", Conf.getValue("appkey")) //由soa分配,必填
				.header("source", Conf.getValue("source")) //来源,必填
				.header("seq", CodecUtil.codeGenerate()) //编码:年月日时分秒+6位随机
				.header("sign", SignUtil.getSign(mapTemp, Conf.getValue("appkey"), Conf.getValue("secret")))
				.ignoreContentType(true)
				.postDataCharset("utf-8")
				.data(mapTemp)
				.timeout(20000)
				.post();
		return doc.body().text();
	}
}
