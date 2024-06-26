package com.music.userMS.exception;

@SuppressWarnings("serial")
public class InvalidRoleException extends Exception {

	public InvalidRoleException(String userRole) {
		super(String.format("Cannot change permissions of user with the role %s", userRole));
	}
}
