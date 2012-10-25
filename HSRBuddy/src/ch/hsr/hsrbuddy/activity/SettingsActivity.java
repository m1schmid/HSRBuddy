package ch.hsr.hsrbuddy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
