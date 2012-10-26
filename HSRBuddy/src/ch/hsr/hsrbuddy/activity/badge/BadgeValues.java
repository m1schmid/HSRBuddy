package ch.hsr.hsrbuddy.activity.badge;

import java.util.ArrayList;
import java.util.Date;

import ch.hsr.hsrbuddy.activity.BadgeActivity;

public class BadgeValues {
	
	private double latestBalance;
	private ArrayList<ExpenseItem> lastPurchases = new ArrayList<ExpenseItem>();
	private Date lastUpdatedBalance;
	private double mensaTotal;
	private double printerTotal;
	private double total;
	private Date lastUpdatedWholeBalance;
	
	public BadgeValues() {
		latestBalance = 0.0;
		for (int i = 0; i < BadgeActivity.NUMBER_OF_LAST_PURCHASES; i++) {
			lastPurchases.add(new ExpenseItem());
		}
		lastUpdatedBalance = new Date();
		mensaTotal = 0.0;
		printerTotal = 0.0;
		total = 0.0;
		lastUpdatedWholeBalance = new Date();
	}

	public double getLatestBalance() {
		return latestBalance;
	}
	
	public void setLatestBalance(double latestBalance) {
		this.latestBalance = latestBalance;
	}
	
	public ArrayList<ExpenseItem> getLastPurchases() {
		return lastPurchases;
	}
	
	public Date getLastUpdatedBalance() {
		return lastUpdatedBalance;
	}
	
	public void setLastUpdatedBalance(Date lastUpdatedBalance) {
		this.lastUpdatedBalance = lastUpdatedBalance;
	}
	
	public double getMensaTotal() {
		return mensaTotal;
	}
	
	public void setMensaTotal(double mensaTotal) {
		this.mensaTotal = mensaTotal;
	}
	
	public double getPrinterTotal() {
		return printerTotal;
	}
	
	public void setPrinterTotal(double printerTotal) {
		this.printerTotal = printerTotal;
	}
	
	public double getTotal() {
		return total;
	}
	
	public void setTotal(double total) {
		this.total = total;
	}
	
	public Date getLastUpdatedWholeBalance() {
		return lastUpdatedWholeBalance;
	}
	
	public void setLastUpdatedWholeBalance(Date lastUpdatedWholeBalance) {
		this.lastUpdatedWholeBalance = lastUpdatedWholeBalance;
	}
}
