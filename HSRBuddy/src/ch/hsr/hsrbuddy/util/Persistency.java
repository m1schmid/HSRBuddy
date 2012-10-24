package ch.hsr.hsrbuddy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;

public class Persistency {
	
	public static Object readFile(String filePath) {
		Object obj = null;
		try {
			File file = new File(filePath);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream is = new ObjectInputStream(fis);
				obj = is.readObject();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static void writeFile(Object obj, String filePath) {
		try {
			File file = new File(filePath);
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(obj);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isOlder(String filePath, int days) {
		Date fileDate = getLastMadified(filePath);
		if(fileDate == null) return true;
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -days);
		return fileDate.before(c.getTime());
	}
	
	public static boolean wasModifiedThisWeek(String filePath){
		Calendar c = Calendar.getInstance();
		
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date monday = c.getTime();
		
		c.add(Calendar.DATE, 5);
		Date friday = c.getTime();
		
		return isModifiedBetween(filePath, monday, friday);
	}
	
	public static boolean isModifiedBetween(String filePath, Date start, Date end){
		Date fileDate = getLastMadified(filePath);
		return !(fileDate == null) && fileDate.after(start) && fileDate.before(end);
	}
	
	private static Date getLastMadified(String filePath){
		File file = new File(filePath);
		if(!file.exists()) return null;
		return new Date(file.lastModified());
	}
}
