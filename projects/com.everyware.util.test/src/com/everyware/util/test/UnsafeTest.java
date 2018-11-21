package com.everyware.util.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.everyware.util.UnsafeHolder;

import sun.misc.Unsafe;

public class UnsafeTest {
	@Test
	public void test() {
		Unsafe unsafe = UnsafeHolder.getUnsafe();
		assertNotNull(unsafe);
	}
}
