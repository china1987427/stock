package com.china.stock.admin.server;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.china.stock.admin.dao.DapanDataDao;
import com.china.stock.admin.entity.MarketIndexEveryday;

@Service(value = "/dapanDataService")
public class DapanDataService {

	@Autowired
	private DapanDataDao dapanDataDao;
	@Autowired
	private StockAndDapanHistoryDataService history;

	public List<Map<String, Object>> getDapan(String date) {
		return dapanDataDao.getDapan(date);
	}

	public List<Map<String, Object>> getAllStocks(String marketMark) {
		return dapanDataDao.getAllStocks(marketMark);
	}

	public int checkRiseOrFall(String number) {
		return dapanDataDao.checkRiseOrFall(number);
	}

	public List<Map<String, Object>> getRiseOrFallData(String number) {
		return dapanDataDao.getRiseOrFallData(number);
	}

	public List<Map<String, Object>> getDateRiseOrFall(String number) {
		return dapanDataDao.getDateRiseOrFall(number);
	}

	public List<Map<String, Object>> getMaxOrMinDate(String param) {
		return dapanDataDao.getMaxOrMinDate(param);
	}

	public void verifydapanData() {
		Map<String, Object> map = dapanDataDao.verifyDapan();
		String param = "dapan";
		history.getHistoryData(param, map);
	}
	public void saveToDaPanDataEveryday(List<MarketIndexEveryday> marketIndex) {
		dapanDataDao.saveToDaPanDataEveryday(marketIndex);
	}
}
