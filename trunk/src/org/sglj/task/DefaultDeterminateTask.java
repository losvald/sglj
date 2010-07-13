/*
 * DefaultDeterminateTask.java
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


/**
 * The default implementation of {@link DeterminateTask} interface
 * .
 * @author Leo Osvald
 * @version 1.01
 * @deprecated see {@link AbstractDeterminateTask}
 */
@Deprecated
public class DefaultDeterminateTask extends AbstractDeterminateTask {
	private String name;
	
	public DefaultDeterminateTask(String name, TaskState state) {
		this.name = name;
		super.setState(state);
	}
	
	public DefaultDeterminateTask(String name, boolean started) {
		this(name, started ? TaskState.STARTED : TaskState.NOT_STARTED);
	}
	
	/**
	 * Creates a new task with the specified name, which has
	 * not yet started (has {@link TaskState#NOT_STARTED} state).
	 * @param name name of the task
	 */
	public DefaultDeterminateTask(String name) {
		this.name = name;
	}
	
	public DefaultDeterminateTask() {
		this(null);
	}
	
	@Override
	public String name() {
		return name;
	}
	
	/**
	 * Sets the name to the specified one.
	 * If the name changed, the {@link #fireTaskUpdated()} method.
	 * @param name new name of the task
	 */
	protected void setName(String name) {
		if(this.name == null && name != null
				|| this.name != null && name == null
				|| !this.name.equals(name)) {
			this.name = name;
			fireTaskUpdated();
		}
	}

}
