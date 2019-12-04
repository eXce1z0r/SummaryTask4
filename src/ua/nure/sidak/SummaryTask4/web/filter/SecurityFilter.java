package ua.nure.sidak.SummaryTask4.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.nure.sidak.SummaryTask4.constants.GeneralAttributes;
import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.constants.UserRole;
import ua.nure.sidak.SummaryTask4.db.dao.DAOFactory;
import ua.nure.sidak.SummaryTask4.db.entity.User;
import ua.nure.sidak.SummaryTask4.web.command.common.TourSearchCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.admin.ToUserManagementPageCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.customer.ToProfilePageCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.manager.ToOrdersManagementPageCommand;
import ua.nure.sidak.SummaryTask4.web.utility.Localizator;
import ua.nure.sidak.SummaryTask4.web.utility.MultiParser;


/**
 * Used for processing authorization.
 * @author eXce1z0r
 *
 */
@WebFilter("/*")
public class SecurityFilter implements Filter 
{	
	private static List<String> loginOrRegisterCommands = new ArrayList<>();
	
	private static List<String> nonUserAvailableCommands = new ArrayList<>();
	private static List<String> userAvailableCommands = new ArrayList<>();
	private static List<String> managerAvailableCommands = new ArrayList<>();
	private static List<String> adminAvailableCommands = new ArrayList<>();
	
	private static List<String> resourcesUrlsList = new ArrayList<>();
	
	static
	{
		loginOrRegisterCommands.add("loginCommand");
		loginOrRegisterCommands.add("toLoginPageCommand");
		loginOrRegisterCommands.add("registerCommand");
		loginOrRegisterCommands.add("toRegisterPageCommand");
		
		nonUserAvailableCommands.addAll(loginOrRegisterCommands);
		nonUserAvailableCommands.add("tourListCommand");
		nonUserAvailableCommands.add("noCommand");
		
		userAvailableCommands.addAll(nonUserAvailableCommands);
		userAvailableCommands.add("logoutCommand");
		userAvailableCommands.add("toProfilePageCommand");
		userAvailableCommands.add("makeAnOrderActionCommand");
		
		managerAvailableCommands.addAll(nonUserAvailableCommands);
		managerAvailableCommands.add("logoutCommand");
		managerAvailableCommands.add("toOrdersManagementPageCommand");
		managerAvailableCommands.add("updateOrderStatusActionCommand");
		managerAvailableCommands.add("updateTourDiscountOptionsActionCommand");
		
		adminAvailableCommands.addAll(managerAvailableCommands);
		adminAvailableCommands.add("toUserManagementPageCommand");
		adminAvailableCommands.add("updateUserStatusActionCommand");
		adminAvailableCommands.add("updateOrDestroyTourActionCommand");
		adminAvailableCommands.add("createNewUserActionCommand");
		adminAvailableCommands.add("createNewTourActionCommand");	
		
		resourcesUrlsList.add("/style/bootstrap.min.css");
		resourcesUrlsList.add("/style/sign_reg_style.css");
		resourcesUrlsList.add("/js/jquery-3.3.1.slim.min.js");
		resourcesUrlsList.add("/js/bootstrap.bundle.min.js");
	}
	
	@Override
	public void init(FilterConfig filterConf) throws ServletException 
	{
		// TODO Auto-generated method stub		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
			throws IOException, ServletException 
	{
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpServletResponse httpResp = (HttpServletResponse) resp;
		
		String command = req.getParameter("command");
		
		boolean isResource = false;
		
		Localizator.checkForSessionLanguageUpdate(httpReq);	//	used for check session language updates
		
		String url = httpReq.getRequestURL().toString();
		
		//	resource redirect
		for(String resourceUrl : resourcesUrlsList)
		{
			if(url.endsWith(resourceUrl))
			{
				httpReq.getRequestDispatcher(resourceUrl).forward(req, resp);
				isResource = true;
			}
		}
		
		if(!isResource)
		{
			
			//	to redirect on new index page		
			if((command == null || command.isEmpty()) 
				&& req.getParameterMap().isEmpty())
			{
				this.redirectToIndexPage(httpReq, httpResp, command);
			}
			else
			{	
				if(this.isAccessAllowed(httpReq, httpResp, command))
				{
					filterChain.doFilter(req, resp);
				}
				else
				{
					req.setAttribute(GeneralAttributes.ERROR_MESSAGE_EL_TAG.getValue(), "You do not have permission to access the requested resource");
					req.getRequestDispatcher(PagePath.PAGE_ERROR).forward(req, resp);	
				}		
			}
		}
	}
	
	public boolean isAccessAllowed(HttpServletRequest req, HttpServletResponse resp, String command)
	{				
		
		HttpSession session = req.getSession(false);
		
		if(command == null || command.isEmpty())
		{
			return false;
		}		

		if(session != null)
		{		
			Object userRoleObject = session.getAttribute(GeneralAttributes.SESSION_USER_ROLE.getValue());
				
			if(userRoleObject != null)
			{
				UserRole userRole = (UserRole) userRoleObject;
				
				if(!this.isUserActive(
						MultiParser.objectToInteger(
								session.getAttribute(
										GeneralAttributes.SESSION_USER_ID.getValue()))))
				{
					session.invalidate();
					return false;
				}
				
				if(userRole.equals(UserRole.CUSTOMER) 
				&& SecurityFilter.userAvailableCommands.contains(command))
				{
					return true;
				}
				else if(userRole.equals(UserRole.MANAGER) 
				&& SecurityFilter.managerAvailableCommands.contains(command))
				{
					return true;
				}
				else if(userRole.equals(UserRole.ADMIN) 
				&& SecurityFilter.adminAvailableCommands.contains(command))
				{
					return true;
				}
			}
		}

		if(SecurityFilter.nonUserAvailableCommands.contains(command))
		{
			return true;
		}
		
		return false;
	}
	
	private void redirectToIndexPage(HttpServletRequest req, HttpServletResponse resp, String command)
	{		
		try 
		{			
			this.clearHttpServletRequestContent(req);
			
			String urlToRedirect = new TourSearchCommand().execute(req, resp);
				
			req.getRequestDispatcher(urlToRedirect).forward(req, resp);
		}
		catch(IOException | ServletException e)
		{
			e.printStackTrace();
		}
	}
	
	private void clearHttpServletRequestContent(HttpServletRequest req)
	{
		Enumeration<String> attributeNames = req.getAttributeNames();
		while(attributeNames.hasMoreElements())
		{
			String attributeName = attributeNames.nextElement();
			req.removeAttribute(attributeName);
		}
	}
	
	private boolean isUserActive(int userId)
	{
		User user = DAOFactory.getUserDao().getById(userId);
		
		return user.isStatus();
	}
	
	@Override
	public void destroy() 
	{
		// TODO Auto-generated method stub		
	}
}
