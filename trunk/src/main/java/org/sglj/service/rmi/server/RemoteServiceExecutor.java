/*
 * RemoteServiceExecutor.java
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

import org.sglj.service.rmi.BasicRemoteServiceErrors;
import org.sglj.service.rmi.RemoteCallResult;
import org.sglj.service.rmi.RemoteService;


/**
 * The executor of the service method.
 * This interface should be implemented by all hosted services.
 * 
 * @author Leo Osvald
 *
 * @param <S> service (usually an interface) which this executor can execute
 * @param <T> the class which represents caller info
 */
public interface RemoteServiceExecutor<S extends RemoteService,
T extends RemoteCallerInfo> 
extends RemoteCallExecutor<S, T>, RemoteService {
	
	/**
	 * Returns the corresponding service which this executor executes.
	 * 
	 * @return service which this executor executes
	 */
	S getService();
	
	/**
	 * Returns the thread-local info about caller of this service method.
	 * The returned value will only be valid if this method is called
	 * after a previous call to the 
	 * {@link #executeServiceMethod(RemoteCallerInfo, String, Object...)} method
	 * from the same thread.
	 * 
	 * @return information about the caller of the last called remote
	 * method from this thread
	 */
	T getCallerInfo();
	
	/**
	 * Sets the thread-local info about caller of this service method.
	 * The returned value will only be valid if this method is called
	 * after a previous call to the 
	 * {@link #executeServiceMethod(RemoteCallerInfo, String, Object...)} method
	 * from the same thread.<br>
	 * This method is usually called before a direct call to the service method, 
	 * so that the caller info can be obtained from inside that service method.
	 * 
	 * @param callerInfo info about remote caller. If <code>null</code>
	 * is specified, the caller info should be removed from the
	 * underlying data structure
	 * @return information about the caller of the last called remote
	 * method from this thread
	 */
	void setCallerInfo(T callerInfo);
	
	/**
	 * Checks whether the given remote method can be executed.
	 * If this method returns <code>false</code>, trying to execute
	 * it with this executor is guaranteed to fail, and in that case
	 * a wrapped {@link BasicRemoteServiceErrors#PERMISSION_DENIED} error
	 * will be wrapped in the result.
	 * 
	 * @param method method which should be checked 
	 * @return <code>true</code> if this executor can execute the
	 * specified remote method, <code>false</code> otherwise
	 */
	boolean canExecute(Method method);

	/**
	 * Check whether the given remote method can be executed if it is called
	 * by a non-authenticated user.
	 * If this method returns <code>false</code>, trying to execute
	 * it with this executor is guaranteed to fail, and in that case
	 * a wrapped {@link BasicRemoteServiceErrors#PERMISSION_DENIED} error
	 * will be wrapped in the result.
	 * 
	 * @param method method which should be checked 
	 * @return <code>true</code> if this executor can execute the
	 * specified remote method, <code>false</code> otherwise
	 */
	boolean canExecuteAnonymously(Method method);
	
	/**
	 * A convenient wrapper for the {@link #executeServiceMethod(
	 * RemoteCallerInfo, RemoteService, Method, Object...)}.
	 * 
	 * @param callerInfo info about the caller
	 * @param method method to be executed
	 * @param args arguments to pass to the method
	 * @return the result of a remote call
	 */
	RemoteCallResult executeServiceMethod(T callerInfo, 
			Method method, Object... args);
	
	/**
	 * A convenient wrapper for the {@link #executeServiceMethod(
	 * RemoteCallerInfo, RemoteService, String, Object...)}.
	 * 
	 * @param callerInfo info about the caller
	 * @param methodName name of the method that should be executed
	 * @param args arguments to pass to the method
	 * @return the result of a remote call
	 */
	RemoteCallResult executeServiceMethod(T callerInfo, 
			String methodName, Object... args);
}
