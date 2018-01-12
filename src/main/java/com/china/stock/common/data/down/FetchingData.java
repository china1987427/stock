package com.china.stock.common.data.down;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import com.china.stock.common.server.CommonAdminService;

public class FetchingData {

	private static int timeout = 5000;
	private static String html_hs = "http://q.10jqka.com.cn/index/index/board/hs/field/zdf/order/desc/page/assignPage/ajax/1/";//上证A股
	private static String html_ss = "http://q.10jqka.com.cn/index/index/board/ss/field/zdf/order/desc/page/assignPage/ajax/1/";//深证A股
	private static String html_zxb = "http://q.10jqka.com.cn/index/index/board/zxb/field/zdf/order/desc/page/assignPage/ajax/1/";//中小板
	private static String html_cyb = "http://q.10jqka.com.cn/index/index/board/cyb/field/zdf/order/desc/page/assignPage/ajax/1/";//创业板

	public static void main(String[] args) {
		FetchingData.getData();
	}

	public static Document parserHtml(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).timeout(5000).get();
		} catch (IOException e) {
			return null;
		}
		return doc;
	}

	public static void getData() {
		Document doc = CommonAdminService.parserHtml(html_hs.replace("assignPage", "1"));
		Elements sumEle = doc.getElementById("m-page").getElementsByTag("a");
		int sumPage = StringUtils.isEmpty(sumEle.get(sumEle.size() - 1).attr("page")) ? 0 : Integer
				.valueOf(sumEle.get(sumEle.size() - 1).attr("page"));
		for (int n = 1; n <= 5; n++) {
			Document document = CommonAdminService.parserHtml(html_hs.replace("assignPage", String.valueOf(n)));
			int m = 1;
			do {
				timeout = (m == 1) ? timeout : timeout + 5000 * (m++);
				doc = CommonAdminService.parserHtml(html_hs.replace("assignPage", String.valueOf(n)));
				if (m == 5) {
					break;
				}
			} while (doc == null);
			Elements stocksEle = document.getElementsByTag("tbody").select("tr");
			for (int i = 0; i < stocksEle.size(); i++) {
				Elements ele = stocksEle.get(i).select("td");
				String stockCode = ele.get(1).text();
				String stockName = ele.get(2).text();
				String cash = ele.get(3).text();
				String riseorfallRange = ele.get(4).text();// 涨跌幅
				String riseOrFall = ele.get(5).text();// 涨跌
				String handover = ele.get(7).text();// 换手
				String volamountRatio = ele.get(8).text();// 量比
				String amplitude = ele.get(9).text();// 振幅
				String volumeOfBusiness = ele.get(10).text();// 成交额
				String circulationStock = ele.get(11).text();// 流通股
				String circulationMarketValue = ele.get(12).text();// 流通市值
				String priceEarningsRatio = ele.get(13).text();// 市盈率
				System.out.println(stockCode + "/" + stockName + "/" + cash + "/" + riseorfallRange + "/"
						+ riseOrFall + "/" + handover + "/" + volamountRatio + "/" + amplitude + "/"
						+ volumeOfBusiness + "/" + circulationStock + "/" + circulationMarketValue + "/"
						+ priceEarningsRatio);
				// stockDataService.saveChangeRate(currencyCode, currencyName,
				// newPrice, changeAmount,
				// riseorfallRange, opening);
			}
		}
	}
}
