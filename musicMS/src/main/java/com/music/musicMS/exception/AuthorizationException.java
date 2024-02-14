package com.music.musicMS.exception;

@SuppressWarnings("serial")
public class AuthorizationException extends Exception {

	public AuthorizationException() {
		super("Does not have the authorization to make the current action");
	}
}
