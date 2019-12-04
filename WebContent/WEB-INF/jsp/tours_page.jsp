<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/webSiteLib.tld" prefix="website" %>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Tours page</title>
		
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
		        
			    <c:if test="${adminPaneAccess}">
			    	<li class="nav-item" style="min-width: 100px;">	
						<a class="nav-link" href="controller?command=toUserManagementPageCommand">${userManagementPanelLink}</a>
					</li>
					<li class="nav-item" style="min-width: 100px; margin-right: 200px;">
						<a class="nav-link" href="controller?command=toOrdersManagementPageCommand">${ordersManagementPanelLink}</a>	
					</li>
				</c:if>
				
				<c:if test="${managerPaneAccess}">
					<li class="nav-item" style="min-width: 100px; margin-right: 300px;">
						<a class="nav-link" href="controller?command=toOrdersManagementPageCommand">${ordersManagementPanelLink}</a>	
					</li>
				</c:if>
				
				<c:if test="${orderTourPaneAccess}">
					<li class="nav-item" style="min-width: 100px; margin-right: 400px;">
						<a class="nav-link" href="controller?command=toProfilePageCommand">${customerProfileLink}</a>
					</li>
				</c:if>
		        
				<c:choose>				
					<c:when test="${adminPaneAccess or managerPaneAccess or orderTourPaneAccess}">
						<li style="margin-right: 10px;">
							<a href="controller?command=logoutCommand">${logoutLinkLabel}</a>
						</li>
					</c:when>					
					<c:otherwise>
						<li style="margin-right: 10px;">
							<a href="controller?command=toRegisterPageCommand">${registerLinkLabel}</a>
						</li>
						<li style="margin-right: 10px;">
							<a href="controller?command=toLoginPageCommand">${logInLinkLabel}</a>
						</li>
					</c:otherwise>
				</c:choose>
		        
		        <li style="float:right;">
			        <div id="language_controls">
						<a href="controller?command=tourListCommand&language=en">${enLang}</a>
						<a href="controller?command=tourListCommand&language=ru">${ruLang}</a>
					</div>
		        </li>
		        
		      </ul>
		    </div>
		  </div>
		</nav>
		
		<div id="search_form_container">	
			<form action="controller" method="post">			
					
					<p>${searchFormNameLabel}:</p>
									
					<input type="hidden" name="command" value="tourListCommand"/>
					
					<p>${searchFormMinTourPrice}: </p>
					<input name="priceMinRange" type="number" min="0" max="${maxPrice}" value="${priceMinRange}"/>
					<p>${searchFormMaxTourPrice}: </p>
					<input name="priceMaxRange" type="number" min="0" max="${maxPrice}" value="${priceMaxRange}"/>
					
					<p>${searchFormMinHumans}: </p>
					<input name="humansMinAmount" type="number" min="0" max="${humansAmountLimit}" value="${humansMinAmount}"/>
					<p>${searchFormMaxHumans}: </p>
					<input name="humansMaxAmount" type="number" min="0" max="${humansAmountLimit}" value="${humansMaxAmount}"/>
					
					<p>${searchFormTourType}: </p>
					<select name="type">
						<c:forEach items="${tourTypes}" var="forEachTourTypeElement">
							<c:choose>
								<c:when test="${forEachTourTypeElement.id == type}">
									<option value="${forEachTourTypeElement.id}" selected>${forEachTourTypeElement.name}</option>
								</c:when>
								<c:otherwise>
									<option value="${forEachTourTypeElement.id}">${forEachTourTypeElement.name}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					
					<p>${searchFormTourLodgingType}: </p>
					<select name="lodgingType">
						<c:forEach items="${lodgingTypes}" var="forEachLodgingTypeElement">
							<c:choose>
								<c:when test="${forEachLodgingTypeElement.id == lodgingType}">
									<option value="${forEachLodgingTypeElement.id}" selected>${forEachLodgingTypeElement.name}</option>
								</c:when>
								<c:otherwise>
									<option value="${forEachLodgingTypeElement.id}">${forEachLodgingTypeElement.name}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
					
					<input type="submit" value="${searchButtonText}"/>				
			</form>
		</div>
			
		<c:if test="${searchResults != null}">
			<div id="search_results_container" style="width:100%; overflow-x: scroll;">
			<p>${resultFormNameLabel}:</p>
				<table style="text-align: center; vertical-align: middle; border: 1px solid black;">					
					<tr>
						<td style="border: 1px solid black;">${tableIdColumn}</td>
						<td style="border: 1px solid black;">${tableTitleColumn}</td>
						<td style="border: 1px solid black;">${tableInfoColumn}</td>
						<td style="border: 1px solid black;">${tablePriceColumn}</td>
						<td style="border: 1px solid black;">${tableTypeColumn}</td>
						<td style="border: 1px solid black;">${tableLodgingTypeColumn}</td>
						<td style="border: 1px solid black;">${tableTouristsAmountColumn}</td>
						<td style="border: 1px solid black;">${tableDiscountColumn}</td>						
						<td style="border: 1px solid black;">${tableHotColumn}</td>
						<td style="border: 1px solid black;"></td>
						<td style="border: 1px solid black;"></td>
					</tr>
					
					<c:choose>							
						<c:when test="${adminPaneAccess}">
						
							<tr>
								<form action="controller" method="post" >
								
									<input type="hidden" name="command" value="createNewTourActionCommand" />
									
									<td style="border: 1px solid black;">${autoGeneratedStub}</td>
									
									<td style="border: 1px solid black;">
										<input type="text" name="newTourTitle" placeholder="title" />
									</td>
									
									<td style="border: 1px solid black;">
										<input type="text" name="newTourInfo" placeholder="info" />
									</td>
									
									<td style="border: 1px solid black;">
										<input type="number" name="newTourPrice" placeholder="price" step="0.01" min="0" />
									</td>
									
									<td style="border: 1px solid black;">
										<select name="newTourType">
											<c:forEach items="${adminOptionTourTypes}" var="tourType">
												<option value="${tourType.id}">${tourType.name}</option>
											</c:forEach>
										</select>
									</td>
									
									<td style="border: 1px solid black;">
										<select name="newTourLodgingType">
											<c:forEach items="${adminOptionLodgingTypes}" var="lodgingType">
												<option value="${lodgingType.id}">${lodgingType.name}</option>
											</c:forEach>
										</select>
									</td>
									
									<td style="border: 1px solid black;">
										0
									</td>
									
									<td style="border: 1px solid black;">
										<div id="discount_step_label_holder">${discountStepConfigLabel}: </div>
										
										<div id="discount_step_field_holder">							
											<input type="number" name="newTourDiscountStep" value="${searchResult.discountStep}" min="0" max="100" step="0.01" />
										</div>
										
										<div class="discount_procent_symb"> % </div>
														
										<div id="discount_limit_label_holder">${discountLimitConfigLabel}: </div>
										
										<div id="discount_limit_field_holder">
											<input type="number" name="newTourDiscountLimit" value="${searchResult.discountLimit}" min="0" max="100"  step="0.01" />	
										</div>
										
										<div class="discount_procent_symb"> % </div>
									</td>						
									
									<td style="border: 1px solid black;">
										<input type="checkbox" name="newTourIsHot" value="1" />
									</td>
									
									<td style="border: 1px solid black;">
										<input type="submit" value="${createButtonText}" />
									</td>
								</form>
							</tr>
					
							<c:set var="counter" value="0"/>
							<c:forEach items="${searchResults}" var="searchResult">
								<c:set var="counter" value="${counter + 1}"/>
								<tr>
														
									<td style="border: 1px solid black;"><c:out value="${counter}"/></td>
											
									<form action="controller" method="post">
											
										<input type="hidden" name="command" value="updateOrDestroyTourActionCommand" />
										<input type="hidden" name="tourId" value="${searchResult.id}" />
										<input type="hidden" name="actionToDo" value="Apply to tour" />
											
										<td style="border: 1px solid black;">
											<input type="text" name="tourTitle" value="${searchResult.title}" />
										</td>
												
										<td style="border: 1px solid black;">
											<input type="text" name="tourInfo" value="${searchResult.info}" />
										</td>
												
										<td style="border: 1px solid black;">
											<input type="number" name="tourPrice" value="${searchResult.price}" step="0.01" />
										</td>
												
										<td style="border: 1px solid black;">
											<select name="tourType">
												<c:forEach items="${adminOptionTourTypes}" var="tourType">
													<c:choose>
														<c:when test="${searchResult.type == tourType.id}">
															<option value="${tourType.id}" selected>${tourType.name}</option>
														</c:when>
														<c:otherwise>
															<option value="${tourType.id}">${tourType.name}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</td>
												
										<td style="border: 1px solid black;">
											<select name="tourLodgingType">
												<c:forEach items="${adminOptionLodgingTypes}" var="lodgingType">
													<c:choose>
														<c:when test="${searchResult.lodging == lodgingType.id}">
															<option value="${lodgingType.id}" selected>${lodgingType.name}</option>
														</c:when>
														<c:otherwise>
															<option value="${lodgingType.id}">${lodgingType.name}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</td>
												
										<td style="border: 1px solid black;">
											${searchResult.touristsAmount}
										</td>
											
										<td style="border: 1px solid black;">											
											<div id="discount_step_label_holder">${discountStepConfigLabel}: </div>
											
											<div id="discount_step_field_holder">
												<input type="number" name="discountStep" value="${searchResult.discountStep}" min="0" max="100" step="0.01" />
											</div>
											
											<div class="discount_procent_symb"> % </div>
											
											<div id="discount_limit_label_holder">${discountLimitConfigLabel}: </div>
											
											<div id="discount_limit_field_holder">
												<input type="number" name="discountLimit" value="${searchResult.discountLimit}" min="0" max="100"  step="0.01" />	
											</div>
											
											<div class="discount_procent_symb"> % </div>
										</td>
												
										<td style="border: 1px solid black;">
											<c:choose>
												<c:when test="${searchResult.hot}">
													<input type="checkbox" name="isHot" value="1" checked/>
												</c:when>
												<c:otherwise>
													<input type="checkbox" name="isHot" value="1"/>
												</c:otherwise>
											</c:choose>									
										</td>
												
										<td style="border: 1px solid black;">
											<input type="submit" value="${applyUpdateButtonText}" />
										</td>
												
									</form>
									
									<td style="border: 1px solid black;">
										<form action="controller" method="post">
											
											<input type="hidden" name="command" value="updateOrDestroyTourActionCommand" />
											<input type="hidden" name="tourId" value="${searchResult.id}" />
											<input type="hidden" name="actionToDo" value="Destroy tour" />
											
											<input type="submit" value="${destroyButtonText}" />
											
										</form>
									
									</td>
									
								</tr>
							</c:forEach>
						</c:when>
								
						<c:when test="${managerPaneAccess}">
						
							<c:set var="counter" value="0"/>
							<c:forEach items="${searchResults}" var="searchResult">
								<c:set var="counter" value="${counter + 1}"/>
								<tr>
									
									<td style="border: 1px solid black;"><c:out value="${counter}"/></td>
									
									<td style="border: 1px solid black;">${searchResult.title}</td>
									
									<td style="border: 1px solid black;">${searchResult.info}</td>
									
									<td style="border: 1px solid black;">${searchResult.price}</td>
									
									<c:forEach items="${tourTypes}" var="tourType">
										<c:if test="${searchResult.type == tourType.id}">
											<td style="border: 1px solid black;">${tourType.name}</td>
										</c:if>
									</c:forEach>
									
									<c:forEach items="${lodgingTypes}" var="lodgingType">
										<c:if test="${searchResult.lodging == lodgingType.id}">
											<td style="border: 1px solid black;">${lodgingType.name}</td>
										</c:if>
									</c:forEach>
									
									<td style="border: 1px solid black;">${searchResult.touristsAmount}</td>
									
									<form action="controller" method="post">
										<td style="border: 1px solid black;">
											<input type="hidden" name="command" value="updateTourDiscountOptionsActionCommand" />
											<input type="hidden" name="tourId" value="${searchResult.id}" />
											
											<div id="discount_step_label_holder">${discountStepConfigLabel}: </div>
											<div id="discount_step_field_holder">
												<input type="number" name="discountStep" value="${searchResult.discountStep}" min="0" max="100" step="0.01" />
											</div>
											<div class="discount_procent_symb"> % </div>
											
											<div id="discount_limit_label_holder">${discountLimitConfigLabel}: </div>
											<div id="discount_limit_field_holder">
												<input type="number" name="discountLimit" value="${searchResult.discountLimit}" min="0" max="100"  step="0.01" />	
											</div>
											<div class="discount_procent_symb"> % </div>
										</td>
										<td style="border: 1px solid black;">
											<c:choose>
												<c:when test="${searchResult.hot}">
													<input type="checkbox" name="isHot" value="1" checked/>
												</c:when>
												<c:otherwise>
													<input type="checkbox" name="isHot" value="1"/>
												</c:otherwise>
											</c:choose>									
										</td>
										<td style="border: 1px solid black;">
											<input type="submit" value="${applyUpdateButtonText}" />
										</td>
									</form>
									
								</tr>
							</c:forEach>
						
						</c:when>
						<c:when test="${orderTourPaneAccess}">
								
							<c:set var="counter" value="0"/>
							<c:forEach items="${searchResults}" var="searchResult">
								<c:set var="counter" value="${counter + 1}"/>
								<tr>
						
									<td style="border: 1px solid black;"><c:out value="${counter}"/></td>
									
									<td style="border: 1px solid black;">${searchResult.title}</td>
									
									<td style="border: 1px solid black;">${searchResult.info}</td>
									
									<td style="border: 1px solid black;">${searchResult.price}</td>
									
									<c:forEach items="${tourTypes}" var="tourType">
										<c:if test="${searchResult.type == tourType.id}">
											<td style="border: 1px solid black;">${tourType.name}</td>
										</c:if>
									</c:forEach>
									
									<c:forEach items="${lodgingTypes}" var="lodgingType">
										<c:if test="${searchResult.lodging == lodgingType.id}">
											<td style="border: 1px solid black;">${lodgingType.name}</td>
										</c:if>
									</c:forEach>
									
									<td style="border: 1px solid black;">${searchResult.touristsAmount}</td>
								
									<td style="border: 1px solid black;">
										<div id="tour_discount">
											<div class="discount_value"> ${searchResult.discount}</div>
											<div class="discount_procent_symb"> % </div>
										</div>
									</td>
																		
									<td style="border: 1px solid black;">
										${searchResult.hot}
									</td>
									
									<td style="border: 1px solid black;">
										<form action="controller" method="post">
											<input type="hidden" name="command" value="makeAnOrderActionCommand"/>
											
											<input type="hidden" name="tourId" value="${searchResult.id}" />
											
											<input type="submit" value="${makeAnOrderButtonText}"/>
										</form>
									</td>
									
								</tr>
							</c:forEach>
							
						</c:when>
						<c:otherwise>
						
							<c:set var="counter" value="0"/>
							<c:forEach items="${searchResults}" var="searchResult">
								<c:set var="counter" value="${counter + 1}"/>
								<tr>
									<td style="border: 1px solid black;"><c:out value="${counter}"/></td>
									
									<td style="border: 1px solid black;">${searchResult.title}</td>
									
									<td style="border: 1px solid black;">${searchResult.info}</td>
									
									<td style="border: 1px solid black;">${searchResult.price}</td>
									
									<c:forEach items="${tourTypes}" var="tourType">
										<c:if test="${searchResult.type == tourType.id}">
											<td style="border: 1px solid black;">${tourType.name}</td>
										</c:if>
									</c:forEach>
									
									<c:forEach items="${lodgingTypes}" var="lodgingType">
										<c:if test="${searchResult.lodging == lodgingType.id}">
											<td style="border: 1px solid black;">${lodgingType.name}</td>
										</c:if>
									</c:forEach>
									
									<td style="border: 1px solid black;">${searchResult.touristsAmount}</td>
								
									<td style="border: 1px solid black;">
										<div id="tour_discount">
											<div class="discount_value"> ${searchResult.discountStep} </div>
											<div class="discount_procent_symb"> % ${nonUserDiscountStub}</div>
										</div>
									</td>
									
									<td style="border: 1px solid black;">
										${searchResult.hot}
									</td>
									<td style="border: 1px solid black;"></td>
									
								</tr>
							</c:forEach>									
									
						</c:otherwise>
					</c:choose>							

				</table>
				
				<c:if test="${adminPaneAccess}">
					<table>
						<tr>
							<td style="border: 1px solid black;">${tableIdColumn}</td>
							<td style="border: 1px solid black;">${tableTitleColumn}</td>
							<td style="border: 1px solid black;">${tableInfoColumn}</td>
							<td style="border: 1px solid black;">${tablePriceColumn}</td>
							<td style="border: 1px solid black;">${tableTypeColumn}</td>
							<td style="border: 1px solid black;">${tableLodgingTypeColumn}</td>
							<td style="border: 1px solid black;">${tableTouristsAmountColumn}</td>
							<td style="border: 1px solid black;">${tableDiscountColumn}</td>						
							<td style="border: 1px solid black;">${tableHotColumn}</td>
						</tr>
						<c:set var="counter" value="0"/>
						<c:forEach items="${top5ToursList}" var="searchResult">
							<c:set var="counter" value="${counter + 1}"/>
							<tr>
								<td style="border: 1px solid black;"><c:out value="${counter}"/></td>
									
								<td style="border: 1px solid black;">${searchResult.title}</td>
									
								<td style="border: 1px solid black;">${searchResult.info}</td>
									
								<td style="border: 1px solid black;">${searchResult.price}</td>
									
								<c:forEach items="${tourTypes}" var="tourType">
									<c:if test="${searchResult.type == tourType.id}">
										<td style="border: 1px solid black;">${tourType.name}</td>
									</c:if>
								</c:forEach>
									
								<c:forEach items="${lodgingTypes}" var="lodgingType">
									<c:if test="${searchResult.lodging == lodgingType.id}">
										<td style="border: 1px solid black;">${lodgingType.name}</td>
									</c:if>
								</c:forEach>
									
								<td style="border: 1px solid black;">${searchResult.touristsAmount}</td>
								
								<td style="border: 1px solid black;">
									<div id="tour_discount">
										<div class="discount_value"> ${searchResult.discountStep} </div>
										<div class="discount_procent_symb"> % ${searchResult.discountLimit}</div>
									</div>
								</td>
									
								<td style="border: 1px solid black;">
									${searchResult.hot}
								</td>
									
							</tr>
						</c:forEach>
					</table>
				</c:if>			
			</div>
		</c:if>
	</body>
</html>