package com.china.stock.user.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.tool.controller.BaseController;
import com.china.stock.common.util.JsonUtils;
import com.china.stock.common.util.StringUtil;
import com.china.stock.common.util.TimeUtil;
import com.china.stock.user.server.RealTimeService;
import com.china.stock.user.server.StockService;


@Controller
@RequestMapping(value = "/realTime")
public class RealTimeController extends BaseController {
	private static Logger log = Logger.getLogger(RealTimeController.class);
	int a = 0;
	@Autowired
	private RealTimeService realTimeService;
	@Autowired
	private StockService stockService;

	@RequestMapping(value = "/toRealTime")
	public String toRealTime(Model model,
			@RequestParam(value = "stockCode") String stockCode) {
		String mark = getRequest().getParameter("mark");
		model.addAttribute("stockCode", stockCode);
		model.addAttribute("mark", mark);
		return "jsp/stock/realTime";
	}

	@ResponseBody
	@RequestMapping(value = "/getRealTime", method = RequestMethod.GET)
	public void getRealTime(Model model) {
		try {
			String mark = getRequest().getParameter("mark");
			String stockCode = getRequest().getParameter("stockCode");
			stockCode = new String(stockCode.getBytes("iso-8859-1"), "UTF-8");
			String data = realTimeService.getRealTime(stockCode, mark);
			JSONObject retjson = JSON.parseObject(data);
			String stockinfo = "";
			JSONArray jsonSI = JSON.parseArray(ObjUtil.toString(retjson
					.get("stockinfo")));
			if (!CollectionUtils.isEmpty(jsonSI)) {
				stockinfo = ObjUtil.toString(jsonSI.get(0));
			}
			JSONObject jsonStockinfo = JSON.parseObject(stockinfo);
			retjson.remove("stockinfo");
			retjson.put("stockinfo", jsonStockinfo);
			List<String> list = TimeUtil.getTimeArray();
			retjson.put("timeArray", list);
			retjson.put("timeSize", list.size());
			List<String> initValue = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				initValue.add(i, "-");
			}
			retjson.put("initValue", initValue);
			// JSONObject allLine = realTimeService.getAllLine(stockCode);
			String nowDate = StringUtil.getStrDate("yyyyMMdd", new Date());
			long date = TimeUtil.getCal(
					StringUtil.getDate("yyyyMMdd", nowDate), 1);
			String week = (String) TimeUtil.sdfDateTime("EEEE", "", "str",
					new Date(date));
			if ("星期六".equals(week)) {
				date = TimeUtil.getCal(StringUtil.getDate("yyyyMMdd", nowDate),
						2);
			} else if ("星期日".equals(week)) {
				date = TimeUtil.getCal(StringUtil.getDate("yyyyMMdd", nowDate),
						3);
			} else if ("星期一".equals(week)) {
				date = TimeUtil.getCal(StringUtil.getDate("yyyyMMdd", nowDate),
						4);
			}
			List<Map<String, Object>> stockList = new ArrayList<Map<String, Object>>();
			Map<String, Object> stock = new HashMap<String, Object>();
			if ("dapan".equals(stockCode)) {
				stock.put("pe_ratio", "");
				stock.put("industry", "");
			} else {
				stockList = stockService.getStockData(stockCode);
				if (!CollectionUtils.isEmpty(stockList)) {
					stock = stockList.get(0);
				}
			}
			// retjson.put("allLine", allLine);
			retjson.put("stock", stock);
			JsonUtils.writeJson(retjson, getRequest(), getResponse());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getCpValue")
	public void getCpValue(Model model) {
		try {
			String mark = getRequest().getParameter("mark");
			String stockCode = getRequest().getParameter("stockCode");
			JSONObject json = new JSONObject();
			List<String> list = TimeUtil.getTimeArray();
			String data = realTimeService.getRealTime(stockCode, mark);
			JSONObject retjson = JSON.parseObject(data);
			JSONArray jsonSI = JSON.parseArray(ObjUtil.toString(retjson
					.get("stockinfo")));
			String stockinfo = ObjUtil.toString(jsonSI.get(0));
			JSONObject jsonStockinfo = JSON.parseObject(stockinfo);
			// String currentPrice = ObjUtil.toString(jsonStockinfo
			// .get("currentPrice"));
			DecimalFormat fnum = new DecimalFormat("##0.00");
			String currentPrice = ObjUtil.toString(fnum.format(4 + Math
					.random()));
			Date datetime = StringUtil.getDate(
					"yyyy-MM-dd HH:mm:ss",
					jsonStockinfo.getString("date") + " "
							+ jsonStockinfo.getString("time"));
			Date nowDate = new Date();
			int index = 0;
			if ((datetime.getTime() - nowDate.getTime()) / 1000 >= 0) {
				long d = TimeUtil.setCal(datetime.getHours(),
						datetime.getMinutes(), 0).getTimeInMillis();
				String date = StringUtil.getStrDate("yyyy-MM-dd HH:mm:ss",
						new Date(d));
				index = list.indexOf(date);
			} else {
				long d = TimeUtil.setCal(nowDate.getHours(),
						nowDate.getMinutes(), 0).getTimeInMillis();
				String date = StringUtil.getStrDate("yyyy-MM-dd HH:mm:ss",
						new Date(d));
				index = list.indexOf(date);
			}
			a++;
			json.put("index", a);
			json.put("currentPrice", currentPrice);
			json.put("stockinfo", jsonStockinfo);
			JsonUtils.writeJson(json, getRequest(), getResponse());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getData")
	public void getData() {
		try {
			String mark = getRequest().getParameter("mark");
			String stockCode = getRequest().getParameter("stockCode");
			JSONObject json = new JSONObject();
			String stockinfo = "";
			String data = realTimeService.getRealTime(stockCode, mark);
			JSONObject retjson = JSON.parseObject(data);
			JSONArray jsonSI = JSON.parseArray(ObjUtil.toString(retjson
					.get("stockinfo")));
			if (!CollectionUtils.isEmpty(jsonSI)) {
				stockinfo = ObjUtil.toString(jsonSI.get(0));
			}
			JSONObject jsonStockinfo = JSON.parseObject(stockinfo);
			retjson.remove("stockinfo");
			retjson.put("stockinfo", jsonStockinfo);
			json.put("realData", retjson);
			JSONObject allLine = realTimeService.getAllLine(stockCode);
			json.put("allLine", allLine);
			String nowWeek = (String) TimeUtil.sdfDateTime("EEEE", "", "str",
					new Date());
			int n = 0;
			if ("星期六".equals(nowWeek)) {
				n = 1;
			} else if ("星期日".equals(nowWeek)) {
				n = 2;
			} else if ("星期一".equals(nowWeek)) {
				n = 3;
			}
			String date = (String) TimeUtil.sdfDateTime("yyyy-MM-dd", "",
					"str", new Date(new Date().getTime() - n * 24 * 60 * 60
							* 1000));
			List<Map<String, Object>> list = stockService.getStockEveryDayData(
					"", date, stockCode);
			String yestClosingIndex = "";
			for (Map<String, Object> map : list) {
				yestClosingIndex = ObjUtil.toString(map.get("closing_index"));
			}
			json.put("yestClosingIndex", yestClosingIndex);
			JsonUtils.writeJson(json, getRequest(), getResponse());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/closeRealTime")
	public void closeRealTime() {
		SimpleDateFormat format = new SimpleDateFormat("EEEE");
		JSONObject json = new JSONObject();
		Calendar c1 = TimeUtil.setCal(9, 30, 0);
		long d1 = c1.getTimeInMillis();

		Calendar c2 = TimeUtil.setCal(11, 30, 0);
		long d2 = c2.getTimeInMillis();

		Calendar c3 = TimeUtil.setCal(13, 0, 0);
		long d3 = c3.getTimeInMillis();

		Calendar c4 = TimeUtil.setCal(15, 0, 0);
		long d4 = c4.getTimeInMillis();

		Date date = new Date();
		String week = format.format(date);
		if ("星期六".equals(week) || "星期日".equals(week)) {
			json.put("closeCD", "close");
		} else {
			json.put("closeCD", "open");
		}
		if ((date.after(new Date(d1)) && date.before(new Date(d2)))
				|| (date.after(new Date(d3)) && date.before(new Date(d4)))) {
			json.put("closeCD", "open");
		} else {
			json.put("closeCD", "close");
		}
		JsonUtils.writeJson(json, getRequest(), getResponse());
	}
}