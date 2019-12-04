package ua.nure.sidak.SummaryTask4.web.command.individual.customer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.sidak.SummaryTask4.constants.GeneralAttributes;
import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.constants.TourOrderStatus;
import ua.nure.sidak.SummaryTask4.db.dao.DAOFactory;
import ua.nure.sidak.SummaryTask4.db.entity.Order;
import ua.nure.sidak.SummaryTask4.db.entity.Tour;
import ua.nure.sidak.SummaryTask4.db.entity.User;
import ua.nure.sidak.SummaryTask4.web.command.Command;
import ua.nure.sidak.SummaryTask4.web.command.common.TourSearchCommand;
import ua.nure.sidak.SummaryTask4.web.utility.MultiParser;

/**
 * Command used by logged customer for ordering tour and adding new order into database
 * @author eXce1z0r
 *
 */
public class MakeAnOrderActionCommand extends Command
{

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{		
		int tourId = MultiParser.stringToIntValue(req.getParameter("tourId"));
			
		int userId = MultiParser.objectToInteger(req.getSession().getAttribute(GeneralAttributes.SESSION_USER_ID.getValue()));
			
		if(tourId > -1 && userId > -1)
		{
			Tour tour = new Tour();
			tour.setId(tourId);
				
			User user = new User();
			user.setId(userId);
				
			float discount = DAOFactory.getOrderDao().getUserDiscountOnTour(tour, user);
				
			Order order = new Order();
			order.setTour(tour);
			order.setUser(user);
			order.setDiscount(discount);
			order.setStatusId(TourOrderStatus.REGISTERED.getId());
				
			// TODO: Do something with return statement
			DAOFactory.getOrderDao().create(order);
		}
		
		TourSearchCommand.fillSearchForm(req);
		
		return PagePath.PAGE_TOURS_SEARCH;
	}
}
