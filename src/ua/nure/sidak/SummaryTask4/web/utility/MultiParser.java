package ua.nure.sidak.SummaryTask4.web.utility;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import ua.nure.sidak.SummaryTask4.constants.UserRole;

/**
 * Used for parsing request attributes and parameters
 * @author eXce1z0r
 *
 */
public class MultiParser 
{
	private MultiParser()
	{
		
	}
	
	public static int stringToIntValue(HttpServletRequest req, String parameterName)
	{		
		try
		{
			int intValue = Integer.parseInt(req.getParameter(parameterName));
			
			req.setAttribute(parameterName, intValue);
			
			return intValue;
		}
		catch(Exception e)
		{
			return -1;
		}
	}
	
	public static int stringToIntValue(String parameterValue)
	{		
		try
		{
			int intValue = Integer.parseInt(parameterValue);
			
			return intValue;
		}
		catch(Exception e)
		{
			return -1;
		}
	}
	
	public static float stringToFloatValue(HttpServletRequest req, String parameterName)
	{		
		try
		{
			float floatValue = Float.parseFloat(req.getParameter(parameterName));
			
			req.setAttribute(parameterName, floatValue);
			
			return floatValue;
		}
		catch(Exception e)
		{
			return -1;
		}
	}
	
	public static float stringToFloatValue(String parameterValue)
	{		
		try
		{
			float floatValue = Float.parseFloat(parameterValue);
			
			return floatValue;
		}
		catch(Exception e)
		{
			return -1;
		}
	}
	
	public static boolean stringToBoolean(String value)
	{
		if("1".equals(value))
		{
			return true;
		}

		return false;
	}
	
	public static int objectToInteger(Object containsIntegerValue)
	{
		try
		{
			return (Integer) containsIntegerValue;
		}
		catch(Exception e)
		{
			return -1;
		}
	}
	
	public static UserRole objectToUserRole(Object userRole)
	{
		try
		{
			return (UserRole) userRole;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static BigDecimal stringToBigDecimal(String parameterValue)
	{
		BigDecimal valueToReturn = null;
		if(parameterValue != null)
		{
			try
			{
				valueToReturn = new BigDecimal(parameterValue);
				
				if(valueToReturn.compareTo(BigDecimal.ZERO) < 0)
				{
					valueToReturn = null;
				}
			}
			catch(Exception e)
			{
				return valueToReturn;
			}
		}
		
		return valueToReturn;
	}
}
