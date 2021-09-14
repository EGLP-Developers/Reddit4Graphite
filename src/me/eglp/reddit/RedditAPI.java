package me.eglp.reddit;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import me.eglp.reddit.entity.Listing;
import me.eglp.reddit.entity.SubredditSort;
import me.eglp.reddit.entity.Thing;
import me.eglp.reddit.entity.data.Link;
import me.eglp.reddit.entity.data.Subreddit;
import me.eglp.reddit.entity.data.ThingData;
import me.eglp.reddit.ratelimit.Ratelimiter;
import me.eglp.reddit.util.ListingPaginator;
import me.eglp.reddit.util.OAuthToken;
import me.eglp.reddit.util.RedditEndpoint;
import me.mrletsplay.mrcore.http.HttpGet;
import me.mrletsplay.mrcore.http.HttpPost;
import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.mrcore.http.HttpResult;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONConverter;
import me.mrletsplay.mrcore.misc.FriendlyException;

public class RedditAPI {
	
	public static final String
		ENDPOINT = "https://www.reddit.com/api/v1/",
		OAUTH_ENDPOINT = "https://oauth.reddit.com/";
	
	private String clientID, clientSecret;
	private UserAgent userAgent;
	private OAuthToken token;
	
	public RedditAPI(String clientID, String clientSecret, UserAgent userAgent) {
		this.clientID = clientID;
		this.clientSecret = clientSecret;
		this.userAgent = userAgent;
		refreshToken();
	}
	
	@SuppressWarnings("unchecked")
	public ListingPaginator<Link> getPosts(String subreddit, SubredditSort sort, int limit) {
		String url = RedditEndpoint.SUBREDDIT_SORT.getURL(subreddit, sort.name().toLowerCase());
		Listing<Link> l = JSONConverter.decodeObject(makeGetRequest(url, "limit", ""+limit), Listing.class);
		return new ListingPaginator<>(this, url, limit, l);
	}
	
	public Subreddit getAbout(String subreddit) {
		return (Subreddit) JSONConverter.decodeObject(makeGetRequest(RedditEndpoint.SUBREDDIT_ABOUT.getURL(subreddit)), Thing.class).getData();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ThingData> Listing<T> getListingBefore(String url, String before, int limit) {
		return JSONConverter.decodeObject(makeGetRequest(url, "before", before, "limit", ""+limit), Listing.class);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ThingData> Listing<T> getListingAfter(String url, String after, int limit) {
		return JSONConverter.decodeObject(makeGetRequest(url, "after", after, "limit", ""+limit), Listing.class);
	}
	
	public synchronized JSONObject makeGetRequest(String endpoint, String... queryParams) {
		return makeGetRequest(endpoint, true, queryParams);
	}
	
	private void ensureTokenValid(boolean forceRefresh) {
		if(forceRefresh || token.getExpiresAt() - System.currentTimeMillis() < 10000) refreshToken();
	}
	
	private void refreshToken() {
		HttpPost post = HttpRequest.createPost("https://www.reddit.com/api/v1/access_token");
		post.setPostParameter("grant_type", "client_credentials");
		post.setHeaderParameter("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientID + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)));
		
		try {
			this.token = new OAuthToken(post.execute().asJSONObject());
		}catch(Exception e) {
			throw new FriendlyException("OAuth token request failed", e);
		}
	}
	
	public synchronized JSONObject makeGetRequest(String endpoint, boolean tokenCheck, String... queryParams) {
		if(tokenCheck) ensureTokenValid(false);
		Ratelimiter.waitForRatelimitIfNeeded();
		HttpGet r = HttpRequest.createGet(endpoint);

		r.setHeaderParameter("User-Agent", userAgent.getUserAgent());
		if(token != null) r.setHeaderParameter("Authorization", "Bearer " + token.getAccessToken());
		for(int i = 0; i < queryParams.length; i+=2) {
			r.addQueryParameter(queryParams[i], queryParams[i+1]);
		}
		
		try {
			HttpResult res = r.execute();
			Ratelimiter.addRequest();
			return res.asJSONObject();
		}catch(Exception e) {
			if(!tokenCheck) throw new FriendlyException("Request failed", e);
			ensureTokenValid(true);
			return makeGetRequest(endpoint, false, queryParams);
		}
	}
	
	public synchronized JSONObject makePostRequest(String endpoint, String... queryParams) {
		return makePostRequest(endpoint, true, queryParams);
	}
	
	public synchronized JSONObject makePostRequest(String endpoint, boolean tokenCheck, String... queryParams) {
		if(tokenCheck) ensureTokenValid(false);
		Ratelimiter.waitForRatelimitIfNeeded();
		HttpPost r = HttpRequest.createPost(endpoint);

		r.setHeaderParameter("User-Agent", userAgent.getUserAgent());
		if(token != null) r.setHeaderParameter("Authorization", "Bearer " + token.getAccessToken());
		for(int i = 0; i < queryParams.length; i+=2) {
			r.setQueryParameter(queryParams[i], queryParams[i+1]);
		}
		
		try {
			HttpResult res = r.execute();
			Ratelimiter.addRequest();
			return res.asJSONObject();
		}catch(Exception e) {
			if(!tokenCheck) throw e;
			ensureTokenValid(true);
			return makePostRequest(endpoint, false, queryParams);
		}
	}

}