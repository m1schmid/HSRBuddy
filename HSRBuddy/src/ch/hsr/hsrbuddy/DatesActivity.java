package ch.hsr.hsrbuddy;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class DatesActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dates);
		LinearLayout layout = (LinearLayout) findViewById(R.id.dates);
		ArrayList<String> informations = new ArrayList<String>();

		Document doc;
		try {
			doc = Jsoup.connect("http://www.hsr.ch/Aktuelles.93.0.html?&L=0").get();
			Element navi = doc.getElementById("navigation");
			Elements links = navi.getElementsByTag("a");
			for (Element link : links) {
				informations.add(link.text());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TextView informationView;

		for (int i = 0; i < informations.size(); i++) {
			View line = new View(this);
			line.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			line.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			informationView = new TextView(this);
			informationView.setText(informations.get(i));
			layout.addView(informationView, i);
			layout.addView(line, i + 1);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_dates, menu);
		return true;
	}
}
