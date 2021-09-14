package me.eglp.reddit.entity;

import me.eglp.reddit.entity.data.Link;
import me.eglp.reddit.entity.data.Subreddit;
import me.eglp.reddit.entity.data.ThingData;
import me.eglp.reddit.util.UnknownKindException;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONConverter;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class Thing<T extends ThingData> implements JSONConvertible {

	@JSONValue
	private String id;
	
	@JSONValue
	private String name;
	
	@JSONValue
	private String kind;
	
	private T data;
	
	@JSONConstructor
	private Thing() {}
	
	public String getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getKind() {
		return kind;
	}
	
	public T getData() {
		return data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void preDeserialize(JSONObject object) {
		switch(object.getString("kind")) {
			case "t3":
				data = (T) JSONConverter.decodeObject(object.getJSONObject("data"), Link.class);
				break;
			case "t5":
				data = (T) JSONConverter.decodeObject(object.getJSONObject("data"), Subreddit.class);
				break;
			default:
				throw new UnknownKindException(object.getString("kind"));
		}
	}
	
}
