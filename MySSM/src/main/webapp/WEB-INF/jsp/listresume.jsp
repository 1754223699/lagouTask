<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/22
  Time: 12:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    pageContext.setAttribute("APP_PATH", request.getContextPath());
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>查询所有用户Resume</title>
</head>

<script type="text/javascript" src="${APP_PATH }/static/js/jquery-2.0.3.min.js"></script>
<link href="${APP_PATH }/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
<script src="${APP_PATH }/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
<body>
<table align='center' border='1' cellspacing='0' class="table table-bordered"
<th>
<td>id</td>
<td>address</td>
<td>name</td>
<td>phone</td>
</th>
<c:forEach items="${resumes.resume}" var="u">
    <tr class="active">
        <td>${u.id}</td>
        <td>${u.address}</td>
        <td>${u.name}</td>
        <td><a href="${pageContext.request.contextPath}/resume/edit?id=${u.id}">编辑</a></td>
        <td><a href="${pageContext.request.contextPath}/resume/delete?id=${u.id}">删除</a></td>
    </tr>
</c:forEach>
</table>

<div style="text-align:center">
    <a href="?start=0">首 页</a>
    <a href="?start=${page.start-page.count}">上一页</a>
    <a href="?start=${page.start+page.count}">下一页</a>
    <a href="?start=${page.last}">末 页</a>
</div>
<form action="resume/add" method="post">
    <div class="form-group">
        <label for="name">名称</label>
        <input type="text" class="form-control" id="name" name="name" placeholder="name">
    </div>
    <div class="form-group">
        <label for="address">地址</label>
        <input type="number" class="form-control" id="address" name="age" placeholder="address">
    </div>
    <div class="form-group">
        <label for="phone">电话</label>
        <input type="number" class="form-control" id="phone" name="age" placeholder="phone">
    </div>
    <button type="submit" class="btn btn-default">新增Resume</button>
</form>
</body>
</html>
