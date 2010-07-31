/*
 * AmortizedRandomAccessQueue.java
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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.RandomAccess;

/**
 * Lista koja ima jednako efikasne operacije kao i {@link ArrayList}-a, osim
 * sto su queue operacije (definirane u sucelju {@link Queue}) implementirane na 
 * efikasniji nacin:<br>
 * -dodavanje u queue ima vremensku slozenost O(1)
 * -izbacivanje iz queue-a ima vremensku amortiziranu vremensku slozenost O(1)
 * 
 * @see ArrayList
 * @see Queue
 * 
 * @author Leo Osvald
 * @version 1.0
 * @param <E>
 */
public class AmortizedRandomAccessQueue<E> extends AbstractList<E> 
implements QueueList<E>, RandomAccess {

	private ArrayList<E> pushList;
	private ArrayList<E> popList;
	
	public AmortizedRandomAccessQueue() {
		pushList = new ArrayList<E>();
		popList = new ArrayList<E>();
	}
	
	public AmortizedRandomAccessQueue(int initialCapacity) {
		pushList = new ArrayList<E>(initialCapacity);
		popList = new ArrayList<E>(initialCapacity);
	}
	
	public AmortizedRandomAccessQueue(Collection<? extends E> c) {
		this(c.size());
		pushList.addAll(c);
	}
	
	@Override
	public boolean add(E e) {
		if(e == null)
			throw new NullPointerException();
		return pushList.add(e);
	}

	@Override
	public void add(int index, E element) {
		if(element == null)
			throw new NullPointerException();
		int popSize = popList.size();
		if(index < popSize) //notice that popList is reversed so no -1
			popList.add(popSize-index, element);
		else
			pushList.add(index-popSize, element);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for(Object o : c)
			if(o == null)
				throw new NullPointerException();
		return pushList.addAll(c);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		for(Object o : c)
			if(o == null)
				throw new NullPointerException();
		
		int popSize = popList.size();
		if(index < popSize) {
			//since popList is reversed, we must insert a reversed copy
			ArrayList<? extends E> copy = (ArrayList<? extends E>) new ArrayList<Object>(c);
			Collections.reverse(copy);
			return popList.addAll(popSize-index, copy);
		}
		return pushList.addAll(index-popSize, c);
	}

	@Override
	public void clear() {
		pushList.clear();
		popList.clear();
	}

	@Override
	public boolean contains(Object o) {
		return pushList.contains(o) || popList.contains(o);
	}

	@Override
	public E get(int index) {
		int popSize = popList.size();
		if(index < popSize)
			return popList.get(popSize-index-1);
		return pushList.get(index-popSize);
	}

	@Override
	public boolean isEmpty() {
		return pushList.isEmpty() && popList.isEmpty();
	}

	@Override
	public boolean remove(Object o) {
		//optimize if this is the head
		E head = peek();
		if(head != null && head.equals(o))
			return poll() != null;
		//if not, do simple remove
		if(popList.contains(o))
			return popList.remove(o);
		if(pushList.contains(o))
			return pushList.remove(o);
		return false;
	}

	@Override
	public E remove(int index) {
		//optimaze if this is head
		if(index == 0)
			return poll();
		int popSize = popList.size();
		if(index < popSize)
			return popList.remove(popSize-index-1);
		return pushList.remove(index-popSize);
	}

	@Override
	public E set(int index, E element) {
		int popSize = popList.size();
		if(index < popSize)
			return popList.set(popSize-index-1, element);
		return pushList.set(index-popSize, element);
	}

	@Override
	public int size() {
		return pushList.size() + popList.size();
	}

	@Override
	public E element() {
		if(isEmpty())
			throw new NoSuchElementException();
		return peek();
	}

	@Override
	public boolean offer(E e) {
		return add(e);
	}

	@Override
	public E peek() {
		if(!popList.isEmpty())
			return popList.get(popList.size()-1);
		if(!pushList.isEmpty())
			return pushList.get(0);
		return null;
	}

	/**
	 * {@inheritDoc}<br>
	 * This implementation has amortized constant time complexity (time is linear
	 * to the number of removals)
	 */
	@Override
	public E poll() {
		E front = peek();
		if(front == null)
			return null;
		//if popList is empty, move all from pushlist to it
		if(popList.isEmpty()) {
			Collections.reverse(pushList);
			popList.addAll(pushList);
			pushList.clear();
		}
		//pop the element from poplist
		popList.remove(popList.size()-1);
		return front;
	}

	/**
	 * {@inheritDoc}<br>
	 * This implementation has amortized constant time complexity (time is linear
	 * to the number of removals)
	 */
	@Override
	public E remove() {
		if(isEmpty())
			throw new NoSuchElementException();
		return poll();
	}	
	
}
