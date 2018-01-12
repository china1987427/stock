package com.china.stock.common.tool.page;

import com.china.stock.common.tool.base.StrUtils;



public class SimplePage implements Paginable {
	public static final int DEF_COUNT = 15;

	public SimplePage() {
	}

	public SimplePage(int pageNo, int pageSize, int totalCount) {
		if (totalCount <= 0) {
			this.totalCount = 0;
		} else {
			this.totalCount = totalCount;
		}
		if (pageSize <= 0) {
			this.pageSize = DEF_COUNT;
		} else {
			this.pageSize = pageSize;
		}
		if (pageNo <= 0) {
			this.pageNo = 1;
		} else {
			this.pageNo = pageNo;
		}
		if ((this.pageNo - 1) * this.pageSize >= totalCount) {
			this.pageNo = totalCount / pageSize;
		}
	}

	/**
	 * 调整分页参数，使合理�?
	 */
	public void adjustPage() {
		if (totalCount <= 0) {
			totalCount = 0;
		}
		if (pageSize <= 0) {
			pageSize = DEF_COUNT;
		}
		if (pageNo <= 0) {
			pageNo = 1;
		}
		if ((pageNo - 1) * pageSize >= totalCount) {
			pageNo = totalCount / pageSize;
		}
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getTotalPage() {
		int totalPage = totalCount / pageSize;
		if (totalCount % pageSize != 0 || totalPage == 0) {
			totalPage++;
		}
		return totalPage;
	}

	public boolean isFirstPage() {
		return pageNo <= 1;
	}

	public boolean isLastPage() {
		return pageNo >= getTotalPage();
	}

	public int getNextPage() {
		if (isLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}

	public int getPrePage() {
		if (isFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}

	public String showPageString() {
		/**
		 * 类似如[ 1 2 3 4 5 6 7 8 9 10 ]的分页类
		 * 
		 * @return 字符�?sb
		 */
		StringBuffer sb = new StringBuffer();
		String fileName = this.pageURL;
		if (fileName.indexOf("?") == -1) {
			if (fileName.indexOf("pageNo") == -1) {
				fileName += "?pageNo={$pageNo}";
			}
		} else {
			if (fileName.indexOf("pageNo") == -1) {
				fileName += "&pageNo={$pageNo}";
			}
		}
		fileName = StrUtils.replaceAll(fileName, "pageNo=" + pageNo,
				"pageNo={$pageNo}");
		if (this.pageStyle == 1) {
			if (this.pageNo > this.getTotalPage()) {
				pageNo = this.getTotalPage();
			}
			int aa = pageNo - 5;
			int bb = pageNo + 4;
			if (aa <= 0) {
				aa = 1;
				bb = 10;
			}
			if (this.getTotalCount() > this.getPageSize()) {
				sb.append("共" + this.getTotalCount() + "条&nbsp;" + pageNo + "/"
						+ this.getTotalPage() + "页&nbsp;&nbsp;");
				if (pageNo > 1) {
					sb.append("<a href='"
							+ StrUtils.replaceAll(fileName, "{$pageNo}",
									(pageNo - 1) + "") + "'>[上一页]</a>&nbsp;");
				}

				int i = 0;
				sb.append("[ ");
				for (i = aa; i <= bb; i++) {
					if (i == pageNo) {
						sb.append("<font color='#ff0000'> " + i + " </font>");
						sb.append("&nbsp;");
					} else {
						sb.append("<a href='"
								+ StrUtils.replaceAll(fileName, "{$pageNo}", i
										+ "") + "'>" + i + "</a>&nbsp;");
					}
					if (i == this.getTotalPage()) {
						i++;
						break;
					}
				}
				sb.append(" ] ");
				if (pageNo < this.getTotalPage()) {
					sb.append("<a href='"
							+ StrUtils.replaceAll(fileName, "{$pageNo}",
									(pageNo + 1) + "") + "'>[下一页]</a>&nbsp;");
				}
			} else {
				sb.append("共" + this.getTotalCount() + "条");
			}
		}
		return sb.toString();
	}

	protected int totalCount = 0;
	protected int pageSize = 15;
	protected int pageNo = 1;
	protected int pageStyle = 1;
	protected String pageURL = "";
	protected int totalPage;
	protected int startIndex;
	protected int lastIndex;

	public int getPageStyle() {
		return pageStyle;
	}

	public void setPageStyle(int pageStyle) {
		this.pageStyle = pageStyle;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public String getPageURL() {
		return pageURL;
	}

	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}
	
	public int getStartIndex() {
		return startIndex;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}
}
