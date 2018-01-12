package com.china.stock.common.thread;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import com.china.stock.admin.dao.StockDataDao;
import com.china.stock.admin.server.AdminService;

public class HtmlUtil {

	@Autowired
	private StockDataDao stockDataDao;
	@Autowired
	private AdminService adminService;

	public static void main(String[] args) {
		// HtmlUtil hu = new HtmlUtil();
		// hu.getStocksData("hs");
		HtmlUtil.downLoadFromUrl(
				"http://www.szse.cn/szseWeb/ShowReport.szse?SHOWTYPE=xlsx&CATALOGID=1110&tab2PAGENO=1&ENCODE=1&TABKEY=tab2",
				"sz_stock.xlsx", "D:/stock");
	}

	public static Document parserHtml(String url) {
		Document doc = null;
		try {
			Connection con = Jsoup
					.connect(url)
					.data("query", "Java") // 请求参数
					.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36") // 设置 User-Agent
					.timeout(30000); // 设置连接超时时间
			/*
			 * .ignoreHttpErrors(true) .followRedirects(true)
			 */

			Connection.Response resp = con.execute();
			if (resp.statusCode() == 200) {
				doc = con.get();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 从网络Url中下载文件
	 * 
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static void downLoadFromUrl(String urlStr, String fileName,
			String savePath) {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			// 设置超时间为3秒
			conn.setConnectTimeout(3 * 1000);
			// 防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

			// 得到输入流
			InputStream inputStream = conn.getInputStream();
			// 获取自己数组
			byte[] getData = readInputStream(inputStream);

			// 文件保存位置
			File saveDir = new File(savePath);
			if (!saveDir.exists()) {
				saveDir.mkdir();
			}
			File file = new File(saveDir + File.separator + fileName);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(getData);
			if (fos != null) {
				fos.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			System.out.println("info:" + url + " download success");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			while ((len = inputStream.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
}
