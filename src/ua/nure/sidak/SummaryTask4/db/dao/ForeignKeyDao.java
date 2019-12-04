package ua.nure.sidak.SummaryTask4.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ua.nure.sidak.SummaryTask4.db.DBManager;
import ua.nure.sidak.SummaryTask4.db.constants.SQLTemplates;
import ua.nure.sidak.SummaryTask4.db.entity.ForeignKeyField;

/**
 * Contains all request to database for foreign keys tables such as (user_roles, tour_types, tour_lodging_types, user_tour_order_statuses)
 * @author eXce1z0r
 *
 */
public abstract class ForeignKeyDao 
{
	public List<ForeignKeyField> getForeingKeyField(String tableName, Locale locale)
	{
		List<ForeignKeyField> tourForeignKeyFields = new ArrayList<>();
		
		String sqlRequest = SQLTemplates.SELECT_REQUEST_TEMPLATE.getValue();
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_NAME.getValue(), tableName);
		
		if(locale != null && "ru".equals(locale.getLanguage()))
		{
			sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "id, name_ru");	
		}
		else
		{
			sqlRequest = sqlRequest.replaceFirst(SQLTemplates.TABLE_COLUMNS.getValue(), "id, name_en");	
		}
		
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.WHERE_CASE.getValue(), "");
		sqlRequest = sqlRequest.replaceFirst(SQLTemplates.SELECT_ADDITIONAL_INFO.getValue(), "");
		
		try(Connection conn = DBManager.getInstance().getConnection())
		{
			try(PreparedStatement prepStmt = conn.prepareStatement(sqlRequest))
			{
				ResultSet foreignKeyFields = prepStmt.executeQuery();
				while(foreignKeyFields.next())
				{
					ForeignKeyField tFKF = new ForeignKeyField();
					tFKF.setId(foreignKeyFields.getInt(1));
					tFKF.setName(foreignKeyFields.getString(2));
					tourForeignKeyFields.add(tFKF);
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return tourForeignKeyFields;
	}
}
