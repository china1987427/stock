<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<ul>
	<c:forEach items="${dataCenter}" var="dc">
		<li>股票名称:${dc.name}</li>
		<li>现价:${dc.currentPrice}</li>
		<li>昨天价格${dc.yestClosingIndex}</li>
		<li>开盘:${dc.OpenningPrice}</li>
		<li>最高:${dc.hPrice}</li>
		<li>最低:${dc.lPrice}</li>
		<li>收盘:${dc.closingPrice}</li>
		<li>量:${dc.totalNumber}</li>
		<li>涨跌:${dc.increase}</li>
		
		<li>卖5:${dc.sellFivePrice} ${dc.sellFive}</li>
		<li>卖4:${dc.sellFourPrice} ${dc.sellFour}</li>
		<li>卖3:${dc.sellThreePrice} ${dc.sellThree}</li>
		<li>卖2:${dc.sellTwoPrice} ${dc.sellTwo}</li>
		<li>卖1:${dc.sellOnePrice} ${dc.sellOne}</li>
		
		<li>买1:${dc.buyOnePrice} ${dc.buyOne}</li>
		<li>买2:${dc.buyTwoPrice} ${dc.buyTwo}</li>
		<li>买3:${dc.buyThreePrice} ${dc.buyThree}</li>
		<li>买4:${dc.buyFourPrice} ${dc.buyFour}</li>
		<li>买5:${dc.buyFivePrice} ${dc.buyFive}</li>
		<img src="${dc.minurl }"/>
	</c:forEach>
</ul>
