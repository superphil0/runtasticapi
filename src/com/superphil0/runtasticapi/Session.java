package com.superphil0.runtasticapi;

public class Session {
	private String sessionURL;
	private String token;
	private String userName;
	private String UID;

	public Session(String token, String userName) {
		super();
		this.token = token;
		this.userName = userName;
		this.sessionURL = "https://www.runtastic.com/en/users/" + userName
				+ "/sport-sessions";
	}

	public String getSessionURL() {
		return sessionURL;
	}

	public String getToken() {
		return token;
	}

	public String getUID() {
		return UID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUID(String uID) {
		UID = uID;
	}

}
