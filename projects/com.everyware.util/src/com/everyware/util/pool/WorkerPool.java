package com.everyware.util.pool;

import java.util.List;
import java.util.ArrayList;

public class WorkerPool {
	private int threadcount;
	private List<Thread> threads = new ArrayList<>();
	private List<Runnable> tasks = new ArrayList<>();

	private final Runnable worker = () -> {
		main: while(!Thread.currentThread().isInterrupted()) {
			Runnable r;
			synchronized(tasks) {
				while(tasks.size() == 0) {
					try {
						tasks.wait();
					} catch(InterruptedException e) {
						break main;
					}
				}
				assert tasks.size() != 0;
				// TODO: make sure there is no starvation
				r = tasks.remove(tasks.size() - 1);
				tasks.notifyAll();
			}
			try {
				r.run();
			} catch(Throwable t) {
				t.printStackTrace();
			}
		}
	};

	public WorkerPool(int n) {
		threadcount = n;
	}

	public void start() {
		for(int i = 0; i < threadcount; i++) {
			Thread t = new Thread(worker);
			threads.add(t);
			t.start();
		}
	}

	public void submit(Runnable r) {
		synchronized(tasks) {
			tasks.add(r);
			tasks.notifyAll();
		}
	}

	public void sync() {
		try {
			synchronized(tasks) {
				while(tasks.size() > 0)
					tasks.wait();
			}
		} catch(InterruptedException e) {
		}
	}

	public void shutdown() {
		for(Thread t : threads)
			t.interrupt();
		try {
			for(Thread t : threads)
				t.join();
		} catch(InterruptedException e) {
		}
	}

}
