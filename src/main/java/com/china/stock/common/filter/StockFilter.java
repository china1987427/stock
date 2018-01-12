package com.china.stock.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 执行顺序：过滤前 – 拦截前 – Action处理 – 拦截后 – 过滤后
 * @ClassName: FilterDemo01
 * @Description:filter的三种典型应用： 1、可以在filter中根据条件决定是否调用chain.doFilter(request,
 *                             response)方法， 即是否让目标资源执行
 *                             2、在让目标资源执行之前，可以对request\response作预处理，再让目标资源执行
 *                             3、在目标资源执行之后，可以捕获目标资源的执行结果，从而实现一些特殊的功能
 * @author: 孤傲苍狼
 * @date: 2014-8-31 下午10:09:24
 */
public class StockFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("----过滤器初始化----");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		// 对request和response进行一些预处理
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		System.out.println("FilterDemo01执行前！！！");
		chain.doFilter(request, response); // 让目标资源执行，放行
		System.out.println("FilterDemo01执行后！！！");
	}

	@Override
	public void destroy() {
		System.out.println("----过滤器销毁----");
	}
}
