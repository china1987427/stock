<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<title>财经新闻</title>
<base href="<%=basePath%>">
<link rel="stylesheet" href="css/news.css" />
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/news.js"></script>
<script type="text/javascript" src="js/time.js"></script>
<style type="text/css">
#time {
	float: left;
}
</style>
<script type="text/javascript">
	$(function() {
		var obj = $("div[mark='shanghai']");
		showS(obj);
	});
	function showS(obj) {
		var mark = $(obj).attr("mark");
		if (mark == "shanghai") {
			$(obj).siblings("[mark='gem']").css("top", "");
			$(obj).siblings("[mark='shenzhen']").css("top", "");
			$(obj).siblings("[mark='gem']").css({
				"bottom" : "70px",
				"position" : "absolute"
			});
			$(obj).siblings("[mark='shenzhen']").css({
				"bottom" : "100px",
				"position" : "absolute"
			});
		} else if (mark == "shenzhen") {
			$(obj).siblings("[mark='gem']").css("top", "");
			$(obj).css("bottom", "");
			$(obj).css("top", "80px");
			$(obj).siblings("[mark='gem']").css({
				"bottom" : "0px",
				"position" : "absolute"
			});
		} else if (mark == "gem") {
			$(obj).css("top", "120px");
		}
		$(obj).find("img").show();
		$(obj).siblings(".showS").find("img").hide();
	}

	function hideS(obj) {
		$(obj).find("img").hide();
	}
</script>
</head>
<body>
	<input type="hidden" id="backUrl" value="${backUrl}">
	<div>
		<c:forEach items="${weather}" var="w">
			<div id="weather" style="float:left;">
				<span>${w.temp }℃</span> <span>${w.city }</span> <span>${w.weather }</span>
			</div>
		</c:forEach>
		<div id="time"></div>
		<jsp:include page="../common/navi.jsp" flush="true" />
	</div>
	<div class="top">
		<ul class="nav">
			<li class="nav" onclick="window.location.href='news/toNews'">首页</li>
			<li class="nav"
				onclick="window.location.href='stockWorld/toStock?username=${param.username}'">股票</li>
			<li class="nav">新股</li>
			<li class="nav">基金</li>
			<li class="nav">期货</li>
			<li class="nav">外汇</li>
			<li class="nav">港股</li>
			<li class="nav">论坛</li>
		</ul>
	</div>
	<div class="news">
		<div class="active">
			<ul class="tab_box">
				<li class="on"><a class="main">财经要闻</a></li>
				<li><a class="scroll">流动新闻</a></li>
			</ul>
			<div class="refresh_area" style="display: none;">
				<input type="checkbox" checked="checked"> <span><span
					class="ft_refresh">60</span>秒后刷新</span> <span class="refresh">刷新</span>
			</div>
			<input type="hidden" id="page" value="2"> <input
				type="hidden" id="toScroll"> </span>
		</div>
		<div class="box">
			<ul class="link_news" style="display: inline;">
				<c:forEach items="${newsContent}" var="nc">
					<li><a href="${nc.link }" title="${nc.desc}">${nc.title }</a></li>
				</c:forEach>
			</ul>
		</div>
		<div class="scroll_box"></div>
	</div>
	<div class="index">
		<div class="stock">
			<ul>
				<li id="showStock">股票</li>
				<li id="global">全球</li>
			</ul>
		</div>
		<c:forEach items="${stockInfo}" var="si">
			<div class="showS" onmouseover="showS(this)" mark="${si.mark }">
				<div class="data_line">
					<strong>${si.name }</strong> <span><fmt:formatNumber
							type="number" value="${si.currentPrice }" maxFractionDigits="2" /></span>
					<span><fmt:formatNumber type="number"
							value="${si.currentPrice-si.closingPrice}" maxFractionDigits="2" /></span>
					<span><fmt:formatNumber type="number" value="${si.increase}"
							maxFractionDigits="2" />%</span>
				</div>
				<img alt="" src="${si.minurl }"
					style="display: none; height: 270px;">
			</div>
		</c:forEach>
		<c:forEach items="${globalInfo}" var="gi">
			<div class="showG" style="display: none;">
				<div class="global_data">
					<strong>${gi.name }</strong> <span>${gi.curdot }</span> <span>${gi.curprice }</span>
					<span>${gi.rate }%</span>
				</div>
			</div>
		</c:forEach> 
	</div>
	<jsp:include page="../common/footer.jsp" flush="true" />
</body>
<script type="text/javascript">
	
</script>
</html>