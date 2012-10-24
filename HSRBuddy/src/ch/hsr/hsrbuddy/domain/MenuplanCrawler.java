package ch.hsr.hsrbuddy.domain;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class MenuplanCrawler {
	
	private final static String MENUPLAN_BASE_URL = "http://hochschule-rapperswil.sv-group.ch/de/menuplan.html?addGP%5Bweekday%5D=";
	private final static int WEEKDAYS = 5;

	private final static String HTML_ELEMENT_MENUPLAN_DATE = "div.date";
	private final static String HTML_ELEMENT_MENU = "div.offer";
	private final static String HTML_ELEMENT_MENU_PRICE = "div.price";
	private final static String HTML_ELEMENT_MENU_DESCRIPTION = "div.menu-description";
	private final static String HTML_ELEMENT_MENU_TITLE = "div.offer-description";
	
	public static MenuplanList getMenuplans() {
		MenuplanList menuplans = new MenuplanList();
		for (int i = 1; i <= WEEKDAYS; i++) {
			String dailyMenuplanUrl = MENUPLAN_BASE_URL + i;
			try {
				Document menuplanOnWebsite = Jsoup.connect(dailyMenuplanUrl).get();
				String date = menuplanOnWebsite.select(HTML_ELEMENT_MENUPLAN_DATE).text();
				Elements menusOnWebsite = menuplanOnWebsite.select(HTML_ELEMENT_MENU);
				menuplans.add(crawlMenuplan(date, menusOnWebsite));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return menuplans;
	}

	private static Menuplan crawlMenuplan(String date, Elements menusOnWebsite) {
		Menuplan menuplan = new Menuplan(date);

		// i=1 cause leave out the take-away offer
		for (int i = 1; i < menusOnWebsite.size(); i++) {
			Element menuWebsite = menusOnWebsite.get(i);
			String title = menuWebsite.select(HTML_ELEMENT_MENU_TITLE).text();
			String description = menuWebsite.select(HTML_ELEMENT_MENU_DESCRIPTION).text();
			String price = menuWebsite.select(HTML_ELEMENT_MENU_PRICE).text();
			menuplan.add(new MenuplanItem(title, description, price));
		}

		return menuplan;
	}
}
