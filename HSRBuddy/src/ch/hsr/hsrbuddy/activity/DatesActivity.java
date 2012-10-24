package ch.hsr.hsrbuddy.activity;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.hsr.hsrbuddy.R;

public class DatesActivity extends Activity {

	private ArrayList<String> semesterDateURLs = new ArrayList<String>();
	private ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
	final Handler datesHandler = new Handler();
	private LinearLayout layout;
	private Context that = this;
	private ProgressDialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dates);
		layout = (LinearLayout) findViewById(R.id.dates);

		showLoadingDialog();
		new Thread(parseValues, "parseValuesThread").start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_dates, menu);
		return true;
	}

	private final Runnable parseValues = new Runnable() {
		ArrayList<String> oneLine;

		public void run() {
			getURLsToParse();
			Document doc;
			try {
				doc = Jsoup.connect(semesterDateURLs.get(0)).get();
				Elements rows = doc.select("table tr");
				for (int i = 1; i < rows.size(); i++) {
					Elements cells = rows.get(i).select("td");
					oneLine = new ArrayList<String>();
					if (cells.get(0).select("b").isEmpty()
							&& cells.get(0).select("[style*=bold]").isEmpty()) {
						if (cells.get(0).text().length() > 0) {
							oneLine.add(cells.get(0).text());
							oneLine.add(cells.get(1).text());
						} else {
							oneLine.add(" ");
							oneLine.add(" ");
						}
					} else {
						oneLine.add("<b>" + cells.get(0).text() + "<b>");
						oneLine.add(cells.get(1).text());
					}
					lines.add(oneLine);
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
			LinearLayout line;
			LinearLayout leftLayout;
			LinearLayout rightLayout;

			for (int i = 0; i < lines.size(); i++) {
				line = new LinearLayout(that);
				line.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

				leftLayout = new LinearLayout(that);
				leftLayout.setLayoutParams(new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
				leftLayout.setGravity(Gravity.LEFT);

				rightLayout = new LinearLayout(that);
				leftLayout.setLayoutParams(new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT, 2));
				rightLayout.setGravity(Gravity.RIGHT);

				line.addView(leftLayout, 0);
				line.addView(rightLayout, 1);

				informationView = new TextView(that);
				informationView.setText(Html.fromHtml(lines.get(i).get(0)));
				leftLayout.addView(informationView);

				informationView = new TextView(that);
				informationView.setText(Html.fromHtml(lines.get(i).get(1)));
				rightLayout.addView(informationView);

				layout.addView(line);
			}
			mDialog.dismiss();
		}
	};

	private void getURLsToParse() {
		Document doc;
		try {
			doc = Jsoup.connect("http://www.hsr.ch/Aktuelles.93.0.html?&L=0").get();
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

	private void showLoadingDialog() {
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("Loading...");
		mDialog.setCancelable(false);
		mDialog.show();
	}
}
