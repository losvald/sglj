/*
 * ArrayStack.java
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

package org.sglj.util;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Implementation of the {@link Stack} interface using
 * {@link ArrayList}.<br>
 * This collection does not permit <code>null</code> values and every
 * attempt of insertion of <code>null</code> values will result in
 * a {@link NullPointerException} being thrown.
 * 
 * @author Leo Osvald
 * @version 1.0
 * @param <E> type of elements that are pushed on the stack
 */
public class ArrayStack<E> extends ArrayList<E>
implements Stack<E> {
	
	private static final long serialVersionUID = 2582239474052847242L;

	/**
	 * @see ArrayList#ArrayList()
	 */
	public ArrayStack() {
	}
	
	/**
	 * @see ArrayList#ArrayList(int)
	 * @param capacity initial capacity of the list
	 */
	public ArrayStack(int capacity) {
		super(capacity);
	}

	/**
	 * @see ArrayList#ArrayList(Collection)
	 * @param c the collection whose elements are to be placed into this list
	 */
	public ArrayStack(Collection<? extends E> c) {
		super(c);
	}
	
	@Override
	public E pop() {
		return remove(size()-1);
	}

	@Override
	public void push(E e) {
		add(e);
	}

	@Override
	public E top() {
		if(isEmpty())
			return null;
		return get(size()-1);
	}
	
	public boolean add(E e) {
		if(e == null)
			throw new NullPointerException();
		return super.add(e);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		for(E i : c)
			if(i == null)
				throw new NullPointerException();
		return super.addAll(c);
	}
	
	public void add(int index, E element) {
		super.add(index, element);
	}

}
