<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
<script type="text/javascript">
	function toThisPage(){
		$("#showData").click();
	}
</script>
</head>
<body>
	<div class="title">股票数据/大盘数据分析/<div class="ontitle" onclick="toThisPage(this)" name="showData" style="left: -38px;width:150px;">泸指数据展示</div></div>
	<c:forEach items="${allStock}" var="stocks" varStatus="vs">
		<c:if test="${vs.index==0||vs.index==size1+1||vs.index==size2+1||vs.index==size3+1}">
			<div class="region">
		</c:if>
		<div
			class="<c:if test="${stocks.riseorfall_case==1}">riseStock</c:if><c:if test="${stocks.riseorfall_case==0}">fallStock</c:if>">
			<span>${fn:substring(stocks.date, 5, 7)}</span><span>${stocks.date }</span> <span>${stocks.closing_index
				}</span> <span>${stocks.day_in_week }</span> <span>${stocks.total_hand }</span>
		</div>
		<c:if test="${vs.index==size1||vs.index==size2||vs.index==size3||vs.index==fn:length(allStock)}">
			</div>
		</c:if>
	</c:forEach>
</body>
</html>