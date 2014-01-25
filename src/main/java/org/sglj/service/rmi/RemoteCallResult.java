/*
 * RemoteCallResult.java
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

import java.io.Serializable;

/**
 * <p>When a remote call executes, the caller must be somehow notified of 
 * the status of the execution. A remote call can either return nothing 
 * (if it is a procedure call) or can return some result. In certain situations, 
 * a remote call can also fail.</p>
 * <p>This class provides a way to store both the returned value (result)
 * of the call and eventual error (in case of a failure).</p>
 *  
 * @author Leo Osvald
 * @version 1.0
 */
public final class RemoteCallResult implements Serializable {
	
	private Object returnValue;
	private Byte errorCode;
	
	private static final long serialVersionUID = 2226991564519475509L;
	
	/**
	 * Creates a successful result with no return value.
	 */
	public RemoteCallResult() {
	}
	
	/**
	 * Creates a successful result with the specified return value.
	 * 
	 * @param returnValue the value returned by the remote call
	 */
	public RemoteCallResult(Object returnValue) {
		this.returnValue = returnValue;
	}
	
	/**
	 * Creates an unsuccessful result with the specified error.
	 * The return valus is set <code>null</code>.
	 * 
	 * @param error the error returned by the remote call
	 */
	public RemoteCallResult(byte error) {
		this.errorCode = error;
	}
	
	/**
	 * Returns the value returned by the remote call. If the value 
	 * If the call returns nothing, <code>null</code> is returned.
	 * is unsuccessful (result is an error), this method returns
	 * <code>null</code>.
	 * Those two cases can be differentiated by calling
	 * the {@link #isError()} method.
	 * 
	 * @return returned value
	 */
	public Object getReturnValue() {
		return returnValue;
	}
	
	/**
	 * A convenient wrapper for the
	 * {@link #getReturnValue()} which automatically casts the return
	 * type to the expected type.
	 * 
	 * @param <T> type of the expected return
	 * @param clazz expected class of the result
	 * @return returned value
	 * @throws ClassCastException if the return value cannot be cast
	 * to the specified class
	 */
	public <T> T getReturnValue(Class<T> clazz) throws ClassCastException {
		return clazz.cast(returnValue);
	}
	
	/**
	 * Checks whether the result is an error.
	 * 
	 * @return <code>true</code> if the result is an error, <code>false</code>
	 * otherwise
	 */
	public boolean isError() {
		return this.errorCode != null;
	}
	
	/**
	 * Returns the error which this result represents.
	 * If this result does not indicates that there was an error,
	 * the <code>null</code> value returned.
	 * 
	 * @return the error
	 */
	public Byte getError() {
		return errorCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		RemoteCallResult other = (RemoteCallResult) obj;
		if (errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!errorCode.equals(other.errorCode))
			return false;
		if (returnValue == null) {
			if (other.returnValue != null)
				return false;
		} else if (!returnValue.equals(other.returnValue))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime * result
				+ ((returnValue == null) ? 0 : returnValue.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		if (isError())
			return "error#"+getError();
			
		Object retVal = getReturnValue();
		return retVal != null ? retVal.toString() : "(null)";
	}
	
}
