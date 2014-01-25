/*
 * SwingTaskProgressListener.java
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

import org.sglj.task.TaskProgressEvent;
import org.sglj.task.TaskProgressListener;


/**
 * All Swing implementations of class {@link TaskProgressListener} that catch 
 * event from a non-EDT thread must subclass this class, to prevent 
 * unexpected behavior.<br>
 * This class handles all events through EDT, and thus provides
 * equivalent replacement methods prefixed with "on". 
 * 
 * @author Leo Osvald
 * @version 1.1
 * @param <T>
 */
public abstract class SwingTaskProgressListener implements TaskProgressListener {

	@Override
	public final void taskProgressChanged(TaskProgressEvent e) {
		final TaskProgressEvent clone = new TaskProgressEvent(e, true);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				onTaskProgressChanged(clone);
			}
		});
	}
	
	public abstract void onTaskProgressChanged(TaskProgressEvent e);

}
