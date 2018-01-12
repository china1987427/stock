package com.china.stock.common.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.china.stock.admin.dao.StockDataDao;
import com.china.stock.admin.server.AdminService;
import com.china.stock.admin.server.StockDataService;
import com.china.stock.common.data.BaiDuStockCurrent;
import com.china.stock.common.data.BaiDuWeather;
import com.china.stock.common.data.HgtTen;
import com.china.stock.common.data.SearchNews;
import com.china.stock.common.data.WaiWangIP;
import com.china.stock.common.thread.HtmlUtil;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.tool.base.RedisUtil;
import com.china.stock.common.tool.entity.MyException;
import com.china.stock.common.util.StringUtil;

@Service
public class CommonDataService {
	private static int timeout = 5000;
	@Autowired
	private StockDataDao stockDataDao;
	@Autowired
	private AdminService adminService;
	@Autowired
	private StockDataService stockDataService;

	public static void main(String[] args) {
	}
	
	/**
	 * 在使用的从和讯网获取股票的代码和名称
	 * @param market
	 */
	
	public void saveStocks(String market) {
		try {
			URL ur = null;
			int page = 1;
			String html = "";
			String marketMark = "";
			boolean isData = false;
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> updateData = new ArrayList<Map<String, Object>>();
			//循环是处理页数
			do {
				String stockData = "";
				if ("1".equals(market) || "2".equals(market)) {// 1表示上证A股，2表示深证A股
					html = "http://quote.tool.hexun.com/hqzx/quote.aspx?type=2&market=marketNum&sorttype=0&updown=down&page=pageNum&count=50";
					html = html.replace("marketNum", market).replace("pageNum", page + "");
					marketMark = "1".equals(market) ? "1" : "2";
				} else if ("Y0002".equals(market) || "Y0003".equals(market)) {// 3表示中小板(Y0002)，4表示创业板(Y0003
																				// )
					html = "http://quote.tool.hexun.com/hqzx/stocktype.aspx?columnid=5500&type_code=market&sorttype=0&updown=down&page=pageNum&count=50";
					html = html.replace("market", market).replace("pageNum", page + "");
					marketMark = "Y0002".equals(market) ? "3" : "4";
				}
				try {
					ur = new URL(html);
					BufferedReader reader = new BufferedReader(new InputStreamReader(ur.openStream(), "GBK"));
					String line;
					while ((line = reader.readLine()) != null) {
						stockData += line;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				isData = stockData.indexOf("[[") >= 0;
				if (isData) {
					int a = stockData.indexOf("[[");
					int b = stockData.indexOf("]]");
					String stocks = stockData.substring(a, b) + "]]";
					JSONArray array = JSONArray.parseArray(stocks);
					for (int i = 0; i < array.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						Map<String, Object> updatemap = new HashMap<String, Object>();
						JSONArray json = array.getJSONArray(i);
						String stockCode = json.getString(0);
						String stockName = json.getString(1);
						// System.out.println(stockName + "/" + stockCode + "/"
						// + market);
						List<Map<String, Object>> info = stockDataDao.getStockInfo(stockCode);
						if (!CollectionUtils.isEmpty(info)) {
							Map<String, Object> stockInfo = info.get(0);
							int stockMarketMark = ObjUtil.toInt(stockInfo.get("market_mark"));
							//处理股票同时存在2,3或2,4市场
							updatemap.put("marketMark", ObjUtil.toInt(marketMark) > stockMarketMark ? stockMarketMark + "," + marketMark : marketMark + ","
									+ stockMarketMark);
							updatemap.put("stockCode", stockCode);
							updateData.add(updatemap);
							continue;
						} 
						map.put("marketMark", marketMark);
						map.put("stockCode", stockCode);
						map.put("stockName", stockName);
						list.add(map);
					}
					page++;
				}
			} while (isData);
			System.out.println(market + "/" + list.size());
			stockDataService.saveAllStock(list);
			//更新同处于一个市场的股票的marketMark
			stockDataService.updateStock(updateData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 在使用的从和讯网获取股票的相关信息
	 */
	public void saveStocksInfo() {
		try {
			String stock_html = "http://stockdata.stock.hexun.com/stockCode.shtml";
			String data_html = "http://stockdata.stock.hexun.com/gszl/sstockCode.shtml";
			List<Map<String, Object>> stocks = adminService.getAllStocks();
			for (int i = 0; i < stocks.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				Map<String, Object> stock = stocks.get(i);
				String isget_info = stock.get("isget_info") == null ? "" : ObjUtil.toString(stock.get("isget_info"));
				if ("yes".equals(isget_info)) {
					continue;
				}
				String stockCode = ObjUtil.toString(stock.get("code"));
				String stockName = ObjUtil.toString(stock.get("name"));
				String market = ObjUtil.toString(stock.get("market_mark"));

				long time1 = System.currentTimeMillis();
				Document doc = HtmlUtil.parserHtml(stock_html.replace("stockCode", stockCode));
				int m = 1;
				do {
					timeout = (m == 1) ? timeout : timeout + 5000 * (m++);
					doc = HtmlUtil.parserHtml(stock_html.replace("stockCode", stockCode));
					if (m == 5) {
						break;
					}
				} while (doc == null);
				Elements ele = doc.getElementsByClass("box6").select("tr");
				String priceEarningsRatio = doc.getElementById("q_profitrate").text();// 市盈率
				String belongToArea = ele.isEmpty() ? "" : ele.get(0).child(1).select("a").text();// 所属地区
				String profitPerShare = ele.isEmpty() ? "" : ele.get(0).child(3).text();// 每股收益
				String netAssetPerShare = ele.isEmpty() ? "" : ele.get(1).child(3).text();// 每股净资产(元)
				String totalStockIssue = ele.isEmpty() ? "" : ele.get(2).child(1).text();// 总股本(亿)
				String netAssetYieldRate = ele.isEmpty() ? "" : ele.get(2).child(3).text();// 净资产收益率(%)
				String outstandingAShares = ele.isEmpty() ? "" : ele.get(3).child(1).text();// 流通A股(亿)
				String mainBusinessIncomeGrowthRate = ele.isEmpty() ? "" : ele.get(3).child(3).text();// 主营收入增长(%)
				String newDistributionPlans = ele.isEmpty() ? "" : ele.get(4).child(1).text();// 最新分配预案(17年前3季)
				String netProfitsGrowthRate = ele.isEmpty() ? "" : ele.get(4).child(3).text();// 净利润增长率(%)
				String undistributedProfitPerShare = ele.isEmpty() ? "" : ele.get(5).child(1).text();// 每股未分配利润
				String belongToIndustry = ele.isEmpty() ? "" : ele.get(7).child(1).text();// 所属行业
				String industryRanking = ele.isEmpty() ? "" : ele.get(7).child(2).text();// 行业排名
				String ICBIndustry = ele.isEmpty() ? "" : ele.get(8).child(1).text();// ICB行业
				String ICBIndustryRanking = ele.isEmpty() ? "" : ele.get(8).child(2).text();// ICB行业排名
				long time2 = System.currentTimeMillis();
				Document docData = HtmlUtil.parserHtml(data_html.replace("stockCode", stockCode));
				Elements begin = docData.getElementsByClass("begin");
				String dateOfEstablishment = begin.get(5).siblingElements().text();// 成立日期
				String belongToConcept = begin.get(7).siblingElements().text();// 所属概念
				String registeredAssets = begin.get(14).siblingElements().text();// 注册资本
				String timeToMarket = begin.get(20).siblingElements().text();// 上市日期
				String issuePrice = begin.get(26).siblingElements().text();// 发行价
				String issuePriceEarningRatio = begin.get(31).siblingElements().text();// 发行市盈率
				String newPriceEarningRatio = begin.get(32).siblingElements().text();// 最新市盈率
				long time3 = System.currentTimeMillis();
				long num1 = time2 - time1;
				long num2 = time3 - time2;
				System.out.println(i + "/" + stockName + "/" + stockCode + "/" + market + "/" + num1 + "/" + num2);
				map.put("stockCode", stockCode);
				map.put("priceEarningsRatio", priceEarningsRatio);
				map.put("belongToArea", belongToArea);
				map.put("profitPerShare", profitPerShare);
				map.put("netAssetPerShare", netAssetPerShare);
				map.put("totalStockIssue", totalStockIssue);
				map.put("netAssetYieldRate", netAssetYieldRate);
				map.put("outstandingAShares", outstandingAShares);
				map.put("mainBusinessIncomeGrowthRate", mainBusinessIncomeGrowthRate);
				map.put("newDistributionPlans", newDistributionPlans);
				map.put("netProfitsGrowthRate", netProfitsGrowthRate);
				map.put("undistributedProfitPerShare", undistributedProfitPerShare);
				map.put("belongToIndustry", belongToIndustry);
				map.put("industryRanking", industryRanking);
				map.put("ICBIndustry", ICBIndustry);
				map.put("ICBIndustryRanking", ICBIndustryRanking);
				map.put("dateOfEstablishment", "--".equals(dateOfEstablishment) || "".equals(dateOfEstablishment) ? null : dateOfEstablishment);
				map.put("belongToConcept", belongToConcept);
				map.put("registeredAssets", registeredAssets);
				map.put("timeToMarket", timeToMarket);
				map.put("issuePrice", issuePrice);
				map.put("issuePriceEarningRatio", issuePriceEarningRatio);
				map.put("newPriceEarningRatio", newPriceEarningRatio);
				stockDataService.managerAllstock(map);
			}
		} catch (MyException e) {
			e.printStackTrace();
		}
	}

	public JSONArray getStock() {
		JSONArray jsona = new JSONArray();
		JSONArray arrayjson = new JSONArray();
		BaiDuStockCurrent bdsc = new BaiDuStockCurrent();
		String data = bdsc.request("000001", "1");
		JSONObject index = JSON.parseObject(data);
		JSONArray shanghaiarray = index.getJSONObject("retData").getJSONArray("stockinfo");
		for (Object obj : shanghaiarray) {
			JSONObject shanghai = JSON.parseObject(obj + "");
			Map<String, Object> map = (Map<String, Object>) obj;
			map.put("mark", "shanghai");
			jsona.add(shanghaiarray.get(0));
		}
		String shenzhenData = bdsc.request("399001", "2");
		JSONObject jsonobject = JSON.parseObject(shenzhenData);
		JSONArray shenzhenArray = jsonobject.getJSONObject("retData").getJSONArray("stockinfo");
		for (Object obj : shenzhenArray) {
			JSONObject shenzhen = JSON.parseObject(obj + "");
			Map<String, Object> map = (Map<String, Object>) obj;
			map.put("mark", "shenzhen");
			jsona.add(shenzhenArray.get(0));
		}
		String gemData = bdsc.request("399006", "2");
		JSONObject json = JSON.parseObject(gemData);
		JSONArray array = json.getJSONObject("retData").getJSONArray("stockinfo");
		for (Object obj : array) {
			JSONObject gem = JSON.parseObject(obj + "");
			Map<String, Object> map = (Map<String, Object>) obj;
			map.put("mark", "gem");
			jsona.add(array.get(0));
		}
		arrayjson.add(jsona);
		JSONObject DJI = index.getJSONObject("retData").getJSONObject("market").getJSONObject("DJI");
		JSONObject IXIC = index.getJSONObject("retData").getJSONObject("market").getJSONObject("IXIC");
		JSONObject INX = index.getJSONObject("retData").getJSONObject("market").getJSONObject("INX");
		JSONObject HSI = index.getJSONObject("retData").getJSONObject("market").getJSONObject("HSI");
		JSONArray global = new JSONArray();
		global.add(DJI);
		global.add(IXIC);
		global.add(INX);
		global.add(HSI);
		arrayjson.add(global);
		return arrayjson;
	}

	public List<Object> getWeather() {
		String addr = WaiWangIP.getWebIp();
		String cityname = addr.substring(addr.indexOf("省") + 1, addr.indexOf("市"));
		String weatherData = BaiDuWeather.request(cityname);
		JSONObject retData = JSON.parseObject(weatherData);
		JSONObject weather = retData.getJSONObject("retData");
		List<Object> list = new ArrayList<Object>();
		list.add(weather);
		return list;
	}

	public JSONArray getNews(String page) {
		JSONArray newsContent = new JSONArray();
		try {
			String date = RedisUtil.get("date");
			if (StringUtils.isEmpty(date)) {
				date = StringUtil.getStrDate("yyyy-MM-dd", new Date());
				RedisUtil.set("date", date);
			}
			Date d = StringUtil.getDate("yyyy-MM-dd", StringUtil.getStrDate("yyyy-MM-dd", new Date()));
			if (d.getTime() > StringUtil.getDate("yyyy-MM-dd", date).getTime()) {
				RedisUtil.del("date");
				RedisUtil.del("news_" + date + "_" + page);
			}
			String news = RedisUtil.get("news_" + date + "_" + page);
			if (StringUtils.isEmpty(news)) {
				news = SearchNews.request(page);
				RedisUtil.set("news_" + date + "_" + page, news);
			}
			JSONObject newsjson = JSON.parseObject(news);
			JSONObject pagebean = newsjson.getJSONObject("showapi_res_body").getJSONObject("pagebean");
			// String allNum = pagebean.getString("allNum");
			// String allPages = pagebean.getString("allPages");
			newsContent = JSON.parseArray(ObjUtil.toString(pagebean.get("contentlist")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newsContent;
	}

	public String getDate() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 可以方便地修改日期格式
		String date = sdf.format(now);
		SimpleDateFormat weekSdf = new SimpleDateFormat("EEEE");
		String week = weekSdf.format(now);
		return date + "date" + week;

	}

	public JSONArray getHgtTen() {
		JSONArray hgtTen = new JSONArray();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfWeek = new SimpleDateFormat("EEEE");
			String week = sdfWeek.format(new Date());
			String date = "";
			if ("星期一".equals(week)) {
				long d = new Date().getTime() - 3 * 24 * 60 * 60 * 1000;
				date = sdf.format(new Date(d));
			} else if ("星期日".equals(week)) {
				long d = new Date().getTime() - 2 * 24 * 60 * 60 * 1000;
				date = sdf.format(new Date(d));
			} else {
				long d = new Date().getTime() - 24 * 60 * 60 * 1000;
				date = sdf.format(new Date(d));
			}
			String dataTen = HgtTen.request(date);
			JSONObject dataRow = JSON.parseObject(dataTen);
			hgtTen = dataRow.getJSONArray("rows");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hgtTen;
	}

}
