package com.china.stock.user.server;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.china.stock.common.data.BaiDuStockCurrent;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.user.dao.RealTimeDao;


@Service(value = "/realTimeService")
public class RealTimeService {

	@Autowired
	private RealTimeDao realTimeDao;

	@Autowired
	private StockService stockService;

	public String getRealTime(String stockCode, String mark) {
		String retData = "";
		try {
			BaiDuStockCurrent bdsc =new BaiDuStockCurrent();
			if ("dapan".equals(stockCode)) {
				// String data = SinaStockCurrent.getCurrentData();
				String data = bdsc.request("000001", "1");
				if (!StringUtils.isEmpty(data)) {
					// String[] dataArray = data.split("=")[1].replace("\"",
					// "").split(",");
					// String name = dataArray[0];
					// String openClosing = dataArray[1];
					// String pastClosing = dataArray[2];
					// String closing = dataArray[3];
					// String highClosing = dataArray[4];
					// String lowClosing = dataArray[5];
					// String tatalHand = dataArray[8];
					// String sum = dataArray[9];
					// String date = dataArray[30];
					// String time = dataArray[31];
					JSONObject json = JSON.parseObject(data);
					retData = json.get("retData") + "";
					JSONObject retjson = JSON.parseObject(retData);
					JSONArray jsonSI = JSON.parseArray(retjson.get("stockinfo") + "");
					String stockinfo = ObjUtil.toString(jsonSI.get(0));
					JSONObject stockData = JSON.parseObject(stockinfo);
					String name = stockData.getString("name");
					String openClosing = stockData.getString("OpenningPrice");
					String pastClosing = stockData.getString("currentPrice");
					String closing = stockData.getString("closingPrice");
					String highClosing = stockData.getString("hPrice");
					String lowClosing = stockData.getString("lPrice");
					String tatalHand = stockData.getString("turnover");
					String sum = stockData.getString("totalNumber");
					String date = stockData.getString("date");
					String time = stockData.getString("time");
					realTimeDao.saveToMarket(name, openClosing, pastClosing, closing, highClosing, lowClosing,
							tatalHand, sum, date, time);
				}
			} else {
				String data = bdsc.request(stockCode, mark);
				JSONObject json = JSON.parseObject(data);
				retData = ObjUtil.toString(json.get("retData"));
				JSONObject retjson = JSON.parseObject(retData);
				JSONArray jsonSI = json.getJSONObject("retData").getJSONArray("stockinfo");
				String stockinfo = ObjUtil.toString(jsonSI.get(0));
				dealStockInfo(stockinfo);
				String market = ObjUtil.toString(retjson.get("market"));
				JSONObject mjson = JSON.parseObject(market);
				String shanghai = ObjUtil.toString(mjson.get("shanghai"));
				JSONObject shjson = JSON.parseObject(shanghai);
				String SHcurdot = shjson.getString("curdot");
				String SHname = shjson.getString("name");
				String SHcurprice = shjson.getString("curprice");
				String SHrate = shjson.getString("rate");
				String SHdealnumber = shjson.getString("dealnumber");
				String SHturnover = shjson.getString("turnover");
				String shenzhen = ObjUtil.toString(mjson.get("shenzhen"));
				JSONObject szjson = JSON.parseObject(shenzhen);
				String SZcurdot = szjson.getString("curdot");
				String SZname = szjson.getString("name");
				String SZcurprice = szjson.getString("curprice");
				String SZrate = szjson.getString("rate");
				String SZdealnumber = szjson.getString("dealnumber");
				String SZturnover = szjson.getString("turnover");
				// String DJI = ObjUtil.toString(mjson.get("DJI"));// 道琼斯
				// JSONObject DJIjson = JSON.parseObject(DJI);
				// String DJIcurdot = ObjUtil.toString(DJIjson.get("curdot"));
				realTimeDao.saveMarketInfo(SHcurdot, SHname, SHcurprice, SHrate, SHdealnumber, SHturnover, "1");
				realTimeDao.saveMarketInfo(SZcurdot, SZname, SZcurprice, SZrate, SZdealnumber, SZturnover, "2");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return retData;
	}

	public void dealStockInfo(String stockinfo) {
		try {
			JSONObject json = JSON.parseObject(stockinfo);
			String name = ObjUtil.toString(json.get("name"));
			String code = ObjUtil.toString(json.get("code"));
			String date = json.getString("date");
			String time = json.getString("time");
			String OpenningPrice = ObjUtil.toString(json.get("OpenningPrice"));
			String closingPrice = ObjUtil.toString(json.get("closingPrice"));
			String currentPrice = ObjUtil.toString(json.get("currentPrice"));
			String hPrice = ObjUtil.toString(json.get("hPrice"));
			String lPrice = ObjUtil.toString(json.get("lPrice"));
			String competitivePrice = ObjUtil.toString(json.get("competitivePrice"));
			String auctionPrice = ObjUtil.toString(json.get("auctionPrice"));
			String totalNumber = ObjUtil.toString(json.get("totalNumber"));
			String turnover = ObjUtil.toString(json.get("turnover"));
			String increase = ObjUtil.toString(json.get("increase"));
			String buyOne = ObjUtil.toString(json.get("buyOne"));
			String buyOnePrice = ObjUtil.toString(json.get("buyOnePrice"));
			String buyTwo = ObjUtil.toString(json.get("buyTwo"));
			String buyTwoPrice = ObjUtil.toString(json.get("buyTwoPrice"));
			String buyThree = ObjUtil.toString(json.get("buyThree"));
			String buyThreePrice = ObjUtil.toString(json.get("buyThreePrice"));
			String buyFour = ObjUtil.toString(json.get("buyFour"));
			String buyFourPrice = ObjUtil.toString(json.get("buyFourPrice"));
			String buyFive = ObjUtil.toString(json.get("buyFive"));
			String buyFivePrice = ObjUtil.toString(json.get("buyFivePrice"));

			String sellOne = ObjUtil.toString(json.get("sellOne"));
			String sellOnePrice = ObjUtil.toString(json.get("sellOnePrice"));
			String sellTwo = ObjUtil.toString(json.get("sellTwo"));
			String sellTwoPrice = ObjUtil.toString(json.get("sellTwoPrice"));
			String sellThree = ObjUtil.toString(json.get("sellThree"));
			String sellThreePrice = ObjUtil.toString(json.get("sellThreePrice"));
			String sellFour = ObjUtil.toString(json.get("sellFour"));
			String sellFourPrice = ObjUtil.toString(json.get("sellFourPrice"));
			String sellFive = ObjUtil.toString(json.get("sellFive"));
			String sellFivePrice = ObjUtil.toString(json.get("sellFivePrice"));

			String minurl = ObjUtil.toString(json.get("minurl"));
			String dayurl = ObjUtil.toString(json.get("dayurl"));
			String weekurl = ObjUtil.toString(json.get("weekurl"));
			String monthurl = ObjUtil.toString(json.get("monthurl"));

			realTimeDao.saveCurrentData(name, code, date, time, OpenningPrice, closingPrice, currentPrice, hPrice,
					lPrice, competitivePrice, auctionPrice, totalNumber, turnover, increase, buyOne, buyOnePrice,
					buyTwo, buyTwoPrice, buyThree, buyThreePrice, buyFour, buyFourPrice, buyFive, buyFivePrice,

					sellOne, sellOnePrice, sellTwo, sellTwoPrice, sellThree, sellThreePrice, sellFour, sellFourPrice,
					sellFive, sellFivePrice,

					minurl, dayurl, weekurl, monthurl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JSONObject getAllLine(String stockCode) {
		JSONObject json = new JSONObject();
		Map<String, Object> fiveLine = getRecentLine(stockCode, "5");
		Map<String, Object> tenLine = getRecentLine(stockCode, "10");
		Map<String, Object> twentyLine = getRecentLine(stockCode, "20");
		Map<String, Object> thirtyLine = getRecentLine(stockCode, "30");
		Map<String, Object> sixtyLine = getRecentLine(stockCode, "60");
		json.put("fiveLine", fiveLine);
		json.put("tenLine", tenLine);
		json.put("twentyLine", twentyLine);
		json.put("thirtyLine", thirtyLine);
		json.put("sixtyLine", sixtyLine);
		return json;
	}

	public Map<String, Object> getRecentLine(String stockCode, String line) {
		Map<String, Object> stockMap = new HashMap<String, Object>();
		try {
			double nowCI = 0;
			DecimalFormat df = null;
			List<Map<String, Object>> ciList = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			int n = Integer.valueOf(line);
			if ("dapan".equals(stockCode)) {
				map = stockService.getFuncDapan();
			} else {
				map = stockService.getFuncStockData(stockCode);
			}
			String nowDate = ObjUtil.toString(map.get("max"));
			List<Object> list = stockService.getAverageLineList(nowDate, stockCode, n, "recent");
			if ("dapan".equals(stockCode)) {
				df = new DecimalFormat("######0.00");
				ciList = stockService.getDapanData(nowDate);
			} else {
				df = new DecimalFormat("######0.0000");
				ciList = stockService.getStockEveryDayData("", nowDate, stockCode);
			}
			nowCI = ObjUtil.toDouble(ciList.get(0).get("closing_index"));
			stockMap.put("date", list.get(1));
			stockMap.put("index", list.get(0));
			stockMap.put("latelyCI", JSON.parseArray(ObjUtil.toString(list.get(0))).get(0));
			stockMap.put("latelyDate", JSON.parseArray(ObjUtil.toString(list.get(1))).get(0));
			stockMap.put("nowDate", nowDate);
			//stockMap.put("nowCI", df.format(nowCI));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stockMap;
	}
}
