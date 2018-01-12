<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<style type="text/css">
#scrollDiv {
	position: fixed;
	bottom: 0;
	width: 100%;
	border: 2px;
	color: red;
	background: #f2f2f2 no-repeat scroll 0 -52px;
	border: 1px solid red;
}

#scrollDiv div {
	float: left;
	width: 300px;
}
</style>
<script type="text/javascript">
	function start() {
		$("#scrollDiv marquee").attr("stopscroll", "false");
		$("#scrollDiv marquee").attr("SCROLLDELAY", "320");
	}
	function stop() {
		$("#scrollDiv marquee").attr("stopscroll", "true");
		$("#scrollDiv marquee").attr("SCROLLDELAY", "1000000000");
	}
</script>
</head>
<body>
	<div id="scrollDiv">
		<marquee onmouseout="start()" onmouseover="stop()" behavior="scroll"
			direction="left" width="100%" height="30px" SCROLLDELAY="320">
			<c:forEach items="${indexStock}" var="is">
				<div>
					<strong>${is.name }</strong> <span><fmt:formatNumber
							type="number" value="${is.currentPrice }" maxFractionDigits="2" /></span>
					<span><fmt:formatNumber type="number"
							value="${is.currentPrice-is.closingPrice}" maxFractionDigits="2" /></span>
					<span><fmt:formatNumber type="number" value="${is.increase}"
							maxFractionDigits="2" />%</span> <input type="hidden" name="state"
						value="${(is.currentPrice-is.closingPrice)<0}">
				</div>
			</c:forEach>
			<c:forEach items="${globalStock}" var="gs">
				<div>
					<strong>${gs.name }</strong> <span>${gs.curdot }</span> <span>${gs.curprice }</span>
					<span>${gs.rate }%</span>
				</div>
			</c:forEach>
		</marquee>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		$("input[name='state']").each(function() {
			var param=$(this).val();
			var state=false;
			if(param=="true"){
				state = true;
			}
			if (state) {
				$(this).parent().css("color", "green");
			}
		});
	});
</script>
</html>