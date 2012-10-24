package ch.hsr.hsrbuddy.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
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
	// TODO new name
	private final Handler mHandler = new Handler();
	private ProgressDialog mDialog;
	private MenuplanList menuplanList = new MenuplanList();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
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
			String filePath = getFilesDir() + "/" + MENUPLAN_FILENAME;

			if (Persistency.isModifiedBetweenMonAndFri(filePath)) {
				Object obj = Persistency.readFile(filePath);
				if(obj instanceof MenuplanList)
					menuplanList = (MenuplanList) obj;
			}
			if (menuplanList.size() == 0) {
				menuplanList = MenuplanCrawler.getMenuplans();
				Persistency.writeFile(menuplanList, filePath);
			}
			mHandler.post(updateUI);
		}
	};

	final Runnable updateUI = new Runnable() {
		public void run() {

			RelativeLayout layout = (RelativeLayout) findViewById(R.id.menu);
			layout.removeAllViews();

			HorizontalSwipeLayout horizontalSwipeLayout = new HorizontalSwipeLayout(
					layout.getContext());

			ArrayList<View> menuplanViews = new ArrayList<View>();
			for (int i = 0; i < menuplanList.size(); i++) {
				MenuplanView menuplanView = new MenuplanView(horizontalSwipeLayout.getContext());
				menuplanView.setMenuplan(menuplanList.get(i));
				menuplanViews.add(menuplanView);
			}
			horizontalSwipeLayout.setViewItems(menuplanViews);

			layout.addView(horizontalSwipeLayout);
			mDialog.dismiss();
		}
	};
}