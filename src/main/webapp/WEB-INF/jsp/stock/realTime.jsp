<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script src="echarts/dist/echarts.js"></script>
<script src="echarts/dist/echarts-all.js"></script>
<script type="text/javascript" src="js/jquery.json-2.3.js"></script>
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	//配置路径  
	require.config({
		paths : {
			echarts : 'echarts'
		}
	});
	// 按需加载  
	require([ 'echarts', 'echarts/chart/bar', 'echarts/chart/line' ], function(
			ec) {
		var chart = document.getElementById('r_data');
		var echart = echarts.init(chart);

		echart.showLoading({
			text : '正在努力加载中...'
		});
		var market;
		var stockCode = "${stockCode}";
		var mark = "${mark}";
		var timeArray = [];
		var OpenningPrice;
		var ymin;
		var ymax;
		var timeSize;
		var value = [];
		var name;
		var code;
		var OpenningPrice;
		var hPrice;
		var lPrice;
		var currentPrice;
		var fiveLine;
		var tenLine;
		var thirtyLine;
		var twentyLine;
		var sixtyLine;
		var lineName;
		var lineValue;
		var diff;//与均线差
		var deficit = "";//逆差
		var deficitName = "";
		var deficitValue = "";
		var isBreak = "";
		var peRatio;
		var industry;
		var array = new Array();
		var yestData;
		// 同步执行  
		$.ajaxSettings.async = false;

		// 加载数据  
		$.getJSON('realTime/getRealTime', {
			stockCode : stockCode,
			mark : mark
		}, function(json) {
			market = json.market;
			timeArray = json.timeArray;
			timeSize = parseInt(json.timeSize);
			yestData = json.stockinfo.closingPrice;
			OpenningPrice = (json.stockinfo.OpenningPrice).toFixed(2);
			hPrice = (json.stockinfo.hPrice).toFixed(2);
			lPrice = (json.stockinfo.lPrice).toFixed(2);
			currentPrice = (json.stockinfo.currentPrice).toFixed(2);
			if (stockCode == "dapan") {
				ymin = (parseInt(currentPrice) - 20).toFixed(0);
				ymax = (parseInt(currentPrice) + 20).toFixed(0);
			} else {
				ymin = (parseFloat(yestData) - parseFloat(yestData) * 0.1)
						.toFixed(0);
				ymax = (parseFloat(yestData) + parseFloat(yestData) * 0.1)
						.toFixed(0);
			}
			value = json.initValue;
			name = json.stockinfo.name;
			peRatio = json.stock.pe_ratio;
			industry = json.stock.industry;
			if (stockCode != "dapan") {
				code = json.stockinfo.code.substring(2);
			} else {
				code = "大盘指数";
				peRatio = "16.77";
			}
			fiveLine = json.allLine.fiveLine.index;
			tenLine = json.allLine.tenLine.index;
			twentyLine = json.allLine.twentyLine.index;
			thirtyLine = json.allLine.thirtyLine.index;
			sixtyLine = json.allLine.sixtyLine.index;
			var difFive = parseFloat(currentPrice) - parseFloat(fiveLine);
			var difTen = parseFloat(currentPrice) - parseFloat(tenLine);
			var difTwenty = parseFloat(currentPrice) - parseFloat(twentyLine);
			var difThirty = parseFloat(currentPrice) - parseFloat(thirtyLine);
			var difSixty = parseFloat(currentPrice) - parseFloat(sixtyLine);
			if (difFive > 0) {
				diff = (difFive / parseFloat(currentPrice)).toFixed(4) * 100
						+ "%";
				lineName = "5日均线";
				lineValue = fiveLine;
			} else if (difTen > 0) {
				diff = (difTen / parseFloat(currentPrice)).toFixed(4) * 100
						+ "%";
				lineName = "10日均线";
				lineValue = tenLine;
			} else if (difTwenty > 0) {
				diff = (difTwenty / parseFloat(currentPrice)).toFixed(4) * 100
						+ "%";
				lineName = "20日均线";
				lineValue = twentyLine;
			} else if (difThirty > 0) {
				diff = (difThirty / parseFloat(currentPrice)).toFixed(4) * 100
						+ "%";
				lineName = "30日均线";
				lineValue = thirtyLine;
			} else if (difSixty > 0) {
				diff = (difSixty / parseFloat(currentPrice)).toFixed(4) * 100
						+ "%";
				lineName = "60日均线";
				lineValue = sixtyLine;
			} 
			if (difSixty < 0) {
				deficit = (difSixty / parseFloat(currentPrice)).toFixed(4);
				array.push("60");
			}
			if (difThirty < 0) {
				deficit = (difThirty / parseFloat(currentPrice)).toFixed(4);
				array.push("30");
			}
			if (difTwenty < 0) {
				deficit = (difTwenty / parseFloat(currentPrice)).toFixed(4);
				array.push("20");
			}
			if (difTen < 0) {
				deficit = (difTen / parseFloat(currentPrice)).toFixed(4);
				array.push("10");
			}
			if (difFive < 0) {
				deficit = (difFive / parseFloat(currentPrice)).toFixed(4);
				array.push("5");
			}
			for (var m = 0; m < array.length; m++) {
				if (array != null) {
					isBreak = "已破位:";
				}
				if (array[m] == "5") {
					deficitName = "5日均线";
					deficitValue = fiveLine;
					break;
				} else if (array[m] == "10") {
					deficitName = "10日均线";
					deficitValue = fiveLine;
					break;
				} else if (array[m] == "10") {
					deficitName = "10日均线";
					deficitValue = tenLine;
					break;
				} else if (array[m] == "20") {
					deficitName = "20日均线";
					deficitValue = twentyLine;
					break;
				} else if (array[m] == "30") {
					deficitName = "30日均线";
					deficitValue = thirtyLine;
				} else if (array[m] == "60") {
					deficitName = "60日均线";
					deficitValue = sixtyLine;
					break;
				}
			}
			var timeTicket;
			var newValue = [];
			clearInterval(timeTicket);
			timeTicket = setInterval(aaa, 20000);
			function aaa() {
				var closeCD = isClose();
				console.log(closeCD);
				console.log(closeCD != "close");
				if (closeCD != "close") {
					$.ajax({
						type : 'post',
						url : 'realTime/getCpValue',
						dataType : 'json',
						data : {
							'newValue' : newValue,
							'stockCode' : stockCode,
							'mark' : mark
						},
						success : function(data) {
							newValue = data.dynValue;
							if (newValue != null && newValue != "") {
								value = newValue;
								refresh(value);
							}
							currentPrice = data.currentPrice;
						}
					});
				}
			}
			function isClose() {
				var closeCD="close";
				$.ajax({
					type : 'post',
					url : 'realTime/closeRealTime',
					dataType : 'json',
					async : false,
					data : {},
					success : function(data) {
						closeCD = data.closeCD;
					}
				});
				return closeCD;
			}
		});

		function refresh(value) {
			/* if (echart && echart.dispose) {
				echart.dispose();
			} */
			var option = {
				title : {
					text : name + "(" + code + ")",
					subtext : '动态数据',
					textStyle : {
						fontSize : 18,
						fontWeight : 'bolder',
						color : 'red'
					}
				},
				tooltip : {
					trigger : 'axis'
				},
				legend : {
					textStyle : {
						color : 'red'
					},
					data : [ {
						name : name + '  最新成交价:' + currentPrice,
						textStyle : {
							fontWeight : 'bold',
							color : 'red'
						}
					}, {
						name : '开盘:' + OpenningPrice,
						color : 'red'
					}, {
						name : '最高:' + hPrice,
						color : 'red'
					}, {
						name : '最低:' + lPrice,
						color : 'green'
					}, {
						name : lineName + ":" + lineValue + "    " + diff,
						color : 'red'
					}, {
						name : isBreak + deficitName + deficit
					}, {
						name : "市盈率:" + peRatio
					}, {
						name : industry
					} ]
				},
				toolbox : {
					show : true,
					feature : {
						mark : {
							show : true
						},
						dataView : {
							show : true,
							readOnly : false
						},
						magicType : {
							show : true,
							type : [ 'line' ]
						},
						restore : {
							show : true
						},
						saveAsImage : {
							show : true
						}
					}
				},
				dataZoom : {
					show : false,
					start : 0,
					end : 100
				},
				xAxis : [ {
					type : 'category',
					boundaryGap : true,
					data : timeArray
				} ],
				yAxis : [ {
					type : 'value',
					scale : true,
					name : '价格',
					min : ymin,
					max : ymax,
					boundaryGap : [ 0.2, 0.2 ]
				} ],
				series : [ {
					name : '当前价格',
					type : 'line',
					data : value
				} ]
			};
			echart = echarts.init(chart);
			window.onresize = echart.resize;
			echart.setOption(option, true);
			echart.hideLoading();
		}
		var option = {
			title : {
				text : name + "(" + code + ")",
				subtext : '动态数据',
				textStyle : {
					fontSize : 18,
					fontWeight : 'bolder',
					color : 'red'
				}
			},
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				textStyle : {
					color : 'red'
				},
				data : [ {
					name : name + '  最新成交价:' + currentPrice,
					textStyle : {
						fontWeight : 'bold',
						color : 'red'
					}
				}, {
					name : '开盘:' + OpenningPrice,
					color : 'red'
				}, {
					name : '最高:' + hPrice,
					color : 'red'
				}, {
					name : '最低:' + lPrice,
					color : 'green'
				}, {
					name : lineName + ":" + lineValue + "    " + diff,
					color : 'red'
				}, {
					name : isBreak + deficitName + deficit
				}, {
					name : "市盈率:" + peRatio
				}, {
					name : industry
				} ]
			},
			toolbox : {
				show : true,
				feature : {
					mark : {
						show : true
					},
					dataView : {
						show : true,
						readOnly : false
					},
					magicType : {
						show : true,
						type : [ 'line' ]
					},
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			dataZoom : {
				show : false,
				start : 0,
				end : 100
			},
			xAxis : [ {
				type : 'category',
				name : '时间',
				boundaryGap : true,
				data : timeArray
			} ],
			yAxis : [ {
				type : 'value',
				scale : true,
				name : '价格',
				min : ymin,
				max : ymax,
				boundaryGap : [ 0.2, 0.2 ]
			} ],
			series : [ {
				name : '当前价格',
				type : 'line',
				data : value
			} ]
		};

		echart.setOption(option);
		echart.hideLoading();

	});
</script>
