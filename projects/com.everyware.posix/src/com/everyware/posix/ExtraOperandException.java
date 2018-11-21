package com.everyware.posix;

public class ExtraOperandException extends IllegalArgumentException {
	private static final long serialVersionUID = 2013678244935652697L;

	private String operand;

	public ExtraOperandException(String operand) {
		super("extra operand -- '" + operand + "'");
		this.operand = operand;
	}

	public String getOperand() {
		return operand;
	}
}
