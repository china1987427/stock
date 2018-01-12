package com.china.stock.user.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
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
import com.china.stock.common.data.StockHistoryDataConnection;
import com.china.stock.common.server.CommonDataService;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.tool.controller.BaseController;
import com.china.stock.common.util.JsonUtils;
import com.china.stock.common.util.StringUtil;
import com.china.stock.common.util.TimeUtil;
import com.china.stock.user.server.RealTimeService;
import com.china.stock.user.server.StockService;




@Controller
@RequestMapping(value = "/stockWorld")
public class StockController extends BaseController {
	private static Logger log = Logger.getLogger(StockController.class);

	@Autowired
	private RealTimeService realTimeService;
	@Autowired
	private StockService stockService;
	@Autowired
	private CommonDataService commonService;

	/**
	 * 到数据页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/toStock")
	public String toStock(Model model) {
		try {
			List<Map<String, Object>> list = stockService.getStockData("");
			model.addAttribute("allStock", list);
			model.addAttribute("hgtten", commonService.getHgtTen());
			String username = getRequest().getParameter("username");
			model.addAttribute("username", username);
			model.addAttribute("backUrl", "stockWorld/toStock");
			getRequest().setAttribute("indexStock", commonService.getStock().get(0));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/stock/stock";
	}

	/**
	 * 获取k线数据
	 * 
	 * @param model
	 * @param stockCode
	 */
	@ResponseBody
	@RequestMapping(value = "/dynData", method = RequestMethod.GET)
	public void dynData(Model model, @RequestParam(value = "stockCode") String stockCode) {
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> json = new HashMap<String, Object>();
			if ("dapan".equals(stockCode) || "大盘指数".equals(stockCode)) {
				list = stockService.getDaPanData();
				List<Object> json1 = new ArrayList<Object>();
				List<Long> json2 = new ArrayList<Long>();
				List<Float> json3 = new ArrayList<Float>();
				List<Float> json5 = new ArrayList<Float>();
				List<Float> json6 = new ArrayList<Float>();
				List<Object> json7 = new ArrayList<Object>();
				for (Map<String, Object> map : list) {
					List<Object> json4 = new ArrayList<Object>();
					String date = ObjUtil.toString(map.get("date"));
					String week = ObjUtil.toString(map.get("day_in_week"));
					long sum = (ObjUtil.toLong(map.get("sum_money")));
					float mRiseorfall = ObjUtil.toFloat(map.get("rise_or_fall"));
					float mAmplitude = ObjUtil.toFloat(map.get("amplitude"));
					float mTotalhand = ObjUtil.toFloat(map.get("total_hand"));
					float mClosingIndex = ObjUtil.toFloat(map.get("closing_index"));
					float mOpeningIndex = ObjUtil.toFloat(map.get("opening_index"));
					float mHighIndex = ObjUtil.toFloat(map.get("high_index"));
					float mLowIndex = ObjUtil.toFloat(map.get("low_index"));
					json1.add(date);
					json2.add(sum);
					json3.add(mClosingIndex);
					json4.add(mOpeningIndex);
					json4.add(mClosingIndex);
					json4.add(mLowIndex);
					json4.add(mHighIndex);
					json5.add(mHighIndex);
					json6.add(mLowIndex);
					json7.add(json4);
				}
				float maxci = Collections.max(json3);
				long minSumMoney = Collections.min(json2);
				long maxSumMoney = Collections.max(json2);
				float maxhi = Collections.max(json5);
				float minli = Collections.min(json6);
				json.put("kdate", json1);
				json.put("closingIndex", json3);
				json.put("sumMoney", json2);
				json.put("kdata", json7);
				json.put("maxci", maxci);
				json.put("latelyCI", json3.get(0));
				json.put("latelyDate", json1.get(0));
				json.put("maxhi", maxhi);
				json.put("minli", minli);
				json.put("maxSumMoney", maxSumMoney);
				json.put("minSumMoney", minSumMoney);
				JsonUtils.writeJson(json, getRequest(), getResponse());
			} else {
				list = stockService.getStockEveryDayData(" order by date desc LIMIT 60)s order by s.date asc", "", stockCode);
				List<Object> json1 = new ArrayList<Object>();
				List<Float> json2 = new ArrayList<Float>();
				List<Double> json3 = new ArrayList<Double>();
				List<Object> json5 = new ArrayList<Object>();
				List<Float> json8 = new ArrayList<Float>();
				List<Float> json9 = new ArrayList<Float>();
				for (Map<String, Object> map : list) {
					List<Object> json4 = new ArrayList<Object>();
					float closingIndex = ObjUtil.toFloat(map.get("closing_index"));
					String date = ObjUtil.toString(map.get("date"));
					String dayInWeek = ObjUtil.toString(map.get("day_in_week"));
					double volumeOfBusiness = (ObjUtil.toDouble(map.get("total_money")));//成交额
					float openingIndex = ObjUtil.toFloat(map.get("opening_index"));
					float highestIndex = ObjUtil.toFloat(map.get("high_index"));
					float lowestIndex = ObjUtil.toFloat(map.get("low_index"));
					json1.add(date);
					json2.add(closingIndex);
					json3.add(volumeOfBusiness);
					json4.add(openingIndex);
					json4.add(closingIndex);
					json4.add(lowestIndex);
					json4.add(highestIndex);
					json5.add(json4);
					json8.add(highestIndex);
					json9.add(lowestIndex);
				}
				float maxci = Collections.max(json2);
				float maxhi = Collections.max(json8);
				float minli = Collections.min(json9);
				double minSumMoney = Collections.min(json3);
				double maxSumMoney = Collections.max(json3);
				json.put("date", json1);
				json.put("closingIndex", json2);
				json.put("sumMoney", json3);
				json.put("kdata", json5);
				json.put("maxci", maxci);
				json.put("latelyCI", json2.get(0));
				json.put("latelyDate", json1.get(0));
				json.put("maxhi", maxhi);
				json.put("minli", minli);
				json.put("maxSumMoney", maxSumMoney);
				json.put("minSumMoney", minSumMoney);
				JsonUtils.writeJson(json, getRequest(), getResponse());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}

	}

