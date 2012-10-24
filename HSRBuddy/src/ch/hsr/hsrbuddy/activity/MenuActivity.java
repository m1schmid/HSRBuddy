package ch.hsr.hsrbuddy.activity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import ch.hsr.hsrbuddy.R;
import ch.hsr.hsrbuddy.domain.Menuplan;
import ch.hsr.hsrbuddy.domain.MenuplanItem;
import ch.hsr.hsrbuddy.view.HorizontalSwipeLayout;

public class MenuActivity extends Activity {

	private final String MENUPLAN_BASE_URL = "http://hochschule-rapperswil.sv-group.ch/de/menuplan.html?addGP%5Bweekday%5D=";
	private final String MENUPLAN_FILENAME = "menuplan.tmp";
	private final int WEEKDAYS = 5;
	//TODO new name
	final Handler menuHandler = new Handler();
	private ProgressDialog mDialog;
	private List<Menuplan> menuplans = new ArrayList<Menuplan>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("Lade neusten Menuplan herunter...");
        mDialog.setCancelable(true);
        mDialog.show();

		new Thread(crawlMenuplan, "crawlMenuplanWebsite").start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}
	
	private final Runnable crawlMenuplan = new Runnable() {
		public void run() {
			for (int i = 1; i <= WEEKDAYS; i++) {
				String menuplanUrl = MENUPLAN_BASE_URL + i;
				Log.i("swag", menuplanUrl);
				try {
					Document menuplanOnWebsite = Jsoup.connect(menuplanUrl).get();
					String date = menuplanOnWebsite.select("div.date").text();
					Elements menusOnWebsite = menuplanOnWebsite.select("div.offer");
					menuplans.add(crawlMenuplan(date, menusOnWebsite));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			saveMenuplan();
			
			menuHandler.post(updateUI);
		}
	};

	private void saveMenuplan() {
		try {
			FileOutputStream fos = this.openFileOutput(MENUPLAN_FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(menuplans);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Menuplan crawlMenuplan(String date, Elements menusOnWebsite) {
		Menuplan menuplan = new Menuplan(date);
		
		//i=1 cause leave out the take-away offer
		for (int i=1; i<menusOnWebsite.size(); i++) {
			Element menuWebsite = menusOnWebsite.get(i);
			String title = menuWebsite.select("div.offer-description").text();
			String description = menuWebsite.select("div.menu-description").text();
			String price = menuWebsite.select("div.price").text();
			menuplan.add(new MenuplanItem(title, description, price));
		}

		return menuplan;
	}

	final Runnable updateUI = new Runnable() {
		public void run() {

			RelativeLayout layout = (RelativeLayout) findViewById(R.id.menu);

			HorizontalSwipeLayout horizontalSwipeLayout = new HorizontalSwipeLayout(layout.getContext());
			
			ArrayList<View> menuplanViews = new ArrayList<View>();
			for (int i = 0; i < menuplans.size(); i++) {
				View menuplanView = createScrollableMenuplanView(menuplans.get(i), horizontalSwipeLayout.getContext());
				menuplanViews.add(menuplanView);
			}
			horizontalSwipeLayout.setViewItems(menuplanViews);

			layout.addView(horizontalSwipeLayout);
			mDialog.dismiss();
		}
	};

	private View createScrollableMenuplanView(Menuplan menuplan, Context context) {
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		ScrollView scrollableMenuplan = new ScrollView(context);
		scrollableMenuplan.setLayoutParams(new LayoutParams(metrics.widthPixels, metrics.heightPixels));
		scrollableMenuplan.addView(createMenuplanLayout(menuplan));
		
		return scrollableMenuplan;
	}

	private LinearLayout createMenuplanLayout(Menuplan menuplan) {
		LinearLayout menuplanLayout = new LinearLayout(this);
		menuplanLayout.setPadding(5, 10, 5, 10);
		menuplanLayout.setOrientation(LinearLayout.VERTICAL);
		menuplanLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		ArrayList<View> menuplanItems = createMenuplanItemsView(menuplan, menuplanLayout);
		
		for (int i = 0; i < menuplanItems.size(); i++) {
			menuplanLayout.addView(menuplanItems.get(i), i);
		}
		
		return menuplanLayout;
	}

	private ArrayList<View> createMenuplanItemsView(Menuplan menuplan, LinearLayout menuplanLayout) {
		
		ArrayList<View> menuplanItems = new ArrayList<View>();
		menuplanItems.add(createTextView(menuplan.getDate(), Typeface.BOLD));
		menuplanItems.add(createHorizontalLine(2));
		for (int i = 0; i <= menuplan.getCount(); i++) {
			MenuplanItem menu = menuplan.get(i);
			menuplanItems.add(createTextView(menu.getTitle(), Typeface.BOLD));
			menuplanItems.add(createTextView(menu.getDescription(), Typeface.NORMAL));
			menuplanItems.add(createTextView(menu.getPrice(), Typeface.NORMAL));
			menuplanItems.add(createHorizontalLine(1));
		}
		return menuplanItems;
	}

	private View createHorizontalLine(int lineHeight) {
		View line = new View(this);
		line.setBackgroundColor(0xDDDDDDDD);
		line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, lineHeight));
		return line;
	}

	private View createTextView(String text, int typeface) {
		TextView textView = new TextView(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		textView.setLayoutParams(params);
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setTypeface(null, typeface);
		textView.setText(text);
		return textView;
	}
}