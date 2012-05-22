package com.lamb.events;

import java.util.Arrays;

import com.lamb.events.filters.EventFilter;
import com.lamb.utils.FastArrayList;
import com.lamb.utils.IntArrayList;

public class BaseEventDispatcher implements EventDispatcher {

	private IntArrayList mNameHashes;
	private FastArrayList<EventListener> mNameListeners;
	
	private FastArrayList<EventFilter> mFilters;
	private FastArrayList<EventListener> mFilterListeners;
	
	public BaseEventDispatcher() {
		this(10);
	}
	
	public BaseEventDispatcher(int initialSize) {
		mNameHashes = new IntArrayList(initialSize);
		mNameListeners = new FastArrayList<EventListener>(initialSize);
		mFilters = new FastArrayList<EventFilter>(initialSize);
		mFilterListeners = new FastArrayList<EventListener>(initialSize);
	}
	
	@Override
	public void addEventListener(String name, EventListener listener) {
		synchronized (mNameHashes) {
			int i = mNameHashes.addSorted(name.hashCode());
			mNameListeners.add(i, listener);
		}
	}

	@Override
	public void addEventListener(EventFilter filter, EventListener listener) {
		synchronized (mFilters) {
			mFilters.add(filter);
			mFilterListeners.add(listener);
		}
	}

	@Override
	public boolean removeEventListener(String name, EventListener listener) {
		int hash = name.hashCode();
		synchronized (mNameHashes) {
			for (int i = mNameHashes.size() - 1; i >= 0; i--) {
				if (hash == mNameHashes.get(i) && listener == mNameListeners.get(i)) {
					mNameHashes.removeIndex(i);
					mNameListeners.remove(i);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean removeEventListener(EventFilter filter, EventListener listener) {
		synchronized (mFilters) {
			for (int i = mFilters.size() - 1; i >= 0; i--) {
				if (mFilters.get(i) == filter && mFilterListeners.get(i) == listener) {
					mFilters.remove(i);
					mFilterListeners.remove(i);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean containsEventListener(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsEventListener(String name, EventListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsEventListener(EventFilter filter, EventListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void dispatchEvent(Event event) {
		String name = event.getName();
		int hash = name.hashCode();
		synchronized (mNameHashes) {
			int[] data = mNameHashes.getData();
			int l = mNameHashes.size();
			int anIndex = Arrays.binarySearch(data, 0, l, hash);
			for (int i = anIndex; data[i] == hash; i--) {
				mNameListeners.get(i).dispatchEvent(event);
				if (event.isCanceled()) {
					break;
				}
			}
			if (!event.isCanceled()) {
				for (int i = anIndex + 1; data[i] == hash; i++) {
					mNameListeners.get(i).dispatchEvent(event);
					if (event.isCanceled()) {
						return;
					}
				}
			}
		}
		synchronized (mFilters) {
			for (int i = mFilters.size() - 1; i >= 0; i--) {
				EventFilter filter = mFilters.get(i);
				if (filter.filter(event)) {
					mFilterListeners.get(i).dispatchEvent(event);
					if (event.isCanceled()) {
						return;
					}
				}
			}
		}
	}
	
}
