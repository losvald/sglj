/*
 * TaskList.java
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

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

/**
 * {@link JList} extension for displaying task lists.
 * 
 * @author Leo Osvald
 * @version 0.81
 */
public class TaskList extends JList {
	
	private TaskListModel model;
	
	private static final long serialVersionUID = 1L;
	
	public TaskList(TaskListModel model, ListCellRenderer taskListCellRenderer) {
		setCellRenderer(taskListCellRenderer);
		setModel(model);
	}
	
	public TaskListModel getTaskListModel() {
		return model;
	}
	
	@Override
	public void setModel(ListModel model) {
		if(!(model instanceof TaskListModel))
			throw new IllegalArgumentException("Wrong model");
		super.setModel((ListModel)model);
	}
	
}
