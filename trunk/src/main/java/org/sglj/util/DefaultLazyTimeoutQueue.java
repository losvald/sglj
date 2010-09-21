/*
 * DefaultLazyTimeoutQueue.java
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

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * The default implementation of the {@link LazyTimeoutQueue} based
 * on the {@link ArrayDeque} implementation.
 * 
 * @author Leo Osvald
 * 
 * @param <E> the type of elements held in the queue
 */
public class DefaultLazyTimeoutQueue<E> extends 
AbstractLazyTimeoutQueue<E> {

	/**
	 * Creates the timeout queue with the specified timeout.
	 * @param timeout timeout in milliseconds
	 */
	public DefaultLazyTimeoutQueue(long timeout) {
		super(timeout);
		setTimeout(timeout);
	}

	@Override
	protected Queue<Long> createTimestampQueue() {
		return new ArrayDeque<Long>();
	}

	@Override
	protected Queue<E> createQueue() {
		return new ArrayDeque<E>();
	}

}
