package com.china.stock.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

//@Controller
//@RequestMapping("/analysis.do")
public class DataAnalysisController extends MultiActionController{
//	@RequestMapping(params="method=analysis")
	public ModelAndView analysis(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("==============================");
		return null;
		
	}
//	@RequestMapping(params="method=login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("-------------------------------------------------");
		return null;
		
	}
}
