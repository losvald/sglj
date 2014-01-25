/*
 * IncrementalSearchBar.java
 * 
 * Copyright (C) 2009 Leo Osvald <leo.osvald@gmail.com>
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

package org.sglj.swing.search;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComponent;

import org.sglj.search.IncrementalSearchModel;


//TODO not yet implemented
@SuppressWarnings("all")
public class IncrementalSearchBar<T> extends JComponent {
	

	private IncrementalSearchBox<T> box;
	
	private JButton buttonRewind;
	private JButton buttonPrevious;
	private JButton buttonNext;
	private JButton buttonFind;
	
	private IncrementalSearchModel<T> model;
	
	public IncrementalSearchBar() {
		// TODO Auto-generated constructor stub
		initGUI();
	}
	
	private void createButtons() {
		buttonFind = new JButton("Find");
		buttonRewind = new JButton("Rewind");
		//TODO
	}
	
	private void initGUI() {
		
		setPreferredSize(new Dimension());
		
		//TODO
	}
	
	public void setModel(IncrementalSearchModel<T> model) {
		box.setModel(model);
	}
	
}
