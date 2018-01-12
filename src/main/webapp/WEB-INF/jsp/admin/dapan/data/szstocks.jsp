<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="css/shszstock.css" />
<script type="text/javascript">
	$(".searchDiv input").bind("input propertychange", function() {
		var id = $(this).attr("id");
		var stockMarket = "";
		var name = "";
		if (id == "shanghai") {
			stockMarket = "1";
			name = "shanghaiStocks";
		} else if (id == "shenzhen") {
			stockMarket = "2";
			name = "shenzhenStocks";
		}
		var searchValue = $(this).val();
		$("#" + name + " ul").load("stockdata/search", {
			'stockMarket' : stockMarket,
			'searchValue' : searchValue
		}, function(response, status, xhr) {

		});
	});
</script>
<c:if test="${marketMark=='2'&& empty toPosition}">
	<div class="title">
		股票管理/
		<div class="ontitle" onclick="toAllShSzStocks(this)" name="sz" style="left:-170px;width:150px;">深指代码</div>
	</div>
</c:if>
<span class="symbol">+</span>
<span style="font-size: 30px;">深市:(共${total}个)</span>
<c:if test="${position=='stock'}">
	<div class="searchDiv">
		<span class="search">搜索:</span><input type="text" id="shenzhen" />
	</div>
</c:if>
<div id="shenzhenStocks">
	<ul>
		<c:forEach items="${allStock}" var="ast">
			<c:if test="${ast.market_mark=='2'}">
				<li <c:if test="${ast.is_everydata=='no'}">style="color:red" class="nodata"</c:if>
					id="${ast.code}" onclick="selectMyStock(this)"><a>${ast.name}(${ast.code })</a></li>
			</c:if>
		</c:forEach>
	</ul>
</div>
<!-- 分页 -->
<jsp:include page="/WEB-INF/jsp/common/page.jsp" />