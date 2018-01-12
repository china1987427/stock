package com.china.stock.admin.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.china.stock.admin.dao.StockDataDao;
import com.china.stock.admin.server.AdminService;
import com.china.stock.admin.server.DapanDataService;
import com.china.stock.admin.server.StockDataService;
import com.china.stock.common.database.util.HibernateStockDaoImpl;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.tool.controller.BaseController;
import com.china.stock.common.tool.entity.MyException;
import com.china.stock.common.util.StringUtil;
import com.china.stock.user.server.StockService;


@Controller
@RequestMapping(value = "/stockdata")
public class StockDataController extends BaseController {
	private static Logger log = Logger.getLogger(StockDataController.class);
	@Autowired
	private StockDataService stockDataService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private DapanDataService dapanDataService;
	
	@Autowired
	private HibernateStockDaoImpl stockDaoImpl;
	@Autowired
	private StockService stockService;
	@Autowired
	private StockDataDao stockDataDao;
	
	/**
	 * 全部股票数据
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/stockEveryDayData", method = RequestMethod.POST)
	public String stockEveryDayData(Model model) {
		getRequest().getSession().setAttribute("position", "stock");
		return "jsp/admin/stock/data/everyday_stockdata";
	}

	/**
	 * 个股数据
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/everyStock", method = RequestMethod.POST)
	public String everyStock(Model model) {
		try {
			String stockCode = getRequest().getParameter("stockCode");
			String stockName = getRequest().getParameter("stockName");
			model.addAttribute("stockCode", stockCode);
			model.addAttribute("stockName", stockName);
			String uri = getRequest().getRequestURI();
			model.addAttribute("uri", uri);
			List<Map<String, Object>> stocks = stockDataService.getSingleStock(stockCode, "");
			model.addAttribute("stocks", stocks);
			Map<String, Object> map = stockDataService.checkStockEveryDayData(stockCode);
			String maxdate = ObjUtil.toString(map.get("max"));
			List<Map<String, Object>> maxdateStock = stockDataService.getSingleStock(stockCode, maxdate);
			if (!CollectionUtils.isEmpty(maxdateStock)) {
				model.addAttribute("maxdateStock", maxdateStock.get(0));
			}
			List<Map<String, Object>> upAndDownStocks = stockDataService.getUpOrDownTime(stockCode);
			if (!CollectionUtils.isEmpty(upAndDownStocks)) {
				Map<String, Object> udmap = upAndDownStocks.get(0);
				JSONArray lujson = JSON.parseArray(ObjUtil.toString(udmap.get("limitup_date")));
				model.addAttribute("limitupDate", lujson);
				int limitupTime = ObjUtil.toInt(udmap.get("limitup_time"));
				model.addAttribute("limitupTime", limitupTime);
				JSONArray ldjson = JSON.parseArray(ObjUtil.toString(udmap.get("limitdown_date")));
				model.addAttribute("limitdownDate", ldjson);
				int limitdownTime = ObjUtil.toInt(udmap.get("limitdown_time"));
				model.addAttribute("limitdownTime", limitdownTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/admin/stock/data/everyStock";
	}

	/**
	 * 涨跌分布图
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/riseOrFallMap", method = RequestMethod.POST)
	public String riseOrFallMap(Model model) {
		try {
			String date = StringUtil.getStrDate("yyyy-MM-dd", new Date());
			String stockCode = getRequest().getParameter("stockCode");
			String uri = getRequest().getRequestURI();
			model.addAttribute("uri", uri);
			String nowDate = date;
			List<Map<String, Object>> dapan = new ArrayList<Map<String, Object>>();
			int n = 0;
			do {
				n++;
				if (n > 1) {
					nowDate = StringUtil.getStrByLT("yyyy-MM-dd", nowDate, -1);
				}
				dapan = dapanDataService.getDapan(nowDate);
			} while (CollectionUtils.isEmpty(dapan));

			if (!CollectionUtils.isEmpty(dapan)) {
				model.addAttribute("dapanData", dapan.get(0));
			}
			List<Map<String, Object>> stocks = new ArrayList<Map<String, Object>>();
			int i = 0;
			do {
				i++;
				if (i > 1) {
					date = StringUtil.getStrByLT("yyyy-MM-dd", date, -1);
				}
				stocks = stockDataService.getSingleStock("", date);
			} while (CollectionUtils.isEmpty(stocks));
			model.addAttribute("date", date);
			model.addAttribute("stocks", stocks);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/admin/stock/data/riseOrFallMap";
	}

	/**
	 * 涨停板预测
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/raisingLimitPredict", method = RequestMethod.POST)
	public String raisingLimitPredict(Model model) {
		try {
			String stockCode = getRequest().getParameter("stockCode");
			stockDataService.raisingLimitPredict("", "");
			String uri = getRequest().getRequestURI();
			model.addAttribute("uri", uri);
			// stockDataService.getSingleStock("", date);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/admin/stock/data/risingLimitPredict";
	}

	/**
	 * 数据分析
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/stockAnalysis", method = RequestMethod.POST)
	public String stockAnalysis(Model model) {
		try {
			String stockCode = getRequest().getParameter("stockCode");
			// stockDataService.getSingleStock("", date);
			String uri = getRequest().getRequestURI();
			model.addAttribute("uri", uri);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/admin/stock/data/stockAnalysis";
	}

	/**
	 * k线图 股票k线图
	 * 
	 * @param model
	 * @param stockCode
	 * @return
	 */
	@RequestMapping(value = "/toKline", method = RequestMethod.POST)
	public String toKline(Model model, @RequestParam(value = "stockCode") String stockCode) {
		try {
			String stockName = getRequest().getParameter("stockName");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int year = c.get(Calendar.YEAR);
			model.addAttribute("year", year);
			model.addAttribute("stockName", stockName);
			model.addAttribute("stockCode", stockCode);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/common/kline";
	}

	/**
	 * 泸指k线图
	 * 
	 * @param model
	 * @param stockCode
	 * @return
	 */
	@RequestMapping(value = "/toMarketKline", method = RequestMethod.POST)
	public String toMarketKline(Model model) {
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int year = c.get(Calendar.YEAR);
			model.addAttribute("year", year);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/common/marketKline";
	}

	/**
	 * 更新数据
	 * 
	 * @param model
	 * @param stockCode
	 * @return
	 */
	@RequestMapping(value = "/updateData", method = RequestMethod.POST)
	public String updateData(Model model) {
		return "jsp/admin/stock/data/updateData";
	}

	/**
	 * 测试k线图
	 * 
	 * @param model
	 * @param stockCode
	 * @return
	 */
	@RequestMapping(value = "/testKline", method = RequestMethod.POST)
	public String testKline(Model model, @RequestParam(value = "stockCode") String stockCode) {
		try {
			String stockName = getRequest().getParameter("stockname");
			model.addAttribute("stockName", stockName);
			model.addAttribute("stockCode", stockCode);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/test/canvas3";
	}

	/**
	 * -10-10各区间的数据
	 */
	@ResponseBody
	@RequestMapping(value = "/stockIntervalRisefall", method = RequestMethod.POST)
	public void stockIntervalRisefall() {
		try {
			List<Map<String, Object>> stocks = new ArrayList<Map<String, Object>>();
			String now = StringUtil.getStrDate("yyyy-MM-dd", new Date());
			Date nowDate = StringUtil.getDate("yyyy-MM-dd", now);
			Date thisDate = null;
			int n = 1;
			Map<String, Object> mapdate = stockDataService.getLimitUpDown();
			int count = ObjUtil.toInt(mapdate.get("count"));
			String theDate = "";
			if (count == 0) {
				mapdate = stockDataService.checkStockEveryDayData("");
				theDate = ObjUtil.toString(mapdate.get("min"));
				theDate = StringUtil.getStrByLT("yyyy-MM-dd", theDate, -1);
			} else {
				theDate = ObjUtil.toString(mapdate.get("max"));
			}
			do {
				theDate = StringUtil.getStrByLT("yyyy-MM-dd", theDate, n);
				JSONArray json1 = new JSONArray();
				JSONArray json2 = new JSONArray();
				JSONArray json3 = new JSONArray();
				JSONArray json4 = new JSONArray();
				JSONArray json5 = new JSONArray();
				JSONArray json6 = new JSONArray();
				JSONArray json7 = new JSONArray();
				JSONArray json8 = new JSONArray();
				JSONArray json9 = new JSONArray();
				JSONArray json10 = new JSONArray();
				JSONArray json11 = new JSONArray();
				JSONArray json12 = new JSONArray();
				JSONArray json13 = new JSONArray();
				JSONArray json14 = new JSONArray();
				JSONArray json15 = new JSONArray();
				JSONArray json16 = new JSONArray();
				JSONArray json17 = new JSONArray();
				JSONArray json18 = new JSONArray();
				JSONArray json19 = new JSONArray();
				JSONArray json20 = new JSONArray();
				JSONArray json21 = new JSONArray();
				thisDate = StringUtil.getDate("yyyy-MM-dd", theDate);
				String week = StringUtil.getWeek(theDate);
				stocks = stockDataService.getSingleStock("", theDate);
				if (!CollectionUtils.isEmpty(stocks)) {
					for (Map<String, Object> map : stocks) {
						double amplitude = ObjUtil.toDouble(map.get("amplitude"));
						String stock_code = ObjUtil.toString(map.get("stock_code")).trim();
						if (-10 < amplitude && amplitude < -9) {
							json1.add(stock_code);
						} else if (-9 <= amplitude && amplitude < -8) {
							json2.add(stock_code);
						} else if (-8 <= amplitude && amplitude < -7) {
							json3.add(stock_code);
						} else if (-7 <= amplitude && amplitude < -6) {
							json5.add(stock_code);
						} else if (-6 <= amplitude && amplitude < -5) {
							json5.add(stock_code);
						} else if (-5 <= amplitude && amplitude < -4) {
							json6.add(stock_code);
						} else if (-4 <= amplitude && amplitude < -3) {
							json7.add(stock_code);
						} else if (-3 <= amplitude && amplitude < -2) {
							json8.add(stock_code);
						} else if (-2 <= amplitude && amplitude < -1) {
							json9.add(stock_code);
						} else if (-1 <= amplitude && amplitude < 0) {
							json10.add(stock_code);
						} else if (0 <= amplitude && amplitude < 1) {
							json11.add(stock_code);
						} else if (1 <= amplitude && amplitude < 2) {
							json12.add(stock_code);
						} else if (2 <= amplitude && amplitude < 3) {
							json13.add(stock_code);
						} else if (3 <= amplitude && amplitude < 4) {
							json14.add(stock_code);
						} else if (4 <= amplitude && amplitude < 5) {
							json15.add(stock_code);
						} else if (5 <= amplitude && amplitude < 6) {
							json16.add(stock_code);
						} else if (6 <= amplitude && amplitude < 7) {
							json17.add(stock_code);
						} else if (7 <= amplitude && amplitude < 8) {
							json18.add(stock_code);
						} else if (8 <= amplitude && amplitude < 9) {
							json19.add(stock_code);
						} else if (9 <= amplitude && amplitude < 10) {
							json20.add(stock_code);
						} else if (10 <= amplitude) {
							json21.add(stock_code);
						}
					}
					if (!CollectionUtils.isEmpty(stocks) && thisDate.getTime() <= nowDate.getTime()) {
						stockDataService.saveLimitUpOrDown(theDate, week, json1, json21);
						int num = stockDataService.stockIntervalRisefall(theDate, week, json1, json2, json3,
								json4, json5, json6, json7, json8, json9, json10, json11, json12, json13,
								json14, json15, json16, json17, json18, json19, json20, json21);
					}
				}
			} while (thisDate.getTime() < nowDate.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	/**
	 * 记录涨跌数
	 * 
	 * @param model
	 */
	@ResponseBody
	@RequestMapping(value = "/recordRiseOrFallStock", method = RequestMethod.POST)
	public void recordRiseOrFallStock(Model model) {
		try {
			List<Map<String, Object>> shanghaiStock = dapanDataService.getAllStocks("1");
			if (!CollectionUtils.isEmpty(shanghaiStock)) {
				for (Map<String, Object> map : shanghaiStock) {
					JSONArray limitupDate = new JSONArray();
					JSONArray limitdownDate = new JSONArray();
					String code = ObjUtil.toString(map.get("code"));
					String name = ObjUtil.toString(map.get("name"));
					List<Map<String, Object>> singleStock = stockDataService.getSingleStock(code, "");
					List<Map<String, Object>> info = stockDataService.getStockInfo(code);
					int limitupTime = 0;
					int limitdownTime = 0;
					if(!CollectionUtils.isEmpty(info)){
						Object time1 = info.get(0).get("limitup_time");
						Object time2 = info.get(0).get("limitdown_time");
						if (StringUtils.isEmpty(time1)) {
							time1 = 0;
						}
						if (StringUtils.isEmpty(time2)) {
							time2 = 0;
						}
						limitupTime = ObjUtil.toInt(time1);
						limitdownTime = ObjUtil.toInt(time2);
						for (Map<String, Object> ss : singleStock) {
							String date = ObjUtil.toString(ss.get("date"));
							float amplitude = ObjUtil.toFloat(ss.get("amplitude"));
							if (amplitude >= 9.9) {
								limitupTime = limitupTime + 1;
								limitupDate.add(date);
							} else if (-10 < amplitude && amplitude < -9.9) {
								limitdownTime = limitdownTime + 1;
								limitdownDate.add(date);
							}
						}
					}
					stockDataService.saveStockRiseorfallInfo(code, name, limitupDate, limitupTime,
							limitdownDate, limitdownTime);
				}
			}
			List<Map<String, Object>> shenzhenStock = dapanDataService.getAllStocks("2");
			if (!CollectionUtils.isEmpty(shenzhenStock)) {
				for (Map<String, Object> map : shenzhenStock) {
					JSONArray limitupDate = new JSONArray();
					JSONArray limitdownDate = new JSONArray();
					String code = ObjUtil.toString(map.get("code"));
					String name = ObjUtil.toString(map.get("name"));
					List<Map<String, Object>> singleStock = stockDataService.getSingleStock(code, "");
					List<Map<String, Object>> info = stockDataService.getStockInfo(code);
					int limitupTime = 0;
					int limitdownTime =0;
					if(!CollectionUtils.isEmpty(info)){
						Object time1 = info.get(0).get("limitup_time");
						Object time2 = info.get(0).get("limitdown_time");
						if (StringUtils.isEmpty(time1)) {
							time1 = 0;
						}
						if (StringUtils.isEmpty(time2)) {
							time2 = 0;
						}
						limitupTime = ObjUtil.toInt(time1);
						limitdownTime = ObjUtil.toInt(time2);
						for (Map<String, Object> ss : singleStock) {
							String date = ObjUtil.toString(ss.get("date"));
							float amplitude = ObjUtil.toFloat(ss.get("amplitude"));
							if (amplitude >= 9.9) {
								limitupTime = limitupTime + 1;
								limitupDate.add(date);
							} else if (-10 < amplitude && amplitude < -9.9) {
								limitdownTime = limitdownTime + 1;
								limitdownDate.add(date);
							}
						}
					}
					stockDataService.saveStockRiseorfallInfo(code, name, limitupDate, limitupTime,
							limitdownDate, limitdownTime);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/count", method = RequestMethod.POST)
	public void count() {
		List<Map<String, Object>> count = stockDataService.count();
		for (Map<String, Object> map : count) {
			List<String> ld = new ArrayList<String>();
			List<String> lu = new ArrayList<String>();
			String date = "";
			try {
				date = ObjUtil.toString(map.get("date"));
			} catch (MyException e) {
				e.printStackTrace();
			}
			Object obj1 = map.get("limitdown_stocks");
			Object obj2 = map.get("limitup_stocks");
			if (!"[]".equals(obj1)) {
				ld = (List<String>) obj1;
			}
			if (!"[]".equals(obj2)) {
				lu = (List<String>) obj2;
			}
			stockDataService.updateCount(ld.size(), lu.size(), date);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/weekOrMonthRiseOrFall", method = RequestMethod.POST)
	public void weekOrMonthRiseOrFall() {
		try {
			List<Map<String, Object>> shanghaiStock = dapanDataService.getAllStocks("1");
			stockDataService.commonWeekAndMonth(shanghaiStock);
			List<Map<String, Object>> shenzhenStock = dapanDataService.getAllStocks("2");
			stockDataService.commonWeekAndMonth(shenzhenStock);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/weekOrMonthData", method = RequestMethod.POST)
	public String weekOrMonthData(Model model, String stockCode) {
		try {
			List<Map<String, Object>> weekAndMonthData = stockDataDao.getWeekAndMonthData(stockCode);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			if (!CollectionUtils.isEmpty(weekAndMonthData)) {
				Calendar cl = Calendar.getInstance();
				Map<String, Object> wamd = weekAndMonthData.get(0);
				JSONObject rwrd = JSONObject.parseObject(wamd.get("recent_week_riseorfall_data") + "");
				JSONArray array = JSON.parseArray(wamd.get("week_riseorfall_data") + "");
				Set<Entry<String, Object>> set = rwrd.entrySet();
				Iterator<Entry<String, Object>> it = set.iterator();
				while (it.hasNext()) {
					Entry<String, Object> str = it.next();
					String key = str.getKey();
					Float value = ObjUtil.toFloat(str.getValue());
					String[] s = key.split("\\|");
					if (key.contains("?")) {
						String startDay = s[0].split("\\?")[0];
						cl.setTime(StringUtil.getDate("yyyy-MM-dd", startDay));
						String endDay = s[0].split("\\?")[1];
						map.put("startDay", startDay);
						map.put("endDay", endDay);
						map.put("startClosing", s[1]);
						map.put("endClosing", s[2]);
						map.put("per", s[3]);
						map.put("differ", value);
						map.put("weekNum", cl.get(Calendar.WEEK_OF_YEAR));
					} else {
						map.put("startDay", s[0]);
						map.put("startClosing", s[1]);
						cl.setTime(StringUtil.getDate("yyyy-MM-dd", s[0]));
						map.put("weekNum", cl.get(Calendar.WEEK_OF_YEAR));
					}
				}
				list.add(0, map);
				if (!CollectionUtils.isEmpty(array)) {
					for (int i = 0; i < array.size(); i++) {
						Map<String, Object> lastmap = new HashMap<String, Object>();
						JSONObject object = JSONObject.parseObject(array.getString(i));
						Set<Entry<String, Object>> lastset = object.entrySet();
						Iterator<Entry<String, Object>> lastit = lastset.iterator();
						while (lastit.hasNext()) {
							Entry<String, Object> str = lastit.next();
							String key = str.getKey();
							Float value = ObjUtil.toFloat(str.getValue());
							String[] s = key.split("\\|");
							if (key.contains("?")) {
								String startDay = s[0].split("\\?")[0];
								cl.setTime(StringUtil.getDate("yyyy-MM-dd", startDay));
								String endDay = s[0].split("\\?")[1];
								lastmap.put("startDay", startDay);
								lastmap.put("endDay", endDay);
								lastmap.put("startClosing", s[1]);
								lastmap.put("endClosing", s[2]);
								lastmap.put("per", s[3]);
								lastmap.put("differ", value);
								lastmap.put("weekNum", cl.get(Calendar.WEEK_OF_YEAR));
							} else {
								lastmap.put("startDay", s[0]);
								lastmap.put("startClosing", s[1]);
								cl.setTime(StringUtil.getDate("yyyy-MM-dd", s[0]));
								lastmap.put("weekNum", cl.get(Calendar.WEEK_OF_YEAR));
							}
						}
						list.add(lastmap);
					}
				}
			}
			model.addAttribute("wamData", list);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return "jsp/admin/stock/data/weekAndMonthData";
	}

	/**
	 * 搜索
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String serach(Model model) {
		try {
			String stockMarket = getRequest().getParameter("stockMarket");
			String searchValue = getRequest().getParameter("searchValue");
			List<Map<String, Object>> stock = stockService.serach(stockMarket, searchValue);
			model.addAttribute("allStock", stock);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/stock/search";
	}

	@RequestMapping(value = "/test")
	public void test(Model model, String stockCode) {
		try {
			// commonAdminService.getRMBExchangeRate();
			// stockDataService.test();
			// stockDaoImpl.findById(1);
			// List<Object> obj = stockService.getAverageLineList("", "600000",
			// 5,
			// "");
			stockDataService.updateWeekAndMonthData("600000", "16.22");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/appTest")
	public void appTest() {
		String json = "";
		try {
			// commonAdminService.getRMBExchangeRate();
			// stockDataService.test();
			// stockDaoImpl.findById(1);
			// List<Object> obj = stockService.getAverageLineList("", "600000",
			// 5,
			// "");
			Map<String, String> params = getParameterMap();
			json = stockDataService.getStockToApp(params.get("stockCode"), params.get("date")).toJSONString();
			getResponse().setContentType("text/plain; charset=UTF-8");
			getResponse().setCharacterEncoding("UTF-8");
			PrintWriter printWriter = getResponse().getWriter();
			printWriter.print(json);
			printWriter.flush();
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/appAllStock", method = RequestMethod.POST)
	public void appAllStock() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Map<String, String> params = getParameterMap();
			list = dapanDataService.getAllStocks(params.get("marketMark"));
			getResponse().setContentType("text/plain; charset=UTF-8");
			getResponse().setCharacterEncoding("UTF-8");
			PrintWriter printWriter = getResponse().getWriter();
			JSONArray array = new JSONArray();
			array.addAll(list);
			printWriter.print(array.toString());
			printWriter.flush();
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
