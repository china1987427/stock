package com.china.stock.admin.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.china.stock.admin.server.DapanDataService;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.tool.controller.BaseController;
import com.china.stock.common.util.StringUtil;



@Controller
@RequestMapping(value = "/dapanData")
public class DapanDataController extends BaseController {
	private static Logger log = Logger.getLogger(DapanDataController.class);
	@Autowired
	private DapanDataService dapanDataService;

	/**
	 * 大盘今日数据
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getdapanData")
	public String dapanData(Model model) {
		try {
			dapanDataService.verifydapanData();
			List<Map<String, Object>> list = dapanDataService.getDapan(StringUtil.getStrDate("yyyy-MM-dd", new Date()));
			if (CollectionUtils.isEmpty(list)) {
				String now = StringUtil.getStrDate("yyyy-MM-dd HH:mm:ss", new Date());
				String week = StringUtil.getWeek(now);
				Date d = null;
				if ("星期日".equals(week)) {
					d = new Date(StringUtil.getDate("yyyy-MM-dd HH:mm:ss", now).getTime() - 2 * 24 * 60 * 60 * 1000);
				} else if ("星期一".equals(week)) {
					d = new Date(StringUtil.getDate("yyyy-MM-dd HH:mm:ss", now).getTime() - 3 * 24 * 60 * 60 * 1000);
				}
				int n = 0;
				do {
					n++;
					d = new Date(StringUtil.getDate("yyyy-MM-dd HH:mm:ss", now).getTime() - n * 24 * 60 * 60 * 1000);
					list = dapanDataService.getDapan(StringUtil.getStrDate("yyyy-MM-dd", d));
				} while (CollectionUtils.isEmpty(list));
				if (!CollectionUtils.isEmpty(list)) {
					model.addAttribute("dapan", list.get(0));
				}
			} else {
				model.addAttribute("dapan", list.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/admin/dapan/data/dapandata";
	}

	/**
	 * 数据分析
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dataAnalysis")
	public String dataAnalysis(Model model) {
		try {
			String uri =getRequest().getRequestURI();
			model.addAttribute("uri", uri);
			float fall = dapanDataService.checkRiseOrFall("0");
			float rise = dapanDataService.checkRiseOrFall("1");
			float fallRate = fall / (fall + rise);
			float riseRate = rise / (fall + rise);
			DecimalFormat df = new DecimalFormat("######0.00");
			model.addAttribute("fallRate", df.format(fallRate * 100));
			model.addAttribute("riseRate", df.format(riseRate * 100));
			model.addAttribute("fall", (int) fall);
			model.addAttribute("rise", (int) rise);
			List<Map<String, Object>> max = dapanDataService.getMaxOrMinDate("max");
			if (!CollectionUtils.isEmpty(max)) {
				Map<String, Object> maxDate = (Map<String, Object>) max.get(0);
				model.addAttribute("maxDate", maxDate);
			}
			List<Map<String, Object>> min = dapanDataService.getMaxOrMinDate("min");
			if (!CollectionUtils.isEmpty(min)) {
				Map<String, Object> minDate = (Map<String, Object>) min.get(0);
				model.addAttribute("minDate", minDate);
			}
			List<Map<String, Object>> fallData = dapanDataService.getRiseOrFallData("0");
			List<Map<String, Object>> riseData = dapanDataService.getRiseOrFallData("1");
			List<Map<String, Object>> dateOfFall = dapanDataService.getDateRiseOrFall("0");
			if (!CollectionUtils.isEmpty(dateOfFall)) {
				List<Integer> list = new ArrayList<Integer>();
				List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
				List<Object> list2 = new ArrayList<Object>();
				for (int i = dateOfFall.size() - 1; i >= 0; i--) {
					Map<String, Object> map = dateOfFall.get(i);
					int differ = ObjUtil.toInt(map.get("differ"));
					if (i != dateOfFall.size() - 1) {
						if (list.get(0) != differ) {
							list2.add(list1);
						}
					}
					if (!CollectionUtils.isEmpty(list)) {
						if (list.get(0) == differ) {
							list1.add(map);
						} else {
							list1 = new ArrayList<Map<String, Object>>();
							list1.add(map);
						}
						if (i == 0 && list.get(0) != differ) {
							list2.add(list1);
						}
					} else {
						list1.add(map);
					}
					list.clear();
					list.add(differ);
				}
				model.addAttribute("dateOfFall", list2);
			}
			List<Map<String, Object>> dateOfRise = dapanDataService.getDateRiseOrFall("1");
			if (!CollectionUtils.isEmpty(dateOfRise)) {
				List<Integer> list = new ArrayList<Integer>();
				List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
				List<Object> list2 = new ArrayList<Object>();
				for (int i = dateOfRise.size() - 1; i >= 0; i--) {
					Map<String, Object> map = dateOfRise.get(i);
					int differ = ObjUtil.toInt(map.get("differ"));
					if (i != dateOfRise.size() - 1) {
						if (list.get(0) != differ) {
							list2.add(list1);
						}
					}
					if (!CollectionUtils.isEmpty(list)) {
						if (list.get(0) == differ) {
							list1.add(map);
						} else {
							list1 = new ArrayList<Map<String, Object>>();
							list1.add(map);
						}
						if (i == 0 && list.get(0) != differ) {
							list2.add(list1);
						}
					} else {
						list1.add(map);
					}
					list.clear();
					list.add(differ);
				}
				model.addAttribute("dateOfRise", list2);
			}
			List<Map<String, Object>> list = dapanDataService.getDapan("");
			if (!CollectionUtils.isEmpty(list)) {
				for (Map<String, Object> map : list) {

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/admin/dapan/data/dapanDataAnalysis";
	}

	/**
	 * 数据展示
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showData")
	public String showData(Model model) {
		try {
			String uri =getRequest().getRequestURI();
			model.addAttribute("uri", uri);
			List<Map<String, Object>> list = dapanDataService.getDapan("");
			if (!CollectionUtils.isEmpty(list)) {
				model.addAttribute("allStock", list);
				model.addAttribute("size1", (int)list.size()/4);
				model.addAttribute("size2", (int)list.size()/2);
				model.addAttribute("size3", (int)3*list.size()/4);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/admin/stock/data/showStocks";
	}

}
