package com.china.stock.user.dao;

import java.sql.Connection;
import java.sql.Statement;

import com.china.stock.common.database.ConnectionDB;


public class ReadTxtDao {
	static Connection conn;

	static {
		conn = ConnectionDB.getConnection();
	}

	public void saveToAllStock(String code, String name, int stockMarket) {
		try {
			System.out.println(code + "/" + name);
			Statement stmt = conn.createStatement();
			String sql = "insert into stock(code,name,stock_market) values('" + code + "' ,'" + name + "',"
					+ stockMarket + ")";
			int result = stmt.executeUpdate(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
			if (result != -1) {
				System.out.println("创建数据表成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int saveToStockInfo(String code, String name, String industry, String area, float pe, double outstanding,
			double totals, float eps, float bvps, float pb, String timetomarket) {
		int result = 0;
		try {
			Statement stmt = conn.createStatement();
			String sql = "insert into stock_info(code,name,pe_ratio,eps,bvps,pb,area,industry,time_to_market,totals,out_standing) values('"
					+ code + "' ,'" + name + "'," + pe + "," + eps + "," + bvps + "," + pb + ",'" + area + "','"
					+ industry + "','" + timetomarket + "'," + totals + "," + outstanding + ")";
			result = stmt.executeUpdate(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
			if (result != -1) {
				System.out.println("向stock_info插入数据成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int saveToStockIndex(String date, String week, float openingIndex, float highestIndex, float lowestIndex,
			float closingIndex, String riseOrFall, String increase, String amplitude, double totalHand,
			double sumMoney) {
		int result = 0;
		try {
			Statement stmt = conn.createStatement();
			String sql = "insert into stock_index(date,day_in_week,import_time,opening_index,highest_index,lowest_index,closing_index,rise_or_fall,increase,amplitude,total_hand,sum_money) "
					+ "values('" + date + "','" + week + "'," + "now()" + "," + openingIndex + "," + highestIndex + ","
					+ lowestIndex + "," + closingIndex + ",'" + riseOrFall + "','" + increase + "','" + amplitude
					+ "'," + totalHand + "," + sumMoney + ")";
			result = stmt.executeUpdate(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
			if (result != -1) {
				System.out.println("创建数据表成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
