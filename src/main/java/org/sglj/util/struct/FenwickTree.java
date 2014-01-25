/*
 * FenwickTree.java
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

import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

import org.sglj.math.discrete.CodomainMergeable;
import org.sglj.math.discrete.SubCodomainInheritable;

/**
 * TODO
 * 
 * @author Leo Osvald
 *
 * @param <E>
 * @param <T>
 */
public abstract class FenwickTree<E, T> 
extends AbstractIndexedStaticCollection<E> 
implements RetrieveIndexQueryable<T>, RetrieveRangeQueryable<T>,
CodomainMergeable<T>, SubCodomainInheritable<T>, RandomAccess {

	private final E[] elements;
	private final T[] data;
	
	private final boolean rightToLeft;
	
	@SuppressWarnings("unchecked")
	public FenwickTree(int size, boolean rightToLeft) {
		this.elements = (E[]) new Object[size];
		this.data = (T[]) new Object[size + 1];
		for (int i = 1; i < data.length; ++i)
			data[i] = createData(null);
		this.rightToLeft = rightToLeft;
	}
	
	public FenwickTree(E[] elements, boolean rightToLeft) {
		this(elements.length, rightToLeft);
		for (int i = 0; i < elements.length; ++i)
			set(i, elements[i]);
	}
	
	public FenwickTree(StaticCollection<? extends E> c, boolean rightToLeft) {
		this(c.size(), rightToLeft);
		int index = 0;
		for (StaticIterator<? extends E> it = c.iterator(); it.hasNext(); )
			set(index++, it.next());
	}
	
	public FenwickTree(Collection<? extends E> c, boolean rightToLeft) {
		this(c.size(), rightToLeft);
		int index = 0;
		for (E e : c)
			set(index++, e);
	}
	
	@Override
	public E get(int index) {
		return elements[index];
	}

	@Override
	public void set(int index, E element) {
		E oldElement = elements[index];
		elements[index] = element;
		T diff = inheritSubCodomain(createData(element), createData(oldElement));
		if (!rightToLeft) {
			for (++index; index < data.length; index += index & -index)
				data[index] = mergeCodomains(data[index], diff);
		} else {
			for (++index; index > 0; index -= index & -index)
				data[index] = mergeCodomains(data[index], diff);
		}
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
	public T retrieveQuery(int index) {
		T ret = createData(null);
		if (!rightToLeft) {
			for (; index > 0; index -= index & -index)
				ret = mergeCodomains(ret, data[index]);
		} else {
			for (++index; index < data.length; index += index & -index)
				ret = mergeCodomains(ret, data[index]);
		}
		return ret;
	}
	
	@Override
	public T retrieveQuery(int fromIndex, int toIndex) {
		return inheritSubCodomain(
				retrieveQuery(toIndex), 
				retrieveQuery(fromIndex));
	}
	
	public boolean isRightToLeft() {
		return rightToLeft;
	}
	
	protected abstract T createData(E element);

	@Override
	public String toString() {
		return super.toString() + "\n" + Arrays.toString(data);
	}
	
}
