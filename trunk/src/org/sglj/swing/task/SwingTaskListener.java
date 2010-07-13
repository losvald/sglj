/*
 * SwingTaskListener.java
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

import javax.swing.SwingUtilities;

import org.sglj.task.TaskEvent;
import org.sglj.task.TaskListener;


/**
 * All Swing implementations of class {@link TaskListener} that catch 
 * event from a non-EDT thread must subclass this class, to prevent 
 * unexpected behavior.<br>
 * This class handles all events through EDT, and thus provides
 * equivalent replacement methods prefixed with "on". 
 * 
 * @author Leo Osvald
 * @version 1.11
 * @param <T>
 */
public abstract class SwingTaskListener implements TaskListener {
	
	@Override
	public final void taskUpdated(TaskEvent e) {
		final TaskEvent clone = new TaskEvent(e, true);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				onTaskUpdated(clone);
			}
		});
	}
	
	@Override
	public final void taskStateChanged(TaskEvent e) {
		final TaskEvent clone = new TaskEvent(e, true);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				onTaskStateChanged(clone);
			}
		});
	}

	@Override
	public final void taskStatusChanged(TaskEvent e) {
		final TaskEvent clone = new TaskEvent(e, true);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				onTaskStatusChanged(clone);
			}
		});
	}
	
	protected abstract void onTaskUpdated(TaskEvent e);	
	protected abstract void onTaskStateChanged(TaskEvent e);
	protected abstract void onTaskStatusChanged(TaskEvent e);

}
