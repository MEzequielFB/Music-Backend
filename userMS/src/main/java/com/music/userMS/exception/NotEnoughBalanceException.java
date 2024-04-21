package com.music.userMS.exception;

@SuppressWarnings("serial")
public class NotEnoughBalanceException extends Exception {
	public NotEnoughBalanceException(Integer id) {
		super(String.format("The account with id %s doesn't have enough balance for the transaction", id));
	}
}
