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
.bigDiv div{
	margin-top:10px;
}
</style>
<script type="text/javascript">
	$(function() {
		 $("#updateStock").on("click", function() {
			$.ajax({
				type : "POST",
				url : "sdhistory/getHistoryData",
				data : {},
				dataType : "json",
				success : function(data) {
					if (data) {
						alert("个股数据更新完成")
					}
				}
			});
		});  
		$("#limitupdown").click(function() {
			$.ajax({
				type : "POST",
				url : "stockdata/stockIntervalRisefall",
				data : {},
				dataType : "json",
				success : function(data) {
					if (data) {
						alert("个股数据更新完成")
					}
				}
			});
		});

		$("#test").click(function() {
			$.ajax({
				type : "POST",
				url : "stockdata/test",
				data : {},
				dataType : "json",
				success : function(data) {
					if (data) {
						alert("个股数据更新完成")
					}
				}
			});
		});

		$("#record").click(function() {
			$.ajax({
				type : "POST",
				url : "stockdata/recordRiseOrFallStock",
				data : {},
				dataType : "json",
				success : function(data) {
					if (data) {
						alert("个股数据更新完成")
					}
				}
			});
		});

		$("#count").click(function() {
			$.ajax({
				type : "POST",
				url : "stockdata/count",
				data : {},
				dataType : "json",
				success : function(data) {
					if (data) {
						alert("个股数据更新完成")
					}
				}
			});
		});
	});
</script>
</head>
<body>
	<div style="font-size:40px;" class="bigDiv">
		<div id="updateDapan">更新股票代码大全</div>
		<div id="updateStock">更新个股数据</div>
		<div id="limitupdown">涨停区间</div>
		<div id="test">测试</div>
		<div id="record">记录涨跌股</div>
		<div id="count">涨跌数量</div>
	</div>
</body>
</html>