package me.eglp.reddit.util;

import me.eglp.reddit.RedditAPI;

public enum RedditEndpoint {
	
	SUBREDDIT_SORT(RedditAPI.ENDPOINT + "r/%s/%s"),
	SUBREDDIT_ABOUT(RedditAPI.ENDPOINT + "r/%s/about"),
	;
	
	public final String url;
	
	private RedditEndpoint(String url) {
		this.url = url;
	}
	
	public String getURL(String... params) {
		return String.format(url, (Object[]) params);
	}
	
}
