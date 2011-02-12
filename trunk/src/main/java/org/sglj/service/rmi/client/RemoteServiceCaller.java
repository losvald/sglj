/*
 * RemoteServiceCaller.java
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

package org.sglj.service.rmi.client;

import java.util.concurrent.Future;

import org.sglj.service.rmi.RemoteCallResult;
import org.sglj.service.rmi.RemoteService;
import org.sglj.service.rmi.ServiceReflectionCallException;


/**
 * A delegator which delegates method calls to the
 * 
 * @author Leo Osvald
 *
 */
public interface RemoteServiceCaller<S extends RemoteService> {
	
	/**
	 * Calls the
	 * @param methodName
	 * @param args
	 * @return
	 * @throws ServiceReflectionCallException
	 */
	Future<RemoteCallResult> callServiceMethod(String methodName, Object... args)
	throws ServiceReflectionCallException; 
	
	/**
	 * Returns the remote service for which this caller is responsible for 
	 * (i.e. whose methods can be called through this caller).
	 * 
	 * @return the corresponding remote service 
	 */
	S getService();
	
	/**
	 * Returns the request sender to which calls are delegated when
	 * the {@link #callServiceMethod(String, Object...)} is called.
	 * 
	 * @return a request sender
	 */
	RemoteCallRequestSender getRequestSender();
}
