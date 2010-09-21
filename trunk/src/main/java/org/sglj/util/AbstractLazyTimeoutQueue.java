/*
 * AbstractLazyTimeoutQueue.java
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
import java.util.List;
import java.util.Queue;

/**
 * The abstract implementation of the {@link LazyTimeoutQueue} inteface.
 * 
 * @author Leo Osvald
 *
 * @param <E> the type of elements held in the queue
 */
public abstract class AbstractLazyTimeoutQueue<E> extends 
AbstractTimeoutQueue<E> implements LazyTimeoutQueue<E> {

	protected final Queue<Long> timestampQueue;
	
	/**
	 * Creates the timeout queue with the specified timeout.
	 * @param timeout timeout in milliseconds
	 */
	public AbstractLazyTimeoutQueue(long timeout) {
		super(timeout);
		this.timestampQueue = createTimestampQueue();
		if (this.timestampQueue == null)
			throw new NullPointerException("Underlying queue cannot be null");
	}

	@Override
	public int dequeExpired() {
		long curTime = System.currentTimeMillis();
		int popCount = 0;
		while (!timestampQueue.isEmpty()) {
			long ts = timestampQueue.peek();
			if (curTime - ts >= getTimeout()) {
				remove();
				++popCount;
			} else {
				break;
			}
		}
//		System.out.println("Deque expired : "+popCount + "( timeout = "
//				+ getTimeout() + " ms)");
		return popCount;
	}

	@Override
	public List<E> popExpired() {
		long curTime = System.currentTimeMillis();
		ArrayList<E> ret = new ArrayList<E>();
		while (!timestampQueue.isEmpty()) {
			long ts = timestampQueue.peek();
			if (curTime - ts >= getTimeout()) {
				ret.add(remove());
			} else {
				break;
			}
		}
		return ret;
	}

	@Override
	public boolean offer(E e) {
		if (super.offer(e)) {
			timestampQueue.offer(System.currentTimeMillis());
			dequeExpired();
			return true;
		}
		dequeExpired();
		return false;
	}


	@Override
	public E poll() {
		if (!timestampQueue.isEmpty())
			timestampQueue.poll();
		E ret = super.poll();
		dequeExpired();
		return ret;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean ret = super.addAll(c);
		dequeExpired();
		return ret;
	}
	
	/**
	 * Creates the queue which will hold the time stamps of element insertion.
	 * @return the queue that should be used to hold time stamps
	 */
	protected abstract Queue<Long> createTimestampQueue();
	
}
