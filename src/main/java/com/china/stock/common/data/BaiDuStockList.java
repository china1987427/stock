package com.china.stock.common.data;

/**
 * 获取所有股票列表
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.user.dao.ReadTxtDao;


public class BaiDuStockList {
	private static String rows = "20";
	private static String httpUrl = "http://apis.baidu.com/tehir/stockassistant/stocklist";
	private static String apikey="0b7f79b2d1d83ca86ff8342ae0c884d3";
	public static void main(String[] args) {
		try {
			//for (int i = 58; i < 140; i++) {
			//	String page = String.valueOf(i);
				String page = "1";
				String httpArg = "page=" + page + "&rows=" + rows;
				System.out.println(httpArg);
				String jsonResult = request(httpUrl, httpArg);
				JSONObject rspJson = JSON.parseObject(jsonResult);
				String total = rspJson.getString("total");
				JSONArray stocks = rspJson.getJSONArray("rows");
				for (Object obj : stocks) {
					JSONObject stock = JSON.parseObject(obj.toString());
					String code = stock.getString("code");
					String name = stock.getString("name");
					String industry = stock.getString("industry");
					String area = stock.getString("area");
					float pe = ObjUtil.toFloat(stock.getString("pe"));// 市盈率
					double outstanding = ObjUtil.toDouble(stock.getString("outstanding"));// 流通股本
					double totals = ObjUtil.toDouble(stock.getString("totals"));// 总股本(万)
					float eps = ObjUtil.toFloat(stock.getString("eps"));// 每股收益
					float bvps = ObjUtil.toFloat(stock.getString("bvps"));// 每股净资
					float pb = ObjUtil.toFloat(stock.getString("pb"));// 市净率
					String timetomarket = stock.getString("timetomarket");
					ReadTxtDao rtd = new ReadTxtDao();
					rtd.saveToStockInfo(code, name, industry, area, pe, outstanding, totals, eps, bvps, pb,
							timetomarket);
				}
				if (stocks.size() < 20) {
					System.out.println("完成数据插入工作");
					//break;
				}
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param urlAll
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 */
	public static String request(String httpUrl, String httpArg) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		String http = httpUrl + "?" + httpArg;

		try {
			URL url = new URL(http);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			// 填入apikey到HTTP header
			connection.setRequestProperty("apikey", apikey);
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
