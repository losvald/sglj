/*
 * RangeMinimumQuery.java
 * 
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
 * 
 * This file is part of SGLJ.
 * 
 * SGLJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SGLJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.sglj.util.struct;

import java.util.Collection;
import java.util.RandomAccess;

import org.sglj.math.discrete.CodomainMergeable;
import org.sglj.util.Preprocessable;


public abstract class RangeMinimumQuery<E, T> 
extends AbstractIndexedStaticCollection<E>
implements RetrieveRangeQueryable<T>, CodomainMergeable<T>, Preprocessable,
RandomAccess {

	private final E[] elements;
	private final T[][] data;
	
	private volatile boolean preprocessed;
	
	@SuppressWarnings("unchecked")
	public RangeMinimumQuery(int capacity) {
		if (capacity < 0)
			throw new IllegalArgumentException("Invalid capacity: " + capacity);
		
		this.elements = (E[]) new Object[capacity];
		int maxK = 31 - Integer.numberOfLeadingZeros(capacity);
		this.data = (T[][]) new Object[maxK + 1][capacity];
	}
	
	public RangeMinimumQuery(E[] elements) {
		this(elements.length);
		System.arraycopy(elements, 0, this.elements, 0, elements.length);
		preprocess();
	}
	
	public RangeMinimumQuery(StaticCollection<? extends E> c) {
		this(c.size());
		int index = 0;
		for (StaticIterator<? extends E> it = c.iterator(); it.hasNext(); )
			elements[index++] = it.next();
		preprocess();
	}
	
	public RangeMinimumQuery(Collection<? extends E> c) {
		this(c.size());
		int index = 0;
		for (E element : c)
			elements[index++] = element;
		preprocess();
	}
	
	@Override
	public int size() {
		int ret = 0;
		for (int i = 1; i < elements.length; ++i)
			if (elements[i] != null)
				++ret;
		return ret;
	}
	
	@Override
	public int capacity() {
		return elements.length;
	}

	@Override
	public E get(int index) {
		return elements[index];
	}

	@Override
	public void set(int index, E element) {
		elements[index] = element;
	}

	@Override
	public T retrieveQuery(int fromIndex, int toIndex) {
		if (!preprocessed)
			throw new IllegalStateException("Need to preprocess first");
		
		if (fromIndex > toIndex)
			throw new IllegalArgumentException("Bad retrieve query range");

		if (fromIndex == toIndex)
			return createIdentityData(null);
		
		int k = 31 - Integer.numberOfLeadingZeros(toIndex - fromIndex);
		return mergeCodomains(
				data[k][fromIndex], 
				data[k][toIndex - (1 << k)]);
	}
	
	@Override
	public void preprocess() {
		int capacity = capacity();
		int k = 31 - Integer.numberOfLeadingZeros(capacity);
		for (int i = 0; i < capacity; ++i)
			data[0][i] = createIdentityData(get(i));
		for (int j = 1; j <= k; ++j)
			for (int i = 0; i + (1 << j) <= capacity; ++i) {
				data[j][i] = mergeCodomains(
						data[j - 1][i],
						data[j - 1][i + (1 << (j - 1))]
				);
			}
		preprocessed = true;
	}
	
	protected abstract T createIdentityData(E element);
	
}
