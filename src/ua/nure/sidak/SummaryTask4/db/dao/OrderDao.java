package ua.nure.sidak.SummaryTask4.db.dao;

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
import ua.nure.sidak.SummaryTask4.db.entity.Order;
import ua.nure.sidak.SummaryTask4.db.entity.Tour;
import ua.nure.sidak.SummaryTask4.db.entity.User;
import ua.nure.sidak.SummaryTask4.web.utility.Calculator;

/**
 * Stores all database requests for interacting with "user_tour_orders"
 * @author eXce1z0r
 *
 */
public class OrderDao extends ForeignKeyDao
{
	private static OrderDao instance;
	
	public static final String TABLE_NAME = "user_tour_orders";
	
	public static final String ORDER_STATUS_TABLE_NAME = "user_tour_order_statuses";
	
	private OrderDao()
	{
		
	}
	
	public boolean create(Order order) 
	{
		String sqlRequest = SQLTemplates.INSERT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), OrderDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "tour_id, user_id, status_id, discount");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.INSERT_REQUEST_VALUES.getValue(), "?,?,?,?");
		
		if(order != null && order.getTour() != null && order.getUser() != null)
		{
			try(Connection conn = DBManager.getInstance().getConnection())
			{				
				try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
				{
					prepStmt.setInt(1, order.getTour().getId());
					prepStmt.setInt(2, order.getUser().getId());
					prepStmt.setInt(3, order.getStatusId());
					prepStmt.setFloat(4, order.getDiscount());
					
					prepStmt.executeUpdate();
					
					return true;
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public List<Integer> getTop5TourIds()
	{
		List<Integer> tours = new ArrayList<>();
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), OrderDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "tour_id, COUNT(id) as ordersAmount");
		sqlRequest = sqlRequest.replace(SQLTemplates.WHERE_CASE.getValue(), "");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "GROUP BY tour_id ORDER BY ordersAmount DESC LIMIT 5");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				ResultSet top5TourIdsData = prepStmt.executeQuery();
				
				while(top5TourIdsData.next())
				{
					tours.add(top5TourIdsData.getInt(1));
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return tours;
	}
	
	public List<Order> getByTour(Tour tour) 
	{
		List<Order> ordersList = new ArrayList<>();
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), OrderDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "*");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "WHERE tour_id=?");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "");
		
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{	
			
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				prepStmt.setInt(1, tour.getId());
				
				ResultSet ordersData = prepStmt.executeQuery();
				
				while(ordersData.next())
				{
					ordersList.add(this.parseResultSetToOrder(ordersData));
				}
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return ordersList;
	}
	
	public float getUserDiscountOnTour(Tour tour, User user)
	{
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), OrderDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "COUNT(id)");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "WHERE (tour_id=? AND user_id=?) AND (status_id=? OR status_id=?)");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "GROUP BY tour_id AND user_id");
		
		String sqlRequest2 = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), TourDao.TABLE_NAME);
		sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "discount_step, discount_limit");
		sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "WHERE id=?");
		sqlRequest2 = sqlRequest2.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			int amountOfOrders = 0;	
			
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				prepStmt.setInt(1, tour.getId());
				prepStmt.setInt(2, user.getId());
				prepStmt.setInt(3, TourOrderStatus.PAYED.getId());
				prepStmt.setInt(4, TourOrderStatus.REGISTERED.getId());
				
				ResultSet ordersAmountData = prepStmt.executeQuery();							
				while(ordersAmountData.next())
				{
					amountOfOrders = ordersAmountData.getInt(1);
				}
			}
			
			float discountStep = 0;
			float discountLimit = 0;
			
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest2))
			{
				prepStmt.setInt(1, tour.getId());
				
				ResultSet tourDiscountData = prepStmt.executeQuery(); 
				while(tourDiscountData.next())
				{
					discountStep = tourDiscountData.getFloat(1);
					discountLimit = tourDiscountData.getFloat(2);
				}
			}
			
			return Calculator.calculateDiscount(amountOfOrders, discountStep, discountLimit);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return 0;
	}

	public List<Order> getByUser(User user) 
	{
		List<Order> ordersList = new ArrayList<>();
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), OrderDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "*");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "WHERE user_id=?");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{				
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				prepStmt.setInt(1, user.getId());
				
				ResultSet ordersData = prepStmt.executeQuery();
				
				while(ordersData.next())
				{
					ordersList.add(this.parseResultSetToOrder(ordersData));
				}
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return ordersList;
	}

	public List<Order> getAll() 
	{
		List<Order> ordersList = new ArrayList<>();
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), OrderDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "*");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{				
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				ResultSet ordersData = prepStmt.executeQuery();
				
				while(ordersData.next())
				{
					ordersList.add(this.parseResultSetToOrder(ordersData));
				}
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return ordersList;
	}
	
	public int updateOrderStatus(Order order)
	{
		String sqlRequest = SQLTemplates.UPDATE_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), OrderDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.UPDATE_SET_EXPRESSION.getValue(), "status_id=?");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "WHERE id=?");
	
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest, Statement.RETURN_GENERATED_KEYS))
			{
				prepStmt.setInt(1, order.getStatusId());
				prepStmt.setInt(2, order.getId());
				
				prepStmt.executeUpdate();
				
				ResultSet updatedOrderKeyData = prepStmt.getGeneratedKeys();				
				while(updatedOrderKeyData.next())
				{
					return updatedOrderKeyData.getInt(1);
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return -1;
	}
	
	public static OrderDao getInstance()
	{
		if(OrderDao.instance == null)
		{
			OrderDao.instance = new OrderDao();
		}
		
		return OrderDao.instance;
	}
	
	private Order parseResultSetToOrder(ResultSet orderData) throws SQLException
	{
		int orderId = orderData.getInt(1);
		int tourId = orderData.getInt(2);
		int userId = orderData.getInt(3);
		int statusId = orderData.getInt(4);	
		float discount = orderData.getFloat(5);
		
		Order order = new Order();
		order.setId(orderId);
		order.setTour(DAOFactory.getTourDao().getById(tourId));
		order.setUser(DAOFactory.getUserDao().getById(userId));
		order.setStatusId(statusId);
		order.setDiscount(discount);
		
		return order;
	}
}
