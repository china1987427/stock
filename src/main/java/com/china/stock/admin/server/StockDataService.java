package com.china.stock.admin.server;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.china.stock.admin.dao.StockDataDao;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.util.StringUtil;

@Service(value = "/stockDataService")
public class StockDataService {

	@Autowired
	private StockDataDao stockDataDao;

	public void managerAllstock(final List<Map<String, Object>> list) {
		stockDataDao.managerAllstock(list);
	}

	public void managerAllstock(Map<String, Object> map) {
		stockDataDao.managerAllstock(map);
	}
	
	/**
	 * 在使用的从和讯网获取股票的代码和名称
	 * @param list
	 */
	public void saveAllStock(final List<Map<String, Object>> list) {
		stockDataDao.saveAllStock(list);
	}

	/**
	 * 更新同处于一个市场的股票的marketMark
	 * @param list
	 */
	public void updateStock(final List<Map<String, Object>> list) {
		stockDataDao.updateStock(list);
	}
	
	public Map<String, Object> checkStockEveryDayData(String code) {
		return stockDataDao.checkStockEveryDayData(code);
	}

	public List<Map<String, Object>> getSingleStock(String code, String date) {
		return stockDataDao.getSingleStock(code, date);
	}

	public List<Map<String, Object>> raisingLimitPredict(String code, String date) {
		return stockDataDao.raisingLimitPredict(code, date);
	}

	public void updateStockInfo(String code, String param) {
		stockDataDao.updateStockInfo(code, param);
	}

	public void changeStatus(String param, String isGet) {
		stockDataDao.changeStatus(param, isGet);
	}

	public int stockIntervalRisefall(String theDate, String week, JSONArray json1, JSONArray json2, JSONArray json3, JSONArray json4, JSONArray json5,
			JSONArray json6, JSONArray json7, JSONArray json8, JSONArray json9, JSONArray json10, JSONArray json11, JSONArray json12, JSONArray json13,
			JSONArray json14, JSONArray json15, JSONArray json16, JSONArray json17, JSONArray json18, JSONArray json19, JSONArray json20, JSONArray json21) {
		return stockDataDao.stockIntervalRisefall(theDate, week, json1, json2, json3, json4, json5, json6, json7, json8, json9, json10, json11, json12, json13,
				json14, json15, json16, json17, json18, json19, json20, json21);

	}

	public void saveLimitUpOrDown(String theDate, String week, JSONArray json1, JSONArray json21) {
		stockDataDao.saveLimitUpOrDown(theDate, week, json1, json21);
	}

	public Map<String, Object> getStatus(String param) {
		return stockDataDao.changeStatus(param);
	}

	public int saveChangeRate(String currencyCode, String currencyName, String newPrice, String changeAmount, String riseorfallRange, String opening) {
		return stockDataDao.saveChangeRate(currencyCode, currencyName, newPrice, changeAmount, riseorfallRange, opening);
	}

	public List<Map<String, Object>> getStockInfo(String code) {
		return stockDataDao.getStockInfo(code);
	}

	public int saveStockRiseorfallInfo(String code, String name, JSONArray limitupDate, int limitupTime, JSONArray limitdownDate, int limitdownTime) {
		return stockDataDao.saveStockRiseorfallInfo(code, name, limitupDate, limitupTime, limitdownDate, limitdownTime);
	}

	public Map<String, Object> getLimitUpDown() {
		return stockDataDao.getLimitUpDown();
	}

	public List<Map<String, Object>> count() {
		return stockDataDao.count();
	}

	public int updateCount(int ld, int lu, String date) {
		return stockDataDao.updateCount(ld, lu, date);
	}

	public void test() {
		stockDataDao.test();
	}

	public List<Map<String, Object>> weekOrMonthRiseOrFall(String code) {
		return stockDataDao.weekOrMonthRiseOrFall(code);
	}

	public List<Map<String, Object>> getMonthData(String code) {
		return stockDataDao.getMonthData(code);
	}

	public void saveWeekAndMonthData(String code, String name, JSONArray week, JSONArray month) {
		stockDataDao.saveWeekAndMonthData(code, name, week, month);
	}

