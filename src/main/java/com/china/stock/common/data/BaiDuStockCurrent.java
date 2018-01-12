package com.china.stock.common.data;

/**
 * 获取股票的实时数据
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.china.stock.common.tool.controller.BaseController;


public class BaiDuStockCurrent extends BaseController {
	public static void main(String[] args) {
		BaiDuStockCurrent bdsc = new BaiDuStockCurrent();
		String a =bdsc.request("002043", "2");
		System.out.println(a);
	}

	/**
	 * @param urlAll
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 */
	public String request(String code, String mark) {
		String result = null;
		try {
			String httpUrl = "http://apis.baidu.com/apistore/stockservice/stock";
			String market = "";
			if ("1".equals(mark)) {
				market = "sh";
			} else if ("2".equals(mark)) {
				market = "sz";
			}
			String httpArg = "stockid=" + market + code + "&list=1";
			BufferedReader reader = null;
			StringBuffer sbf = new StringBuffer();
			httpUrl = httpUrl + "?" + httpArg;

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
