<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Object code = session.getAttribute("code");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册</title>
<script src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/user.js"></script>
<style type="text/css">
tr th {
	width: 100px;
	float: left;
}

tr td {
	float: left;
}
</style>
<script type="text/javascript">
	
</script>
</head>
<body>
	<div>
		<form action="user/registering" id="regForm" method="post">
			<table>
				<tbody>
					<tr>
						<th><font color="red">*</font>用户名:</th>
						<td><input type="text" id="username" name="username">
						</td>
						<td><span>字母与数字组合</span></td>
					</tr>
					<tr>
						<th><font color="red">*</font>设置密码:</th>
						<td><input type="password" id="password" name="password"></td>
						<td><span>密码大于等于6位数</span></td>
					</tr>
					<tr>
						<th><font color="red">*</font>确认密码:</th>
						<td><input type="password" id="repassword"></td>
					</tr>
					<tr>
						<th><font color="red">*</font>手机号码：</th>
						<td><input type="text" id="mobile" name="mobile"></td>
					</tr>
					<tr>
						<td><select id="province" name="province"
							onchange="getArea(this[selectedIndex].innerHTML,1);">
								<option>**省</option>
								<c:forEach items="${province}" var="province">
									<option value="${province.id}">${province.a_name}</option>
								</c:forEach>
						</select></td>

						<td id="tdcity" name="tdcity" width="100%"><select id="city"
							name="city" onchange="getArea(this[selectedIndex].innerHTML,2);">
								<option>**市</option>
						</select></td>

						<td id="tdcounty" name="tdcounty" width="100%"><select
							id="county" name="county">
								<option>**区</option>
						</select></td>
						<td id="street">街道:<input type="text" name="street"></td>
					</tr>
					<tr>
						<th><font color="red">*</font>验证码：</th>
						<td><input type="text" id="Captcha" class="Captcha"></td>
						<td><img src="code" alt="验证码" title="点击更换" id="imageCode"
							onclick="changeImg();" /></td>
					</tr>
					<tr>
						<th>邮箱：</th>
						<td><input type="text" id="email" name="email"></td>
					</tr>
					<tr>
						<td><input  value="提交注册" id="reg"/></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>