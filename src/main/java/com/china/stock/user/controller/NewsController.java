package com.china.stock.user.controller;



import java.util.Date;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.china.stock.common.server.CommonDataService;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.tool.base.RedisUtil;
import com.china.stock.common.tool.controller.BaseController;
import com.china.stock.common.util.StringUtil;



@Controller
@RequestMapping(value = "/news")
public class NewsController extends BaseController {
	private static Logger log = Logger.getLogger(NewsController.class);
	@Autowired
	private CommonDataService commonService;

	@RequestMapping(value = "/toNews")
	public String toNews(Model model) {
		try {
			JSONArray result =  new JSONArray();
			String username = getRequest().getParameter("username");
			boolean isTimeDiffer =false;
			getRequest().setAttribute("username", username);
			String oldtime =RedisUtil.get("oldtime");
			if(!StringUtils.isEmpty(oldtime)){
				isTimeDiffer =StringUtil.isTimeDiffer(new Date(ObjUtil.toLong(oldtime)));
				if (isTimeDiffer) {
					result = JSONArray.parseArray(RedisUtil.get("stock_list"));
					RedisUtil.del("oldtime");
				}
			}
			JSONArray stock = new JSONArray();
			if(CollectionUtils.isEmpty(result)){
				stock =commonService.getStock();
				stock.add(StringUtil.getStrDate("yyyy-MM-dd HH:mm:ss", new Date()));
				RedisUtil.set("stock_list", stock.toString());
			}else{
				RedisUtil.set("oldtime",  ObjectUtils.toString(System.currentTimeMillis()));
				stock=result;
			}
			model.addAttribute("stockInfo", stock.get(0));
			model.addAttribute("globalInfo", stock.get(1));
			model.addAttribute("newsContent", commonService.getNews("1"));
			model.addAttribute("weather", commonService.getWeather());
			model.addAttribute("backUrl", "news/toNews");
			model.addAttribute("date", commonService.getDate().split("date")[0]);
			model.addAttribute("week", commonService.getDate().split("date")[1]);
			getRequest().setAttribute("indexStock", stock.get(0));
			getRequest().setAttribute("globalStock", stock.get(1));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/news/news";
	}

	@RequestMapping(value = "/scrollNews")
	public String scrollNews(Model model) {
		try {
			String page = getRequest().getParameter("page");	
			model.addAttribute("scrollNewsContent", commonService.getNews(page));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/news/scrollNews";
	}
}
