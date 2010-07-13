/*
 * AbstractDeterminateInterruptibleTask.java
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

public abstract class AbstractDeterminateInterruptibleTask extends
		AbstractDeterminateCancelableTask implements InterruptibleTask {

	private final EventListenerList interruptListeners = new EventListenerList();
	private TaskInterruptEvent event;
	
	@Override
	public void addInterruptibleTaskListener(InterruptibleTaskListener l) {
		addCancelableTaskListener(l);
		addTaskInterruptListener(l);
	}

	@Override
	public void addTaskInterruptListener(TaskInterruptListener l) {
		interruptListeners.add(TaskInterruptListener.class, l);
	}

	@Override
	public void removeInterruptibleTaskListener(InterruptibleTaskListener l) {
		removeCancelableTaskListener(l);
		removeTaskInterruptListener(l);
	}

	@Override
	public void removeTaskInterruptListener(TaskInterruptListener l) {
		interruptListeners.remove(TaskInterruptListener.class, l);
	}

	protected void fireTaskInterrupted() {
		Object[] listeners;
		synchronized (interruptListeners) {
			listeners = this.interruptListeners.getListenerList();
			for (int i = listeners.length-2; i>=0; i-=2) {
				if (listeners[i]==TaskInterruptListener.class) {
					if (event == null)
						event = new TaskInterruptEvent(this);
					((TaskInterruptListener)listeners[i+1]).taskInterrupted(event);
				}
			}
		}
		
	}

}
