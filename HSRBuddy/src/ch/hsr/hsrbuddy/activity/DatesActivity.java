package ch.hsr.hsrbuddy.activity;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.hsr.hsrbuddy.R;
import ch.hsr.hsrbuddy.R.id;
import ch.hsr.hsrbuddy.R.layout;
import ch.hsr.hsrbuddy.R.menu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DatesActivity extends Activity {

	private ArrayList<String> semesterDateURLs = new ArrayList<String>();
	private ArrayList<String> dates = new ArrayList<String>();
	final Handler datesHandler = new Handler();
	private LinearLayout layout;
	private Context that = this;
	private ProgressDialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dates);
		layout = (LinearLayout) findViewById(R.id.dates);		
		
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

		new Thread(parseValues, "parseValuesThread").start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_dates, menu);
		return true;
	}

	private final Runnable parseValues = new Runnable() {
		public void run() {
			getURLsToParse();
			Document doc;
			try {
				doc = Jsoup.connect(semesterDateURLs.get(0)).get(); // TODO: add the second view
				Elements rows = doc.select("table tr");
				for (int i = 1; i < rows.size(); i++) {
					Elements cells = rows.get(i).select("td");
					if (cells.get(0).select("b").isEmpty() && cells.get(0).select("[style*=bold]").isEmpty()) {
						if (cells.get(0).text().length() > 0) { 
							dates.add(cells.get(0).text() + ": " + cells.get(1).text());
						} else {
							dates.add("-------------------");
						}
					} else {
						dates.add("<b>" + cells.get(0).text() + "<b>");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			datesHandler.post(updateUI);
		}
	};

	final Runnable updateUI = new Runnable() {
		public void run() {
			TextView informationView;

			for (int i = 0; i < dates.size() - 3; i++) {
				informationView = new TextView(that);
				informationView.setText(Html.fromHtml(dates.get(i)));
				layout.addView(informationView, i);
			}
			mDialog.dismiss();
		}
	};

	private void getURLsToParse() {
		Document doc;
		try {
			doc = Jsoup.connect("http://www.hsr.ch/Aktuelles.93.0.html?&L=0")
					.get();
			Element navi = doc.getElementById("navigation");
			Elements links = navi.getElementsByTag("a");
			for (Element link : links) {
				if (link.attr("href").startsWith("Semesterdaten")) {
					semesterDateURLs.add(link.attr("abs:href"));
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
