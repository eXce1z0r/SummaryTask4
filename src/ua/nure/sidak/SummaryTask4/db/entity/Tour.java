package ua.nure.sidak.SummaryTask4.db.entity;

import java.math.BigDecimal;

/**
 * Represent instance from database table 'tours'
 * @author eXce1z0r
 *
 */
public class Tour {
	
	public static final float LOWEST_DISCOUNT = 0;
	public static final float HIGHEST_DISCOUNT = 100;
	
	private int id;
	private String title;
	private String info;
	private int type;
	private BigDecimal price;
	private int touristsAmount;
	private int lodging;
	private float discountStep;
	private float discountLimit;
	private boolean hot;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getTouristsAmount() {
		return touristsAmount;
	}
	public void setTouristsAmount(int touristsAmount) {
		this.touristsAmount = touristsAmount;
	}
	public int getLodging() {
		return lodging;
	}
	public void setLodging(int lodging) {
		this.lodging = lodging;
	}
	public float getDiscountStep() {
		return discountStep;
	}
	public void setDiscountStep(float discountStep) {
		this.discountStep = discountStep;
	}
	public float getDiscountLimit() {
		return discountLimit;
	}
	public void setDiscountLimit(float discountLimit) {
		this.discountLimit = discountLimit;
	}
	public boolean isHot() {
		return hot;
	}
	public void setHot(boolean hot) {
		this.hot = hot;
	}
}
