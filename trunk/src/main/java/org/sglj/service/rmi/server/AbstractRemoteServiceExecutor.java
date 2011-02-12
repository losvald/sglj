/*
 * AbstractRemoteServiceExecutor.java
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.sglj.service.rmi.RemoteCallResult;
import org.sglj.service.rmi.RemoteService;
import org.sglj.service.rmi.annotations.RemoteMethod;


/**
 * Abstract implementation of the {@link RemoteServiceExecutor} interface.
 * It implements all generic methods through reflection, and offers
 * a certain level of method cache.<br>
 * Also, methods {@link #setCallerInfo(RemoteCallerInfo)} and
 * {@link #getCallerInfo()} are implemented in such a way to be useful
 * in multi-threaded environment, where methods are called from many
 * threads. The caller info is, therefore, stored in a
 * {@link ThreadLocal} variable.
 * 
 * @author Leo Osvald
 *
 * @param <S> service which is implemented by this executor (can
 * be an interface)
 * @param <T> type of the info about remote caller
 */
public abstract class AbstractRemoteServiceExecutor<S extends RemoteService,
T extends RemoteCallerInfo> 
extends AbstractRemoteCallExecutor<S, T>
implements RemoteServiceExecutor<S, T> {

	/**
	 * Set of all remote methods that can be executed.
	 */
	private final Set<Method> remoteMethods;
	
	/**
	 * Set of all remote methods that have the
	 * {@link RemoteMethod#anonymousAccess()} set to <code>true</code>
	 */
	private final Set<Method> remoteAnonymousMethods;
	
	private final ThreadLocal<T> callerInfoThreadLocal = new ThreadLocal<T>();
	
	/**
	 * Creates a service based on the specified implementation. 
	 * 
	 * @param clazz the class that implements the given service
	 */
	public AbstractRemoteServiceExecutor(Class<? extends S> clazz) {
		
		// cache methods that can be executed
		Method[] methods = clazz.getDeclaredMethods();
		Set<Method> all = new HashSet<Method>();
		Set<Method> anonymous = new HashSet<Method>();
		for (Method m : methods) {
			if (!m.isAccessible()) {
				try {
					m.setAccessible(true);
				} catch (SecurityException ignorable) {
				}
			}
			RemoteMethod annot = m.getAnnotation(RemoteMethod.class);
			if (annot != null && annot.value() == RemoteMethod.RemoteMethodSide
					.EXECUTOR) {
				all.add(m);
				if (annot.anonymousAccess()) {
					anonymous.add(m);
				}
			}
		}
		this.remoteMethods = Collections.unmodifiableSet(all);
		this.remoteAnonymousMethods = Collections.unmodifiableSet(anonymous);
	}
	
	@Override
	public boolean canExecute(Method method) {
		return remoteMethods.contains(method);
	}
	
	@Override
	public T getCallerInfo() {
		return callerInfoThreadLocal.get();
	}
	
	@Override
	public void setCallerInfo(T callerInfo) {
		if (callerInfo != null) {
			callerInfoThreadLocal.set(callerInfo);
		}
		else {
			callerInfoThreadLocal.remove();
		}
	}
	
	public boolean canExecute(S service, Method method, boolean anonymously) {
		if (service == null || service.getId() != getId())
			return false;
		return !anonymously ? canExecute(method) : canExecuteAnonymously(method);
	}
	
	@Override
	public boolean canExecuteAnonymously(Method method) {
		return remoteAnonymousMethods.contains(method);
	}
	
	public T getCallerInfo(S service) {
		return getCallerInfo();
	}
	
	public void setCallerInfo(S service, T callerInfo) {
		setCallerInfo(callerInfo);
	}
	
	@Override
	protected S getRemoteService(byte serviceId) {
		if (serviceId != getId())
			return null;
		return getService();
	}

	@Override
	public RemoteCallResult executeServiceMethod(T callerInfo, Method method,
			Object... args) {
		return executeServiceMethod(callerInfo, getService(), method, args);
	}

	@Override
	public RemoteCallResult executeServiceMethod(T callerInfo,
			String methodName, Object... args) {
		return executeServiceMethod(callerInfo, getService(), methodName, args);
	}
	
	@Override
	public Collection<Method> getCallableMethods(byte serviceId) {
		return remoteMethods;
	}
	
	public Method getMatchingMethod(S service, String methodName, Class<?> params) {
		return null;
	}
	
}
