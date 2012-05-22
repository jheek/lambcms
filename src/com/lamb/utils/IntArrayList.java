package com.lamb.utils;

public final class IntArrayList {
	
	private int[] mData;
	
	private int mSize = 0;
	
	public IntArrayList() {
		this(0);
	}
	
	public IntArrayList(int capacity) {
		mData = new int[capacity];
	}
	
	public int get(int index) {
		return mData[index];
	}
	
	public void set(int index, int value) {
		mData[index] = value;
	}
	
	public void add(int v) {
		ensureCapacity(mSize + 1);
		mData[mSize] = v;
		mSize++;
	}
	
	public int addSorted(int v) {
		final int l = size();
		if (l == 0 || v >= mData[l - 1]) {
			add(v);
			return l;
		}
		
		for (int i = 0; i < l; i++) {
			if (v <= mData[i]) {
				add(i, v);
				return i;
			}
		}
		// this will never happen
		return -1;
	}
	
	public void add(int index, int value) {
		ensureCapacity(mSize+1);
		System.arraycopy(mData, index, mData, index + 1,
				 mSize - index);
		mData[index] = value;
		mSize++;
    }
	
	public int removeIndex(int index) {
		int oldValue = mData[index];

		int numMoved = mSize - index - 1;
		if (numMoved > 0)
		    System.arraycopy(mData, index+1, mData, index,
				     numMoved);

		mSize--;
		return oldValue;
	}
	
	public boolean removeValue(int value) {
		int i = indexOf(value);
		if (i >= 0) {
			removeIndex(i);
			return true;
		}
		return false;
	}
	
	public int indexOf(int v) {
		int[] data = mData;
		final int l = mSize;
		for (int i = 0; i < l; i++) {
			if (data[i] == v) {
				return i;
			}
		}
		return -1;
	}
	
	public void clear() {
		this.mSize = 0;
	}
	
	public boolean contains(int v) {
		return indexOf(v) >= 0;
	}
	
	public void ensureCapacity(int minCapacity) {
		int oldCapacity = mData.length;
		if (minCapacity > oldCapacity) {
		    int newCapacity = (oldCapacity * 3)/2 + 1;
	    	if (newCapacity < minCapacity)
	    		newCapacity = minCapacity;
	        // minCapacity is usually close to size, so this is a win:
	    	mData = copyOf(mData, newCapacity);
		}
    }
	
	public void optimize() {
		mData = copyOf(mData, mSize);
	}
	
	public int[] getData() {
		return mData;
	}
	
	public int size() {
		return mSize;
	}
	
	public int[] copyOf(int[] source, int capacity) {
		int[] data = new int[capacity];
		System.arraycopy(source, 0, data, 0, Math.min(source.length, capacity));
		return data;
	}
	
}
