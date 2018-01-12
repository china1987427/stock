package com.china.stock.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;


/**
 * @author Hongten
 * @created 2014-5-20
 */
public class ReadExcelUtil {
	public static void main(String[] args) {
		try {
			myExcel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void myExcel() throws Exception {

		// 读excel数据
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File("E:/stock.xlsx")));
		Sheet sheet = workbook.getSheetAt(0);// 读取第一个sheet
		List<Map<String, Object>> sourceList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-ddHH:mm:ss");
		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {// 逐行处理excel数据
			Row row = sheet.getRow(i);
			Map<String, Object> order = new HashMap<String, Object>();
			Cell cell0 = row.getCell(0);
			cell0.setCellType(Cell.CELL_TYPE_STRING); // 整数数据要转为txt，否则会变成浮点数
			Cell cell1 = row.getCell(1);
			cell1.setCellType(Cell.CELL_TYPE_STRING);
			Cell cell2 = row.getCell(2);
			cell2.setCellType(Cell.CELL_TYPE_STRING);
			order.put("ORDERID", cell0.toString());
			order.put("CLIENT", cell1.toString());
			order.put("SELLERID", cell2.toString());
			order.put("AMOUNT", row.getCell(3).toString()); // 处理日期类型的数据
			order.put("ORDERDATE", HSSFDateUtil.getJavaDate(row.getCell(4).getNumericCellValue()));
			sourceList.add(order);
		}
		for (int i = 0, len = sourceList.size(); i < len; i++) {// 按照条件过滤
			Map<String, Object> order = (Map<String, Object>) sourceList.get(i);
			System.out.println("1order.get(\"SELLERID\")=" + order.get("SELLERID"));
			if (Integer.parseInt(order.get("SELLERID").toString()) == 18
					&& ((Date) order.get("ORDERDATE")).after(format.parse("2009-12-31 23:59:59"))) {// 判断是否符合条件
				resultList.add(order); // 符合条件的加入List对象resultList
			}
		} // 写excel文件
		HSSFWorkbook workbook1 = new HSSFWorkbook();// 创建excel文件对象
		Sheet sheet1 = workbook1.createSheet();// 创建sheet对象
		Row row1;
		row1 = sheet1.createRow(0);// 第一行，标题
		row1.createCell(0).setCellValue("ORDERID");
		row1.createCell(1).setCellValue("CLIENT");
		row1.createCell(2).setCellValue("SELLERID");
		row1.createCell(3).setCellValue("AMOUNT");
		row1.createCell(4).setCellValue("ORDERDATE");
		for (int i = 1, len = resultList.size(); i < len; i++) {// 循环创建数据行
			row1 = sheet1.createRow(i);
			row1.createCell(0).setCellValue(resultList.get(i).get("ORDERID").toString());
			row1.createCell(1).setCellValue(resultList.get(i).get("CLIENT").toString());
			row1.createCell(2).setCellValue(resultList.get(i).get("SELLERID").toString());
			row1.createCell(3).setCellValue(resultList.get(i).get("AMOUNT").toString());
			row1.createCell(4).setCellValue(format.format((Date) resultList.get(i).get("ORDERDATE")));
		}
		FileOutputStream fos = new FileOutputStream("e:/file/orders_result.xls");
		workbook1.write(fos);// 写文件
		fos.close();
	}
}