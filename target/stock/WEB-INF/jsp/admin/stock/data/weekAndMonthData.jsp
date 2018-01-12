<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:forEach items="${wamData}" var="wamd" varStatus="vs">
	<div class="wamData">
		<div>
			<span>第${wamd.weekNum }周</span>
		</div>
		<div>
			<span>${wamd.startDay }</span>(<span>${wamd.startClosing }</span>)——<span>${wamd.endDay
				}</span>(<span>${wamd.endClosing }</span>)
		</div>
		<div>
			<span>${wamd.differ }</span><span style="float:right;">${wamd.per }</span>
		</div>
	</div>
</c:forEach>
