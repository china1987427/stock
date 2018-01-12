package com.china.stock.common.data;

/**
 * 获取历史数据
 * 2016-03-22,3.960,3.980,3.940,3.910,264513//新浪数据
 * 日期，开盘，最高，收盘，最低，总手
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class StockHistoryDataConnection {
	// static String
	// url="http://biz.finance.sina.com.cn/stock/flash_hq/kline_data.php?&rand=random(10000)&symbol=sh600653&end_date=20160322&begin_date=20160301&type=plain";//
	// 新浪股票行情历史接口
	// static String url =
	// "http://q.stock.sohu.com/hisHq?code=cn_600653&start=20160220&end=20160321&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp";//
	// 搜狐股票行情历史接口
	private static String httpUrl = "http://q.stock.sohu.com/hisHq?stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp";

	public static void main(String[] args) {
		String a =getStockHistoryData("600000", "20171225", "20171225");
		System.out.println(a);
	}

	public static String getStockHistoryData(String code, String startDate, String endDate) {
		if ("dapan".equals(code)) {
			code = "zs_000001";
		}
		String httpParam = "code=cn_" + code + "&start=" + startDate + "&end=" + endDate;
		URL ur = null;
		String stockData = "";
		String url = httpUrl + "&" + httpParam;
		try {
			ur = new URL(url);
			// HttpURLConnection uc = (HttpURLConnection) ur.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(ur.openStream(), "GBK"));
			String line;
			while ((line = reader.readLine()) != null) {
				stockData = line.replace("historySearchHandler(", "").replace(")", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stockData;
	}
}
