/*
 * RemoteCallExecutor.java
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

package org.sglj.service.rmi.server;

import java.lang.reflect.Method;
import java.util.Collection;

import org.sglj.service.rmi.BasicRemoteServiceErrors;
import org.sglj.service.rmi.RemoteCallRequest;
import org.sglj.service.rmi.RemoteCallResult;
import org.sglj.service.rmi.RemoteService;


/**
 * The executor of remote calls.
 * 
 * @author Leo Osvald
 *
 * @param <S> type of the service that can be executed
 * @param <T> type of the remote caller info
 */
public interface RemoteCallExecutor<S extends RemoteService,
T extends RemoteCallerInfo> {
	
	/**
	 * Executes remote method in the context of the specified callerInfo.
	 * The method to be called is determined from the specified request.<br>
	 * This method is guaranteed to never throw any <tt>Exception</tt>;
	 * instead, if any occurs, it will be wrapped in the returned result.<br>
	 * The {@link #setCallerInfo(RemoteCallerInfo)} is automatically
	 * before a call to the corresponding service method, and
	 * after the service method returns (to free memory).<br>
	 * Note that this method also checks whether the method belongs
	 * to this service to prevent unauthorized access.
	 * 
	 * @param callerInfo info about the caller
	 * @param request request which contain information about which
	 * method should be called with which arguments
	 * @return the result of a remote call
	 */
	RemoteCallResult executeServiceMethod(T callerInfo, 
			RemoteCallRequest request);
	
	/**
	 * An optimized variant of the 
	 * {@link #executeServiceMethod(RemoteCallerInfo, RemoteCallRequest)}
	 * method. Use of this method is recommended when the caller
	 * knows exactly what method should be invoked.<br>
	 * Note that this method also checks whether the method belongs
	 * to this service to prevent unauthorized access.
	 * 
	 * @param callerInfo info about the caller
	 * @param request request which contain information about which
	 * method should be called with which arguments
	 * @param method method to be executed
	 * @return the result of a remote call
	 */
	RemoteCallResult executeServiceMethod(T callerInfo,
			RemoteCallRequest request, Method method);
	
	/**
	 * Executes the remote method with the specified name which belongs
	 * to the specified service. The method is invoked with the specified
	 * arguments.<br>
	 * Note that this method also checks whether the method belongs
	 * to this service to prevent unauthorized access.
	 * 
	 * @param callerInfo info about the caller
	 * @param service the implementation class for the remote service which
	 * method should be invoked
	 * @param methodName name of the method that should be executed
	 * @param args arguments to pass to the method
	 * @return the result of a remote call
	 */
	RemoteCallResult executeServiceMethod(T callerInfo, S service,
			String methodName, Object... args);
	
	/**
	 * An optimized variant of the {@link #executeServiceMethod(
	 * RemoteCallerInfo, RemoteService, String, Object...)} method.
	 * Use of this method is recommended when the caller knows exactly 
	 * what method should be invoked.<br>
	 * Note that this method also checks whether the method belongs
	 * to this service to prevent unauthorized access.
	 * 
	 * @param callerInfo info about the caller
	 * @param service the implementation class for the remote service which
	 * method should be invoked
	 * @param method method to be executed
	 * @param args arguments to pass to the method
	 * @return the result of a remote call
	 */
	RemoteCallResult executeServiceMethod(T callerInfo, S service,
			Method method, Object... args);
	
	/**
	 * Checks whether the given remote method can be executed.
	 * If this method returns <code>false</code> trying to execute
	 * it with this executor is guaranteed to fail, and in that case
	 * a wrapped {@link BasicRemoteServiceErrors#PERMISSION_DENIED} error
	 * will be wrapped in the result.
	 * 
	 * @param service the implementation class for the remote service which
	 * method should be invoked
	 * @param method method which should be checked
	 * @param anonymously whether the anonymous access is considered 
	 * @return <code>true</code> if this executor can execute the
	 * specified remote method, <code>false</code> otherwise
	 */
	boolean canExecute(S service, Method method, boolean anonymously);
	
	/**
	 * Returns the thread-local info about caller of this service method.
	 * The returned value will only be valid if this method is called
	 * after a previous call to the 
	 * {@link #executeServiceMethod(RemoteCallerInfo, String, Object...)} method
	 * from the same thread.
	 * 
	 * @param service the service for which the caller info should be retrieved
	 * @return information about the caller of the last called remote
	 * method from this thread
	 */
	T getCallerInfo(S service);
	
	/**
	 * Sets the thread-local info about caller of this service method.
	 * The returned value will only be valid if this method is called
	 * after a previous call to the 
	 * {@link #executeServiceMethod(RemoteCallerInfo, String, Object...)} method
	 * from the same thread.<br>
	 * This method is usually called before a direct call to the service method, 
	 * so that the caller info can be obtained from inside that service method.
	 * 
	 * @param service the service for which the caller info should be set
	 * @param callerInfo info about remote caller. If <code>null</code>
	 * is specified, the caller info should be removed from the
	 * underlying data structure
	 * @return information about the caller of the last called remote
	 * method from this thread
	 */
	void setCallerInfo(S service, T callerInfo);
	
	/**
	 * Returns the collection of methods which belong to 
	 * the specified remote service and can be called remotely,
	 * 
	 * @param serviceId the identifier of the service
	 * @return immutable collection of methods which can be called remotely
	 */
	Collection<Method> getCallableMethods(byte serviceId);
}
