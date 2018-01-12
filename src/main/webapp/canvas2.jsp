<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>canvas test</title>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<title>HTML5 Canvas 柱形图 鼠标滑过显示百分比DEMO演示</title>
<style type="text/css">
#canvas {
	position: relative;
	border:5px solid red;
	left:150px;
	top:150px;
}

#show_num {
	position: absolute;
	z-index: 111;
}

.show_num {
	border: 1px solid #bfbfbf;
	background-color: #fff;
	width: 90px;
	height: 24px;
	line-height: 24px;
	text-indent: 5px;
	position: relative;
	z-index: 0;
}

#show_num em {
	position: absolute;
	bottom: -7px;
	left: 4px;
	overflow: hidden;
	width: 13px;
	height: 13px;
	background: #fff;
	border-bottom: 1px solid #bfbfbf;
	border-right: 1px solid #bfbfbf;
	-webkit-transform: rotate(45deg);
	-moz-transform: rotate(45deg);
	-o-transform: rotate(45deg);
	transform: rotate(45deg);
	z-index: -2;
}
.show {
    background: #fff none repeat scroll 0 0;
    border: 1px solid #dfdfdf;
    display: none;
    left: 65px;
    line-height: 13px;
    overflow: auto;
    padding: 15px 12px;
    position: absolute;
    top: 50px;
    z-index: 10;
}
</style>
<script type="text/javascript">
	function show_coords(event) {
		x = event.clientX;
		y = event.clientY;
		$("#show").append("<span>x:"+x+"y:"+y+"</span>");
	}
</script>
</head>
<body onmousedown="show_coords(event)">

	<canvas id="canvas"></canvas>

	<div id="show_num"></div>

	<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="js/canvas/canvas.js"></script>

	<div style="text-align: center; clear: both"></div>
	<div id="show"></div>
</body>
</html>