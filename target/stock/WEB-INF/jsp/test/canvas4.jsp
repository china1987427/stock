<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>canvas test</title>
<base href="<%=basePath%>">
<head>
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	var stockCode = "${stockCode}";
	var stockname = "${stockname}";
</script>
<style type="text/css">
#canvasPanel {
	position: relative;
	left: 20px;
	top: 20px;
}

#mycanvas {
	position: absolute;
	border: 1px solid red;
}

#show_num {
	position: absolute;
	z-index: 111;
	border-style: solid;
	white-space: nowrap;
	transition: left 0.4s ease 0s, top 0.4s ease 0s;
	background-color: rgba(0, 0, 0, 0.7);
	border-width: 0px;
	border-color: rgb(51, 51, 51);
	border-radius: 4px;
	padding: 5px;
}
#show_num span{
	display:block;
	color:white;
}
</style>
<script type="text/javascript">
var stockname = "${stockname}";
var stockCode = "${stockCode}";
	function show_coords(event) {
		x = event.clientX;
		y = event.clientY;
		$("#show").append("<span>x:" + x + "y:" + y + "</span>");
	}
</script>
</head>
<body onmousedown="show_coords(event)">
	<div id="canvasPanel">
		<canvas id="mycanvas"></canvas>
	</div>
<!-- 	<div id="show_num"></div> -->
	<!-- <div style="text-align: center; clear: both"></div> -->
</body>
<script type="text/javascript" src="js/realtime/canvas.js"></script>
</html>