/*
 * AbstractTimeoutQueue.java
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

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * An implementation of timeout queue which uses a queue specified
 * in the constructor to hold the elements.<br>
 * This abstract implementation has no implemented logic for determining 
 * which elements have timed out nor provides automatic popping of
 * timed out elements.
 * 
 * @author Leo Osvald
 * @version 1.0
 * 
 * @param <E> the type of elements held in the queue
 */
public abstract class AbstractTimeoutQueue<E> extends AbstractQueue<E> 
implements TimeoutQueue<E> {
	
	private final Queue<E> queue;
	private long timeout;

	/**
	 * Creates the timeout queue with the specified timeout.
	 * @param timeout timeout in milliseconds
	 */
	public AbstractTimeoutQueue(long timeout) {
		this.queue = createQueue();
		setTimeout(timeout);
		if (this.queue == null)
			throw new NullPointerException("Underlying queue cannot be null");
	}
	
	@Override
	public Iterator<E> iterator() {
		return queue.iterator();
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public long getTimeout() {
		return timeout;
	}

	@Override
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	@Override
	public boolean offer(E e) {
		return queue.offer(e);
	}

	@Override
	public E peek() {
		return queue.peek();
	}

	@Override
	public E poll() {
		return queue.poll();
	}
	
	@Override
	public void clear() {
		queue.clear();
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		return queue.addAll(c);
	}

	/**
	 * Creates the underlying queue which will hold the element of this queue
	 * @return the queue that should be used to hold elements
	 */
	protected abstract Queue<E> createQueue();
	
}
