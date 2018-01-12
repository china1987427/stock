<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- 分页 -->
<div class="page page1">
	 共<span>${page.totalPage}</span>页
	 当前第<span>${page.index}</span>页 
	<a href="javascript:void(0);" onclick="javascript:goPage(1,${page.pageSize})">首页</a>
	<c:choose>
		 <c:when test="${page.index==1}"><a href="javascript:void(0);" onclick="javascript:goPage(1,${page.pageSize},this)">上一页</a></c:when>
		 <c:otherwise>
		      <a href="javascript:void(0);" onclick="javascript:goPage(${page.index-1},${page.pageSize},this)">上一页</a>
		 </c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${page.index==page.totalPage}"><a href="javascript:void(0);" onclick="javascript:goPage(${page.totalPage},${page.pageSize},this)">下一页</a></c:when>
		
		<c:otherwise>
		      <a href="javascript:void(0);" onclick="goPage(${page.index+1},${page.pageSize},this)">下一页</a>
		</c:otherwise>
	</c:choose>
	<a href="javascript:void(0);" onclick="goPage(${page.totalPage},${page.pageSize},this)">末页</a>
	 转至第
	<input class="inp" name="curPage" id="page_name" value="${page.index}" onkeydown="if(event.keyCode==13) {goPage(this.value,${page.pageSize},this);}"/> 页
	<input type="button" onclick="goPage($('#page_name').val(),${page.pageSize},this)" class="page_go" value="go"/>
</div> 
