package ch.hsr.hsrbuddy.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import ch.hsr.hsrbuddy.R;
import ch.hsr.hsrbuddy.domain.MenuplanCrawler;
import ch.hsr.hsrbuddy.domain.MenuplanList;
import ch.hsr.hsrbuddy.util.Persistency;
import ch.hsr.hsrbuddy.view.HorizontalSwipeLayout;
import ch.hsr.hsrbuddy.view.MenuplanView;

public class MenuActivity extends Activity {

	private final String LOADING_MESSAGE = "Lade neusten Menuplan herunter...";
	private final String MENUPLAN_FILENAME = "menuplan.tmp";
	private final Handler mHandler = new Handler();
	private ProgressDialog mDialog;
	private MenuplanList menuplanList = new MenuplanList();
	private RelativeLayout rootLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		rootLayout = (RelativeLayout) findViewById(R.id.menu);
	}

	@Override
	protected void onResume() {
		super.onResume();

		mDialog = new ProgressDialog(this);
		mDialog.setMessage(LOADING_MESSAGE);
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
			initMenuplanList();
			mHandler.post(updateUI);
		}

		private void initMenuplanList() {
			String filePath = getFilesDir() + "/" + MENUPLAN_FILENAME;

			if (Persistency.wasModifiedThisWeek(filePath)) {
				Object obj = Persistency.readFile(filePath);
				if (obj instanceof MenuplanList)
					menuplanList = (MenuplanList) obj;
			}
			if (menuplanList.size() == 0) {
				menuplanList = MenuplanCrawler.getMenuplans();
				Persistency.writeFile(menuplanList, filePath);
			}
		}
	};

	final Runnable updateUI = new Runnable() {
		public void run() {
			rootLayout.removeAllViews();
			
			HorizontalSwipeLayout swipeLayout = new HorizontalSwipeLayout(rootLayout.getContext());
			swipeLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			ArrayList<View> menuplanViews = createMenuplanViews(swipeLayout.getContext());
			swipeLayout.setViewItems(menuplanViews);

			rootLayout.addView(swipeLayout);
			mDialog.dismiss();
		}

		private ArrayList<View> createMenuplanViews(Context ctx) {
			ArrayList<View> menuplanViews = new ArrayList<View>();
			for (int i = 0; i < menuplanList.size(); i++) {
				MenuplanView menuplanView = new MenuplanView(ctx);
				menuplanView.setMenuplan(menuplanList.get(i));
				menuplanViews.add(menuplanView);
			}
			return menuplanViews;
		}
	};
}