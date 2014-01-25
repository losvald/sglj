/*
 * TimeoutQueue.java
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

import java.util.Queue;

/**
 * A queue in which elements cannot be more than the specified time interval
 * (referred as the timeout).
 * 
 * @author Leo Osvald
 * @version 1.0
 * 
 * @param <E> the type of elements held in the queue
 */
public interface TimeoutQueue<E> extends Queue<E> {
	
	/**
	 * Sets the timeout for the elements in this queue.
	 * @param timeout time interval in milliseconds
	 */
	void setTimeout(long timeout);
	
	/**
	 * Returns the timeout for the elements in this queue.
	 * @return time interval in milliseconds representing timeout
	 */
	long getTimeout();
}
