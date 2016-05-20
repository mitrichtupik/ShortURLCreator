<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
	<head>
		<meta charset="utf-8">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
		<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/1.5.10/clipboard.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/main.js"></script>
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/main.css">
		
		<title>Short URL creator</title>
		
	</head> 
	<body>
		<header class="header">
			<div class="header-div clearfix">
				<div class="header-h1">
					<h1>Short URL creator service</h1>
				</div>
				<div class="header-menu-left"></div>
				<div class="header-menu-right">
					<ul>
						<li><span class="header-userName"></span></li><li>
						<button class="header-login-button">Login</button></li><li>
						<button class="header-signup-button">Sign up</button></li>
					</ul>
				</div>
			</div>
			<hr>
		</header>		
		<div class="main-container"></div>
	</body>
</html>
