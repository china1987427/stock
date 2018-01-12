package com.china.stock.common.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.china.stock.admin.server.DapanDataService;
import com.china.stock.admin.server.StockDataService;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.util.StringUtil;


@Service
public class CommonAdminService {
	private static Logger log = Logger.getLogger(CommonAdminService.class);
	private final static String html = "http://forex.eastmoney.com/CNY.html";
	static int timeout = 5000;
	@Autowired
	private StockDataService stockDataService;
	@Autowired
	private DapanDataService dapanDataService;

	public static Document parserHtml(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).timeout(5000).get();
		} catch (IOException e) {
			return null;
		}
		return doc;
	}

	public void getRMBExchangeRate() {
		Document doc = CommonAdminService.parserHtml(html);
		int m = 1;
		do {
			timeout = (m == 1) ? timeout : timeout + 5000 * (m++);
			doc = CommonAdminService.parserHtml(html);
			if (m == 5) {
				break;
			}
		} while (doc == null);
		Elements elements = doc.getElementsByAttributeValue("id", "tab1_zone2").select("ul");
		for (int i = 1; i < elements.size(); i++) {
			Elements ele = elements.get(i).select("li");
			String currencyCode = ele.get(0).select("a").text();
			String currencyName = ele.get(1).select("a").text();
			String newPrice = ele.get(2).select("span").text();
			String changeAmount = ele.get(3).select("span").text();
			String riseorfallRange = ele.get(4).select("span").text();
			String opening = ele.get(5).select("span").text();
			stockDataService.saveChangeRate(currencyCode, currencyName, newPrice, changeAmount, riseorfallRange, opening);
		}
	}

	public void stockIntervalRisefall() {
		try {
			List<Map<String, Object>> stocks = new ArrayList<Map<String, Object>>();
			String now = StringUtil.getStrDate("yyyy-MM-dd", new Date());
			Date nowDate = StringUtil.getDate("yyyy-MM-dd", now);
			Date thisDate = null;
			int n = 1;
			Map<String, Object> mapdate = stockDataService.getLimitUpDown();
			int count = ObjUtil.toInt(mapdate.get("count"));
			String theDate = ObjUtil.toString(mapdate.get("max")==null?"":mapdate.get("max"));
			if (count == 0) {
				mapdate = stockDataService.checkStockEveryDayData("");
				theDate = ObjUtil.toString(mapdate.get("min"));
				theDate = StringUtil.getStrByLT("yyyy-MM-dd", theDate, -1);
			}
			do {
				theDate = StringUtil.getStrByLT("yyyy-MM-dd", theDate, n);
				JSONArray json1 = new JSONArray();
				JSONArray json2 = new JSONArray();
				JSONArray json3 = new JSONArray();
				JSONArray json4 = new JSONArray();
				JSONArray json5 = new JSONArray();
				JSONArray json6 = new JSONArray();
				JSONArray json7 = new JSONArray();
				JSONArray json8 = new JSONArray();
				JSONArray json9 = new JSONArray();
				JSONArray json10 = new JSONArray();
				JSONArray json11 = new JSONArray();
				JSONArray json12 = new JSONArray();
				JSONArray json13 = new JSONArray();
				JSONArray json14 = new JSONArray();
				JSONArray json15 = new JSONArray();
				JSONArray json16 = new JSONArray();
				JSONArray json17 = new JSONArray();
				JSONArray json18 = new JSONArray();
				JSONArray json19 = new JSONArray();
				JSONArray json20 = new JSONArray();
				JSONArray json21 = new JSONArray();
				thisDate = StringUtil.getDate("yyyy-MM-dd", theDate);
				String week = StringUtil.getWeek(theDate);
				stocks = stockDataService.getSingleStock("", theDate);
				if (!CollectionUtils.isEmpty(stocks)) {
					for (Map<String, Object> map : stocks) {
						double amplitude = ObjUtil.toDouble(map.get("amplitude"));
						String stock_code = ObjUtil.toString(map.get("stock_code")).trim();
						if (-10 < amplitude && amplitude < -9) {
							json1.add(stock_code);
						} else if (-9 <= amplitude && amplitude < -8) {
							json2.add(stock_code);
						} else if (-8 <= amplitude && amplitude < -7) {
							json3.add(stock_code);
						} else if (-7 <= amplitude && amplitude < -6) {
							json5.add(stock_code);
						} else if (-6 <= amplitude && amplitude < -5) {
							json5.add(stock_code);
						} else if (-5 <= amplitude && amplitude < -4) {
							json6.add(stock_code);
						} else if (-4 <= amplitude && amplitude < -3) {
							json7.add(stock_code);
						} else if (-3 <= amplitude && amplitude < -2) {
							json8.add(stock_code);
						} else if (-2 <= amplitude && amplitude < -1) {
							json9.add(stock_code);
						} else if (-1 <= amplitude && amplitude < 0) {
							json10.add(stock_code);
						} else if (0 <= amplitude && amplitude < 1) {
							json11.add(stock_code);
						} else if (1 <= amplitude && amplitude < 2) {
							json12.add(stock_code);
						} else if (2 <= amplitude && amplitude < 3) {
							json13.add(stock_code);
						} else if (3 <= amplitude && amplitude < 4) {
							json14.add(stock_code);
						} else if (4 <= amplitude && amplitude < 5) {
							json15.add(stock_code);
						} else if (5 <= amplitude && amplitude < 6) {
							json16.add(stock_code);
						} else if (6 <= amplitude && amplitude < 7) {
							json17.add(stock_code);
						} else if (7 <= amplitude && amplitude < 8) {
							json18.add(stock_code);
						} else if (8 <= amplitude && amplitude < 9) {
							json19.add(stock_code);
						} else if (9 <= amplitude && amplitude < 10) {
							json20.add(stock_code);
						} else if (10 <= amplitude) {
							json21.add(stock_code);
						}
					}
					if (!CollectionUtils.isEmpty(stocks) && thisDate.getTime() <= nowDate.getTime()) {
						stockDataService.saveLimitUpOrDown(theDate, week, json1, json21);
						int num = stockDataService.stockIntervalRisefall(theDate, week, json1, json2, json3, json4, json5, json6, json7, json8,
								json9, json10, json11, json12, json13, json14, json15, json16, json17, json18, json19, json20, json21);
					}
				}
			} while (thisDate.getTime() < nowDate.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}
}
