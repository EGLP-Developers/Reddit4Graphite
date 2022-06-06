package me.eglp.reddit;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import me.eglp.apibase.APIBase;
import me.eglp.apibase.util.Endpoint;
import me.eglp.apibase.util.RequestParameters;
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
import me.eglp.reddit.util.RedditException;
import me.eglp.reddit.util.UnknownKindException;
import me.mrletsplay.mrcore.http.HttpPost;
import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.mrcore.http.HttpResult;
import me.mrletsplay.mrcore.http.data.URLEncodedData;
import me.mrletsplay.mrcore.json.JSONArray;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONConverter;
import me.mrletsplay.mrcore.misc.FriendlyException;

public class RedditAPI extends APIBase {
	
	public static final String
		ENDPOINT = "https://oauth.reddit.com/";
	
	private String clientID, clientSecret;
	private UserAgent userAgent;
	private OAuthToken token;
	private HttpClient httpClient;
	
	public RedditAPI(String clientID, String clientSecret, UserAgent userAgent) {
		this.clientID = clientID;
		this.clientSecret = clientSecret;
		this.userAgent = userAgent;
		this.httpClient = HttpClient.newBuilder()
			.followRedirects(Redirect.NEVER)
			.build();
		refreshToken();
	}
	
	@Override
	public void onRequest(Endpoint endpoint, RequestParameters parameters, HttpRequest request) {
		ensureTokenValid(false);
		request.setClient(httpClient);
		request.setHeader("User-Agent", userAgent.getUserAgent());
		if(token != null) request.setHeader("Authorization", "Bearer " + token.getAccessToken());
	}
	
	@Override
	public void onRequestResult(Endpoint endpoint, RequestParameters parameters, HttpRequest request, HttpResult result) {
		Ratelimiter.addRequest();
	}
	
	/**
	 * Retrieves the top posts according to the provided sorting<br>
	 * If the subreddit provided does not exist, this method will return garbage (e.g. a ListingPaginator&lt;Subreddit&gt; with subreddit search results)
	 * @param subreddit The subreddit to retrieve posts in
	 * @param sort The sorting method to use
	 * @param limit The maximum amount of posts per page
	 * @return A {@link ListingPaginator} to paginate posts, initialized with the first page
	 */
	@SuppressWarnings("unchecked")
	public ListingPaginator<Link> getPosts(String subreddit, SubredditSort sort, int limit) {
		RequestParameters params = new RequestParameters().put("subreddit", subreddit).put("sort", sort.name().toLowerCase()).put("limit", ""+limit);
		JSONObject obj = makeRequest(RedditEndpoint.SUBREDDIT_SORT, params).asJSONObject();
		if(obj.has("error")) return null;
		Listing<Link> l = JSONConverter.decodeObject(obj, Listing.class);
		return new ListingPaginator<>(this, RedditEndpoint.SUBREDDIT_SORT, params, l);
	}
	
	/**
	 * Retrieves a random post
	 * @param subreddit The subreddit to retrieve the post from
	 * @return A random post from that subreddit
	 */
	@SuppressWarnings("unchecked")
	public Link getRandomPost(String subreddit) {
		try {
			Optional<String> loc = makeRequest(RedditEndpoint.SUBREDDIT_RANDOM, new RequestParameters().put("subreddit", subreddit)).getHeaders().firstValue("Location");
			if(!loc.isPresent()) return null;
			JSONArray postAndComments = new JSONArray(httpClient.send(java.net.http.HttpRequest.newBuilder(URI.create(loc.get())).build(), BodyHandlers.ofString()).body());
			System.out.println(postAndComments);
			Listing<Link> l = JSONConverter.decodeObject(postAndComments.getJSONObject(0), Listing.class);
			return l.getData().getChildren().get(0);
		} catch (IOException | InterruptedException e) {
			throw new RedditException(e);
		}catch(ClassCastException e) {
			// Probably got a listing
			return null;
		}
	}
	
	/**
	 * Retrieves information about the subreddit
	 * @param subreddit The subreddit to retrieve information about
	 * @return A {@link Subreddit} object or <code>null</code> if the subreddit doesn't exist
	 */
	public Subreddit getAbout(String subreddit) {
		try {
			return (Subreddit) JSONConverter.decodeObject(makeRequest(RedditEndpoint.SUBREDDIT_ABOUT, new RequestParameters().put("subreddit", subreddit)).asJSONObject(), Thing.class).getData();
		}catch(UnknownKindException e) {
			// We probably got a search listing
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ThingData> Listing<T> getListingBefore(RedditEndpoint data, RequestParameters params, String before) {
		return JSONConverter.decodeObject(makeRequest(data, params.put("before", before)).asJSONObject(), Listing.class);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ThingData> Listing<T> getListingAfter(RedditEndpoint data, RequestParameters params, String after) {
		return JSONConverter.decodeObject(makeRequest(data, params.put("after", after)).asJSONObject(), Listing.class);
	}
	
	private void ensureTokenValid(boolean forceRefresh) {
		if(forceRefresh || token.getExpiresAt() - System.currentTimeMillis() < 10000) refreshToken();
	}
	
	private void refreshToken() {
		HttpPost post = HttpRequest.createPost("https://www.reddit.com/api/v1/access_token");
		post.setData(new URLEncodedData().set("grant_type", "client_credentials"));
		post.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientID + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)));
		
		try {
			this.token = new OAuthToken(post.execute().asJSONObject());
		}catch(Exception e) {
			throw new FriendlyException("OAuth token request failed", e);
		}
	}
	
}
