package com.china.stock.common.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WaiWangIP {
	public static void main(String[] args) throws Exception {
		String result=WaiWangIP.getWebIp();
		System.out.println(result);
		
	//	String a=WaiWangIP.getSrc();
		//System.out.println(a);
	}

	public static String getWebIp() {
		String httpUrl =WaiWangIP.getSrc();
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		String d="";
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "GB2312"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
			int a =result.indexOf("<center>");
			int b =result.indexOf("</center>");
			String c=result.substring(a, b);
			d=c.split("来自：")[1];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}
	
	public static String getSrc() {
		String httpUrl = "http://www.ip138.com/";
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		String f="";

		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "GB2312"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
			int a=result.indexOf("<iframe src=");
			int b=result.indexOf("></iframe>");
			String c=result.substring(a, b);
			String[] d = c.split(" ");
			String e= d[1];
			f = e.split("=")[1].replace("\"", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
}
