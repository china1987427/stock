$(function() {
	var $username = $("#username");
	var $password = $("#password");
	var $rePassword = $("#repassword");
	var $mobile = $("#mobile");
	var $Captcha = $("#Captcha");
	var $email = $("#email");
	$username.blur(function() {
		checkIsExist($(this).val(), $(this), 1);
	});

	$password.blur(function() {
		if ($password.val().length < 6 && $password.val().length > 0) {
			alert("密码大于等于6位数");
			$password.val("");
			$password.focus();
		}
	});
	$rePassword.blur(function() {
		if ($password.val() != $rePassword.val()) {
			alert("两次输入的密码不一致.");
			$password.val("");
			$rePassword.val("");
			$password.focus();
		}
	});
	$mobile.blur(function() {
		checkIsExist($(this).val(), $(this), 2);
		if (!isNotNull($(this).val())) {
			var result=checkPhone($(this).val());
			if(!result){
				$mobile.val("");
				$mobile.focus();
			}
		}
	});
	$Captcha.blur(function() {
		if (!verify() && $Captcha.val().length > 0) {
			alert("验证码不正确");
			$Captcha.val("");
			$Captcha.focus();
		}
	});
	$email.blur(function() {
		var email=$(this).val();
		if (!isNotNull(email)) {
			var result=checkEmail(email);
			if(!result){
				$email.val("");
				$email.focus();
			}
		}
	});
	$("#reg").click(function() {
		var username = $("#username").val();
		var password = $("#password").val();
		var mobile = $("#mobile").val();
		var email = $("#email").val();
		/*
		 * var privince = $("#privince option:selected").html(); var city =
		 * $("#city option:selected").html(); var county = $("#county
		 * option:selected").html(); var street = $("#street input").val();
		 */
		if (isNotNull(username)) {
			alert("帐号不能为空");
			return;
		}
		if (isNotNull(password)) {
			alert("密码不能为空");
			return;
		}
		if (isNotNull(mobile)) {
			alert("手机号不能为空");
			checkPhone(mobile);
			return;
		}
		$("#regForm").submit();
	});
});
function checkPhone(str) {
	var re = /^1[3|4|5|7|8]\d{9}$/;
	if (!(re.test(str))) {
		alert("请输入正确的手机号码");
		return false;
	}
	return true;
}
function checkEmail(str) {
	var re = /\w@\w*\.\w/;
	if (!re.test(str)) {
		alert("请输入正确的邮箱");
		return false;
	}
	return true;
}
function checkIsExist(v, $jqueryObject, type) {
	$.ajax({
		type : "POST",
		url : "user/verify",
		data : {
			'value' : v,
			'type' : type
		},
		success : function(data) {
			if (data == "true" && type == "1") {
				alert("亲，此帐号已经被注册，请重新输入哦！");
				$jqueryObject.val("");
				$jqueryObject.focus();
			} else if (data == "true" && type == "2") {
				alert("亲，手机号码已经被注册，请重新输入哦！");
				$jqueryObject.val("");
				$jqueryObject.focus();
			}
		}
	});

}

function changeImg() {
	var imgSrc = $("#imageCode");
	var src = imgSrc.attr("src");
	imgSrc.attr("src", chgUrl(src));
}
// 时间戳
// 为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳
function chgUrl(url) {
	var timestamp = (new Date()).valueOf();
	url = url.substring(0, 17);
	if ((url.indexOf("&") >= 0)) {
		url = url + "×tamp=" + timestamp;
	} else {
		url = url + "?timestamp=" + timestamp;
	}
	return url;
}
function verify() {
	var captcha = $("#Captcha").val();
	var codeResult;
	if (isNotNull(captcha)) {
		return;
	} else {
		$.ajax({
			url : "user/verifyCode",
			data : {
				"captcha" : captcha
			},
			async : false,
			type : "post",
			success : function(data) {
				if (data == "true") {
					codeResult = true;
				} else {
					codeResult = false;
				}
			}
		});
	}
	return codeResult;
}
function isNotNull(param) {
	if (param == null || param == undefined || param == "") {
		return true;
	}
	return false;
}
function getArea(laaName, mark) {
	if (mark == "1") {
		var fatherId = $("#province option:selected").val();
	} else {
		var fatherId = $("#city option:selected").val();
	}
	if ("请选择" == laaName)
		return;
	$.ajax({
		url : 'user/getArea',
		type : 'post',
		data : {
			'fatherId' : fatherId
		},
		success : function(data) {
			var info = data.substring(1, data.length - 2);
			var array = new Array();
			array = info.split(",");
			if (mark == "1") {
				var selectid = document.getElementById("city");
			} else {
				var selectid = document.getElementById("county");
			}
			for (var i = 0; i < array.length; i++) {
				selectid[0] = new Option("请选择", true, true);
				var cityvalue = array[i].split("=")[0];
				var cityname = array[i].split("=")[1];
				selectid[i + 1] = new Option(cityname, cityvalue);
			}
		}
	});
}