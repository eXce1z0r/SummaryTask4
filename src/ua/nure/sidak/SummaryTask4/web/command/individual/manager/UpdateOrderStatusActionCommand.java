package ua.nure.sidak.SummaryTask4.web.command.individual.manager;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.db.dao.DAOFactory;
import ua.nure.sidak.SummaryTask4.db.entity.Order;
import ua.nure.sidak.SummaryTask4.web.command.Command;
import ua.nure.sidak.SummaryTask4.web.utility.MultiParser;

/**
 * Used for updated order status in database
 * @author eXce1z0r
 *
 */
public class UpdateOrderStatusActionCommand extends Command
{	
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{		
		int orderId = MultiParser.stringToIntValue(req, "orderId");
		int orderStatusId = MultiParser.stringToIntValue(req, "orderStatusId");
			
		if(orderId > 0 && orderStatusId > 0)
		{
			Order order = new Order();
			order.setId(orderId);
			order.setStatusId(orderStatusId);
				
			DAOFactory.getOrderDao().updateOrderStatus(order);
		}
			
		ToOrdersManagementPageCommand.setAttributesForPage(req);
		
		return PagePath.PAGE_MANAGER;
	}
}
