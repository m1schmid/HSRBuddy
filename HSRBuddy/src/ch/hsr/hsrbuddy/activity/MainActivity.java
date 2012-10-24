package ch.hsr.hsrbuddy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import ch.hsr.hsrbuddy.R;

public class MainActivity extends Activity {

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
    
    /** Called when the user clicks the Send button 
     * @throws Exception */
    public void open(View view) throws Exception {
    	switch (view.getId())
    	{
    		case R.id.activity_menu:
    	        startActivity(new Intent(this, MenuActivity.class));
    	        break;
    		case R.id.activity_prices:
    	        startActivity(new Intent(this, PriceActivity.class));
    	        break;
    		case R.id.activity_map:
    	        startActivity(new Intent(this, MapActivity.class));
    	        break;
    		case R.id.activity_badge:
    	        startActivity(new Intent(this, BadgeActivity.class));
    	        break;
    		case R.id.activity_info:
    	        startActivity(new Intent(this, InfoActivity.class));
    	        break;
    		case R.id.activity_dates:
    	        startActivity(new Intent(this, DatesActivity.class));
    	        break;
    	default:
    		throw new Exception("Error. Activity not found");
    	}
    }
}