	/**
	 * 获取当前时间后的一个均线数据
	 * 
	 * @param model
	 * @param stockCode
	 */
	@ResponseBody
	@RequestMapping(value = "/getAverageLine")
	public void getAverageLine(Model model, @RequestParam(value = "stockCode") String stockCode) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<Object> ciList = new ArrayList<Object>();
			String lineName = getRequest().getParameter("lineName");
			int n = Integer.valueOf(lineName);
			Map<String, Object> map = new HashMap<String, Object>();
			if ("dapan".equals(stockCode)) {
				map = stockService.getFuncDapan();
			} else {
				map = stockService.getFuncStockData(stockCode);
			}
			Date date = sdf.parse(ObjUtil.toString(map.get("max")));
			List<Map<String, Object>> list = stockService.getAverageLineData(date, stockCode, n);
			for (Map<String, Object> stock : list) {
				double closingIndex = ObjUtil.toDouble(stock.get("closing_index"));
				ciList.add(closingIndex);
			}
			double sum = 0;
			for (int i = 0; i < ciList.size(); i++) {
				double j = (double) ciList.get(i);
				sum += j;
			}
			double averageLine = sum / n;
			WriterJson(ObjUtil.toString(averageLine));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	/**
	 * 加载k线图
	 * 
	 * @param model
	 * @param stockCode
	 * @return
	 */
	@RequestMapping(value = "/loadk")
	public String loadk(Model model, @RequestParam(value = "stockCode") String stockCode) {
		try {
			String stockname = getRequest().getParameter("stockname");
			model.addAttribute("stockname", stockname);
			model.addAttribute("stockCode", stockCode);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/test/canvas3";
	}

	/**
	 * 加载均线图
	 * 
	 * @param model
	 * @param line
	 * @param stockCode
	 * @return
	 */
	@RequestMapping(value = "/loada")
	public String loada(Model model, @RequestParam(value = "line") String line, @RequestParam(value = "stockCode") String stockCode) {
		try {
			String stockname = getRequest().getParameter("stockname");
			model.addAttribute("line", line);
			model.addAttribute("stockCode", stockCode);
			model.addAttribute("stockname", stockname);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/stock/aline";
	}

	/**
	 * 获取5，10，20，30，60，120均线数据
	 * 
	 * @param stockCode
	 * @param line
	 */
	@ResponseBody
	@RequestMapping(value = "/getAverageLineList")
	public void getAverageLineList(@RequestParam(value = "stockCode") String stockCode, @RequestParam(value = "line") String line) {
		try {
			String nowCI = "";
			DecimalFormat df = null;
			int n = Integer.valueOf(line);
			Map<String, Object> map = new HashMap<String, Object>();
			if ("dapan".equals(stockCode)) {
				map = stockService.getFuncDapan();
			} else {
				map = stockService.getFuncStockData(stockCode);
			}
			String nowDate = ObjUtil.toString(map.get("max"));
			SimpleDateFormat sdfor = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdfor.parse(nowDate));
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 200); // 获取1年的数据
			long d = cal.getTimeInMillis();
			List<Object> list = stockService.getAverageLineList("", stockCode, n, "");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(ObjUtil.toString(map.get("max")));
			Map<String, Object> stockMap = new HashMap<String, Object>();
			List<String> dateList = new ArrayList<String>();
			List<String> indexList = new ArrayList<String>();
			stockMap.put("date", list.get(1));
			stockMap.put("index", list.get(0));
			stockMap.put("latelyCI", JSON.parseArray(ObjUtil.toString(list.get(0))).get(0));
			stockMap.put("latelyDate", JSON.parseArray(ObjUtil.toString(list.get(1))).get(0));
			stockMap.put("nowDate", nowDate);
			JsonUtils.writeJson(stockMap, getRequest(), getResponse());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	/**
	 * 验证该股票数据库中是否存在
	 * 
	 * @param stockCode
	 */
	@ResponseBody
	@RequestMapping(value = "/verify")
	public void verify(@RequestParam(value = "stockCode") String stockCode) {
		try {
			JSONObject object = new JSONObject();
			Map<String, Object> map = new HashMap<String, Object>();
			if ("dapan".equals(stockCode)) {
				map = stockService.getFuncDapan();
			} else {
				map = stockService.getFuncStockData(stockCode);
			}
			int count = ObjUtil.toInt(map.get("count"));
			String nowDate = StringUtil.getStrDate("yyyyMMdd", new Date());
			if (count >= 1) {
				String mDate = ObjectUtils.toString(map.get("max"));
				String week = StringUtil.getWeek(mDate);
				Calendar cal = TimeUtil.setCal(15, 30, 0);
				String oneday = StringUtil.getStrByLT("yyyyMMdd", mDate, 1);
				long l = cal.getTimeInMillis();
				if (!oneday.equals(nowDate)) {
					if ("星期五".equals(week)) {
						String currDate = StringUtil.getStrByLT("yyyyMMdd", mDate, 3);
						object.put("date", currDate.replace("-", ""));
						object.put("result", "true");
					} else {
						object.put("date", oneday.replace("-", ""));
						object.put("result", "true");
					}
				} else {
					if (new Date().getTime() >= new Date(l).getTime()) {
						object.put("date", oneday.replace("-", ""));
						object.put("result", "true");
					}
				}
			} else {
				long date = TimeUtil.getCal(new Date(), 360);
				String pastDate = StringUtil.getStrDate("yyyyMMdd", new Date(date));
				object.put("date", pastDate);
				object.put("result", "true");
			}
			WriterJson(object.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	/**
	 * 获取历史数据
	 * 
	 * @param stockCode
	 * @param stockname
	 */
	@ResponseBody
	@RequestMapping(value = "/getHistoryData")
	public void getHistoryData(@RequestParam(value = "stockCode") String stockCode, @RequestParam(value = "stockname") String stockname) {
		try {
			String startDate = getRequest().getParameter("startDate");
			String endDate = getRequest().getParameter("endDate");
			String nowDate = StringUtil.getStrDate("yyyyMMdd", new Date());
			long date = TimeUtil.getCal(new Date(), 360);
			String pastDate = StringUtil.getStrDate("yyyyMMdd", new Date(date));
			if (StringUtils.isEmpty(startDate)) {
				startDate = pastDate;
			}
			if (StringUtils.isEmpty(endDate)) {
				endDate = nowDate;
			}
			String historyStock = StockHistoryDataConnection.getStockHistoryData(stockCode, startDate, endDate);
			if ("{}".equals(historyStock)) {
				WriterJson("暂无当天数据");
			} else {
				stockService.saveHistoryData(historyStock, stockCode, stockname);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	/**
	 * 搜索
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/search")
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

	/**
	 * 数据中心
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dataCenter")
	public String dataCenter(Model model) {
		try {
			String mark = getRequest().getParameter("mark");
			String stockCode = getRequest().getParameter("stockCode");
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			String data = realTimeService.getRealTime(stockCode, mark);
			JSONObject retjson = JSON.parseObject(data);
			JSONArray jsonSI = JSON.parseArray(ObjUtil.toString(retjson.get("stockinfo")));
			if (!CollectionUtils.isEmpty(jsonSI)) {
				map = (Map<String, Object>) jsonSI.get(0);
			}
			JSONObject allLine = realTimeService.getAllLine(stockCode);
			String fiveLine = allLine.getJSONObject("fiveLine").getString("latelyCI");
			String fiveLineDate = allLine.getJSONObject("fiveLine").getString("latelyDate");
			String nowCI = allLine.getJSONObject("fiveLine").getString("nowCI");
			String nowDate = allLine.getJSONObject("fiveLine").getString("nowDate");
			String tenLine = allLine.getJSONObject("tenLine").getString("latelyCI");
			String tenLineDate = allLine.getJSONObject("tenLine").getString("latelyDate");
			String twentyLine = allLine.getJSONObject("twentyLine").getString("latelyCI");
			String twentyLineDate = allLine.getJSONObject("twentyLine").getString("latelyDate");
			String thirtyLine = allLine.getJSONObject("thirtyLine").getString("latelyCI");
			String thirtyLineDate = allLine.getJSONObject("thirtyLine").getString("latelyDate");
			String sixtyLine = allLine.getJSONObject("sixtyLine").getString("latelyCI");
			String sixtyLineDate = allLine.getJSONObject("sixtyLine").getString("latelyDate");
			String ohtLine = allLine.getJSONObject("ohtLine").getString("latelyCI");
			String ohtLineDate = allLine.getJSONObject("ohtLine").getString("latelyDate");
			map.put("fiveLine", fiveLine);
			map.put("fiveLineDate", fiveLineDate);
			map.put("nowCI", nowCI);
			map.put("nowDate", nowDate);
			map.put("tenLine", tenLine);
			map.put("tenLineDate", tenLineDate);
			map.put("tenLineDate", tenLineDate);
			map.put("twentyLine", twentyLine);
			map.put("twentyLineDate", twentyLineDate);
			map.put("thirtyLine", thirtyLine);
			map.put("thirtyLineDate", thirtyLineDate);
			map.put("sixtyLine", sixtyLine);
			map.put("sixtyLineDate", sixtyLineDate);
			map.put("ohtLine", ohtLine);
			map.put("ohtLineDate", ohtLineDate);
			String nowWeek = (String) TimeUtil.sdfDateTime("EEEE", "", "str", new Date());
			int n = 0;
			if ("星期六".equals(nowWeek)) {
				n = 1;
			} else if ("星期日".equals(nowWeek)) {
				n = 2;
			} else if ("星期一".equals(nowWeek)) {
				n = 3;
			} else {
				n = 1;
			}
			String date = (String) TimeUtil.sdfDateTime("yyyy-MM-dd", "", "str", new Date(new Date().getTime() - n * 24 * 60 * 60 * 1000));
			List<Map<String, Object>> maxDate = stockService.getStockEveryDayData("", date, stockCode);
			String yestClosingIndex = "";
			for (Map<String, Object> ci : maxDate) {
				yestClosingIndex = ObjUtil.toString(ci.get("closing_index"));
			}
			map.put("yestClosingIndex", yestClosingIndex);
			list.add(map);
			model.addAttribute("dataCenter", list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/stock/dataCenter";
	}

	/**
	 * 所有股票
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/allStocks")
	public String allStocks(Model model) {
		try {
			List<Map<String, Object>> list = stockService.getStockData("");
			model.addAttribute("allStock", list);
			String username = getRequest().getParameter("username");
			model.addAttribute("username", username);
			model.addAttribute("backUrl", "stockWorld/allStocks");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/stock/allStocks";
	}

	@RequestMapping(value = "/showStock")
	public String showStock(Model model) {
		try {
			String mark = getRequest().getParameter("mark");
			String stockCode = getRequest().getParameter("stockCode");
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			String data = realTimeService.getRealTime(stockCode, mark);
			JSONObject retjson = JSON.parseObject(data);
			JSONArray jsonSI = JSON.parseArray(ObjUtil.toString(retjson.get("stockinfo")));
			if (!CollectionUtils.isEmpty(jsonSI)) {
				map = (Map<String, Object>) jsonSI.get(0);
				map.put("code", (map.get("code") + "").replace("sh", ""));
			}
			map.put("mark", mark);
			list.add(map);
			model.addAttribute("stock", list);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/stock/showData";
	}

	/**
	 * 加入自选股
	 * 
	 * @param model
	 */
	@ResponseBody
	@RequestMapping(value = "/tomystocks")
	public void tomystocks(Model model) {
		try {
			String mark = getRequest().getParameter("mark");
			String stockCode = getRequest().getParameter("stockCode");
			String username = getRequest().getParameter("username");
			if (StringUtils.isEmpty(username)) {
				WriterJson("nologin");
			} else {
				String stockName = getRequest().getParameter("stockName");
				List<Map<String, Object>> list = stockService.getFuncDataFromMyStock(stockCode);
				int count = 0;
				for (Map<String, Object> map : list) {
					count = ObjUtil.toInt(map.get("count"));
				}
				if (count == 0) {
					String result = stockService.tomystocks(mark, stockCode, username, stockName);
					WriterJson(result);
				} else {
					WriterJson("yes");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	@RequestMapping(value = "/myStock")
	public String myStock(Model model) {
		try {
			String username = getRequest().getParameter("username");
			if (StringUtils.isEmpty(username)) {
				return "redirect:/user/toLogin?backUrl=stockWorld/showMyStock";
			} else {
				List<Map<String, Object>> list = stockService.getDataFromMyStock("", username);
				for (Map<String, Object> map : list) {
					String stockCode = ObjUtil.toString(map.get("stock_code"));
					String mark = ObjUtil.toString(map.get("market"));
					String data = realTimeService.getRealTime(stockCode, mark);
					JSONObject retjson = JSON.parseObject(data);
					JSONArray jsonSI = JSON.parseArray(ObjUtil.toString(retjson.get("stockinfo")));
					if (!CollectionUtils.isEmpty(jsonSI)) {
						Map<String, Object> mapsi = (Map<String, Object>) jsonSI.get(0);
						map.put("code", (mapsi.get("code") + "").replace("sh", ""));
						map.put("stockData", jsonSI);
					}
				}
				model.addAttribute("myStock", list);
				model.addAttribute("username", username);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/stock/myStock";
	}

	@RequestMapping(value = "/showMyStock")
	public String showMyStock(Model model) {
		try {
			String username = getRequest().getParameter("username");
			if (StringUtils.isEmpty(username)) {
				return "redirect:/user/toLogin?backUrl=stockWorld/showMyStock";
			} else {
				List<Map<String, Object>> list = stockService.getDataFromMyStock("", username);
				for (Map<String, Object> map : list) {
					String stockCode = ObjUtil.toString(map.get("stock_code"));
					String mark = ObjUtil.toString(map.get("market"));
					String data = realTimeService.getRealTime(stockCode, mark);
					JSONObject retjson = JSON.parseObject(data);
					JSONArray jsonSI = JSON.parseArray(ObjUtil.toString(retjson.get("stockinfo")));
					if (!CollectionUtils.isEmpty(jsonSI)) {
						Map<String, Object> mapsi = (Map<String, Object>) jsonSI.get(0);
						map.put("code", (mapsi.get("code") + "").replace("sh", ""));
						map.put("stockData", jsonSI);
					}
				}
				model.addAttribute("myStock", list);
				model.addAttribute("username", username);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/stock/showMyStock";
	}

	@RequestMapping(value = "/getCurrentData")
	public String getCurrentData(Model model, String stockCode) {
		try {
			String stockname = getRequest().getParameter("stockname");
			model.addAttribute("stockname", stockname);
			model.addAttribute("stockCode", stockCode);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/test/canvas4";
	}
}
