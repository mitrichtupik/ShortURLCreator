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
				<div class="header-menu-left">
					<ul>
						<li><button class="header-create-button">Create shortURL</button></li>
						<li><button class="header-view-button">View others shortURL</button></li>
						<li><button class="header-viewAllMy-button">View all my shortURL</button></li>
					</ul>
				</div>
				<div class="header-menu-right">
					<ul>
						<li><span class="header-userName"></span></li>
						<li><button class="header-login-button">Login</button></li>
						<li><button class="header-signup-button">Sign up</button></li>
					</ul>
				</div>
			</div>
			<hr>
		</header>		
		<div class="create-url">
				<div class="create-menu">
					<label>URL to be shorten
						<br>
			  			<input type="text" class="inputLongURL" name="longURL" value="https://www.owasp.org/index.php/REST_Security_Cheat_Sheet"
			  			autocomplete="off">
		  			</label>
		  			<br>
		  			<label>Description
			  			<br>
			  			<textarea class="inputDescription" name="description" rows="4">This is another link to resource</textarea>
		  			</label>
		  			<br>
		  			<label>Tags (separate by comma)
						<br>
			  			<input type="text" class="inputTags" name="tags" value="spring, NoSQL,   mongoDB" autocomplete="off">
		  			</label>
		  			<br><br>
		  			<button class="create-button big-button">Shorten</button>
				</div>
				<div class="create-result">
					<script> new Clipboard(".copy-button");</script>
				</div>
		</div>
		<div class="view-url">
				<div class="view-menu">
					<label>Enter short URL to viewing full info
						<br>
			  			<input type="text" class="inputShortURL" name="ShortURL" value="http://localhost:8080/ShortURLCreator/Af5w6K"
			  			autocomplete="off">
		  			</label>
		  			<br><br>
		  			<button class="view-button big-button">View info</button>
				</div>
				<div class="view-result">
					<script> new Clipboard(".copy-button");</script>
				</div>
		</div>
		
	</body>
</html>
