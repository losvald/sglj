/*
 * IncrementalSearchBox.java
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

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.sglj.search.IncrementalSearchModel;


public class IncrementalSearchBox<T> extends JTextField {
	private static final long serialVersionUID = 1L;

	/** model which is does the incremental search */
	private IncrementalSearchModel<T> model;
	
	public IncrementalSearchBox(IncrementalSearchModel<T> model) {
		// TODO Auto-generated constructor stub
		setModel(model);
		
		this.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				IncrementalSearchBox.this.model.continueFind(getText());
				System.out.println("[IF DOCUMENT REMOVE EVENT]" + e);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				IncrementalSearchBox.this.model.continueFind(getText());
				System.out.println("[IF DOCUMENT INSERT EVENT]" + e);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
			
		});
		
		initGUI();
		
	}
	
	public void setModel(IncrementalSearchModel<T> model) {
		this.model = model;
	}
	
	private void initGUI() {
		
	}
	
	public void appendChar(char c) {
		try {
			getDocument().insertString(getDocument().getLength(), ""+c, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public boolean removeLastChar() {
		try {
			getDocument().remove(getDocument().getLength()-1, 1);
			return true;
		} catch (BadLocationException e) {
			return false;
		}
	}
	
}
