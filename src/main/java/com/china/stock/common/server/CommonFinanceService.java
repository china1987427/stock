package com.china.stock.common.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.china.stock.admin.dao.StockDataDao;
import com.china.stock.admin.entity.Finance;
import com.china.stock.admin.server.DapanDataService;
import com.china.stock.common.thread.HtmlUtil;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.util.StringUtil;

@Service
public class CommonFinanceService {
	private static int timeout = 5000;
	@Autowired
	private DapanDataService dapanDataService;
	@Autowired
	private StockDataDao stockDataDao;

	public void saveFinanceData() {
		String stockCode = "";
		try {
			String html = "http://stockdata.stock.hexun.com/2009_zxcwzb_stockCode.shtml";
			List<Map<String, Object>> stocks = dapanDataService.getAllStocks("");
			List<Finance> list = new ArrayList<Finance>();
			for (int i = 0; i < stocks.size(); i++) {
				List<Finance> finance = new ArrayList<Finance>();
				Finance finance1 = new Finance();
				Finance finance2 = new Finance();
				Finance finance3 = new Finance();
				finance.add(finance1);
				finance.add(finance2);
				finance.add(finance3);
				
				Map<String, Object> stock = stocks.get(i);
				stockCode = ObjUtil.toString(stock.get("code"));
				String stockName = ObjUtil.toString(stock.get("name"));
				int m = 1;
				Document doc = null;
				do {
					timeout = (m == 1) ? timeout : timeout + 5000 * (m++);
					doc = HtmlUtil.parserHtml(html.replace("stockCode", stockCode));
					if (m == 5) {
						break;
					}
				} while (doc == null);
				Elements ele = doc.getElementById("zaiyaocontent").getElementsByClass("tishi");
				for (int n = 1; n <= 3; n++) {
					int index = n - 1;
					finance.get(index).setStockCode(stockCode);
					finance.get(index).setStockName(stockName);
					// 日期
					finance.get(index).setDate(
							ele.eq(n).text() == null || "".equals(ele.eq(n).text()) ? null : StringUtil.getDate("yyyy-MM-dd", ele.eq(n).text()
									.replace(".", "-")));
					finance.get(index).setProceedsOfBusiness(ele.eq(n + 4).text());// 营业收入
					finance.get(index).setNetProfit(ele.eq(n + 2 * 4).text());// 净利润（元)
					finance.get(index).setTotalProfit(ele.eq(n + 3 * 4).text());// 利润总额（元)
					finance.get(index).setTotalAssets(ele.eq(n + 5 * 4).text());// 总资产（元)
					list.add(finance.get(index));
				}
			}
			stockDataDao.saveFianceData(list);
		} catch (Exception e) {
			System.out.println("error-----------------" + stockCode);
			e.printStackTrace();
		}
	}
}
