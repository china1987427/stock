var stockCode = "600653";
var timeArray = [];
var ymin;
var ymax;
var sumMoney;
var value = [];
var currentPrice;
var index;
var arrayfirst = new Array();
// 同步执行
$.ajaxSettings.async = false;

// 加载数据
$.getJSON('realTime/getRealTime', {
	stockCode : stockCode,
	mark : "1"
}, function(json) {
	timeArray = json.timeArray;
	currentPrice = (json.stockinfo.currentPrice).toFixed(2);
	yestData = json.stockinfo.closingPrice;
	if (stockCode == "dapan") {
		ymin = (parseInt(currentPrice) - 20).toFixed(0);
		ymax = (parseInt(currentPrice) + 20).toFixed(0);
	} else {
		ymin = (parseFloat(yestData) - parseFloat(yestData) * 0.1).toFixed(0);
		ymax = (parseFloat(yestData) + parseFloat(yestData) * 0.1).toFixed(0);
	}
});
var data = {
	title : {
		text : '2016年' + stockname + '上证指数'
	},
	width : 1000,
	height : 500,
	legend : {
		data : [ '百分比', '(%)' ]
	},
	yAxis : [ {
		splitNumber : 10,
		minValue : ymin,
		maxValue : ymax
	// boundaryGap : [ 10, 10 ]
	} ],
	xAxis : [ {
		xdata : timeArray
	} ],
	series : [ {
		name : stockname,
		data : value
	} ],
	sumMoney : sumMoney
};

var number = 240;
var kwidth = 8;
var splitStep = 20;
var onetime = 0;
var stepYArr = [], stepXArr = [];
var array = new Array();
var len = data.xAxis[0].xdata.length;
var leastSplit = 0;
var stepWidth = 0;// 一个类型所占的宽度
var x_scale = 0, y_scale = 0, heightVal = 0, stepWidth = 0, stepHeight = 0;
var arrowWidth = 4, arrowHeight = 6;
var rectColor = [ "red", "white", "yellow", "purple", "green", "blue", "black" ];
var canvas = document.getElementById("mycanvas");
var ctx = canvas.getContext("2d");
var myDraw = new Draw(canvas);
var init = function(data) {
	canvas.width = data.width;
	canvas.height = data.height;
	x_scale = data.width / splitStep;// x轴刻度
	y_scale = data.height / splitStep;// y轴刻度
	leastSplit = (data.width - 2 * x_scale) / (number);
	drawXAxis(data.xAxis[0].xdata);// 画X轴
	stepWidth = (data.width - 2 * x_scale) / len;
	drawYaxis(data.yAxis[0].maxValue, data.yAxis[0].splitNumber);// 画Y轴
	// $(canvas).on("mousemove", mouseMove);
	// $(canvas).on("click", mouseClick);
	// $(canvas).on("mouseleave", mouseLeave);
}

/* 画x轴 */
var drawXAxis = function(xAxis) {
	ctx.beginPath();// 清除之前的路径，开始一条新的路径
	// 画x轴横线
	ctx.moveTo(data.width - x_scale, data.height - y_scale);
	ctx.lineTo(x_scale, data.height - y_scale);
	// 加标签
	len = data.xAxis[0].xdata.length;
	stepWidth = (data.width - 2 * x_scale) / len;// 一个类型所占的宽度
	for ( var i = 0; i <= (number / 20); i++) {
		var parma = x_scale + leastSplit * 20 * i;
		// 画标签，默认字体为12个像素
		ctx.font = "normal normal bold 7px 微软雅黑";
		ctx.fillStyle = "black";
		// 字体居中
		ctx.textAlign = "center";
		ctx.fillText(data.xAxis[0].xdata[i * (number / (number / 20))], parma,
				data.height - y_scale + 20);
		ctx.globalCompositeOperation = "destination-over";
		ctx.moveTo(parma, data.height - y_scale - 5);
		ctx.lineTo(parma, data.height - y_scale);
	}
	ctx.stroke();
	// 加箭头
	drawArrow(data.width - x_scale, data.height - y_scale, false);
}

