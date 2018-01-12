package com.china.stock.admin.controller;


import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.china.stock.admin.server.DapanDataService;
import com.china.stock.admin.server.StockAndDapanHistoryDataService;
import com.china.stock.admin.server.StockDataService;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.tool.controller.BaseController;


@Controller
@RequestMapping(value = "/sdhistory")
public class StockAndDapanHistoryDataController extends BaseController{

	private static Logger log = Logger.getLogger(StockAndDapanHistoryDataController.class);
	@Autowired
	private StockAndDapanHistoryDataService history;
	@Autowired
	private DapanDataService dapanDataService;
	@Autowired
	private StockDataService stockDataService;

	/**
	 * 获取个股股票的历史数据
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getHistoryData")
	public String getHistoryData(Model model) {
		try {
			/*
			 * String ss = RedisUtil.get("shanghaiStock"); List<Map<String,
			 * Object>> shanghaiStock = new ArrayList<Map<String, Object>>(); if
			 * (!StringUtils.isEmpty(ss)) { shanghaiStock = (List<Map<String,
			 * Object>>) ((Object) ss); }
			 */
			List<Map<String, Object>> shanghaiStock = null;
			if (CollectionUtils.isEmpty(shanghaiStock)) {
				shanghaiStock = dapanDataService.getAllStocks("3");// 上海股票
				/*
				 * if (!CollectionUtils.isEmpty(shanghaiStock)) {
				 * RedisUtil.set("riseStock", shanghaiStock.toString(), 30 * 24
				 * * 60 * 60 * 1000); }
				 */
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
			/*
			 * String sz = RedisUtil.get("shenzhenStock"); List<Map<String,
			 * Object>> shenzhenStock = new ArrayList<Map<String, Object>>(); if
			 * (!StringUtils.isEmpty(sz)) { shenzhenStock = (List<Map<String,
			 * Object>>) ((Object) sz); }
			 */
			List<Map<String, Object>> shenzhenStock = null;
			if (CollectionUtils.isEmpty(shenzhenStock)) {
				shenzhenStock = dapanDataService.getAllStocks("4");// 深圳股票
				/*
				 * if (!CollectionUtils.isEmpty(shenzhenStock)) {
				 * RedisUtil.set("fallStock", shenzhenStock.toString()); } else
				 * { RedisUtil.set("fallStock", null); }
				 */
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
			WriterJson("true");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return null;
		
	}
}
