<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	$(function() {
		$("#sh_stock").load("admin/getstock", {
			"name" : "sh",
			"toPosition" : "all"
		});
		$("#sz_stock").load("admin/getstock", {
			"name" : "sz",
			"toPosition" : "all"
		});
		$(".stockarea").find("a").attr("href", "javascript:void(0)");
	});
</script>
<c:if test="${empty toPosition}">
	<div class="title">
		股票管理/
		<div class="ontitle" onclick="toAllShSzStocks(this)" name="all" style="left:-170px;width:150px;">股票代码大全</div>
	</div>
</c:if>
<div id="sh_stock" class="stockarea" name="sh"></div>
<div id="sz_stock" class="stockarea" name="sz"></div>
