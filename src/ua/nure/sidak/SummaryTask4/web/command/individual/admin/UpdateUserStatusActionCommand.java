package ua.nure.sidak.SummaryTask4.web.command.individual.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.db.dao.DAOFactory;
import ua.nure.sidak.SummaryTask4.db.entity.User;
import ua.nure.sidak.SummaryTask4.web.command.Command;
import ua.nure.sidak.SummaryTask4.web.utility.MultiParser;

/**
 * Used for updating user inside database
 * @author eXce1z0r
 *
 */
public class UpdateUserStatusActionCommand extends Command
{
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{		
		int userId = MultiParser.stringToIntValue(req.getParameter("userToUpdateId"));
							
		User user = DAOFactory.getUserDao().getById(userId);			
			
		if(MultiParser.stringToBoolean(req.getParameter("userStatus")))
		{		
			user.setStatus(true);
		}
		else
		{
			user.setStatus(false);
		}
			
		if(user != null)
		{
			//	TODO: Do something with boolean result return error or something
			DAOFactory.getUserDao().update(user);
		}
			
			ToUserManagementPageCommand.setAttributesForPage(req);			
		
		return PagePath.PAGE_ADMIN;
	}
}
