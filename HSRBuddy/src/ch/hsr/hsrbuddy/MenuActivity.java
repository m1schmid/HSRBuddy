package ch.hsr.hsrbuddy;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.menu);

		HorizontalSwipeLayout horizontalSwipeLayout = new HorizontalSwipeLayout(layout.getContext());
		
		ArrayList<View> weekdayMenus = new ArrayList<View>();
		for (int i = 1; i <= WEEKDAYS; i++) {
			String weekdayUrl = MENU_URL + i;
			try {
				Document menuSite = Jsoup.connect(weekdayUrl).get();
				String date = menuSite.select("div.date").text();
				Elements htmlDishes = menuSite.select("div.offer");
				View weekdayView = createWeekdayMenuView(date, getWeekdayDishes(htmlDishes), horizontalSwipeLayout.getContext());
				weekdayMenus.add(weekdayView);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		horizontalSwipeLayout.setFeatureItems(weekdayMenus);

		layout.addView(horizontalSwipeLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}

	private ArrayList<Dish> getWeekdayDishes(Elements htmlDishes) {
		ArrayList<Dish> dishes = new ArrayList<Dish>();
		
		//i=1 cause leave out the take-away offer
		for (int i=1; i<htmlDishes.size(); i++) {
			Element htmlDish = htmlDishes.get(i);
			String title = htmlDish.select("div.offer-description").text();
			String description = htmlDish.select("div.menu-description").text();
			String price = htmlDish.select("div.price").text();
			dishes.add(new Dish(title, description, price));
		}

		return dishes;
	}

	private View createWeekdayMenuView(String date, ArrayList<Dish> dishes, Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		
		LinearLayout singleWeekDayLayout = new LinearLayout(context);
		singleWeekDayLayout.setPadding(5, 10, 5, 10);
		singleWeekDayLayout.setOrientation(LinearLayout.VERTICAL);
		singleWeekDayLayout.setLayoutParams(new LayoutParams(metrics.widthPixels, metrics.heightPixels));
		
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		int index = 0;
		addTextViewToLayout(date, singleWeekDayLayout, layoutParams, index++);
		View line = new View(singleWeekDayLayout.getContext());
		line.setBackgroundColor(Color.BLACK);
		line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		singleWeekDayLayout.addView(line, index++);
		for (int i = 0; i < dishes.size(); i++) {
			Dish dish = dishes.get(i);
			addTextViewToLayout(dish.getTitle(), singleWeekDayLayout, layoutParams, index++);
			addTextViewToLayout(dish.getDescription(), singleWeekDayLayout, layoutParams, index++);
			addTextViewToLayout(dish.getPrice(), singleWeekDayLayout, layoutParams, index++);
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

	class Dish {
		private String title;
		private String description;
		private String price;

		public Dish(String title, String description, String price) {
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
