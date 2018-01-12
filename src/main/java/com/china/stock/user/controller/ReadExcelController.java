package com.china.stock.user.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.china.stock.common.tool.controller.BaseController;
import com.china.stock.common.util.ReadExcelUtils;
import com.china.stock.user.server.ReadExcelService;



@Controller
@RequestMapping(value = "/readExcel")
public class ReadExcelController extends BaseController{
	private static Logger log = Logger.getLogger(ReadExcelController.class);
	private static String filepath = "E:/stock.xlsx";
	@Autowired
	private ReadExcelService readExcelService;
	
	@ResponseBody
	@RequestMapping(value = "/saveData")
	public void saveData(Model model) {
		try {
			String stockCode=getRequest().getParameter("stockCode");
			String stockName=getRequest().getParameter("stockName");
			String peRatio=getRequest().getParameter("peRatio");
			String region=getRequest().getParameter("region");
			String industry=getRequest().getParameter("industry");
			String mainBusiness=getRequest().getParameter("mainBusiness");
			ReadExcelUtils excelReader = new ReadExcelUtils(filepath);
			Map<Integer, Map<Integer, Object>> map = excelReader.readExcelContent();
			int result=0;
			for (Integer key : map.keySet()) {
				List list = new ArrayList();
				Map<Integer, Object> stock = map.get(key);
				for (Integer n : stock.keySet()) {
					list.add(stock.get(n));
				}
				result=readExcelService.saveData(list,stockCode,stockName);
			}
			int param=readExcelService.saveToStock(stockCode, stockName,peRatio,region,industry,mainBusiness);
			if(result==1&&param==1){
				WriterJson("true");
			}else{
				WriterJson("false");
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}
}
