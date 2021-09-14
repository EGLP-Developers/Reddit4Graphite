package me.eglp.reddit;

public class UserAgent {
	
	private String platform;
	private String appID;
	private String version;
	private String author;
	
	public UserAgent(String platform, String appID, String version, String author) {
		this.platform = platform;
		this.appID = appID;
		this.version = version;
		this.author = author;
	}
	
	public String getUserAgent() {
		return String.format("%s:%s:%s (by /u/%s)", platform, appID, version, author);
	}

}
