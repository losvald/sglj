/*
 * SingleTaskModel.java
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

package org.sglj.swing.task;

import org.sglj.task.Task;
import org.sglj.task.TaskEvent;
import org.sglj.task.TaskListener;
import org.sglj.task.TaskManager;

/**
 * Model intended for display of one task at a time.
 * 
 * @author Leo Osvald
 * @version 1.05
 */
public interface SingleTaskModel {
	
	/**
	 * Sets currently displayed task.
	 * @param task task to be displayed
	 */
	void setTask(Task task);
	
	/**
	 * Gets currently displayed task.
	 * @param task task which is currently displayed
	 */
	Task getTask();
	
	/**
	 * Register new {@link SingleTaskModelListener} listener.
	 * @param l listener
	 */
	void addSingleTaskModelListener(SingleTaskModelListener l);
	
	/**
	 * Unregisters {@link SingleTaskModelListener} listener.
	 * @param l listener
	 */
	void removeSingleTaskModelListener(SingleTaskModelListener l);
	
	/**
	 * Synchronizes with task manager.
	 * @param taskManager task manager
	 */
	void syncWithManager(TaskManager taskManager);
	
	/**
	 * Returns the task manager this model is synchronized with,
	 * or <code>null</code> if there is none.
	 * @return task manager
	 */
	TaskManager getManager();
	
	
	/**
	 * Listener which listens for changes in this model
	 * @author Leo Osvald
	 */
	interface SingleTaskModelListener extends TaskListener {
		void taskChanged(TaskEvent e);
	}
	
}