	public void commonWeekAndMonth(List<Map<String, Object>> allstock) {
		try {
			if (!CollectionUtils.isEmpty(allstock)) {
				for (Map<String, Object> ss : allstock) {
					String code = ObjUtil.toString(ss.get("code"));
					String name = ObjUtil.toString(ss.get("name"));
					List<Map<String, Object>> stock = stockDataDao.weekOrMonthRiseOrFall(code);
					if (!CollectionUtils.isEmpty(stock)) {
						List<Integer> list = new ArrayList<Integer>();
						List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
						List<Object> list2 = new ArrayList<Object>();
						for (int i = stock.size() - 1; i >= 0; i--) {
							Map<String, Object> map = stock.get(i);
							int differ = ObjUtil.toInt(map.get("differ"));
							if (i != stock.size() - 1) {
								if (list.get(0) != differ) {
									list2.add(list1);
								}
							}
							if (!CollectionUtils.isEmpty(list)) {
								if (list.get(0) == differ) {
									list1.add(map);
								} else {
									list1 = new ArrayList<Map<String, Object>>();
									list1.add(map);
								}
								if (i == 0 && list.get(0) != differ) {
									list2.add(list1);
								}
							} else {
								list1.add(map);
							}
							list.clear();
							list.add(differ);
						}
						DecimalFormat df = new DecimalFormat("######0.00");
						if (!CollectionUtils.isEmpty(list2)) {
							JSONArray week = new JSONArray();
							int n = 10;
							if (list2.size() < 10) {
								n = list2.size() - 1;
							}
							if (list2.size() > 0) {
								for (int i = 0; i < n; i++) {
									List<Map<String, Object>> slist = (List<Map<String, Object>>) list2.get(i);
									Map<String, Object> minmap = slist.get(slist.size() - 1);
									float firstDayClosingIndex = ObjUtil.toFloat(minmap.get("closing_index"));
									String firstDay = ObjUtil.toString((minmap.get("date")));
									Map<String, Object> maxmap = slist.get(0);
									float lastDayClosingIndex = ObjUtil.toFloat(maxmap.get("closing_index"));
									String lastDay = ObjUtil.toString(maxmap.get("date"));
									float weekRiseOrFall = ObjUtil.toFloat(df.format(lastDayClosingIndex - firstDayClosingIndex));
									String per = df.format((weekRiseOrFall / firstDayClosingIndex) * 100);
									String interval = firstDay + "?" + lastDay;
									JSONObject json = new JSONObject();
									json.put(interval + "|" + firstDayClosingIndex + "|" + lastDayClosingIndex + "|" + per + "%", weekRiseOrFall);
									week.add(json);
								}
								List<Map<String, Object>> md = stockDataDao.getMonthData(code);
								JSONArray month = new JSONArray();
								for (Map<String, Object> map : md) {
									String min = ObjUtil.toString(map.get("min"));
									String max = ObjUtil.toString(map.get("max"));
									List<Map<String, Object>> minStock = stockDataDao.getSingleStock(code, min);
									List<Map<String, Object>> maxStock = stockDataDao.getSingleStock(code, max);
									float minClosingIndex = ObjUtil.toFloat(minStock.get(0).get("closing_index"));
									float maxClosingIndex = ObjUtil.toFloat(maxStock.get(0).get("closing_index"));
									String differ = df.format(maxClosingIndex - minClosingIndex);
									String per = df.format((ObjUtil.toFloat(differ) / minClosingIndex) * 100);
									JSONObject monthjson = new JSONObject();
									monthjson.put(min + "?" + max + "|" + minClosingIndex + "|" + maxClosingIndex + "|" + per + "%", differ);
									month.add(monthjson);
								}
								stockDataDao.saveWeekAndMonthData(code, name, week, month);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Log.error(e);
			e.printStackTrace();
		}
	}

	public void updateWeekAndMonthData(String stockCode, String closingIndex) {
		try {
			List<Map<String, Object>> weekAndMonthData = stockDataDao.getWeekAndMonthData(stockCode);
			String now = StringUtil.getStrDate("yyyy-MM-dd", new Date());
			String nowWeek = StringUtil.getWeek(now);
			Date nowDate = StringUtil.getDate("yyyy-MM-dd", now);
			Calendar cl = Calendar.getInstance();
			if (!CollectionUtils.isEmpty(weekAndMonthData) && !"星期六".equals(nowWeek) && !"星期日".equals(nowWeek)) {
				Map<String, Object> wamd = weekAndMonthData.get(0);
				DecimalFormat df = new DecimalFormat("######0.00");
				for (int i = 0; i < 2; i++) {
					String field1 = "";
					String field2 = "";
					String param = "";
					if (i == 0) {
						field1 = "recent_week_riseorfall_data";
						field2 = "week_riseorfall_data";
						param = "week";
					} else {
						field1 = "recent_month_riseorfall_data";
						field2 = "month_riseorfall_data";
						param = "month";
					}
					JSONObject jsonobject = JSONObject.parseObject(wamd.get(field1) + "");
					JSONObject obj = jsonobject;
					JSONArray jsonarray = JSONArray.parseArray((wamd.get(field2) + ""));
					Set<Entry<String, Object>> set = jsonobject.entrySet();
					Iterator<Entry<String, Object>> it = set.iterator();
					while (it.hasNext()) {
						Entry<String, Object> str = it.next();
						String key = str.getKey();
						// Float value = ObjUtil.toFloat(str.getValue());
						String[] s = key.split("\\|");
						float f = ObjUtil.toFloat(closingIndex) - ObjUtil.toFloat(s[1]);
						float p = (f / ObjUtil.toFloat(s[1])) * 100;
						String per = df.format(p);
						String differ = df.format(f);
						JSONArray array = new JSONArray();
						if (key.contains("?")) {
							String firstDay = s[0].split("\\?")[0];
							String lastDay = s[0].split("\\?")[1];
							String week = StringUtil.getWeek(lastDay);
							Date lastDate = StringUtil.getDate("yyyy-MM-dd", lastDay);
							if (nowDate.getTime() > lastDate.getTime()) {
								boolean a = false;
								boolean b = false;
								if (i == 0) {
									cl.setTime(nowDate);
									int nowWeekNum = cl.get(Calendar.WEEK_OF_YEAR);
									cl.setTime(lastDate);
									int lastWeekNum = cl.get(Calendar.WEEK_OF_YEAR);
									a = nowWeekNum == lastWeekNum;
									b = nowWeekNum > lastWeekNum;
								} else if (i == 1) {
									int n = ObjUtil.toInt(now.replace("-", "").substring(0, 6));
									int l = ObjUtil.toInt(lastDay.replace("-", "").substring(0, 6));
									a = n == l;
									b = n > l;
								}
								if (!"星期一".equals(nowWeek) || i == 1) {
									if (a) {
										jsonobject.clear();
										jsonobject.put(firstDay + "?" + now + "|" + s[1] + "|" + closingIndex + "|" + per + "%", differ);
										array.add(jsonobject);
										stockDataDao.updateWeekAndMonthData(stockCode, array, param + "-1");
									} else if (b) {
										jsonarray.add(0, obj);
										stockDataDao.updateWeekAndMonthData(stockCode, jsonarray, param + "-2");
										jsonobject.clear();
										jsonobject.put(now + "|" + closingIndex, closingIndex);
										array.add(jsonobject);
										stockDataDao.updateWeekAndMonthData(stockCode, array, param + "-1");
									}
								} else if (i == 0) {
									jsonarray.add(0, jsonobject);
									stockDataDao.updateWeekAndMonthData(stockCode, jsonarray, param + "-2");
									jsonobject.clear();
									jsonobject.put(now + "|" + closingIndex, closingIndex);
									array.add(jsonobject);
									stockDataDao.updateWeekAndMonthData(stockCode, array, param + "-1");
								}
							}

						} else {
							String firstDay = s[0];
							jsonobject.clear();
							jsonobject.put(firstDay + "?" + now + "|" + s[1] + "|" + closingIndex + "|" + per + "%", differ);
							array.add(jsonobject);
							stockDataDao.updateWeekAndMonthData(stockCode, array, param + "-1");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Map<String, Object>> serach(String stockMarket, String serachValue) {
		return stockDataDao.serach(stockMarket, serachValue);
	}

	public List<Map<String, Object>> getUpOrDownTime(String code) {
		return stockDataDao.getUpOrDownTime(code);
	}

	public JSONArray getStockToApp(String stockCode, String date) {
		List<Map<String, Object>> list = stockDataDao.getStockToApp(stockCode, date);
		JSONArray array = new JSONArray();
		array.addAll(list);
		return array;
	}
	// ............................................................................................

}
