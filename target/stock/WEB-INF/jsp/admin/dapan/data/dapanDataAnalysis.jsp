<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
	/* $(function() {
		<c:if test="${dapan.m_riseorfall gt 0}">
		$(".middle").css("color", "red");
		</c:if>
		<c:if test="${dapan.m_riseorfall lt 0}">
		$(".middle").css("color", "green");
		</c:if>
	}); */
</script>
</head>
<body>
	<div class="title">
		股票数据/大盘数据分析/
		<div class="ontitle" onclick="toThisPage(this)" name="dataAnalysis" style="left: -38px;width:150px;">泸指数据分析</div>
	</div>
	<div id="total">
		<div style="color: red;">
			至今泸指涨日总数：<span>${rise}</span>
			<div class="riseRate">
				涨率：<span>${riseRate }%</span>
			</div>
		</div>
		<div style="color: green;">
			至今泸指跌日总数：<span>${fall}</span>
			<div class="fallRate">
				跌率：<span>${fallRate }%</span>
			</div>
		</div>
	</div>
	<div id="maxandminDate">
		<div class="showdate">
			最大日期：<span>${maxDate.max }</span>
		</div>
		<div class="showdate">
			最小日期：<span>${minDate.min }</span>
		</div>
	</div>
	<div id="dor">
		<c:forEach items="${dateOfRise}" var="rise">
			<div class="risestock">
				<c:forEach items="${rise}" var="dor" varStatus="vs">
					<div>
						<c:if test="${vs.index==0 }">
							<span>${fn:length(rise)}</span>
						</c:if>
						<c:if test="${vs.index!=0 }">
							<span style="width: 5px">&#8194</span>
						</c:if>
						<span>${dor.date }</span><span>${dor.closing_index }</span> <span>${dor.day_in_week }</span><span>${dor.total_hand
							}</span>
					</div>
				</c:forEach>
			</div>
		</c:forEach>
	</div>
	<div id="dof">
		<c:forEach items="${dateOfFall}" var="fall">
			<div class="fallstock">
				<c:forEach items="${fall}" var="dof" varStatus="vs">
					<div>
						<c:if test="${vs.index==0 }">
							<span>${fn:length(fall)}</span>
						</c:if>
						<c:if test="${vs.index!=0 }">
							<span style="width: 5px">&#8194</span>
						</c:if>
						<span>${dof.date }</span> <span>${dof.closing_index }</span><span>${dof.day_in_week }</span><span>${dof.total_hand
							}</span>
					</div>
				</c:forEach>
			</div>
		</c:forEach>
	</div>
</body>
</html>