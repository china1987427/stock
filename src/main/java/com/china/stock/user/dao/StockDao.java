package com.china.stock.user.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.china.stock.common.database.DBJdbc;
import com.china.stock.common.tool.base.ObjUtil;

@Service(value = "/stockDao")
public class StockDao extends DBJdbc {
	public List<Map<String, Object>> getAverageLineData(Date date, String stockCode, int n) {
		String sql = "select * from stock_data where date<=:date and stock_code=:stockCode order by date desc LIMIT :n";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("date", date);
		map.put("stockCode", stockCode);
		map.put("n", n);
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}

	public int saveHistoryData(String stockCode, String stockname, String date, String weekOfCurrent, String openingIndex, String closingIndex,
			String amplitude, String riseOrFall, String lowestIndex, String highestIndex, String totalHand, String sumMoney, String handover) {
		String sql = "insert into stock_data(stock_code,stock_name,riseorfall_case,date,day_in_week,import_time,opening_index,highest_index,lowest_index,closing_index,rise_or_fall,amplitude,total_hand,sum_money,handover,volamount) "
				+ "values(:stockCode,:stockName,:riseorfallCase,:date,:dayInWeek,now(),:openingIndex,:highestIndex,:lowestIndex,:closingIndex,"
				+ ":riseOrFall,:amplitude,:totalHand,:sumMoney,:handover,:volamount)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stockCode", stockCode);
		map.put("stockName", stockname);
		if (Float.valueOf(amplitude) > 0) {
			map.put("riseorfallCase", "1");
		} else {
			map.put("riseorfallCase", "0");
		}
		map.put("date", date);
		map.put("dayInWeek", weekOfCurrent);
		map.put("openingIndex", openingIndex);
		map.put("highestIndex", highestIndex);
		map.put("lowestIndex", lowestIndex);
		map.put("closingIndex", closingIndex);
		map.put("riseOrFall", riseOrFall);
		map.put("amplitude", amplitude);
		map.put("totalHand", totalHand);
		map.put("sumMoney", sumMoney);
		map.put("handover", handover);
		map.put("volamount", "");
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public int deleteStock(String stockCode) {
		String sql = "delete from stock_data where stock_code=?";
		return getStockJdbc().getJdbcTemplate().update(sql, stockCode);
	}

	public List<Map<String, Object>> serach(String stockMarket, String serachValue) {
		String sql = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (serachValue.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
			sql = "select DISTINCT  * from stock_info where code like '" + serachValue + "%' and market_mark=:marketMark";
		} else {
			sql = "select DISTINCT  * from stock_info where name like '" + serachValue + "%' and market_mark=:marketMark";
		}
		map.put("marketMark", stockMarket);
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}

	public int deleteData() {
		String sql = "delete from market_index_everyday";
		return getStockJdbc().getJdbcTemplate().update(sql);
	}

