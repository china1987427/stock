package com.china.stock.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.china.stock.common.database.DBJdbc;
import com.china.stock.common.tool.base.ObjUtil;


@Service(value = "/readExcelDao")
public class ReadExcelDao extends DBJdbc {

	public int saveData(List list, String stockCode, String stockName) {
		String sql = "insert into stock_data(stock_code,stock_name,date,day_in_week,import_time,opening_index,highest_index,lowest_index,closing_index,rise_or_fall,amplitude,total_hand,sum,handover,volamount) "
				+ "values(:stockCode,:stockName,:date,:dayInWeek,now(),:openingIndex,:highestIndex,:lowestIndex,:closingIndex,"
				+ ":riseOrFall,:amplitude,:totalHand,:sum,:handover,:volamount)";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for (int i = 0; i < list.size(); i++) {
				map.put("stockCode", stockCode);
				map.put("stockName", stockName);
				map.put("date", ObjUtil.toString(list.get(0)).split(",")[0]);
				map.put("dayInWeek", ObjUtil.toString(list.get(0)).split(",")[1]);
				map.put("openingIndex", list.get(1));
				map.put("highestIndex", list.get(2));
				map.put("lowestIndex", list.get(3));
				map.put("closingIndex", list.get(4));
				map.put("riseOrFall", list.get(5));
				map.put("amplitude", list.get(6));
				map.put("totalHand", list.get(7));
				map.put("sum", list.get(8));
				map.put("handover", list.get(9));
				map.put("volamount", list.get(10));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public int saveToStock(String stockCode,String stockName,String peRatio,String region,String industry,String mainBusiness) {
		String sql ="insert into stock_info(code,name,pe_ratio,region,industry,main_business) "
				+ "values(:code,:name,:peRatio,:region,:industry,:mainBusiness)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", stockCode);
		map.put("name", stockName);
		map.put("peRatio", peRatio);
		map.put("region", region);
		map.put("industry", industry);
		map.put("mainBusiness", mainBusiness);
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}
}
