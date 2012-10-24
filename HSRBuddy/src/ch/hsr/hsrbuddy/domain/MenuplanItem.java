package ch.hsr.hsrbuddy.domain;

import java.io.Serializable;

public class MenuplanItem implements Serializable {

	private static final long serialVersionUID = 7971992559958239790L;

	private String title;
	private String description;
	private String price;

	public MenuplanItem() { }

	public MenuplanItem(String title, String description, String price) {
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