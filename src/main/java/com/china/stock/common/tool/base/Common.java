package com.china.stock.common.tool.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>session和cookie的操作类</p>
 * 
 * @version 1.0.0
 */
public class Common {
	
	static Logger logger = Logger.getLogger(Common.class);
	
	@Autowired
	private static HttpServletRequest request;

	/**
	 * 验证是否合法请求
	 * @param params 参数名与参数值的键值对
	 * @param ssign 客户端上传的签名
	 * @return 返回验证结果Map<String,Object>---->key=result value=true/false;key=msg value=非法请求/合法请求
	 */
	public static Map<String,Object> authentication (Map<String, String> params){
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			String appkey = request.getHeader("app" + "key");
			String ssign = request.getHeader("sign");
			if(StrUtils.isEmpty(appkey)||StrUtils.isEmpty(ssign)){
				result.put("result", false);
				result.put("msg", "非法请求。");
			}else{
				String secret = XmlUtil.getNodeAttrbute("config/keysecretmap.xml", "Maps/Map[@key='"+appkey+"']", "secret");
				String dsign = SignUtil.getSign(params, appkey, secret);
				if(dsign.equals(ssign)){
					result.put("result", true);
					result.put("msg", "合法请求。");
				}else{
					result.put("result", false);
					result.put("msg", "非法请求。");
				}
			}
		} catch (IOException e) {
			result.put("result", false);
			result.put("msg", e.getMessage());
		}
		return result;
	}
	
	public static String getRandom(){
		StringBuilder sb = new StringBuilder();
		String[] array = {"a","b","c","d","e","f","g","h","i",
						  "j","k","l","m","n","o","p","q","r",
						  "s","t","u","v","w","x","y","z","0",
						  "1","2","3","4","5","6","7","8","9"};
		Random random = new Random();
		for(int i=0;i<4;i++){
			sb.append(array[random.nextInt(36)]);
		}
		return sb.toString();
	}
	
	/**
	 * 返回length长度的随机数
	 * @param length
	 * @return
	 */
	public static String getRandom(int length){
		StringBuilder sb = new StringBuilder();
		String[] array = {"a","b","c","d","e","f","g","h","i",
						  "j","k","l","m","n","o","p","q","r",
						  "s","t","u","v","w","x","y","z","0",
						  "1","2","3","4","5","6","7","8","9"};
		Random random = new Random();
		for(int i=0;i<length;i++){
			sb.append(array[random.nextInt(36)]);
		}
		return sb.toString();
	}
	
	public static boolean checkSign(Map<String,Object> paramMap,String sign)throws Exception{
		//boolean result = true;
		StringBuffer sb = new StringBuffer();
		String sp="";
		for(Entry<String,Object> entry:paramMap.entrySet()){
			sb.append(sp);
			sb.append(entry.getKey()+"="+entry.getValue());
			sp="&";
		}
		//logger.error("checkSign-------->"+sb.toString()+"====="+EncryptUtil.md5Digest(sb.toString(), 32));
		return sign.equals(EncryptUtil.md5Digest(sb.toString(), 32));
	}
	
	public static String getRemoteHost(HttpServletRequest request){

	    String ip = request.getHeader("x-forwarded-for");

	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }

	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }

	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		/*for(int i=0;i<100;i++){
		}*/
		//SimpleDateFormat format = new SimpleDateFormat("yyyy")
		System.out.println(String.valueOf(System.currentTimeMillis()/1000));
		System.out.println(URLEncoder.encode("重庆", "utf-8"));
		System.out.println(URLDecoder.decode("%E9%87%8D%E5%BA%86", "utf-8"));
		
	}

}
