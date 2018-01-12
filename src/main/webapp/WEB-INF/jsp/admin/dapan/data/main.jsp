<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>金融世界</title>
<link rel="stylesheet" href="css/font-awesome.css">
<link rel="stylesheet" href="css/admin/admin.css">
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/google-maps.js"></script>
<script>
	$(document).ready(function() {
		$(".vertical-nav").verticalnav({
			speed : 400,
			align : "left"
		});
	});
</script>
</head>
<body>
	<div class="kePublic">
		<div class="content">
			<ul class="vertical-nav dark red">
				<li class="active" id="homePage"><a href="javascript:void(0)"><i class="icon-home"></i>首页</a></li>
				<li><a><i class="icon-stockManagement"></i>股票管理 <span class="submenu-icon"></span></a>
					<ul>
						<li class="stock" name="all"><a>股票代码大全</a></li>
						<li class="stock" name="sh"><a>泸指代码</a></li>
						<li class="stock" name="sz"><a>深指代码</a></li>
					</ul></li>
				<li><a><i class="icon-stockdata"></i>股票数据 <span class="submenu-icon"></span></a>
					<ul>
						<li><a>大盘数据分析<span class="submenu-icon"></span></a>
							<ul>
								<li id="dapandata"><a>泸指今日数据</a></li>
								<li id="dataAnalysis"><a>泸指数据分析</a></li>
								<li id="showData"><a>泸指数据展示</a></li>
								<li id="dapanRaisingLimitPredict"><a>泸指涨停预测</a></li>
							</ul></li>
						<li><a>股票数据分析<span class="submenu-icon"></span></a>
							<ul>
								<li id="raisingLimitPredict"><a>涨停板预测</a></li>
								<li id="riseOrFallMap"><a>涨跌分布图</a></li>
								<li id="stockdata"><a>全部股票数据</a></li>
								<li id="stockAnalysis"><a>数据分析</a></li>
							</ul></li>
					</ul></li>
				<li><a><i class="icon-addStocks"></i>股票代码添加<span class="submenu-icon"></span></a>
					<ul>
						<li id="updateData"><a>数据更新</a></li>
					</ul></li>
				<li><a><i class="icon-blogs"></i>博客管理<span class="submenu-icon"></span></a>
					<ul>
						<li id="blog"><a>博客</a></li>
					</ul>
				</li>
				<li><a><i class="icon-news"></i>股票新闻管理</a></li>
				<li><a><i class="icon-user"></i>用户管理</a></li>
				<li><a><i class="icon-announcement"></i>公告管理</a></li>
				<li><a><i class="icon-question"></i>常见问题</a></li>
				<li><a><i class="icon-limits"></i>权限管理</a></li>
				<li><a><i class="icon-contactUs"></i>联系我们</a></li>
			</ul>
		</div>
		<div class="clear"></div>
	</div>
	<div id="mainContent">bbbb</div>
	<div id="slogan"
		style="bottom: 0px; position: relative; width: 800px; left: 30%; clear: both; height: auto;font-size:50px;top:120px;">
		<p>一声怒吼百废兴，牛气冲天节节高</p>
		<p style="text-align:center;left:0px;">
			来源：<a href="javascript:void(0)" target="_blank">金融世界</a>
		</p>
	</div>
</body>
<script type="text/javascript" src="js/admin/admin.js"></script>
</html>