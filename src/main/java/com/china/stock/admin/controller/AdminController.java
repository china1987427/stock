package com.china.stock.admin.controller;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import com.china.stock.admin.server.AdminService;
import com.china.stock.admin.server.StockDataService;
import com.china.stock.common.job.StockJob;
import com.china.stock.common.server.CommonDataService;
import com.china.stock.common.server.CommonFinanceService;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.tool.controller.BaseController;
import com.china.stock.common.tool.entity.MyException;
import com.china.stock.common.tool.page.Page;

@Controller
@RequestMapping(value = "/admin")
public class AdminController extends BaseController {
	private static Logger log = Logger.getLogger(AdminController.class);
	int timeout = 5000;
	@Autowired
	private AdminService adminService;
	@Autowired
	private StockDataService stockDataService;
	@Autowired
	private CommonDataService commonDataService;
	@Autowired
	private StockJob stockJob;
	@Autowired
	private CommonFinanceService finance;

	@RequestMapping(value = "/test")
	public String test(Model model) {
//		 commonDataService.saveStocksInfo();
//		 commonDataService.saveStocks("1");
//		 commonDataService.saveStocks("2");
//		 commonDataService.saveStocks("Y0002");
//		 commonDataService.saveStocks("Y0003");
		// stockJob.getHistoryData();
		finance.saveFinanceData();
		System.out.println("数据收集完成------------------------");
		return "jsp/test/test";
	}

	/**
	 * 首页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/homePage")
	public String homePage(Model model) {
		// String toPosition = getRequest().getParameter("toPosition");
		// model.addAttribute("toPosition", toPosition);
		return "jsp/admin/dapan/data/homePage";
	}

	/**
	 * 代码大全
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/toallStocks")
	public String toallStocks(Model model) {
		String toPosition = getRequest().getParameter("toPosition");
		model.addAttribute("toPosition", toPosition);
		return "jsp/admin/dapan/data/allStocks";
	}

	/**
	 * 查看全部股票
	 */

	@RequestMapping(value = "/getstock")
	public String getstock(Model model, Page<Map<String, Object>> page) {
		String returnJsp = "";
		try {
			String stockCode = getRequest().getParameter("stockCode");
			String toPosition = getRequest().getParameter("toPosition");
			model.addAttribute("toPosition", toPosition);
			String name = getRequest().getParameter("name");
			String marketMark = "";
			if ("sh".equals(name)) {
				marketMark = "1";
				returnJsp = "jsp/admin/dapan/data/shstocks";
			} else if ("sz".equals(name)) {
				marketMark = "2";
				returnJsp = "jsp/admin/dapan/data/szstocks";
			}
			Map<String, Object> map = adminService.getstock(stockCode, page.getIndex(), page.getPageSize(), marketMark);
			List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
			for (Map<String, Object> stock : list) {
				Object is_everydata = stock.get("is_everydata");
				if (StringUtils.isEmpty(is_everydata)) {
					String code = ObjUtil.toString(stock.get("code"));
					Map<String, Object> csed = stockDataService.checkStockEveryDayData(code);
					int count = ObjUtil.toInt(csed.get("count"));
					if (count >= 1) {
						stock.put("is_everydata", "yes");
						stockDataService.updateStockInfo(code, "yes");
						// stockDataService.raisingLimitPredict(code, "");
					} else {
						stockDataService.updateStockInfo(code, "no");
						stock.put("is_everydata", "no");
					}
				}
			}
			model.addAttribute("marketMark", marketMark);
			model.addAttribute("allStock", list);
			model.addAttribute("nameMark", name);
			page.setTotalRow(ObjUtil.toInt(map.get("total")));
			model.addAttribute("total", ObjUtil.toInt(map.get("total")));
			page.setRecords((List<Map<String, Object>>) map.get("list"));
		} catch (MyException e) {
			e.printStackTrace();
		}
		return returnJsp;
	}

}
