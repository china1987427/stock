var date = [];
var closingIndex = [];
var sumMoney = [];
var kdata = [];
var maxci;
var maxhi;
var maxli;
var ymin;
var ymax;
var json;
var stockName = getName();
var stockCode = getCode();
var year = getYear();
$.ajaxSettings.async = false;
$.getJSON('stockWorld/dynData', {
	"stockCode" : stockCode
}, function(json) {
	date = json.date;
	closingIndex = json.closingIndex;
	sumMoney = json.sumMoney;
	kdata = json.kdata;
	maxci = json.maxci;
	maxhi = json.maxhi;
	minli = json.minli;
	maxSumMoney = json.maxSumMoney;
	minSumMoney = json.minSumMoney;
	if (stockCode == "dapan") {
		ymin = parseFloat(minli - 20);
		ymax = parseFloat(maxhi + 20);
	} else {
		ymin = parseFloat(minli) - parseFloat(minli) * 0.1;
		ymax = parseFloat(maxhi) + parseFloat(maxhi) * 0.1;
	}
});
$.getJSON('stockWorld/getAverageLineList', {
	line : "5",
	stockCode : stockCode
}, function(json) {
	fivelinedate = json.date;
	fiveindex = json.index;
	fivelatelyCI = json.latelyCI;
	fivelatelyDate = json.latelyDate;
	// nowDate = json.nowDate;
	nowCI = json.nowCI;
});

$.getJSON('stockWorld/getAverageLineList', {
	line : "10",
	stockCode : stockCode
}, function(json) {
	tenlinedate = json.date;
	tenindex = json.index;
	tenlatelyCI = json.latelyCI;
	tenlatelyDate = json.latelyDate;
});

$.getJSON('stockWorld/getAverageLineList', {
	line : "20",
	stockCode : stockCode
}, function(json) {
	twentylinedate = json.date;
	twentyindex = json.index;
	twentylatelyCI = json.latelyCI;
	twentylatelyDate = json.latelyDate;
});

$.getJSON('stockWorld/getAverageLineList', {
	line : "30",
	stockCode : stockCode
}, function(json) {
	thirtylinedate = json.date;
	thirtyindex = json.index;
	thirtylatelyCI = json.latelyCI;
	thirtylatelyDate = json.latelyDate;
});

