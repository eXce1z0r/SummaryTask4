package ua.nure.sidak.SummaryTask4.web.command.individual.admin;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.db.dao.DAOFactory;
import ua.nure.sidak.SummaryTask4.db.entity.Tour;
import ua.nure.sidak.SummaryTask4.web.command.Command;
import ua.nure.sidak.SummaryTask4.web.command.common.TourSearchCommand;
import ua.nure.sidak.SummaryTask4.web.utility.Encoder;
import ua.nure.sidak.SummaryTask4.web.utility.FieldChecker;
import ua.nure.sidak.SummaryTask4.web.utility.MultiParser;

/**
 * Used by admin user to create new tour
 * @author eXce1z0r
 *
 */
public class CreateNewTourActionCommand extends Command
{

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{		
		String tourTitle = Encoder.encodeString(req.getParameter("newTourTitle"));
		int tourTitleCheckResult = FieldChecker.tourTitleFieldCheck(tourTitle);
				
		String tourInfo = Encoder.encodeString(req.getParameter("newTourInfo"));
		int tourInfoCheckResult = FieldChecker.tourInfoFieldCheck(tourInfo);
		
		BigDecimal tourPrice = MultiParser.stringToBigDecimal(req.getParameter("newTourPrice"));
		
		int tourType = MultiParser.stringToIntValue(req.getParameter("newTourType"));
		int tourTypeCheckResult = FieldChecker.tourTypeFieldCheck(tourType);
		
		int tourLodgingType = MultiParser.stringToIntValue(req.getParameter("newTourLodgingType"));
		int tourLodgingTypeCheckResult = FieldChecker.tourLodgingTypeFieldCheck(tourLodgingType);
		
		float tourDiscountStep = MultiParser.stringToFloatValue(req.getParameter("newTourDiscountStep"));
		if(tourDiscountStep < 0)
		{
			tourDiscountStep = 0;
		}
		
		float tourDiscountLimit = MultiParser.stringToFloatValue(req.getParameter("newTourDiscountLimit"));
		if(tourDiscountLimit < 0)
		{
			tourDiscountLimit = 0;
		}
			
		boolean tourStatus = MultiParser.stringToBoolean(req.getParameter("newTourIsHot"));
			
		if(tourTitleCheckResult > -1 && tourInfoCheckResult > -1 && tourPrice != null 
		&& tourTypeCheckResult > -1 && tourLodgingTypeCheckResult > -1)
		{
			Tour tour = new Tour();
			tour.setTitle(tourTitle);
			tour.setInfo(tourInfo);
			tour.setPrice(tourPrice);
			tour.setType(tourType);
			tour.setLodging(tourLodgingType);
			tour.setTouristsAmount(0);
			tour.setDiscountStep(tourDiscountStep);
			tour.setDiscountLimit(tourDiscountLimit);
			tour.setHot(tourStatus);
			
			DAOFactory.getTourDao().create(tour);				
		}
		
		TourSearchCommand.fillSearchForm(req);
		
		return PagePath.PAGE_TOURS_SEARCH;
	}
}
