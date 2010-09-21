/*
 * LazyTimeoutQueue.java
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

import java.util.List;

/**
 * A lazy timeout queue.
 * This interface in a way breaks the contract specified in the 
 * {@link TimeoutQueue} because it does not guarantee that elements 
 * will be popped as soon as possible. Instead, this interface
 * requires implementing classes to ensure that timed out elements
 * are popped whenever some modification is made to the queue, such as
 * insertion (enqueue) or removal (dequeue).
 * Additionally, timed out elements must be be popped from the queue when
 * the following methods are called:
 * <ul>
 * <li>{@link #dequeExpired()}</li>
 * <li>{@link #popExpired()}</li>
 * </ul>
 * 
 * @author Leo Osvald
 * @version 1.0
 * 
 * @param <E> the type of elements held in the queue
 */
public interface LazyTimeoutQueue<E> extends TimeoutQueue<E> {
	
	/**
	 * Pops all elements out of the queue which expired. More precisely,
	 * all elements which were inserted before 
	 * {@link System#currentTimeMillis()} - {@link #getTimeout()} milliseconds
	 * will be popped.
	 * In contrast to the {@link #popExpired()} method, this method returns
	 * only the number of popped elements and is thus more efficient when
	 * popped elements are not interesting to the caller.
	 */
	int dequeExpired();
	
	/**
	 * Pops all elements out of the queue which expired. More precisely,
	 * all elements which were inserted before 
	 * {@link System#currentTimeMillis()} - {@link #getTimeout()} milliseconds
	 * will be popped. 
	 * @return the array containing popped elements, in the order popped
	 */
	List<E> popExpired();
}
