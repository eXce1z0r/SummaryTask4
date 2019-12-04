<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="/WEB-INF/tld/webSiteLib.tld" prefix="website" %>

<html>
	<head>
		<meta charset="UTF-8">
		<title>Register</title>
		
		<link rel="stylesheet" type="text/css" href="style/bootstrap.min.css">
	</head>
	<body style="min-width: 1000px; min-height: 700px;">
	
		<nav class="navbar navbar-expand-lg navbar-dark bg-dark" style="top: 0; width: 100%; min-width: 1000px;">
		  <div class="container">
		    <a class="navbar-brand" href="controller?command=tourListCommand"><website:printWebsiteTitle /></a>
		
		    <div class="collapse navbar-collapse show" id="navbarsExample07">
		      <ul class="navbar-nav mr-auto">
		      
		        <li class="nav-item" style="margin-right: 650px; min-width: 110px;">
		          <a class="nav-link" href="controller?command=tourListCommand">${tourSearchPanelLink}</a>
		        </li>
		        
		        <li style="margin-right: 10px;">
					<a href="controller?command=toLoginPageCommand">${logInLinkLabel}</a>
		        </li>
		        
		        <li style="float:right;">
			        <div id="language_controls">
						<a href="controller?command=toRegisterPageCommand&language=en">${enLang}</a>
						<a href="controller?command=toRegisterPageCommand&language=ru">${ruLang}</a>
					</div>
		        </li>
		        
		      </ul>
		    </div>
		  </div>
		</nav>	
		
		<div id="register_form" style="position: absolute; top: 200px; left: 40%;">
			<div>${errorMessage}</div>
			<form action="controller" method="post">
				<input type="hidden" name="command" value="registerCommand" />
				<p>${loginLabel}</p>
				<input type="text" name="loginField" value="${loginField}"/>
				<br />
				<p>${mailLabel}:</p>
				<input type="email" name="mailField" value="${mailField}"/>
				<br />
				<p>${passwordLabel}:</p>
				<input type="text" name="passwordField" value="${passwordField}"/>
				<br />
				<p>${rePasswordLabel}:</p>
				<input type="text" name="passwordFieldConfirm"/>
				<br />
				<input type="submit" value="${registerButtonText}"/>
			</form>
		</div>
	</body>
</html>