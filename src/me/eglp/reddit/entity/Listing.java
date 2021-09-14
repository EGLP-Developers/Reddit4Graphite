package me.eglp.reddit.entity;

import me.eglp.reddit.entity.data.ListingData;
import me.eglp.reddit.entity.data.ThingData;
import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class Listing<T extends ThingData> implements JSONConvertible {
	
	@JSONValue
	private ListingData<T> data;
	
	@JSONConstructor
	private Listing() {}
	
	public ListingData<T> getData() {
		return data;
	}
	
}
