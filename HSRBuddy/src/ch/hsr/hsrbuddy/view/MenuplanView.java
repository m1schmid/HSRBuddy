package ch.hsr.hsrbuddy.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import ch.hsr.hsrbuddy.domain.Menuplan;
import ch.hsr.hsrbuddy.domain.MenuplanItem;

public class MenuplanView extends ScrollView {

	public MenuplanView(Context context) {
		super(context);
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		display.getMetrics(metrics);
		setLayoutParams(new LayoutParams(metrics.widthPixels, LayoutParams.MATCH_PARENT));
	}
	
	public void setMenuplan(Menuplan menuplan){
		addView(createMenuplanLayout(menuplan));
	}

	private LinearLayout createMenuplanLayout(Menuplan menuplan) {
		LinearLayout menuplanLayout = new LinearLayout(getContext());
		menuplanLayout.setPadding(5, 10, 5, 10);
		menuplanLayout.setOrientation(LinearLayout.VERTICAL);
		menuplanLayout.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		ArrayList<View> menuplanItems = createMenuplanItemsView(menuplan,
				menuplanLayout);

		for (int i = 0; i < menuplanItems.size(); i++) {
			menuplanLayout.addView(menuplanItems.get(i), i);
		}

		return menuplanLayout;
	}

	private ArrayList<View> createMenuplanItemsView(Menuplan menuplan,
			LinearLayout menuplanLayout) {

		ArrayList<View> menuplanItems = new ArrayList<View>();
		menuplanItems.add(createTextView(menuplan.getDate(), Typeface.BOLD));
		menuplanItems.add(createHorizontalLine(2));
		for (int i = 0; i <= menuplan.getCount(); i++) {
			MenuplanItem menu = menuplan.get(i);
			menuplanItems.add(createTextView(menu.getTitle(), Typeface.BOLD));
			menuplanItems.add(createTextView(menu.getDescription(),
					Typeface.NORMAL));
			menuplanItems.add(createTextView(menu.getPrice(), Typeface.NORMAL));
			menuplanItems.add(createHorizontalLine(1));
		}
		return menuplanItems;
	}

	private View createHorizontalLine(int lineHeight) {
		View line = new View(getContext());
		line.setBackgroundColor(0xDDDDDDDD);
		line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				lineHeight));
		return line;
	}

	private View createTextView(String text, int typeface) {
		TextView textView = new TextView(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		textView.setLayoutParams(params);
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setTypeface(null, typeface);
		textView.setText(text);
		return textView;
	}
}
