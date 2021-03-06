package com.china.stock.common.tool.page;

import java.util.List;

/**
 * 分页数据对象
 * 
 */
public class Page<T> {

	/** 当前页数 */
	private int index = 1;

	/** 总记录数 */
	private int totalRow;

	/** 每页显示条数，默�?0�?*/
	private int pageSize = 100;

	/** 当前页起始行 */
	private int start;

	/** 当前页结束行 */
	private int end;

	/** 记录列表，分页数�?*/
	private List<T> records;

	/** 总页�?*/
	private int totalPage;

	/** 显示的页码列表的�?��索引 */
	private int startPageIndex = 1;

	/** 显示的页码列表的结束索引 */
	private int endPageIndex = 10;

	public Page() {
	}

	public Page(int index, int pageSize) {
		this.pageSize = pageSize;
		this.setIndex(index);
	}

	public Page(int index, int pageSize, List<T> records, int totalRow) {
		this.setIndex(index);
		this.pageSize = pageSize;
		this.records = records;
		this.totalRow = totalRow;
		buildPageIndex();
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(int index) {
		if (index < 1) {
			index = 1; // 当前页小�?的时候自动赋值为第一�?
		} else {
			start = pageSize * (index - 1); // 计算�?��行数
		}
		end = (int) (start + pageSize > totalRow ? totalRow : start + pageSize); // 计算结束行数
		this.index = index;
	}

	public Integer getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		totalPage = (int) ((totalRow + pageSize - 1) / pageSize);
		this.totalRow = totalRow;
		if (totalPage < index) { // 当前
			index = totalPage;
			/*start = pageSize * (index - 1);
			end = totalRow;*/
		}
		end = (int) (start + pageSize > totalRow ? totalRow : start + pageSize);
		buildPageIndex();
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getStartPageIndex() {
		return startPageIndex;
	}

	public void setStartPageIndex(int startPageIndex) {
		this.startPageIndex = startPageIndex;
	}

	public int getEndPageIndex() {
		return endPageIndex;
	}

	public void setEndPageIndex(int endPageIndex) {
		this.endPageIndex = endPageIndex;
	}
	
	
	/**
	 * 是否还有上一�?
	 * @return
	 */
	public boolean hasPreviousPage() {
		return getIndex() > 0;
	}
	
	
	/**
	 * 是否为第�?��
	 * @return
	 */
	public boolean isFirstPage() {
		return !hasPreviousPage();
	}
	
	/**
	 * 是否有下�?��
	 * @return
	 */
	public boolean hasNextPage() {
		return getIndex() + 1 < getTotalPage();
	}
	
	
	/**
	 * 是否为最后一�?
	 * @return
	 */
	public boolean isLastPage() {
		return !hasNextPage();
	}

	/** 构建显示的页码列表的索引 */
	private void buildPageIndex() {
		if (totalPage <= 10) { // a, 总页码不大于10�?
			startPageIndex = 1;
			endPageIndex = totalPage;
		} else { // b, 总码大于10�?
			// 在中间，显示前面4个，后面5�?
			startPageIndex = index - 4;
			endPageIndex = index + 5;

			// 前面不足4个时，显示前10个页�?
			if (startPageIndex < 1) {
				startPageIndex = 1;
				endPageIndex = 10;
			} else if (endPageIndex > totalPage) {// 后面不足5个时，显示后10个页�?
				endPageIndex = totalPage;
				startPageIndex = totalPage - 10 + 1;
			}
		}
	}
}
