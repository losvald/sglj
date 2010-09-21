/*
 * ServiceReflectionCallException.java
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

package org.sglj.service.rmi;

/**
 * TODO
 * 
 * @author Leo Osvald
 *
 */
public class ServiceReflectionCallException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	/**
     * Constructs a <code>ServiceReflectionCallException</code> with <tt>null</tt> 
     * as its error message string.
     */
	public ServiceReflectionCallException() {
	}

	/**
     * Constructs a <code>ServiceReflectionCallException</code>, saving a reference 
     * to the error message string <tt>s</tt> for later retrieval by the 
     * <tt>getMessage</tt> method.
     *
     * @param message the detail message.
     */
	public ServiceReflectionCallException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with a wrapped throwable.
	 * 
	 * @param cause exception to be wrapped
	 */
	public ServiceReflectionCallException(Throwable cause) {
		super(cause);
	}

	/**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * <p>Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this exception's detail
     * message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link Throwable#getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link Throwable#getCause()} method).  (A <tt>null</tt> value
     *         is permitted, and indicates that the cause is nonexistent or
     *         unknown.)
	 */
	public ServiceReflectionCallException(String message, Throwable cause) {
		super(message, cause);
	}
}
