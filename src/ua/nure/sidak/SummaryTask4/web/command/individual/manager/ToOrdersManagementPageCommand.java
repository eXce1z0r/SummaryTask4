package ua.nure.sidak.SummaryTask4.web.command.individual.manager;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.sidak.SummaryTask4.constants.GeneralAttributes;
import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.constants.UserRole;
import ua.nure.sidak.SummaryTask4.db.dao.DAOFactory;
import ua.nure.sidak.SummaryTask4.db.dao.OrderDao;
import ua.nure.sidak.SummaryTask4.web.command.Command;
import ua.nure.sidak.SummaryTask4.web.utility.Localizator;
import ua.nure.sidak.SummaryTask4.web.utility.PermissionCheck;

/**
 * Used for moving on orders management page
 * @author eXce1z0r
 *
 */
public class ToOrdersManagementPageCommand extends Command
{
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		ToOrdersManagementPageCommand.setAttributesForPage(req);
				
		return PagePath.PAGE_MANAGER;
	}
	
	public static void setAttributesForPage(HttpServletRequest req)
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
		req.setAttribute(GeneralAttributes.ORDERS_LIST.getValue(), DAOFactory.getOrderDao().getAll());
		
		if(PermissionCheck.isAllowed(req.getSession().getAttribute(GeneralAttributes.SESSION_USER_ROLE.getValue()), UserRole.ADMIN))
		{
			req.setAttribute(GeneralAttributes.ADMIN_CONTROLL_PANE.getValue(), true);
		}
		
		ToOrdersManagementPageCommand.setLocalizationAttributesForPage(req);
	}
	
	private static void setLocalizationAttributesForPage(HttpServletRequest req)
	{
		Localizator localizator = new Localizator(req);
		
		String attributeName = GeneralAttributes.TOUR_SEARCH_PANEL_LINK.getValue();
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.ORDERS_MANAGEMENT_PANEL_LINK.getValue();
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
		
		attributeName = GeneralAttributes.TABLE_USER_INFO.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.TABLE_ORDER_STATUS.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.TITLE_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.PRICE_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.DISCOUNT_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.APPLY_UPDATE_BUTTON.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		if(PermissionCheck.isAllowed(req.getSession().getAttribute(GeneralAttributes.SESSION_USER_ROLE.getValue()), UserRole.ADMIN))
		{
			attributeName = GeneralAttributes.USER_MANAGEMENT_PANEL_LINK.getValue();		
			req.setAttribute(attributeName, localizator.getContent(attributeName));
		}	
	}
}
