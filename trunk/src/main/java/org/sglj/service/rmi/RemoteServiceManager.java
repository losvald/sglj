/*
 * RemoteServiceManager.java
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

import java.lang.reflect.Method;
import java.util.List;

import org.sglj.service.rmi.RemoteService;
import org.sglj.service.rmi.annotations.RemoteMethod;


/**
 * A manager that keeps track of the available services.<br>
 * These are the restrictions that apply to the managed remote services:
 * <ul>
 * <li>No two remote services can have the same identifier</li>
 * <li>None of the services can have two remote methods (annotated
 * with {@link RemoteMethod} annotation) with the same name and the same
 * number of arguments. Although this restriction may seem strange at the
 * first glance, it makes sense when remote call data which is intended
 * to be cross-language does not have the exact information on the
 * type of the arguments.</li>
 * </ul>
 * 
 * @author Leo Osvald
 *
 * @param <T> type of the remote services that this manager manages
 */
public interface RemoteServiceManager<T extends RemoteService> {
	
	/**
	 * Returns the unmodifiable list of all registered remote services.
	 * @return unmodifiable list of services
	 */
	List<T> availableServices();
	
	/**
	 * Returns the service with the specified identifier.
	 * @param serviceId identifier
	 * @return the service which matches the specified identifier or
	 * <code>null</code> if none matches.
	 */
	T getService(byte serviceId);
	
	/**
	 * Checks whether the remote service identified by <value>serviceId</value>
	 * offers the method named <value>methodName</value> with 
	 * <value>parameterCount</value> parameters.
	 * 
	 * @param serviceId identifier of the service
	 * @param methodName the name of the remote method
	 * @param parameterCount the number of parameters that the remote method has
	 * @return <code>true</code> if and only if the following is met:
	 * <ol>
	 * <li>service with the specified identifier exists</li>
	 * <li>service has the method named <value>methodName</value> that
	 * takes exactly <value>parameterCount</value> arguments</li>
	 * <li>that method is annotated with {@link RemoteMethod} annotation</li>
	 * </ol>
	 * If any of the above requirement is not met, <code>false</code>
	 * will be returned.
	 */
	boolean existsRemoteMethod(byte serviceId, String methodName,
			int parameterCount);
	
	/**
	 * Returns the method named <value>methodName</value> which
	 * takes <value>paramCount</value> arguments and is offered by 
	 * the remote service identified by <value>serviceId</value>.
	 * 
	 * @param serviceId identifier of the service
	 * @param methodName the name of the remote method
	 * @param parameterCount the number of parameters that the remote method has
	 * @return a <tt>Method</tt> instance is returned if and only if the 
	 * following is met:
	 * <ol>
	 * <li>service with the specified identifier exists</li>
	 * <li>service has the method named <value>methodName</value> that
	 * takes exactly <value>parameterCount</value> arguments</li>
	 * <li>that method is annotated with {@link RemoteMethod} annotation</li>
	 * </ol>
	 * If any of the above requirement is not met, <code>null</code>
	 * will be returned.
	 */
	Method getRemoteMethod(byte serviceId, String methodName,
			int parameterCount);
	
}
