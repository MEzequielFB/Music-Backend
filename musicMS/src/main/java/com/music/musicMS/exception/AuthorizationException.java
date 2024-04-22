package com.music.musicMS.exception;

@SuppressWarnings("serial")
public class AuthorizationException extends Exception {

	public AuthorizationException() {
		super("No authenticated user available");
	}
}
