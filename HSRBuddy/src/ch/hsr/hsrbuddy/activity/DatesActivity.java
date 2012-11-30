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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.hsr.hsrbuddy.R;
import ch.hsr.hsrbuddy.util.Persistency;

public class DatesActivity extends Activity {

	private final String MAIN_URL = "http://www.hsr.ch/Aktuelles.93.0.html?&L=0";
	private final String DATES_FILENAME = "/dates.tmp";
	private final Handler DATES_HANDLER = new Handler();
	private ArrayList<String> semesterDateURLs = new ArrayList<String>();
	private ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
	private LinearLayout layout;
	private Context that = this;
	private ProgressDialog mDialog;
	private boolean manualRefresh = false;

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.dates_refresh:
			Log.i("blubb", "bla");
			manualRefresh = true;
			showLoadingDialog();
			new Thread(parseValues, "parseValuesThread").start();
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private final Runnable parseValues = new Runnable() {

		public void run() {
			String filePath = getFilesDir() + DATES_FILENAME;
			if (Persistency.isOlder(filePath, 7) || manualRefresh) {
				crawlWebsite();
				Persistency.writeFile(lines, filePath);
			} else {
				Object obj = Persistency.readFile(filePath);
				if (obj instanceof ArrayList<?>) {
					lines = (ArrayList<ArrayList<String>>) obj;
				}
			}
			DATES_HANDLER.post(updateUI);
		}

		private void crawlWebsite() {
			ArrayList<String> oneLine;
			lines.clear();
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
				e.printStackTrace();
			}
		}
	};

	final Runnable updateUI = new Runnable() {
		public void run() {
			TextView testView;
			LinearLayout line;
			LinearLayout leftLayout;
			LinearLayout rightLayout;
			
			layout.removeAllViews();

			for (int i = 0; i < lines.size(); i++) {
				line = new LinearLayout(that);
				line.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

				leftLayout = new LinearLayout(that);
				leftLayout.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				leftLayout.setGravity(Gravity.LEFT);

				rightLayout = new LinearLayout(that);
				leftLayout
						.setLayoutParams(new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT, 2));
				rightLayout.setGravity(Gravity.RIGHT);

				line.addView(leftLayout, 0);
				line.addView(rightLayout, 1);

				testView = new TextView(that);
				testView.setText(Html.fromHtml(lines.get(i).get(0)));
				leftLayout.addView(testView);

				testView = new TextView(that);
				testView.setText(Html.fromHtml(lines.get(i).get(1)));
				rightLayout.addView(testView);

				layout.addView(line);
			}
			manualRefresh = false;
			mDialog.dismiss();
		}
	};

	private void getURLsToParse() {
		Document doc;
		try {
			doc = Jsoup.connect(MAIN_URL).get();
			Element navi = doc.getElementById("navigation");
			Elements links = navi.getElementsByTag("a");
			for (Element link : links) {
				if (link.attr("href").startsWith("Semesterdaten")) {
					semesterDateURLs.add(link.attr("abs:href"));
				}
			}
		} catch (IOException e) {
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
