/*
 * ImmutableFenwickTree.java
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

/**
 * A fenwick tree which does not use or require the
 * {@link #inheritSubCodomain(Object, Object)} method to be implemented.
 * However, this implementation imposes two new restrictions. First, 
 * the collection is no longer fully mutable. 
 * The {@link #set(int, Object)} method
 * may only be called at most once, provided that the method
 * {@link #lock()} has not been called. After the {@link #lock()} method
 * has been called, it is guaranteed that no modifications are
 * allowed and this static collection may then be considered "immutable".
 * Constructors which take a collection or array creates a true
 * immutable objects (the {@link #lock()} method is automatically called).
 * Second, the {@link #retrieveQuery(int, int)} method is no longer
 * a supported operation, since it cannot be calculated without
 * the {@link #inheritSubCodomain(Object, Object)} method.
 * <br>
 * 
 * By not having to implement that method, the 
 * {@link #mergeCodomains(Object, Object)} method is made 
 * less-restrictive. For example, one might now implement fenwick tree 
 * to retrieve minimum element in range [0, index], which
 * is impossible to work if the {@link #inheritSubCodomain(Object, Object)} 
 * has to be implemented. Here is the example code for a fenwick
 * tree which computes the minimum element in range [0, index]:<br>
 * 
 * <pre>
 *	class FTMin extends ImmutableFenwickTree<Integer, Integer> {
 *
 *		public FTMin(Integer[] elements, boolean rightToLeft) {
 *			super(elements, 
 *				false); // we want min[0, index], not min[index, indexCount()]
 *		}
 *
 *		//@Override
 *		public Integer mergeCodomains(Integer a, Integer b) {
 *			return Math.min(a, b);
 *		}
 *
 *		//@Override
 *		protected Integer createData(Integer element) {
 *			return element != null ? element
 *					: Integer.MAX_VALUE; // this is neutral for minimum
 *		}
 *	}
 * </pre>
 * 
 * @author Leo Osvald
 *
 * @param <E>
 * @param <T>
 */
public abstract class ImmutableFenwickTree<E, T> extends FenwickTree<E, T> {

	private boolean locked;
	
	public ImmutableFenwickTree(int size, boolean rightToLeft) {
		super(size, rightToLeft);
	}
	
	public ImmutableFenwickTree(E[] elements, boolean rightToLeft) {
		super(elements, rightToLeft);
		lock();
	}
	
	public ImmutableFenwickTree(StaticCollection<? extends E> c,
			boolean rightToLeft) {
		super(c, rightToLeft);
		lock();
	}
	
	public ImmutableFenwickTree(Collection<? extends E> c,
			boolean rightToLeft) {
		super(c, rightToLeft);
		lock();
	}

	@Override
	public final T inheritSubCodomain(T domain, T subdomain) {
		return domain;
	}
	
	@Override
	public final T retrieveQuery(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}
	
	public void set(int index, E element) {
		if (get(index) != null || locked)
			throw new UnsupportedOperationException();
		super.set(index, element);
	}
	
	public void lock() {
		this.locked = true;
	}

}
