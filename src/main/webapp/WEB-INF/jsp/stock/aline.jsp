<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script src="echarts/dist/echarts.js"></script>
<script src="echarts/dist/echarts-all.js"></script>
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
		var chart = document.getElementById('a_data');
		var echart = echarts.init(chart);

		echart.showLoading({
			text : '正在努力加载中...'
		});

		var date = [];
		var index = [];
		var latelyCI;
		var latelyDate;
		var nowDate;
		var nowCI;
		var line = "${line}";
		var stockCode = "${stockCode}";
		var stockname = "${stockname}";

		// 同步执行  
		$.ajaxSettings.async = false;

		// 加载数据  
		$.getJSON('stockWorld/getAverageLineList', {
			line : line,
			stockCode : stockCode
		}, function(json) {
			date = json.date;
			index = json.index;
			latelyCI = json.latelyCI;
			latelyDate = json.latelyDate;
			nowDate = json.nowDate;
			nowCI = json.nowCI;
		});
		var option = {
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				data : [ stockname + line + '日均线图', {
					name : latelyDate + " " + line + "日均线：" + latelyCI,
					textStyle : {
						fontWeight : 'bold',
						color : 'red'
					}
				},nowDate+"收盘:"+nowCI ]
			},
			toolbox : {
				show : true,
				feature : {
					mark : {
						show : true
					},
					dataZoom : {
						show : true
					},
					dataView : {
						show : true
					},
					magicType : {
						show : true,
						type : [ 'line', 'bar', 'stack', 'tiled' ]
					},
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			calculable : true,
			dataZoom : {
				show : true,
				realtime : true,
				start : 0,
				end : 100
			},
			xAxis : [ {
				type : 'category',
				boundaryGap : false,
				axisTick : {
					onGap : false
				},
				splitLine : {
					show : false
				},
				data : date
			} ],
			yAxis : [ {
				type : 'value',
				scale : true,
				splitNumber : 5,
				boundaryGap : [ 0.01, 0.01 ]
			} ],
			series : [ {
				name : line + '日均线',
				type : 'line',
				data : index
			} ]
		};
		echart.setOption(option);
		echart.hideLoading();
	});
</script>