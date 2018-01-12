package com.china.stock.common.filter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class AuthInterceptor extends HandlerInterceptorAdapter {
	private static Logger log = Logger.getLogger(AuthInterceptor.class);

	@Autowired
	private AuthService user;

	// 继承HandlerInterceptorAdapter类

	// 这个方法在业务处理器处理请求之前被调用，在该方法中对用户请求request进行处理。如果程序员决定该拦截器对请求进行拦截处理后还要调用其他的拦截器，或者是业务处理器去进行处理，
	// 则返回true；如果决定不需要再调用其他的组件去处理请求，则返回false。
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
		}
		return true;
	}

	// 这个方法在业务处理器处理完请求后，但是DispatcherServlet向客户端返回请求前被调用，在该方法中对用户请求request进行处理
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView mav)
			throws Exception {

	}

	// 这个方法在DispatcherServlet完全处理完请求后被调用，可以在该方法中进行一些资源清理的操作。
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception excptn)
			throws Exception {

	}
}