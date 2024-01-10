package com.music.merchandisingMS.exception;

@SuppressWarnings("serial")
public class NoTagsException extends Exception {
	public NoTagsException(String tagName) {
		super(String.format("The tag '%s' cannot be deleted because is the only one for some products", tagName));
	}
}
