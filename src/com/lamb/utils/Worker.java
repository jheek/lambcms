package com.lamb.utils;

import java.util.ArrayList;

public final class Worker {
	
	protected static final int WORKERS_PER_CPU = 2;
	protected static final int DEFAULT_POOL_SIZE = 100;
	
	public static final int DEFAULT_PRIORITY = 0;
	
	protected SortedArrayList<Task> mTasks = new SortedArrayList<Task>();
	protected Task[] mTaskPool;
	protected int mTaskPoolSize;
	
	protected ArrayList<WorkerThread> mWorkers = new ArrayList<WorkerThread>();
	
	protected int mMinThreads;
	private int mMaxThreads;
	
	protected String mName;
	
	public Worker() {
		this("DEFAULT WORKER", 1, WORKERS_PER_CPU * Runtime.getRuntime().availableProcessors());
	}
	
	public Worker(String name, int minThreads, int maxThreads) {
		this(name, minThreads, maxThreads, DEFAULT_POOL_SIZE);
	}
	
	public Worker(String name, int minThreads, int maxThreads, int poolSize) {
		setName(name);
		setMinThreads(minThreads);
		setMaxThreads(maxThreads);
		mTaskPool = new Task[poolSize];
	} 
	
	public void setName(String name) {
		mName = name;
	}
	
	public int getMinThreads() {
		return mMinThreads;
	}
	
	public void setMinThreads(int v) {
		synchronized (mWorkers) {
			mMinThreads = v;
			while (mWorkers.size() < v) {
				addWorker();
			}
		}
	}
	
	public int getMaxThreads() {
		return mMaxThreads;
	}
	
	public void setMaxThreads(int v) {
		synchronized (mWorkers) {
			mMaxThreads = v;
			while (mWorkers.size() > v) {
				removeWorker();
			}
		}
	}
	
	public boolean remove(Runnable r) {
		synchronized (mTasks) {
			for (int i = mTasks.size() - 1; i >= 0; i--) {
				if (mTasks.get(i).runnable == r) {
					mTasks.remove(i);
					return true;
				}
			}
			return false;
		}
	}
	
	private Task createTask(Runnable r, long time, int priority) {
		synchronized (mTaskPool) {
			if (mTaskPoolSize > 0) {
				mTaskPoolSize--;
				Task task = mTaskPool[mTaskPoolSize];
				mTaskPool[mTaskPoolSize] = null;
				task.runnable = r;
				task.time = time;
				task.priority = priority;
				return task;
			}
		}
		return new Task(r, priority, time);
	}
	
	private void recycleTask(Task task) {
		synchronized (mTaskPool) {
			if (mTaskPoolSize < mTaskPool.length) {
				task.runnable = null;
				mTaskPool[mTaskPoolSize] = task;
				mTaskPoolSize++;
			}
		}
	}
	
	public void post(Runnable r) {
		post(r, System.currentTimeMillis(), DEFAULT_PRIORITY);
	}
	
	public void post(Runnable r, long time) {
		post(r, time, DEFAULT_PRIORITY);
	}
	
	public void post(Runnable r, long time, int priority) {
		synchronized (mWorkers) {
			if (mWorkers.size() == 0 || (!hasWaitingWorkers() && mWorkers.size() < mMaxThreads) ) {
				addWorker();
			}
		}
		Task task = createTask(r, time, priority);
		synchronized (mTasks) {
			mTasks.add(task);
			mTasks.notify();
		}
	}
	
	public void postDelayed(Runnable r, long delay) {
		post(r, System.currentTimeMillis() + delay, DEFAULT_PRIORITY);
	}
	
	public void postDelayed(Runnable r, long delay, int priority) {
		post(r, System.currentTimeMillis() + delay, priority);
	}
	
	private boolean hasWaitingWorkers() {
		for (int i = mWorkers.size() - 1; i >= 0; i--) {
			if (mWorkers.get(i).isWaiting) {
				return true;
			}
		}
		return false;
	}
	
	protected Runnable getTask() {
		synchronized (mTasks) {
			int last = mTasks.size() - 1;
			if (last != -1) {
				Task task = mTasks.get(last);
				if (task.time <= System.currentTimeMillis()) {
					mTasks.remove(last);
					Runnable r = task.runnable;
					recycleTask(task);
					return r;
				}
			}
		}
		return null;
	}
	
	protected long getWaitTime() {
		synchronized (mTasks) {
			int last = mTasks.size() - 1;
			if (last != -1) {
				Task task = mTasks.get(last);
				return Math.max(1, task.time - System.currentTimeMillis());
			}
		}
		return 0;
	}
	
	protected void removeWorker() {
		synchronized (mWorkers) {
			for (int i = mWorkers.size() - 1; i >= 0; i--) {
				if (mWorkers.get(i).isWaiting) {
					WorkerThread t = mWorkers.remove(i);
					t.exit();
					updateThreadNames();
					return;
				}
			}
			WorkerThread t = mWorkers.remove(mWorkers.size()-1);
			t.exit();
			updateThreadNames();
		}
	}
	
	private void addWorker() {
		synchronized (mWorkers) {
			WorkerThread t = new WorkerThread();
			mWorkers.add(t);
			t.setName(mName + " #" + mWorkers.size());
		}
	}
	
	private void updateThreadNames() {
		synchronized (mWorkers) {
			for (int i = mWorkers.size() - 1; i >= 0; i--) {
				mWorkers.get(i).setName(mName + " #" + (i + 1));
			}
		}
	}
	
	private class WorkerThread extends Thread {
		
		private boolean mRun = true;
		
		protected boolean isWaiting = false;
		
		public WorkerThread() {
			start();
		}
		
		public void exit() {
			mRun = false;
			interrupt();
		}
		
		@Override
		public void run() {
			while (mRun) {
				Runnable task;
				synchronized (mTasks) {
					task = getTask();
					if (task == null) {
						isWaiting = true;
						long waitTime = getWaitTime();
						if (waitTime == 0 || mMinThreads > 0) {
							synchronized (mWorkers) {
								if (mWorkers.size() > mMinThreads) {
									removeWorker();
									isWaiting = false;
									continue;
								}
							}
						}
						try {
							mTasks.wait(getWaitTime());
						} catch (InterruptedException e) {
							// exiting
						}
					}
				}
				if (task != null) {
					task.run();
				}
			}
		}
	}
	
	private static final class Task implements Comparable<Task> {
		
		protected Runnable runnable;
		protected int priority;
		protected long time;
		
		public Task() {
		}
		
		public Task(Runnable runnable, int priority, long time) {
			super();
			this.runnable = runnable;
			this.priority = priority;
			this.time = time;
		}
		
		@Override
		public int compareTo(Task o) {
			return priority > o.priority ? 1 : (priority < o.priority ? -1 : (time < o.time ? 1 : (time > o.time ? -1 : 0)));
		}
	}
}