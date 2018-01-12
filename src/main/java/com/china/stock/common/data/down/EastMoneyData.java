package com.china.stock.common.data.down;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class EastMoneyData {
	private static int timeout = 5000;

	public static void main(String[] args) {
		EastMoneyData.getData();
	}

	public static void getData() {
		String html = "http://quote.eastmoney.com/sz000004.html";
		Document doc = parserHtml(html);
		Elements sumEle = doc.getElementById("rtp2").getElementsByTag("tr");
		System.out.println(sumEle);
		// Document document =
		// CommonAdminService.parserHtml(html_hs.replace("assignPage",
		// String.valueOf(n)));
		int m = 1;
		do {
			timeout = (m == 1) ? timeout : timeout + 5000 * (m++);
			// doc = CommonAdminService.parserHtml(html_hs.replace("assignPage",
			// String.valueOf(n)));
			if (m == 5) {
				break;
			}
		} while (doc == null);
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
}
