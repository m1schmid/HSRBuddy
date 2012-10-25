package ch.hsr.hsrbuddy.activity.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import ch.hsr.hsrbuddy.R;
import ch.hsr.hsrbuddy.view.ImageMap;
import ch.hsr.hsrbuddy.view.ImageMap.OnImageMapClickedHandler;

public class MapActivity extends Activity {
	ImageMap mImageMap;
	Drawable hsrmap;
	
	OnImageMapClickedHandler handler = new ImageMap.OnImageMapClickedHandler() {
		public void onImageMapClicked(int id) { changeMap(id); }
		public void onBubbleClicked(int id) { }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		mImageMap = (ImageMap) findViewById(R.id.map);
		mImageMap.addOnImageMapClickedHandler(handler);
	}
	
	private void changeMap(int id){
		if(id == R.id.building1){
	        startActivity(new Intent(this, MapActivityBuildingOne.class));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}
}
