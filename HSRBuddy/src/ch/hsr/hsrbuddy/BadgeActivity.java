package ch.hsr.hsrbuddy;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BadgeActivity extends Activity {

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
}
