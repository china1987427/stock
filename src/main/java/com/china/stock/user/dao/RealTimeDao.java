package com.china.stock.user.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.china.stock.common.database.DBJdbc;


@Service(value = "/realTimeDao")
public class RealTimeDao extends DBJdbc {

	public int saveCurrentData(String name, String code, String date, String time, String openningPrice,
			String closingPrice, String currentPrice, String hPrice, String lPrice, String competitivePrice,
			String auctionPrice, String totalNumber, String turnover, String increase, String buyOne,
			String buyOnePrice, String buyTwo, String buyTwoPrice, String buyThree, String buyThreePrice,
			String buyFour, String buyFourPrice, String buyFive, String buyFivePrice, String sellOne,
			String sellOnePrice, String sellTwo, String sellTwoPrice, String sellThree, String sellThreePrice,
			String sellFour, String sellFourPrice, String sellFive, String sellFivePrice, String minurl, String dayurl,
			String weekurl, String monthurl) {
		String sql = "insert into stock_current(code,name,date,time,in_time,openning_price,closing_price,current_price,h_price,l_price,competitive_price,auction_price,total_number,turnover,increase,buy_one,buy_one_price,buy_two,buy_two_price,buy_three,buy_three_price,buy_four,buy_four_price,buy_five,buy_five_price,sell_one,sell_one_price,sell_two,sell_two_price,sell_three,sell_three_price,sell_four,sell_four_price,sell_five,sell_five_price,minurl,dayurl,weekurl,monthurl) "
				+ "values(:code,:name,:date,:time,now(),:openningPrice,:closingPrice,:currentPrice,:hPrice,:lPrice,:competitivePrice,:auctionPrice,:totalNumber,:turnover,:increase,:buyOne,:buyOnePrice,:buyTwo,:buyTwoPrice,:buyThree,:buyThreePrice,:buyFour,:buyFourPrice,:buyFive,:buyFivePrice,:sellOne,:sellOnePrice,:sellTwo,:sellTwoPrice,:sellThree,:sellThreePrice,:sellFour,:sellFourPrice,:sellFive,:sellFivePrice,:minurl,:dayurl,:weekurl,:monthurl)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("name", name);
		map.put("date", date);
		map.put("time", time);
		map.put("openningPrice", openningPrice);
		map.put("closingPrice", closingPrice);
		map.put("currentPrice", currentPrice);
		map.put("hPrice", hPrice);
		map.put("lPrice", lPrice);
		map.put("competitivePrice", competitivePrice);
		map.put("auctionPrice", auctionPrice);
		map.put("totalNumber", totalNumber);
		map.put("turnover", turnover);
		map.put("increase", increase);
		map.put("buyOne", buyOne);
		map.put("buyOnePrice", buyOnePrice);
		map.put("buyTwo", buyTwo);
		map.put("buyTwoPrice", buyTwoPrice);
		map.put("buyThree", buyThree);
		map.put("buyThreePrice", buyThreePrice);
		map.put("buyFour", buyFour);
		map.put("buyFourPrice", buyFourPrice);
		map.put("buyFive", buyFive);
		map.put("buyFivePrice", buyFivePrice);
		map.put("sellOne", sellOne);
		map.put("sellOnePrice", sellOnePrice);
		map.put("sellTwo", sellTwo);
		map.put("sellTwoPrice", sellTwoPrice);
		map.put("sellThree", sellThree);
		map.put("sellThreePrice", sellThreePrice);
		map.put("sellFour", sellFour);
		map.put("sellFourPrice", sellFourPrice);
		map.put("sellFive", sellFive);
		map.put("sellFivePrice", sellFivePrice);
		map.put("minurl", minurl);
		map.put("dayurl", dayurl);
		map.put("weekurl", weekurl);
		map.put("monthurl", monthurl);
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public int saveMarketInfo(String curdot, String name, String curprice, String rate, String dealnumber,
			String turnover, String marketMark) {
		String sql = "insert into stock_market(market_name,in_time,curdot,curprice,rate,dealnumber,turnover,market_mark) values(:marketName,now(),:curdot,:curprice,:rate,:dealnumber,:turnover,:marketMark)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("marketName", name);
		map.put("curdot", curdot);
		map.put("curprice", curprice);
		map.put("rate", rate);
		map.put("dealnumber", dealnumber);
		map.put("turnover", turnover);
		map.put("marketMark", marketMark);
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}

	public int saveToMarket(String name, String openClosing, String pastClosing, String closing, String highClosing,
			String lowClosing, String tatalHand, String sum, String date, String time) {
		String sql = "insert into market_current(market_name,in_time,market_open_closing,market_past_closing,market_closing,market_h_closing,market_l_closing,market_totalhand,sum,date,time) values(:marketName,now(),:marketOpenClosing,:marketPastClosing,:marketClosing,:marketHClosing,:marketLClosing,:marketTotalhand,:sum,:date,:time)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("marketName", name);
		map.put("marketOpenClosing", openClosing);
		map.put("marketPastClosing", pastClosing);
		map.put("marketClosing", closing);
		map.put("marketHClosing", highClosing);
		map.put("marketLClosing", lowClosing);
		map.put("marketTotalhand", tatalHand);
		map.put("sum", sum);
		map.put("date", date);
		map.put("time", time);
		return getStockJdbc().getNamedParameterJdbcTemplate().update(sql, map);
	}
}
