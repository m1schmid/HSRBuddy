package ch.hsr.hsrbuddy.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ExampleService extends IntentService {

	//TODO: Remove in final code
	private boolean running = true;

	public ExampleService() {
		super("ExampleService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		while (running) {
			Log.i("Service", "sleeping");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDestroy() {
		Log.i("Andreas", "Haaalt STOP!");
		running = false;
		super.onDestroy();
	}

}
