package com.everyware.util.pool;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.everyware.util.exception.Messages;
import com.everyware.util.log.Trace;

public class SerializedWorkerPool {
	private static final Logger log = Trace.create(SerializedWorkerPool.class);

	private int threadcount;
	private List<Thread> threads = new ArrayList<>();
	private Map<Object, Category> tasks = new HashMap<>();

	private static class Category {
		public final Object key;
		private final List<Runnable> tasks;
		private boolean locked;
		private Object lock;

		public Category(Object key) {
			this.key = key;
			tasks = new LinkedList<>();
			lock = new Object();
		}

		@Override
		public int hashCode() {
			return key.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			if(!(o instanceof Category)) {
				return false;
			}
			Category c = (Category) o;
			return c.key.equals(key);
		}

		public boolean has() {
			synchronized(lock) {
				return tasks.size() > 0;
			}
		}

		public boolean locked() {
			return locked;
		}

		public Runnable get() {
			synchronized(lock) {
				if(locked == true) {
					return null;
				}
				if(tasks.size() == 0) {
					return null;
				} else {
					locked = true;
					return tasks.remove(0);
				}
			}
		}

		public int done() {
			synchronized(lock) {
				locked = false;
				return tasks.size();
			}
		}

		public void add(Runnable r) {
			synchronized(lock) {
				tasks.add(r);
			}
		}

		public Object getKey() {
			return key;
		}

		@Override
		public String toString() {
			return key.toString();
		}
	}

	private final Runnable worker = () -> {
		main: while(!Thread.currentThread().isInterrupted()) {
			Runnable r = null;
			Category cat = null;
			synchronized(tasks) {
				while(cat == null) {
					while(tasks.size() == 0) {
						try {
							tasks.wait();
						} catch(InterruptedException e) {
							break main;
						}
					}
					assert tasks.size() != 0;
					for(Category c : tasks.values()) {
						if(!c.locked() && c.has()) {
							cat = c;
							break;
						}
					}
					if(cat == null) {
						try {
							tasks.wait();
						} catch(InterruptedException e) {
							break main;
						}
						continue;
					}
					r = cat.get();
					tasks.notifyAll();
				}
			}
			assert cat != null;
			assert r != null;
			try {
				r.run();
			} catch(Throwable t) {
				log.log(Level.WARNING, Messages.POOL_EXEC_FAIL.format(t.getMessage()), t);
			}
			synchronized(tasks) {
				// no more tasks
				if(cat.done() == 0) {
					cat = tasks.remove(cat.getKey());
					assert cat != null;
				}
				tasks.notifyAll();
			}
		}
	};

	public SerializedWorkerPool(int n) {
		threadcount = n;
	}

	public void start() {
		for(int i = 0; i < threadcount; i++) {
			Thread t = new Thread(worker);
			threads.add(t);
			t.start();
		}
	}

	public void submit(Object key, Runnable r) {
		synchronized(tasks) {
			Category cat = tasks.get(key);
			if(cat == null) {
				cat = new Category(key);
				tasks.put(key, cat);
			}
			cat.add(r);
			tasks.notifyAll();
		}
	}

	public void sync() {
		try {
			synchronized(tasks) {
				while(tasks.size() > 0) {
					tasks.wait();
				}
			}
		} catch(InterruptedException e) {
		}
	}

	public void shutdown() {
		for(Thread t : threads) {
			t.interrupt();
		}
		try {
			for(Thread t : threads) {
				t.join();
			}
		} catch(InterruptedException e) {
		}
	}

}
