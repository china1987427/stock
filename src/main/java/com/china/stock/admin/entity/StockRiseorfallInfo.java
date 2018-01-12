package com.china.stock.admin.entity;

import java.sql.Clob;
import java.sql.Date;

public class StockRiseorfallInfo {

	private Integer id;
	private String code;
	private String name;
	private Clob limitupDate;
	private Integer limitupTime;
	private Clob limitdownDate;
	private Integer limitdownTime;
	private Date updateTime;
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
	public Clob getLimitupDate() {
		return limitupDate;
	}
	public void setLimitupDate(Clob limitupDate) {
		this.limitupDate = limitupDate;
	}
	public Integer getLimitupTime() {
		return limitupTime;
	}
	public void setLimitupTime(Integer limitupTime) {
		this.limitupTime = limitupTime;
	}
	public Clob getLimitdownDate() {
		return limitdownDate;
	}
	public void setLimitdownDate(Clob limitdownDate) {
		this.limitdownDate = limitdownDate;
	}
	public Integer getLimitdownTime() {
		return limitdownTime;
	}
	public void setLimitdownTime(Integer limitdownTime) {
		this.limitdownTime = limitdownTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
