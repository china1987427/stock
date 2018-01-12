package com.china.stock.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.springframework.util.StringUtils;

import com.china.stock.user.dao.ReadTxtDao;



public class ReadTxtUtil {
	static int stockMarket=2;
	public static void main(String[] args) {
	//	String filePath = "E:\\stock\\沪市.txt";
//		String filePath = "E:\\stock\\基金.txt";
		String filePath = "E:\\stock\\深市.txt";
		readTxtFile(filePath);
	}

	public static void readTxtFile(String filePath) {
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if (!StringUtils.isEmpty(lineTxt)) {
						lineTxt = lineTxt.trim();
					}
					for (int i = 0; i < lineTxt.length(); i++) {
						char everyChar = lineTxt.charAt(i);
						boolean isDigit = Character.isDigit(everyChar);
						if (isDigit) {
							String stockName = lineTxt.substring(0, i);
							String stockCode = lineTxt.substring(i);
							ReadTxtDao rtd= new ReadTxtDao();
							rtd.saveToAllStock(stockCode, stockName, stockMarket);
							break;
						}
					}
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
	}
}