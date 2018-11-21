package com.everyware.util;

import java.util.regex.Pattern;

public class UUID {
	private static final Pattern format = Pattern
			.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

	public static String randomUUID() {
		return java.util.UUID.randomUUID().toString();
	}

	public static boolean isUUID(String s) {
		if(s.length() != 36) {
			return false;
		}
		return format.matcher(s).matches();
	}
}
