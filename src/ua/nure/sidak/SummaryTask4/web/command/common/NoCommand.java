package ua.nure.sidak.SummaryTask4.web.command.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.sidak.SummaryTask4.constants.GeneralAttributes;
import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.web.command.Command;

/**
 * Used for wrong command name representation as error
 * @author eXce1z0r
 *
 */
public class NoCommand extends Command
{

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{	
		req.setAttribute(GeneralAttributes.ERROR_MESSAGE_EL_TAG.getValue(), "No such command");
		
		return PagePath.PAGE_ERROR;
	}

}
