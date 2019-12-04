<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="/WEB-INF/tld/webSiteLib.tld" prefix="website" %>

<html>
	<head>
				
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		
		<title>Login</title>
		
		<!-- Bootstrap core CSS -->
		<link rel="stylesheet" type="text/css" href="style/bootstrap.min.css">
	
		<style>
	      .bd-placeholder-img {
	        font-size: 1.125rem;
	        text-anchor: middle;
	        -webkit-user-select: none;
	        -moz-user-select: none;
	        -ms-user-select: none;
	        user-select: none;
	      }
	
	      @media (min-width: 768px) {
	        .bd-placeholder-img-lg {
	          font-size: 3.5rem;
	        }
	      }
	    </style>
	
		<!-- Custom styles for this template -->
		<link rel="stylesheet" type="text/css" href="style/sign_reg_style.css">
	</head>
	
	<body class="text-center" style="min-width: 1000px; min-height: 700px;">	
		
		<nav class="navbar navbar-expand-lg navbar-dark bg-dark" style="position: absolute; top: 0; width: 100%; min-width: 1000px;">
		  <div class="container">
		    <a class="navbar-brand" href="controller?command=tourListCommand"><website:printWebsiteTitle /></a>
		
		    <div class="collapse navbar-collapse show" id="navbarsExample07">
		      <ul class="navbar-nav mr-auto">
		      
		        <li class="nav-item" style="margin-right: 650px; min-width: 110px;">
		          <a class="nav-link" href="controller?command=tourListCommand">${tourSearchPanelLink}</a>
		        </li>
		        
		        <li>
			        <div id="language_controls">
						<a href="controller?command=toLoginPageCommand&language=en">${enLang}</a>
						<a href="controller?command=toLoginPageCommand&language=ru">${ruLang}</a>
					</div>
		        </li>
		        
		      </ul>
		    </div>
		  </div>
		</nav>		

		<div style="position: absolute; top: 20%; left: 30%">${successMessage}${errorMessage}</div>
		
		<form action="controller" method="post" class="form-signin">			
			<input  type="hidden" name="command" value="loginCommand">
			
			<label for="inputLogin" class="sr-only">${loginLabel}</label>
			<input  type="text" name="loginField" value="${loginField}" id="inputLogin" class="form-control" placeholder="Login" required autofocus/>
			<br />
			
			<label for="inputPassword" class="sr-only">${passwordLabel}</label>
			<input type="password" name="passwordField" value="${passwordField}" id="inputPassword" class="form-control" placeholder="Password" required/>		
			<br />
			
			<a href="controller?command=toRegisterPageCommand">${registerLinkLabel}</a>
			
			<button class="btn btn-lg btn-primary btn-block" type="submit">${logButtonText}</button>		
		</form>

	</body>
</html>