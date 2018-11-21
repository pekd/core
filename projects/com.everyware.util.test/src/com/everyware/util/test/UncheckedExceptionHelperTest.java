package com.everyware.util.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.everyware.util.UncheckedExceptionHelper;

public class UncheckedExceptionHelperTest {
	public class TestException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	public void f() {
		Exception exc = new TestException();
		UncheckedExceptionHelper.throwException(exc);
	}

	@Test(expected = TestException.class)
	public void test() {
		f();
		fail();
	}
}
