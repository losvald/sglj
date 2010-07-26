/*
 * AbstractDeterminateTask.java
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
 * TODO documentation
 * <br>
 * This implementation is thread-safe.
 * 
 * @author Leo Osvald
 * @version 1.07
 */
public abstract class AbstractDeterminateTask extends AbstractTask 
implements DeterminateTask {

	private final EventListenerList listeners = new EventListenerList();
	private TaskProgressEvent event;
	
	private volatile double progress;
	private volatile double sensitivity = DEFAULT_SENSITIVITY;
	
	protected static final double DEFAULT_SENSITIVITY = 0.005;
	
	@Override
	public void addDeterminedTaskListener(DeterminateTaskListener l) {
		addTaskListener(l);
		addTaskProgressListener(l);
	}
	
	@Override
	public void addTaskProgressListener(TaskProgressListener l) {
		listeners.add(TaskProgressListener.class, l);
	}

	@Override
	public double getProgress() {
		return progress;
	}

	@Override
	public double getSensitivity() {
		return sensitivity;
	}

	@Override
	public void removeDeterminedTaskListener(DeterminateTaskListener l) {
		removeTaskListener(l);
		removeTaskProgressListener(l);
	}
	
	@Override
	public void removeTaskProgressListener(TaskProgressListener l) {
		listeners.remove(TaskProgressListener.class, l);
	}

	@Override
	public boolean setProgress(double value) {
		if(Math.abs(value-progress) >= getSensitivity()) {
			this.progress = value;
			fireTaskProgressChanged();
			fireTaskUpdated();
			return true;
		}
		return false;
	}

	@Override
	public void setSensitivity(double value) {
		this.sensitivity = value;
	}
	
	protected synchronized void fireTaskProgressChanged() {
		synchronized (this.listeners) {
			Object[] listeners = this.listeners.getListenerList();
			for (int i = listeners.length-2; i>=0; i-=2) {
				if (listeners[i]==TaskProgressListener.class) {
					if (event == null)
			            event = new TaskProgressEvent(this);
					event.setTask(this);
					((TaskProgressListener)listeners[i+1]).taskProgressChanged(event);
				}
			}
		}
	}
	
}
