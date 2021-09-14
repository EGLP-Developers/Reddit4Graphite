package me.eglp.reddit.entity.data;

import java.util.List;
import java.util.stream.Collectors;

import me.eglp.reddit.entity.Thing;
import me.mrletsplay.mrcore.json.converter.JSONComplexListType;
import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class ListingData<T extends ThingData> implements ThingData {

	@JSONValue
	private String before;
	
	@JSONValue
	private String after;
	
	@JSONValue
	@JSONComplexListType(Thing.class)
	private List<Thing<T>> children;
	
	@JSONConstructor
	private ListingData() {}
	
	public String getBefore() {
		return before;
	}
	
	public String getAfter() {
		return after;
	}
	
	public List<Thing<T>> getRawChildren() {
		return children;
	}
	
	public List<T> getChildren() {
		return children.stream()
				.map(c -> c.getData())
				.collect(Collectors.toList());
	}
	
}
