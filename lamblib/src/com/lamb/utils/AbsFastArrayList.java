package com.lamb.utils;

import java.util.Arrays;
import java.util.Collection;

public class AbsFastArrayList<E> {

	    protected Object[] elementData;
	    protected int size;

	    public AbsFastArrayList(int initialCapacity) {
	        if (initialCapacity < 0)
	            throw new IllegalArgumentException("Illegal Capacity: "+ initialCapacity);
	        this.elementData = new Object[initialCapacity];
	    }

	    public AbsFastArrayList() {
	        this(10);
	    }
	    
	    public AbsFastArrayList(Collection<? extends E> c) {
	        elementData = c.toArray();
	        size = elementData.length;
	        // c.toArray might (incorrectly) not return Object[] (see 6260652)
	        if (elementData.getClass() != Object[].class)
	            elementData = Arrays.copyOf(elementData, size, Object[].class);
	    }

	    public void trimToSize() {
	        int oldCapacity = elementData.length;
	        if (size < oldCapacity) {
	            elementData = Arrays.copyOf(elementData, size);
	        }
	    }

	    public void ensureCapacity(int minCapacity) {
	        int oldCapacity = elementData.length;
	        if (minCapacity > oldCapacity) {
	            int newCapacity = (oldCapacity * 3)/2 + 1;
	            if (newCapacity < minCapacity)
	                newCapacity = minCapacity;
	            // minCapacity is usually close to size, so this is a win:
	            elementData = Arrays.copyOf(elementData, newCapacity);
	        }
	    }
	    
	    public void sort() {
	    	Arrays.sort(elementData, 0, size);
	    }

	    public int size() {
	        return size;
	    }

	    public boolean isEmpty() {
	        return size == 0;
	    }
	    
	    public boolean contains(Object o) {
	        return indexOf(o) >= 0;
	    }
	    
	    public int indexOf(Object o) {
	        if (o == null) {
	            for (int i = 0; i < size; i++)
	                if (elementData[i]==null)
	                    return i;
	        } else {
	            for (int i = 0; i < size; i++)
	                if (o.equals(elementData[i]))
	                    return i;
	        }
	        return -1;
	    }

	    public int lastIndexOf(Object o) {
	        if (o == null) {
	            for (int i = size-1; i >= 0; i--)
	                if (elementData[i]==null)
	                    return i;
	        } else {
	            for (int i = size-1; i >= 0; i--)
	                if (o.equals(elementData[i]))
	                    return i;
	        }
	        return -1;
	    }
	    
	    public Object[] toArray() {
	        return Arrays.copyOf(elementData, size);
	    }

	    @SuppressWarnings("unchecked")
	    public <T> T[] toArray(T[] a) {
	        if (a.length < size)
	            // Make a new array of a's runtime type, but my contents:
	            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
	        System.arraycopy(elementData, 0, a, 0, size);
	        if (a.length > size)
	            a[size] = null;
	        return a;
	    }

	    @SuppressWarnings("unchecked")
		public E get(int index) {
	        return (E) elementData[index];
	    }

	    @SuppressWarnings("unchecked")
		public E set(int index, E element) {
	        E oldValue = (E) elementData[index];
	        elementData[index] = element;
	        return oldValue;
	    }

	    public void add(E e) {
	        ensureCapacity(size + 1);
	        elementData[size++] = e;
	    }

	    public void add(int index, E element) {
	        ensureCapacity(size+1);
	        System.arraycopy(elementData, index, elementData, index + 1,
	                         size - index);
	        elementData[index] = element;
	        size++;
	    }
	    
	    @SuppressWarnings("unchecked")
		public E remove(int index) {
	        E oldValue = (E) elementData[index];

	        int numMoved = size - index - 1;
	        if (numMoved > 0)
	            System.arraycopy(elementData, index+1, elementData, index,
	                             numMoved);
	        elementData[--size] = null; // Let gc do its work

	        return oldValue;
	    }

	    public boolean remove(Object o) {
	        if (o == null) {
	            for (int index = 0; index < size; index++)
	                if (elementData[index] == null) {
	                    fastRemove(index);
	                    return true;
	                }
	        } else {
	            for (int index = 0; index < size; index++)
	                if (o.equals(elementData[index])) {
	                    fastRemove(index);
	                    return true;
	                }
	        }
	        return false;
	    }

	    protected void fastRemove(int index) {
	        int numMoved = size - index - 1;
	        if (numMoved > 0)
	            System.arraycopy(elementData, index+1, elementData, index,
	                             numMoved);
	        elementData[--size] = null; // Let gc do its work
	    }

	    public void clear() {
	        // Let gc do its work
	        for (int i = 0; i < size; i++)
	            elementData[i] = null;

	        size = 0;
	    }
	    
	    public boolean addAll(Collection<? extends E> c) {
	        Object[] a = c.toArray();
	        int numNew = a.length;
	        ensureCapacity(size + numNew);  // Increments modCount
	        System.arraycopy(a, 0, elementData, size, numNew);
	        size += numNew;
	        return numNew != 0;
	    }
	    
	    public boolean addAll(int index, Collection<? extends E> c) {
	        Object[] a = c.toArray();
	        int numNew = a.length;
	        ensureCapacity(size + numNew);  // Increments modCount

	        int numMoved = size - index;
	        if (numMoved > 0)
	            System.arraycopy(elementData, index, elementData, index + numNew,
	                             numMoved);

	        System.arraycopy(a, 0, elementData, index, numNew);
	        size += numNew;
	        return numNew != 0;
	    }

	    protected void removeRange(int fromIndex, int toIndex) {
	        int numMoved = size - toIndex;
	        System.arraycopy(elementData, toIndex, elementData, fromIndex,
	                         numMoved);

	        // Let gc do its work
	        int newSize = size - (toIndex-fromIndex);
	        while (size != newSize)
	            elementData[--size] = null;
	    }

	    public boolean removeAll(Collection<?> c) {
	        return batchRemove(c, false);
	    }

	    public boolean retainAll(Collection<?> c) {
	        return batchRemove(c, true);
	    }

	    private boolean batchRemove(Collection<?> c, boolean complement) {
	        final Object[] elementData = this.elementData;
	        int r = 0, w = 0;
	        boolean modified = false;
	        try {
	            for (; r < size; r++)
	                if (c.contains(elementData[r]) == complement)
	                    elementData[w++] = elementData[r];
	        } finally {
	            // Preserve behavioral compatibility with AbstractCollection,
	            // even if c.contains() throws.
	            if (r != size) {
	                System.arraycopy(elementData, r,
	                                 elementData, w,
	                                 size - r);
	                w += size - r;
	            }
	            if (w != size) {
	                for (int i = w; i < size; i++)
	                    elementData[i] = null;
	                size = w;
	                modified = true;
	            }
	        }
	        return modified;
	    }
}
