/*
 * MessageFilter.java
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
 * Message filter.<br>
 * 
 * @author Leo Osvald
 * @version 0.8
 */
public interface MessageFilter {
	
	/**
	 * This method is automatically called by various implementations
	 * of interfaces {@link MessageHandler} and {@link MessagePublisher},
	 * to determine which message should be received/displayed, or sent.
	 * @param message message which is checked
	 * @return <code>true</code> if message needs to be rejected, 
	 * <code>false</code> otherwise.
	 */
	boolean accept(Message message);
}
