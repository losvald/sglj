/*
 * DefaultTask.java
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
 * The default implementation of the task.<br>
 * Implemented method include setters and getters which
 * fire appropriate events if the value is changed.
 * 
 * @author Leo Osvald
 * @version 1.1
 */
public class DefaultTask extends AbstractTask {

	private String name;

	public DefaultTask(String name, TaskState state) {
		this.name = name;		
	}

	/**
	 * Creates a new task with the spacified name and state.
	 * @param name the name of the task 
	 * @param started <code>true</code> =  {@link TaskState#STARTED},
	 * <code>false</code> = {@link TaskState#NOT_STARTED}
	 */
	public DefaultTask(String name, boolean started) {
		this(name, started ? TaskState.STARTED : TaskState.NOT_STARTED);
	}

	/**
	 * Creates a new task with the specified name, with its state
	 * set to {@link TaskState#NOT_STARTED}.
	 * @param name the name of the task
	 */
	public DefaultTask(String name) {
		this(name, TaskState.NOT_STARTED);
	}

	public DefaultTask() {
		this(null);
	}

	/**
	 * {@inheritDoc}
	 */
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
