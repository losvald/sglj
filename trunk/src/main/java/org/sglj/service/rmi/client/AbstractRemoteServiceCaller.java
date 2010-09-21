/*
 * AbstractRemoteServiceCaller.java
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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.commons.lang.reflect.MethodUtils;
import org.sglj.service.rmi.RemoteCallRequest;
import org.sglj.service.rmi.RemoteCallResult;
import org.sglj.service.rmi.RemoteService;
import org.sglj.service.rmi.ServiceReflectionCallException;
import org.sglj.service.rmi.annotations.RemoteMethod;


public abstract class AbstractRemoteServiceCaller<T extends RemoteService>
implements RemoteServiceCaller<T> {
	
	private final Class<? extends T> clazz;
	
	private final Set<Method> remoteMethods;
	
	private final RemoteCallRequestSender requestSender;
	
	public AbstractRemoteServiceCaller(Class<? extends T> remoteServiceStubClass,
			RemoteCallRequestSender requestSender) {
		this.clazz = remoteServiceStubClass;
		this.requestSender = requestSender;
		
		Method[] methods = clazz.getDeclaredMethods();
		Set<Method> set = new HashSet<Method>();
		for (Method m : methods) {
			if (!m.isAccessible()) {
				try {
					m.setAccessible(true);
				} catch (SecurityException ignorable) {
				}
			}
			RemoteMethod annot = m.getAnnotation(RemoteMethod.class);
			if (annot != null && annot.value() == RemoteMethod.RemoteMethodSide
					.CALLER) {
				set.add(m);
			}
		}
		this.remoteMethods = Collections.unmodifiableSet(set);
	}
	
	@SuppressWarnings("unchecked")
	public AbstractRemoteServiceCaller(T remoteServiceStub,
			RemoteCallRequestSender requestSender) {
		this((Class<? extends T>) remoteServiceStub.getClass(), requestSender);
	}
	
	@Override
	public Future<RemoteCallResult> callServiceMethod(String methodName,
			Object... args) throws ServiceReflectionCallException {
		
		Class<?>[] paramTypes = new Class<?>[args == null ? 0 : args.length];
		if (args != null) {
			for (int i = 0; i < paramTypes.length; ++i) {
				paramTypes[i] = args[i].getClass();
			}
		}
		
		Method method;
		try {
//			System.err.print("Call: "+methodName+"(");
//			for (int i = 0; i < args.length; ++i)
//				System.err.print((i > 0 ? ", " : "")+args[i]);
//			System.err.println(")");
//			method = clazz.getDeclaredMethod(methodName, paramTypes);
			method = MethodUtils.getMatchingAccessibleMethod(clazz, 
					methodName, paramTypes);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new ServiceReflectionCallException(e);
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//			throw new ServiceReflectionCallException(e);
		}
		if (!this.remoteMethods.contains(method)) {
			throw new ServiceReflectionCallException("No such method");
		}
		
		return getRequestSender().sendRemoteCallRequest(new RemoteCallRequest(
				getService().getId(), methodName, args));
	}
	
	@Override
	public RemoteCallRequestSender getRequestSender() {
		return requestSender;
	}
	
}
