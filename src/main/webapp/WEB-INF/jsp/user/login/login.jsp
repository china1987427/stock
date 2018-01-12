<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="org.apache.commons.lang.StringUtils"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Object code = session.getAttribute("code");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en" class="no-js">

<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<title>金融网</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<!-- CSS -->
<link rel="stylesheet" href="css/reset.css">
<link rel="stylesheet" href="css/supersized.css">
<link rel="stylesheet" href="css/style.css">
<!-- <link href="themes/default/easyui.css" rel="stylesheet" /> -->
<link href="themes/icon.css" rel="stylesheet" />
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/supersized.3.2.7.min.js"></script>
<script type="text/javascript" src="js/supersized-init.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<!-- <script type="text/javascript" src="js/scripts.js"></script> -->
<!-- <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/messages_zh.js"></script> -->
<style type="text/css">
.msg-error {
	background: #ffebeb none repeat scroll 0 0;
	border: 1px solid #e4393c;
	color: #e4393c;
	line-height: 18px;
	min-height: 18px;
	padding: 3px 10px 3px 40px;
	position: relative;
}

.msg-error span {
	background: rgba(0, 0, 0, 0) url("image/icons-all.png") no-repeat scroll
		-104px -49px;
	display: block;
	height: 16px;
	left: 10px;
	margin-top: -8px;
	overflow: hidden;
	position: absolute;
	top: 50%;
	width: 16px;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		var info = "${error}";
		if (!isNotNull(info)) {
			//$.messager.alert("操作提示", "操作失败！", "error");
			$(".msg-error").show();
		} else {
			$(".msg-error").hide();
		}
	});
	function toValid() {
		var n = 0;
		$("input").each(function() {
			var content = $(this).val();
			if (content == "") {
				var type = $(this).attr("name");
				$(".msg-error").hide();
				if (type == "username") {
					++n;
					$(this).after("<div id='uerror' style='float:right;color:red;'>用户名不能为空</div>");
					return false;
				} else if (type == "password") {
					++n;
					$("#uerror").remove();
					$(this).after("<div id='perror' style='float:right;color:red;'>密码不能为空</div>");
					return false;
				} else if (type == "Captcha") {
					++n;
					$("#perror").remove();
					$(this).after("<div id='cerror' style='float:right;color:red;'>请输入验证码</div>");
					return false;
				}
			}
		});
		if (n == 0) {
			$("#uerror").remove();
			$("#perror").remove();
			$("#cerror").remove();
			$("#login_form").submit();
		}
	}
	function changeImg() {
		var imgSrc = $("#imageCode");
		var src = imgSrc.attr("src");
		imgSrc.attr("src", chgUrl(src));
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
						$("#codeInfo").empty();
						codeResult = true;
					} else {
						$("#codeInfo").text("验证码不正确");
						$("#codeInfo").css("color", "red");
						codeResult = false;
						return;
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
	//时间戳   
	//为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳   
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
</script>
</head>
<body>
	<div class="page-container">
		<h1>登录</h1>
		<!-- <form action="user/login" method="post" id="login_form"
			onsubmit="javascript:return doSubmit();"> -->
		<form action="user/login" method="post" id="login_form">
			<div class="msg-error" style="display: none;">
				<span></span>${error }</div>
			<input type="text" id="username" name="username"
				placeholder="请输入您的用户名！"> <input type="password"
				id="password" name="password" placeholder="请输入您的用户密码！">
			<div>
				<input type="Captcha" class="Captcha" id="Captcha" name="Captcha"
					placeholder="请输入验证码！"> <img src="code" alt="验证码"
					title="点击更换" id="imageCode" onclick="changeImg();" /> <span
					id="codeInfo"></span>
			</div>
			<input type="hidden" value="${empty param.backUrl?backUrl:param.backUrl}" name="backUrl">
			<button type="button" class="submit_button"
				onclick="return toValid()">登录</button>
			<div class="error">
				<span>+</span>
			</div>
		</form>
	</div>
</body>
</html>