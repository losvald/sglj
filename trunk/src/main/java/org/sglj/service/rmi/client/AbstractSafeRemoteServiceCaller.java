/*
 * AbstractSafeRemoteServiceCaller.java
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

import org.sglj.service.rmi.RemoteCallResult;
import org.sglj.service.rmi.RmiUtils;
import org.sglj.service.rmi.RemoteService;
import org.sglj.service.rmi.ServiceReflectionCallException;
import org.sglj.service.rmi.annotations.RemoteMethod;
import org.sglj.service.rmi.annotations.RemoteMethod.RemoteMethodSide;

/**
 * Abstract implementation of the {@link RemoteServiceCaller} which
 * uses Java reflection to create remote call requests which are
 * then delegated to the corresponding {@link RemoteCallRequestSender}.<br>
 * It is sufficient to call the {@link #callServiceMethod(String, Object...)}
 * and this implementation will ensure that the method which matches
 * the passed arguments is called. The method to be called is determined
 * via call to the 
 * {@link MethodUtils#getMatchingAccessibleMethod(Class, String, Class[])}.<br>
 * This ensures that only valid remote call methods are delegated
 * to the remote call request server. The method is considered valid for
 * remote call iff it is annotated with the {@link RemoteMethod} with
 * the {@link RemoteMethodSide} set to {@link RemoteMethodSide#CALLER}.
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
abstract class AbstractSafeRemoteServiceCaller<T extends RemoteService>
extends AbstractRemoteServiceCaller<T> {

	private final Class<? extends T> clazz;
	
	private final Set<Method> remoteMethods;
	
	public AbstractSafeRemoteServiceCaller(Class<? extends T> remoteServiceStubClass,
			RemoteCallRequestSender requestSender) {
		super(requestSender);
		this.clazz = remoteServiceStubClass;
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
	public AbstractSafeRemoteServiceCaller(T remoteServiceStub,
			RemoteCallRequestSender requestSender) {
		this((Class<? extends T>) remoteServiceStub.getClass(), requestSender);
	}
	
	@Override
	public Future<RemoteCallResult> callServiceMethod(String methodName,
			Object... args) throws ServiceReflectionCallException {
			
			Class<?>[] paramTypes = new Class<?>[args == null ? 0 : args.length];
			if (args != null) {
				for (int i = 0; i < paramTypes.length; ++i) {
					paramTypes[i] = (args[i] != null ? args[i].getClass() : null);
				}
			}
			
			Method method;
			try {
//				System.err.print("Call: "+methodName+"(");
//				for (int i = 0; i < args.length; ++i)
//					System.err.print((i > 0 ? ", " : "")+args[i]);
//				System.err.println(")");
//				method = clazz.getDeclaredMethod(methodName, paramTypes);
				method = RmiUtils.getMatchingAccessibleMethod(
						clazz, methodName, paramTypes, remoteMethods);
			} catch (SecurityException e) {
				e.printStackTrace();
				throw new ServiceReflectionCallException(e);
//			} catch (NoSuchMethodException e) {
//				e.printStackTrace();
//				throw new ServiceReflectionCallException(e);
			}
			if (!this.remoteMethods.contains(method)) {
				throw new ServiceReflectionCallException("No such method");
			}
		return super.callServiceMethod(methodName, args);
	}
}
