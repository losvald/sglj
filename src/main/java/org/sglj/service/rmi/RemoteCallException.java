/*
 * RemoteCallException.java
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

import org.sglj.service.rmi.annotations.RemoteCallExceptionThrower;

/**
 * The exception which is thrown by the remote method and is transferred
 * through the network. The type of the exception is defined by the
 * exception code, which can be obtained by calling the
 * {@link #getExceptionCode()} method.<br> 
 * This code can practically eliminate the need of defining 
 * the subclasses of this exception and thus enforce the cross-language
 * communication, where exceptions are identified not by their class
 * but by their code. So, instead of subclassing this class and 
 * adding often many of them to the method signatures, one can
 * simply indicate that a method throws a {@link RemoteCallException}
 * and then specify which types of remote exceptions can be thrown by using the
 * {@link RemoteCallExceptionThrower} annotation.
 * 
 * @author Leo Osvald
 * 
 */
public class RemoteCallException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final Byte exceptionCode;
	
	/**
     * Constructs a <code>RemoteCallException</code> with <tt>null</tt> 
     * as its error message string.
     * 
     * @param exceptionCode the code which indicates the type of the remote
	 * call exception
     */
	public RemoteCallException(Byte exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	/**
     * Constructs a <code>RemoteCallException</code>, saving a reference 
     * to the error message string <tt>s</tt> for later retrieval by the 
     * <tt>getMessage</tt> method.
     *
     * @param message the detail message.
     * @param exceptionCode the code which indicates the type of the remote
	 * call exception
     */
	public RemoteCallException(String message, Byte exceptionCode) {
		super(message);
		this.exceptionCode = exceptionCode;
	}

	/**
	 * Constructs a new exception with a wrapped throwable.
	 * 
	 * @param cause exception to be wrapped
	 * @param exceptionCode the code which indicates the type of the remote
	 * call exception
	 */
	public RemoteCallException(Throwable cause, Byte exceptionCode) {
		super(cause);
		this.exceptionCode = exceptionCode;
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
     * @param exceptionCode the code which indicates the type of the remote
	 * call exception
	 */
	public RemoteCallException(String message, Throwable cause, 
			Byte exceptionCode) {
		super(message, cause);
		this.exceptionCode = exceptionCode;
	}
	
	/**
	 * Returns the constant which represents the exception.
	 * 
	 * @param exceptionCode the code which indicates the type of the remote
	 * call exception
	 */
	public Byte getExceptionCode() {
		return exceptionCode;
	}
	
}
