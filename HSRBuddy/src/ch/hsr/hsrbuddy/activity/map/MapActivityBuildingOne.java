package ch.hsr.hsrbuddy.activity.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import ch.hsr.hsrbuddy.R;
import ch.hsr.hsrbuddy.view.TouchImageView;

public class MapActivityBuildingOne extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_activity_building_one);
        TouchImageView zoomableImage = new TouchImageView(this);
        zoomableImage.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        zoomableImage.setImageResource(R.drawable.detail_building_1);
        addContentView(zoomableImage, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

}
