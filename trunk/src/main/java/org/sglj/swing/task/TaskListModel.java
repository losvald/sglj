/*
 * TaskListModel.java
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

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.SwingUtilities;

import org.sglj.task.Task;
import org.sglj.task.TaskEvent;
import org.sglj.task.TaskManager;
import org.sglj.task.TaskManagerListener;
import org.sglj.util.AmortizedRandomAccessQueue;


/**
 * Model for {@link TaskList}.
 * 
 * @author Leo Osvald
 * @version 0.76
 */
public class TaskListModel extends AbstractListModel 
implements TaskManagerListener {
	
	private TaskManager taskManager;
	private List<Task> list = new AmortizedRandomAccessQueue<Task>();
	
	private static final long serialVersionUID = 1L;

	public TaskListModel(TaskManager taskManager) {
		setTaskManager(taskManager);
	}
	
	@Override
	public Task getElementAt(int index) {
		return list.get(index);
	}
	
	@Override
	public int getSize() {
		return list.size();
	}
	
	@Override
	public void taskAdded(TaskEvent e) {
//		pd("TASK ADDED: "+e.getTask());
		list.add(e.getTask());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				fireIntervalAdded(1, list.size()-1, list.size()-1);
			}
		});
	}

	@Override
	public void taskRemoved(final TaskEvent e) {
//		pd("TASK REMOVED: "+e.getTask());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				int index = indexOf(e.getTask());
				if(index == -1) return ;
				list.remove(index);
				fireIntervalRemoved(this, index, index);
			}
		});
	}
	
	@Override
	public void taskStateChanged(TaskEvent e) {
	}

	@Override
	public void taskStatusChanged(TaskEvent e) {
	}
	
	@Override
	public void taskUpdated(final TaskEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				int index = indexOf(e.getTask());
				fireContentsChanged(this, index, index);
			}
		});
	}
	
	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		fireIntervalRemoved(this, 0, list.size()-1);
		list.clear();
		if(this.taskManager != null)
			this.taskManager.removeTaskManagerListener(this);
		
		this.taskManager = taskManager;
		
		if(taskManager != null) {
			taskManager.addTaskManagerListener(this);
			list.addAll(taskManager.getTasks());
			fireIntervalAdded(this, 0, list.size());
		}
	}

	/**
	 * Returns the position of the specified task in this list model. 
	 * @param task task which position is queried
	 * @return the position of the task or -1 if task is not found in the list
	 */
	private int indexOf(Task task) {
		int ind = 0;
		for(Task t : list) {
			if(t == task)
				return ind;
			++ind;
		}
		return -1;
	}
	
//	private void pd(String msg) {
//		System.out.println(msg);
//	}
	
}
