package com.china.stock.common.data;

/**
 * 泸港通十大成交股
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HgtTen {
	public static void main(String[] args) {
		String a = HgtTen.request("2016-06-30");
		System.out.println(a);
	}

	public static String request(String date) {
		String httpUrl = "http://apis.baidu.com/tehir/stockassistant/hgtten";
		// String httpArg = "date=2015-12-04";
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		httpUrl = httpUrl + "?date=" + date;
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			// 填入apikey到HTTP header
			connection.setRequestProperty("apikey", "0b7f79b2d1d83ca86ff8342ae0c884d3");
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
