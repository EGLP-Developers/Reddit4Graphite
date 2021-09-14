package me.eglp.reddit.entity.data;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.JSONType;
import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class Link implements ThingData {
	
	@JSONValue
	private String author;
	
	@JSONValue("author_flair_css_class")
	private String authorFlairCSSClass;
	
	@JSONValue("author_flair_text")
	private String authorFlairText;
	
	@JSONValue
	private String domain;
	
	@JSONValue
	private boolean hidden;
	
	@JSONValue("is_self")
	private boolean isSelf;
	
	@JSONValue("link_flair_css_class")
	private String linkFlairCSSClass;
	
	@JSONValue("link_flair_text")
	private String linkFlairText;
	
	@JSONValue
	private boolean locked;
	
	@JSONValue("num_comments")
	private int numComments;
	
	@JSONValue("over_18")
	private boolean over18;
	
	@JSONValue
	private boolean spoiler;
	
	@JSONValue
	private String permalink;
	
	@JSONValue
	private int score;
	
	@JSONValue
	private String selftext;
	
	@JSONValue("selftext_html")
	private String selftextHTML;
	
	@JSONValue
	private String subreddit;
	
	@JSONValue("subreddit_id")
	private String subredditID;
	
	@JSONValue
	private String thumbnail;
	
	@JSONValue
	private String title;
	
	@JSONValue
	private String url;
	
	private Long edited;
	
	@JSONValue
	private String distinguished;
	
	@JSONValue
	private boolean stickied;
	
	// Votable
	
	@JSONValue
	private int ups;
	
	@JSONValue
	private int downs;
	
	// Created
	
	@JSONValue
	private long created;
	
	@JSONValue("created_utc")
	private long createdUTC;
	
	@JSONConstructor
	private Link() {}
	
	@Override
	public void preDeserialize(JSONObject object) {
		if(!object.isOfType("edited", JSONType.BOOLEAN)) {
			edited = object.getLong("edited");
		}
	}

	public String getAuthor() {
		return author;
	}

	public String getAuthorFlairCSSClass() {
		return authorFlairCSSClass;
	}

	public String getAuthorFlairText() {
		return authorFlairText;
	}

	public String getDomain() {
		return domain;
	}

	public boolean isHidden() {
		return hidden;
	}

	public boolean isSelf() {
		return isSelf;
	}

	public String getLinkFlairCSSClass() {
		return linkFlairCSSClass;
	}

	public String getLinkFlairText() {
		return linkFlairText;
	}

	public boolean isLocked() {
		return locked;
	}

	public int getNumComments() {
		return numComments;
	}

	public boolean isOver18() {
		return over18;
	}
	
	public boolean isSpoiler() {
		return spoiler;
	}

	public String getPermalink() {
		return permalink;
	}

	public int getScore() {
		return score;
	}

	public String getSelftext() {
		return selftext;
	}

	public String getSelftextHTML() {
		return selftextHTML;
	}

	public String getSubreddit() {
		return subreddit;
	}

	public String getSubredditID() {
		return subredditID;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public String getTitle() {
		return title;
	}

	public String getURL() {
		return url;
	}

	public Long getEdited() {
		return edited;
	}

	public String getDistinguished() {
		return distinguished;
	}

	public boolean isStickied() {
		return stickied;
	}
	
	public int getUps() {
		return ups;
	}
	
	public int getDowns() {
		return downs;
	}
	
	public long getCreated() {
		return created;
	}
	
	public long getCreatedUTC() {
		return createdUTC;
	}
	
}
