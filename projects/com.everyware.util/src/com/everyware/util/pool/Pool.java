package com.everyware.util.pool;

import java.util.Deque;
import java.util.LinkedList;

public class Pool<T extends AutoCloseable> {
	private int size;

	private Source<T> source;
	private Deque<T> pool;

	public Pool(Source<T> source, int size) throws Exception {
		this.source = source;
		this.size = size;
		pool = new LinkedList<>();
		for(int i = 0; i < size; i++) {
			pool.add(source.produce());
		}
	}

	public synchronized T get() throws Exception {
		if(pool.size() > 0) {
			return pool.pop();
		} else {
			return source.produce();
		}
	}

	public synchronized void release(T item) throws Exception {
		if(pool.size() >= size) {
			item.close();
		} else {
			pool.push(item);
		}
	}

	public void destroy(T item) throws Exception {
		item.close();
	}
}
