package me.eglp.reddit.util;

import me.eglp.reddit.RedditAPI;

public enum RedditEndpoint {
	
	SUBREDDIT_SORT(RedditAPI.OAUTH_ENDPOINT + "r/%s/%s"),
	SUBREDDIT_ABOUT(RedditAPI.OAUTH_ENDPOINT + "r/%s/about"),
	
	ACCESS_TOKEN(RedditAPI.ENDPOINT + "access_token")
	;
	
	public final String url;
	
	private RedditEndpoint(String url) {
		this.url = url;
	}
	
	public String getURL(String... params) {
		return String.format(url, (Object[]) params);
	}
	
}
