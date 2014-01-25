/*
 * Task.java
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
 * The interface which defines a generic task.<br>
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public interface Task {
	
	/**
	 * A task can be in the on of the following phases:
	 * <ul>
	 * <li>{@value #NOT_STARTED} - means that the task has not yet
	 * started running</li>
	 * <li>{@value #STARTED} -  means that task has started running</li>
	 * <li>{@value #COMPLETED} - means that the task is done</li>
	 * </ul> 
	 *
	 */
	enum TaskState {
		NOT_STARTED, STARTED, COMPLETED
	}

	/**
	 * A status of the task describe a further refine what is actually
	 * happening.
	 */
	interface TaskStatus {
		String name();
	}
	
	/**
	 * Returns the name of the task.
	 * @return name
	 */
	String name();

	/**
	 * Returns the state of the task.
	 * @see TaskState
	 * @return state
	 */
	TaskState getState();

	/**
	 * Setter for the state.
	 * @param state the state of the task
	 */
	void setState(TaskState state);

	/**
	 * Returns the status, which explains what is happening
	 * at the moment.
	 * @return status
	 */
	TaskStatus getStatus();
	
	/**
	 * Setter for the status.
	 * @param status the status of the task
	 */
	void setStatus(TaskStatus status);

	/**
	 * Registers a new task listener.<br>
	 * <b>Note:</b> The use of this method as a way of
	 * registering task manager is discouraged, use
	 * {@link TaskManager#registerTask(Task)} instead.
	 * @param l listener
	 */	
	void addTaskListener(TaskListener taskListener);

	/**
	 * Unregister an existing task listener.
	 * @param l listener
	 */
	void removeTaskListener(TaskListener listener);
}
