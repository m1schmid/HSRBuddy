package ch.hsr.hsrbuddy.activity.map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import ch.hsr.hsrbuddy.R;
import ch.hsr.hsrbuddy.view.TouchImageView;

public class MapActivityBuildingOne extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TouchImageView img = new TouchImageView(this);
        Bitmap snoop = BitmapFactory.decodeResource(getResources(), R.drawable.detail_building_1);
        img.setImageBitmap(snoop);
        img.setMaxZoom(4f);
        setContentView(img);
    }

}
