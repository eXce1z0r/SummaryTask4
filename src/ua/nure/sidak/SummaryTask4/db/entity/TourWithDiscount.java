package ua.nure.sidak.SummaryTask4.db.entity;

/**
 * Same as "Tour" class, but contains extra field which absent at 'tours' and calculated base on 'tours' and 'user_tour_orders' tables
 * @author eXce1z0r
 *
 */
public class TourWithDiscount extends Tour
{
	private float discount;

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}
}
