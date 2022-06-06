package me.eglp.reddit.util;

public class UnknownKindException extends RedditException {

	private static final long serialVersionUID = 7823184646573138133L;

	public UnknownKindException(String kind) {
		super("Unknown kind: " + kind);
	}
	
}
