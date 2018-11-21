package com.everyware.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UniqueSequence {
	private Set<Integer> values;
	private Random rng;

	public UniqueSequence() {
		values = new HashSet<>();
		rng = new Random();
	}

	public int next() {
		while(true) {
			int val = rng.nextInt();
			if(!values.contains(val)) {
				values.add(val);
				return val;
			}
		}
	}

	public void clear() {
		values.clear();
	}

	@Override
	public String toString() {
		return "Sequence[" + values.size() + " values]";
	}
}
