package com.china.stock.common.data.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.china.stock.common.entity.Stocks;
import com.china.stock.common.tool.conf.Conf;

public class ExcelUtil {
	/** 总行数 */
	private int totalRows = 0;
	/** 总列数 */
	private int totalCells = 0;
	/** 错误信息 */
	private String errorInfo;

	public int getTotalRows() {
		return totalRows;
	}

	public int getTotalCells() {
		return totalCells;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	/**
	 * 
	 * @描述：验证excel文件
	 * @参数：@param filePath　文件完整路径
	 */

	public boolean validateExcel(String filePath) {
		/** 检查文件名是否为空或者是否是Excel格式的文件 */
		String excel = filePath.split("//")[2];
		if (excel == null || !(isExcel2003(excel) || isExcel2007(excel))) {
			errorInfo = "文件名不是excel格式";
			return false;
		}
		/** 检查文件是否存在 */
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			errorInfo = "文件不存在";
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @描述：根据文件名读取excel文件
	 * @参数：@param filePath 文件完整路径
	 */

	public List<List<Stocks>> readExcel(String market) {
		List<List<Stocks>> dataLst = new ArrayList<List<Stocks>>();
		String stockPath = "";
		String path = "";
		if ("hz".equals(market)) {
			path = "hzStockPath";
		} else if ("sz".equals(market)) {
			path = "szStockPath";
		}
		try {
			stockPath = Conf.getValue(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		InputStream is = null;
		BufferedReader bReader = null;
		InputStreamReader iReader = null;
		try {
			/** 验证文件是否合法 */
			// if (!validateExcel(stockPath)) {
			// System.out.println(errorInfo);
			// return null;
			// }
			/** 判断文件的类型，是2003还是2007 */
			boolean isExcel2003 = true;
			// if (isExcel2007(stockPath)) {
			// isExcel2003 = false;
			// }
			String str = stockPath.split("\\.")[1] == null ? null : stockPath.split("\\.")[1].trim();
			if ("xls".equals(str)) {
				isExcel2003 = true;
			} else if ("xlsx".equals(str)) {
				isExcel2003 = false;
			} else {
				return null;
			}
			/** 调用本类提供的根据流读取的方法 */
			File file = new File(stockPath);
			is = new FileInputStream(file);
			market = "sz";
			if ("hz".equals(market)) {
				iReader = new InputStreamReader(is, "gbk");
				bReader = new BufferedReader(iReader);
				String string = bReader.readLine();
				String str1 = "";
				Pattern pCells = Pattern.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
				while ((string = bReader.readLine()) != null) {
					if (!string.endsWith(",")) {
						string = string + ",";
					}
					Matcher mCells = pCells.matcher(string);
					List<String> listTemp = new ArrayList<String>();
					// 读取每个单元格
					while (mCells.find()) {
						str1 = mCells.group();
						str1 = str1.replaceAll("(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
						str1 = str1.replaceAll("(?sm)(\"(\"))", "$2");
						listTemp.add(str1);
					}
					new ArrayList().add((String[]) listTemp.toArray(new String[listTemp.size()]));
				}
			} else if ("sz".equals(market)) {
				/** 根据版本选择创建Workbook的方式 */
				Workbook wb = null;
				if (isExcel2003) {
					wb = new HSSFWorkbook(is);
				} else {
					wb = new XSSFWorkbook(is);
				}
				/** 得到第一个shell */
				Sheet sheet = wb.getSheetAt(0);
				/** 得到Excel的行数 */
				this.totalRows = sheet.getPhysicalNumberOfRows();
				/** 得到Excel的列数 */
				if (this.totalRows >= 1 && sheet.getRow(0) != null) {
					this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				/** 循环Excel的行 */
				for (int r = 1; r < this.totalRows; r++) {
					Row row = sheet.getRow(r);
					if (row == null) {
						continue;
					}
					Stocks stock = new Stocks();
					boolean isHz = "hz".equals(market) ? true : false;
					String stockCode = row.getCell(isHz ? 2 : 5).getStringCellValue();
					String stockName = row.getCell(isHz ? 3 : 6).getStringCellValue();
					String marketingDate = row.getCell(isHz ? 4 : 7).getStringCellValue();
					int totalShares = row.getCell(isHz ? 5 : 8).getColumnIndex();
					int tradableShare = row.getCell(isHz ? 6 : 9).getColumnIndex();
					stock.setStockCode(stockCode);
					stock.setStockName(stockName);
					stock.setMarketingDate(sdf.parse(marketingDate));
					stock.setTotalShares(totalShares);
					stock.setTradableShare(tradableShare);
					if ("sz".equals(market)) {
						String province = row.getCell(16).getStringCellValue();
						String city = row.getCell(17).getStringCellValue();
						String toBelong = row.getCell(18).getStringCellValue();
						stock.setProvince(province);
						stock.setCity(city);
						stock.setToBelong(toBelong);
					}
					List<Stocks> rowLst = new ArrayList<Stocks>();
					rowLst.add(stock);
					/** 保存第r行的第c列 */
					dataLst.add(rowLst);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (is != null||null != bReader) {
				try {
					is.close();
					bReader.close();
					iReader.close();
				} catch (IOException e) {
					is = null;
					e.printStackTrace();
				}
			}
		}
		/** 返回最后读取的结果 */
		return dataLst;
	}

	public boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	public boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}
}
