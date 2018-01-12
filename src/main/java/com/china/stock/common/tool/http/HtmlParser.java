package com.china.stock.common.tool.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
	private final static String html1 = "http://quote.eastmoney.com/stocklist.html";
	private static String html2 = "http://quote.eastmoney.com/stockcode.html";
	private static String html3 = "http://f10.eastmoney.com/f10_v2/BusinessAnalysis.aspx?code=stockcode";

	public static void main(String[] args) {
		Document doc1 = HtmlParser.parserHtml(html1);
		Elements listA = doc1.getElementsByAttributeValue("id", "quotesearch").tagName("li").select("a");
		int i = 1;
		Map<String,Object> map = new HashMap<String,Object>();
		List l = new ArrayList();
		for (Element e : listA) {
			i++;
			if (i == 5) {
				String href = e.attr("href");
				String name = e.attr("name");
				if (!"#sh".equals(href) && !"#sz".equals(href) && !"sh".equals(name) && !"sz".equals(name)) {
					// String num = href.split("com/")[1].replace(".html", "");
					String num = "sh600000";
					String stocknum = num.substring(0, 2);
					String stockcode = num.substring(2);
					String text = e.text().trim();
					String stockname=text.split("\\(")[0];
					l.add(stockcode);
					l.add(stockname);
					html2 = html2.replace("stockcode", num);
					Document doc2 = HtmlParser.parserHtml(html2);
					Elements list = doc2.getElementsByAttributeValue("id", "rtp2").select("td");
					for (Element element : list) {
						String tdtext = element.text();
						String key = tdtext.split("：")[0];
						String value = tdtext.split("：")[1];
						l.add(value);
					}
					html3 = html3.replace("stockcode", num);
					Document doc3 = HtmlParser.parserHtml(html3);
					//Elements article = doc3.getElementsByAttributeValue("class", "article");
					String article = doc3.getElementsByAttributeValue("class", "article").text();
					l.add(article);
					System.out.println(l);
				}
			}
		}
	}

	public static Document parserHtml(String url) {
		// System.out.println(url);
		Document doc = null;
		try {
			doc = Jsoup.connect(url).timeout(5000).get();
		} catch (IOException e) {
			return null;
		}
		return doc;
	}
}
