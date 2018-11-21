package com.everyware.posix;

public class InvalidOptionException extends IllegalArgumentException {
	private static final long serialVersionUID = 2013678244935652697L;

	private String option;

	public InvalidOptionException(String option) {
		super("invalid option -- '" + option + "'");
		this.option = option;
	}

	public String getOption() {
		return option;
	}
}
