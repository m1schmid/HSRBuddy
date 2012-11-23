package ch.hsr.hsrbuddy.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import ch.hsr.hsrbuddy.R;
import ch.hsr.hsrbuddy.service.BadgeService;

public class SettingsActivity extends Activity {

	AlarmManager alarmManager;
	private Intent myIntent;
	private PendingIntent pendingIntent;
	private long serviceIntervalInMs = 5000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE); 
	}

	public void saveCredentials(View view) {
		EditText editTextUsername = ((EditText) findViewById(R.id.editTextUsername));
		String rcvdUsername = editTextUsername.getText().toString();

		EditText editTextPassword = ((EditText) findViewById(R.id.editTextPassword));
		String rcvdPassword = editTextPassword.getText().toString();

		SharedPreferences prefs = getSharedPreferences(
				BadgeActivity.PREFS_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("Username", rcvdUsername);
		editor.putString("Password", rcvdPassword);
		editor.commit();

		// TODO: is this ok? or am i starting several badge activites like that?
		// --> I allready came from a badge act.
		// TODO: relpace BadgeActivity.class with the class the user came from.
		startActivity(new Intent(this, BadgeActivity.class));
	}

	public void deleteCredentials(View view) {
		SharedPreferences prefs = getSharedPreferences(
				BadgeActivity.PREFS_NAME, 0);
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}

	public void startPollingService(View view) {
		myIntent = new Intent(SettingsActivity.this, BadgeService.class);
		pendingIntent = PendingIntent.getService(SettingsActivity.this, 0, myIntent, 0);
		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, serviceIntervalInMs, pendingIntent);
		Log.i("SettingsActivity", "Service gestartet");
	}

	public void stopPollingService(View view) {
		/*
		 * This is a workaround. If you start the service, then close the app,
		 * then try to stop the service, it won't stop the service. This
		 * is because the pendindIntent passed as arg in cancel() does no
		 * longer match the old pendindIntent which is scheduled!
		 * Short: cancel can't find this intent and does nothing.
		 * 
		 *  Quick workaround. If you want to stop the service, 
		 *  just overwrite it first and then stop it.
		 */
		startPollingService(view);
		alarmManager.cancel(pendingIntent);
		Log.i("SettingsActivity", "Service gestoppt");
	}
}
