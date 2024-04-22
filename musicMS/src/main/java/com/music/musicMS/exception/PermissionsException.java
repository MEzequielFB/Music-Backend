package com.music.musicMS.exception;

@SuppressWarnings("serial")
public class PermissionsException extends Exception {

	public PermissionsException(Integer userId) {
		super(String.format("The user with id '%s' doesn't have permissions to do the current action", userId));
	}
}
