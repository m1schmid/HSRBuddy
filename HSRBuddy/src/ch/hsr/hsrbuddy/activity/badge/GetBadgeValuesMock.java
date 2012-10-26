package ch.hsr.hsrbuddy.activity.badge;

import java.util.Date;

import ch.hsr.hsrbuddy.activity.BadgeActivity;

public class GetBadgeValuesMock extends Thread {
	
	private BadgeActivity badgeActivity;

	public GetBadgeValuesMock(BadgeActivity badgeActivity) {
		this.badgeActivity = badgeActivity;
}
	public void run() {
		System.out.println("The Thread GetBadgeValuesMock has been started.");
					
		//TODO: get real balance from server
		try {
			//Simulate loading time
			Thread.sleep(1500);
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
			badgeActivity.getBadgeValues().getLastPurchases().add(expenseItem);
		}
		
		badgeActivity.getBadgeValues().setLastUpdatedBalance(new Date());
		badgeActivity.getBadgeValues().setMensaTotal(780.60);
		badgeActivity.getBadgeValues().setPrinterTotal(277.40);
		badgeActivity.getBadgeValues().setTotal(1027.20);
		badgeActivity.getBadgeValues().setLastUpdatedWholeBalance(new Date ());
		
		System.out.println("The variables have been set.");
		
		/*
		 * The badgeHandler represents the UI-Msg-Queue to the BadgeActivityInstance.
		 * With post you can add threads to the handler which will then be executed
		 * from the same thread in which the handler is instanced. This exact functionality
		 * is needed to update your UI, because you can only update your UI with threads
		 * started by this UI-instance. 
		 */
        //badgeHandler.post(updateUI);
		badgeActivity.updateUI();
		System.out.println("The Thread GetBadgeValuesMock ended.");

	}
}