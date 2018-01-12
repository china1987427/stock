package com.china.stock.admin.entity;

import java.sql.Timestamp;
import java.util.Date;

public class MarketIndexEveryday {
	private Integer id;
	private String marketName;
	private Date date;
	private String numberDate;
	private String week;//
	private String riseorfallCase;
	private String riseorfallRange;
	private Timestamp inTime;
	private String mOpeningIndex;
	private String mClosingIndex;
	private float mRiseorfall;
	private String mAmplitude;
	private String mLowIndex;
	private String mHighIndex;
	private String handover;
	private String mTotalhand;
	private String sumMoney;

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	

	public String getNumberDate() {
		return numberDate;
	}

	public void setNumberDate(String numberDate) {
		this.numberDate = numberDate;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getRiseorfallCase() {
		return riseorfallCase;
	}

	public void setRiseorfallCase(String riseorfallCase) {
		this.riseorfallCase = riseorfallCase;
	}

	
	public String getRiseorfallRange() {
		return riseorfallRange;
	}

	public void setRiseorfallRange(String riseorfallRange) {
		this.riseorfallRange = riseorfallRange;
	}

	public Timestamp getInTime() {
		return inTime;
	}

	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}

	public String getmOpeningIndex() {
		return mOpeningIndex;
	}

	public void setmOpeningIndex(String mOpeningIndex) {
		this.mOpeningIndex = mOpeningIndex;
	}

	public String getmClosingIndex() {
		return mClosingIndex;
	}

	public void setmClosingIndex(String mClosingIndex) {
		this.mClosingIndex = mClosingIndex;
	}

	public float getmRiseorfall() {
		return mRiseorfall;
	}

	public void setmRiseorfall(float mRiseorfall) {
		this.mRiseorfall = mRiseorfall;
	}

	public String getmAmplitude() {
		return mAmplitude;
	}

	public void setmAmplitude(String mAmplitude) {
		this.mAmplitude = mAmplitude;
	}

	public String getmLowIndex() {
		return mLowIndex;
	}

	public void setmLowIndex(String mLowIndex) {
		this.mLowIndex = mLowIndex;
	}

	public String getmHighIndex() {
		return mHighIndex;
	}

	public void setmHighIndex(String mHighIndex) {
		this.mHighIndex = mHighIndex;
	}
	
	
	public String getHandover() {
		return handover;
	}

	public void setHandover(String handover) {
		this.handover = handover;
	}

	public String getmTotalhand() {
		return mTotalhand;
	}

	public void setmTotalhand(String mTotalhand) {
		this.mTotalhand = mTotalhand;
	}

	public String getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(String sumMoney) {
		this.sumMoney = sumMoney;
	}

	
}
