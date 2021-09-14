package me.eglp.reddit.entity.data;

import java.util.List;

import me.mrletsplay.mrcore.json.JSONType;
import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONListType;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class Subreddit implements ThingData {
	
	@JSONValue
	private String id;
	
	@JSONValue
	private String name;
	
	@JSONValue
	private int accountsActive;
	
	@JSONValue("comment_score_hide_mins")
	private int commentScoreHideMins;
	
	@JSONValue
	private String description;
	
	@JSONValue("description_html")
	private String descriptionHTML;
	
	@JSONValue("display_name")
	private String displayName;
	
	@JSONValue("header_img")
	private String headerImg;
	
	@JSONValue("header_size")
	@JSONListType(JSONType.INTEGER)
	private List<Integer> headerSize;
	
	@JSONValue("header_title")
	private String headerTitle;
	
	@JSONValue("over_18")
	private boolean over18;
	
	@JSONValue("public_description")
	private String publicDescription;
	
	@JSONValue("public_traffic")
	private boolean publicTraffic;
	
	@JSONValue
	private long subscribers;
	
	@JSONValue("submission_type")
	private String submissionType;
	
	@JSONValue("submit_link_label")
	private String submitLinkLabel;
	
	@JSONValue("submit_text_label")
	private String submitTextLabel;
	
	@JSONValue("subreddit_type")
	private String subredditType;
	
	@JSONValue
	private String title;
	
	@JSONValue
	private String url;
	
	@JSONConstructor
	private Subreddit() {}
	
	public String getID() {
		return id;
	}
	
	public String getFullName() {
		return name;
	}

	public int getAccountsActive() {
		return accountsActive;
	}

	public int getCommentScoreHideMins() {
		return commentScoreHideMins;
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionHTML() {
		return descriptionHTML;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getHeaderImg() {
		return headerImg;
	}

	public List<Integer> getHeaderSize() {
		return headerSize;
	}

	public String getHeaderTitle() {
		return headerTitle;
	}

	public boolean isOver18() {
		return over18;
	}

	public String getPublicDescription() {
		return publicDescription;
	}

	public boolean isPublicTraffic() {
		return publicTraffic;
	}

	public long getSubscribers() {
		return subscribers;
	}

	public String getSubmissionType() {
		return submissionType;
	}

	public String getSubmitLinkLabel() {
		return submitLinkLabel;
	}

	public String getSubmitTextLabel() {
		return submitTextLabel;
	}

	public String getSubredditType() {
		return subredditType;
	}

	public String getTitle() {
		return title;
	}

	public String getURL() {
		return url;
	}
	
	public String getFullURL() {
		return "https://reddit.com" + url;
	}
	
}
