package ch.hsr.hsrbuddy.activity;

import ch.hsr.hsrbuddy.R;
import ch.hsr.hsrbuddy.R.layout;
import ch.hsr.hsrbuddy.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PriceActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_price, menu);
        return true;
    }
}