package com.music.userMS.exception;

@SuppressWarnings("serial")
public class NotEnoughBalanceException extends Exception {
	public NotEnoughBalanceException(Integer id) {
		super(String.format("The account with id %s has a balance of zero or lower", id));
	}
}
