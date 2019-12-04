<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/webSiteLib.tld" prefix="website" %>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${userName} profile page</title>
		
		<link rel="stylesheet" type="text/css" href="style/bootstrap.min.css">
	</head>
	<body style="min-width: 1000px; min-height: 700px;">
	
		<nav class="navbar navbar-expand-lg navbar-dark bg-dark" style="top: 0; width: 100%; min-width: 1000px;">
		  <div class="container">
		    <a class="navbar-brand" href="controller?command=tourListCommand"><website:printWebsiteTitle /></a>
		
		    <div class="collapse navbar-collapse show" id="navbarsExample07">
		      <ul class="navbar-nav mr-auto">
		      
		        <li class="nav-item" style="min-width: 110px;">
		          <a class="nav-link" href="controller?command=tourListCommand">${tourSearchPanelLink}</a>
		        </li>        
		        
		        <li class="nav-item" style="min-width: 100px; margin-right: 400px;">
					<a class="nav-link" href="controller?command=toProfilePageCommand">${customerProfileLink}</a>
				</li>
		        
		        <li style="margin-right: 10px;">
					<a href="controller?command=logoutCommand">${logoutLinkLabel}</a>
				</li>
		        
		        <li style="float:right;">
			        <div id="language_controls">
						<a href="controller?command=toProfilePageCommand&language=en">${enLang}</a>
						<a href="controller?command=toProfilePageCommand&language=ru">${ruLang}</a>
					</div>
		        </li>
		        
		      </ul>
		    </div>
		  </div>
		</nav>	
		
		<div id="profile_info">
			<p>${loginLabel}: ${userName}</p>
			<p>${mailLabel}: ${userMail}</p>
		</div>
		
		<div id="orders" style="width:100%; overflow-x: scroll;">
			<table style="text-align: center; vertical-align: middle; border: 1px solid black;">
				<tr>
					<td style="border: 1px solid black;">${tableTourInfoColumn}</td>
					<td style="border: 1px solid black;">${tableOrderStatusColumn}</td>
				</tr>			
				<c:forEach items="${ordersList}" var="order">
					<tr>
						<td style="border: 1px solid black;">
							<p>Title: ${order.tour.title}<p>
							<p>Price: ${order.tour.price}</p>
							<p>Discount: ${order.discount}</p>
						</td>
						<td style="border: 1px solid black;">
							<c:forEach items="${orderStatusesList}" var="orderStatus">
								<c:if test="${order.statusId == orderStatus.id}">
									${orderStatus.name}
								</c:if>
							</c:forEach>
						</td>
					</tr>			
				</c:forEach>
			</table>
		</div>
		
	</body>
</html>