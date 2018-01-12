package com.china.stock.common.job;

import java.util.Map;

import org.jfree.util.Log;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.china.stock.admin.server.DapanDataService;
import com.china.stock.admin.server.StockDataService;
import com.china.stock.common.server.CommonAdminService;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.tool.controller.BaseController;

@Service
public class StockJob extends BaseController implements Job {
	//private final static String html = "http://forex.eastmoney.com/CNY.html";
	private final static String html = "http://quotes.money.163.com/old/#query=EQA&DataType=HS_RANK&sort=PERCENT&order=desc&count=24&page=0";
	static int timeout = 5000;
	@Autowired
	private StockJobService stockJobService;
	@Autowired
	private StockDataService stockDataService;
	@Autowired
	private CommonAdminService commonAdminService;
	@Autowired
	private DapanDataService dapanDataService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
	}

	public static void main(String[] args) {
		StockJob.getRMBExchangeRate();
	}

	public void getHistoryData() {
		System.out.println("-----getHistoryData");
		Map<String, Object> map = stockDataService.getStatus("stock");
		try {
			String isGetData = ObjUtil.toString(map.get("is_get_data"));
			if ("no".equals(isGetData)) {
				stockJobService.getHistoryData();
				dapanDataService.verifydapanData();
				commonAdminService.stockIntervalRisefall();
			}
			stockDataService.changeStatus("stock", "yes");
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e);
		}
	}

	public void changeStatus() {
		System.out.println("-----changeStatus");
		stockDataService.changeStatus("stock", "no");
		stockDataService.changeStatus("dapan", "no");
	}

	public static void getRMBExchangeRate() {
		// String today = StringUtil.getStrDate("yyyy-MM-dd", new Date());
		// boolean isGet = stockJobService.isGetRMBExchangeRate(today);
		System.out.println("-----getRMBExchangeRate----");
		Document doc = CommonAdminService.parserHtml(html);
		int m = 1;
		do {
			timeout = (m == 1) ? timeout : timeout + 5000 * (m++);
			doc = CommonAdminService.parserHtml(html);
			if (m == 5) {
				break;
			}
		} while (doc == null);
		Elements elements = doc.getElementsByAttributeValue("id", "tab1_zone2").select("ul");
		for (int i = 1; i < elements.size(); i++) {
			Elements ele = elements.get(i).select("li");
//			String currencyCode = ele.get(0).select("a").text();
//			String currencyName = ele.get(1).select("a").text();
//			String newPrice = ele.get(2).select("span").text();
//			String changeAmount = ele.get(3).select("span").text();
//			String riseorfallRange = ele.get(4).select("span").text();
//			String opening = ele.get(5).select("span").text();
//			stockDataService.saveChangeRate(currencyCode, currencyName, newPrice, changeAmount,
//					riseorfallRange, opening);
		}
		System.out.println(doc);
	}
}
