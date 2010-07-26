/*
 * AbstractSingleTaskModel.java
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

import javax.swing.event.EventListenerList;

import org.sglj.task.Task;
import org.sglj.task.TaskEvent;


/**
 * Abstract implementation of interface {@link SingleTaskModel}.
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public abstract class AbstractSingleTaskModel implements SingleTaskModel {
	
	private static final long serialVersionUID = 1L;
	
	private EventListenerList listeners = new EventListenerList();
	private TaskEvent event;
	
	@Override
	public void addSingleTaskModelListener(SingleTaskModelListener l) {
		listeners.add(SingleTaskModelListener.class, l);
	}
	
	@Override
	public void removeSingleTaskModelListener(SingleTaskModelListener l) {
		listeners.remove(SingleTaskModelListener.class, l);
	}
	
	protected void fireTaskChanged(Task task) {
		Object[] listeners = this.listeners.getListenerList();
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==SingleTaskModelListener.class) {
	             if (event == null)
	                 event = new TaskEvent(this);
	             event.setTask(task);
	             ((SingleTaskModelListener)listeners[i+1]).taskChanged(event);
	         }
	     }
	}
	
	protected void fireTaskStateChanged(Task task) {
		Object[] listeners = this.listeners.getListenerList();
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==SingleTaskModelListener.class) {
	             if (event == null)
	                 event = new TaskEvent(this);
	             event.setTask(task);
	             ((SingleTaskModelListener)listeners[i+1]).taskStateChanged(event);
	         }
	     }
	}
	
	protected void fireTaskStatusChanged(Task task) {
		Object[] listeners = this.listeners.getListenerList();
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==SingleTaskModelListener.class) {
	             if (event == null)
	                 event = new TaskEvent(this);
	             event.setTask(task);
	             ((SingleTaskModelListener)listeners[i+1]).taskStatusChanged(event);
	         }
	     }
	}
	
	protected void fireTaskUpdated(Task task) {
		Object[] listeners = this.listeners.getListenerList();
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==SingleTaskModelListener.class) {
	             if (event == null)
	                 event = new TaskEvent(this);
	             event.setTask(task);
	             ((SingleTaskModelListener)listeners[i+1]).taskUpdated(event);
	         }
	     }
	}
}
