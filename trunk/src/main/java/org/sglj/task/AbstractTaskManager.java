/*
 * AbstractTaskManager.java
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
 * Abstract implementation of the {@link TaskManager} interface.<br>
 * This implementation is thread-safe.
 * 
 * @author Leo Osvald
 * @version 1.03
 */
public abstract class AbstractTaskManager implements TaskManager {
	
	private EventListenerList listeners = new EventListenerList();
	private TaskEvent event;
	
	@Override
	public void addTaskManagerListener(TaskManagerListener l) {
		listeners.add(TaskManagerListener.class, l);
	}
	
	@Override
	public void removeTaskManagerListener(TaskManagerListener l) {
		listeners.remove(TaskManagerListener.class, l);
	}
	
	protected void fireTaskAdded(Task task) {
		synchronized (this.listeners) {
			Object[] listeners = this.listeners.getListenerList();
		    for (int i = listeners.length-2; i>=0; i-=2) {
		         if (listeners[i]==TaskManagerListener.class) {
		             if (event == null)
		                 event = new TaskEvent(this);
		             event.setTask(task);
		             ((TaskManagerListener)listeners[i+1]).taskAdded(event);
		         }
		     }
		}
	}
	
	protected void fireTaskRemoved(Task task) {
		synchronized (this.listeners) {
			Object[] listeners = this.listeners.getListenerList();
			for (int i = listeners.length-2; i>=0; i-=2) {
		         if (listeners[i]==TaskManagerListener.class) {
		             if (event == null)
		                 event = new TaskEvent(this);
		             event.setTask(task);
		             ((TaskManagerListener)listeners[i+1]).taskRemoved(event);
		         }
		     }
		}
	}
	
	protected void fireTaskStateChanged(Task task) {
		synchronized (this.listeners) {
			Object[] listeners = this.listeners.getListenerList();
		    for (int i = listeners.length-2; i>=0; i-=2) {
		         if (listeners[i]==TaskManagerListener.class) {
		             if (event == null)
		                 event = new TaskEvent(this);
		             event.setTask(task);
		             ((TaskManagerListener)listeners[i+1]).taskStateChanged(event);
		         }
		     }
		}
	}
	
	protected void fireTaskStatusChanged(Task task) {
		synchronized (this.listeners) {
			Object[] listeners = this.listeners.getListenerList();
		    for (int i = listeners.length-2; i>=0; i-=2) {
		         if (listeners[i]==TaskManagerListener.class) {
		             if (event == null)
		                 event = new TaskEvent(this);
		             event.setTask(task);
		             ((TaskManagerListener)listeners[i+1]).taskStatusChanged(event);
		         }
		     }
		}
	}
	
	protected void fireTaskUpdated(Task task) {
		synchronized (this.listeners) {
			Object[] listeners = this.listeners.getListenerList();
		    for (int i = listeners.length-2; i>=0; i-=2) {
		         if (listeners[i]==TaskManagerListener.class) {
		             if (event == null)
		                 event = new TaskEvent(this);
		             event.setTask(task);
		             ((TaskManagerListener)listeners[i+1]).taskUpdated(event);
		         }
		     }
		}
	}
	
}

