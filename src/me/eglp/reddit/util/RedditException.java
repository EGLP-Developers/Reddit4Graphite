package me.eglp.reddit.util;

public class RedditException extends RuntimeException {

	private static final long serialVersionUID = 4558035590259170470L;

	public RedditException() {
		super();
	}

	public RedditException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedditException(String message) {
		super(message);
	}

	public RedditException(Throwable cause) {
		super(cause);
	}
	
}
