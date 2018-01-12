package com.china.stock.admin.server;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.china.stock.admin.dao.AdminDao;

@Service(value = "/adminService")
public class AdminService {
	@Autowired
	private AdminDao adminDao;

	public Map<String, Object> checkStockInfo(String code) {
		return adminDao.checkStockInfo(code);
	}

	public Map<String, Object> getstock(String stockcode, Integer curPage, Integer pageSize, String marketMark) {
		return adminDao.getstock(stockcode, curPage, pageSize, marketMark);
	}
	
	public List<Map<String, Object>> getAllStocks() {
		return adminDao.getAllStocks();
	}
	
}
