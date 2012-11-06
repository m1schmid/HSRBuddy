package ch.hsr.hsrbuddy.activity;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter.LengthFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import ch.hsr.hsrbuddy.R;
import ch.hsr.hsrbuddy.activity.badge.BadgeValues;
import ch.hsr.hsrbuddy.activity.badge.GetBadgeValues;

public class BadgeActivity extends Activity {

	final Handler badgeHandler = new Handler();
	private ProgressDialog mDialog;
	//private Toast toast;
	private DecimalFormat dFormat = new DecimalFormat("0.00");
	private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm");
	private BadgeValues badgeValues = new BadgeValues();
	public static final int NUMBER_OF_LAST_PURCHASES = 3;
	public static final String PREFS_NAME = "HSRBuddyPreferences";
	private String username;
	private String password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_badge);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_badge, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		mDialog = new ProgressDialog(this);
		mDialog.setMessage("Loading...");
		mDialog.setCancelable(false);
		mDialog.show();

		
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);	
		username = prefs.getString("Username", "NOT_FOUND");
		password = prefs.getString("Password", "NOT_FOUND");
		if(username.equals("NOT_FOUND") || password.equals("NOT_FOUND")){
			startActivity(new Intent(this, SettingsActivity.class));
		}
		
		/*
		 * Starts a seperate thread which is absolutely detached to this
		 * UI-thread. This thread contains heavy calculation which can be done
		 * asynchronous.
		 */

		// new GetBadgeValuesMock(this).start();

		new GetBadgeValues(this, username, password).start();
	}

	/*
	 * The badgeHandler represents the UI-Msg-Queue to the
	 * BadgeActivityInstance. With post you can add threads to the handler which
	 * will then be executed from the same thread in which the handler is
	 * instanced. This exact functionality is needed to update your UI, because
	 * you can only update your UI with threads started by this UI-instance.
	 */
	public void updateUI() {
		badgeHandler.post(updateUI);
	}
	
	public void showWrongPassword() {
		badgeHandler.post(showWrongPasswordDialog);
	}
	
	final Runnable showWrongPasswordDialog = new Runnable(){
		public void run(){	
			Toast.makeText(getApplicationContext(), "Username or Password is wrong!", Toast.LENGTH_SHORT).show();
		}
	};

	// The handler will create this thread which will eventually update the UI
	// with the new values.
	final Runnable updateUI = new Runnable() {
		public void run() {

			TextView currentBalanceView = (TextView) findViewById(R.id.currentBalanceValue);
			currentBalanceView.setText(dFormat.format(badgeValues
					.getLatestBalance()) + " CHF");

			TextView expenseTitel1 = (TextView) findViewById(R.id.lastExpenseRowTitel1);
			expenseTitel1.setText(dateFormat.format(badgeValues
					.getLastPurchases().get(0).getDate()));

			TextView expenseLocationAndAmount1 = (TextView) findViewById(R.id.lastExpenseRowValue1);
			expenseLocationAndAmount1.setText(badgeValues.getLastPurchases()
					.get(0).getLocation()
					+ " "
					+ dFormat.format(badgeValues.getLastPurchases().get(0)
							.getAmount()) + " CHF");

			TextView expenseTitel2 = (TextView) findViewById(R.id.lastExpenseRowTitel2);
			expenseTitel2.setText(dateFormat.format(badgeValues
					.getLastPurchases().get(1).getDate()));

			TextView expenseLocationAndAmount2 = (TextView) findViewById(R.id.lastExpenseRowValue2);
			expenseLocationAndAmount2.setText(badgeValues.getLastPurchases()
					.get(1).getLocation()
					+ " "
					+ dFormat.format(badgeValues.getLastPurchases().get(1)
							.getAmount()) + " CHF");

			TextView expenseTitel3 = (TextView) findViewById(R.id.lastExpenseRowTitel3);
			expenseTitel3.setText(dateFormat.format(badgeValues
					.getLastPurchases().get(2).getDate()));

			TextView expenseLocationAndAmount3 = (TextView) findViewById(R.id.lastExpenseRowValue3);
			expenseLocationAndAmount3.setText(badgeValues.getLastPurchases()
					.get(2).getLocation()
					+ " "
					+ dFormat.format(badgeValues.getLastPurchases().get(2)
							.getAmount()) + " CHF");

			TextView lastUpdatedBalanceView = (TextView) findViewById(R.id.lastUpdatedBalanceLabel);
			lastUpdatedBalanceView.setText("Aktualisiert: "
					+ dateFormat.format(badgeValues.getLastUpdatedBalance()));

			TextView mensaTotalView = (TextView) findViewById(R.id.totalMensaValue);
			mensaTotalView.setText(dFormat.format(badgeValues.getMensaTotal())
					+ " CHF");

			TextView printerTotalView = (TextView) findViewById(R.id.totalPrinterValue);
			printerTotalView.setText(dFormat.format(badgeValues
					.getPrinterTotal()) + " CHF");

			TextView totalView = (TextView) findViewById(R.id.totalValue);
			totalView.setText(dFormat.format(badgeValues.getTotal()) + " CHF");

			TextView lastUpdatedWholeBalanceView = (TextView) findViewById(R.id.lastUpdatedWholeBalanceLabel);
			lastUpdatedWholeBalanceView.setText("Aktualisiert: "
					+ dateFormat.format(badgeValues
							.getLastUpdatedWholeBalance()));

			mDialog.dismiss();

			System.out.println("The values has been set in the UI.");
		}
	};

	public BadgeValues getBadgeValues() {
		return badgeValues;
	}

	public void setBadgeValues(BadgeValues badgeValues) {
		this.badgeValues = badgeValues;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}