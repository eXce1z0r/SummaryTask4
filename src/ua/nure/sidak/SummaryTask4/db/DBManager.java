package ua.nure.sidak.SummaryTask4.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import ua.nure.sidak.SummaryTask4.db.dao.DAOFactory;
import ua.nure.sidak.SummaryTask4.db.entity.User;

/**
 * Used for interacting with Tomcat JDBC (getting new connections) 
 * @author eXce1z0r
 *
 */
//	TODO: remade read from context
public class DBManager 
{	
	private final static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/summary_task_4_db"
													+ "?user=root"
													+ "&password="
													+ "&useUnicode=true"
													+ "&useJDBCCompliantTimezoneShift=true"
													+ "&useLegacyDatetimeCode=false"
													+ "&serverTimezone=UTC"
													+ "&characterEncoding=UTF-8";
	
	private final static String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
	
	private static DBManager instance;
	
	private static DataSource dataSource;
	
	private DBManager()
	{
		this.initDataSource();
	}
	
	public Connection getConnection() throws SQLException
	{			
		return dataSource.getConnection();
	}
	
	public static DBManager getInstance()
	{
		if(DBManager.instance == null)
		{
			DBManager.instance = new DBManager();
		}
		
		return DBManager.instance;
	}
	
	private void initDataSource()
	{		
		DBManager.dataSource = new DataSource();		
		
		PoolProperties dataSourceProperties = new PoolProperties();
		dataSourceProperties.setUrl(DB_CONNECTION_URL);
		
		dataSourceProperties.setDriverClassName(DRIVER_CLASS_NAME);
		
		dataSourceProperties.setTestOnBorrow(true);
		dataSourceProperties.setMaxActive(100);
		dataSourceProperties.setInitialSize(10);
		dataSourceProperties.setRemoveAbandoned(true);
		dataSourceProperties.setRemoveAbandonedTimeout(60);
		dataSourceProperties.setMaxIdle(50);
		dataSourceProperties.setMinIdle(10);
		dataSourceProperties.setLogAbandoned(true);
		
		dataSource.setPoolProperties(dataSourceProperties);
	}
}
