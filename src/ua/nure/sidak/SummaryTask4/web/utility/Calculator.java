package ua.nure.sidak.SummaryTask4.web.utility;

/**
 * Makes all the calculations in the system.
 * @author eXce1z0r
 *
 */
public class Calculator 
{
	public static float calculateDiscount(int amountOfRegisteredTours, float discountStep, float discountLimit)
	{		
		float calculatedDiscount = amountOfRegisteredTours * discountStep;
		if(calculatedDiscount > discountLimit)
		{
			calculatedDiscount = discountLimit;
		}
		else if(calculatedDiscount < 0)
		{
			calculatedDiscount = 0;
		}
		return calculatedDiscount;
	}
}
