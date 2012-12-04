package ch.hsr.hsrbuddy.activity;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import ch.hsr.hsrbuddy.R;
import ch.hsr.hsrbuddy.activity.map.MapActivity;

public class MainActivity extends Activity {

	final Handler badgeHandler = new Handler();
	private DecimalFormat dFormat = new DecimalFormat("0.00");
	public static final String PREFS_NAME = "HSRBuddyPreferences";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
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

	/**
	 * Called when the user clicks the Send button
	 * 
	 * @throws Exception
	 */
	public void open(View view) throws Exception {
		switch (view.getId()) {
		case R.id.activity_menu:
			startActivity(new Intent(this, MenuActivity.class));
			break;
		case R.id.activity_map:
			startActivity(new Intent(this, MapActivity.class));
			break;
		case R.id.activity_badge:
			startActivity(new Intent(this, BadgeActivity.class));
			break;
		case R.id.activity_dates:
			startActivity(new Intent(this, DatesActivity.class));
			break;
		default:
			throw new Exception("Error. Activity not found");
		}
	}

	@Override
		protected void onResume() {
			super.onResume();
			
			TextView homeScreenBalance = (TextView) findViewById(R.id.homeScreenBalance);
			homeScreenBalance.setText("Aktueller Kontostand: " + dFormat.format(getCurrentMainBalance()) + " CHF");
			
		}

	private double getCurrentMainBalance() {
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);	
		return prefs.getFloat("MainBalance", -1.0f);
	}
}
