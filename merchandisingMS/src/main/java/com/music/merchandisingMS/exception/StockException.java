package com.music.merchandisingMS.exception;

import com.music.merchandisingMS.model.Product;

@SuppressWarnings("serial")
public class StockException extends Exception {
	public StockException(Product product) {
		super(String.format("The product '%s' doesn't have enough samples (stock: %s)", product.getName(), product.getStock()));
	}
}
