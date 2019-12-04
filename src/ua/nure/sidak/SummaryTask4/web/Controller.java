package ua.nure.sidak.SummaryTask4.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.db.DBManager;
import ua.nure.sidak.SummaryTask4.web.command.Command;
import ua.nure.sidak.SummaryTask4.web.command.CommandContainer;
import ua.nure.sidak.SummaryTask4.web.command.individual.admin.CreateNewTourActionCommand;
import ua.nure.sidak.SummaryTask4.web.utility.Localizator;

/**
 * Main application controller used for receiving requests and determine Command class for them
 * @author eXce1z0r
 *
 */
@WebServlet("/controller")
public class Controller extends HttpServlet
{	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		this.process(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		this.doGet(req, resp);
	}
	
	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String commandName = req.getParameter("command");
		
		Command command = CommandContainer.getCommandByName(commandName);
		
		String generatedForwardRequest = PagePath.PAGE_ERROR;
		
		generatedForwardRequest = command.execute(req, resp);	
		
		// PRG(Post-Redirect-Get) template realization. For requests which can make changes into database
		if(commandName != null && commandName.contains("ActionCommand"))
		{			
			//	for cases when post request which update database was received
			
			StringBuilder stringBuilder = new StringBuilder("result_controller?command=" + commandName);
			
			Enumeration<String> parameterNames = req.getParameterNames();
			while(parameterNames.hasMoreElements())
			{
				String parameterName = parameterNames.nextElement();
				String parameterValue = req.getParameter(parameterName);
				
				stringBuilder.append("&").append(parameterName).append("=").append(parameterValue);				
			}
			
			resp.sendRedirect(stringBuilder.toString());
			
		}
		else
		{
			//	for cases when any other request which does not update database was received
			
			req.getRequestDispatcher(generatedForwardRequest).forward(req, resp);
		}
	}
}
