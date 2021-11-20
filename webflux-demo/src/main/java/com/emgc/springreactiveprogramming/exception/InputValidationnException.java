package com.emgc.springreactiveprogramming.exception;

public class InputValidationnException extends RuntimeException{
	private static final String  MSG = "allowd range is 10 - 20";
	private static final int errorCode = 100;
	private final int input;

	public InputValidationnException(int input) {
		super(MSG);
		this.input = input;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public int getInput() {
		return input;
	}
}
