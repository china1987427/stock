var splitStep = 20;
var arrowWidth = 4, arrowHeight = 6;
var asCanvas = document.getElementById("amountSum");
var asCtx = asCanvas.getContext("2d");
var myDrawAS = new DrawAS(asCanvas);
var dataAS = {
	width : 1000,
	height : 200,
	maxSumMoney : maxSumMoney,
	minSumMoney : minSumMoney
};
$("#kline").css("height", data.height + dataAS.height + 200);
var y_scaleAS = dataAS.height / splitStep;// y轴刻度
var maxscale = dataAS.maxSumMoney + dataAS.maxSumMoney * 0.1;
var minscale = dataAS.minSumMoney + dataAS.minSumMoney * 0.1;
var maxSumMoney = dataAS.maxSumMoney;
var minSumMoney = dataAS.minSumMoney;
var intervalScale = (dataAS.height - 4 * y_scaleAS) / maxSumMoney;
var asInit = function(data) {
	asCanvas.width = data.width;
	asCanvas.height = dataAS.height;
	drawX();// 画X轴
	drawY();// 画Y轴
	var sumMoney = data.sumMoney;
	for (i = 0; i < sumMoney.length; i++) {
		var smallKdata = kdata[i];
		var openIndex = smallKdata[0];
		var closingIndex = smallKdata[1];
		var lowIndex = smallKdata[2];
		var highIndex = smallKdata[3];
		var isRise = (closingIndex - openIndex) > 0;
		var sm = sumMoney[i];
		var heightScale = intervalScale * (maxSumMoney - sm);
		var h = (intervalScale * sm).toFixed(2);
		var x = x_scale + leastSplit * (1 + 4 * i);
		var y = (heightScale + (y_scaleAS * 3)).toFixed(2);
		var w = kwidth;
		if (isRise) {
			myDrawAS.drawStrokeRect(x, y, w, Math.abs(parseFloat(h)));
		} else {
			myDrawAS.drawRect(x, y, w, Math.abs(parseFloat(h)));
		}
	}
}
/* 画x轴 */
var drawX = function(xAxis) {
	asCtx.beginPath();// 清除之前的路径，开始一条新的路径
	// 画x轴横线
	asCtx.moveTo(data.width - x_scale, dataAS.height - y_scaleAS);
	asCtx.lineTo(x_scale, dataAS.height - y_scaleAS);
	asCtx.moveTo(data.width - x_scale, 3 * y_scaleAS);
	asCtx.lineTo(x_scale, 3 * y_scaleAS);
	asCtx.stroke();
}

// 画y轴
var drawY = function(maxValue, step) {
	asCtx.beginPath();
	// 画Y轴线
	var n = 50;
	asCtx.moveTo(x_scale, 3 * y_scaleAS);
	asCtx.lineTo(x_scale, dataAS.height - y_scaleAS);
	asCtx.stroke();
}
function DrawAS() {
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
	}
	this.drawRect = function(x, y, width, height) {
		asCtx.fillStyle = "green";
		asCtx.fillRect(x, y, width, height);
		asCtx.fill();
	}
	this.drawStrokeRect = function(x, y, width, height) {
		asCtx.strokeStyle = "red";
		asCtx.strokeRect(x, y, width, height);
		asCtx.fill();
	}
	this.setLineWidth = function(lw) {
		asCtx.lineWidth = lw;
	}
	this.chooseColor = function(c) {
		asCtx.strokeStyle = c;
	}
}
asInit(data);