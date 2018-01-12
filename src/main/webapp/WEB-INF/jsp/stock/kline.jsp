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
		var chart = document.getElementById('k_data');
		var echart = echarts.init(chart);

		echart.showLoading({
			text : '正在努力加载中...'
		});

		var date = [];
		var closingIndex = [];
		var sumMoney = [];
		var kdata = [];
		var maxci;
		var maxhi;
		var maxli;
		var ymin;
		var ymax;
		var stockname = "${stockname}";
		var stockCode = "${stockCode}";
		// 同步执行  
		$.ajaxSettings.async = false;

		// 加载数据  
		$.getJSON('stockWorld/dynData', {
			stockCode : stockCode
		}, function(json) {
			date = json.date;
			closingIndex = json.closingIndex;
			sumMoney = json.sumMoney;
			kdata = json.kdata;
			maxci=json.maxci;
			maxhi=json.maxhi;
			minli=json.minli;
			if(stockCode=="dapan"){
				ymin=parseInt(minli-20);
				ymax=parseInt(maxhi+20);
			}else{
				ymin=parseInt(minli)-parseInt(minli)*0.1;
				ymax=parseInt(maxhi)+parseInt(maxhi)*0.1;
			}
		});
		var option = {
			title : {
				text : '2016年' + stockname + '上证指数'
			},
			tooltip : {
				trigger : 'axis',
				formatter : function(params) {
					var res = params[0].name;
					for (var i = params.length - 1; i >= 0; i--) {
						if (params[i].value instanceof Array) {
							res += '<br/>' + params[i].seriesName;
							res += '<br/>  开盘 : ' + params[i].value[0]
									+ '  最高 : ' + params[i].value[3];
							res += '<br/>  收盘 : ' + params[i].value[1]
									+ '  最低 : ' + params[i].value[2];
						} else {
							res += '<br/>' + params[i].seriesName;
							res += ' : ' + params[i].value;
						}
					}
					return res;
				}
			},
			legend : {
				data : [ '上证指数', '成交金额(万)' ]
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
						show : true,
						readOnly : false
					},
					magicType : {
						show : true,
						type : [ 'line', 'bar' ]
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
				show : true,
				realtime : true,
				start : 0,
				end : 100
			},
			calculable : true,
			xAxis : [ {
				type : 'category',
				boundaryGap : true,
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
				splitNumber : 10,
				min : ymin,
				max : ymax,
				boundaryGap : [ 10,	10 ]
			}, {
				type : 'value',
				scale : true,
				splitNumber : 10,
				boundaryGap : [ 0.1, 0.1 ],
				axisLabel : {
					formatter : function(v) {
						return Math.round(v) + ' 万'
					}
				}
			} ],
			series : [
					{
						name : '成交金额(万)',
						type : 'line',
						yAxisIndex : 1,
						symbol : 'none',
						data : sumMoney,
						markPoint : {
							symbol : 'emptyPin',
							itemStyle : {
								normal : {
									color : '#1e90ff',
									label : {
										show : true,
										position : 'top',
										formatter : function(param) {
											return Math.round(param.value)+ ' 万'
										}
									}
								}
							},
							data : [ {
								type : 'max',
								name : '最大值',
								symbolSize : 5
							}, {
								type : 'min',
								name : '最小值',
								symbolSize : 5
							} ]
						},
						markLine : {
							symbol : 'none',
							itemStyle : {
								normal : {
									color : '#1e90ff',
									label : {
										show : true,
										formatter : function(param) {
											return Math.round(param.value)+ ' 万'
										}
									}
								}
							},
							data : [ {
								type : 'average',
								name : '平均值'
							} ]
						}
					}, {
						name : stockname,
						type : 'k',
						barGap :'1%',
						data : // 开盘，收盘，最低，最高
						kdata
					} ]
		};
		echart.setOption(option);
		echart.hideLoading();
	});
</script>
