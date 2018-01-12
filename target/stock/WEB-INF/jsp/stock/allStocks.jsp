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
<title>股票世界</title>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/stock.js"></script>
<link rel="stylesheet" href="css/stock.css" />
<style type="text/css">
ul {
	list-style: none;
}

ul li {
	width: 200px;
	float: left;
	line-height: 25px;
}

.shenzhen_stock span {
	float: left;
	margin-right: 90%;
}

.shanghai_stock span {
	float: left;
	margin-right: 90%;
}

#myStock {
	background: #fff none repeat scroll 0 0;
	border: 1px solid #dfdfdf;
	overflow-y: auto;
	overflow-x: auto;
	display: none;
	left: 65px;
	line-height: 13px;
	padding: 15px 12px;
	position: absolute;
	top: 50px;
	z-index: 10;
	width: 600px;
	position: fixed;
}
</style>
<script type="text/javascript">
	function selectMyStock(obj) {
		var code = $(obj).attr("id");
		var hall = $(obj).parents("div").attr("class");
		var mark;
		if (hall == "shanghai_stock") {
			mark = 1;
		} else if (hall == "shenzhen_stock") {
			mark = 2;
		}
		if (!isNotNull(code) && !isNotNull(hall)) {
			$("#myStock").load("stockWorld/showStock", {
				"mark" : mark,
				"stockCode" : code
			}, function() {
				$("#myStock ul li:eq(0)").css("width", "200px");
				$("#myStock").show();
			});
		}
	}
	function hideMyStock(obj) {
		$("#myStock").hide();
	}
	function isNotNull(param) {
		if (param == null || param == undefined || param == "") {
			return true;
		}
		return false;
	}
</script>
</head>
<body>
	<input type="hidden" id="backUrl" value="${backUrl}">
	<jsp:include page="../common/navi.jsp" flush="true" />
	<div class="top_box">
		<ul>
			<li class="all_stocks">全部股票</li>
			<li class="my_stock">自选股票</li>
		</ul>
	</div>
	<div class="shanghai_stock">
		<span style="font-size: 30px;">泸市:</span>
		<ul>
			<c:forEach items="${allStock}" var="ast">
				<c:if test="${ast.stock_market=='1'}">
					<li id="${ast.code}" onclick="selectMyStock(this)"><a
						href="javascript:void(0)">${ast.name}(${ast.code })</a></li>
				</c:if>
			</c:forEach>
		</ul>
	</div>

	<div class="shenzhen_stock">
		<span style="font-size: 30px;">深市:</span>
		<ul>
			<c:forEach items="${allStock}" var="ast">
				<c:if test="${ast.stock_market=='2'}">
					<li id="${ast.code}" onclick="selectMyStock(this)"><a
						href="javascript:void(0)">${ast.name}(${ast.code })</a></li>
				</c:if>
			</c:forEach>
		</ul>
	</div>
	<div id="myStock"></div>
	<input type="hidden" id="count">
	<input type="hidden" id="username" value="${username }">
</body>
<script type="text/javascript">
	
</script>
</html>