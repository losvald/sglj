/*
 * MessageConsoleView.java
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

package org.sglj.msg;


/**
 * <p>All classes implementing this interface provide a view on 
 * message console.<br>
 * To display contents of some console, it is sufficient to set
 * a console by calling {@link #setMessageConsole(MessageConsole)} method.<br>
 * To display only messages meeting certain criteria, a {@link MessageFilter}
 * can be provided by calling {@link #setMessageFilter(MessageFilter)} method.
 * Messages which are blocked by that filter will not be in any way stored
 * or displayed by the view.
 * <p>A view has also has its own buffer. Buffer size can be changed by 
 * calling {@link #setBufferSize(int)} method, but the implementation of this
 * interface does not need to guarantee that this size will be respected. 
 * </p>
 * 
 * @see MessageConsole
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public interface MessageConsoleView extends MessageHandler {
	/**
	 * Refreshes contents with the contents of the corresponding console.<br>
	 * This method is automatically called in certain (rare) situations,
	 * and need not be called manually by the user.
	 * @param console console which this view should be synchronized with
	 */
	void refresh(MessageConsole console);
	
	/**
	 * Returns the console attached to this view
	 * @return message console
	 */
	MessageConsole getMessageConsole();
	
	/**
	 * Sets the console whose contents this view should display. 
	 * @param messageConsole message console
	 */
	void setMessageConsole(MessageConsole messageConsole);
	
	/**
	 * Returns message separator. This is usually something like '\n'.
	 * @return separator
	 */
	String getMessageSeparator();
	
	/**
	 * Returns buffer size - number of characters that this view can display.
	 * @return number of characters
	 */
	int getBufferSize();
	
	/**
	 * Sets buffer size - number of characters that this view can display.
	 * @param numberOfChars maximum number of characters that can be displayed
	 */
	void setBufferSize(int numberOfChars);
}
