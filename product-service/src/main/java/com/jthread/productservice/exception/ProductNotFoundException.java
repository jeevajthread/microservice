package com.jthread.productservice.exception;

public class ProductNotFoundException extends RuntimeException {
	
	public ProductNotFoundException(String mesage) {
		super(mesage);
	}

}
