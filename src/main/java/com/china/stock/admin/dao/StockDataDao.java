package com.china.stock.admin.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.china.stock.admin.entity.Account;
import com.china.stock.admin.entity.Finance;
import com.china.stock.admin.entity.StockIndexEveryday;
import com.china.stock.common.database.DBJdbc;
import com.china.stock.common.database.util.DBAgent;
import com.china.stock.common.database.util.JDBCAgent;

@Service(value = "/stockDataDao")
public class StockDataDao extends DBJdbc {
	public void managerAllstock(Map<String, Object> map) {
		String sql = "update stock_info set belong_to_area=:belongToArea,time_to_market=:timeToMarket,profit_per_share=:profitPerShare,net_asset_per_share=:netAssetPerShare,"
				+ "total_stock_issue=:totalStockIssue,net_asset_yield_rate=:netAssetYieldRate,outstanding_a_shares=:outstandingAShares,main_business_income_growth_rate=:mainBusinessIncomeGrowthRate,"
				+ "new_distribution_plans=:newDistributionPlans,net_profits_growth_rate=:netProfitsGrowthRate,undistributed_profit_per_share=:undistributedProfitPerShare,price_earning_ratio=:priceEarningsRatio,"
				+ "date_of_establishment=:dateOfEstablishment,belong_to_industry=:belongToIndustry,industry_ranking=:industryRanking,belong_to_concept=:belongToConcept,ICB_industry=:ICBIndustry,"
				+ "ICB_industry_ranking=:ICBIndustryRanking,registered_assets=:registeredAssets,issue_price=:issuePrice,issue_price_earning_ratio=:issuePriceEarningRatio,new_price_earning_ratio=:newPriceEarningRatio where code=:stockCode";
		getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
		getStockJdbc().getJdbcTemplate().update("update stock_info set isget_info='yes' where code=?", map.get("stockCode"));
		// getStockJdbc().getJdbcTemplate().batchUpdate(sql, new
		// BatchPreparedStatementSetter() {
		// @Override
		// public void setValues(PreparedStatement ps, int i) throws
		// SQLException {
		// Map<String, Object> map = list.get(i);
		// ps.setString(1, String.valueOf(map.get("belongToArea")));
		// }
		// });
	}

