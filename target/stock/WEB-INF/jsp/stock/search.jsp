<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:forEach items="${allStock}" var="ast">
	<li><a href="javascript:void(0)" onclick="selectMyStock(this);" id="${ast.code }" name="${ast.name}">${ast.name}(${ast.code })</a></li>
</c:forEach>
