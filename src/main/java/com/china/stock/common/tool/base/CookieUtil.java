package com.china.stock.common.tool.base;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	public static void cookieAdd(HttpServletResponse response, String key,
			String value) throws UnsupportedEncodingException {
		Cookie cookie = new Cookie(key, URLEncoder.encode(value, "UTF-8"));
		cookie.setMaxAge(24 * 60 * 60 * 14);
		cookie.setPath("/");
		//cookie.setDomain(".tyfo.com");
		response.setCharacterEncoding("UTF-8 ");
		response.addCookie(cookie);
	}
	
	public static void cookieAdd(HttpServletResponse response, String key,
			String value,int time) throws UnsupportedEncodingException {
		Cookie cookie = new Cookie(key, URLEncoder.encode(value, "UTF-8"));
		cookie.setMaxAge(time);
		cookie.setPath("/");
		//cookie.setDomain(".tyfo.com");
		response.setCharacterEncoding("UTF-8 ");
		response.addCookie(cookie);
	}

	public static void cookieUpdate(HttpServletResponse response,HttpServletRequest request, String key,String value) throws UnsupportedEncodingException {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(key)) {
					cookie.setValue(URLEncoder.encode(value, "UTF-8"));
					//cookie.setDomain(".tyfo.com");
					response.addCookie(cookie);
					break; 					
				}
			}
		}
	}
	
	public static String cookieGet(HttpServletRequest request, String key) throws UnsupportedEncodingException {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(key)) {
					return URLDecoder.decode(cookie.getValue(),"UTF-8"); 
				}
			}
		}
		return "";
	}

	public static void cookieDel(HttpServletResponse response,
			HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(key)) {
					cookie.setMaxAge(0);
					cookie.setPath("/");
					//cookie.setDomain(".tyfo.com");
					response.addCookie(cookie);
					break;
				}
			}
		}
	}
}
