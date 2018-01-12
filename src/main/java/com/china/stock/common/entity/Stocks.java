package com.china.stock.common.entity;

import java.util.Date;

public class Stocks {
	private String stockName;
	private String stockCode;
	private Date marketingDate;// 入市时间
	private long totalShares;// 总股本
	private long tradableShare;// 流通股本
	private String province;
	private String city;
	private String ToBelong;// 所属行业

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public Date getMarketingDate() {
		return marketingDate;
	}

	public void setMarketingDate(Date marketingDate) {
		this.marketingDate = marketingDate;
	}

	public long getTotalShares() {
		return totalShares;
	}

	public void setTotalShares(long totalShares) {
		this.totalShares = totalShares;
	}

	public long getTradableShare() {
		return tradableShare;
	}

	public void setTradableShare(long tradableShare) {
		this.tradableShare = tradableShare;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getToBelong() {
		return ToBelong;
	}

	public void setToBelong(String toBelong) {
		ToBelong = toBelong;
	}

}
