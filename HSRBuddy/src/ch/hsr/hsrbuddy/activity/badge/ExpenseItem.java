package ch.hsr.hsrbuddy.activity.badge;

import java.util.Date;

public class ExpenseItem{
	private Date date;
	private String location;
	private Double amount;
	
	public ExpenseItem() {
		date = new Date();
		location = "";
		amount = 0.0;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
