package com.china.stock.admin.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.china.stock.common.database.DBJdbc;

@Service(value = "/adminDao")
public class AdminDao extends DBJdbc {
	

	public Map<String, Object> checkStockInfo(String code) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "select count(1) as count from stock_info where code=:stockCode";
		map.put("stockCode", code);
		return getStockJdbc().getNamedParameterJdbcTemplate().queryForMap(sql, map);
	}

	public Map<String, Object> getstock(String stockcode, Integer curPage, Integer pageSize, String marketMark) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String sql = "select SQL_CALC_FOUND_ROWS DISTINCT * from stock_info where 1=1 ";
		sb.append(sql);
		if (!StringUtils.isEmpty(stockcode)) {
			sb.append("and code=:stockcode ");
			map.put("stockcode", stockcode);
		}
		if (!StringUtils.isEmpty(marketMark)) {
			sb.append("and market_mark=:marketMark ");
			map.put("marketMark", marketMark);
		}
		if (pageSize == 0) { // 显示条数
			map.put("pageSize", 100);
		} else {
			map.put("pageSize", pageSize);
		}
		map.put("curPage", (curPage - 1) * (pageSize == 0 ? 100 : pageSize));
		sb.append("limit :curPage,:pageSize");
		resultMap.put("list", getStockJdbc().getNamedParameterJdbcTemplate().queryForList(sb.toString(), map));
		String sqlTotal = "select found_rows()";
		resultMap.put("total", getStockJdbc().getJdbcTemplate().queryForInt(sqlTotal));
		return resultMap;
	}

	public List<Map<String, Object>> getAllStocks() {
		String sql = "select * from stock_info";
		return getStockJdbc().getJdbcTemplate().queryForList(sql);
	}

}
