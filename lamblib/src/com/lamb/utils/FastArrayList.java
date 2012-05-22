package com.lamb.utils;

import java.util.Collection;

public final class FastArrayList<E> extends AbsFastArrayList<E> {

	public FastArrayList() {
		super();
	}

	public FastArrayList(Collection<? extends E> c) {
		super(c);
	}

	public FastArrayList(int initialCapacity) {
		super(initialCapacity);
	}

}
