package com.lamb.utils;

import java.util.Arrays;
import java.util.Collection;

public final class FastSortedArrayList<E extends Comparable<E>> extends AbsFastArrayList<E> {

	public FastSortedArrayList() {
		super();
	}

	public FastSortedArrayList(Collection<? extends E> c) {
		super(c);
	}

	public FastSortedArrayList(int initialCapacity) {
		super(initialCapacity);
		sort();
	}
	
	public void add(E e) {
		final int l = size();
		if (l == 0) {
			super.add(e);
		}
		if (e.compareTo(get(l - 1)) > 0) {
			super.add(e);
		}
		
		for (int i = 0; i < l; i++) {
			if (e.compareTo(get(i)) <= 0) {
				super.add(i, e);
			}
		}
		super.add(e);
	};
	
	@Override
	public int indexOf(Object o) {
		return Arrays.binarySearch(elementData, 0, size, o);
	}
	
	@Override
	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}
	
	@Override
	public boolean remove(Object o) {
		int i = indexOf(o);
		if (i >= 0) {
			fastRemove(i);
			return true;
		}
		return false;
	}
	
	public void add(int index, E element) {
		add(element);
	};
	
	
}
