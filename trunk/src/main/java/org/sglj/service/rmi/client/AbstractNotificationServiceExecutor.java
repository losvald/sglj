/*
 * AbstractNotificationServiceExecutor.java
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

import org.sglj.service.rmi.NotificationService;
import org.sglj.service.rmi.ServiceReflectionCallException;
import org.sglj.service.rmi.annotations.RemoteMethod;


/**
 * The abstract implementation of the {@link NotificationServiceExecutor}
 * interface.
 * 
 * @author Leo Osvald
 *
 * @param <T> type of the notification service that this executor executes
 */
public abstract class AbstractNotificationServiceExecutor
<T extends NotificationService> 
extends AbstractNotificationHandler<T>
implements NotificationServiceExecutor<T> {

	/**
	 * Set of all remote methods that can be executed.
	 */
	private final Set<Method> remoteMethods;
	
	/**
	 * Creates a notification service based on the specified implementation. 
	 * 
	 * @param clazz the class that implements the given notification service
	 */
	public AbstractNotificationServiceExecutor(
			Class<? extends T> implementationClass) {
		// cache methods that can be executed
		Method[] methods = implementationClass.getDeclaredMethods();
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
					.EXECUTOR) {
				set.add(m);
			}
		}
		this.remoteMethods = Collections.unmodifiableSet(set);
	}
	
	@Override
	public void handleNotification(String methodName, Object... args)
			throws ServiceReflectionCallException {
		handleNotification(getNotificationService(), methodName, args);
	}
	
	@Override
	public boolean canExecute(T service, Method method) {
		if (service == null || service.getId() != getId())
			return false;
		return remoteMethods.contains(method);
	}

}
