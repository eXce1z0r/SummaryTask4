package ua.nure.sidak.SummaryTask4.web.command.common;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.sidak.SummaryTask4.constants.GeneralAttributes;
import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.constants.TourOrderStatus;
import ua.nure.sidak.SummaryTask4.constants.UserRole;
import ua.nure.sidak.SummaryTask4.db.dao.DAOFactory;
import ua.nure.sidak.SummaryTask4.db.dao.TourDao;
import ua.nure.sidak.SummaryTask4.db.entity.Tour;
import ua.nure.sidak.SummaryTask4.db.entity.ForeignKeyField;
import ua.nure.sidak.SummaryTask4.db.entity.TourWithDiscount;
import ua.nure.sidak.SummaryTask4.db.entity.User;
import ua.nure.sidak.SummaryTask4.web.command.Command;
import ua.nure.sidak.SummaryTask4.web.utility.Localizator;
import ua.nure.sidak.SummaryTask4.web.utility.MultiParser;

/**
 * Used for moving on tour search page
 * @author eXce1z0r
 *
 */
public class TourSearchCommand extends Command
{

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		TourSearchCommand.fillSearchForm(req);
		
		return PagePath.PAGE_TOURS_SEARCH;
	}
	
	public static void fillSearchForm(HttpServletRequest req)
	{
		int minPriceLimit = MultiParser.stringToIntValue(req, "priceMinRange");	
		if(minPriceLimit < 0)
		{
			minPriceLimit = 0;
			req.setAttribute("priceMinRange", minPriceLimit);
		}
		int maxPriceLimit = MultiParser.stringToIntValue(req, "priceMaxRange");
		if(maxPriceLimit < 0)
		{
			if(maxPriceLimit == -1)
			{
				BigDecimal maxPriceBD = DAOFactory.getTourDao().getMaxTourPrice();
				if(maxPriceBD != null)
				{
					maxPriceLimit = maxPriceBD.intValue();
				}
				else
				{
					maxPriceLimit = 999999999;
				}
			}
			req.setAttribute("priceMaxRange", maxPriceLimit);
		}
		int touristsMinAmount = MultiParser.stringToIntValue(req, "humansMinAmount");	
		if(touristsMinAmount < 0)
		{
			touristsMinAmount = 0;
			req.setAttribute("humansMinAmount", 0);
		}
		int touristsMaxAmount = MultiParser.stringToIntValue(req, "humansMaxAmount");
		if(touristsMaxAmount < 0)
		{
			touristsMaxAmount = DAOFactory.getTourDao().getMaxTouristsAmount(TourOrderStatus.PAYED, TourOrderStatus.REGISTERED);
			if(touristsMaxAmount < 0)
			{
				touristsMaxAmount = 999999;
			}
			req.setAttribute("humansMaxAmount", touristsMaxAmount);
		}
		
		int tourTypeId = MultiParser.stringToIntValue(req, "type");
		int lodgingTypeId = MultiParser.stringToIntValue(req, "lodgingType");
		
		List tours = null;
		
		UserRole userRole = (UserRole) req.getSession().getAttribute(GeneralAttributes.SESSION_USER_ROLE.getValue());
		
		if("post".equalsIgnoreCase(req.getMethod()))
		{
			//	for post request to page
			
			//	to represent a customer discount on tours_page
			if(userRole != null && userRole == UserRole.CUSTOMER)
			{
				//	generate tours with discount list which unique calculated for each customer
				
				int userId = MultiParser.objectToInteger(req.getSession().getAttribute(GeneralAttributes.SESSION_USER_ID.getValue()));
				
				//	current "tours" list contains "ToursWithDiscount" elements
				tours = DAOFactory.getTourDao().getToursByMultipleWhereCase(userId, minPriceLimit, maxPriceLimit, 
																touristsMinAmount, touristsMaxAmount, tourTypeId, lodgingTypeId,
																TourOrderStatus.PAYED, TourOrderStatus.REGISTERED);
				
				//	loop used for getting users registered per each tour
				List<TourWithDiscount> toursList = (List) tours;
				for(Tour tour : toursList)
				{
					TourSearchCommand.setTouristsAmountToEachTour(tour);
				}
			}
			else
			{
				// generate list of tours without discount which used by manager and admin, because for them does not generate unique discount

				//	current "tours" list contains "Tours" elements
				tours = DAOFactory.getTourDao().getToursByMultipleWhereCase(minPriceLimit, maxPriceLimit,
																touristsMinAmount, touristsMaxAmount, tourTypeId, lodgingTypeId, 
																TourOrderStatus.PAYED, TourOrderStatus.REGISTERED);
				
				//	loop used for getting users registered per each tour
				List<Tour> toursList = (List) tours;
				for(Tour tour : toursList)
				{
					TourSearchCommand.setTouristsAmountToEachTour(tour);
				}
			}
		}
		else
		{
			//	for any type of request except post to page (main purpose first get method to tour search page)
			
			if(userRole != null && userRole == UserRole.CUSTOMER)
			{			
				int userId = MultiParser.objectToInteger(req.getSession().getAttribute(GeneralAttributes.SESSION_USER_ID.getValue()));
				
				tours = DAOFactory.getTourDao().getAll(userId);
				
				List<Tour> toursList = (List) tours;
				for(Tour tour : toursList)
				{
					TourSearchCommand.setTouristsAmountToEachTour(tour);
				}
			}
			else
			{
				tours = DAOFactory.getTourDao().getAll();
				
				List<Tour> toursList = (List) tours;
				for(Tour tour : toursList)
				{
					TourSearchCommand.setTouristsAmountToEachTour(tour);
				}
			}
		}
		
		TourSearchCommand.setFunctionalityAttributesByUserRights(req);
		
		req.setAttribute("maxPrice", DAOFactory.getTourDao().getMaxTourPrice());
		
		req.setAttribute("humansAmountLimit", DAOFactory.getTourDao().getMaxTouristsAmount(TourOrderStatus.PAYED, TourOrderStatus.REGISTERED));
		
		req.setAttribute("searchResults", tours);
	}
	
	private static void setFunctionalityAttributesByUserRights(HttpServletRequest req)
	{
		int userId = MultiParser.objectToInteger(req.getSession().getAttribute(GeneralAttributes.SESSION_USER_ID.getValue()));
		
		User user = DAOFactory.getUserDao().getById(userId);
		
		// same for "lodging type" and "tour type" of any user role at search form BEGIN
		ForeignKeyField hardcodedAnyCase = new ForeignKeyField();
		hardcodedAnyCase.setId(0);
		hardcodedAnyCase.setName("-");
		
		List<ForeignKeyField> tourTypesList = new ArrayList<>();
		tourTypesList.add(hardcodedAnyCase);			
		
		List<ForeignKeyField> lodgingTypesList = new ArrayList<>();				
		lodgingTypesList.add(hardcodedAnyCase);
		// same for "lodging type" and "tour type" of any user role at search form END
		
		Locale currentSessionLocale = null;		
		try
		{
			currentSessionLocale = (Locale) req.getSession().getAttribute(GeneralAttributes.SESSION_CLIENT_LOCALE.getValue());
		}
		catch(Exception e)
		{
			
		}
		
		if(user != null)
		{
			UserRole userRole = UserRole.getRoleByRoleId(user.getRoleId());
			
			switch(userRole)
			{
				case ADMIN:
					
					List<ForeignKeyField> adminOptionUpdatesForTourTypes = DAOFactory.getTourDao().getForeingKeyField(TourDao.TOUR_TYPE_TABLE_NAME, currentSessionLocale);
					req.setAttribute("adminOptionTourTypes", adminOptionUpdatesForTourTypes);
					
					List<ForeignKeyField> adminOptionUpdatesForLodgingTypes = DAOFactory.getTourDao().getForeingKeyField(TourDao.LODGING_TYPE_TABLE_NAME, currentSessionLocale);
					req.setAttribute("adminOptionLodgingTypes", adminOptionUpdatesForLodgingTypes);
					
					tourTypesList.addAll(adminOptionUpdatesForTourTypes);
					lodgingTypesList.addAll(adminOptionUpdatesForLodgingTypes);
					
					req.setAttribute(GeneralAttributes.ADMIN_CONTROLL_PANE.getValue(), true);
					
					List<Tour> tours = DAOFactory.getTourDao().getTop5ToursList();
					
					req.setAttribute("top5ToursList", DAOFactory.getTourDao().getTop5ToursList());
					
					break;
					
				case MANAGER:					
					
					tourTypesList.addAll(DAOFactory.getTourDao().getForeingKeyField(TourDao.TOUR_TYPE_TABLE_NAME, currentSessionLocale));
					lodgingTypesList.addAll(DAOFactory.getTourDao().getForeingKeyField(TourDao.LODGING_TYPE_TABLE_NAME, currentSessionLocale));
					
					req.setAttribute(GeneralAttributes.MANAGER_CONTROLL_PANE.getValue(), true);
					
					break;
					
				case CUSTOMER:
					
					tourTypesList.addAll(DAOFactory.getTourDao().getForeingKeyField(TourDao.TOUR_TYPE_TABLE_NAME, currentSessionLocale));
					lodgingTypesList.addAll(DAOFactory.getTourDao().getForeingKeyField(TourDao.LODGING_TYPE_TABLE_NAME, currentSessionLocale));
					
					req.setAttribute(GeneralAttributes.ORDER_TOUR_PANE.getValue(), true);
					
					break;
					
				default:					
			}
			
		}
		else
		{
			//	unregistered user case
			
			tourTypesList.addAll(DAOFactory.getTourDao().getForeingKeyField(TourDao.TOUR_TYPE_TABLE_NAME, currentSessionLocale));
			lodgingTypesList.addAll(DAOFactory.getTourDao().getForeingKeyField(TourDao.LODGING_TYPE_TABLE_NAME, currentSessionLocale));
		}
		
		req.setAttribute("tourTypes", tourTypesList);	
		req.setAttribute("lodgingTypes", lodgingTypesList);	
		
		TourSearchCommand.setLocalizationAttributesForPage(req);
	}
	
	private static void setLocalizationAttributesForPage(HttpServletRequest req)
	{
		Localizator localizator = new Localizator(req);
		
		String attributeName = GeneralAttributes.TOUR_SEARCH_PANEL_LINK.getValue();
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.USER_MANAGEMENT_PANEL_LINK.getValue();
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.ORDERS_MANAGEMENT_PANEL_LINK.getValue();
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.CUSTOMER_PROFILE_PANEL_LINK.getValue();
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.LOGOUT_LINK_LABEL.getValue();
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.LOG_IN_LINK_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.REGISTER_LINK_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.EN_LANG.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.RU_LANG.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.SEARCH_FORM_NAME_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.SEARCH_FORM_MIN_TOUR_PRICE.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.SEARCH_FORM_MAX_TOUR_PRICE.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.SEARCH_FORM_MIN_HUMANS.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.SEARCH_FORM_MAX_HUMANS.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.SEARCH_FORM_TOUR_TYPE.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.SEARCH_FORM_TOUR_LODGING_TYPE.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.SEARCH_BUTTON.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));	
		
		attributeName = GeneralAttributes.RESULT_FORM_NAME_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));					
		
		attributeName = GeneralAttributes.TABLE_ID.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));	
		
		attributeName = GeneralAttributes.TABLE_TITLE_COLUMN.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));					
		
		attributeName = GeneralAttributes.TABLE_INFO_COLUMN.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));	
		
		attributeName = GeneralAttributes.TABLE_PRICE_COLUMN.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));					
		
		attributeName = GeneralAttributes.TABLE_TYPE_COLUMN.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));	
		
		attributeName = GeneralAttributes.TABLE_LODGING_TYPE_COLUMN.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));					
		
		attributeName = GeneralAttributes.TABLE_TOURISTS_AMOUNT_COLUMN.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));	
		
		attributeName = GeneralAttributes.TABLE_DISCOUNT_COLUMN.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));					
		
		attributeName = GeneralAttributes.TABLE_HOT_COLUMN.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));	
		
		attributeName = GeneralAttributes.AUTO_GENERATED_STUB.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.NON_USER_DISCOUNT_STUB.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.DISCOUNT_STEP_CONFIG_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.DISCOUNT_LIMIT_CONFIG_LABEL.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.APPLY_UPDATE_BUTTON.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.CREATE_BUTTON.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.DESTROY_BUTTON.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));
		
		attributeName = GeneralAttributes.MAKE_AN_ORDER_BUTTON.getValue();		
		req.setAttribute(attributeName, localizator.getContent(attributeName));

	}
	
	/**
	 * Set amount of orders for this tour
	 * @param tour - tour which should be filled with amount of orders for current tour
	 */
	private static void setTouristsAmountToEachTour(Tour tour)
	{
		int tourOrders = DAOFactory.getTourDao().getTouristsAmountByTour(tour, TourOrderStatus.REGISTERED, TourOrderStatus.PAYED);
		if(tourOrders < 0)
		{
			tourOrders = 0;
		}
		
		tour.setTouristsAmount(tourOrders);		
	}

}
