/*
 * DynamicContentDialog.java
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

package org.sglj.swing.dialog;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * Dialog which contents can be changed easily.
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public abstract class DynamicContentDialog extends MyDialog {

	protected final JPanel contentPane = new JPanel(new BorderLayout());
	
	private JComponent currContent;
	private JComponent leftComp;
	private JComponent rightComp;
	
	private boolean contentPaneSet = false;
	
	private static final long serialVersionUID = 1L;
	
	public DynamicContentDialog(Frame frame, String title, 
			boolean setOptionPaneNow) {
		super(frame);
		if(setOptionPaneNow)
			setOptionPane(createOptionPane());
		setTitle(title);
	}
	
	public void setLeft(JComponent comp) {
		if(!contentPaneSet)
			return ;
		if(this.leftComp != null)
			contentPane.remove(this.leftComp);
		if(comp != null)
			contentPane.add(comp, BorderLayout.WEST);
		this.leftComp = comp;
		getRootPane().validate();
	}
	
	public void setRight(JComponent comp) {
		if(!contentPaneSet)
			return ;
		if(this.rightComp != null)
			contentPane.remove(this.rightComp);
		if(comp != null)
			contentPane.add(comp, BorderLayout.EAST);
		this.rightComp = comp;
		getRootPane().validate();
	}
	
	@Override
	protected void handleOption(Object option) {
		System.out.println("User option: " + option + "(instance of Integer?"
				+ (option instanceof Integer) + ")");
		//ako je zatvoren dijalog, nista
		if(option == null) return ;
		//inace, pogledaj sta je kliknuto
		if(option.equals(JOptionPane.CLOSED_OPTION)) {
			onClose();
		}
	}
	
	@Override
	public void setOptionPane(JOptionPane optionPane) {
		if(optionPane == null)
			contentPaneSet = false;
		else {
			if(optionPane.getMessage() != contentPane) {
				contentPaneSet = false;
				throw new IllegalArgumentException("Invalid message.");
			}
			contentPaneSet = true;
		}
		super.setOptionPane(optionPane);
	}
	
	/**
	 * Creates option pane. For message parameter,
	 * {@link #contentPane} field <b>must</b> be passed. 
	 * @return created option pane
	 */
	protected abstract JOptionPane createOptionPane();
	
	/**
	 * Returns the content of this dialog.
	 * @return component
	 */
	protected JComponent getContent() {
		return currContent;
	}
	
	/**
	 * Sets the specified component as the content of this dialog.
	 * 
	 * @param content component 
	 */
	protected void setContent(JComponent content) {
		if(!contentPaneSet)
			return ;
		if(currContent != null)
			contentPane.remove(currContent);
		contentPane.add(currContent = content, BorderLayout.CENTER);
		getRootPane().validate();
		
		//for debugging purposes
		System.out.println("ContentPane comps: ");
		for(Component c : contentPane.getComponents())
			System.out.println(c);
		System.out.println();
	}
	
	

}
