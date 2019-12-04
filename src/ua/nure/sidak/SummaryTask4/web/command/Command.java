package ua.nure.sidak.SummaryTask4.web.command;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Superclass for all commands polimorfizm realization
 * @author eXce1z0r
 *
 */
public abstract class Command implements Serializable
{	
	public abstract String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
	
	@Override
	public String toString()
	{
		return this.getClass().getSimpleName();
	}
}
