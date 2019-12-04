package ua.nure.sidak.SummaryTask4.web.listener;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ua.nure.sidak.SummaryTask4.constants.GeneralAttributes;

/**
 * Logger for logging info about income request. Used to simplify DoDOSers location calculation
 * @author eXce1z0r
 *
 */
@WebListener
public class RequestLogger implements ServletRequestListener
{
	private static final Logger LOG = Logger.getLogger(RequestLogger.class);
	
	private static final String LINE_SEPARATOR = System.lineSeparator();
	
	@Override
	public void requestInitialized(ServletRequestEvent sre) 
	{			
		HttpServletRequest req = (HttpServletRequest) sre.getServletRequest();
		
		this.initLog4J(req.getServletContext());
				
		StringBuilder logBuilder = new StringBuilder("Request initialization started.").append(RequestLogger.LINE_SEPARATOR);	
		
		HttpSession session = req.getSession();
		if(session != null)
		{	
			logBuilder.append(GeneralAttributes.SESSION_USER_ID.getValue()).append(": ")
					  .append(session.getAttribute(GeneralAttributes.SESSION_USER_ID.getValue()))
					  .append(";");
		}
		else
		{
			logBuilder.append("Unlogged user; ");
		}
			
		logBuilder.append(" remoteAddr: ").append(req.getRemoteAddr()).append(";")
				  .append(" remoteHost: ").append(req.getRemoteHost()).append(";")
				  .append(" remotePort: ").append(req.getRemotePort()).append(";")
				  .append(" requestURL: ").append(req.getRequestURL()).append(";");
			
		logBuilder.append(" Parameters:(");
		Enumeration<String> parameterNames = req.getParameterNames();
		while(parameterNames.hasMoreElements())
		{
			String parameterName = parameterNames.nextElement();
			logBuilder.append(parameterName).append("=").append(req.getParameter(parameterName)).append("; ");
		}
		logBuilder.append(");");
		
		logBuilder.append(" Attributes:(");
		Enumeration<String> attributeNames = req.getAttributeNames();
		while(attributeNames.hasMoreElements())
		{
			String attributeName = attributeNames.nextElement();
			logBuilder.append(attributeName).append("=").append(req.getParameter(attributeName)).append("; ");
		}
		logBuilder.append(");");
			
		LOG.log(Level.INFO, logBuilder.toString());		
	}
	
	@Override
	public void requestDestroyed(ServletRequestEvent sre) 
	{
		HttpServletRequest req = (HttpServletRequest) sre.getServletRequest();	
		
		LOG.log(Level.INFO, "Request initialization finished");
	}
	
	
	private void initLog4J(ServletContext servletContext)
	{		
		LOG.log(Level.INFO, "Log4J initialization started");	//	LOG.info();
		
		try
		{
			PropertyConfigurator.configure(servletContext.getRealPath("WEB-INF/log4j.properties"));
		}
		catch(Exception e)
		{
			this.printExceptionToLogger(e);
			e.printStackTrace();
		}
		
		LOG.log(Level.INFO, "Log4J initialization finished");
	}
	
	private void printExceptionToLogger(Throwable e)
	{
		StringBuilder errorBuilder = new StringBuilder();
		
		errorBuilder.append(e.toString()).append(RequestLogger.LINE_SEPARATOR);
		
		for(StackTraceElement stackTraceElement : e.getStackTrace())
		{
			errorBuilder.append(stackTraceElement.toString()).append(RequestLogger.LINE_SEPARATOR);;
		}
		
		LOG.log(Level.ERROR, errorBuilder.toString());
	}
}
