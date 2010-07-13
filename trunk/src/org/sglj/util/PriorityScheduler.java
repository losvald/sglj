/*
 * PriorityScheduler.java
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

/**
 * Scheduler which can schedule "tasks" according to specified
 * priority.
 * Scheduling is done on demand by calling {@link #get()} method. 
 * 
 * <br>
 * Note: Adding <code>null</code> values for scheduling should be
 * avoided. 
 * 
 * @author Leo Osvald
 * @version 1.0
 *
 * @param <T> type of object which represent some kind of a "task"
 */
public interface PriorityScheduler<T> {
	
	static final short MAX_PRIORITY		= -0x7fff;
	static final short HIGH_PRIORITY 	= -0x4000;
	static final short NORMAL_PRIORITY 	=  0x0000;
	static final short LOW_PRIORITY 	=  0x4000;
	static final short MIN_PRIORITY		=  0x7fff;
	
	/**
	 * Returns "task" that is right on schedule.<br>
	 * @return "task" or <code>null</code> there is none scheduled.
	 */
	T get();
	
	/**
	 * Adds new object for schedule.
	 * The frequency (likelihood) that {@link #get()} method
	 * will return this "task" over long time is proportional
	 * to its priority.
	 * @param task "task"
	 * @param priority priority
	 */
	void add(T task, short priority);
	
	/**
	 * Removes "task" from schedule.
	 * @param task task which should be removed from schedule
	 */
	void remove(T task);
	
	/**
	 * Sets priority for the specified "task".
	 * @param task "task"
	 * @param priority new priority
	 */
	void setPriority(T task, short priority);
	
	/**
	 * Returns the priority of the specified "task".
	 * @param task "task"
	 * @return priority
	 */
	short getPriority(T task);
}