	public int saveToStockIndex(String date, String week, String openingIndex, String highIndex, String lowIndex, String closingIndex, String riseOrFall,
			String increase, Object amplitude, String totalHand, String sumMoney) {
		String sql = "";
		Map<String, Object> map = null;
		try {
			sql = "insert into market_index_everyday(date,riseorfall_case,day_in_week,import_time,opening_index,high_index,low_index,closing_index,rise_or_fall,increase,amplitude,total_hand,sum_money) "
					+ "values(:date,:riseorfallCase,:week,now(),:openingIndex,:highIndex,:lowIndex,:closingIndex,:riseOrFall,:increase,:amplitude,:totalHand,:sumMoney)";
			map = new HashMap<String, Object>();
			map.put("date", date);
			map.put("week", week);
			map.put("openingIndex", openingIndex);
			map.put("highIndex", highIndex);
			map.put("lowIndex", lowIndex);
			map.put("closingIndex", closingIndex);
			map.put("riseOrFall", riseOrFall);
			map.put("amplitude", amplitude);
			map.put("increase", increase);
			map.put("totalHand", totalHand);
			map.put("sumMoney", sumMoney);
			if (ObjUtil.toFloat(increase) > 0) {
				map.put("riseorfallCase", "1");
			} else {
				map.put("riseorfallCase", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public List<Map<String, Object>> getStockData(String stockCode) {
		StringBuilder sb = new StringBuilder();
		String sql = "select * from stock_info where 1=1 ";
		Map<String, Object> map = new HashMap<String, Object>();
		sb.append(sql);
		if (!StringUtils.isEmpty(stockCode)) {
			sb.append("and code=:stockCode ");
			map.put("stockCode", stockCode);
		}
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sb.toString(), map);
	}

	public List<Map<String, Object>> getDapanData(String date) {
		StringBuilder sb = new StringBuilder();
		String sql = "select market_name,date,number_date,day_in_week,riseorfall_case,in_time,opening_index,closing_index,rise_or_fall,CAST(replace(amplitude,'%','') AS DECIMAL(11,2)) as amplitude,low_index,high_index,total_hand,sum_money from market_index_everyday ";
		sb.append(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(date)) {
			sb.append("where date=:date ");
			map.put("date", date);
		}
		sb.append(" group by date order by date asc");
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sb.toString(), map);
	}

	public List<Map<String, Object>> getDapanData(String date, String sqlParam) {
		StringBuilder sb = new StringBuilder();
		String sql = "select market_name,date,number_date,day_in_week,riseorfall_case,in_time,opening_index,closing_index,rise_or_fall,CAST(replace(amplitude,'%','') AS DECIMAL(11,2)) as amplitude,low_index,high_index,total_hand,sum_money from market_index_everyday ";
		sb.append(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(date)) {
			sb.append("where date=:date ");
			map.put("date", date);
		}
		if (!StringUtils.isEmpty(sqlParam)) {
			sb.append(sqlParam);
		}
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sb.toString(), map);
	}

	public Map<String, Object> getFuncDapan() {
		String sql = "select count(1) as count,max(date) as max,min(date) as min from market_index_everyday ";
		Map<String, Object> map = new HashMap<String, Object>();
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForMap(sql, map);
	}

	public List<Map<String, Object>> getStockEveryDayData(String sqlParam, String date, String stockCode) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select id,stock_code,stock_name,market_mark,riseorfall_case,date,rise_or_fall,opening_index,high_index,low_index,closing_index,day_in_week,"
				+ "amplitude,total_hand,total_money,handover,number_of_week from stock_index_everyday where 1=1 ";
		sb.append(sql);
		if (!StringUtils.isEmpty(stockCode)) {
			sb.append("and stock_code=:stockCode ");
			map.put("stockCode", stockCode);
		}
		if (!StringUtils.isEmpty(date)) {
			sb.append(" and date=:date ");
			map.put("date", date);
		}
		if (!StringUtils.isEmpty(sqlParam)) {
			sb.append(sqlParam);
		}
		if (!sqlParam.endsWith("180")) {
			sql = "select * from (" + sb.toString();
		} else {
			sql = sb.toString();
		}
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}

	public Map<String, Object> getFuncStockData(String stockCode) {
		String sql = "select count(1) as count,max(date) as max,min(date) as min from stock_index_everyday where 1=1 and stock_code=:stockCode ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stockCode", stockCode);
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForMap(sql, map);
	}

	public int saveToMyStock(String mark, String stockCode, String userId, String username, String stockName) {
		String sql = "insert into my_stock(user_id,user_name,stock_code,stock_name,time,market) "
				+ "values(:userId,:username,:stockCode,:stockName,now(),:market)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("username", username);
		map.put("stockCode", stockCode);
		map.put("stockName", stockName);
		map.put("market", mark);
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public List<Map<String, Object>> getDataFromMyStock(String stockCode, String username) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select s.user_id,s.user_name,stock_code,stock_name,market,a.* from my_stock s,account a where s.user_id=a.user_id ";
		sb.append(sql);
		if (!StringUtils.isEmpty(stockCode)) {
			sb.append(" and s.stock_code=:stockCode ");
			map.put("stockCode", stockCode);
		}
		if (!StringUtils.isEmpty(username)) {
			sb.append(" and s.user_name=:username ");
			map.put("username", username);
		}
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sb.toString(), map);
	}

	public List<Map<String, Object>> getFuncDataFromMyStock(String stockCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select count(1) as count from my_stock s,account a where s.user_id=a.user_id and stock_code=:stockCode";
		map.put("stockCode", stockCode);
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}
}
