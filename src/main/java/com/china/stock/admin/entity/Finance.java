package com.china.stock.admin.entity;

import java.util.Date;

public class Finance {
	private Integer id;
	private String stockCode;
	private String stockName;
	private Date date;
	private String proceedsOfBusiness;
	private String netProfit;
	private String totalProfit;
	private String totalAssets;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getProceedsOfBusiness() {
		return proceedsOfBusiness;
	}
	public void setProceedsOfBusiness(String proceedsOfBusiness) {
		this.proceedsOfBusiness = proceedsOfBusiness;
	}
	public String getNetProfit() {
		return netProfit;
	}
	public void setNetProfit(String netProfit) {
		this.netProfit = netProfit;
	}
	public String getTotalProfit() {
		return totalProfit;
	}
	public void setTotalProfit(String totalProfit) {
		this.totalProfit = totalProfit;
	}
	public String getTotalAssets() {
		return totalAssets;
	}
	public void setTotalAssets(String totalAssets) {
		this.totalAssets = totalAssets;
	}

}
