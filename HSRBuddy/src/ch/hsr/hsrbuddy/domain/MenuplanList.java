package ch.hsr.hsrbuddy.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuplanList implements Serializable {

	private static final long serialVersionUID = 2584672185955587574L;

	private List<Menuplan> menuplanList = new ArrayList<Menuplan>();
	
	public void add(Menuplan menuplan){
		menuplanList.add(menuplan);
	}
	
	public Menuplan get(int i){
		return menuplanList.get(i);
	}
	
	public int size(){
		return menuplanList.size();
	}
}
