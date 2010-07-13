/*
 * CancelableTask.java
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
 * An extension of the {@link Task}, which represents a task
 * that can be canceled.
 *  
 * @author Leo Osvald
 * @version 0.9
 */
public interface CancelableTask extends Task {
	/**
	 * Cancels the task.
	 * @return <code>true</code> ako the task was canceled,
	 * <code>false</code> otherwise (or if the task already finished).
	 */
	boolean cancel();
	
	/**
	 * Checks whether the task wac canceled.
	 * @return <code>true</code> if the task was canceled, 
	 * <code>false</code> otherwise.
	 */
	boolean isCanceled();
	
	/**
	 * Registers new {@link TaskCancelListener} listener.
	 * @param l listener
	 */
	void addTaskCancelListener(TaskCancelListener l);
	
	/**
	 * Unregisters existing {@link TaskCancelListener} listener.
	 * @param l listener
	 */
	void removeTaskCancelListener(TaskCancelListener l);
	
	/**
	 * Registers new {@link CancellableTaskListener} listener.<br>
	 * Equivalent to the following to calls (in that order):
	 * {@link #addTaskListener(TaskListener)},
	 * {@link #addTaskCancelListener(TaskCancelListener)}
	 * @param l listener
	 */
	void addCancelableTaskListener(CancelableTaskListener l);
	
	/**
	 * Unregisters existing {@link CancellableTaskListener} listener.<br>
	 * Equivalent to the following to calls (in that order):
	 * {@link #removeTaskListener(TaskListener)},
	 * {@link #removeTaskCancelListener(TaskCancelListener)}
	 * @param l listener
	 */
	void removeCancelableTaskListener(CancelableTaskListener l);
}
