package com.china.stock.user.server;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.china.stock.admin.dao.DapanDataDao;
import com.china.stock.admin.dao.UserDao;
import com.china.stock.admin.server.DapanDataService;
import com.china.stock.admin.server.StockAndDapanHistoryDataService;
import com.china.stock.common.data.StockHistoryDataConnection;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.util.TimeUtil;
import com.china.stock.user.dao.StockDao;


@Service(value = "/stockService")
public class StockService {

	@Autowired
	private StockDao stockDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private DapanDataService dapanDataService;
	@Autowired
	private DapanDataDao dapanDataDao;
	@Autowired
	private StockAndDapanHistoryDataService history;

	public List<Map<String, Object>> getAverageLineData(Date date, String stockCode, int n) {
		return stockDao.getAverageLineData(date, stockCode, n);
	}

	public List<Object> getAverageLineList(String date, String stockCode, int n, String mark) {
		List<Object> aList = new ArrayList<Object>();
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if ("dapan".equals(stockCode)) {
				list = stockDao.getDapanData(date, "order by date desc limit 0,180");
			} else {
				list = stockDao.getStockEveryDayData("order by date desc limit 0,180", "", stockCode);
			}
			List<Object> list1 = new ArrayList<Object>();
			List<Object> list2 = new ArrayList<Object>();
			List<Object> list3 = new ArrayList<Object>();
			List<Object> list4 = new ArrayList<Object>();
			for (int m = 0; m < list.size(); m++) {
				Map<String, Object> map = list.get(m);
				list1.add(ObjUtil.toFloat(map.get("closing_index")));
				list2.add(ObjUtil.toString(map.get("date")));
			}
			float averageLine = 0;
			for (int a = 0; a < list1.size() - n; a++) {
				List<Object> subList = list1.subList(a, a + n);
				float sum = 0;
				for (int i = 0; i < subList.size(); i++) {
					float j = (Float) subList.get(i);
					sum += j;
				}
				DecimalFormat df = new DecimalFormat("######0.0000");
				averageLine = sum / n;
				list3.add(df.format(averageLine));
				list4.add(ObjUtil.toString(list2.get(a)));
			}
			aList.add(list3);
			aList.add(list4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aList;
	}

	public Map<String, Object> calAverLine(String date, String stockCode, int page, int pageSize) {
		double averageLine = 0;
		Map<String, Object> map = null;
		try {
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<Object> ciList = new ArrayList<Object>();
			List<Object> mindate = new ArrayList<Object>();
			map = new HashMap<String, Object>();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if ("dapan".equals(stockCode)) {
				list = stockDao.getDapanData(date, "order by date desc limit " + page + "," + pageSize);
			} else {
				list = stockDao.getStockEveryDayData("order by date desc limit " + page + "," + pageSize, date, stockCode);
			}
			if (!CollectionUtils.isEmpty(list)) {
				for (Map<String, Object> stock : list) {
					double closingIndex = ObjUtil.toDouble(stock.get("closing_index"));
					String opendate = ObjUtil.toString(stock.get("date"));
					ciList.add(closingIndex);
					mindate.add(opendate);
					if (mindate.size() > 0) {
						if (TimeUtil.compareDate(ObjUtil.toString(mindate.get(0)), opendate)) {
							mindate.clear();
							mindate.add(opendate);
						}
					}
				}
				double sum = 0;
				for (int m = 0; m < ciList.size(); m++) {
					double j = (double) ciList.get(m);
					sum += j;
				}
				DecimalFormat df = new DecimalFormat("######0.0000");
				averageLine = sum / pageSize;
				map.put(ObjUtil.toString(mindate.get(0)), df.format(averageLine));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public void saveHistoryData(String historyStock, String stockCode, String stockname) {
		try {
			JSONArray hs = JSONObject.parseArray(historyStock);
			if (!StringUtils.isEmpty(stockname) && !"dapan".equals(stockname)) {
				stockname = stockname.substring(0, stockname.indexOf("("));
			}
			for (Object object : hs) {
				JSONArray jsona = JSON.parseObject(ObjUtil.toString(object)).getJSONArray("hq");
				for (Object obj : jsona) {
					@SuppressWarnings("unchecked")
					List<String> list = (List<String>) obj;
					String date = ObjUtil.toString(list.get(0));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat format = new SimpleDateFormat("EEEE");
					String weekOfCurrent = format.format(sdf.parse(date));
					String openingIndex = ObjUtil.toString(list.get(1));
					String closingIndex = ObjUtil.toString(list.get(2));
					String amplitude = ObjUtil.toString(list.get(3));
					String riseOrFall = ObjUtil.toString(list.get(4));
					String lowestIndex = ObjUtil.toString(list.get(5));
					String highestIndex = ObjUtil.toString(list.get(6));
					String totalHand = ObjUtil.toString(list.get(7));
					String sumMoney = ObjUtil.toString(list.get(8));
					String handover = ObjUtil.toString(list.get(9));
					if ("dapan".equals(stockCode)) {
						DecimalFormat df = new DecimalFormat("######0.00");
						float a = ObjUtil.toFloat(amplitude) / ObjUtil.toFloat(closingIndex);
						String increase = df.format(a);
						stockDao.saveToStockIndex(date, weekOfCurrent, openingIndex, highestIndex, lowestIndex, closingIndex, riseOrFall, increase,
								amplitude, totalHand, sumMoney);
					} else {
						stockDao.saveHistoryData(stockCode, stockname, date, weekOfCurrent, openingIndex, closingIndex, amplitude, riseOrFall,
								lowestIndex, highestIndex, totalHand, sumMoney, handover);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int deleteStock(String stockCode) {
		return stockDao.deleteStock(stockCode);
	}

	public List<Map<String, Object>> serach(String stockMarket, String serachValue) {
		return stockDao.serach(stockMarket, serachValue);
	}

	public List<Map<String, Object>> getDaPanData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Map<String, Object> map = stockDao.getFuncDapan();
			String max = ObjUtil.toString(map.get("max"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String nowDate = sdf.format(new Date());
			Date date1 = sdf.parse(nowDate);
			Date date2 = sdf.parse(max);
			if (date1.getTime() > date2.getTime()) {
				history.getHistoryData("dapan", map);
			}
			list = dapanDataDao.getDapanData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int saveData(String date, String nowDate) {
		int r = 0;
		try {
			String historyStock = StockHistoryDataConnection.getStockHistoryData("dapan", date, nowDate);
			JSONArray hs = JSONObject.parseArray(historyStock);
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format = new SimpleDateFormat("EEEE");
			for (Object object : hs) {
				JSONArray jsona = JSON.parseObject(ObjUtil.toString(object)).getJSONArray("hq");
				for (Object obj : jsona) {
					@SuppressWarnings("unchecked")
					List<String> list = (List<String>) obj;
					String dapanDate = ObjUtil.toString(list.get(0));
					String week = format.format(sdfDate.parse(dapanDate));
					String openingIndex = ObjUtil.toString(list.get(1));
					String closingIndex = ObjUtil.toString(list.get(2));
					String riseOrFall = ObjUtil.toString(list.get(3));
					String increase = ObjUtil.toString(list.get(4));
					String lowestIndex = ObjUtil.toString(list.get(5));
					String highestIndex = ObjUtil.toString(list.get(6));
					String totalHand = ObjUtil.toString(list.get(7));
					String sumMoney = ObjUtil.toString(list.get(8));
					double amplitude = (ObjUtil.toDouble(highestIndex) - ObjUtil.toDouble(lowestIndex)) / ObjUtil.toDouble(closingIndex);
					DecimalFormat df = new DecimalFormat("######0.0000");
					r = stockDao.saveToStockIndex(dapanDate, week, openingIndex, highestIndex, lowestIndex, closingIndex, riseOrFall, increase,
							df.format(amplitude), totalHand, sumMoney);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	public List<Map<String, Object>> getDapanData(String date) {
		return stockDao.getDapanData(date);
	}

	public List<Map<String, Object>> getDapanData(String date, String sqlParam) {
		return stockDao.getDapanData(date, sqlParam);
	}

	public Map<String, Object> getFuncDapan() {
		return stockDao.getFuncDapan();
	}

	public List<Map<String, Object>> getStockData(String stockCode) {
		return stockDao.getStockData(stockCode);
	}

	public List<Map<String, Object>> getStockEveryDayData(String sqlParam, String date, String stockCode) {
		return stockDao.getStockEveryDayData(sqlParam, date, stockCode);
	}

	public Map<String, Object> getFuncStockData(String stockCode) {
		return stockDao.getFuncStockData(stockCode);
	}

	public String tomystocks(String mark, String stockCode, String username, String stockName) {
		Map<String, Object> map = userDao.verify(username, "username");
		String userId = map.get("user_id") + "";
		int result = stockDao.saveToMyStock(mark, stockCode, userId, username, stockName);
		if (result > 0) {
			return "true";
		} else {
			return "false";
		}
	}

	public List<Map<String, Object>> getDataFromMyStock(String stockCode, String username) {
		return stockDao.getDataFromMyStock(stockCode, username);
	}

	public List<Map<String, Object>> getFuncDataFromMyStock(String stockCode) {
		return stockDao.getFuncDataFromMyStock(stockCode);
	}
}
