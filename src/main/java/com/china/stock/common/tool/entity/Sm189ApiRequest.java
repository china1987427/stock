package com.china.stock.common.tool.entity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.china.stock.common.tool.base.HMACSHA1;



public class Sm189ApiRequest implements java.io.Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private String app_id="";
	
	private String acceptor_tel="";
	
	private String access_token="";
	
	private String template_id="";
	
	private String template_param="";
	
	private String timestamp="";//yyyy-MM-dd HH:mm:ss
	
	private String sign;
	
	public String genSign(String secret)throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException{
		StringBuffer sb=new StringBuffer();
		sb.append("acceptor_tel=").append(acceptor_tel);
		sb.append("&access_token=").append(access_token);
		sb.append("&app_id=").append(app_id);
		sb.append("&template_id=").append(template_id);
		sb.append("&template_param=").append(template_param);
		sb.append("&timestamp=").append(timestamp);
		setSign(HMACSHA1.getSignature(sb.toString().getBytes("UTF-8"), secret.getBytes("UTF-8")));
		return sign;
	}
	
	public String getPostStr(boolean useSign){
		StringBuffer sb=new StringBuffer();
		sb.append("app_id=").append(app_id);
		sb.append("&acceptor_tel=").append(acceptor_tel);
		sb.append("&access_token=").append(access_token);
		sb.append("&template_id=").append(template_id);
		//sb.append("&template_param=").append(template_param);
		try {
	        sb.append("&template_param=").append(URLEncoder.encode(template_param, "UTF-8"));
	        sb.append("&timestamp=").append(URLEncoder.encode(timestamp, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	        sb.append("&template_param=").append(template_param);
	        sb.append("&timestamp=").append(timestamp);
        }
	
		if(useSign){
		  // sb.append("&sign=").append(sign);
		   
		   try {
		        sb.append("&sign=").append(URLEncoder.encode(sign, "UTF-8"));
	        } catch (UnsupportedEncodingException e) {
		        e.printStackTrace();
		        sb.append("&sign=").append(sign);
	        }
		}
		return sb.toString();
	}
	
	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getAcceptor_tel() {
		return acceptor_tel;
	}

	public void setAcceptor_tel(String acceptor_tel) {
		this.acceptor_tel = acceptor_tel;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getTemplate_param() {
		return template_param;
	}

	public void setTemplate_param(String template_param) {
		this.template_param = template_param;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
    public String toString() {
	    return "Sm189ApiRequest [app_id=" + app_id + ", acceptor_tel=" + acceptor_tel + ", access_token="
	            + access_token + ", template_id=" + template_id + ", template_param=" + template_param + ", timestamp="
	            + timestamp + ", sign=" + sign + "]";
    }
}
