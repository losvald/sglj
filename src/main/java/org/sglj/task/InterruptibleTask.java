/*
 * InterruptibleTask.java
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
 * The task which can be interrupted (i.e. paused) or postponed.
 * 
 * @author Leo Osvald
 * @version 0.9
 */
public interface InterruptibleTask extends CancelableTask {
	
	/**
	 * Checks whether the task is running.
	 * @return <code>true</code> if the task is running,
	 * <code>false</code> otherwise.
	 */
	boolean isRunning();
	
	/**
	 * Changes task status from running to interrupted and vice-versa.
	 */
	void setRunning(boolean running);
	
	/**
	 * Registers a new {@link TaskInterruptListener} listener.
	 * @param l listener
	 */
	void addTaskInterruptListener(TaskInterruptListener l);
	
	/**
	 * Unregister an existing {@link TaskInterruptListener} listener.
	 * @param l listener
	 */
	void removeTaskInterruptListener(TaskInterruptListener l);
	
	/**
	 * Registers new {@link CancellableTaskListener} listener.<br>
	 * Equivalent to the following to calls (in that order):
	 * {@link #addTaskListener(TaskListener)},
	 * {@link #addTaskInterruptListener(TaskInterruptListener)}
	 * @param l listener
	 */
	void addInterruptibleTaskListener(InterruptibleTaskListener l);
	
	/**
	 * Unregisters existing {@link CancellableTaskListener} listener.<br>
	 * Equivalent to the following to calls (in that order):
	 * {@link #removeTaskListener(TaskListener)},
	 * {@link #addTaskInterruptListener(TaskInterruptListener)}
	 * @param l listener
	 */
	void removeInterruptibleTaskListener(InterruptibleTaskListener l);
}
