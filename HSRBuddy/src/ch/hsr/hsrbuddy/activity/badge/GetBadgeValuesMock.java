package ch.hsr.hsrbuddy.activity.badge;

import java.util.Date;

import android.util.Log;
import ch.hsr.hsrbuddy.activity.BadgeActivity;

public class GetBadgeValuesMock extends Thread {
	
	private BadgeActivity badgeActivity;

	public GetBadgeValuesMock(BadgeActivity badgeActivity) {
		this.badgeActivity = badgeActivity;
}
	public void run() {
		Log.d("GetBadgeValuesMock", "The Thread GetBadgeValuesMock has been started.");
					
		try {
			//Simulate loading time
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		
		badgeActivity.getBadgeValues().setLatestBalance(17.70);
			
		ExpenseItem expenseItem; 
		for (int i = 0; i < 5; i++) {
			expenseItem = new ExpenseItem();
			expenseItem.setDate(new Date());
			expenseItem.setLocation("Mensa"); 
			expenseItem.setAmount(8.60);
			badgeActivity.getBadgeValues().getLastPurchases().add(0, expenseItem);
		}
		
		badgeActivity.getBadgeValues().setLastUpdatedBalance(new Date());
		badgeActivity.getBadgeValues().setMensaTotal(780.60);
		badgeActivity.getBadgeValues().setPrinterTotal(277.40);
		badgeActivity.getBadgeValues().setTotal(1027.20);
		badgeActivity.getBadgeValues().setLastUpdatedWholeBalance(new Date ());
		
		Log.d("GetBadgeValuesMock", "The variables have been set.");
		
		/*
		 * Please read the whole explanation at the updateUILater() method.
		 */
		badgeActivity.updateUILater();
		
		Log.d("GetBadgeValuesMock", "The Thread GetBadgeValuesMock ended.");

	}
}