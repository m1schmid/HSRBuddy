package ch.hsr.hsrbuddy.service;

import ch.hsr.hsrbuddy.activity.badge.GetBadgeValues;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BadgeService extends IntentService {
	
	public static final String PREFS_NAME = "HSRBuddyPreferences";
	private String username;
	private String password;

	public BadgeService() {
		super("BadgeService");
		Log.d("BadgeService", "The BadgeService Thread has been created");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);	
		username = prefs.getString("Username", "NOT_FOUND");
		password = prefs.getString("Password", "NOT_FOUND");
		
		if(username.equals("NOT_FOUND") || password.equals("NOT_FOUND")){
			//do nothing, user must find out that he must set password on his own
		} else {
			new GetBadgeValues(username, password, prefs).start();
		}

		Log.d("BadgeService", "The BadgeService Thread started the GetBadgeValues Thread!");
	}

	@Override
	public void onDestroy() {
		Log.d("BadgeService", "The BadgeService Thread has stopped");
		super.onDestroy();
	}

}
