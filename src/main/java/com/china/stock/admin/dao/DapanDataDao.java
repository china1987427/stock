package com.china.stock.admin.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.china.stock.admin.entity.MarketIndexEveryday;
import com.china.stock.common.database.DBJdbc;

@Service(value = "/dapanDataDao")
public class DapanDataDao extends DBJdbc {

	public void saveToDaPanDataEveryday(List<MarketIndexEveryday> marketIndex) {
		String sql = "insert into market_index_everyday(market_name,date,number_date,day_in_week,riseorfall_case,in_time,opening_index,closing_index,rise_or_fall,rise_or_fall_range,amplitude,low_index,high_index,total_hand,sum_money) "
				+ "values(:marketName,:date,:numberDate,:week,:riseorfallCase,:inTime,:mOpeningIndex,:mClosingIndex,:mRiseorfall,:riseorfallRange,:mAmplitude,"
				+ ":mLowIndex,:mHighIndex,:mTotalhand,:sumMoney)";
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(marketIndex.toArray());
		getStockJdbc().getNamedParameterJdbcTemplate().batchUpdate(sql, batch);
	}

	public List<Map<String, Object>> getDapan(String date) {
		StringBuilder sb = new StringBuilder();
		String sql = "select market_name,date,number_date,week,riseorfall_case,in_time,opening_index,closing_index,rise_or_fall,CAST(replace(amplitude,'%','') AS DECIMAL(11,2)) as amplitude,low_index,high_index,total_hand,sum_money from market_index_everyday where 1=1 ";
		sb.append(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(date)) {
			sb.append("and date=:date ");
			map.put("date", date);
		}
		sb.append("order by date desc");
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sb.toString(), map);
	}

	public List<Map<String, Object>> getAllStocks(String marketMark) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select id,code,name,belong_to_area,time_to_market,market_mark,profit_per_share,net_asset_per_share,"
				+ "total_stock_issue,net_asset_yield_rate,outstanding_a_shares,main_business_income_growth_rate,new_distribution_plans,"
				+ "net_profits_growth_rate,undistributed_profit_per_share,price_earning_ratio,date_of_establishment,belong_to_industry,"
				+ "industry_ranking,belong_to_concept,ICB_industry,ICB_industry_ranking,registered_assets,issue_price,issue_price_earning_ratio,"
				+ "new_price_earning_ratio from stock_info where 1=1 ";
		sb.append(sql);
		if (!StringUtils.isEmpty(marketMark)) {
			sb.append("and market_mark like :marketMark ");
			map.put("marketMark", marketMark);
		}
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sb.toString(), map);
	}

	public Map<String, Object> verifyDapan() {
		String sql = "select count(1) as count,max(date) as max from market_index_everyday ";
		Map<String, Object> map = new HashMap<String, Object>();
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForMap(sql, map);
	}

	public int checkRiseOrFall(String number) {
		StringBuilder sb = new StringBuilder();
		String sql = "select count(riseorfall_case) as count from market_index_everyday where 1=1 ";
		sb.append(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(number)) {
			sb.append("and riseorfall_case=:riseorfallCase ");
			map.put("riseorfallCase", number);
		}
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForInt(sb.toString(), map);
	}

	public List<Map<String, Object>> getRiseOrFallData(String number) {
		StringBuilder sb = new StringBuilder();
		String sql = "select market_name,date,number_date,day_in_week,riseorfall_case,in_time,opening_index,closing_index,rise_or_fall,CAST(replace(amplitude,'%','') AS DECIMAL(11,2)) as amplitude,low_index,high_index,total_hand,sum_money from market_index_everyday where 1=1 ";
		sb.append(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(number)) {
			sb.append("and riseorfall_case=:riseorfallCase ");
			map.put("riseorfallCase", number);
		}
		sb.append("order by date desc");
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sb.toString(), map);
	}

	public List<Map<String, Object>> getDateRiseOrFall(String number) {
		String sqlParam = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(number)) {
			sqlParam = "where riseorfall_case=:riseorfallCase";
			map.put("riseorfallCase", number);
		}
		String sql = "select a.id,a.date,CAST((a.id-a.rownum) as SIGNED) as differ,a.* from (select @rownum:=@rownum+1 AS rownum,m.*from (select @rownum:=0) r,market_index_everyday m "
				+ sqlParam + " order by id, date) a ";
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}

	public List<Map<String, Object>> getMaxOrMinDate(String param) {
		String sql = "";
		if ("max".equals(param)) {
			sql = "select max(date) as max from market_index_everyday where 1=1";
		} else if ("min".equals(param)) {
			sql = "select min(date) as min from market_index_everyday where 1=1";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}

	public List<Map<String, Object>> getDapanData() {
		String sql = "select * from (select market_name,date,number_date,day_in_week,riseorfall_case,in_time,opening_index,closing_index,rise_or_fall,CAST(replace(amplitude,'%','') AS DECIMAL(11,2)) as amplitude,low_index,high_index,total_hand,sum_money from market_index_everyday where 1=1 order by date desc limit 60) s order by s.date asc";
		Map<String, Object> map = new HashMap<String, Object>();
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sql, map);
	}
}
