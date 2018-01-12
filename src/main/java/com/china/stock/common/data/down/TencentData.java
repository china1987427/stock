package com.china.stock.common.data.down;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class TencentData {
	private static int timeout = 5000;

	public static void main(String[] args) {
		TencentData.getData();
	}

	public static void getData() {
		String html = "http://gu.qq.com/sz000002/gp";
		Document doc = null;
		int m = 1;
		do {
			timeout = (m == 1) ? timeout : timeout + 5000 * (m++);
			doc = parserHtml(html);
			if (m == 5) {
				break;
			}
		} while (doc == null);
		Elements ele = doc.getElementById("hqpanel").getElementsByClass("bl");
		String marketCapitalization = ele.get(6).text();//总市值
		System.out.println(marketCapitalization);
		
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
