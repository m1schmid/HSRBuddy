package ch.hsr.hsrbuddy;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ch.hsr.hsrbuddy.view.HorizontalSwipeLayout;

public class MenuActivity extends Activity {

	private final String MENU_URL = "http://hochschule-rapperswil.sv-group.ch/de/menuplan.html?addGP%5Bweekday%5D=";
	private final int WEEKDAYS = 5;
	//TODO new name
	final Handler menuHandler = new Handler();
	private ProgressDialog mDialog;
	private ArrayList<Menuplan> dailyMenus = new ArrayList<Menuplan>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

		new Thread(parseValues, "parseValuesThread").start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}

	private Menuplan getWeekdayMenuplan(String date, Elements htmlMenus) {
		Menuplan menuplan = new Menuplan(date);
		
		//i=1 cause leave out the take-away offer
		for (int i=1; i<htmlMenus.size(); i++) {
			Element htmlDish = htmlMenus.get(i);
			String title = htmlDish.select("div.offer-description").text();
			String description = htmlDish.select("div.menu-description").text();
			String price = htmlDish.select("div.price").text();
			menuplan.add(new SingleMenu(title, description, price));
		}

		return menuplan;
	}

	private View createWeekdayMenuView(Menuplan menuplan, Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		
		LinearLayout singleWeekDayLayout = new LinearLayout(context);
		singleWeekDayLayout.setPadding(5, 10, 5, 10);
		singleWeekDayLayout.setOrientation(LinearLayout.VERTICAL);
		singleWeekDayLayout.setLayoutParams(new LayoutParams(metrics.widthPixels, metrics.heightPixels));
		
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		int index = 0;
		addTextViewToLayout(menuplan.getDate(), singleWeekDayLayout, layoutParams, index++);
		View line = new View(singleWeekDayLayout.getContext());
		line.setBackgroundColor(Color.BLACK);
		line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		singleWeekDayLayout.addView(line, index++);
		for (int i = 0; i <= menuplan.getCount(); i++) {
			SingleMenu menu = menuplan.get(i);
			addTextViewToLayout(menu.getTitle(), singleWeekDayLayout, layoutParams, index++);
			addTextViewToLayout(menu.getDescription(), singleWeekDayLayout, layoutParams, index++);
			addTextViewToLayout(menu.getPrice(), singleWeekDayLayout, layoutParams, index++);
			line = new View(singleWeekDayLayout.getContext());
			line.setBackgroundColor(Color.BLACK);
			line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
			singleWeekDayLayout.addView(line, index++);
		}
		
		
		return singleWeekDayLayout;
	}

	private void addTextViewToLayout(String text, LinearLayout layout,
			LayoutParams layoutParams, int index) {
		TextView textView = new TextView(layout.getContext());
		textView.setLayoutParams(layoutParams);
		textView.setText(text);
		layout.addView(textView, index);
	}
	private final Runnable parseValues = new Runnable() {
		public void run() {
			for (int i = 1; i <= WEEKDAYS; i++) {
				String weekdayUrl = MENU_URL + i;
				try {
					Document menuSite = Jsoup.connect(weekdayUrl).get();
					String date = menuSite.select("div.date").text();
					Elements htmlDishes = menuSite.select("div.offer");
					dailyMenus.add(getWeekdayMenuplan(date, htmlDishes));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			menuHandler.post(updateUI);
		}
	};

	final Runnable updateUI = new Runnable() {
		public void run() {

			RelativeLayout layout = (RelativeLayout) findViewById(R.id.menu);

			HorizontalSwipeLayout horizontalSwipeLayout = new HorizontalSwipeLayout(layout.getContext());
			
			ArrayList<View> weekdayMenus = new ArrayList<View>();
			for (int i = 0; i < dailyMenus.size(); i++) {
				View weekdayView = createWeekdayMenuView(dailyMenus.get(i), horizontalSwipeLayout.getContext());
				weekdayMenus.add(weekdayView);
			}
			horizontalSwipeLayout.setFeatureItems(weekdayMenus);

			layout.addView(horizontalSwipeLayout);
			mDialog.dismiss();
		}
	};
	
	class Menuplan {
		private String date;
		private ArrayList<SingleMenu> menuplan = new ArrayList<SingleMenu>();
		
		public Menuplan(String date) {
			this.date = date;
		}

		public String getDate() {
			return date;
		}
		
		public void add(SingleMenu menu){
			menuplan.add(menu);
		}
		
		public SingleMenu get(int i){
			return menuplan.get(i);
		}
		
		public int getCount(){
			return menuplan.size() - 1;
		}
	}
	
	class SingleMenu {
		private String title;
		private String description;
		private String price;

		public SingleMenu(String title, String description, String price) {
			this.title = title;
			this.description = description;
			this.price = price;
		}

		public String getTitle() {
			return title;
		}

		public String getDescription() {
			return description;
		}

		public String getPrice() {
			return price;
		}

	}

}
