package ua.nure.sidak.SummaryTask4.db.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ua.nure.sidak.SummaryTask4.constants.TourOrderStatus;
import ua.nure.sidak.SummaryTask4.db.DBManager;
import ua.nure.sidak.SummaryTask4.db.constants.SQLTemplates;
import ua.nure.sidak.SummaryTask4.db.entity.Tour;
import ua.nure.sidak.SummaryTask4.db.entity.TourWithDiscount;
import ua.nure.sidak.SummaryTask4.db.entity.User;

/**
 * Stores all database requests for interacting with "tours"
 * @author eXce1z0r
 *
 */
public class TourDao extends ForeignKeyDao
{
	private static TourDao instance;
	
	public static final String TABLE_NAME = "tours";
	
	public static final String TOUR_TYPE_TABLE_NAME = "tour_types";
	
	public static final String LODGING_TYPE_TABLE_NAME = "tour_lodging_types";
	
	private TourDao()
	{
		
	}
	
	public int create(Tour tour) {
		String sqlRequest = SQLTemplates.INSERT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), TourDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "name, info, price, type_id, "
										+ "lodging_type_id, hot, discount_step, discount_limit");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.INSERT_REQUEST_VALUES.getValue(), "?,?,?,?,?,?,?,?");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest, Statement.RETURN_GENERATED_KEYS))
			{
				prepStmt.setString(1, tour.getTitle());
				prepStmt.setString(2, tour.getInfo());
				prepStmt.setBigDecimal(3, tour.getPrice());
				prepStmt.setInt(4, tour.getType());

				prepStmt.setInt(5, tour.getLodging());
				prepStmt.setBoolean(6, tour.isHot());
				prepStmt.setFloat(7, tour.getDiscountStep());
				prepStmt.setFloat(8, tour.getDiscountLimit());
				
				prepStmt.executeUpdate();
				
				ResultSet tourKeyData = prepStmt.getGeneratedKeys();
				
				while(tourKeyData.next())
				{
					tour.setId(tourKeyData.getInt(1));					
					return tour.getId();
				}
				
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return -1;
	}

	public Tour getById(int id) 
	{		
		Tour foundTour = null;
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), TourDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "*");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "WHERE id=?");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				prepStmt.setInt(1, id);
				
				ResultSet tourData = prepStmt.executeQuery();
				
				while(tourData.next())
				{
					foundTour = this.parseResultSetToTour(tourData);
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		
		return foundTour;
	}
	
	public int getMaxTouristsAmount(TourOrderStatus... tourOrderStatuses)
	{
		StringBuilder whereCasePart = new StringBuilder();		
		if(tourOrderStatuses.length > 0)
		{		
			whereCasePart.append(" WHERE (");
			
			for(int i = 0; i < tourOrderStatuses.length; i++)
			{
				whereCasePart.append("status_id=?");
				
				if(i < tourOrderStatuses.length - 1)
				{
					whereCasePart.append(" OR ");
				}
			}
			
			whereCasePart.append(")");
		}
		
		String subSqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		subSqlRequest = subSqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), OrderDao.TABLE_NAME);
		subSqlRequest = subSqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "COUNT(id) as tourists_per_tour");
		subSqlRequest = subSqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), whereCasePart.toString());
		subSqlRequest = subSqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "GROUP BY tour_id");
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), "(" + subSqlRequest + ") as tourists_per_tour_table");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "MAX(tourists_per_tour)");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				for(int i = 0; i < tourOrderStatuses.length; i++)
				{
					prepStmt.setInt(i + 1, tourOrderStatuses[i].getId());
				}
				
				ResultSet maxTouristsAmountData = prepStmt.executeQuery();
				while(maxTouristsAmountData.next())
				{
					return maxTouristsAmountData.getInt(1);
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public List<Tour> getTop5ToursList()
	{
		List<Tour> top5OrderedToursList = new ArrayList<>();
		
		List<Integer> top5OrderedTourIds = DAOFactory.getOrderDao().getTop5TourIds();
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), TourDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "*");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "WHERE id=?");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "");
		
		StringBuilder sB = new StringBuilder();
		
		String sqlRequest2 = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), TourDao.TABLE_NAME);
		sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "*");
		
		if(top5OrderedTourIds.size() > 0)
		{
			sB.append("WHERE (not id=?)");
			
			for(int i = 1; i < top5OrderedTourIds.size(); i++)
			{
				sB.append(" AND (not id=?)");
			}
		
			sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), sB.toString());
		}
		else
		{
			sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), sB.toString());
		}
		
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{	
				for(int tourId : top5OrderedTourIds)
				{
					prepStmt.setInt(1, tourId);
					ResultSet top5OrderedToursData = prepStmt.executeQuery();
						
					while(top5OrderedToursData.next())
					{
						Tour tour = this.parseResultSetToTour(top5OrderedToursData);
						tour.setTouristsAmount(DAOFactory.getOrderDao().getByTour(tour).size());
						top5OrderedToursList.add(tour);
						
					}
				}
			}
			
			int notEnough = 5 - top5OrderedToursList.size();
			
			if(notEnough > 0)
			{					
				sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), " LIMIT ?");
				
				try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest2))
				{	
					int counter = 0;
					for(; counter < top5OrderedTourIds.size(); counter++)
					{
						prepStmt.setInt(counter + 1, top5OrderedTourIds.get(counter));
					}
					prepStmt.setInt(counter + 1, notEnough);
					
					ResultSet top5OrderedToursData = prepStmt.executeQuery();
							
					while(top5OrderedToursData.next())
					{
						Tour tour = this.parseResultSetToTour(top5OrderedToursData);
						tour.setTouristsAmount(DAOFactory.getOrderDao().getByTour(tour).size());
						top5OrderedToursList.add(tour);
							
					}
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return top5OrderedToursList;
	}
	
	public BigDecimal getMaxTourPrice()
	{
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), TourDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "MAX(price)");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				ResultSet maxTourPriceData = prepStmt.executeQuery();
				while(maxTourPriceData.next())
				{
					BigDecimal maxTourPrice = maxTourPriceData.getBigDecimal(1);
					if(maxTourPrice == null)
					{
						maxTourPrice = BigDecimal.ZERO;
					}
					return maxTourPrice.setScale(0, BigDecimal.ROUND_CEILING);
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int getTouristsAmountByTour(Tour tour, TourOrderStatus... tourOrderStatuses)
	{
		
		StringBuilder whereCasePart = new StringBuilder();
		
		if(tourOrderStatuses.length > 0)
		{		
			whereCasePart.append(" AND (");
			
			for(int i = 0; i < tourOrderStatuses.length; i++)
			{
				whereCasePart.append("status_id=?");
				
				if(i < tourOrderStatuses.length - 1)
				{
					whereCasePart.append(" OR ");
				}
			}
			
			whereCasePart.append(")");
		}
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), OrderDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "COUNT(id)");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "WHERE tour_id=?" + whereCasePart.toString());
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest, Statement.RETURN_GENERATED_KEYS))
			{
				prepStmt.setInt(1, tour.getId());
				
				for(int i = 0; i < tourOrderStatuses.length; i++)
				{
					prepStmt.setInt(i + 2, tourOrderStatuses[i].getId());
				}
				
				prepStmt.executeQuery();
				
				ResultSet amountOfTouristsAtTourData = prepStmt.getResultSet(); 
				while(amountOfTouristsAtTourData.next())
				{
					return amountOfTouristsAtTourData.getInt(1);
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public List<Tour> getToursByMultipleWhereCase(int minPrice, int maxPrice,
												  int touristsMinAmount, int touristsMaxAmount,
												  int tourType,
												  int lodgingType,
												  TourOrderStatus... tourOrderStatuses)
	{		
		List<Tour> toursList = new ArrayList<>();
		
		StringBuilder whereChoosePart = new StringBuilder(); 
		
		if(tourType > 0)
		{
			whereChoosePart.append("AND (type_id=?)");
		}
		
		if(lodgingType > 0)
		{
			whereChoosePart.append("AND (lodging_type_id=?)");
		}
		
		StringBuilder whereCasePart = new StringBuilder();		
		if(tourOrderStatuses.length > 0)
		{		
			whereCasePart.append(" WHERE (");
			
			for(int i = 0; i < tourOrderStatuses.length; i++)
			{
				whereCasePart.append("status_id=?");
				
				if(i < tourOrderStatuses.length - 1)
				{
					whereCasePart.append(" OR ");
				}
			}
			
			whereCasePart.append(")");
		}
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), OrderDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "tour_id, COUNT(id)");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), whereCasePart.toString());
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "GROUP BY tour_id");
		
		String sqlRequest2 = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), TourDao.TABLE_NAME);
		sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "*");
		sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), 
				"WHERE (price BETWEEN ? AND ?) " + 
				whereChoosePart.toString());
		sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "ORDER BY hot DESC");
		
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			
			//	idOfAcceptableByTouristsAmountToursList - contain  cases which 100% match
			List<Integer> idOfAcceptableByTouristsAmountToursList = new ArrayList<>();
			//	idOfAcceptableByTouristsAmountToursList - contain  cases which 100% does't match
			List<Integer> idOfNotAcceptableByTouristsAmountToursList = new ArrayList<>();
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				
				for(int i = 0; i < tourOrderStatuses.length; i++)
				{
					prepStmt.setInt(i + 1, tourOrderStatuses[i].getId());
				}
				
				ResultSet tourOrdersDataList = prepStmt.executeQuery();
				while(tourOrdersDataList.next())
				{
					int tourId = tourOrdersDataList.getInt(1);
					int tourTouristsAmount = tourOrdersDataList.getInt(2);
					
					//	for cases when no one ticket for current tour has been bought
					if(tourTouristsAmount < 0)
					{
						tourTouristsAmount = 0;
					}
					
					//idOfAcceptableByTouristsAmountToursList.add(new Integer[] {tourId, tourTouristsAmount});
					
					if(tourTouristsAmount >= touristsMinAmount && tourTouristsAmount <= touristsMaxAmount)
					{
						idOfAcceptableByTouristsAmountToursList.add(tourId);
					}
					else
					{
						idOfNotAcceptableByTouristsAmountToursList.add(tourId);
					}
				}
			}			
			
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest2))
			{
				prepStmt.setInt(1, minPrice);
				prepStmt.setInt(2, maxPrice);
				
				if(tourType > 0)
				{
					prepStmt.setInt(3, tourType);
				}
				
				if(tourType > 0 && lodgingType > 0)
				{
					prepStmt.setInt(4, lodgingType);
				}
				else if(lodgingType > 0)
				{
					prepStmt.setInt(3, lodgingType);
				}
				
				ResultSet toursDataList = prepStmt.executeQuery();
				while(toursDataList.next())
				{	
					/* "(!idOfNotAcceptableByTouristsAmountToursList.contains(tourId) && touristsMinAmount == 0)" 
					 *  where "touristsMinAmount == 0" used for cases when no one ticket for current tour has been 
					 *  bought. 
					 *  "!idOfNotAcceptableByTouristsAmountToursList.contains(tourId)" used for exclude cases
					 *  when amount of tickets which have been bought greater than "touristsMaxAmount".*/
					
					int tourId = toursDataList.getInt("id");
					if(idOfAcceptableByTouristsAmountToursList.contains(tourId) 
					|| (!idOfNotAcceptableByTouristsAmountToursList.contains(tourId) && touristsMinAmount == 0))
					{
						toursList.add(this.parseResultSetToTour(toursDataList));
					}
				}
			}			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return toursList;
	}
	
	public List<TourWithDiscount> getToursByMultipleWhereCase(int userId, 
															  int minPrice, int maxPrice,
															  int touristsMinAmount, int touristsMaxAmount,
															  int tourType,
															  int lodgingType,
															  TourOrderStatus... tourOrderStatuses)
	{		
		List<TourWithDiscount> toursList = new ArrayList<>();

		StringBuilder whereChoosePart = new StringBuilder(); 

		if(tourType > 0)
		{
			whereChoosePart.append("AND (type_id=?)");
		}
		
		if(lodgingType > 0)
		{
			whereChoosePart.append("AND (lodging_type_id=?)");
		}
		
		StringBuilder whereCasePart = new StringBuilder();		
		if(tourOrderStatuses.length > 0)
		{		
			whereCasePart.append(" WHERE (");
			
			for(int i = 0; i < tourOrderStatuses.length; i++)
			{
				whereCasePart.append("status_id=?");
				
				if(i < tourOrderStatuses.length - 1)
				{
					whereCasePart.append(" OR ");
				}
			}
			
			whereCasePart.append(")");
		}
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), OrderDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "tour_id, COUNT(id)");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), whereCasePart.toString());
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "GROUP BY tour_id");
		
		String sqlRequest2 = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), TourDao.TABLE_NAME);
		sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "*");
		sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), 
																	"WHERE (price BETWEEN ? AND ?) " + 
																	//"AND (tourists_amount BETWEEN ? AND ?) " + 
																	whereChoosePart.toString());
		sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "ORDER BY hot DESC");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{			
			//	idOfAcceptableByTouristsAmountToursList - contain  cases which 100% match
			List<Integer> idOfAcceptableByTouristsAmountToursList = new ArrayList<>();
			
			//	idOfAcceptableByTouristsAmountToursList - contain  cases which 100% does't match
			List<Integer> idOfNotAcceptableByTouristsAmountToursList = new ArrayList<>();
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				
				for(int i = 0; i < tourOrderStatuses.length; i++)
				{
					prepStmt.setInt(i + 1, tourOrderStatuses[i].getId());
				}
				
				ResultSet tourOrdersDataList = prepStmt.executeQuery();
				while(tourOrdersDataList.next())
				{
					int tourId = tourOrdersDataList.getInt(1);
					int tourTouristsAmount = tourOrdersDataList.getInt(2);
					
					if(tourTouristsAmount >= touristsMinAmount && tourTouristsAmount <= touristsMaxAmount)
					{
						idOfAcceptableByTouristsAmountToursList.add(tourId);
					}
					else
					{
						idOfNotAcceptableByTouristsAmountToursList.add(tourId);
					}
				}
			}		
			
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest2))
			{
				prepStmt.setInt(1, minPrice);
				prepStmt.setInt(2, maxPrice);
				
				if(tourType > 0)
				{
					prepStmt.setInt(3, tourType);
				}
				
				if(tourType > 0 && lodgingType > 0)
				{
					prepStmt.setInt(4, lodgingType);
				}
				else if(lodgingType > 0)
				{
					prepStmt.setInt(3, lodgingType);
				}
				
				ResultSet toursDataList = prepStmt.executeQuery();
				while(toursDataList.next())
				{
					/* "(!idOfNotAcceptableByTouristsAmountToursList.contains(tourId) && touristsMinAmount == 0)" 
					 *  where "touristsMinAmount == 0" used for cases when no one ticket for current tour has been 
					 *  bought. 
					 *  "!idOfNotAcceptableByTouristsAmountToursList.contains(tourId)" used for exclude cases
					 *  when amount of tickets which have been bought greater than "touristsMaxAmount".*/
					
					int tourId = toursDataList.getInt("id");
					if(idOfAcceptableByTouristsAmountToursList.contains(tourId) 
					|| (!idOfNotAcceptableByTouristsAmountToursList.contains(tourId) && touristsMinAmount == 0))
					{
						toursList.add(this.parseResultSetToTourWithDiscount(toursDataList, userId));
					}
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return toursList;
	}
	
	public List<Tour> getAll(int userId) 
	{
		List<Tour> toursList = new ArrayList<>();
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), TourDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "*");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "ORDER BY hot DESC");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				ResultSet toursDataList = prepStmt.executeQuery();
				
				while(toursDataList.next())
				{
					toursList.add(this.parseResultSetToTourWithDiscount(toursDataList, userId));
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return toursList;
	}
	
	public List<Tour> getAll() 
	{
		List<Tour> toursList = new ArrayList<>();
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), TourDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "*");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "ORDER BY hot DESC");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				ResultSet toursDataList = prepStmt.executeQuery();
				
				while(toursDataList.next())
				{
					toursList.add(this.parseResultSetToTour(toursDataList));
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return toursList;
	}
	
	public boolean removeById(int id) 
	{
		String sqlRequest = SQLTemplates.DELETE_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), TourDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "WHERE id=?");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				prepStmt.setInt(1, id);
				
				prepStmt.executeUpdate();
				return true;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public int update(Tour tour) 
	{
		String sqlRequest = SQLTemplates.UPDATE_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), TourDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.UPDATE_SET_EXPRESSION.getValue(), "name=?, info=?, "
				+ "price=?, type_id=?, lodging_type_id=?, hot=?, "
				+ "discount_step=?, discount_limit=?");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "WHERE id=?");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest, Statement.RETURN_GENERATED_KEYS))
			{
				prepStmt.setString(1, tour.getTitle());
				prepStmt.setString(2, tour.getInfo());
				prepStmt.setBigDecimal(3, tour.getPrice());
				prepStmt.setInt(4, tour.getType());
				prepStmt.setInt(5, tour.getLodging());
				prepStmt.setBoolean(6, tour.isHot());
				prepStmt.setFloat(7, tour.getDiscountStep());
				prepStmt.setFloat(8, tour.getDiscountLimit());
				prepStmt.setInt(9, tour.getId());
				
				prepStmt.executeUpdate();
				
				ResultSet updatedTourId = prepStmt.getGeneratedKeys();
				while(updatedTourId.next())
				{
					tour.setId(updatedTourId.getInt(1));
					return tour.getId();
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static TourDao getInstance()
	{
		if(TourDao.instance == null)
		{
			TourDao.instance = new TourDao();
		}
		return TourDao.instance;
	}
	
	private Tour parseResultSetToTour(ResultSet tourData) throws SQLException
	{
		Tour tour = new Tour();
		
		tour.setId(tourData.getInt("id"));
		tour.setTitle(tourData.getString("name"));
		tour.setInfo(tourData.getString("info"));
		tour.setPrice(tourData.getBigDecimal("price"));
		tour.setType(tourData.getInt("type_id"));
		tour.setLodging(tourData.getInt("lodging_type_id"));
		tour.setHot(tourData.getBoolean("hot"));
		tour.setDiscountStep(tourData.getFloat("discount_step"));
		tour.setDiscountLimit(tourData.getFloat("discount_limit"));
		
		return tour;
	}
	
	private TourWithDiscount parseResultSetToTourWithDiscount(ResultSet tourData, int userId) throws SQLException
	{
		TourWithDiscount tour = new TourWithDiscount();
		
		tour.setId(tourData.getInt("id"));
		tour.setTitle(tourData.getString("name"));
		tour.setInfo(tourData.getString("info"));
		tour.setPrice(tourData.getBigDecimal("price"));
		tour.setType(tourData.getInt("type_id"));
		tour.setLodging(tourData.getInt("lodging_type_id"));
		tour.setHot(tourData.getBoolean("hot"));
		tour.setDiscountStep(tourData.getFloat("discount_step"));
		tour.setDiscountLimit(tourData.getFloat("discount_limit"));
		
		User user = new User();
		user.setId(userId);		
		tour.setDiscount(DAOFactory.getOrderDao().getUserDiscountOnTour(tour, user));
		
		return tour;
	}
}
