package com.china.stock.user.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.china.stock.user.dao.ReadExcelDao;


@Service(value = "/readExcelService")
public class ReadExcelService {
	@Autowired
	private ReadExcelDao readExcelDao;
	
	public int saveData(List list,String stockCode,String stockName) {
		return readExcelDao.saveData(list,stockCode,stockName);
	}
	public int saveToStock(String stockCode,String stockName,String peRatio,String region,String industry,String mainBusiness) {
		return readExcelDao.saveToStock(stockCode, stockName,peRatio,region,industry,mainBusiness);
	}
}
