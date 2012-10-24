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
		File file = new File(filePath);
		if(!file.exists()) return true;
		Date fileDate = new Date(file.lastModified());
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -days);
		return fileDate.before(c.getTime());
	}
}
