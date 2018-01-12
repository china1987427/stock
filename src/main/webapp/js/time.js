$(function() {
	showtime();
	setInterval("showtime()", 1000);
});
function showtime() {
	var today, hour, second, minute, year, month, date;
	var strDate;
	today = new Date();
	var n_day = today.getDay();
	switch (n_day) {
	case 0: {
		strDate = "星期日"
	}
		break;
	case 1: {
		strDate = "星期一"
	}
		break;
	case 2: {
		strDate = "星期二"
	}
		break;
	case 3: {
		strDate = "星期三"
	}
		break;
	case 4: {
		strDate = "星期四"
	}
		break;
	case 5: {
		strDate = "星期五"
	}
		break;
	case 6: {
		strDate = "星期六"
	}
		break;
	case 7: {
		strDate = "星期日"
	}
		break;
	}
	year = today.getFullYear();
	month = today.getMonth() + 1;
	date = today.getDate();
	hour = today.getHours();
	minute = today.getMinutes();
	second = today.getSeconds();
	document.getElementById('time').innerHTML = year + " 年 "
			+ formatDate(month) + " 月 " + formatDate(date) + " 日 " + strDate
			+ " " + formatDate(hour) + ":" + formatDate(minute) + ":"
			+ formatDate(second); // 显示时间
}
function formatDate(obj) {
	return obj >= 10 ? obj : ("0" + obj);
}