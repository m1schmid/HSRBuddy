package ch.hsr.hsrbuddy.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import ch.hsr.hsrbuddy.R;
import ch.hsr.hsrbuddy.service.ExampleService;

public class SettingsActivity extends Activity {

    private Intent intent;

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
    
	public void saveCredentials(View view) {
		EditText editTextUsername = ((EditText) findViewById(R.id.editTextUsername));
		String rcvdUsername = editTextUsername.getText().toString();

		EditText editTextPassword = ((EditText) findViewById(R.id.editTextPassword));
		String rcvdPassword = editTextPassword.getText().toString();

		SharedPreferences prefs = getSharedPreferences(BadgeActivity.PREFS_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("Username", rcvdUsername);
		editor.putString("Password", rcvdPassword);
		editor.commit();
		
		//TODO: is this ok? or am i starting several badge activites like that? --> I allready came from a badge act.
		//TODO: relpace  BadgeActivity.class with the class the user came from.
		startActivity(new Intent(this, BadgeActivity.class));
	}
	
	public void deleteCredentials(View view){
		SharedPreferences prefs = getSharedPreferences(BadgeActivity.PREFS_NAME, 0);
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}
   
    public void startPollingService(View view){
    	intent = new Intent(this, ExampleService.class);
    	startService(intent);
    	Log.i("SettingsActivity", "Service gestartet");
    }
    
    public void stopPollingService(View view){
    	stopService(intent);
    	Log.i("SettingsActivity", "Service gestoppt");
    }
}
