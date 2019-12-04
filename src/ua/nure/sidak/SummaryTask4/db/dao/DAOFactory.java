package ua.nure.sidak.SummaryTask4.db.dao;

/**
 * Organizes access to DAO objects
 * @author eXce1z0r
 *
 */
public final class DAOFactory 
{
	private DAOFactory()
	{
		
	}
	
	public static UserDao getUserDao()
	{
		return UserDao.getInstance();
	}
	
	public static TourDao getTourDao()
	{
		return TourDao.getInstance();
	}
	
	public static OrderDao getOrderDao()
	{
		return OrderDao.getInstance();
	}
}
