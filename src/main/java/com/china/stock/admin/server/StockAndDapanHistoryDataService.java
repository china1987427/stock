package com.china.stock.admin.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.china.stock.admin.dao.StockDataDao;
import com.china.stock.admin.entity.MarketIndexEveryday;
import com.china.stock.admin.entity.StockIndexEveryday;
import com.china.stock.admin.entity.StockRiseorfallInfo;
import com.china.stock.common.data.StockHistoryDataConnection;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.util.StringUtil;
import com.china.stock.common.util.TimeUtil;

@Service(value = "/history")
public class StockAndDapanHistoryDataService {
	
	private static Logger log = Logger.getLogger(StockAndDapanHistoryDataService.class);
	@Autowired
	private StockDataService stockDataService;
	@Autowired
	private DapanDataService dapanDataService;
	@Autowired
	private StockDataDao stockDataDao;

	public void getHistoryData(String param, Map<String, Object> map) {
		try {
			String endDate = "";
			String startDate = "";
			boolean data = false;
			int count = ObjUtil.toInt(map.get("count"));
			String nowDate = StringUtil.getStrDate("yyyyMMdd", new Date());
			if (count >= 1) {
				String now = StringUtil.getStrDate("yyyy-MM-dd", new Date());
				String mDate = ObjectUtils.toString(map.get("max"));
				Date day = StringUtil.getDate("yyyy-MM-dd", StringUtil.getStrByLT("yyyy-MM-dd", mDate, 1));
				Date nday = StringUtil.getDate("yyyy-MM-dd", now);
				if (!now.equals(mDate)) {
					data = true;
					String oneday = StringUtil.getStrByLT("yyyyMMdd", mDate, 1);
					Calendar cal = TimeUtil.setCal(15, 30, 0);
					long l = cal.getTimeInMillis();
					if (!oneday.equals(nowDate) && nday.getTime() > day.getTime()) {
						startDate = oneday;
					} else if (oneday.equals(nowDate)) {
						if (new Date().getTime() >= new Date(l).getTime()) {
							startDate = oneday.replace("-", "");
						} else {
							data = false;
						}
					} else {
						data = false;
					}
				}
			} else {
				long date = TimeUtil.getCal(new Date(), 720);
				startDate = StringUtil.getStrDate("yyyyMMdd", new Date(date));
				data = true;
			}
			if (data) {
				endDate = nowDate;
				String historyStock = StockHistoryDataConnection.getStockHistoryData(param, startDate, endDate);
				if (!"{}".equals(historyStock)) {
					JSONArray hs = JSONObject.parseArray(historyStock);
					if (!CollectionUtils.isEmpty(hs)) {
						for (Object object : hs) {
							JSONObject json = JSON.parseObject(ObjUtil.toString(object));
							String status = json.getString("status");
							if ("0".equals(status)) {
								JSONArray jsona = json.getJSONArray("hq");
								List<StockIndexEveryday> stocks = new ArrayList<StockIndexEveryday>();
								List<MarketIndexEveryday> dapan = new ArrayList<MarketIndexEveryday>();
								List<StockRiseorfallInfo> rofInfo = new ArrayList<StockRiseorfallInfo>();
								for (int i = jsona.size() - 1; i >= 0; i--) {
									String stockCode = "";
									StockIndexEveryday stockIndex = new StockIndexEveryday();
									if (!"dapan".equals(param)) {
										stockCode = ObjUtil.toString(map.get("stockCode"));
									}
									List<String> list = (List<String>) jsona.get(i);
									String date = ObjUtil.toString(list.get(0));
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									SimpleDateFormat format = new SimpleDateFormat("EEEE");
									String dayInWeek = format.format(sdf.parse(date));
									String openingIndex = ObjUtil.toString(list.get(1));
									String closingIndex = ObjUtil.toString(list.get(2));
									String riseOrFall = ObjUtil.toString(list.get(3));// 涨跌
									String riseorfallRange = ObjUtil.toString(list.get(4));// 涨跌幅
									String amplitude = "";// （当日最高点的价格－当日最低点的价格）/昨天收盘价×100%=振幅
									String lowIndex = ObjUtil.toString(list.get(5));
									String highIndex = ObjUtil.toString(list.get(6));// 高
									String totalHand = ObjUtil.toString(list.get(7));// 总手
									String totalMoney = ObjUtil.toString(list.get(8));// 总金额
									String handover = ObjUtil.toString(list.get(9));
									String riseorfallCase = Float.valueOf(riseOrFall) <= 0 ? "0" : "1";
									stockIndex.setDate(sdf.parse(date));
									stockIndex.setOpeningIndex(openingIndex);
									stockIndex.setHighIndex(highIndex);
									stockIndex.setLowIndex(lowIndex);
									stockIndex.setClosingIndex(closingIndex);
									stockIndex.setRiseOrFall(riseOrFall);
									stockIndex.setRiseorfallCase(riseorfallCase);
									stockIndex.setAmplitude(amplitude);
									stockIndex.setDayInWeek(dayInWeek);
									stockIndex.setHandover(handover);
									stockIndex.setTotalHand(totalHand);
									stockIndex.setTotalMoney(totalMoney);// 总金额
									if ("dapan".equals(param)) {
										MarketIndexEveryday marketIndex = new MarketIndexEveryday();
										marketIndex.setMarketName("上证指数");
										marketIndex.setDate(sdf.parse(date));
										marketIndex.setNumberDate(date.replace("-", ""));
										marketIndex.setWeek(dayInWeek);
										marketIndex.setmOpeningIndex(openingIndex);
										marketIndex.setmClosingIndex(closingIndex);
										marketIndex.setmHighIndex(highIndex);
										marketIndex.setmLowIndex(lowIndex);
										marketIndex.setRiseorfallCase(riseorfallCase);
										marketIndex.setRiseorfallRange(riseorfallRange);
										marketIndex.setmAmplitude(amplitude);
										marketIndex.setmTotalhand(totalHand);
										marketIndex.setSumMoney(totalMoney);
										marketIndex.setHandover(handover);
										dapan.add(marketIndex);
									} else {
										stockIndex.setStockCode(stockCode);
										String stockName = ObjUtil.toString(map.get("stockName"));
										stockIndex.setStockName(stockName);
										stockIndex.setMarketMark(String.valueOf(map.get("marketMark")));
										stockIndex.setNumberDate(date.replace("-", ""));
										if ("星期一".equals(dayInWeek)) {
											stockIndex.setNumberOfWeek("1");
										} else if ("星期二".equals(dayInWeek)) {
											stockIndex.setNumberOfWeek("2");
										} else if ("星期三".equals(dayInWeek)) {
											stockIndex.setNumberOfWeek("3");
										} else if ("星期四".equals(dayInWeek)) {
											stockIndex.setNumberOfWeek("4");
										} else if ("星期五".equals(dayInWeek)) {
											stockIndex.setNumberOfWeek("5");
										}
										stockIndex.setRiseorfallRange(riseorfallRange);
										stocks.add(stockIndex);
										float nowRiseorfallRange = ObjUtil.toFloat(riseorfallRange.replace("%", ""));
										if (nowRiseorfallRange >= 9.9 || -10 < nowRiseorfallRange && nowRiseorfallRange < -9.9) {
											List<Map<String, Object>> stock = stockDataDao.getUpOrDownTime(stockCode);
											JSONArray ud = new JSONArray();
											JSONArray dd = new JSONArray();
											int limitdownTime = 0;
											int limitupTime = 0;
											if (!CollectionUtils.isEmpty(stock)) {
												Map<String, Object> mapdata = stock.get(0);
												if (!"".equals(mapdata.get("limitup_date"))) {
													ud = JSONArray.parseArray(ObjUtil.toString(mapdata.get("limitup_date")));
												}
												limitupTime = ObjUtil.toInt(mapdata.get("limitup_time"));
												if (!"".equals(mapdata.get("limitdown_date"))) {
													dd = JSONArray.parseArray(ObjUtil.toString(mapdata.get("limitdown_date")));
												}
												limitdownTime = ObjUtil.toInt(mapdata.get("limitdown_time"));
											}
											if (nowRiseorfallRange >= 9.9) {
												limitupTime = limitupTime + 1;
												ud.add(0, date);
											} else if (-10 < nowRiseorfallRange && nowRiseorfallRange < -9.9) {
												limitdownTime = limitdownTime + 1;
												dd.add(0, date);
											}
											// StockRiseorfallInfo rof = new
											// StockRiseorfallInfo();
											// rof.setCode(stockCode);
											// rof.setName(stockName);
											// rof.setLimitupDate(new
											// SerialClob(ud.toJSONString().toCharArray()));
											// rof.setLimitupTime(limitupTime);
											// rof.setLimitdownDate(new
											// SerialClob(dd.toJSONString().toCharArray()));
											// rof.setLimitdownTime(limitdownTime);
											if (!CollectionUtils.isEmpty(stock)) {
												stockDataDao.updateUpOrDownTime(stockCode, ud, limitupTime, dd, limitdownTime);
											} else {
												stockDataDao.saveStockRiseorfallInfo(stockCode, stockName, ud, limitupTime, dd, limitdownTime);
											}
										}
										if (!StringUtils.isEmpty(closingIndex)) {
											stockDataService.updateWeekAndMonthData(stockCode, closingIndex);
										}
									}
								}
								System.out.println(stocks);
								if ("dapan".equals(param)) {
									dapanDataService.saveToDaPanDataEveryday(dapan);
								} else {
									stockDataDao.saveToStockDataEveryday(stocks);
								}
							}
						}
					}
				} else {
					if (!"dapan".equals(param)) {
						Map<String, Object> csed = stockDataDao.checkStockEveryDayData(ObjUtil.toString(map.get("stockCode")));
						int num = ObjUtil.toInt(csed.get("count"));
						if (num >= 1) {
							stockDataDao.updateStockInfo(ObjUtil.toString(map.get("stockCode")), "yes");
						} else {
							stockDataDao.updateStockInfo(ObjUtil.toString(map.get("stockCode")), "no");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}
}