// 画y轴
var drawYaxis = function(maxValue, step) {
	ctx.beginPath();
	// 画Y轴线
	var n = 50;
	ctx.moveTo(x_scale, 3 * y_scale);
	ctx.lineTo(x_scale, data.height - y_scale);

	// 加标签
	stepHeight = (data.height - 3 * y_scale) / step;
	heightVal = (data.height - 3 * y_scale) / maxValue;// 比例
	for ( var i = 10; i >= 0; i--) {
		ctx.font = "normal normal bold 12px 微软雅黑";
		// 字体居中
		ctx.fillText(((maxValue - ((maxValue - data.yAxis[0].minValue) / 10)
				* (10 - i))).toFixed(2), x_scale - 25,
				((10 - i) * ((data.height - 4 * y_scale) / 10)) + 3 * y_scale);
	}
	ctx.stroke();
	// 加箭头
	drawArrow(x_scale, y_scale + n, true);
	// 加Y轴顶部字体
	ctx.fillText(data.title.text, x_scale + 30, y_scale - 8);
}
// 画箭头
var drawArrow = function(left, top, flag) {
	ctx.beginPath();
	ctx.moveTo(left, top);
	if (flag) {
		ctx.lineTo(left + arrowWidth, top);
		ctx.lineTo(left, top - arrowHeight);
		ctx.lineTo(left - arrowWidth, top);
	} else {
		ctx.lineTo(left, top - arrowWidth);
		ctx.lineTo(left + arrowHeight, top);
		ctx.lineTo(left, top + arrowWidth);
	}

	ctx.fillStyle = "#666";
	ctx.fill();
}
var parentTop = $("#canvasPanel").offset().top;
// mousemove事件
var mouseMove = function(e) {
	e.preventDefault();
	var x = e.clientX;
	var y = e.clientY;
	var top = $("#canvasPanel").css("top").replace("px", "");
	var left = $("#canvasPanel").css("left").replace("px", "");
	for ( var i = 0; i < number; i++) {
		if (7.5 + parseFloat(left) + parseFloat(stepXArr[i]) < x
				&& x < 7.5 + parseFloat(left) + parseFloat(stepXArr[i]) + 15
				&& parseFloat(top) + 75 < y && y < parseFloat(top) + 400 + 75) {
			myDraw.drawLine(x_scale + leastSplit * (2 + 4 * i), 75, x_scale
					+ leastSplit * (2 + 4 * i), 475);
			onetime = 0;
			if (array.length == 0) {
				array.push(i);
			}
			if (array[0] != i) {
				array.push(i);
			}
		}
	}
	if (array.length == 2) {
		if (array[0] != array[1]) {
			myDraw.drawOtherLine(x_scale + leastSplit * (2 + 4 * array[0]), 75,
					x_scale + leastSplit * (2 + 4 * array[0]), 475, "white");
		}
	}
	if (array[1] != undefined) {
		$("#show_num").show();
		// 显示数字
		$("#show_num").html(
				'<div class="show_num"><span>日期：'
						+ data.xAxis[0].xdata[array[1]] + '</span><span>股票名称：'
						+ stockname + '</span><span>开盘：' + kdata[array[1]][0]
						+ '</span><span>收盘：' + kdata[array[1]][1]
						+ '</span><span>最低：' + kdata[array[1]][2]
						+ '</span><span>最高：' + kdata[array[1]][3]
						+ '</span><span>成交量(单位:万)：' + data.sumMoney[array[1]]
						+ '</span></div>');
		$("#show_num").css(
				{
					"left" : x_scale + leastSplit * (2 + 4 * array[1])
							+ parseFloat(left) + 40,
					"top" : parseFloat(parentTop) + (canvas.height / 2)
				});
	}
	if (array.length == 2) {
		array.length = 0;
	}
}
var mouseLeave = function(e) {
	$("#show_num").hide();
	if (onetime == 0) {
		init(data);
		onetime = 1;
	}
}

// click事件
var mouseClick = function(e) {
	init(data);
}
function Draw() {
	this.drawLine = function(mx, my, lx, ly, color) {
		var canvasn = document.getElementById("mycanvas");
		var ctxn = canvasn.getContext("2d");
		ctxn.save();
		ctxn.beginPath();
		ctxn.moveTo(mx, my);
		ctxn.lineTo(lx, ly);
		ctxn.strokeStyle = color;
		ctxn.globalAlpha = 1;
		ctxn.globalCompositeOperation = "source-over";
		ctxn.stroke();
	}
	this.drawOtherLine = function(mx, my, lx, ly, color) {
		var canvasm = document.getElementById("mycanvas");
		var ctxm = canvasm.getContext("2d");
		ctxm.save();
		ctxm.beginPath();
		ctxm.moveTo(mx, my);
		ctxm.lineTo(lx, ly);
		ctxm.strokeStyle = color;
		ctxm.globalCompositeOperation = "copy";
		ctxm.stroke();
		init(data);
	}
	this.drawRect = function(x, y, width, height) {
		ctx.fillStyle = "green";
		ctx.fillRect(x, y, width, height);
		ctx.fill();
	}
	this.drawStrokeRect = function(x, y, width, height) {
		ctx.strokeStyle = "red";
		ctx.strokeRect(x, y, width, height);
		ctx.fill();
	}
	this.setLineWidth = function(lw) {
		ctx.lineWidth = lw;
	}
	this.chooseColor = function(c) {
		ctx.strokeStyle = c;
	}
}
init(data);
arrayfirst.push(x_scale);
arrayfirst
		.push(((((data.height - 4 * y_scale) / (ymax - ymin)) * (ymax - currentPrice)) + (y_scale * 3))
				.toFixed(2));
var timeTicket;
var array = new Array();
clearInterval(timeTicket);
timeTicket = setInterval(getRealTime, 20000);
function getRealTime() {
	// 加载数据
	$.getJSON('realTime/getCpValue', {
		stockCode : stockCode,
		mark : "1"
	}, function(json) {
		var x0 , y0;
		currentPrice = (json.stockinfo.currentPrice).toFixed(2);
		yestData = json.stockinfo.closingPrice;
		index = json.index;
		if (stockCode == "dapan") {
			ymin = (parseInt(currentPrice) - 20).toFixed(0);
			ymax = (parseInt(currentPrice) + 20).toFixed(0);
		} else {
			ymin = (parseFloat(yestData) - parseFloat(yestData) * 0.1)
					.toFixed(0);
			ymax = (parseFloat(yestData) + parseFloat(yestData) * 0.1)
					.toFixed(0);
		}
		drawYaxis(ymax, data.yAxis[0].splitNumber);// 画Y轴
		x0 = x_scale + 3.75 * (index - 1);
		y0 = ((((data.height - 4 * y_scale) / (ymax - ymin)) * (ymax - currentPrice)) + (y_scale * 3)).toFixed(2);
		array.push(x0);
		array.push(y0);
		if (array.length == 2) {
			ctx.beginPath();
			ctx.moveTo(arrayfirst[0],arrayfirst[1]);
			ctx.lineTo(array[0],array[1]);
			ctx.strokeStyle = "red";
			ctx.stroke();
			arrayfirst.length=0;
			arrayfirst.push(x0);
			arrayfirst.push(y0);
			array.length = 0;
		}
	});
}