$.getJSON('stockWorld/getAverageLineList', {
	line : "60",
	stockCode : stockCode
}, function(json) {
	sixtylinedate = json.date;
	sixtyindex = json.index;
	sixtylatelyCI = json.latelyCI;
	sixtylatelyDate = json.latelyDate;
});
var data = {
	title : {
		text : year+'年上证指数'
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
		xdata : date
	} ],
	series : [ {
		name : stockName,
		code : stockCode,
		kdata : // 开盘，收盘，最低，最高
		kdata
	} ],
	sumMoney : sumMoney
};
var number = 60;
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
var klineLeft = $("#mycanvas").offset().left;
x_scale = data.width / splitStep;// x轴刻度
y_scale = data.height / splitStep;// y轴刻度
leastSplit = (data.width - 2 * x_scale) / (number * 4);
stepWidth = (data.width - 2 * x_scale) / len;
var init = function(data) {
	canvas.width = data.width;
	canvas.height = data.height;
	drawXAxis(data.xAxis[0].xdata);// 画X轴
	drawYaxis(data.yAxis[0].maxValue, data.yAxis[0].splitNumber);// 画Y轴
	var kdata = data.series[0].kdata;
	for (i = 0; i < kdata.length; i++) {
		var smallKdata = kdata[i];
		var openIndex = smallKdata[0];
		var closingIndex = smallKdata[1];
		var lowIndex = smallKdata[2];
		var highIndex = smallKdata[3];
		var x = x_scale + leastSplit * (1 + 4 * i);
		var y = 0;
		var h = (((data.height - 4 * y_scale) / (data.yAxis[0].maxValue - data.yAxis[0].minValue)) * (closingIndex - openIndex))
				.toFixed(2);
		if (h > 0) {
			y = ((((data.height - 4 * y_scale) / (data.yAxis[0].maxValue - data.yAxis[0].minValue)) * (data.yAxis[0].maxValue - openIndex)) + (y_scale * 3))
					.toFixed(2);
		} else {
			y = ((((data.height - 4 * y_scale) / (data.yAxis[0].maxValue - data.yAxis[0].minValue)) * (data.yAxis[0].maxValue - closingIndex)) + (y_scale * 3))
					.toFixed(2);
		}
		var w = kwidth;
		var mx = x_scale + leastSplit * (2 + 4 * i);
		var my = ((((data.height - 4 * y_scale) / (data.yAxis[0].maxValue - data.yAxis[0].minValue)) * (data.yAxis[0].maxValue - highIndex)) + (y_scale * 3))
				.toFixed(2);
		var lx = x_scale + leastSplit * (2 + 4 * i);
		var ly = ((((data.height - 4 * y_scale) / (data.yAxis[0].maxValue - data.yAxis[0].minValue)) * (data.yAxis[0].maxValue - lowIndex)) + (y_scale * 3))
				.toFixed(2);
		stepXArr.push(x);
		if (h > 0) {
			myDraw.drawStrokeRect(x, y, w, Math.abs(parseFloat(h)));
			myDraw.drawLine(mx, my, lx, ly, rectColor[0]);
		} else if (h <= 0) {
			myDraw.drawRect(x, y, w, Math.abs(parseFloat(h)));
			myDraw.drawLine(mx, my, lx, ly, rectColor[5]);
		}
	}
	// 均线
	var indexArray = [];
	var color = "black";
	for ( var b = 1; b < 6; b++) {
		if (b == 1) {
			indexArray = fiveindex;
			color = rectColor[0];
		} else if (b == 2) {
			indexArray = tenindex;
			color = rectColor[2];
		} else if (b == 3) {
			indexArray = twentyindex;
			color = rectColor[3];
		} else if (b == 4) {
			indexArray = thirtyindex;
			color = rectColor[4];
		} else if (b == 5) {
			indexArray = sixtyindex;
			color = rectColor[5];
		}
		for ( var a = 1; a <= 60; a++) {
			var x0, x1, x2, y0, y1, y2;

			if (a % 2 != 0) {
				x0 = x_scale + leastSplit * (2 + (a - 1) * 4);
				x1 = x_scale + leastSplit * (2 + a * 4);
				x2 = x_scale + leastSplit * (2 + (a + 1) * 4);
				y0 = ((((data.height - 4 * y_scale) / (data.yAxis[0].maxValue - data.yAxis[0].minValue)) * (data.yAxis[0].maxValue - indexArray[a - 1])) + (y_scale * 3))
						.toFixed(2);
				y1 = ((((data.height - 4 * y_scale) / (data.yAxis[0].maxValue - data.yAxis[0].minValue)) * (data.yAxis[0].maxValue - indexArray[a])) + (y_scale * 3))
						.toFixed(2);
				y2 = ((((data.height - 4 * y_scale) / (data.yAxis[0].maxValue - data.yAxis[0].minValue)) * (data.yAxis[0].maxValue - indexArray[a + 1])) + (y_scale * 3))
						.toFixed(2);
				ctx.beginPath();
				ctx.moveTo(x0, y0);
				ctx.quadraticCurveTo(x1, y1, x2, y2);
				ctx.strokeStyle = color;
				ctx.stroke();
			}
		}
	}
	$(canvas).on("mousemove", mouseMove);
	$(canvas).on("click", mouseClick);
	$(canvas).on("mouseleave", mouseLeave);
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
	for ( var i = 1; i <= (number / 3); i++) {
		var parma = x_scale + leastSplit * 4 * ((2 + (i - 1) * 3) + 0.5);
		// 画标签，默认字体为12个像素
		ctx.font = "normal normal bold 7px 微软雅黑";
		ctx.fillStyle = "black";
		// 字体居中
		ctx.textAlign = "center";
		if ((i - 1) == 0) {
			ctx.fillText(data.xAxis[0].xdata[0], x_scale - 8, canvas.height - y_scale + 20);
		}
		ctx.fillText(data.xAxis[0].xdata[2 + (i - 1) * 3], parma, data.height - y_scale + 20);
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
		ctx.fillText(((maxValue - ((maxValue - data.yAxis[0].minValue) / 10) * (10 - i))).toFixed(2), x_scale - 25,
				((10 - i) * ((data.height - 4 * y_scale) / 10)) + 3 * y_scale);
	}
	ctx.stroke();
	// 加箭头
	drawArrow(x_scale, y_scale + n, true);
	// 加Y轴顶部字体
	ctx.fillText(data.title.text, x_scale + 30, y_scale - 8);
	ctx.fillText(stockName, x_scale , y_scale + 16);
	ctx.fillText(stockCode, x_scale + 60, y_scale + 16);
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
	var left = $("#canvasPanel").css("left").replace("px", "");// 位置不能变动
	for ( var i = 0; i < number; i++) {
		if ((parseFloat(stepXArr[i])) <= x
				&& x <= (parseFloat(stepXArr[i]) + parseFloat(2 * leastSplit))
				&& (parseFloat(top) + 75) <= y && y <= (parseFloat(top) + 400 + 75)) {
			myDraw.drawLine(x_scale + leastSplit * (2 + 4 * i), 75, x_scale + leastSplit * (2 + 4 * i), 475);
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
			myDraw.drawOtherLine(x_scale + leastSplit * (2 + 4 * array[0]), 75, x_scale + leastSplit
					* (2 + 4 * array[0]), 475, "white");
		}
	}
	if (array[1] != undefined) {
		$("#show_num").show();
		// 显示数字
		$("#show_num").html(
				'<div class="show_num"><span>日期：' + data.xAxis[0].xdata[array[1]] + '</span><span>股票名称：' + stockName
						+ '</span><span>开盘：' + kdata[array[1]][0] + '</span><span>收盘：' + kdata[array[1]][1]
						+ '</span><span>最低：' + kdata[array[1]][2] + '</span><span>最高：' + kdata[array[1]][3]
						+ '</span><span>成交量(单位:万)：' + data.sumMoney[array[1]] + '</span></div>');
		$("#show_num").css({
			"left" : x_scale + leastSplit * (2 + 4 * array[1]) + parseFloat(left) + 40 - 4 * leastSplit,
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