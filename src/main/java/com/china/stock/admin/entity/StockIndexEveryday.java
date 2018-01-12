package com.china.stock.admin.entity;

import java.sql.Timestamp;
import java.util.Date;

public class StockIndexEveryday {
	private Integer id;
	private String stockCode;
	private String stockName;
	private String marketMark;
	private String riseorfallCase;
	private Date date;
	private String openingIndex;
	private String highIndex;
	private String lowIndex;
	private String closingIndex;
	private String dayInWeek;
	private String riseOrFall;
	private String amplitude;
	private String riseorfallRange;
	private String totalHand;
	private String totalMoney;
	private String handover;
	private String numberDate;
	private String numberOfWeek;
	private Timestamp importTime;

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

	public String getMarketMark() {
		return marketMark;
	}

	public void setMarketMark(String marketMark) {
		this.marketMark = marketMark;
	}

	public String getRiseorfallCase() {
		return riseorfallCase;
	}

	public void setRiseorfallCase(String riseorfallCase) {
		this.riseorfallCase = riseorfallCase;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getOpeningIndex() {
		return openingIndex;
	}

	public void setOpeningIndex(String openingIndex) {
		this.openingIndex = openingIndex;
	}

	public String getHighIndex() {
		return highIndex;
	}

	public void setHighIndex(String highIndex) {
		this.highIndex = highIndex;
	}

	public String getLowIndex() {
		return lowIndex;
	}

	public void setLowIndex(String lowIndex) {
		this.lowIndex = lowIndex;
	}

	public String getClosingIndex() {
		return closingIndex;
	}

	public void setClosingIndex(String closingIndex) {
		this.closingIndex = closingIndex;
	}

	public String getDayInWeek() {
		return dayInWeek;
	}

	public void setDayInWeek(String dayInWeek) {
		this.dayInWeek = dayInWeek;
	}

	public String getRiseOrFall() {
		return riseOrFall;
	}

	public void setRiseOrFall(String riseOrFall) {
		this.riseOrFall = riseOrFall;
	}

	public String getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(String amplitude) {
		this.amplitude = amplitude;
	}

	public String getRiseorfallRange() {
		return riseorfallRange;
	}

	public void setRiseorfallRange(String riseorfallRange) {
		this.riseorfallRange = riseorfallRange;
	}

	public String getTotalHand() {
		return totalHand;
	}

	public void setTotalHand(String totalHand) {
		this.totalHand = totalHand;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getHandover() {
		return handover;
	}

	public void setHandover(String handover) {
		this.handover = handover;
	}

	public String getNumberDate() {
		return numberDate;
	}

	public void setNumberDate(String numberDate) {
		this.numberDate = numberDate;
	}

	public String getNumberOfWeek() {
		return numberOfWeek;
	}

	public void setNumberOfWeek(String numberOfWeek) {
		this.numberOfWeek = numberOfWeek;
	}

	public Timestamp getImportTime() {
		return importTime;
	}

	public void setImportTime(Timestamp importTime) {
		this.importTime = importTime;
	}
	
	
}
