package com.china.stock.test;

import java.net.URL;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;

public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "http://stockpage.10jqka.com.cn/realHead_v2.html#hs_603466";
		try {
			WebClient webClient = new WebClient(BrowserVersion.CHROME);
			// 设置webClient的相关参数
			webClient.addRequestHeader("Accept", "*/*");
			webClient.addRequestHeader("Accept-Charset", "utf-8");
			webClient.addRequestHeader("Connection", "keep-alive");
			webClient.addRequestHeader("Access-Control-Allow-Origin", "*");
			webClient.addRequestHeader("Host", "stockpage.10jqka.com.cn");
			// webClient.addRequestHeader("Host", "s.thsi.cn");
			webClient
					.addRequestHeader(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
			webClient.addRequestHeader("Referer", "http://stockpage.10jqka.com.cn/realHead_v2.html");
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setActiveXNative(false);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.waitForBackgroundJavaScript(10 * 1000);
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			HtmlPage rootPage = webClient.getPage(url);
			System.out.println(rootPage.asText());
			System.out.println("为了获取js执行的数据 线程开始沉睡等待");
			Thread.sleep(3000);// 主要是这个线程的等待 因为js加载也是需要时间的
			System.out.println("线程结束沉睡");
			String html = rootPage.asText();
			System.out.println(html);
			webClient.closeAllWindows();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
