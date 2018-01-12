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
	String ctxPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>金融世界</title>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/stock.js"></script>
<link rel="stylesheet" href="css/stock.css" />
<style type="text/css">
</style>
<script type="text/javascript">
	$(function() {
		var timeTicket;
		var newValue = [];
		clearInterval(timeTicket);
		timeTicket = setInterval(getData, 20000);
		function getData() {
			var stockCode = $("#show_stock").find("span").attr("stock_code");
			var mark = $("#stockId").val();
			if (mark == "shanghai") {
				mark = "1";
			} else if (mark == "shenzhen") {
				mark = "2";
			}
			if (stockCode != null && stockCode != "") {
					$.ajax({
							type : 'post',
							url : 'realTime/getData',
							dataType : 'json',
							data : {
								'newValue' : newValue,
								'stockCode' : stockCode,
								'mark' : mark
							},
							success : function(data) {
								var oldData = $("#oldData").val();
								//var name = data.realData.stockinfo.name;
								var currentPrice = data.realData.stockinfo.currentPrice;
								var code = data.realData.stockinfo.code;
								$("#oldData").val(currentPrice);
								if (oldData != null && oldData != "") {
									var ratio = (parseFloat(currentPrice) - parseFloat(oldData))
											/ parseFloat(currentPrice);
									$("#showRatio").text(ratio);
									if (ratio < 0) {
										$("#showMp").show();
										$("#showMp").find("audio").attr(
												"autoplay", "autoplay");
									} else {
										$("#showMp").hide();
									}
								}
							}
						});
			}
		}
	});
</script>
</head>
<body>
	<input type="hidden" id="backUrl" value="${backUrl}">
	<jsp:include page="../common/navi.jsp" flush="true" />
	<input type="hidden" id="username" value="${requestScope.username }">
	<div class="top_box">
		<ul>
			<li class="all_stocks"
				onclick="window.location.href='stockWorld/allStocks?username=${param.username}'">全部股票</li>
			<li class="my_stock">自选股票</li>
		</ul>
	</div>
	<div>
		<!-- 股票代码:<input type="text" id="stockCode" name="stockCode"> 股票名称:<input
			type="text" id="stockName" name="stockName"> 市盈率:<input
			type="text" id="peRatio" name="peRatio"> 所属区域:<input
			type="text" id="region" name="region"> 所属行业:<input
			type="text" id="industry" name="industry"> 主营业务:<input
			type="text" id="mainBusiness" name="mainBusiness"> <span
			id="saveData">保存数据</span> -->
		<div onmouseover="showData()" onmouseout="hideData()"
			style="width: 100px; float: right;">
			<span>数据中心</span>
			<div id="dataCenter">板块</div>
		</div>
		<div id="show_stock">
			<span></span>
		</div>
		<div>
			<span id="showRatio"></span>
		</div>
		<div style="display: none;" id="showMp">
			<audio controls="controls">
				<source src="audio/trto.mp3" type="audio/mp3" />
			</audio>
		</div>
		<input type="hidden" id="stockId"> <input type="hidden"
			id="oldData">
		<div onmouseover="selectStock()" onmouseout="hideStock()"
			style="width: 100px;">
			<span>选择股票</span> <span class="change_icon_down" id="change_icon"></span>
			<span id="alert_div"></span>
			<div class="other_stock">
				<div class="stock_index">
					<a href="javascript:void(0)" mark="dapan">大盘指数</a>
				</div>
				<div class="stock_index">
					<a href="javascript:void(0)" mark="shanghai">上证</a>
				</div>
				<div class="stock_index">
					<a href="javascript:void(0)" mark="shenzhen">深证</a>
				</div>
				<div id="serach" style="display: none;">
					查询:<input type="text">
				</div>
				<div class="selected_stock" id="selected_stock_shanghai"
					style="display: none;">
					<ul>
						<c:forEach items="${allStock}" var="ast">
							<c:if test="${ast.stock_market=='1'}">
								<li id="${ast.code}"><a href="javascript:void(0)">${ast.name}(${ast.code })</a></li>
							</c:if>
						</c:forEach>
					</ul>
				</div>
				<div class="selected_stock" id="selected_stock_shenzhen"
					style="display: none;">
					<ul>
						<c:forEach items="${allStock}" var="ast">
							<c:if test="${ast.stock_market=='2'}">
								<li id="${ast.code}"><a href="javascript:void(0)">${ast.name}(${ast.code })</a></li>
							</c:if>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		<div>
			<span class="averageLine" line="k">k线图</span>
			 <!-- <span
				class="averageLine" line="5">5日均线</span> <span class="averageLine"
				line="10">10日均线</span> <span class="averageLine" line="20">20日均线</span>
			<span class="averageLine" line="30">30日均线</span> <span
				class="averageLine" line="60">60日均线</span> <span class="averageLine"
				line="120">120日均线</span> -->
		</div>
		<div showId="rline" id="r_data" style="height: 600px;"></div>
		<div showId="aline"></div>
	</div>
	<!-- <div id="getData">获取数据</div> -->
	<div showId="kline" style="height: 600px;"></div>
		<div id="show_num"></div>
	<div>
		<span id="a_line"></span>
	</div>
	<div>
		<span>泸港通十大成交股</span>
		<c:forEach items="${hgtten}" var="ht" varStatus="status">
			<ul class="hgtten">
				<%-- <c:if test="${ht.numorder== status.index}"> --%>
				<li>当日排名:${ht.numorder }</li>
				<li>${ht.vc2name }(${ht.vc2marcode })</li>
				<li>收盘价:${ht.numclose }</li>
				<li>当日涨幅:${ht.numratio }</li>
				<li>买入量:${ht.numbmount }</li>
				<li>卖出量:${ht.numsmount }</li>
				<li>总成交量:${ht.numsummount }</li>
				<li>${ht.dattrade }</li>
				<%-- </c:if> --%>
			</ul>
		</c:forEach>
	</div>
	<input type="hidden" id="username" value="${username }">
	<jsp:include page="../common/footer.jsp" flush="true" />
</body>
</html>