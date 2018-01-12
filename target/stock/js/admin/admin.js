$(function() {
	$(".stockarea").find("a").attr("href", "javascript:void(0)");
	$(".stock").on("click", function() {
		var name = $(this).attr("name");
		toStock(name);
	});
	$("#dapandata").on("click", function() {
		$("#mainContent").load("dapanData/getdapanData", {});
	});
	$("#dataAnalysis").on("click", function() {
		$("#mainContent").load("dapanData/dataAnalysis", {});
	});
	$("#showData").on("click", function() {
		$("#mainContent").load("dapanData/showData", {});
	});
	$("#sd").on("click", function() {
		$("#mainContent").load("stockdata/getStockHistoryData", function(response, status, xhr) {
			if (status == "success") {
				if (response == "true") {
					alert("数据保存成功");
				}
			}
		});
	});
	$("#stockdata").on("click", function() {
		$("#mainContent").load("stockdata/stockEveryDayData", {});
	});
	$("#riseOrFallMap").on("click", function() {
		$("#mainContent").load("stockdata/riseOrFallMap", {});
	});
	$("#raisingLimitPredict").on("click", function() {
		$("#mainContent").load("stockdata/raisingLimitPredict", {});
	});
	$("#stockAnalysis").on("click", function() {
		$("#mainContent").load("stockdata/stockAnalysis", {});
	});
	$("#updateData").on("click", function() {
		$("#mainContent").load("stockdata/updateData", {});
	});
	$("#homePage").on("click", function() {
		$("#mainContent").load("admin/homePage", {});
	});
	$("#weekAndMonth").on("click", function() {
		$("#mainContent").load("stockdata/weekOrMonthRiseOrFall", {});
	});
	$("#blog").on("click", function() {
		$("#mainContent").load("admin/test", {});
	});
	$(".sdata").on("click", function() {
		$(this).find(".data").toggle();
	});
	$("#updateDapan").on("click", function() {
		$.ajax({
			type : "POST",
			url : "admin/managerAllstock",
			data : {},
			dataType : "json",
			success : function(data) {
				if (data) {
					alert("股票代码大全数据更新完成")
				}
			}
		});
	});
	
	$("#tokline").click(function() {
		$("#kline").load("stockdata/toKline", {
			"stockCode" : $(this).attr("code"),
			"stockName" : $(this).attr("name")
		});
	});
	
	$("#weekAndMonthData").click(function() {	
		$("#wamData").load("stockdata/weekOrMonthData", {
			"stockCode" : $(this).attr("code")
		});
	});
	
});
function isNotNull(param) {
	if (param == null || param == undefined || param == "") {
		return true;
	}
	return false;
}
// 分页
function goPage(pageindex, pagesize, obj) {
	showStock(pageindex, pagesize, obj);
}
function showStock(pageindex, pagesize, obj) {
	var name = $(obj).parents(".stockarea").attr("name");
	var id = $(obj).parents(".stockarea").attr("id");
	$("#" + id + "").load("admin/getstock", {
		"name" : name,
		"index" : pageindex,
		"pageSize" : pagesize,
		"toPosition" : "all"
	});
}
function selectMyStock(obj) {
	var isdata = $(obj).attr("class");
	if (isdata == "nodata") {
		alert("没有历史数据");
		return;
	}
	var stockCode = $(obj).attr("id");
	var stockName = $(obj).attr("name");
	$("#mainContent").empty();
	$("#mainContent").load("stockdata/everyStock", {
		"stockCode" : stockCode,
		"stockName" : stockName
	});
}
function hideMyStock(obj) {
	$("#myStock").hide();
}
function toThisPage(obj) {
	var name = $(obj).attr("name");
	$("#" + name + "").click();
}
function toAllShSzStocks(obj){
	var name = $(obj).attr("name");
	toStock(name);
}
function toStock(name){
	if (name == "all") {
		$("#mainContent").load("admin/toallStocks", {
			"toPosition" : ""
		});
	} else if (name == "sh" || name == "sz") {
		$("#mainContent").load("admin/getstock", {
			"name" : name
		});
	}
}