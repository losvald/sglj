/*
 * RemoteCallRequest.java
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An immutable class representing info about remote call.
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public final class RemoteCallRequest implements Serializable {
	
	public static final Object[] NO_ARGS = new Object[0];
	
	private final byte serviceId;
	
	private final String methodName;
	
	private final List<Object> arguments;
	
	private static final long serialVersionUID = -5323062235750569672L;
	
	public RemoteCallRequest(byte serviceId, String methodName, Object... args) {
		List<Object> argList = new ArrayList<Object>(args.length);
		for (int i = 0; i < args.length; ++i)
			argList.add(args[i]);
		
		this.serviceId = serviceId;
		this.methodName = methodName;
		this.arguments = Collections.unmodifiableList(argList);
	}
	
	/**
	 * Returns the id of the service on which the method should be
	 * invoked.
	 * @return the id of the service
	 */
	public byte getServiceId() {
		return serviceId;
	}
	
	/**
	 * Returns the name of the method that should be called.
	 * @return method name
	 */
	public String getMethodName() {
		return methodName;
	}
	
	/**
	 * Returns the unmodifiable list backed by this class containing
	 * arguments that should be passed to the method. This method
	 * is generally preferred over {@link #getArguments()} as it
	 * does not allocate new object(s).
	 * 
	 * @return the list of arguments, in the order specified in the
	 * constructor
	 */
	public List<Object> getArgumentList() {
		return arguments;
	}
	
	/**
	 * Returns the array of arguments containing arguments that should be 
	 * passed to the method. The returned array is a copy, so the caller is 
	 * free to modify it.
	 * 
	 * @return the array of arguments, in the order specified in the
	 * constructor
	 */
	public Object[] getArguments() {
		return arguments.toArray();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		RemoteCallRequest other = (RemoteCallRequest) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (serviceId != other.serviceId)
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result
				+ ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + serviceId;
		return result;
	}
	
	@Override
	public String toString() {
		return "RemoteCallRequest [serviceId=" + serviceId + ", methodName="
				+ methodName + ", arguments=" + arguments + "]";
	}
	
}
