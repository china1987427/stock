package com.china.stock.admin.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.china.stock.admin.server.UserService;
import com.china.stock.common.secret.MD5Util;
import com.china.stock.common.thread.HtmlUtil;
import com.china.stock.common.tool.base.EncryptUtil;
import com.china.stock.common.tool.base.ObjUtil;
import com.china.stock.common.tool.controller.BaseController;
import com.china.stock.common.util.SpeStrDeal;

@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {
	private static Logger log = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/area")
	public String area(Model model) {
		try{
			Document doc = HtmlUtil.parserHtml("http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html");
			Elements ele = doc.getElementsByClass("MsoNormal");
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for(int i=0;i<ele.size();i++){
				Map<String, Object> map = new HashMap<String, Object>();
				Elements b = ele.eq(i).select("b");
				if(!b.isEmpty()){
					continue;
				}else{
					//List<Map<String, Object>> province = userService.getProvince();
					
					String code = ele.eq(i).select("span").eq(1).text();
					code = new String(code.getBytes(),"GBK").replace("?", "");
					if(code.endsWith("00")){
						continue;
					}
					List<Map<String, Object>> parentProvince = userService.getCity(code.substring(0, 2));
					if(!CollectionUtils.isEmpty(parentProvince)){
						Map<String, Object> pro = parentProvince.get(0);
						String parentId = ObjUtil.toString(pro.get("id"));
						String regionCode = ObjUtil.toString(pro.get("region_code"));
						String name = ele.eq(i).select("span").eq(3).text();
						String fid = parentId;
						map.put("regionCode", code);
						map.put("regionName", name.trim());
						map.put("fid", fid);
						map.put("level", "3");
						list.add(map);
					}
				}
			}
			System.out.println(list.size());
			model.addAttribute("city", list);
			userService.saveRegion(list);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "jsp/test/test";
	}
	
	@RequestMapping(value = "/region")
	public String region(Model model) {
		try{
			Document doc = HtmlUtil.parserHtml("http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html");
			Elements ele = doc.getElementsByClass("MsoNormal");
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for(int i=0;i<ele.size();i++){
				Map<String, Object> map = new HashMap<String, Object>();
				Elements b = ele.eq(i).select("b");
				if(!b.isEmpty()){
					continue;
				}else{
					//List<Map<String, Object>> province = userService.getProvince();
					
					String code = ele.eq(i).select("span").eq(1).text();
					code = new String(code.getBytes(),"GBK").replace("?", "");
					if(!code.endsWith("00")){
						continue;
					}
					List<Map<String, Object>> parentProvince = userService.getParentProvince(code.substring(0, 2));
					if(!CollectionUtils.isEmpty(parentProvince)){
						Map<String, Object> pro = parentProvince.get(0);
						String parentId = ObjUtil.toString(pro.get("id"));
						String regionCode = ObjUtil.toString(pro.get("region_code"));
						String name = ele.eq(i).select("span").eq(3).text();
						String fid = parentId;
						String level = code.endsWith("00")?"2":"3";
						map.put("regionCode", code);
						map.put("regionName", name.trim());
						map.put("fid", fid);
						map.put("level", level);
						list.add(map);
					}
				}
			}
			model.addAttribute("city", list);
			System.out.println(list.size());
			userService.saveRegion(list);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "jsp/test/test";
	}
	@RequestMapping(value = "/test")
	public String test(Model model) {
		Document doc = HtmlUtil.parserHtml("http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html");
		Elements ele = doc.getElementsByClass("MsoNormal").select("b");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("regionCode", "中国");
		m.put("regionName", "中国");
		m.put("fid", "0");
		m.put("level", "0");
		list.add(m);
		for(int i=0;i<ele.size();i++){
			Map<String, Object> map = new HashMap<String, Object>();
			if(i%2!=0){
				continue;
			}
			if(ele.eq(i)==null){
				break;
			}
			try{
				String regionCode = ele.eq(i).text().trim();
				String regionName = ele.eq(i+1).text().trim();
				if("海南省".equals(regionCode)){
					regionName = regionCode;
					regionCode = ele.eq(i).get(0).siblingElements().text();
					map.put("regionCode", new String(regionCode.getBytes(),"GBK").replace("?", ""));
					map.put("regionName", regionName);
					map.put("fid", "1");
					map.put("level", "1");
					list.add(map);
					regionName = ele.eq(i + 1).text().trim();
				}
				if("重庆市".equals(regionName)){
					map = new HashMap<String, Object>();
					regionCode = ele.eq(i+1).get(0).siblingElements().text();
					map.put("regionCode", new String(regionCode.getBytes(),"GBK").replace("?", ""));
					map.put("regionName", regionName);
					map.put("fid", "1");
					map.put("level", "1");
					list.add(map);
					continue;
				}
				map.put("regionCode", new String(regionCode.getBytes(),"GBK").replace("?", ""));
				map.put("regionName", regionName);
				map.put("fid", "1");
				map.put("level", "1");
				list.add(map);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		userService.saveRegion(list);
		System.out.println("数据收集完成------------------------");
		return "jsp/test/test";
	}
	public boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}
	/**
	 * 登录管理员页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toManager")
	public String toManager(Model model) {
		return "jsp/admin/login/admin";

	}

	/**
	 * 管理员登录
	 * 
	 * @param username
	 * @param password
	 */
	@RequestMapping(value = "/managerLogin")
	public String managerLogin(Model model) {
		try {
			String username = getRequest().getParameter("username");
			if (StringUtils.isEmpty(username)) {
				Object object = getRequest().getSession().getAttribute("username");
				username = ObjUtil.toString(StringUtils.isEmpty(object) ? "" : object);
			}
			String password = getRequest().getParameter("password");
			if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)) {
				model.addAttribute("errorInfo", "yes");
				model.addAttribute("error", URLEncoder.encode("用户名或密码错误"));
				return "redirect:/user/toManager";
			}
			username = SpeStrDeal.stringFilter(username);
			Map<String, Object> result = userService.login(username, EncryptUtil.md5Digest(password, 32).toLowerCase());
			String un= ObjUtil.toString(result==null?"":result.get("user_name"));
			String pw = ObjUtil.toString(result==null?"":result.get("password")).toLowerCase();
			String pwd = MD5Util.MD5(password).toLowerCase();
			if(result==null||!un.equals(username)||!pw.equals(pwd)){
				model.addAttribute("errorInfo", "yes");
				model.addAttribute("error", URLEncoder.encode("用户名或密码错误"));
				return "redirect:/user/toManager";
			}
			String ismanager = ObjUtil.toString(result.get("ismanager"));
			if (!"1".equals(ismanager)) {
				model.addAttribute("errorInfo", "yes");
				model.addAttribute("error", URLEncoder.encode("不是管理员"));
				return "redirect:/user/toManager";
			}
			getRequest().getSession().setAttribute("username", username);
			model.addAttribute("username", username);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/admin/dapan/data/main";
	}

	/**
	 * 登录页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toLogin")
	public String toLogin() {
		return "jsp/user/login/login";

	}

	/**
	 * 注册
	 * 
	 * @return
	 */
	@RequestMapping(value = "/register")
	public String register(Model model) {
		List<Map<String, Object>> list = userService.getArea(0, 1, 0, "");
		model.addAttribute("province", list);
		return "jsp/user/login/register";
	}

	/**
	 * 获取省市区
	 * 
	 * @param model
	 */
	@ResponseBody
	@RequestMapping(value = "/getArea")
	public void getArea(Model model) {
		try {
			String id = getRequest().getParameter("id");
			if (StringUtils.isEmpty(id)) {
				id = "0";
			} else {
				id = id.trim();
			}
			String fatherId = getRequest().getParameter("fatherId");
			if (StringUtils.isEmpty(fatherId)) {
				fatherId = "0";
			} else {
				fatherId = fatherId.trim();
			}
			String level = getRequest().getParameter("level");
			if (StringUtils.isEmpty(level)) {
				level = "0";
			} else {
				level = level.trim();
			}
			List<Map<String, Object>> list = userService.getArea(ObjUtil.toInt(id), ObjUtil.toInt(fatherId),
					ObjUtil.toInt(level), "");
			Map<String, Object> area = new HashMap<String, Object>();
			for (Map<String, Object> map : list) {
				String idArea = map.get("id") + "";
				String name = map.get("region_name") + "";
				area.put(idArea, name);
			}
			WriterJson(ObjUtil.toString(area.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	/**
	 * 主页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		String username = getRequest().getParameter("username");
		String identity = getRequest().getParameter("identity");
		model.addAttribute("username", username);
		model.addAttribute("identity", identity);
		return "stock/main";

	}

	/**
	 * 登录
	 * 
	 * @param username
	 * @param password
	 */
	@RequestMapping(value = "/login")
	public String login(String username, String password, Model model) {
		String backUrl = getRequest().getParameter("backUrl");
		try {
			if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)) {
				return "redirect:/user/toLogin?backUrl=" + backUrl;
			}
			username = SpeStrDeal.stringFilter(username);
			Map<String, Object> result = userService.login(username, EncryptUtil.md5Digest(password, 32).toLowerCase());
			String u = ObjUtil.toString(result.get("username"));
			String p = ObjUtil.toString(result.get("password"));
			if (!"false".equals(u) && !"false".equals(p)) {
				return "redirect:/" + backUrl + "?username=" + username;
			}
			if ("false".equals(u) && !"false".equals(p)) {
				model.addAttribute("errorInfo", "yes");
				model.addAttribute("error", "用户名错误");
			}
			if (!"false".equals(u) && "false".equals(p)) {
				model.addAttribute("errorInfo", "yes");
				model.addAttribute("error", "密码错误");
			}
			model.addAttribute("backUrl", backUrl);
			model.addAttribute("username", username);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return "jsp/user/login/login";
	}

	/**
	 * 验证
	 * 
	 * @param value
	 * @param type
	 */
	@ResponseBody
	@RequestMapping(value = "/verify")
	public void verify(String value, String type) {
		String result = userService.verify(value, type);
		try {
			WriterJson(result);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}

	/**
	 * 验证验证码
	 * 
	 * @param captcha
	 */
	@ResponseBody
	@RequestMapping(value = "/verifyCode")
	public void verifyCode(String captcha) {
		String code = (String) getRequest().getSession().getAttribute("code");
		if (!StringUtils.isEmpty(code)) {
			code = code.toLowerCase();
			captcha = captcha.toLowerCase();
			try {
				if (code.equals(captcha)) {
					WriterJson("true");
				} else {
					WriterJson("false");
				}
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
			}
		}

	}

	/**
	 * 注册操作
	 */
	@ResponseBody
	@RequestMapping(value = "/registering")
	public void registering(String username, String password, String mobile, String email, String province,
			String city, String county, String street) {
		try {
			if (!StringUtils.isEmpty(province)) {
				province = province.trim();
			}
			if (!StringUtils.isEmpty(city)) {
				city = city.trim();
			}
			if (!StringUtils.isEmpty(county)) {
				county = county.trim();
			}
			String result = userService.registering(username, EncryptUtil.md5Digest(password, 32)
					.toLowerCase(), mobile, email, province, city, county, street);
			if ("true".equals(result)) {
				getRequest().getSession().setAttribute("username", username);
				getResponse().sendRedirect("/stock/stockWorld/toStock?username=" + username);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}
}
