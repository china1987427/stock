package com.china.stock.common.data;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchNews{
	
	public static void main(String[] args) {
		String a =request("1");
		System.out.println(a);
	}

	/**
	 * @param urlAll
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 * 2419005293576472441  channelId凤凰财经Id
	 */
	public static String request(String page) {
		String httpUrl = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news";
		String httpArg = "channelId=5572a109b3cdc86cf39001e0&channelName=%E8%B4%A2%E7%BB%8F%E6%9C%80%E6%96%B0";
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    httpUrl = httpUrl + "?" + httpArg+"&page="+page;

	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");
	        // 填入apikey到HTTP header
	        connection.setRequestProperty("apikey",  "0b7f79b2d1d83ca86ff8342ae0c884d3");
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
}
