<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String servletPath = request.getRequestURI();
	for (int i = 1; i <= 22; i++) {
		session.removeValue("param" + i);
		session.removeValue("object" + i);
	}
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>金融世界</title>
</head>
<body>	
	<div class="title">股票数/股票数据分析/<div class="ontitle" onclick="toThisPage(this)" name="riseOrFallMap" style="left:-60px;width:150px;">涨跌分布图</div></div>
	<div class="bigdata">
		<div class="date">日期:${date}</div>
		<div class="amount">总共${fn:length(stocks)}只股票</div>
		<div id="prob">
			<div id="riseNum" class="rfbox"></div>
			<div id="fallNum" class="rfbox"></div>
			<div id="riseProbability" class="rfbox"></div>
		</div>
		<div id="dapanNowData" <c:if test="${dapanData.rise_or_fall>0 }">style="color:red;"</c:if><c:if test="${dapanData.rise_or_fall<=0 }">style="color:green;"</c:if>>
			<div id="dapdanDate" style="width:100px;">${dapanData.date }</div>
			<div id="dapdanCI">${dapanData.closing_index }</div>
			<div class="dapanRF">${dapanData.rise_or_fall }   ${dapanData.amplitude }%</div>
			<div class="dapanIndex" style="width:120px;">H:${dapanData.high_index }   L:${dapanData.low_index }</div>
		</div>
		<c:forEach items="${stocks}" var="stocks" varStatus="vs">
			<c:if test="${stocks.amplitude>=9.98}">
				<c:if test="${empty param1}">
					<div class="sdata" name="1" rel="rise">
						<c:set var="param1" scope="session" value="rise" />
						<c:set var="object1" scope="session" value="bigrise" />
						<div class="qujian">
							<span>涨停股</span><span class="smallSum"></span>
						</div>
						<div class="data">
				</c:if>
				<div class="miandata">
					<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
				</div>
			</c:if>
			<c:if test="${stocks.amplitude>=9&&stocks.amplitude<9.98}">
				<c:if test="${object1=='bigrise'||empty param1}">
					<c:remove var="object1" />
					<c:set var="param1" scope="session" value="rise" />
	</div>
	</div>
	</c:if>
	<c:if test="${empty param2}">
		<div class="sdata" name="2" rel="rise">
			<c:set var="param2" scope="session" value="rise" />
			<c:set var="object2" scope="session" value="bigrise" />
			<div class="qujian">
				<span>9%</span>——<span>10%</span><span class="smallSum"></span>
			</div>
			<div class="data">
	</c:if>
	<div class="miandata">
		<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
	</div>
	</c:if>
	<c:if test="${stocks.amplitude>=8&&stocks.amplitude<9}">
		<c:if test="${object2=='bigrise'||empty param2}">
			<c:remove var="object2" />
			<c:set var="param2" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param3}">
			<div class="sdata" name="3" rel="rise">
				<c:set var="param3" scope="session" value="rise" />
				<c:set var="object3" scope="session" value="bigrise" />
				<div class="qujian">
					<span>8%</span>——<span>9%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=7&&stocks.amplitude<8}">
		<c:if test="${object3=='bigrise'||empty param3}">
			<c:remove var="object3" />
			<c:set var="param3" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param4}">
			<div class="sdata" name="4" rel="rise">
				<c:set var="param4" scope="session" value="rise" />
				<c:set var="object4" scope="session" value="bigrise" />
				<div class="qujian">
					<span>7%</span>——<span>8%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=6&&stocks.amplitude<7}">
		<c:if test="${object4=='bigrise'||empty param4}">
			<c:remove var="object4" />
			<c:set var="param4" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param5}">
			<div class="sdata" name="5" rel="rise">
				<c:set var="param5" scope="session" value="rise" />
				<c:set var="object5" scope="session" value="bigrise" />
				<div class="qujian">
					<span>6%</span>——<span>7%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=5&&stocks.amplitude<6}">
		<c:if test="${object5=='bigrise'||empty param5}">
			<c:remove var="object5" />
			<c:set var="param5" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param6}">
			<div class="sdata" name="6" rel="rise">
				<c:set var="param6" scope="session" value="rise" />
				<c:set var="object6" scope="session" value="bigrise" />
				<div class="qujian">
					<span>5%</span>——<span>6%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=4&&stocks.amplitude<5}">
		<c:if test="${object6=='bigrise'||empty param6}">
			<c:remove var="object6" />
			<c:set var="param6" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param7}">
			<div class="sdata" name="7" rel="rise">
				<c:set var="param7" scope="session" value="rise" />
				<c:set var="object7" scope="session" value="bigrise" />
				<div class="qujian">
					<span>4%</span>——<span>5%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=3&&stocks.amplitude<4}">
		<c:if test="${object7=='bigrise'||empty param7}">
			<c:remove var="object7" />
			<c:set var="param7" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param8}">
			<div class="sdata" name="8" rel="rise">
				<c:set var="param8" scope="session" value="rise" />
				<c:set var="object8" scope="session" value="bigrise" />
				<div class="qujian">
					<span>3%</span>——<span>4%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=2&&stocks.amplitude<3}">
		<c:if test="${object8=='bigrise'||empty param8}">
			<c:remove var="object8" />
			<c:set var="param8" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param9}">
			<div class="sdata" name="9" rel="rise">
				<c:set var="param9" scope="session" value="rise" />
				<c:set var="object9" scope="session" value="bigrise" />
				<div class="qujian">
					<span>2%</span>——<span>3%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=1&&stocks.amplitude<2}">
		<c:if test="${object9=='bigrise'||empty param9}">
			<c:remove var="object9" />
			<c:set var="param9" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param10}">
			<div class="sdata" name="10" rel="rise">
				<c:set var="param10" scope="session" value="rise" />
				<c:set var="object10" scope="session" value="bigrise" />
				<div class="qujian">
					<span>1%</span>——<span>2%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=0&&stocks.amplitude<1}">
		<c:if test="${object10=='bigrise'||empty param10}">
			<c:remove var="object10" />
			<c:set var="param10" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param11}">
			<div class="sdata" name="11" rel="rise">
				<c:set var="param11" scope="session" value="rise" />
				<c:set var="object11" scope="session" value="bigrise" />
				<div class="qujian">
					<span>0%</span>——<span>1%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=-1&&stocks.amplitude<0}">
		<c:if test="${object11=='bigrise'||empty param11}">
			<c:remove var="object11" />
			<c:set var="param11" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param12}">
			<div class="sdata" name="12" rel="fall" style="border:1px solid green;">
				<c:set var="param12" scope="session" value="rise" />
				<c:set var="object12" scope="session" value="bigrise" />
				<div class="qujian">
					<span>-1%</span>——<span>0%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=-2&&stocks.amplitude<-1}">
		<c:if test="${object12=='bigrise'||empty param12}">
			<c:remove var="object12" />
			<c:set var="param12" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param13}">
			<div class="sdata" name="13" rel="fall" style="border:1px solid green;">
				<c:set var="param13" scope="session" value="rise" />
				<c:set var="object13" scope="session" value="bigrise" />
				<div class="qujian">
					<span>-2%</span>——<span>-1%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=-3&&stocks.amplitude<-2}">
		<c:if test="${object13=='bigrise'||empty param13}">
			<c:remove var="object13" />
			<c:set var="param13" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param14}">
			<div class="sdata" name="14" rel="fall" style="border:1px solid green;">
				<c:set var="param14" scope="session" value="rise" />
				<c:set var="object14" scope="session" value="bigrise" />
				<div class="qujian">
					<span>-3%</span>——<span>-2%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=-4&&stocks.amplitude<-3}">
		<c:if test="${object14=='bigrise'||empty param14}">
			<c:remove var="object14" />
			<c:set var="param14" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param15}">
			<div class="sdata" name="15" rel="fall" style="border:1px solid green;">
				<c:set var="param15" scope="session" value="rise" />
				<c:set var="object15" scope="session" value="bigrise" />
				<div class="qujian">
					<span>-4%</span>——<span>-3%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=-5&&stocks.amplitude<-4}">
		<c:if test="${object15=='bigrise'||empty param15}">
			<c:remove var="object15" />
			<c:set var="param15" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param16}">
			<div class="sdata" name="16" rel="fall" style="border:1px solid green;">
				<c:set var="param16" scope="session" value="rise" />
				<c:set var="object16" scope="session" value="bigrise" />
				<div class="qujian">
					<span>-5%</span>——<span>-4%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=-6&&stocks.amplitude<-5}">
		<c:if test="${object16=='bigrise'||empty param16}">
			<c:remove var="object16" />
			<c:set var="param16" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param17}">
			<div class="sdata" name="17" rel="fall" style="border:1px solid green;">
				<c:set var="param17" scope="session" value="rise" />
				<c:set var="object17" scope="session" value="bigrise" />
				<div class="qujian">
					<span>-6%</span>——<span>-5%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=-7&&stocks.amplitude<-6}">
		<c:if test="${object17=='bigrise'||empty param17}">
			<c:remove var="object17" />
			<c:set var="param17" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param18}">
			<div class="sdata" name="18" rel="fall" style="border:1px solid green;">
				<c:set var="param18" scope="session" value="rise" />
				<c:set var="object18" scope="session" value="bigrise" />
				<div class="qujian">
					<span>-7%</span>——<span>-6%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=-8&&stocks.amplitude<-7}">
		<c:if test="${object18=='bigrise'||empty param18}">
			<c:remove var="object18" />
			<c:set var="param18" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param19}">
			<div class="sdata" name="19" rel="fall" style="border:1px solid green;">
				<c:set var="param19" scope="session" value="rise" />
				<c:set var="object19" scope="session" value="bigrise" />
				<div class="qujian">
					<span>-8%</span>——<span>-7%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>=-9&&stocks.amplitude<-8}">
		<c:if test="${object19=='bigrise'||empty param19}">
			<c:remove var="object19" />
			<c:set var="param19" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param20}">
			<div class="sdata" name="20" rel="fall" style="border:1px solid green;">
				<c:set var="param20" scope="session" value="rise" />
				<c:set var="object20" scope="session" value="bigrise" />
				<div class="qujian">
					<span>-9%</span>——<span>-8%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude>-9.98&&stocks.amplitude<-9}">
		<c:if test="${object20=='bigrise'||empty param20}">
			<c:remove var="object20" />
			<c:set var="param20" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param21}">
			<div class="sdata" name="21" rel="fall" style="border:1px solid green;">
				<c:set var="param21" scope="session" value="rise" />
				<c:set var="object21" scope="session" value="bigrise" />
				<div class="qujian">
					<span>-9%</span>——<span>-10%</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	<c:if test="${stocks.amplitude<=-9.98}">
		<c:if test="${object21=='bigrise'||empty param21}">
			<c:remove var="object21" />
			<c:set var="param21" scope="session" value="rise" />
			</div>
			</div>
		</c:if>
		<c:if test="${empty param22}">
			<div class="sdata" name="22" rel="fall" style="border:1px solid green;">
				<c:set var="param22" scope="session" value="rise" />
				<c:set var="object22" scope="session" value="bigrise" />
				<div class="qujian">
					<span>跌停板</span><span class="smallSum"></span>
				</div>
				<div class="data">
		</c:if>
		<div class="miandata">
			<span>${stocks.date}</span><span>${stocks.amplitude}</span><span>${stocks.stock_code}</span><span>${stocks.stock_name}</span><span>${stocks.closing_index}</span><span>${stocks.sum_money}</span>
		</div>
	</c:if>
	</c:forEach>
	</div>
</body>
<script type="text/javascript">
	$(".sdata").on("click", function() {
		$(this).find(".data").toggle();
	});
	$.each($(".sdata"), function(i, item){      
      　		var length=$(this).find(".miandata").length;
      	$(this).find(".smallSum").text(length+"只");
    　});

    var riseNum=0,fallNum = 0;
    $.each($(".bigdata").find("div[rel='rise']").find(".miandata"), function(i, item){      
      	riseNum++;
    　	});
    
    $.each($(".bigdata").find("div[rel='fall']").find(".miandata"), function(i, item){      
     	fallNum++;
    　	});
    $("#riseNum").text("上涨个股数: "+riseNum);
    $("#fallNum").text("下跌个股数: "+fallNum);
    var riseProbability = (riseNum/(riseNum+fallNum)).toFixed(4)*100;
     $("#riseProbability").text("赚钱效应: "+riseProbability.toFixed(2)+"%");
</script>
</html>