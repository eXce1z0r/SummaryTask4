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
 * Used for moving on register page
 * @author eXce1z0r
 *
 */
public class ToRegisterPageCommand extends Command
{

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{		
		ToRegisterPageCommand.setAttributesForPage(req);
		
		return PagePath.PAGE_REGISTER;
	}
	
	public static void setAttributesForPage(HttpServletRequest req)
	{
		String loginField = Encoder.encodeString(req.getParameter(GeneralAttributes.USER_LOGIN_EL_TAG.getValue()));
		String mailField = Encoder.encodeString(req.getParameter(GeneralAttributes.USER_MAIL_EL_TAG.getValue()));
		String passwordField = Encoder.encodeString(req.getParameter(GeneralAttributes.USER_PASSWORD_EL_TAG.getValue()));

		req.setAttribute(GeneralAttributes.USER_LOGIN_EL_TAG.getValue(), loginField);
		req.setAttribute(GeneralAttributes.USER_MAIL_EL_TAG.getValue(), mailField);
		req.setAttribute(GeneralAttributes.USER_PASSWORD_EL_TAG.getValue(), passwordField);
		
		ToRegisterPageCommand.setLocalizationAttributesForPage(req);
	}
	
	private static void setLocalizationAttributesForPage(HttpServletRequest req)
	{
		Localizator localizator = new Localizator(req);
		
		String attributeName = GeneralAttributes.TOUR_SEARCH_PANEL_LINK.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.LOG_IN_LINK_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.EN_LANG.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.RU_LANG.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.LOGIN_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.MAIL_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.PASSWORD_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.REPASSWORD_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.REGISTER_BUTTON.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
	}
}
