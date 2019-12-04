package ua.nure.sidak.SummaryTask4.web;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.sidak.SummaryTask4.constants.GeneralAttributes;
import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.web.command.common.TourSearchCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.admin.ToUserManagementPageCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.manager.ToOrdersManagementPageCommand;
/**
 * Used for PRG template realization.
 * @author eXce1z0r
 *
 */
@WebServlet("/result_controller")
public class ResultController extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{		
		Enumeration<String> parameterNames = req.getParameterNames();
		while(parameterNames.hasMoreElements())
		{
			String parameterName = parameterNames.nextElement();
			String parameterValue = req.getParameter(parameterName);
			
			req.setAttribute(parameterName, parameterValue);
		}
		
		String commandName = req.getParameter("command");
		
		if(commandName != null)
		{
			switch(commandName)
			{
				case "updateUserStatusActionCommand":
				case "createNewUserActionCommand":
					ToUserManagementPageCommand.setAttributesForPage(req);
					req.getRequestDispatcher(PagePath.PAGE_ADMIN).forward(req, resp);
					break;
					
				case "updateOrDestroyTourActionCommand":
				case "createNewTourActionCommand":
				case "updateTourDiscountOptionsActionCommand":
				case "makeAnOrderActionCommand":
					TourSearchCommand.fillSearchForm(req);
					req.getRequestDispatcher(PagePath.PAGE_TOURS_SEARCH).forward(req, resp);
					break;
				
				case "updateOrderStatusActionCommand":
					ToOrdersManagementPageCommand.setAttributesForPage(req);
					req.getRequestDispatcher(PagePath.PAGE_MANAGER).forward(req, resp);
					break;
					
				default:
					req.setAttribute(GeneralAttributes.ERROR_MESSAGE_EL_TAG.getValue(), "Oops, something went wrong.");
					req.getRequestDispatcher(PagePath.PAGE_ERROR).forward(req, resp);
			}
		}
	}
}
