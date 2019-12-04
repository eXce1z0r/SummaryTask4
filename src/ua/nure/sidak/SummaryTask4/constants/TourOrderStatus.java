package ua.nure.sidak.SummaryTask4.constants;

/**
 * Contains representation of values from table 'user_tour_order_statuses' 
 * @author eXce1z0r
 *
 */
public enum TourOrderStatus 
{
	REGISTERED(1), PAYED(2), CANCELED(3);
	
	private int id;
	
	TourOrderStatus(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return id;
	}

}
