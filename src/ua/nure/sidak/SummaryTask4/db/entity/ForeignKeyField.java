package ua.nure.sidak.SummaryTask4.db.entity;

/**
 * Represent instances from next database tables as 'user_roles', 'tour_types', 'tour_lodging_types', 'user_tour_order_statuses' 
 * @author eXce1z0r
 *
 */
public class ForeignKeyField 
{
	private int id;
	private String name;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
