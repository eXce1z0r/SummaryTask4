package ua.nure.sidak.SummaryTask4.web.command.individual.admin;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.sidak.SummaryTask4.constants.GeneralAttributes;
import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.db.dao.DAOFactory;
import ua.nure.sidak.SummaryTask4.db.dao.UserDao;
import ua.nure.sidak.SummaryTask4.db.entity.ForeignKeyField;
import ua.nure.sidak.SummaryTask4.db.entity.User;
import ua.nure.sidak.SummaryTask4.web.command.Command;
import ua.nure.sidak.SummaryTask4.web.utility.Localizator;

/**
 * Used for moving to admin used controll panel
 * @author eXce1z0r
 *
 */
public class ToUserManagementPageCommand extends Command
{
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{	
		ToUserManagementPageCommand.setAttributesForPage(req);
			
		return PagePath.PAGE_ADMIN;
	}
	
	public static void setAttributesForPage(HttpServletRequest req)
	{
		List<User> usersList = DAOFactory.getUserDao().getAll();
		
		Locale currentSessionLocale = null;		
		try
		{
			currentSessionLocale = (Locale) req.getSession().getAttribute(GeneralAttributes.SESSION_CLIENT_LOCALE.getValue());
		}
		catch(Exception e)
		{
			
		}
		
		List<ForeignKeyField> rolesList = DAOFactory.getUserDao().getForeingKeyField(UserDao.STATUS_TABLE_NAME, currentSessionLocale);
		
		req.setAttribute("usersList", usersList);
		
		req.setAttribute("roles", rolesList);
		
		ToUserManagementPageCommand.setLocalizationAttributesForPage(req);
	}
	
	private static void setLocalizationAttributesForPage(HttpServletRequest req)
	{
		Localizator localizator = new Localizator(req);
		
		String attributeName = GeneralAttributes.TOUR_SEARCH_PANEL_LINK.getValue();
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.ORDERS_MANAGEMENT_PANEL_LINK.getValue();
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.USER_MANAGEMENT_PANEL_LINK.getValue();
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.LOGOUT_LINK_LABEL.getValue();
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.EN_LANG.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.RU_LANG.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.LOGIN_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.MAIL_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.TABLE_ID.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.TABLE_LOGIN.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.TABLE_MAIL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.TABLE_PASSWORD.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.TABLE_ROLE.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.TABLE_ACCOUNT_STATUS.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.TABLE_ACTION.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.AUTO_GENERATED_STUB.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.CREATE_BUTTON.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.APPLY_UPDATE_BUTTON.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
	}
}
