package ua.nure.sidak.SummaryTask4.web.command.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.nure.sidak.SummaryTask4.constants.GeneralAttributes;
import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.constants.UserRole;
import ua.nure.sidak.SummaryTask4.db.dao.DAOFactory;
import ua.nure.sidak.SummaryTask4.db.entity.User;
import ua.nure.sidak.SummaryTask4.web.command.Command;
import ua.nure.sidak.SummaryTask4.web.command.individual.admin.ToUserManagementPageCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.customer.ToProfilePageCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.manager.ToOrdersManagementPageCommand;
import ua.nure.sidak.SummaryTask4.web.utility.Encoder;
import ua.nure.sidak.SummaryTask4.web.utility.FieldChecker;
import ua.nure.sidak.SummaryTask4.web.utility.Localizator;

/**
 * Used for processing authentication.
 * @author eXce1z0r
 *
 */
public class LoginCommand extends Command
{
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{	
		HttpSession session = req.getSession(true);
		
		//	if user trying to access "login" page first time
		
		String login = Encoder.encodeString(req.getParameter("loginField"));
		String password = Encoder.encodeString(req.getParameter("passwordField"));
		
		Localizator localizator = new Localizator(req);
			
		//	if user enter valid data 
		if(FieldChecker.loginFieldCheck(login) > -1 && FieldChecker.passwordFieldCheck(password) > -1)
		{
			//	trying to get such user from database
			User user = DAOFactory.getUserDao().getUserByLogin(login);
			
			switch(this.authorizationStatus(user, password))
			{
				case 1:
					//	for case when user logged in successful
					session.setAttribute(GeneralAttributes.SESSION_USER_ID.getValue(), user.getId());
					session.setAttribute(GeneralAttributes.SESSION_USER_LOGIN.getValue(), user.getLogin());
					session.setAttribute(GeneralAttributes.SESSION_USER_MAIL.getValue(), user.getMail());
					
					UserRole userRole = UserRole.getRoleByRoleId(user.getRoleId());
					session.setAttribute(GeneralAttributes.SESSION_USER_ROLE.getValue(), userRole);
					
					return this.getUserStartPageWithAttributesByUser(req, user);
							
				case -1:
					// for case if such user was disabled
					req.setAttribute(GeneralAttributes.ERROR_MESSAGE_EL_TAG.getValue(), localizator.getContent("loginErrorCode.disabledAccount"));
					
					break;						
				default:
					// for case if such user doesn't exist
					req.setAttribute(GeneralAttributes.ERROR_MESSAGE_EL_TAG.getValue(), localizator.getContent("loginErrorCode.wrongCreds"));				
					
					break;
			}
		}
		else
		{
			// if user enter not valid data
			
			// if it was "post" request was
			if(req.getMethod().equalsIgnoreCase("post"))
			{
				req.setAttribute(GeneralAttributes.ERROR_MESSAGE_EL_TAG.getValue(), localizator.getContent("loginErrorCode.loginFieldsInvalidData"));
				req.setAttribute(GeneralAttributes.USER_LOGIN_EL_TAG.getValue(), login);
				req.setAttribute(GeneralAttributes.USER_PASSWORD_EL_TAG.getValue(), password);
			}
			
			//	if it was any other request than no error should be
		}
		
		ToLoginPageCommand.setAttributesForPage(req);
		
		return PagePath.PAGE_LOGIN;
	}
	
	private byte authorizationStatus(User user, String password)
	{
		//	check if such user exist into db
		if(user != null)
		{
			//	check if password of user corresponds to password which was entered
			if(this.isPasswordRight(user, password))
			{
				//	checks is current user status active
				if(this.isActive(user))
				{
					return 1;
				}
				
				return -1;
			}
		}		
		return -2;
	}
	
	private boolean isPasswordRight(User dbUser, String password)
	{
		return dbUser.getPassword().equals(password);
	}
	
	private boolean isActive(User user)
	{
		return user.isStatus();
	}
	
	private String getUserStartPageWithAttributesByUser(HttpServletRequest req, User user)
	{
		UserRole userRole = UserRole.getRoleByRoleId(user.getRoleId());
		
		this.setStartPageAttributesByUser(req, user, userRole);
		
		switch(userRole)
		{
			case MANAGER:
				return PagePath.PAGE_MANAGER;
			case ADMIN:
				return PagePath.PAGE_ADMIN;
			case CUSTOMER:
				return PagePath.PAGE_CUSTOMER;
			default:
				return PagePath.PAGE_TOURS_SEARCH;
		}
	}
	
	private void setStartPageAttributesByUser(HttpServletRequest req, User user, UserRole userRole)
	{		
		switch(userRole)
		{
			case ADMIN:						
				ToUserManagementPageCommand.setAttributesForPage(req);				
				break;
				
			case MANAGER:				
				ToOrdersManagementPageCommand.setAttributesForPage(req);				
				break;
				
			case CUSTOMER:
				ToProfilePageCommand.setAttributesForPage(req, user);
			
			default:
		}
	}
}
