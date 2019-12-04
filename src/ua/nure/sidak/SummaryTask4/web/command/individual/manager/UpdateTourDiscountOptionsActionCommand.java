package ua.nure.sidak.SummaryTask4.web.command.individual.manager;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.db.dao.DAOFactory;
import ua.nure.sidak.SummaryTask4.db.entity.Tour;
import ua.nure.sidak.SummaryTask4.web.command.Command;
import ua.nure.sidak.SummaryTask4.web.command.common.TourSearchCommand;
import ua.nure.sidak.SummaryTask4.web.utility.MultiParser;

/**
 * Used for updating in database tour discount field
 * @author eXce1z0r
 *
 */
public class UpdateTourDiscountOptionsActionCommand extends Command
{
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{		
		int tourId = MultiParser.stringToIntValue(req.getParameter("tourId"));
		float discountStep = MultiParser.stringToFloatValue(req.getParameter("discountStep"));
		float discountLimit = MultiParser.stringToFloatValue(req.getParameter("discountLimit"));
			
		if(discountStep >= Tour.LOWEST_DISCOUNT && discountStep <= Tour.HIGHEST_DISCOUNT 
		&& discountLimit >= Tour.LOWEST_DISCOUNT && discountLimit <= Tour.HIGHEST_DISCOUNT)
		{
			Tour tour = DAOFactory.getTourDao().getById(tourId);
				
			if(tour != null)
			{
				tour.setDiscountStep(discountStep);
				tour.setDiscountLimit(discountLimit);
					
				tour.setHot(MultiParser.stringToBoolean(req.getParameter("isHot")));
					
				DAOFactory.getTourDao().update(tour);
			}			
		}
		
		TourSearchCommand.fillSearchForm(req);
		
		return PagePath.PAGE_TOURS_SEARCH;
	}

}
