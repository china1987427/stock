<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String ctxPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>金融世界</title>
<style type="text/css">
</style>
<script type="text/javascript">
	$(function() {
		<c:if test="${dapan.rise_or_fall gt 0}">
		$(".middle").css("color", "red");
		</c:if>
		<c:if test="${dapan.rise_or_fall lt 0}">
		$(".middle").css("color", "green");
		</c:if>
		$("#tokline").click(function() {
			$("#kline").load("stockdata/toMarketKline", {
				"stockCode" : "dapan"
			});
		});
	});
</script>
</head>
<body>
	<div class="title">
		股票数据/大盘数据分析/
		<div class="ontitle" onclick="toThisPage(this)" name="dapandata" style="left: -38px;width:150px;">泸指今日数据</div>
	</div>
	<div>
		<div class="middle">
			<span class="dapandate">${dapan.date}</span> <span id="tokline">k线图</span>
		</div>
		<div class="leftbox">
			<div class="bottombox">
				<div class="middle" id="mci">
					<span>${dapan.closing_index}</span>
				</div>
				<div class="middle" id="mra">
					<span>${dapan.rise_or_fall}</span> <span>${dapan.amplitude}%</span>
				</div>
			</div>
			<div class="middle" id="mot">
				<span>高:${dapan.high_index}</span> <span class="moi">开:${dapan.opening_index}</span> <span
					class="mli">低:${dapan.low_index}</span> <span>量:${dapan.total_hand}</span> <span>额:${dapan.sum_money}</span>
			</div>
		</div>
	</div>
	<div id="kline"></div>
</body>
</html>