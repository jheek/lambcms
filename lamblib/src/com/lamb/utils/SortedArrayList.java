package com.lamb.utils;

import java.util.ArrayList;
import java.util.Collection;

public final class SortedArrayList<E extends Comparable<? super E>> extends ArrayList<E> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SortedArrayList() {
		super();
	}
	
	public SortedArrayList(int initialCapacity) {
		super(initialCapacity);
	}
	
	public SortedArrayList(E... content) {
		super(content.length);
		for (int i = 0; i < content.length; i++) {
			add(content[i]);
		}
	}
	
	public boolean add(E object) {
		
		final int l = size();
		if (l == 0) {
			return super.add(object);
		}
		if (object.compareTo(get(l - 1)) > 0) {
			return super.add(object);
		}
		
		for (int i = 0; i < l; i++) {
			if (object.compareTo(get(i)) <= 0) {
				add(i, object);
				return true;
			}
		}
		return super.add(object);
	};
	
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		for(E item : collection) {
			add(item);
		}
		return true;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		addAll(collection);
		return true;
	}
}
