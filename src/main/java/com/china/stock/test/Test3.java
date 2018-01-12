package com.china.stock.test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Test3 {
	static DirectoryStream<Path> directoryStream;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String path = "D:\\stock\\hz_stock.xls";// Excel文件URL
//		InputStream is;
//		try {
//			is = new FileInputStream(path);
//			Workbook wb = new XSSFWorkbook(is);
//			Sheet st = wb.getSheetAt(0);
//			Row row = st.getRow(0);
//			Cell cell = row.getCell(0);
//			String str = cell.getStringCellValue();
//			System.out.println(str);
//			is.close();// 关闭输入流
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		String str = "D:\\stock\\aa.xls";
		System.out.println(Test3.isExcel2003(str));
	}

	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}
}
