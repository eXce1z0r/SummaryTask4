package ua.nure.sidak.SummaryTask4.web.command.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.sidak.SummaryTask4.constants.GeneralAttributes;
import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.web.command.Command;
import ua.nure.sidak.SummaryTask4.web.utility.Encoder;
import ua.nure.sidak.SummaryTask4.web.utility.Localizator;

/**
 * Used for moving on login page
 * @author eXce1z0r
 *
 */
public class ToLoginPageCommand extends Command
{	
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		ToLoginPageCommand.setAttributesForPage(req);
		
		return PagePath.PAGE_LOGIN;
	}
	
	public static void setAttributesForPage(HttpServletRequest req)
	{
		String loginFieldContent = Encoder.encodeString(req.getParameter(GeneralAttributes.USER_LOGIN_EL_TAG.getValue()));
		req.setAttribute(GeneralAttributes.USER_LOGIN_EL_TAG.getValue(), loginFieldContent);
		
		ToLoginPageCommand.setLocalizationAttributesForPage(req);
	}
	
	private static void setLocalizationAttributesForPage(HttpServletRequest req)
	{
		Localizator localizator = new Localizator(req);
		
		String attributeName = GeneralAttributes.TOUR_SEARCH_PANEL_LINK.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.EN_LANG.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.RU_LANG.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.LOGIN_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.PASSWORD_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.REGISTER_LINK_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.LOG_BUTTON.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
	}

}