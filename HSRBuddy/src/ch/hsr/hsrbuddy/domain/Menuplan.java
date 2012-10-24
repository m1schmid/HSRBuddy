package ch.hsr.hsrbuddy.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public 	class Menuplan implements Serializable {
	
	private static final long serialVersionUID = -894564355751702594L;
	
	private String date;
	private List<MenuplanItem> menuplan = new ArrayList<MenuplanItem>();
	
	public Menuplan(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}
	
	public void add(MenuplanItem menu){
		menuplan.add(menu);
	}
	
	public MenuplanItem get(int i){
		return menuplan.get(i);
	}
	
	public int getCount(){
		return menuplan.size() - 1;
	}
}