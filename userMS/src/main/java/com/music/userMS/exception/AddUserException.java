package com.music.userMS.exception;

@SuppressWarnings("serial")
public class AddUserException extends Exception {

	public AddUserException(Integer id) {
		super(String.format("Cannot add the user with id %s because is not a regular user", id));
	}
}
