package com.china.stock.test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.china.stock.common.thread.HtmlUtil;

public class Test4 {
	private static String html = "http://q.10jqka.com.cn/index/index/board/market/field/zdf/order/desc/page/assignPage/ajax/1/";
	private static String stock_html = "http://stockpage.10jqka.com.cn/stockCode/";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String marketMark = "";
		String market = "ss";
		if ("hs".equals(market)) {
			html = html.replace("market", market);
			marketMark = "1";
		} else if ("ss".equals(market)) {
			html = html.replace("market", market);
			marketMark = "2";
		} else if ("zxb".equals(market)) {
			html = html.replace("market", market);
			marketMark = "3";
		} else if ("cyb".equals(market)) {
			html = html.replace("market", market);
			marketMark = "4";
		}
		Document doc = HtmlUtil.parserHtml(html.replace("assignPage", "1"));
		Document document = HtmlUtil.parserHtml(html.replace("assignPage", String.valueOf(6)));
		Elements stocksEle = document.getElementsByTag("tbody").select("tr");
		Elements ele = stocksEle.get(14).select("td");
		String id = ele.get(0).text();
		String stockCode = ele.get(1).text();
		String stockName = ele.get(2).text();
		try {
			Document stockDoc = HtmlUtil.parserHtml(stock_html.replace("stockCode", stockCode));
			Elements stockInfodt = stockDoc.getElementsByClass("company_details").select("dt");
			Elements stockInfo = stockDoc.getElementsByClass("company_details").select("dd");
			System.out.println(id+"/"+stockCode+"/"+stockName);
			for (int j = 0; j < stockInfodt.size(); j++) {
				String name = stockInfodt.get(j).text().replace("：", "");
				System.out.println(name);
				if ("总股本".equals(name)) {
					String totalShares = stockInfo.get(j + 1).text();// 总股本
					System.out.println(totalShares);
				}
			}
		} catch (Exception e) {
		}
	}

}
