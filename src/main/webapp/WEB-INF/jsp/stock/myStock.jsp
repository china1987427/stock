<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>自选股</title>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<style type="text/css">
ul {
	list-style: none;
}

ul li {
	width: 200px;
	float: left;
	line-height: 25px;
}

.mystocks ul{
	top: 200px;
	position: relative;
	float:right;
}
</style>
<script type="text/javascript">
$(function() {
	setInterval(aaa, 20000);
	function aaa() {
		var username = "${param.username}";
		$(".mystocks").load("stockWorld/showMyStock", {
			'username' : username
		}, function(response, status, xhr) {

		});
	}
});
</script>
</head>
<body>
	<jsp:include page="../common/navi.jsp" flush="true" />
	<div class="top_box">
		<ul>
			<li class="all_stocks"
				onclick="window.location.href='stockWorld/allStocks?username=${param.username}'">全部股票</li>
			<li class="my_stock">自选股</li>
		</ul>
	</div>
	<div class="mystocks" style="border:2px;">
		<c:forEach items="${myStock}" var="ms">
		<input type="hidden" id="code" value="${ms.stock_code}"
						mark="${ms.market}" stockName="${ms.stock_name}" />
			<ul>
				<c:forEach items="${ms.stockData}" var="mssd">
					<c:if test="${mssd.currentPrice-mssd.closingPrice>0}">
						<li style="color: red;">${ms.stock_name}(${ms.stock_code})</li>
						<li style="color: red;">最新:${mssd.currentPrice}</li>
						<li style="color: red;">涨幅:<fmt:formatNumber type="number"
								value="${mssd.increase}" maxFractionDigits="2" />%
						</li>
						<li style="color: red;">涨跌:${mssd.currentPrice-mssd.closingPrice}</li>
					</c:if>
					<c:if test="${mssd.currentPrice-mssd.closingPrice<0}">
						<li style="color: green;">${ms.stock_name}(${ms.stock_code})</li>
						<li style="color: green;">最新:${mssd.currentPrice}</li>
						<li style="color: green;">涨幅:<fmt:formatNumber type="number"
								value="${mssd.increase}" maxFractionDigits="2" />%
						</li>
						<li style="color: green;">涨跌:${mssd.closingPrice-mssd.currentPrice}</li>
					</c:if>
					<c:if
						test="${mssd.currentPrice-mssd.closingPrice ge 0.00 && mssd.currentPrice-mssd.closingPrice le 0.00}">
						<li style="color: black;">${ms.stock_name}(${ms.stock_code})</li>
						<li style="color: black;">最新:${mssd.currentPrice}</li>
						<li style="color: black;">涨幅:<fmt:formatNumber type="number"
								value="${mssd.increase}" maxFractionDigits="2" />%
						</li>
						<li style="color: black;">涨跌:${mssd.closingPrice-mssd.currentPrice}</li>
					</c:if>
				</c:forEach>
			</ul>
		</c:forEach>
	</div>
</body>
<script type="text/javascript">
	
</script>
</html>