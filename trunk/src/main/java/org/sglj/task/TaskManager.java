/*
 * TaskManager.java
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

import java.util.List;


/**
 * The interface which defines a behaviour of a task manager. 
 * 
 * @author Leo Osvald
 * @version 0.87
 */
public interface TaskManager extends TaskListener {

	/**
	 * Register the specified task.
	 * @param task the task which wants to be managed by the task manager
	 */
	void registerTask(Task task);

	/**
	 * Returns the tasks managed by this task manager.
	 * @return the list of the tasks managed
	 */
	List<? extends Task> getTasks();

	/**
	 * Returns the number of tasks currently running.
	 * @return a positive integer
	 */
	int numberOfRunningTasks();

	/**
	 * Terminates the specified task. The termination can fail if the
	 * task that wants to be terminated has completed in the meantime,
	 * or if that task is not managed by this manager.
	 * @param task task to be terminated
	 * @return <code>true</code> if termination succeeded, 
	 * <code>false</code> otherwise.
	 */
	boolean terminateTask(CancelableTask task);

	/**
	 * Registrira a new task manager listener.
	 * @param l listener
	 */
	void addTaskManagerListener(TaskManagerListener l);

	/**
	 * Unregister an existing task manager listener.
	 * @param l listener
	 */
	void removeTaskManagerListener(TaskManagerListener l);
}