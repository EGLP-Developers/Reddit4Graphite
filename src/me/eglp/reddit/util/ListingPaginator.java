package me.eglp.reddit.util;

import java.util.ArrayList;
import java.util.List;

import me.eglp.reddit.RedditAPI;
import me.eglp.reddit.entity.Listing;
import me.eglp.reddit.entity.data.ThingData;

public class ListingPaginator<T extends ThingData> {
	
	private RedditAPI api;
	private String url;
	private int limit;
	private Listing<T> currentListing;
	
	public ListingPaginator(RedditAPI api, String url, int limit, Listing<T> initialListing) {
		this.api = api;
		this.url = url;
		this.limit = limit;
		this.currentListing = initialListing;
	}
	
	public RedditAPI getAPI() {
		return api;
	}
	
	public boolean next() {
		if(currentListing.getData().getAfter() == null) return false;
		currentListing = api.getListingAfter(url, currentListing.getData().getAfter(), limit);
		return true;
	}
	
	public Listing<T> getCurrentListing() {
		return currentListing;
	}
	
	public List<T> collect(int amount) {
		List<T> things = new ArrayList<>();
		things.addAll(currentListing.getData().getChildren());
		while(things.size() < amount && next()) {
			things.addAll(currentListing.getData().getChildren());
		}
		while(things.size() > amount) things.remove(things.size() - 1);
		return things;
	}
	
}
