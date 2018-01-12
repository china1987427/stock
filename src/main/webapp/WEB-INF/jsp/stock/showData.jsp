<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style type="text/css">
#myStock ul li {
	width: 100px;
}
#tomystocks{
	float:right;
}
</style>
<div id="close"><img alt="" src="image/erro.jpg" style="width:20px;height:30px;border:2px;float:right;"></div>
<ul>
	<c:forEach items="${stock}" var="stock">
		<input type="hidden" id="code" value="${stock.code}" mark="${stock.mark}" stockName="${stock.name}"/>
		<c:if test="${stock.currentPrice-stock.closingPrice>0}">
			<li style="color: red;">${stock.name}(${stock.code})</li>
			<li style="color: red;">最新:${stock.currentPrice}</li>
			<li style="color: red;">涨幅:<fmt:formatNumber type="number"
					value="${stock.increase}" maxFractionDigits="2" />%
			</li>
			<li style="color: red;">涨跌:${stock.currentPrice-stock.closingPrice}</li>
		</c:if>
		<c:if test="${stock.currentPrice-stock.closingPrice<0}">
			<li style="color: green;">${stock.name}(${stock.code})</li>
			<li style="color: green;">最新:${stock.currentPrice}</li>
			<li style="color: green;">涨幅:<fmt:formatNumber type="number"
					value="${stock.increase}" maxFractionDigits="2" />%
			</li>
			<li style="color: green;">涨跌:${stock.closingPrice-stock.currentPrice}</li>
		</c:if>
		<c:if
			test="${stock.currentPrice-stock.closingPrice ge 0.00 && stock.currentPrice-stock.closingPrice le 0.00}">
			<li style="color: black;">${stock.name}(${stock.code})</li>
			<li style="color: black;">最新:${stock.currentPrice}</li>
			<li style="color: black;">涨幅:<fmt:formatNumber type="number"
					value="${stock.increase}" maxFractionDigits="2" />%
			</li>
			<li style="color: black;">涨跌:${stock.closingPrice-stock.currentPrice}</li>
		</c:if>
		<img src="${stock.minurl }" style="width: 300px; height: 200px;" />
	</c:forEach>
</ul>
<div id="tomystocks">
	<span>加入自选股</span>
</div>
<script type="text/javascript">
$(function() {
	$("#close").click(function() {
		$("#myStock").hide();
	});
	$("#tomystocks").click(function() {
		var code=$("#code").val();
		var mark=$("#code").attr("mark");
		var stockName=$("#code").attr("stockName");
		var username=$("#username").val();
		$.ajax({
			type : 'post',
			url : 'stockWorld/tomystocks',
			dataType : 'text',
			data : {
				'mark' : mark,
				'stockCode' : code,
				'stockName':stockName,
				'username':username
			},
			success : function(data) {
				if(data=="true"){
					alert("成功加入自选股");
				}else if(data=="nologin"){
					window.location.href="user/toLogin";
				}else if(data=="yes"){
					alert("您已添加到自选股");
				}else{
					alert("添加失败");
				}
			}
		});
	});
});
</script>