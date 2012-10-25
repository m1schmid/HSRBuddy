package ch.hsr.hsrbuddy.activity.map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import ch.hsr.hsrbuddy.view.TouchImageView;

public class MapActivityDetail extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        int imageId = (Integer) b.get("imageId");
        TouchImageView img = new TouchImageView(this);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), imageId);
        img.setImageBitmap(bm);
        setContentView(img);
    }

}
