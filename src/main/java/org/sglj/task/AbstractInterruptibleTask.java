/*
 * AbstractInterruptibleTask.java
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

public abstract class AbstractInterruptibleTask extends AbstractCancelableTask
implements InterruptibleTask {
	private final EventListenerList listeners = new EventListenerList();
	private TaskEvent event;
	
	@Override
	public void addTaskInterruptListener(TaskInterruptListener l) {
		listeners.add(TaskInterruptListener.class, l);
	}

	@Override
	public void removeTaskInterruptListener(TaskInterruptListener l) {
		listeners.remove(TaskInterruptListener.class, l);
	}
	
	@Override
	public void addInterruptibleTaskListener(InterruptibleTaskListener l) {
		addTaskListener(l);
		addTaskInterruptListener(l);
	}
	
	@Override
	public void removeInterruptibleTaskListener(InterruptibleTaskListener l) {
		removeTaskListener(l);
		removeTaskInterruptListener(l);
	}
	
	protected void fireTaskInterrupted() {
		Object[] listeners = this.listeners.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TaskInterruptListener.class) {
				if (event == null)
					event = new TaskEvent(this);
				((TaskInterruptListener)listeners[i+1]).taskInterrupted(event);
			}
		}
	}

}
