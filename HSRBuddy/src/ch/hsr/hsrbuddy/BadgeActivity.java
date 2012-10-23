package ch.hsr.hsrbuddy;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.TextView;

public class BadgeActivity extends Activity {
	
	final Handler badgeHandler = new Handler();
	private double latestBalance;
	//TODO: All variables for location, expense etc, make arraylist, REMOVE ALL DUMY TEXT IN ACTIVTY BADGE XML

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_badge);

		TextView textView = (TextView) findViewById(R.id.currentBalanceValue);
		textView.setText("00.00 CHF");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_badge, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		/*
		 * Starts a seperate thread which is absolutely detached to this UI-thread.
		 * This thread contains heavy calculation which can be done asynchronous. 
		 */
		new Thread(getBalance, "getBalanceThread").start();		
	}
	
	private final Runnable getBalance = new Runnable(){
		public void run(){
			System.out.println("The getBalanceThread started.");
						
			//TODO: get real balance from server
			try {
				//Simulate loading time
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			
			latestBalance = 17.77;
			System.out.println("The variable double balance value has been set.");
			
			/*
			 * The badgeHandler represents the UI-Msg-Queue to the BadgeActivityInstance.
			 * With post you can add threads to the handler which will then be executed
			 * from the same thread in which the handler is instanced. This exact functionality
			 * is needed to update your UI, because you can only update your UI with threads
			 * started by this UI-instance. 
			 */
            badgeHandler.post(updateUI);
			System.out.println("The getBalanceThread ended.");
		}
	};

    // The handler will create this thread which will eventually update the UI.
    final Runnable updateUI = new Runnable() {
        public void run() {
    		TextView textView = (TextView) findViewById(R.id.currentBalanceValue);
    		textView.setText(Double.toString(latestBalance) + " CHF");
    		System.out.println("The string balance value has been set in the UI.");
        }
    };
}
