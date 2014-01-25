/*
 * TaskEvent.java
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

package org.sglj.task;

import java.util.EventObject;

/**
 * The event which indicates that some properties of the task
 * have been changed.
 * 
 * @author Leo Osvald
 * @version 1.11
 */
public class TaskEvent extends EventObject {
	private transient Task task;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * {@inheritDoc}
	 * @param source object which fired this event
	 */
	public TaskEvent(Object source) {
		super(source);
	}
	
	/**
	 * Clones the event.
	 * @param shallowCopy whether a shallow copy or hard copy is preferred
	 * @param clone the event that which is to be cloned
	 */
	public TaskEvent(TaskEvent clone, boolean shallowCopy) {
		super(clone.getSource());
		setTask(clone.getTask());
	}
	
	/**
	 * Sets the task which is related to this event.
	 * @param task the task
	 */
	public void setTask(Task task) {
		this.task = task;
	}
	
	/**
	 * Returns the task which is related to this event.
	 * @return the related task
	 */
	public Task getTask() {
		return task;
	}
	
}
