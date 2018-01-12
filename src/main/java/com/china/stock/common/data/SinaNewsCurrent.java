package com.china.stock.common.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SinaNewsCurrent {
	public static void main(String[] args) {
		String a = SinaNewsCurrent.getCurrentData();
		List list = new ArrayList();
		list.add(a);
		System.out.println(list);
	}

	
	public static String getCurrentData() {
		String httpurl = "http://roll.news.sina.com.cn/interface/rollnews_ch_out_interface.php";
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		try {
			URL url = new URL(httpurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "gb2312"));
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
