package ua.nure.sidak.SummaryTask4.db.entity;

/**
 * Represent instance from database table 'user_tour_orders'
 * @author eXce1z0r
 *
 */
public class Order 
{
	private int id;
	private Tour tour;
	private User user;
	private int statusId;
	private float discount;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Tour getTour() {
		return tour;
	}
	public void setTour(Tour tour) {
		this.tour = tour;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public float getDiscount() {
		return discount;
	}
	public void setDiscount(float discount) {
		this.discount = discount;
	}
}
