package me.eglp.reddit.util;

import me.eglp.apibase.util.DefaultRequestMethod;
import me.eglp.apibase.util.Endpoint;
import me.eglp.apibase.util.EndpointDescriptor;
import me.eglp.reddit.RedditAPI;

public enum RedditEndpoint implements Endpoint {
	
	SUBREDDIT_SORT(EndpointDescriptor.builder(DefaultRequestMethod.GET, RedditAPI.ENDPOINT + "r/{subreddit}/{sort}")
		.dynamicQuery("after", "after")
		.dynamicQuery("limit", "limit")
		.create()),
	SUBREDDIT_RANDOM(EndpointDescriptor.builder(DefaultRequestMethod.GET, RedditAPI.ENDPOINT + "r/{subreddit}/random")
		.create()),
	SUBREDDIT_ABOUT(EndpointDescriptor.builder(DefaultRequestMethod.GET, RedditAPI.ENDPOINT + "r/{subreddit}/about")
		.create()),
	;
	
	private final EndpointDescriptor descriptor;
	
	private RedditEndpoint(EndpointDescriptor descriptor) {
		this.descriptor = descriptor;
	}
	
	@Override
	public EndpointDescriptor getDescriptor() {
		return descriptor;
	}
	
}
