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
 * Used for updating or deleting tour from database
 * @author eXce1z0r
 *
 */
public class UpdateOrDestroyTourActionCommand extends Command
{

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{				
		if("Destroy tour".equals(req.getParameter("actionToDo")))
		{
			int tourId = MultiParser.stringToIntValue(req.getParameter("tourId"));
			if(tourId > 0)
			{
				DAOFactory.getTourDao().removeById(tourId);
			}
		}
		else
		{
			int tourId = MultiParser.stringToIntValue(req.getParameter("tourId"));
			String tourTitle = Encoder.encodeString(req.getParameter("tourTitle"));
			String tourInfo = Encoder.encodeString(req.getParameter("tourInfo"));
			BigDecimal tourPrice = MultiParser.stringToBigDecimal(req.getParameter("tourPrice"));
			int tourType = MultiParser.stringToIntValue(req.getParameter("tourType"));
			int tourLodgingType = MultiParser.stringToIntValue(req.getParameter("tourLodgingType"));
			boolean isHot = MultiParser.stringToBoolean(req.getParameter("isHot"));
			float discountStep = MultiParser.stringToFloatValue(req.getParameter("discountStep"));
			float discountLimit = MultiParser.stringToFloatValue(req.getParameter("discountLimit"));
			
			if(this.checkTourParams(tourId, tourTitle, tourInfo, tourPrice, tourType, tourLodgingType, 
									this.checkDiscountOptions(discountStep, discountLimit)))
			{
				Tour tour = new Tour();
				tour.setId(tourId);
				tour.setTitle(tourTitle);
				tour.setInfo(tourInfo);
				tour.setPrice(tourPrice);
				tour.setType(tourType);
				tour.setLodging(tourLodgingType);
				tour.setHot(isHot);
				tour.setDiscountStep(discountStep);
				tour.setDiscountLimit(discountLimit);
				
				//	TODO: made something with int result statement (error message mybe)
				DAOFactory.getTourDao().update(tour);
			}
		}
		
		TourSearchCommand.fillSearchForm(req);
		
		return PagePath.PAGE_TOURS_SEARCH;
	}
	
	private boolean checkTourParams(int tourId, 
									String tourTitle, 
									String tourInfo, 
									BigDecimal tourPrice, 
									int tourType, 
									int tourLodgingType, 
									boolean discountCheckResult)
	{
		boolean resFlag = true;
		
		if(tourId < 0)
		{
			resFlag = false;
		}
		else if(FieldChecker.tourTitleFieldCheck(tourTitle) < 0)
		{
			resFlag = false;
		}		
		else if(FieldChecker.tourInfoFieldCheck(tourInfo) < 0)
		{
			resFlag = false;
		}		
		else if(tourPrice == null)
		{
			resFlag = false;
		}		
		else if(FieldChecker.tourTypeFieldCheck(tourType) < 0)
		{
			resFlag = false;
		}	
		else if(FieldChecker.tourLodgingTypeFieldCheck(tourLodgingType) < 0)
		{
			resFlag = false;
		}		
		else if(!discountCheckResult)
		{
			resFlag = false;
		}
		
		return resFlag;
	}
	
	private boolean checkDiscountOptions(float discountStep, float discountLimit)
	{
		return discountStep >= Tour.LOWEST_DISCOUNT && discountStep <= Tour.HIGHEST_DISCOUNT 
				&& discountLimit >= Tour.LOWEST_DISCOUNT && discountLimit <= Tour.HIGHEST_DISCOUNT;
	}

}
