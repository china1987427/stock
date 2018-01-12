package com.china.stock.common.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.china.stock.admin.server.DapanDataService;
import com.china.stock.admin.server.StockAndDapanHistoryDataService;
import com.china.stock.admin.server.StockDataService;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.tool.base.RedisUtil;
import com.china.stock.common.tool.entity.MyException;


@Service(value = "/stockJobService")
public class StockJobService {
	private static Logger log = Logger.getLogger(StockJobService.class);
	@Autowired
	private StockAndDapanHistoryDataService history;
	@Autowired
	private StockDataService stockDataService;
	@Autowired
	private DapanDataService dapanDataService;
	@Autowired
	private StockJobDao stockJobDao;
	

	public void getHistoryData() {
		try {
			String ss = RedisUtil.get("shanghaiStock");
			List<Map<String, Object>> shanghaiStock = new ArrayList<Map<String, Object>>();
			if (!StringUtils.isEmpty(ss)) {
				shanghaiStock = (List<Map<String, Object>>) ((Object) ss);
			}
			if (CollectionUtils.isEmpty(shanghaiStock)) {
				shanghaiStock = dapanDataService.getAllStocks("1");// 上海股票
				if (!CollectionUtils.isEmpty(shanghaiStock)) {
					RedisUtil.set("riseStock", shanghaiStock.toString(), 30 * 24 * 60 * 60 * 1000);
				}
			}
			if (!CollectionUtils.isEmpty(shanghaiStock)) {
				for (Map<String, Object> map : shanghaiStock) {
					String code = ObjUtil.toString(map.get("code"));
					String name = ObjUtil.toString(map.get("name"));
					Map<String, Object> cs = stockDataService.checkStockEveryDayData(code);
					cs.put("stockCode", code);
					cs.put("stockName", name);
					cs.put("marketMark", map.get("market_mark"));
					history.getHistoryData(code, cs);
				}
			}
			String sz = RedisUtil.get("shenzhenStock");
			List<Map<String, Object>> shenzhenStock = new ArrayList<Map<String, Object>>();
			if (!StringUtils.isEmpty(sz)) {
				shenzhenStock = (List<Map<String, Object>>) ((Object) sz);
			}
			if (CollectionUtils.isEmpty(shenzhenStock)) {
				shenzhenStock = dapanDataService.getAllStocks("2");// 深圳股票
				if (!CollectionUtils.isEmpty(shenzhenStock)) {
					RedisUtil.set("fallStock", shenzhenStock.toString());
				} else {
					RedisUtil.set("fallStock", null);
				}
			}
			if (!CollectionUtils.isEmpty(shenzhenStock)) {
				for (Map<String, Object> map : shenzhenStock) {
					String code = ObjUtil.toString(map.get("code"));
					String name = ObjUtil.toString(map.get("name"));
					Map<String, Object> cs = stockDataService.checkStockEveryDayData(code);
					cs.put("stockCode", code);
					cs.put("stockName", name);
					cs.put("marketMark", map.get("market_mark"));
					history.getHistoryData(code, cs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	public boolean isGetRMBExchangeRate(String today) {
		List<Map<String, Object>> list = stockJobDao.isGetRMBExchangeRate(today);
		boolean isGet = false;
		if (!CollectionUtils.isEmpty(list)) {
			Map<String, Object> map = list.get(0);
			try {
				int count = ObjUtil.toInt(map.get("count"));
				if (count > 0) {
					isGet = true;
				}
			} catch (MyException e) {
				e.printStackTrace();
			}
		}
		return isGet;
	}
}
