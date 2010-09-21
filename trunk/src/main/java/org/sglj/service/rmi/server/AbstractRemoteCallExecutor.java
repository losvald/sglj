/*
 * AbstractRemoteCallExecutor.java
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.reflect.MethodUtils;
import org.sglj.service.rmi.BasicRemoteServiceErrors;
import org.sglj.service.rmi.RemoteCallException;
import org.sglj.service.rmi.RemoteCallRequest;
import org.sglj.service.rmi.RemoteCallResult;
import org.sglj.service.rmi.RemoteService;


/**
 * The abstract implementation of the {@link RemoteCallExecutor} interface.
 * This implementation allows implementing the executor for remote calls
 * with minimal efforts.
 * 
 * @author Leo Osvald
 *
 * @param <S> type of the service that can be executed
 * @param <T> type of the remote caller info
 */
public abstract class AbstractRemoteCallExecutor<S extends RemoteService,
T extends RemoteCallerInfo>
implements RemoteCallExecutor<S, T> {
	
	@Override
	public RemoteCallResult executeServiceMethod(T callerInfo, S service,
			Method method, Object... args) {
		try {
			boolean anonymously = true;
			if (callerInfo != null) {
				setCallerInfo(service, callerInfo);
				anonymously = !callerInfo.isAuthenticated();
			}
			if (!canExecute(service, method, anonymously)) {
				return new RemoteCallResult(BasicRemoteServiceErrors
						.PERMISSION_DENIED);
			}
			return new RemoteCallResult(method.invoke(service, args));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return new RemoteCallResult(BasicRemoteServiceErrors.INVALID_DATA_SENT);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			Throwable cause = e.getCause();
			if (cause instanceof RemoteCallException) { 
				return new RemoteCallResult(((RemoteCallException) cause)
						.getExceptionCode());
			}
			return new RemoteCallResult(BasicRemoteServiceErrors.INVALID_DATA_SENT);
		} catch (Exception e) {
			// some serious error
			e.printStackTrace();
			return new RemoteCallResult(BasicRemoteServiceErrors.UNKNOWN_ERROR);
		} finally {
			if (callerInfo != null)
				setCallerInfo(service, null);
		}
	}
	
	@Override
	public RemoteCallResult executeServiceMethod(T callerInfo, S service,
			String methodName, Object... args) {
		Class<?>[] paramTypes = new Class<?>[args == null ? 0 : args.length];
		for(int i = 0; i < paramTypes.length; ++i)
			paramTypes[i] = args[i].getClass();
		
		try {
			Method m = MethodUtils.getMatchingAccessibleMethod(
					service.getClass(), methodName, paramTypes);
			boolean anonymously = true;
			if (callerInfo != null) {
				setCallerInfo(service, callerInfo);
				anonymously = !callerInfo.isAuthenticated();
			}
			if (!canExecute(service, m, anonymously)) {
				return new RemoteCallResult(BasicRemoteServiceErrors
						.PERMISSION_DENIED);
			}
			
			return new RemoteCallResult(m.invoke(service, args));
		} catch (NullPointerException e) {
			e.printStackTrace();
			return new RemoteCallResult(BasicRemoteServiceErrors.PERMISSION_DENIED);
		} catch (NoSuchMethodError e) {
			e.printStackTrace();
			return new RemoteCallResult(BasicRemoteServiceErrors.PERMISSION_DENIED);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return new RemoteCallResult(BasicRemoteServiceErrors.INVALID_DATA_SENT);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			Throwable cause = e.getCause();
			if (cause instanceof RemoteCallException) { 
				return new RemoteCallResult(((RemoteCallException) cause)
						.getExceptionCode());
			}
			return new RemoteCallResult(BasicRemoteServiceErrors.INVALID_DATA_SENT);
		} catch (Exception e) {
			// some serious error
			e.printStackTrace();
			return new RemoteCallResult(BasicRemoteServiceErrors.UNKNOWN_ERROR);
		} finally {
			if (callerInfo != null)
				setCallerInfo(service, null);
		}
	}
	
	@Override
	public RemoteCallResult executeServiceMethod(T callerInfo, 
			RemoteCallRequest request) {
		S service = getRemoteService(request.getServiceId());
		if (service == null) {
			return new RemoteCallResult(BasicRemoteServiceErrors.PERMISSION_DENIED);
		}
		return executeServiceMethod(callerInfo, 
				service, 
				request.getMethodName(),
				request.getArguments());
	}
	
	public RemoteCallResult executeServiceMethod(T callerInfo, 
			RemoteCallRequest request, Method method) {
		S service = getRemoteService(request.getServiceId());
		if (service == null) {
			return new RemoteCallResult(BasicRemoteServiceErrors.PERMISSION_DENIED);
		}
		return executeServiceMethod(callerInfo, 
				service, 
				method, 
				request.getArguments());
	}
	
	/**
	 * Returns the service with the specified identifier.
	 * 
	 * @param serviceId the identifier of the service
	 * @return the service which identifier is equal to the specified
	 * one or <code>null</code> if none is.
	 */
	protected abstract S getRemoteService(byte serviceId);

}
