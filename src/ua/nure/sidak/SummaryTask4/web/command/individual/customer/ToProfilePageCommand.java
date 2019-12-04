package ua.nure.sidak.SummaryTask4.web.command.individual.customer;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.sidak.SummaryTask4.constants.GeneralAttributes;
import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.db.dao.DAOFactory;
import ua.nure.sidak.SummaryTask4.db.dao.OrderDao;
import ua.nure.sidak.SummaryTask4.db.entity.User;
import ua.nure.sidak.SummaryTask4.web.command.Command;
import ua.nure.sidak.SummaryTask4.web.utility.Localizator;
import ua.nure.sidak.SummaryTask4.web.utility.MultiParser;

/**
 * Used for moving to logged customer page
 * @author eXce1z0r
 *
 */
public class ToProfilePageCommand extends Command 
{

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		int userId = MultiParser.objectToInteger(req.getSession().getAttribute(GeneralAttributes.SESSION_USER_ID.getValue()));
			
		if(userId > -1)
		{
			User user = new User();
			user.setId(userId);
			
			ToProfilePageCommand.setAttributesForPage(req, user);
			
			return PagePath.PAGE_CUSTOMER;
		}
		
		req.getSession().invalidate();
		
		return PagePath.PAGE_LOGIN;
	}
	
	public static void setAttributesForPage(HttpServletRequest req, User user)
	{
		Locale currentSessionLocale = null;		
		try
		{
			currentSessionLocale = (Locale) req.getSession().getAttribute(GeneralAttributes.SESSION_CLIENT_LOCALE.getValue());
		}
		catch(Exception e)
		{
			
		}
		
		req.setAttribute(GeneralAttributes.ORDER_STATUSES_LIST.getValue(), DAOFactory.getOrderDao().getForeingKeyField(OrderDao.ORDER_STATUS_TABLE_NAME, currentSessionLocale));				
		req.setAttribute(GeneralAttributes.ORDERS_LIST.getValue(), DAOFactory.getOrderDao().getByUser(user));
	
		ToProfilePageCommand.setLocalizationAttributesForPage(req);
	}
	
	private static void setLocalizationAttributesForPage(HttpServletRequest req)
	{
		Localizator localizator = new Localizator(req);
		
		String attributeName = GeneralAttributes.TOUR_SEARCH_PANEL_LINK.getValue();
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.CUSTOMER_PROFILE_PANEL_LINK.getValue();
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
		
		attributeName = GeneralAttributes.TABLE_TOUR_INFO.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.TABLE_ORDER_STATUS.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));	
	}
}
