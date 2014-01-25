/*
 * AbstractTask.java
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

import javax.swing.event.EventListenerList;

/**
 * Abstract implementation of the {@link Task} interface.<br>
 * TODO documentation
 * <br>
 * This implementation is thread-safe.
 * 
 * @author Leo Osvald
 * @version 1.1
 */
public abstract class AbstractTask implements Task {
	private final EventListenerList listeners = new EventListenerList();
	private final TaskEvent event = new TaskEvent(this);
	
	private TaskState state = TaskState.NOT_STARTED;
	private TaskStatus status;
	
	private final Object statusMutex = new Object();
	private final Object stateMutex = new Object();
	
	@Override
	public TaskState getState() {
		synchronized (stateMutex) {			
			return state;
		}
	}
	
	@Override
	public void setState(TaskState state) {
		synchronized (stateMutex) {
			if(state != this.state) {
				this.state = state;
				fireTaskStateChanged();
				fireTaskUpdated();
			}
		}
	}
	
	@Override
	public TaskStatus getStatus() {
		synchronized (statusMutex) {			
			return status;
		}
	}
	
	@Override
	public void setStatus(TaskStatus status) {
		synchronized (statusMutex) {
			if(status != this.status) {
				this.status = status;
				fireTaskStatusChanged();
				fireTaskUpdated();
			}
		}
	}
	
	@Override
	public void addTaskListener(TaskListener l) {
		listeners.add(TaskListener.class, l);
		synchronized (event) {
			if(getState() == TaskState.STARTED) {
				fireTaskStateChanged(l);
			}
		}
	}

	@Override
	public void removeTaskListener(TaskListener l) {
		listeners.remove(TaskListener.class, l);
	}
	
	protected void fireTaskStatusChanged() {
		Object[] listeners;
		synchronized (event) {
			listeners = this.listeners.getListenerList();
			for (int i = listeners.length-2; i>=0; i-=2) {
				if (listeners[i]==TaskListener.class) {
					((TaskListener)listeners[i+1]).taskStatusChanged(event);
				}
			}
		}
	}
	
	protected void fireTaskStateChanged() {
		Object[] listeners;
		synchronized (event) {
			listeners = this.listeners.getListenerList();
		    for (int i = listeners.length-2; i>=0; i-=2) {
		         if (listeners[i]==TaskListener.class) {
		             fireTaskStateChanged((TaskListener)listeners[i+1]);
		         }
		     }
		}
	}
	
	protected void fireTaskUpdated() {
		Object[] listeners;
		synchronized (event) {
			listeners = this.listeners.getListenerList();
			for (int i = listeners.length-2; i>=0; i-=2) {
				if (listeners[i]==TaskListener.class) {
					((TaskListener)listeners[i+1]).taskUpdated(event);
				}
			}
		}
	}
	
	private void fireTaskStateChanged(TaskListener l) {
        event.setTask(this);
        l.taskStateChanged(event);
	}
	
}

