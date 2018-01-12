package com.china.stock.common.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.china.stock.common.database.DBJdbc;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.util.StringUtil;

@Service(value = "/stockJobDao")
public class StockJobDao extends DBJdbc {
	public List<Map<String, Object>> getAllStocks(String marketMark) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select id,code,name,market_mark,time_to_market,profit_per_share,price_earning_ratio,net_asset,pb_ratio,operating_income,yearonyear_growth_oi,net_profit,yearonyear_growth_np,gross_profit_ratio,net_profit_ratio,roe,debt_ratio,total_shares,gross_value,tradable_share,tradable_value,undistributed_profit_per_share,main_business from stock_info where 1=1 ";
		sb.append(sql);
		if (!StringUtils.isEmpty(marketMark)) {
			sb.append("and market_mark=:marketMark ");
			map.put("marketMark", marketMark);
		}
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}

	public Map<String, Object> checkStockEveryDayData(String code) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select count(1) as count,max(date) as max from stock_index_everyday where 1=1 ";
		sb.append(sql);
		if (!StringUtils.isEmpty(code)) {
			sb.append("and stock_code=:stockcode ");
			map.put("stockcode", code);
		}
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForMap(sb.toString(), map);
	}

	public int saveToDaPanDataEveryday(String date, String weekOfCurrent, String openingIndex, String closingIndex, String riseOrFall,
			String amplitude, String lowIndex, String highIndex, String totalHand, String sumMoney) {
		String sql = "insert into market_index_everyday(market_name,date,number_date,week,riseorfall_case,in_time,m_opening_index,m_closing_index,m_riseorfall,m_amplitude,m_low_index,m_high_index,m_totalhand,sum) "
				+ "values(:marketName,:date,:numberDate,:week,:riseorfallCase,:inTime,:mOpeningIndex,:mClosingIndex,:mRiseorfall,:mAmplitude,"
				+ ":mLowIndex,:mHighIndex,:mTotalhand,:sum)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("marketName", "泸指");
		map.put("date", date);
		map.put("numberDate", date.replace("-", ""));
		map.put("week", weekOfCurrent);
		try {
			if (ObjUtil.toFloat(riseOrFall) > 0) {
				map.put("riseorfallCase", "1");
			} else {
				map.put("riseorfallCase", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("inTime", StringUtil.getStrDate("yyyy-MM-dd HH:mm:ss", new Date()));
		map.put("mOpeningIndex", openingIndex);
		map.put("mClosingIndex", closingIndex);
		map.put("mRiseorfall", riseOrFall);
		map.put("mAmplitude", amplitude);
		map.put("mLowIndex", lowIndex);
		map.put("mHighIndex", highIndex);
		map.put("mTotalhand", totalHand);
		map.put("sum", sumMoney);
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public int saveToStockDataEveryday(String marketMark, String stockCode, String stockName, String date, String weekOfCurrent, String openingIndex,
			String closingIndex, String riseOrFall, String amplitude, String lowIndex, String highIndex, String totalHand, String sumMoney,
			String handover) {
		String sql = "insert into stock_index_everyday(stock_code,stock_name,market_mark,riseorfall_case,date,number_date,day_in_week,import_time,opening_index,high_index,low_index,closing_index,rise_or_fall,amplitude,total_hand,sum_money,handover) "
				+ "values(:stockCode,:stockName,:marketMark,:riseorfallCase,:date,:numberDate,:dayInWeek,now(),:openingIndex,:highIndex,:lowIndex,:closingIndex,"
				+ ":riseOrFall,:amplitude,:totalHand,:sumMoney,:handover)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stockCode", stockCode);
		map.put("stockName", stockName);
		try {
			if (ObjUtil.toFloat(riseOrFall) > 0) {
				map.put("riseorfallCase", "1");
			} else {
				map.put("riseorfallCase", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("marketMark", marketMark);
		map.put("date", date);
		map.put("numberDate", date.replace("-", ""));
		map.put("dayInWeek", weekOfCurrent);
		map.put("inTime", StringUtil.getStrDate("yyyy-MM-dd HH:mm:ss", new Date()));
		map.put("openingIndex", openingIndex);
		map.put("highIndex", highIndex);
		map.put("lowIndex", lowIndex);
		map.put("closingIndex", closingIndex);
		map.put("riseOrFall", riseOrFall);
		map.put("amplitude", amplitude);
		map.put("totalHand", totalHand);
		map.put("sumMoney", sumMoney);
		map.put("handover", handover);
		int num = getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
		String updateSql = "update stock_info set is_everydata='yes' where code=?";
		getStockJdbc().getJdbcTemplate().update(updateSql, stockCode);
		return num;
	}

	public void updateStockInfo(String code, String param) {
		String sql = "";
		if ("yes".equals(param)) {
			sql = "update stock_info set is_everydata='yes' where code=?";
		} else if ("no".equals(param)) {
			sql = "update stock_info set is_everydata='no' where code=?";
		}

		getStockJdbc().getJdbcTemplate().update(sql, code);
	}

	public List<Map<String,Object>> isGetRMBExchangeRate(String today) {
		String sql = "select count(1) as count from exchange_rate where in_time=:time";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("time", today);
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}
	
}
