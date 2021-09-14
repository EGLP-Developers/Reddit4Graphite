package me.eglp.reddit.util;

import me.mrletsplay.mrcore.misc.FriendlyException;

public class UnknownKindException extends FriendlyException {

	private static final long serialVersionUID = 7823184646573138133L;

	public UnknownKindException(String kind) {
		super("Unknown kind: " + kind);
	}
	
}
