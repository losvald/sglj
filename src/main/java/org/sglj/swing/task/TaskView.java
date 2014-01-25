/*
 * TaskView.java
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

import org.sglj.task.Task;

/**
 * View that displays one or more task.
 * 
 * @author Leo Osvald
 *
 */
public interface TaskView {
	
	void setNameProvider(NameTextProvider nameTextCreator);
	
	NameTextProvider getNameTextProvider();
	
	StatusTextProvider getStatusTextProvider();
	
	void setStatusTextProvider(StatusTextProvider statusTextCreator);
	
	/**
	 * Provider/factory for task name text.
	 * @author Leo Osvald
	 *
	 */
	public interface NameTextProvider {
		String friendlyName(Task task);
	}
	
	/**
	 * Provider/factory for task status text.
	 * 
	 * @author Leo Osvald
	 * 
	 */
	public interface StatusTextProvider {
		String statusDescription(Task task);
	}
	
}
