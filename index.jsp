<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>お食事処検索サイト</title>
<link rel="stylesheet" href="styleForOshisyoku.css">
</head>
<body>
<h1 class="osishoku">～～～お食事処～～～</h1>
<div class="search-container">
    <div id="main">
        <form action="SearchServlet" method="post">
            <div class="input-container">
                <label>ジャンル:</label>
                <input class="botan" type="text" name="genre" required/><br/>
            </div>
            <div class="input-container">
                <label>日時:　</label>
                <input class="botan" type="date" name="datetime" placeholder="YYYY-MM-DD"/><br/>
            </div>
            <div class="input-container">
                <label>場所:　</label>
                <input class="botan" type="text" name="location" required/><br/>
            </div>
            <div class="input-container">
                <label>人数:　</label>
                <input class="botan" type="text" name="num_people"/><br/>
            </div>
            <!-- 隠し条件 -->   
            <input type="hidden" name="query" value="restaurant">
            <input id="search" type="submit" value="Search" >
        </form>
    </div>
    <div id="sub">
        <!-- <div id="sub"> -->
        <c:forEach var="url" items="${randomUrls}">
            <button class="button" onclick="window.open('${url}', '_blank')"><img src=images/g.png  width="80" height="80"></button>
        </c:forEach>
    </div>
</div>
</body>
</html>
