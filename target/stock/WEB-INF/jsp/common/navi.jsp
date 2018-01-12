<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String url = request.getHeader("referer");
	session.removeAttribute("username");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<style type="text/css">
.top_div {
	float: right;
}

.loginStatus {
	float: right;
}

.login {
	float: right;
}

.register {
	float: right;
}
</style>
<title></title>
<script type="text/javascript">
	$(function() {
		$(".login").click(
				function() {
					window.location.href = "user/toLogin?backUrl="
							+ $("#backUrl").val();
				});
	});
	function goback(){
		
	}
</script>
</head>
<body>
	<div class="top_div">
		<c:choose>
			<c:when test="${empty username}">
				<span>Hi,欢迎来到金融网</span>
				<div class="login">登录</div>
				<div class="register">
					<a href="user/register">注册</a>
				</div>
			</c:when>
			<c:otherwise>
				<span>Hi,欢迎来到金融网</span>
				<div class="loginStatus" onclick="goback()">[退出]</div>
				<div class="loginStatus">${username==null&&username==""?username:requestScope.username}</div>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>