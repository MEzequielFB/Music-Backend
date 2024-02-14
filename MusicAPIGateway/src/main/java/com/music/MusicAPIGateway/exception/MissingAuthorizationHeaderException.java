package com.music.MusicAPIGateway.exception;

@SuppressWarnings("serial")
public class MissingAuthorizationHeaderException extends Exception {

	public MissingAuthorizationHeaderException() {
		super("Missing authorization header!");
	}
}
