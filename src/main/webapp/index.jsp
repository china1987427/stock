<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">

<title>欢迎进入金融世界</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/jquery-debug.js"></script>
<!-- <script type="text/javascript" src="js/prototype.js"></script> -->
<script type="text/javascript" src="js/Moo-debug.js"></script>
<script type="text/javascript" src="js/jsonGateway-debug.js"></script>
<script type="text/javascript" charset="UTF-8"
	src="<c:url value='/ajax.do?method=index&managerName=testManager' />"></script>
<!-- <script type="text/javascript" src="js/seeyon.ui.checkform-debug.js"></script> -->
<script type="text/javascript">
	$(function() {
		$("#mmmmm").click(function() {
			var tm = new testManager();
			var s = "600243";
			var result = tm.getStock(s);
			alert(result);
		});
	});

	/* var testManager=RemoteJsonService.extend({
		jsonGateway:"/stock/ajax.do?method=ajaxAction&managerName=testManager",
		     getStock: function(){
		                  return this.ajaxCall(arguments,"getStock");
		},
		     init: function(){
		                  return this.ajaxCall(arguments,"init");
		}
		});
	var tm = new testManager();
	var result =tm.getStock("600243");
	alert(result) */
</script>
</head>
<body>
	<div style="text-align: center;" onclick="window.location.href='news/toNews'">
		<span style="font-size: 80px;">进入金融世界</span>
	</div>
	<span onclick="window.location.href='analysis.do?method=analysis'">ok</span>
	<span id="mmmmm">aaaaaaaaaaaaaaaaaaaaaaa</span>
	<hr>
	<span onclick="window.location.href='stockdata/testKline?stockCode=600000'">k线图</span>
	<br />
	<span onclick="window.location.href='stockWorld/getAverageLineList?stockCode=002043&&line=5'">mmmmmmmmmmmmmmmmm</span>
	<span onclick="window.location.href='stockWorld/getCurrentData'">实时数据</span>
	<span onclick="window.location.href='admin/test'">同花顺数据</span>
	<span onclick="window.location.href='http://stockpage.10jqka.com.cn/realHead_v2.html#hs_603466'">XXXXX</span>
</body>
</html>
