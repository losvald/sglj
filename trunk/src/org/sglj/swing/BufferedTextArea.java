/*
 * BufferedTextArea.java
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

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * <p>Text area which has it's own buffer. The size of the buffer is equal
 * to the number of characters that can be displayed at a time. Buffer size
 * can be set via constructor or by calling {@link #setBufferSize(int)}
 * method.<br>
 * If the size is not set explicitly, it will be set to the default value
 * which can be obtained by calling {@link #defaultBufferSize()} method.</p>
 * <p>The buffer size is actually two times bigger than specified;
 * whenever the size exceeds that number, first half of the characters
 * are discarded. 
 * In other words, this component displays at most 2*{@link #getBufferSize()}
 * characters.<br>
 * This strategy ensures that appending takes linear time proportional to
 * the length of the appended text (regardless of buffer size).
 * </p>
 * <p><b>Note:</b> Insertion in the middle is not as efficient as appending
 * to the end.
 * </p>
 * 
 * @author Leo Osvald
 *
 * @version 0.86
 */
public class BufferedTextArea extends JTextArea {	
	
	private int bufferSize = DEFAULT_BUFFER_SIZE;
	private int charCount = 0;
	
	private final DocumentListener docListener = new MyDocumentListener();
	
	private static final int DEFAULT_BUFFER_SIZE = 10000;
	
	private static final long serialVersionUID = 1L;

	public BufferedTextArea(int bufferSize) {
		this.bufferSize = bufferSize;
		getDocument().addDocumentListener(docListener);
	}
	
	public BufferedTextArea() {
		this(DEFAULT_BUFFER_SIZE);
	}
	
	public BufferedTextArea(Document doc) {
		super(doc);
		getDocument().addDocumentListener(docListener);
	}
	
	public BufferedTextArea(String text) {
		super(text);
		charCount = text.length();
		getDocument().addDocumentListener(docListener);
	}
	
	public BufferedTextArea(int rows, int cols) {
		super(rows, cols);
		getDocument().addDocumentListener(docListener);
	}
	
	public BufferedTextArea(String text, int rows, int cols) {
		super(text, rows, cols);
		charCount = text.length();
		getDocument().addDocumentListener(docListener);
	}
	
	@Override
	public void append(String str) {
		//u slucaju da je netko umetao u sredinu documenta i tako prepunio buffer
		cutIfNeeded();
		//normalni slucajevi
		if(str.length() >= bufferSize)
			setText(str);
		else if(charCount < bufferSize) {
			super.append(str);
		}
		else {
			int overflow = charCount + str.length() - 2*bufferSize;
			if(overflow >= 0) {
				int toStay = bufferSize - str.length();
				try {
					getDocument().remove(0, charCount-toStay);
					super.append(str);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
			else {
				super.append(str);
			}
		}
	}
	
	@Override
	public void setText(String t) {
		String s = getLastChars(t);
		super.setText(s);
	}
	
	@Override
	public void setDocument(Document doc) {
//		System.out.println("Postavljam document");
		//remove listener from old document
		if(getDocument() != null)
			getDocument().removeDocumentListener(docListener);
		//set new document and add listener
		super.setDocument(doc);
		getDocument().addDocumentListener(docListener);
	}
	
	public int getBufferSize() {
		return bufferSize;
	}
	
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	
	public int defaultBufferSize() {
		return DEFAULT_BUFFER_SIZE;
	}
	
	private String getLastChars(String s) {
		int len = (s != null ? s.length() : 0);
		if(len <= bufferSize) return s;
		return s.substring(len-bufferSize);
	}
	
	private void cutIfNeeded() {
		if(charCount >= 2*bufferSize)
			try {
				getDocument().remove(0, charCount-bufferSize);
				throw new IllegalStateException("Inserting in the middle is forbidden!");
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
	}
	
	private class MyDocumentListener implements DocumentListener {
		@Override
		public void removeUpdate(DocumentEvent e) {
			charCount = e.getDocument().getLength();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			charCount = e.getDocument().getLength();
			//				cutIfNeeded(); //TODO throws exception Write lock?
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
		}
	}
	
}
