package com.china.stock.common.database.util;


import java.math.BigDecimal;
import java.util.*;

public class FlipInfo {

	public FlipInfo() {
		page = 1;
		size = 20;
		needTotal = true;
		total = 0;
	}

	public FlipInfo(int page) {
		this.page = 1;
		size = 20;
		needTotal = true;
		total = 0;
		this.page = page;
	}

	public FlipInfo(int page, int size) {
		this.page = 1;
		this.size = 20;
		needTotal = true;
		total = 0;
		this.page = page;
		this.size = size;
	}

	public FlipInfo(Map flipMap) {
		page = 1;
		size = 20;
		needTotal = true;
		total = 0;
		if (flipMap != null) {
			int p = Integer.parseInt((String) flipMap.get("page"));
			int s = Integer.parseInt((String) flipMap.get("size"));
			String sf = (String) flipMap.get("sortField");
			String so = (String) flipMap.get("sortOrder");
			page = p;
			size = s;
			sortField = sf;
			sortOrder = so;
		}
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Integer getPages() {
		BigDecimal sizeBD = new BigDecimal(size);
		BigDecimal totalBD = new BigDecimal(total);
		return new Integer(totalBD.divide(sizeBD, 0, 2).intValue());
	}

	public Integer getStartAt() {
		Integer p = Integer.valueOf(getPage());
		p = Integer.valueOf(p == null || p.intValue() - 1 < 0 ? 0 : p
				.intValue() - 1);
		return Integer.valueOf(p.intValue() * getSize());
	}

	public List getData() {
		if (data == null)
			data = new ArrayList();
		return data;
	}

	public void setData(List data) {
		if (this.data == null)
			this.data = new ArrayList();
		this.data.clear();
		if (data != null)
			this.data.addAll(data);
	}

	public Integer getDataCount() {
		if (data != null)
			return new Integer(data.size());
		else
			return new Integer(0);
	}

	public void setNeedTotal(boolean needTotal) {
		this.needTotal = needTotal;
	}

	public boolean isNeedTotal() {
		return needTotal;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	private List data;
	private int page;
	private int size;
	private boolean needTotal;
	private int total;
	private String sortField;
	private String sortOrder;
	private Map params;
}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\A8\Seeyon-G6v5.1\ApacheJetspeed\webapps\seeyon\WEB-INF\lib\seeyon_ctp_core.jar
	Total time: 65 ms
	Jad reported messages/errors:
The class file version is 50.0 (only 45.3, 46.0 and 47.0 are supported)
	Exit status: 0
	Caught exceptions:
*/
