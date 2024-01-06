package com.music.userMS.exception;

import com.music.userMS.model.Account;
import com.music.userMS.model.User;

@SuppressWarnings("serial")
public class AlreadyContainsException extends Exception {
	public AlreadyContainsException(Account account, User user) {
		super(String.format("The account with id %s is already linked to the user %s", account.getId(), user.getUsername()));
	}
}
