package ch.hsr.hsrbuddy.activity.badge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class GetBadgeValues extends Thread {

	private String username;
	private String password;
	private boolean hasFailed;
	public static final String PREFS_NAME = "HSRBuddyPreferences";
	SharedPreferences prefs;

	public GetBadgeValues(String username, String password, SharedPreferences prefs) {
		this.username = username;
		this.password = password;
		this.prefs = prefs;
	}

	public void run() {
		Log.d("getBadgeValues", "The Thread getBadgeValues has been started.");

		HttpResponse httpResponse = makeHttpsRequest();
		
		if(!hasFailed){
			String response = processHttpResponse(httpResponse);
			BadgeValues rcvdBadgeValues = extractJSON(response);
			
			Editor editor = prefs.edit();
			editor.putFloat("MainBalance", (float)rcvdBadgeValues.getLatestBalance());
			editor.commit();
			
			Log.d("getBadgeValues", "The variables have been set.");
		}

		Log.d("getBadgeValues", "The Thread getBadgeValues ended.");
	}

	private HttpResponse makeHttpsRequest() {
		DefaultHttpClient httpClient = getNewHttpClient();
		username = "hsr\\" + username;
		Log.d("getBadgeValues", "Username and PW for login was: " + username + " " + password);

		httpClient.getCredentialsProvider().setCredentials(
				new AuthScope(null, -1),
				new UsernamePasswordCredentials(username, password));

		final String url = "https://verrechnungsportal.hsr.ch:4450/VerrechnungsportalService.svc/JSON/getBadgeSaldo";

		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			Log.d("getBadgeValues", "URL unreachable");
			e.printStackTrace();
			hasFailed = true;
		} catch (IOException e) {
			Log.d("getBadgeValues", "URL unreachable");
			e.printStackTrace();
			hasFailed = true;
		}
		return response;
	}

	/*
	 * Returns a DefaultHttpClient with MySSLSocketFactory which accepts every
	 * certificate. This would have been an alternative -->
	 * http://android-developers.blogspot.ch/2011/09/androids-http-clients.html
	 */
	private DefaultHttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			Log.d("getBadgeValues", "Exception, returned DefaultHttpClient()");
			return new DefaultHttpClient();
		}
	}

	private String processHttpResponse(HttpResponse response) {
		String result = "";
		StringBuilder sB = new StringBuilder();
		StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() == 200) {

			HttpEntity entity = response.getEntity();
			InputStream content;
			BufferedReader reader;

			try {
				content = entity.getContent();
				reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					sB.append(line);
				}
				result = sB.toString();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			Log.d("getBadgeValues", "EXCEPTION!!! " + statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
			if (statusLine.getStatusCode() == 401) {
				Log.d("getBadgeValues", "Username or Password wrong!");
			}
		}
		return result;
	}

	private BadgeValues extractJSON(String response) {
		BadgeValues badgeValues = new BadgeValues();
		try {
			JSONObject jsonObj = new JSONObject(response);
			double balance = Double
					.parseDouble(jsonObj.getString("badgeSaldo"));
			badgeValues.setLatestBalance(balance);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return badgeValues;
	}
}