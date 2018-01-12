$(function() {
	$("#saveData").click(function() {
		$.ajax({
			type : 'post',
			url : 'readExcel/saveData',
			dataType : 'text',
			data : {
				"stockCode" : $("#stockCode").val(),
				"stockName" : $("#stockName").val(),
				"peRatio" : $("#peRatio").val(),
				"region" : $("#region").val(),
				"industry" : $("#industry").val(),
				"mainBusiness" : $("#mainBusiness").val()
			},
			success : function(data) {
				if (data == 'true') {
					window.location.reload();// 刷新当前页面
				} else {
					alert("保存数据失败");
				}
			}
		});
	});
	$("#getData").click(function() {
		$.ajax({
			type : 'post',
			url : 'stockWorld/getData',
			dataType : 'text',
			data : {

			},
			success : function(data) {
				var a = data.split("luoli")[0];
				var aLine = data.split("luoli")[1];
				if (a == 'true') {
					$("#a_line").text(aLine);
				} else {
					alert("保存数据失败");
				}
			}
		});
	});
	$(".averageLine").on('click',function() {
				var stockname;
				var showStock = $("#show_stock").find("span").text();
				if (showStock == "" || showStock == null
						|| showStock == undefined) {
					return false;
				}
				var line = $(this).attr("line");
				if(showStock!="dapan"){
					var num = showStock.indexOf("(");
					if (num > 0) {
						stockname = $("#show_stock").find("span").text().substring(
								0, num).trim();
					} else {
						stockname = $("#show_stock").find("span").text();
					}
				}else{
					stockname="大盘指数";
				}
				var stockCode = $("#show_stock").find("span")
						.attr("stock_code");
				var content = $(this).text();
				var reg = new RegExp("^[0-9]*$");
				if (line == 'k') {
					$("div[showId='kline']").attr("id", "k_data");
					$("div[showId='kline']").css("height", "600px !important")
					$("div[showId='kline']").load("stockWorld/loadk", {
						'stockCode' : stockCode,
						'stockname' : stockname
					}, function(response, status, xhr) {
					});
				} /*else if (reg.test(parseInt(line))) {
					$("div[showId='aline']").attr("id", "a_data");
					$("div[showId='aline']").css("height", "400px");
					$("div[showId='kline']").load("stockWorld/loada", {
						'line' : line,
						'stockCode' : stockCode,
						'stockname' : stockname
					}, function(response, status, xhr) {
					});
				}*/
			});
	var m = 1;
	$(".stock_index").click(function() {
		var mark = $(this).find("a").attr("mark");
		$("#stockId").val(mark);
		if (mark == "shanghai") {
			if (m == 1) {
				$(this).siblings().hide();
				$(".other_stock").css({
					"width-color" : "400px",
					"height" : "400px"
				});
				$("#selected_stock_shanghai").show();
				$("#serach").show();
				$("#serach input").attr("stockMark", "1");
				m = 2;
			} else if (m == 2) {
				$(".other_stock").css({
					"width-color" : "",
					"height" : ""
				});
				$(this).siblings().show();
				$("#serach").hide();
				$("#serach input").attr("stockMark", "");
				$("#selected_stock_shanghai").hide();
				$("#selected_stock_shenzhen").hide();
				m = 1;
			}
		} else if (mark == "shenzhen") {
			if (m == 1) {
				$(this).siblings().hide();
				$(".other_stock").css({
					"width-color" : "400px",
					"height" : "400px"
				});
				$("#selected_stock_shenzhen").show();
				$("#serach").show();
				$("#serach input").attr("stockMark", "2");
				m = 2;
			} else if (m == 2) {
				$(".other_stock").css({
					"width-color" : "",
					"height" : ""
				});
				$(this).siblings().show();
				$("#serach").hide();
				$("#serach input").attr("stockMark", "");
				$("#selected_stock_shanghai").hide();
				$("#selected_stock_shenzhen").hide();
				m = 1;
			}

		} else if (mark == "dapan") {
			$("#show_stock").find("span").text("dapan");
			$("#show_stock").find("span").attr("stock_code", "dapan");
			verify("dapan", "dapan", "");
		}
	});

	$(".selected_stock ul li").click(function() {
		var stockname = $(this).find("a").text();
		var stockCode = $(this).attr("id");
		var id = $(this).parents("div").attr("id");
		var mark;
		if (id == "selected_stock_shanghai") {
			mark = "1";
		} else if (id == "selected_stock_shenzhen") {
			mark = "2";
		}
		verify(stockname, stockCode, mark);
	});

	$('#serach input').bind('input propertychange', function() {
		var stockMarket = $("#serach input").attr("stockMark");
		var a;
		if (stockMarket == 1) {
			$("#selected_stock_shanghai ul li").remove();
			a = "#selected_stock_shanghai";
		} else if (stockMarket == 2) {
			$("#selected_stock_shenzhen ul li").remove();
			a = "#selected_stock_shenzhen";
		}
		var searchValue = $('#serach input').val();
		$("" + a + " ul").load("stockWorld/search", {
			'stockMarket' : stockMarket,
			'searchValue' : searchValue
		}, function(response, status, xhr) {

		});
	});

	$(".my_stock").click(function() {
		var username = $("#username").val();
		window.location.href = "stockWorld/myStock?username=" + username;
	});

});
function selectStock() {
	$(".other_stock").show();
	$("#change_icon").removeClass("change_icon_down");
	$("#change_icon").addClass("change_icon_up");
}
function hideStock() {
	$(".other_stock").hide();
	$("#change_icon").removeClass("change_icon_up");
	$("#change_icon").addClass("change_icon_down");
}
function findStock(obj) {
	var stockCode = $(obj).parents("li").attr("id");
	var stockname = $(obj).text();
	var mark = $(obj).parents("li").attr("markId");
	verify(stockname, stockCode, mark);
}
function verify(stockname, stockCode, mark) {
	$("#show_stock").find("span").text(stockname);
	$("#show_stock").find("span").attr("stock_code", stockCode);
	realTimeData(stockCode, mark);
	$.ajax({
		type : 'post',
		url : 'stockWorld/verify',
		dataType : 'json',
		data : {
			'stockCode' : stockCode
		},
		success : function(data) {
			if (data.result == 'true') {
				var maxDate = data.date;
				$.ajax({
					type : 'post',
					url : 'stockWorld/getHistoryData',
					dataType : 'text',
					data : {
						'stockCode' : stockCode,
						'stockname' : stockname,
						'startDate' : maxDate
					},
					success : function(data) {
						$("#alert_div").text(data);
						setTimeout(function() {
							$('#alert_div').empty().remove();
						}, 2000);
					}
				});
			}
		}
	});
}
function realTimeData(content, mark) {
	$("div[showId='rline']").load("realTime/toRealTime", {
		'stockCode' : content,
		'mark' : mark
	}, function(response, status, xhr) {
	});
}
function showData() {
	$("#dataCenter").show();
	var stockCode = $("#show_stock").find("span").attr("stock_code");
	var mark = $("#stockId").val();
	if (mark == "shanghai") {
		mark = "1";
	} else if (mark == "shenzhen") {
		mark = "2";
	}
	if (!verifyIsNull(stockCode) && !verifyIsNull(mark)) {
		$("#dataCenter").load("stockWorld/dataCenter", {
			"mark" : mark,
			"stockCode" : stockCode
		}, function() {

		});
	}
}
function hideData() {
	$("#dataCenter").hide();
}
function verifyIsNull(param) {
	if (param == "" || param == null || param == undefined) {
		return true;
	} else {
		return false;
	}
}