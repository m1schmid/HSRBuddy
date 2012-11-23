package ch.hsr.hsrbuddy.activity.badge;
//TODO: Refactore this whole package, atm it is totaly wrong

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

public class getBadgeValuesMain extends Thread {

	private String username;
	private String password;
	private boolean hasFailed;
	public static final String PREFS_NAME = "HSRBuddyPreferences";
	SharedPreferences prefs;

	public getBadgeValuesMain(String username, String password, SharedPreferences prefs) {
		this.username = username;
		this.password = password;
		this.prefs = prefs;
	}

	public void run() {
		System.out.println("The Thread getBadgeValuesMain has been started.");

		HttpResponse httpResponse = makeHttpsRequest();
		
		if(!hasFailed){
			String response = processHttpResponse(httpResponse);
			BadgeValues rcvdBadgeValues = extractJSON(response);
			
			Editor editor = prefs.edit();
			editor.putFloat("MainBalance", (float)rcvdBadgeValues.getLatestBalance());
			editor.commit();
			
			System.out.println("The variables have been set.");
		}

		System.out.println("The Thread getBadgeValuesMain ended.");
	}

	private HttpResponse makeHttpsRequest() {
		DefaultHttpClient httpClient = getNewHttpClient();

		// String username = "SIFSV-80018\\ChallPUser";
		// String password = "1q$2w$3e$4r$5t";
		username = "hsr\\" + username;
		
		System.out.println("Username and PW for login was: " + username + " " + password);

		httpClient.getCredentialsProvider().setCredentials(
				new AuthScope(null, -1),
				new UsernamePasswordCredentials(username, password));

		// final String url =
		// "https://152.96.80.18/VerrechnungsportalService.svc/json/getBadgeSaldo";
		final String url = "https://verrechnungsportal.hsr.ch:4450/VerrechnungsportalService.svc/JSON/getBadgeSaldo";

		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			System.out.println("URL unreachable");
			e.printStackTrace();
			hasFailed = true;
//			badgeActivity.showURLUnreachableDialog();
		} catch (IOException e) {
			System.out.println("URL unreachable");
			e.printStackTrace();
			hasFailed = true;
//			badgeActivity.showURLUnreachableDialog();
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
			System.out.println("Exception, returned DefaultHttpClient()");
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
			// TODO: Exception Handling
			System.out.println("EXCEPTION!!!");
			System.out.println(statusLine.getStatusCode());
			System.out.println(statusLine.getReasonPhrase());
			if (statusLine.getStatusCode() == 401) {
				System.out.println("Username or Password wrong!");
				// TODO: show this in gui
//				badgeActivity.showWrongPassword();
			}
		}

		return result;
	}

	private BadgeValues extractJSON(String response) {
		BadgeValues badgeValues = new BadgeValues();

		// //Working Example Peter M JSON API
		// try{
		// JSONObject jsonObj = new JSONObject(response);
		// JSONArray jsonArray = jsonObj.getJSONArray("menus");
		// for (int i = 0; i < jsonArray.length(); i++) {
		// JSONObject subJSONObj = jsonArray.getJSONObject(i);
		// String price = subJSONObj.getString("price_external");
		// }
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }

		// Temporary JSON API HSR
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