package com.superphil0.runtasticapi;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class RequestHandler {
	private OkHttpClient client;
	private Session session = null;

	public RequestHandler() {
		this.client = new OkHttpClient();
		CookieManager cookieManager = new CookieManager();
		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		client.setCookieHandler(cookieManager);
	}

	public List<Activity> getActivitiesOfUser(String email, String password) {
		if (!login(email, password))
			return null;

		Request request = new Request.Builder().url(session.getSessionURL())
				.get().build();
		String response = null;
		try {
			response = run(request).body().string();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		// Create a Pattern object
		Pattern r = Pattern.compile("uid: (.*),");
		// Now create matcher object.
		Matcher m = r.matcher(response);
		if (m.find()) {
			session.setUID(m.group(1));
		} 
		else return null;
		r = Pattern.compile("var index_data = (.*);");
		m = r.matcher(response);
		JsonArray jsonArray;
		if (m.find()) {
			 jsonArray = new JsonParser().parse(m.group(1)).getAsJsonArray();
		}
		else return null;
		
		String items = "";
		for(JsonElement e : jsonArray)
		{
			items+= e.getAsJsonArray().get(0);
			items+=",";
		}
		items = items.substring(0,items.length()-2);
		RequestBody body = new FormEncodingBuilder().add("user_id", session.getUID())
				.add("items", items).add("authenticitiy_token",session.getToken()).build();
		request = new Request.Builder().url(URLs.sessionsApiURL).post(body).build();
		try {
			response = run(request).body().string();
		} catch (IOException e) {
			e.printStackTrace();	
			return null;
		}
		logout();
		JsonArray array = new JsonParser().parse(response).getAsJsonArray();
		Gson gson = new Gson();
		List<Activity> list = new LinkedList<Activity>();
		for(JsonElement e : array)
		{
			Activity activity = gson.fromJson(e, Activity.class);
			list.add(activity);
		}
		return  list;
	}

	public Response run(Request request) {
		Response response = null;
		try {
			response = client.newCall(request).execute();
			return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private void logout()
	{
		Request request = new Request.Builder().url(URLs.logoutUrl).get()
				.build();
		Response response = run(request);
		session = null;
	}
	private boolean login(String user, String password) {
		RequestBody body = new FormEncodingBuilder().add("user[email]", user)
				.add("user[password]", password).build();
		Request request = new Request.Builder().url(URLs.loginUrl).post(body)
				.build();
		Response response = run(request);
		String resp = null;
		if (response.code() != 200)
			return false;
		try {
			resp = response.body().string();
		} catch (IOException e) {
			return false;
		}
		JsonObject json = new JsonParser().parse(resp).getAsJsonObject();
		Document document = Jsoup.parse(json.get("update").getAsString());
		String token = document.select("input[name=authenticity_token]").val();
		String userName = document
				.select("a[href~=https://www.runtastic.com/en/users/(.*)/dashboard]")
				.attr("href").split("/")[5];

		this.session = new Session(token, userName);
		return true;
	}
}
