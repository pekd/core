package com.everyware.util;

public class UncheckedExceptionHelper {
	public static void throwException(Throwable t) {
		UnsafeHolder.getUnsafe().throwException(t);
	}
}
