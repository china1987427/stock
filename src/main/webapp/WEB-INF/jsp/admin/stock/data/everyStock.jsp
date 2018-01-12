<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String ctxPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/admin/admin.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>金融世界</title>
</head>
<body>
	<div class="title">
		股票数据/股票数据分析/<div class="upOneLevel" onclick="toThisPage(this)" name="stockdata">全部股票数据/</div>
		<div class="ontitle" onclick="selectMyStock(this)" name="${maxdateStock.stock_name}"
			id="${maxdateStock.stock_code }">k线图</div>
	</div>
	<div id="currentData"
		style="<c:if test='${maxdateStock.rise_or_fall>0}'>color:red</c:if><c:if test='${maxdateStock.rise_or_fall<=0}'>color:green</c:if>">
		<div class="mdsdate">
			<span>${maxdateStock.date}</span>
		</div>
		<div class="upOrdownInfo">
			涨停次数:<span>${limitupTime}</span>
			<c:if test="${limitupTime ne 0}">
				涨停日期:
				<c:forEach items="${limitupDate}" var="lud" varStatus="vs">
					<span>${lud}</span>
				</c:forEach>
			</c:if>
			<br>
			跌停次数:<span>${limitdownTime}</span>
			<c:if test="${limitdownTime ne 0}">跌停日期:
				<c:forEach items="${limitdownDate}" var="ldd" varStatus="vs">
					<span>${ldd}</span>
				</c:forEach>
			</c:if>
		</div>
		<div class="mdssck">
			<span>${maxdateStock.stock_name}(${maxdateStock.stock_code})</span> <span id="tokline"
				code="${maxdateStock.stock_code}" name="${maxdateStock.stock_name}">k线图</span><span
				id="weekAndMonthData" code="${maxdateStock.stock_code}">周月数据</span>
		</div>
		<div class="leftbox">
			<div class="bottombox">
				<div class="middle" id="mdsci">
					<span>${maxdateStock.closing_index}</span>
				</div>
				<div class="middle" id="mdsra">
					<span>${maxdateStock.rise_or_fall}</span> <span>${maxdateStock.amplitude}%</span>
				</div>
			</div>
			<div class="middle" id="holsh">
				<span>高:${maxdateStock.high_index}</span> <span class="moi">开:${maxdateStock.opening_index}</span>
				<span class="mli">低:${maxdateStock.low_index}</span> <span>量:${maxdateStock.total_hand}</span> <span>额:${maxdateStock.sum_money}</span>
				<span>额:${maxdateStock.handover}</span>
			</div>
		</div>
	</div>
	<div id="kline"></div>
	<div id="wamData"></div>
	<!-- <div id="aaa">网页获取数据</div>
	<div id="bbb">获取个股数据</div>  -->
</body>
</html>