	public void managerAllstock(final List<Map<String, Object>> list) {
		String sql = "insert into stock_info(belong_to_area,time_to_market,profit_per_share,net_asset_per_share,"
				+ "total_stock_issue,net_asset_yield_rate,outstanding_a_shares,main_business_income_growth_rate,new_distribution_plans,net_profits_growth_rate,undistributed_profit_per_share,"
				+ "price_earning_ratio,date_of_establishment,belong_to_industry,industry_ranking,belong_to_concept,ICB_industry,ICB_industry_ranking,registered_assets,issue_price,issue_price_earning_ratio,new_price_earning_ratio) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		getStockJdbc().getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

			public int getBatchSize() {
				return list.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Map<String, Object> map = list.get(i);
				ps.setString(1, String.valueOf(map.get("belongToArea")));
				ps.setString(2, String.valueOf(map.get("timeToMarket")));
				ps.setString(3, String.valueOf(map.get("profitPerShare")));
				ps.setString(4, String.valueOf(map.get("netAssetPerShare")));
				ps.setString(5, String.valueOf(map.get("totalStockIssue")));
				ps.setString(6, String.valueOf(map.get("netAssetYieldRate")));
				ps.setString(7, String.valueOf(map.get("outstandingAShares")));
				ps.setString(8, String.valueOf(map.get("mainBusinessIncomeGrowthRate")));
				ps.setString(9, String.valueOf(map.get("newDistributionPlans")));
				ps.setString(10, String.valueOf(map.get("netProfitsGrowthRate")));
				ps.setString(11, String.valueOf(map.get("undistributedProfitPerShare")));
				ps.setString(12, String.valueOf(map.get("priceEarningsRatio")));
				ps.setString(13, String.valueOf(map.get("dateOfEstablishment")));
				ps.setString(14, String.valueOf(map.get("belongToIndustry")));
				ps.setString(15, String.valueOf(map.get("industryRanking")));
				ps.setString(16, String.valueOf(map.get("belongToConcept")));
				ps.setString(17, String.valueOf(map.get("ICBIndustry")));
				ps.setString(18, String.valueOf(map.get("ICBIndustryRanking")));
				ps.setString(19, String.valueOf(map.get("registeredAssets")));
				ps.setString(20, String.valueOf(map.get("issuePrice")));
				ps.setString(21, String.valueOf(map.get("issuePriceEarningRatio")));
				ps.setString(22, String.valueOf(map.get("newPriceEarningRatio")));
			}
		});
	}

	/**
	 * 在使用的从和讯网获取股票的代码和名称
	 * 
	 * @param list
	 */
	public void saveAllStock(final List<Map<String, Object>> list) {
		String sql = "insert into stock_info(code,name,market_mark,isget_everyday_data) values(?,?,?,'no')";
		getStockJdbc().getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

			public int getBatchSize() {
				return list.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Map<String, Object> map = list.get(i);
				ps.setString(1, String.valueOf(map.get("stockCode")));
				ps.setString(2, String.valueOf(map.get("stockName")));
				ps.setString(3, String.valueOf(map.get("marketMark")));
			}
		});
	}

	/**
	 * 更新同处于一个市场的股票的marketMark
	 * 
	 * @param list
	 */
	public void updateStock(final List<Map<String, Object>> list) {
		String sql = "update stock_info set market_mark=? where code=?";
		getStockJdbc().getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

			public int getBatchSize() {
				return list.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Map<String, Object> map = list.get(i);
				ps.setString(1, String.valueOf(map.get("marketMark")));
				ps.setString(2, String.valueOf(map.get("stockCode")));
			}
		});
	}
	
	public int saveToStockDataEveryday(String stockCode, String stockName, String marketMark, String riseorfallCase, String date, String numberDate,
			String dayInWeek, String openingIndex, String highIndex, String lowIndex, String closingIndex, String riseorfallRange, String riseOrFall,
			String amplitude, String volumeOfBusiness, String handover) {
		String sql = "insert into stock_index_everyday(stock_code,stock_name,market_mark,riseorfall_case,date,number_date,day_in_week,"
				+ "number_of_week,import_time,opening_index,high_index,low_index,closing_index,rise_or_fall_range,rise_or_fall,amplitude,"
				+ "volume_of_business,handover) "
				+ "values(:stockCode,:stockName,:marketMark,:riseorfallCase,:date,:numberDate,:dayInWeek,:numberOfWeek,now(),:openingIndex,:highIndex,:lowIndex,:closingIndex,"
				+ ":riseorfallRange,:riseOrFall,:amplitude,:volumeOfBusiness,:handover)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stockCode", stockCode);
		map.put("stockName", stockName);
		map.put("marketMark", marketMark);
		map.put("riseorfallCase", riseorfallCase);
		map.put("date", date);
		map.put("numberDate", numberDate);
		map.put("dayInWeek", dayInWeek);
		if ("星期一".equals(dayInWeek)) {
			map.put("numberOfWeek", 1);
		} else if ("星期二".equals(dayInWeek)) {
			map.put("numberOfWeek", 2);
		} else if ("星期三".equals(dayInWeek)) {
			map.put("numberOfWeek", 3);
		} else if ("星期四".equals(dayInWeek)) {
			map.put("numberOfWeek", 4);
		} else if ("星期五".equals(dayInWeek)) {
			map.put("numberOfWeek", 5);
		}
		map.put("openingIndex", openingIndex);
		map.put("highIndex", highIndex);
		map.put("lowIndex", lowIndex);
		map.put("closingIndex", closingIndex);
		map.put("riseorfallRange", riseorfallRange);
		map.put("riseOrFall", riseOrFall);
		map.put("amplitude", amplitude);
		map.put("volumeOfBusiness", volumeOfBusiness);
		map.put("handover", handover);
		int num = getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
		String updateSql = "update stock_info set isget_everyday_data='yes' where code=?";
		getStockJdbc().getJdbcTemplate().update(updateSql, stockCode);
		return num;
	}

	public void saveToStockDataEveryday(List<StockIndexEveryday> stockIndex) {
		String sql = "insert into stock_index_everyday(stock_code,stock_name,market_mark,riseorfall_case,date,number_date,day_in_week,"
				+ "number_of_week,import_time,opening_index,high_index,low_index,closing_index,rise_or_fall_range,rise_or_fall,amplitude,handover,total_hand,total_money) "
				+ "values(:stockCode,:stockName,:marketMark,:riseorfallCase,:date,:numberDate,:dayInWeek,:numberOfWeek,now(),:openingIndex,:highIndex,:lowIndex,:closingIndex,"
				+ ":riseorfallRange,:riseOrFall,:amplitude,:handover,:totalHand,:totalMoney)";
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(stockIndex.toArray());// 参数与实体类相同，与sql中的参数相同
		getStockJdbc().getNamedParameterJdbcTemplate().batchUpdate(sql, batch);
		String updateSql = "update stock_info set isget_everyday_data='yes' where code=:stockCode";
		getStockJdbc().getNamedParameterJdbcTemplate().batchUpdate(updateSql, batch);
	}

	public Map<String, Object> checkStockEveryDayData(String code) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select count(1) as count,max(date) as max,min(date) as min from stock_index_everyday where 1=1 ";
		sb.append(sql);
		if (!StringUtils.isEmpty(code)) {
			sb.append("and stock_code=:stockcode ");
			map.put("stockcode", code);
		}
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForMap(sb.toString(), map);
	}

	public void updateStockInfo(String code, String param) {
		String sql = "";
		if ("yes".equals(param)) {
			sql = "update stock_info set isget_everyday_data='yes' where code=?";
		} else if ("no".equals(param)) {
			sql = "update stock_info set isget_everyday_data='no' where code=?";
		}

		getStockJdbc().getJdbcTemplate().update(sql, code);
	}

	public List<Map<String, Object>> getSingleStock(String code, String date) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select id,stock_code,stock_name,riseorfall_case,date,number_date,day_in_week,import_time,opening_index,high_index,low_index,closing_index,CAST(rise_or_fall as DECIMAL(6,2)) as rise_or_fall,CAST(replace(amplitude,'%','') AS DECIMAL(11,2)) as amplitude,total_hand,handover from stock_index_everyday where 1=1 ";
		sb.append(sql);
		if (!StringUtils.isEmpty(date)) {
			sb.append("and date=:date ");
			map.put("date", date);
		}
		if (!StringUtils.isEmpty(code)) {
			sb.append("and stock_code=:stockcode ");
			map.put("stockcode", code);
			sb.append("order by date desc");
		} else {
			sb.append("order by amplitude desc");
		}
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sb.toString(), map);
	}

	public List<Map<String, Object>> raisingLimitPredict(String code, String date) {
		String sql = "update stock_index_everyday sie set sie.market_mark=(select market_mark from stock_info si where si.code=sie.stock_code) where sie.stock_code=?";
		getStockJdbc().getJdbcTemplate().update(sql, code);
		return null;
	}

	public void changeStatus(String param, String isGet) {
		String sql = "";
		if ("yes".equals(isGet)) {
			sql = "update info set is_get_data='yes' where dapan_or_stock=?";
		} else if ("no".equals(isGet)) {
			sql = "update info set is_get_data='no' where dapan_or_stock=?";
		}
		getStockJdbc().getJdbcTemplate().update(sql, param);
	}

	public int stockIntervalRisefall(String theDate, String week, JSONArray json1, JSONArray json2, JSONArray json3, JSONArray json4, JSONArray json5,
			JSONArray json6, JSONArray json7, JSONArray json8, JSONArray json9, JSONArray json10, JSONArray json11, JSONArray json12, JSONArray json13,
			JSONArray json14, JSONArray json15, JSONArray json16, JSONArray json17, JSONArray json18, JSONArray json19, JSONArray json20, JSONArray json21) {
		String sql = "insert into stock_interval_risefall(date,update_time,week,minusTen_to_minusNine,minusNine_to_minusEight,minusEight_to_minusSeven,minusSeven_to_minusSix,"
				+ "minusSix_to_minusFive,minusFive_to_minusFour,minusFour_to_minusThree,minusThree_to_minusTwo,minusTwo_to_minusOne,minusOne_to_zero,zero_to_one,"
				+ "one_to_two,two_to_three,three_to_four,four_to_five,five_to_six,six_to_seven,seven_to_eight,eight_to_nine,nine_to_ten,greater_than_ten) "
				+ "values(:date,now(),:week,:minusTenToMinusNine,:minusNineToMinusEight,:minusEightToMinusSeven,:minusSevenToMinusSix,"
				+ ":minusSixToMinusFive,:minusFiveToMinusFour,:minusFourToMinusThree,"
				+ ":minusThreeToMinusTwo,:minusTwoToMinusOne,:minusOneToZero,:zeroToOne,"
				+ ":oneToTwo,:twoToThree,:threeToFour,:fourToFive,:fiveToSix,:sixToSeven," + ":sevenToEight,:eightToNine,:nineToTen,:greaterThanTen)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("date", theDate);
		map.put("week", week);
		map.put("minusTenToMinusNine", json1.toString());
		map.put("minusNineToMinusEight", json2.toString());
		map.put("minusEightToMinusSeven", json3.toString());
		map.put("minusSevenToMinusSix", json4.toString());
		map.put("minusSixToMinusFive", json5.toString());
		map.put("minusFiveToMinusFour", json6.toString());
		map.put("minusFourToMinusThree", json7.toString());
		map.put("minusThreeToMinusTwo", json8.toString());
		map.put("minusTwoToMinusOne", json9.toString());
		map.put("minusOneToZero", json10.toString());
		map.put("zeroToOne", json11.toString());
		map.put("oneToTwo", json12.toString());
		map.put("twoToThree", json13.toString());
		map.put("threeToFour", json14.toString());
		map.put("fourToFive", json15.toString());
		map.put("fiveToSix", json16.toString());
		map.put("sixToSeven", json17.toString());
		map.put("sevenToEight", json18.toString());
		map.put("eightToNine", json19.toString());
		map.put("nineToTen", json20.toString());
		map.put("greaterThanTen", json21.toString());
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public void saveLimitUpOrDown(String theDate, String week, JSONArray json1, JSONArray json21) {
		String sql = "insert into limit_up_down(date,week,limitup_stocks,limitup_number,limitdown_stocks,limitdown_number,in_time) "
				+ "values(:date,:week,:limitupStocks,:limitupNumber,:limitdownStocks,:limitdownNumber,now())";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("date", theDate);
		map.put("week", week);
		map.put("limitupStocks", json21.toString());
		map.put("limitupNumber", json21.size());
		map.put("limitdownStocks", json1.toString());
		map.put("limitdownNumber", json1.size());
		getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public Map<String, Object> changeStatus(String param) {
		String sql = "select id,is_get_data from info where dapan_or_stock=?";
		return getStockJdbc().getJdbcTemplate().queryForMap(sql, param);
	}

	public int saveChangeRate(String currencyCode, String currencyName, String newPrice, String changeAmount, String riseorfallRange, String opening) {
		String sql = "insert into exchange_rate(currency_type,currency_code,currency_name,new_price,change_amount,riseorfall_range,opening,in_time) "
				+ "values('CNY',:currencyCode, :currencyName, :newPrice,:changeAmount, :riseorfallRange, :opening,now())";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currencyCode", currencyCode);
		map.put("currencyName", currencyName);
		map.put("newPrice", newPrice);
		map.put("changeAmount", changeAmount);
		map.put("riseorfallRange", riseorfallRange);
		map.put("opening", opening);
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public List<Map<String, Object>> getStockInfo(String code) {
		String sql = "select * from stock_info where code=?";
		return getStockJdbc().getJdbcTemplate().queryForList(sql, code);
	}

	public int updateUpOrDownTime(String code, JSONArray limitupDate, int limitupTime, JSONArray limitdownDate, int limitdownTime) {
		String sql = "update stock_riseorfall_info set limitup_date=:limitupDate,limitup_time=:limitupTime,limitdown_date=:limitdownDate,limitdown_time=:limitdownTime,update_time=now() where code=:code";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("limitupDate", limitupDate.toString());
		map.put("limitupTime", limitupTime);
		map.put("limitdownDate", limitdownDate.toString());
		map.put("limitdownTime", limitdownTime);
		map.put("code", code);
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public List<Map<String, Object>> getUpOrDownTime(String code) {
		String sql = "select name,limitup_date,limitup_time,limitdown_date,limitdown_time from stock_riseorfall_info where code=?";
		return getStockJdbc().getJdbcTemplate().queryForList(sql, code);
	}

	public int saveStockRiseorfallInfo(String code, String name, JSONArray limitupDate, int limitupTime, JSONArray limitdownDate, int limitdownTime) {
		String sql = "insert into stock_riseorfall_info(code,name,limitup_date,limitup_time,limitdown_date,limitdown_time,update_time) "
				+ "values(:code,:name,:limitupDate,:limitupTime,:limitdownDate,:limitdownTime,now())";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("name", name);
		map.put("limitupDate", limitupDate.toString());
		map.put("limitupTime", limitupTime);
		map.put("limitdownDate", limitdownDate.toString());
		map.put("limitdownTime", limitdownTime);
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public Map<String, Object> getLimitUpDown() {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select count(1) as count,max(date) as max,min(date) as min from limit_up_down where 1=1 ";
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForMap(sql, map);
	}

	public List<Map<String, Object>> count() {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select * from limit_up_down where 1=1 ";
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}

	public void test() {
		// StockInfo sie = (StockInfo)DBAgent.get(StockInfo.class, 1);
		// System.out.println(sie.getCode());
		List list = DBAgent.loadAll(Account.class);
		System.out.println(((Account) list.get(0)).getUserName());

		JDBCAgent jdbc = new JDBCAgent();
		try {
			jdbc.execute("select * from account where id=1");
			ResultSet rs = jdbc.getQueryResult();
			while (rs.next()) {
				String user = rs.getString("user_name");
				System.out.println(user + "-----");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int updateCount(int ld, int lu, String date) {
		String sql = "update limit_up_down set limitdown_number=?,limitup_number=? where date=?";
		return getStockJdbc().getJdbcTemplate().update(sql, ld, lu, date);
	}

	public List<Map<String, Object>> weekOrMonthRiseOrFall(String code) {
		String sqlParam = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(code)) {
			sqlParam = "where stock_code=:stockCode";
			map.put("stockCode", code);
		}
		String sql = "select a.*,CAST((a.rownum-a.number_of_week) as SIGNED) as differ from (select @rownum:=@rownum+1 AS rownum,s.*from (select @rownum:=0) r,stock_index_everyday s "
				+ sqlParam + " order by date) a";
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}

	public List<Map<String, Object>> getMonthData(String code) {
		String sql = "select a.*,count(1),MIN(date) as min,max(date) as max from (select stock_code,stock_name,date,LEFT(number_date,6) as numberdate  from stock_index_everyday where stock_code=:stockCode )a GROUP BY a.numberdate ORDER BY date desc";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stockCode", code);
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}

	public int saveWeekAndMonthData(String code, String name, JSONArray week, JSONArray month) {
		String sql = "insert into week_and_month_data(stock_code,stock_name,week_riseorfall_data,recent_week_riseorfall_data,month_riseorfall_data,recent_month_riseorfall_data) "
				+ "values(:stockCode,:stockName,:weekRiseorfallData,:recentWeekRiseorfallData,:monthRiseorfallData,:recentMonthRiseorfallData)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stockCode", code);
		map.put("stockName", name);
		if (!CollectionUtils.isEmpty(week)) {
			map.put("recentWeekRiseorfallData", week.get(0).toString());
			week.remove(0);
			map.put("weekRiseorfallData", week.toString());
		} else {
			map.put("recentWeekRiseorfallData", null);
			map.put("weekRiseorfallData", null);
		}
		if (!CollectionUtils.isEmpty(month)) {
			map.put("recentMonthRiseorfallData", month.get(0).toString());
			month.remove(0);
			map.put("monthRiseorfallData", month.toString());
		} else {
			map.put("recentMonthRiseorfallData", null);
			map.put("monthRiseorfallData", null);
		}
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public List<Map<String, Object>> getWeekAndMonthData(String stockCode) {
		String sql = "select stock_code,stock_name,week_riseorfall_data,recent_week_riseorfall_data,month_riseorfall_data,recent_month_riseorfall_data from week_and_month_data where 1=1 and stock_code=:stockCode";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stockCode", stockCode);
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}

	public int updateWeekAndMonthData(String code, JSONArray array, String param) {
		String sql = "";
		String param1 = param.split("-")[0];
		String param2 = param.split("-")[1];
		Map<String, Object> map = new HashMap<String, Object>();
		if ("week".equals(param1) && "1".equals(param2)) {
			sql = "update week_and_month_data set recent_week_riseorfall_data=:recentWeekRiseorfallData where stock_code=:stockCode";
			map.put("recentWeekRiseorfallData", array.getString(0));
		} else if ("week".equals(param1) && "2".equals(param2)) {
			sql = "update week_and_month_data set week_riseorfall_data=:weekRiseorfallData where stock_code=:stockCode";
			map.put("weekRiseorfallData", array.toString());
		} else if ("month".equals(param1) && "1".equals(param2)) {
			sql = "update week_and_month_data set recent_month_riseorfall_data=:recentMonthRiseorfallData where stock_code=:stockCode";
			map.put("recentMonthRiseorfallData", array.getString(0));
		} else if ("month".equals(param1) && "2".equals(param2)) {
			sql = "update week_and_month_data set month_riseorfall_data=:monthRiseorfallData where stock_code=:stockCode";
			map.put("monthRiseorfallData", array.toString());
		}
		map.put("stockCode", code);
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public List<Map<String, Object>> serach(String stockMarket, String serachValue) {
		String sql = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (serachValue.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
			sql = "select DISTINCT  i.*,s.stock_market from stock_info i,stock s where i.code like '" + serachValue
					+ "%' and s.stock_market=:stockMarket and i.code=s.code";
		} else {
			sql = "select DISTINCT  i.*,s.stock_market from stock_info i,stock s where i.name like '" + serachValue
					+ "%' and s.stock_market=:stockMarket and i.code=s.code";
		}
		map.put("stockMarket", stockMarket);
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}

	public List<Map<String, Object>> getStockToApp(String stockCode, String date) {
		String sql = "select * from stock_index_everyday where stock_code =:stockCode and date=:date";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("stockCode", stockCode);
		map.put("date", date);
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}

	public void saveFianceData(List<Finance> finance) {
		String sql = "insert into finance(stock_code,stock_name,date,proceeds_of_business,net_profit,total_profit,total_assets) "
				+ "values(:stockCode,:stockName,:date,:proceedsOfBusiness,:netProfit,:totalProfit,:totalAssets)";
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(finance.toArray());// 参数与实体类相同，与sql中的参数相同
		getStockJdbc().getNamedParameterJdbcTemplate().batchUpdate(sql, batch);
	}
}
