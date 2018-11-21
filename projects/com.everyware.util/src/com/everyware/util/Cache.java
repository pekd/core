package com.everyware.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Cache<K, V> {
	private Map<K, V> map;
	private List<K> lru;
	private int size;

	public Cache(int size) {
		this.size = size;
		map = new HashMap<>();
		lru = new LinkedList<>();
	}

	public boolean contains(K key) {
		return map.containsKey(key);
	}

	public V get(K key) {
		if(!contains(key)) {
			return null;
		}
		lru.remove(key);
		lru.add(key);
		return map.get(key);
	}

	public void put(K key, V value) {
		if(map.size() >= size) {
			evict();
		}
		lru.add(key);
		map.put(key, value);
	}

	private void evict() {
		K key = lru.remove(0);
		map.remove(key);
	}
}
