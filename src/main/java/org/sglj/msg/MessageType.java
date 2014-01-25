/*
 * MessageType.java
 * 
 * Copyright (C) 2009-2010 Leo Osvald <leo.osvald@gmail.com>
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
 * Type of the message, which serves as a descriptor.<br>
 * Each class implementing this interface should have distinct
 * {@link #ordinal()}, so that
 * 
 * @see Message
 * 
 * @author Leo Osvald
 * @version 0.3
 */
public interface MessageType {
	
	/**
	 * Logical name of message type, not necessarily unique.
	 * In contrast to {@link #toString()} method, this name
	 * does not need to be user friendly. It is strongly recommended
	 * that this name contains only one word, and no special characters.
	 * @return logical name
	 */
	String name();
	
	/**
	 * This method should return a unique number, so that
	 * different implementations can be.
	 * When called on different instances of the same class,
	 * this method should return the same value. 
	 * @return unique id.
	 */
	int ordinal();
}
