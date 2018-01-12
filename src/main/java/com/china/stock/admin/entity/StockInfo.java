package com.china.stock.admin.entity;

import java.util.Date;

import org.springframework.orm.hibernate3.support.BlobStringType;

import com.mysql.jdbc.Clob;

public class StockInfo {
	private Integer id;
	private String code;
	private String name;
	private Date timeToMarket;
	private String marketMark;// 1表示上海股票，2表示深圳股票
	private String isEverydata;
	private String profitPerShare;// 每股收益(括号中为季度)
	private String priceEarningRatio;// 市盈率PE
	private String netAsset;// 净资产
	private String pbRatio;// 市净率price/book value ratio
	private String operatingIncome;
	private String yearonyearGrowthOi;// 营业收入同比增长
	private String netProfit;// 净利润
	private String yearonyearGrowthNp;// 净利润同比增长
	private String grossProfitRatio;// 毛利率
	private String netProfitRatio;// 净利率
	private String roe;// 净资产收益率Rate of Return on Common Stockholders'Equity
	private String debtRatio;// 负债率
	private String totalShares;// 总股数
	private String grossValue;// 总值
	private String tradableShare;// 流通股
	private String tradableValue;// 流通总值
	private String undistributedProfitPerShare;// 每股未分配利润
	private String mainBusiness;// main_business

	
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTimeToMarket() {
		return timeToMarket;
	}

	public void setTimeToMarket(Date timeToMarket) {
		this.timeToMarket = timeToMarket;
	}

	public String getMarketMark() {
		return marketMark;
	}

	public void setMarketMark(String marketMark) {
		this.marketMark = marketMark;
	}

	public String getIsEverydata() {
		return isEverydata;
	}

	public void setIsEverydata(String isEverydata) {
		this.isEverydata = isEverydata;
	}

	public String getProfitPerShare() {
		return profitPerShare;
	}

	public void setProfitPerShare(String profitPerShare) {
		this.profitPerShare = profitPerShare;
	}

	public String getPriceEarningRatio() {
		return priceEarningRatio;
	}

	public void setPriceEarningRatio(String priceEarningRatio) {
		this.priceEarningRatio = priceEarningRatio;
	}

	public String getNetAsset() {
		return netAsset;
	}

	public void setNetAsset(String netAsset) {
		this.netAsset = netAsset;
	}

	public String getPbRatio() {
		return pbRatio;
	}

	public void setPbRatio(String pbRatio) {
		this.pbRatio = pbRatio;
	}

	public String getOperatingIncome() {
		return operatingIncome;
	}

	public void setOperatingIncome(String operatingIncome) {
		this.operatingIncome = operatingIncome;
	}

	public String getYearonyearGrowthOi() {
		return yearonyearGrowthOi;
	}

	public void setYearonyearGrowthOi(String yearonyearGrowthOi) {
		this.yearonyearGrowthOi = yearonyearGrowthOi;
	}

	public String getNetProfit() {
		return netProfit;
	}

	public void setNetProfit(String netProfit) {
		this.netProfit = netProfit;
	}

	public String getYearonyearGrowthNp() {
		return yearonyearGrowthNp;
	}

	public void setYearonyearGrowthNp(String yearonyearGrowthNp) {
		this.yearonyearGrowthNp = yearonyearGrowthNp;
	}

	

	public String getGrossProfitRatio() {
		return grossProfitRatio;
	}

	public void setGrossProfitRatio(String grossProfitRatio) {
		this.grossProfitRatio = grossProfitRatio;
	}

	public String getNetProfitRatio() {
		return netProfitRatio;
	}

	public void setNetProfitRatio(String netProfitRatio) {
		this.netProfitRatio = netProfitRatio;
	}

	public String getRoe() {
		return roe;
	}

	public void setRoe(String roe) {
		this.roe = roe;
	}

	public String getDebtRatio() {
		return debtRatio;
	}

	public void setDebtRatio(String debtRatio) {
		this.debtRatio = debtRatio;
	}

	public String getTotalShares() {
		return totalShares;
	}

	public void setTotalShares(String totalShares) {
		this.totalShares = totalShares;
	}

	public String getGrossValue() {
		return grossValue;
	}

	public void setGrossValue(String grossValue) {
		this.grossValue = grossValue;
	}

	public String getTradableShare() {
		return tradableShare;
	}

	public void setTradableShare(String tradableShare) {
		this.tradableShare = tradableShare;
	}

	public String getTradableValue() {
		return tradableValue;
	}

	public void setTradableValue(String tradableValue) {
		this.tradableValue = tradableValue;
	}

	public String getUndistributedProfitPerShare() {
		return undistributedProfitPerShare;
	}

	public void setUndistributedProfitPerShare(String undistributedProfitPerShare) {
		this.undistributedProfitPerShare = undistributedProfitPerShare;
	}

	public String getMainBusiness() {
		return mainBusiness;
	}

	public void setMainBusiness(String mainBusiness) {
		this.mainBusiness = mainBusiness;
	}

	
	
}
