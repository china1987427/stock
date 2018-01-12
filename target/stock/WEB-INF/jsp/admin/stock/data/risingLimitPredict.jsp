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

	});
</script>
</head>
<body>
	<div class="title">股票数据/大盘数据分析/<div class="ontitle" onclick="toThisPage(this)" name="${uri }">涨跌板预测</div></div>
	<div style="font-size:30px;">涨跌板预测</div>
</body>
</html>