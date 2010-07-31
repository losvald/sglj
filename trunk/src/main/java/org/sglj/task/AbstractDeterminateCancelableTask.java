/*
 * AbstractDeterminateCancelableTask.java
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

public abstract class AbstractDeterminateCancelableTask extends 
AbstractDeterminateTask implements CancelableTask {

	private final EventListenerList cancelListeners = new EventListenerList();
	private TaskCancelEvent event;
	
	@Override
	public void addCancelableTaskListener(CancelableTaskListener l) {
		addTaskListener(l);
		addTaskCancelListener(l);
	}

	@Override
	public void addTaskCancelListener(TaskCancelListener l) {
		cancelListeners.add(TaskCancelListener.class, l);
	}

	@Override
	public void removeCancelableTaskListener(CancelableTaskListener l) {
		removeTaskListener(l);
		removeTaskCancelListener(l);
	}

	@Override
	public void removeTaskCancelListener(TaskCancelListener l) {
		cancelListeners.remove(TaskCancelListener.class, l);
	}
	
	protected void fireTaskCanceled() {
		Object[] listeners;
		synchronized (cancelListeners) {
			listeners = this.cancelListeners.getListenerList();
			for (int i = listeners.length-2; i>=0; i-=2) {
				if (listeners[i]==TaskCancelListener.class) {
					if (event == null)
						event = new TaskCancelEvent(this);
					((TaskCancelListener)listeners[i+1]).taskCanceled(event);
				}
			}
		}
	}

}
