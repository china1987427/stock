package com.china.stock.common.ajax;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class AjaxController extends MultiActionController {
	private static final Logger LOGGER = Logger.getLogger(AjaxController.class);
	private Set memberIds;

	public AjaxController() {
		memberIds = null;
	}

	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setContentType("text/plain; charset=UTF-8");
			String outStr = generateServiceJavascript(request, response);
			if (outStr != null) {
				OutputStream out = response.getOutputStream();
				byte bout[] = outStr.getBytes("UTF-8");
				response.setIntHeader("Content-Length", bout.length);
				out.write(bout);
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void ajaxAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		response.setStatus(200);
		request.getRequestURI();
		String outStr = "";
		outStr = invokeService(request, response);
		try {
			outStr = new String(outStr.getBytes("ISO_8859_1"), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(outStr);
		out.flush();
		out.close();
	}

	private String generateServiceJavascript(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder strbuf = new StringBuilder();
		String serviceNameStr = request.getParameter("managerName");
		if (serviceNameStr.toLowerCase().endsWith("dao")) {
			return "您所访问的AJAX非法";
		}
		if (serviceNameStr == null || serviceNameStr.trim().length() == 0)
			return strbuf.toString();
		String serviceNames[] = serviceNameStr.split(",");
		String gateway = request.getServletPath();
		String currentServiceName = null;
		if (serviceNames != null && serviceNames.length > 0) {
			for (int j = 0; j < serviceNames.length; j++) {
				currentServiceName = serviceNames[j];
				if (!currentServiceName.matches("^[a-z0-9A-Z]+$"))
					return "";
				strbuf.append("var ").append(currentServiceName).append("=RemoteJsonService.extend({\n");
				strbuf.append("jsonGateway:\"").append(request.getContextPath()).append(gateway)
						.append("?method=ajaxAction&managerName=").append(currentServiceName).append("\"");
				try {
					Object service = getService(request, currentServiceName);
					Method methods[] = service.getClass().getMethods();
					Set methodNameSet = new TreeSet();
					for (int i = 0; i < methods.length; i++) {
						Method method = methods[i];
						if (method.getDeclaringClass().equals(service.getClass()))
							methodNameSet.add(method.getName());
					}

					String methodNames[] = new String[methodNameSet.size()];
					methodNameSet.toArray(methodNames);
					strbuf.append(",\n");
					for (int i = 0; i < methodNames.length; i++) {
						String methodName = methodNames[i];
						strbuf.append("     ").append(methodName).append(": function(){\n");
						strbuf.append((new StringBuilder("                  return this.ajaxCall(arguments,\""))
								.append(methodName).append("\");\n").toString());
						strbuf.append("}");
						if (i != methodNames.length - 1)
							strbuf.append(",");
						strbuf.append("\n");
					}

				} catch (Exception _ex) {
					_ex.printStackTrace();
				}
				strbuf.append("});");
				strbuf.append("\n");
			}

		}
		return strbuf.toString();
	}

	public static int firstAcronym(String word) {
		int n = 0;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			n = i;
			if (Character.isUpperCase(c)) {
				break;
			}
		}
		return n;
	}

	private Object getService(HttpServletRequest request, String serviceName) throws Exception {
		String name = serviceName.substring(0, firstAcronym(serviceName));
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/spring/spring-" + name + "-manager.xml");
		// ApplicationContext ac = new
		// ClassPathXmlApplicationContext("/spring/spring-test-manager.xml");
		Object service = ac.getBean("testManager");
		if ((service instanceof DataSource) || (service instanceof Session) || (service instanceof SessionFactory))
			service = null;
		if (service == null) {
			Exception e = new Exception(
					(new StringBuilder("can not find the service: ")).append(serviceName).toString());
			throw e;
		} else {
			return service;
		}
	}

	private String invokeService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String serviceName = request.getParameter("managerName");
		String methodName = request.getParameter("managerMethod");
		String strArgs = request.getParameter("arguments");
		try {
			strArgs = new String(strArgs.getBytes("ISO_8859_1"), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (serviceName == null || serviceName.trim().length() == 0 || !serviceName.matches("^[a-z0-9A-Z]+$"))
			return "";
		if (methodName == null || methodName.trim().length() == 0 || !methodName.matches("^[a-z0-9A-Z]+$"))
			return "";
		if (serviceName.toLowerCase().endsWith("dao")) {
			logger.error("不允许AJAX访问Dao");
			return "您所访问的AJAX非法";
		}
		Object service = getService(request, serviceName);
		Object result = invokeMethod(service, methodName, strArgs, serviceName);
		String retValue = JSONObject.toJSONString(result);
		return retValue;
	}

	private Object invokeMethod(Object service, String methodName, String strArgs, String serviceName) {
		Object result = null;
		if (strArgs == null || "".equals(strArgs) || "undefined".equals(strArgs))
			strArgs = "[]";
		try {
			JSONArray jsonarray = JSON.parseArray(strArgs);
			int argsNum = jsonarray.size();
			Method methods[] = service.getClass().getMethods();
			List candidateMethods = new ArrayList();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (method.getName().equals(methodName) && method.getParameterTypes().length == argsNum)
					candidateMethods.add(method);
			}

			Method targetMethod = null;
			Object args[] = (Object[]) null;
			Iterator iter = candidateMethods.iterator();
			while (iter.hasNext()) {
				Map map = new HashMap();
				Method method = (Method) iter.next();
				Class argTypes[] = method.getParameterTypes();
				args = new Object[argTypes.length];
				try {
					for (int i = 0; i < argTypes.length; i++)
						args[i] = map.put(jsonarray.get(i), argTypes[i]);

				} catch (Exception e) {
					LOGGER.warn("", e);
					continue;
				}
				targetMethod = method;
				break;
			}
			if (targetMethod != null) {
				result = targetMethod.invoke(service, args);
			} else {
				String errStr = (new StringBuilder("can not find the method [")).append(methodName)
						.append("], which has ").append(argsNum).append(" arguments.").toString();
				NoSuchMethodException ex = new NoSuchMethodException(errStr);
				throw ex;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}
}