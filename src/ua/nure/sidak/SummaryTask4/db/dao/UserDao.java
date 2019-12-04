package ua.nure.sidak.SummaryTask4.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ua.nure.sidak.SummaryTask4.db.DBManager;
import ua.nure.sidak.SummaryTask4.db.constants.SQLTemplates;
import ua.nure.sidak.SummaryTask4.db.entity.User;

/**
 * Stores all database requests for interacting with "users"
 * @author eXce1z0r
 *
 */
public class UserDao extends ForeignKeyDao
{
	private static UserDao instance;
	
	public static final String TABLE_NAME = "users";
	
	public static final String STATUS_TABLE_NAME = "user_roles";
	
	private UserDao()
	{
		
	}
	
	public int create(User user) 
	{		
		String sqlRequest = SQLTemplates.INSERT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), UserDao.TABLE_NAME);		
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "role_id, login, mail, password, status");		
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.INSERT_REQUEST_VALUES.getValue(), "?,?,?,?,?");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest, Statement.RETURN_GENERATED_KEYS))
			{
				prepStmt.setInt(1, user.getRoleId());
				prepStmt.setString(2, user.getLogin());
				prepStmt.setString(3, user.getMail());
				prepStmt.setString(4, user.getPassword());
				prepStmt.setBoolean(5, user.isStatus());
				
				prepStmt.executeUpdate();
				
				ResultSet userKeyData = prepStmt.getGeneratedKeys();
				while(userKeyData != null && userKeyData.next())
				{
					user.setId(userKeyData.getInt(1));
					return user.getId();
				}
			}
		}
		catch(SQLIntegrityConstraintViolationException e)
		{
			e.printStackTrace();
			int exceptionCodeToReturn = -1;
			if(e.getMessage().contains("login"))
			{
				exceptionCodeToReturn = -2;
			}
			else
			{
				exceptionCodeToReturn = -3;
			}
			return exceptionCodeToReturn;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		
		return -1;
	}

	public User getById(int id) 
	{
		User foundUser = null;
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), UserDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "*");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "WHERE id=?");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{		
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				prepStmt.setInt(1, id);
				
				ResultSet userData = prepStmt.executeQuery();
								
				while(userData.next())
				{
					foundUser = this.parseResultSetToUser(userData);
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return foundUser;
	}
	
	public User getUserByLogin(String login)
	{
		User foundUser = null;
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), UserDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "*");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "WHERE login=? OR mail=?");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{		
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				prepStmt.setString(1, login);
				prepStmt.setString(2, login);
				
				ResultSet userData = prepStmt.executeQuery();
				
				while(userData.next())
				{
					foundUser = this.parseResultSetToUser(userData);
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return foundUser;
	}
	
	public List<User> getAll() 
	{
		List<User> foundUsers = new ArrayList<>();
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), UserDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "*");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				ResultSet usersData = prepStmt.executeQuery();
				
				while(usersData.next())
				{
					foundUsers.add(this.parseResultSetToUser(usersData));
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		
		return foundUsers;
	}

	public boolean removeById(int id) 
	{
		String sqlRequest = SQLTemplates.DELETE_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), UserDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "WHERE id=?");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
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

	public int update(User user) 
	{
		String sqlRequest = SQLTemplates.UPDATE_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), UserDao.TABLE_NAME);
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.UPDATE_SET_EXPRESSION.getValue(), "role_id=?, login=?, "
																					+ "mail=?, password=?, status=?");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "WHERE id=?");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest, Statement.RETURN_GENERATED_KEYS))
			{
				prepStmt.setInt(1, user.getRoleId());
				prepStmt.setString(2, user.getLogin());
				prepStmt.setString(3, user.getMail());
				prepStmt.setString(4, user.getPassword());
				prepStmt.setBoolean(5, user.isStatus());
				prepStmt.setInt(6, user.getId());
				
				prepStmt.executeUpdate();
				
				ResultSet updatedUserId = prepStmt.getGeneratedKeys();
				while(updatedUserId.next())
				{
					user.setId(updatedUserId.getInt(1));
					return user.getId();
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
				
		return -1;
	}
	
	public static UserDao getInstance()
	{
		if(UserDao.instance == null)
		{
			UserDao.instance = new UserDao();
		}
		return UserDao.instance;
	}

	private User parseResultSetToUser(ResultSet userData) throws SQLException
	{
		User parsedUser = new User();
		
		parsedUser.setId(userData.getInt("id"));
		parsedUser.setRoleId(userData.getInt("role_id"));
		parsedUser.setLogin(userData.getString("login"));
		parsedUser.setMail(userData.getString("mail"));
		parsedUser.setPassword(userData.getString("password"));
		parsedUser.setStatus(userData.getBoolean("status"));
		
		return parsedUser;
	}
}
