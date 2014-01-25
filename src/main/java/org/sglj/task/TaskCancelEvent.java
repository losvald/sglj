/*
 * TaskCancelEvent.java
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

/**
 * The event which indicates that something which is related
 * to a cancellation of the task has occurred. 
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public class TaskCancelEvent extends TaskEvent {
	
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 * @param source object which fired this event
	 */
	public TaskCancelEvent(Object source) {
		super(source);
	}
	
	/**
	 * Clones the event.
	 * @param shallowCopy whether a shallow copy or hard copy is preferred
	 * @param clone the event that which is to be cloned
	 */
	public TaskCancelEvent(TaskCancelEvent clone, boolean shallowCopy) {
		super(clone, shallowCopy);
	}
	
	@Override
	public CancelableTask getTask() {
		return (CancelableTask) super.getTask();
	}
	
	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException if the argument is not an instance
	 * of {@link CancelableTask}.
	 */
	@Override
	public void setTask(Task task) throws IllegalArgumentException {
		if(!(task instanceof CancelableTask))
			throw new IllegalArgumentException("Task is not instance of "
					+ this.getClass().getName());
		super.setTask(task);
	}
}
