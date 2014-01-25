/*
 * DefaultTaskManager.java
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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * The default implementation of the task manager.<br>
 * This implementation is thread-safe.
 * 
 * @author Leo Osvald
 * @version 1.08
 */
public class DefaultTaskManager extends AbstractTaskManager {
	protected final Object mutex = new Object();
	
	private LinkedHashSet<Task> tasks = new LinkedHashSet<Task>();
	private int numOfRunningTasks;

	@Override
	public void registerTask(Task task) {
		task.addTaskListener(this);
		synchronized (mutex) {
			tasks.add(task);
			fireTaskAdded(task);
			if(task.getState() == Task.TaskState.STARTED)
				++numOfRunningTasks;
		}
	}

	@Override
	public boolean terminateTask(CancelableTask task) {
		//terminate the task
		if(task.cancel()) {
			synchronized (mutex) {
				tasks.remove(task); //if succeeded, remove it from the list
				fireTaskRemoved((Task)task); //and notify the listeners
			}
			return true;
		}
		return false;
		
	}

	@Override
	public List<Task> getTasks() {
		synchronized (mutex) {
			//return a copy, so that tasks cannot be removed/added from the outside
			return new ArrayList<Task>(tasks);
		}
	}

	@Override
	public int numberOfRunningTasks() {
		synchronized (mutex) {
			return numOfRunningTasks;
		}
	}

	@Override
	public void taskStateChanged(TaskEvent e) {
		Task.TaskState state = e.getTask().getState();
		synchronized (mutex) {
			if(state == Task.TaskState.COMPLETED) {
				tasks.remove(e.getTask());
				fireTaskRemoved(e.getTask());
			}
			if(state == Task.TaskState.STARTED)
				++numOfRunningTasks;
			fireTaskStateChanged(e.getTask());
		}
	}

	@Override
	public void taskStatusChanged(TaskEvent e) {
		fireTaskStatusChanged(e.getTask());
	}
	
	@Override
	public void taskUpdated(TaskEvent e) {
		fireTaskUpdated(e.getTask());
	}

}