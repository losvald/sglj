/*
 * FixedLengthPlainDocument.java
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

package org.sglj.swing;

import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * The document which has a contraint on the number of characters it can
 * contain.<br>
 * If that number is exceeded, the system bell will ring.
 * This document usually comes in handy when set on a {@link JTextField}
 * because it ensures that the user cannot type in more then the
 * specified number of characters.
 * 
 * @author Leo Osvald
 *
 * @version 1.0
 */
public class FixedLengthPlainDocument extends PlainDocument {
	private static final long serialVersionUID = 1L;
	
	private int maxLength;
	
	public FixedLengthPlainDocument(int maxLength) {
		this.maxLength = maxLength;
	}
	
	public void insertString(int offs, String str, AttributeSet a)
	throws BadLocationException {
		if(getLength() + str.length() <= maxLength) {
			super.insertString(offs, str, a);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	public int getMaxLength() {
		return maxLength;
	}

}
	
