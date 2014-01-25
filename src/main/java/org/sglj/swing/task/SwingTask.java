/*
 * SwingTask.java
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

import javax.swing.SwingWorker;

import org.sglj.task.AbstractCancelableTask;
import org.sglj.task.Task;

public abstract class SwingTask<T, V> extends AbstractCancelableTask {
	
	private SwingWorker<T, V> worker;
	
	public SwingTask(final Task task) {
		worker = new SwingWorker<T, V>() {

			@Override
			protected T doInBackground() throws Exception {
				return SwingTask.this.doInBackground();
			}
			
		};
	}
	
	protected abstract T doInBackground() throws Exception;
	
	@Override
	public boolean cancel() {
		return worker.cancel(true);
	}
	
}
