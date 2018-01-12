package com.china.stock.common.tool.page;

/**
 * 可分页接�?
 */
public interface Paginable {

	/**
	 * 总记录数
	 * 
	 * @return
	 */
	public int getTotalCount();

	/**
	 * 总页�?
	 * 
	 * @return
	 */
	public int getTotalPage();

	/**
	 * 每页记录�?
	 * 
	 * @return
	 */
	public int getPageSize();

	/**
	 * 当前页号
	 * 
	 * @return
	 */
	public int getPageNo();

	/**
	 * 是否第一�?
	 * 
	 * @return
	 */
	public boolean isFirstPage();

	/**
	 * 是否�?���?��
	 * 
	 * @return
	 */
	public boolean isLastPage();

	/**
	 * 返回下页的页�?
	 */
	public int getNextPage();

	/**
	 * 返回上页的页�?
	 */
	public int getPrePage();

	/**
	 * 分页样式
	 */
	public int getPageStyle();

	/**
	 * 页面路径
	 */
	public String getPageURL();
}
