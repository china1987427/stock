<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<style type="text/css">
#canvasPanel {
	position: relative;
	left: 20px;
	top: 20px;
}

#mycanvas {
	position: relative;
	border: 1px solid red;
}
#amountSum{
	position: relative;
	border: 1px solid red;
	top:10px;
/* 	width:1000px;
	height:300px; */
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

#show_num span {
	display: block;
	color: white;
}
</style>
<script type="text/javascript">
	var stockName = "${stockName}";
	var stockCode = "${stockCode}";
	function show_coords(event) {
		x = event.clientX;
		y = event.clientY;
		$("#show").append("<span>x:" + x + "y:" + y + "</span>");
	}
	function getCode(){
		return stockCode;
	}
	function getName(){
		return stockName;
	}
	function getYear(){
		var year = "${year}";
		return year;
	}
</script>
<div id="canvasPanel" onmousedown="show_coords(event)">
	<canvas id="mycanvas"></canvas>
	<canvas id="amountSum" class="aaaaa"></canvas>
</div>
<div id="show_num"></div>
<div id="show" style="position: relative;top:50px;"></div>
<script type="text/javascript" src="js/kline/kline.js"></script>
<script type="text/javascript" src="js/kline/amountSum.js"></script> 