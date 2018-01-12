<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page trimDirectiveWhitespaces="true"%>
<style type="text/css">
</style>
<c:forEach items="${myStock}" var="ms">
	<input type="hidden" id="code" value="${ms.stock_code}"
		mark="${ms.market}" stockName="${ms.stock_name}" />
	<ul>
		<c:forEach items="${ms.stockData}" var="mssd">
			<c:if test="${mssd.currentPrice-mssd.closingPrice>0}">
				<li style="color: red;">${ms.stock_name}(${ms.stock_code})</li>
				<li style="color: red;">最新:${mssd.currentPrice}</li>
				<li style="color: red;">涨幅:<fmt:formatNumber type="number"
						value="${mssd.increase}" maxFractionDigits="2" />%
				</li>
				<li style="color: red;">涨跌:${mssd.currentPrice-mssd.closingPrice}</li>
			</c:if>
			<c:if test="${mssd.currentPrice-mssd.closingPrice<0}">
				<li style="color: green;">${ms.stock_name}(${ms.stock_code})</li>
				<li style="color: green;">最新:${mssd.currentPrice}</li>
				<li style="color: green;">涨幅:<fmt:formatNumber type="number"
						value="${mssd.increase}" maxFractionDigits="2" />%
				</li>
				<li style="color: green;">涨跌:${mssd.closingPrice-mssd.currentPrice}</li>
			</c:if>
			<c:if
				test="${mssd.currentPrice-mssd.closingPrice ge 0.00 && mssd.currentPrice-mssd.closingPrice le 0.00}">
				<li style="color: black;">${ms.stock_name}(${ms.stock_code})</li>
				<li style="color: black;">最新:${mssd.currentPrice}</li>
				<li style="color: black;">涨幅:<fmt:formatNumber type="number"
						value="${mssd.increase}" maxFractionDigits="2" />%
				</li>
				<li style="color: black;">涨跌:${mssd.closingPrice-mssd.currentPrice}</li>
			</c:if>
		</c:forEach>
	</ul>
</c:forEach>