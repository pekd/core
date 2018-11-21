package com.everyware.util.pool;

public interface Source<T> {
	public T produce() throws Exception;
}
