/*
 * FormData.java
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

package org.sglj.swing.util;

import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

/**
 * Utility class which purpose is to store some basic attributes
 * of each Swing component. This comes in handy when a lot of components
 * which are contained in a form or a dialog should save their 
 * basic properties (i.e. text from text field, selection of 
 * a radio button etc.). 
 * The name of the component, obtained by calling {@link Component#getName()}
 * method is used as a key.
 * 
 * The following table shows remembered properties for each component:
 * <table border=1>
 * <th>Type</th>
 * <th>What is stored</th>
 * <tr><td>{@link JTextField}</td>
 * <td>{@link JTextField#getText()}</td>
 * </tr>
 * <tr><td>{@link JTextArea}</td>
 * <td>{@link JTextArea#getText()}</td>
 * </tr>
 * </tr>
 * <tr><td>{@link JComboBox}</td>
 * <td>{@link JComboBox#getSelectedIndex()}</td>
 * </tr>
 * <tr><td>{@link JToggleButton}</td>
 * <td>{@link JToggleButton#isSelected()}</td>
 * </tr>
 * </table>
 * For all components which subclass the one mentioned in the table,
 * the same properties are stored, for example {@link JCheckBox} is stored
 * in the way identical to the one in case of {@link JToggleButton}.
 *
 * 
 * @author Leo Osvald
 * @version 0.41
 */

public class FormData {
	
	protected final Map<String, Object> map;
	
	public FormData() {
		map = new HashMap<String, Object>();
	}
	
	public FormData(FormData clone) {
		map = new HashMap<String, Object>(clone.map());
	}
	
	/**
	 * Returns the map which is used for storage.<br>
	 * All keys are obtained by a call of {@link Component#getName()} method.
	 * @return map
	 */
	private Map<String, Object> map() {
		return map;
	}
	
	protected Object put(Component c, Object what) {
		return map.put(c.getName(), what);
	}
	
	protected Object get(Component c) {
		return map.get(c.getName());
	}
	
	protected boolean contains(Component c) {
		return map.containsKey(c.getName());
	}
	
	/**
	 * Saves the state of the component.
	 * @param c component which property/state should be stored
	 * @return <code>true</code> if storage succeeded,
	 * <code>false</code> otherwise.
	 */
	public boolean store(Component c) {
		if(c == null || c.getName() == null)
			return false;
		if(c instanceof JTextField) {
			put(c, ((JTextField) c).getText());
		} else if(c instanceof JTextArea) {
			put(c, ((JTextArea) c).getText());
		} else if(c instanceof JComboBox) {
			put(c, ((JComboBox) c).getSelectedIndex());
		} else if(c instanceof JToggleButton) {//obuhvaca i JCheckbox
			put(c, ((JToggleButton) c).isSelected());
		}
		//TODO
		else 
			return false;
		
		return true;
	}
	
	/**
	 * Loads saved property/state of the specified component.
	 * @param c the component which should be restored/loaded
	 * @return the specified component, or <code>null</code> if 
	 * nothing is stored for that component.
	 */
	public Component load(Component c) {
		if(!contains(c))
			return null;
		if(c instanceof JTextField) {
			((JTextField) c).setText((String)get(c));
		} else if(c instanceof JTextArea) {
			((JTextArea) c).setText((String)get(c));
		} else if(c instanceof JComboBox) {
			((JComboBox) c).setSelectedIndex((Integer)get(c));
		} else if(c instanceof JToggleButton) {//obuhvaca i JCheckbox
			((JToggleButton) c).setSelected((Boolean)get(c));
		}
		//TODO
		else 
			return null;
		
		return c;
	}
	
	/**
	 * Checks whether the specified component has its state/property
	 * stored.
	 * @param c component
	 * @return <code>true</code> if its state is stored, 
	 * <code>false</code> if not (or if <code>null</code> component is
	 * passed).
	 */
	public boolean hasSavedState(Component c) {
		return c != null && map.containsKey(c.getName());
	}
	
	/**
	 * Loads state of each component in the specified container,
	 * invoking {@link #load(Component)} method on all contained
	 * components.
	 * @param container container which components should be loaded
	 * @param recursive whether this method will recurse to ensure
	 * that components inside one or more containers contained
	 * will be loaded as well 
	 * @return number of loaded components (excluding containers).
	 */
	public int loadAll(Container container, boolean recursive) {
		int cnt = 0;
		if(container != null) {
			for(Component child : container.getComponents()) {
				if(load(child) != null) 
					++cnt;
				if(recursive && child instanceof Container)
					cnt += loadAll((Container)child, recursive);
			}
		}
		return cnt;
	}
	
	/**
	 * Stores state of each component in the specified container,
	 * invoking {@link #store(Component)} method on all contained
	 * components.
	 * @param container container which components should be stored
	 * @param recursive whether this method will recurse to ensure
	 * that components inside one or more containers contained
	 * will be stored as well 
	 * @return number of stored components (excluding containers).
	 */
	public int storeAll(Container container, boolean recursive) {
		int cnt = 0;
		for(Component child : container.getComponents()) {
			if(store(child)) 
				++cnt;
			if(recursive && child instanceof Container)
				cnt += storeAll((Container)child, recursive);
		}
		return cnt;
	}
	
	/**
	 * Clears stored state of each stored component.
	 */
	public void clear() {
		map.clear();
	}
	
}
