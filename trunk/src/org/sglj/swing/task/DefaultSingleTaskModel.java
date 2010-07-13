/*
 * DefaultSingleTaskModel.java
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
import org.sglj.task.TaskManagerListener;

/**
 * Default implementation of interface {@link SingleTaskModel}.
 * 
 * @author Leo Osvald
 * @version 1.05
 */
public class DefaultSingleTaskModel extends AbstractSingleTaskModel {

	private Task task;
	private TaskListener listener;
	private TaskManagerListener taskMgrListener;

	private TaskManager taskManager;

	@Override
	public Task getTask() {
		return task;
	}

	@Override
	public void setTask(Task task) {
		Task oldTask = this.task;
		//remove old task
		if(oldTask != null)
			oldTask.removeTaskListener(listener);
		//set new task
		if(task != null) {
			listener = new TaskListener() {
				@Override
				public void taskStatusChanged(TaskEvent e) {
					fireTaskStatusChanged(e.getTask());
				}

				@Override
				public void taskStateChanged(TaskEvent e) {
					fireTaskStateChanged(e.getTask());
					if(e.getTask().getState() == Task.TaskState.COMPLETED) {
						listener = null;
					}
				}

				@Override
				public void taskUpdated(TaskEvent e) {
					fireTaskUpdated(e.getTask());
				}
			};
			task.addTaskListener(listener);
		}
		else {
			fireTaskUpdated(task);
		}
		this.task = task;
		//if task actually changed
		if(oldTask != task)
			fireTaskChanged(task);
	}

	@Override
	public void syncWithManager(TaskManager taskManager) {
		//disconnect old manager
		if(this.taskManager != null)
			this.taskManager.removeTaskManagerListener(taskMgrListener);

		//now register as a listener
		if(taskManager != null) {
			setTask(null);
			if(taskMgrListener == null) {
				taskMgrListener = new TaskManagerListener() {

					@Override
					public void taskStatusChanged(TaskEvent e) {
						fireTaskStatusChanged(e.getTask());
					}

					@Override
					public void taskStateChanged(TaskEvent e) {
						fireTaskStateChanged(e.getTask());
					}

					@Override
					public void taskUpdated(TaskEvent e) {
						fireTaskUpdated(e.getTask());
					}

					@Override
					public void taskRemoved(TaskEvent e) {
					}

					@Override
					public void taskAdded(TaskEvent e) {
					}
				};
			}
			this.taskManager = taskManager;
			taskManager.addTaskManagerListener(taskMgrListener);
		}
	}

	@Override
	public TaskManager getManager() {
		return taskManager;
	}

}
