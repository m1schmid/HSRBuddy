package ch.hsr.hsrbuddy.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import ch.hsr.hsrbuddy.activity.SettingsActivity;
import ch.hsr.hsrbuddy.activity.badge.getBadgeValuesMain;

public class BadgeService extends IntentService {
	
	public static final String PREFS_NAME = "HSRBuddyPreferences";
	private String username;
	private String password;

	public BadgeService() {
		super("BadgeService");
		Log.i("BadgeService", "The BadgeService Thread has been created");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);	
		username = prefs.getString("Username", "NOT_FOUND");
		password = prefs.getString("Password", "NOT_FOUND");
		
		if(username.equals("NOT_FOUND") || password.equals("NOT_FOUND")){
			startActivity(new Intent(this, SettingsActivity.class));
		} else {
			new getBadgeValuesMain(username, password, prefs).start();
		}
		
		
		Log.i("BadgeService", "The BadgeService Thread started the GetBadgeValuesMain Thread!");
	}

	@Override
	public void onDestroy() {
		Log.i("BadgeService", "The BadgeService Thread has stopped");
		super.onDestroy();
	}

}
