/*
 * NotificationServiceStub.java
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.reflect.MethodUtils;
import org.sglj.service.rmi.NotificationService;
import org.sglj.service.rmi.RemoteCallRequest;
import org.sglj.service.rmi.ServiceReflectionCallException;
import org.sglj.service.rmi.annotations.RemoteMethod;


/**
 * Stub implementation which delegates the call of a method to the
 * associated {@link NotificationSender}.<br> 
 * All implementing classes should just tag  method with
 * the {@link RemoteMethod} annotation and call the
 * {@link #callNotificationMethod(Object[], String, Object...)} method
 * which delegates the call to the sender.  
 *  
 * @author Leo Osvald
 *
 * @param <T> type of the info about recipients
 */
public abstract class NotificationServiceStub<T>
implements NotificationService {

	private final Set<Method> remoteMethods;
	
	private final NotificationSender<T> sender;

	public NotificationServiceStub(NotificationSender<T> notificationSender) {
		this.sender = notificationSender;
		
		Method[] methods = this.getClass().getDeclaredMethods();
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
					.CALLER && m.getReturnType() == void.class) {
				set.add(m);
			}
		}
		this.remoteMethods = Collections.unmodifiableSet(set);
	}

	protected void callNotificationMethod(T[] recipients, String methodName, 
			Object... args) throws ServiceReflectionCallException {

		Class<?>[] paramTypes = new Class<?>[args == null ? 0 : args.length];
		if (args != null) {
			for (int i = 0; i < paramTypes.length; ++i) {
				paramTypes[i] = args[i].getClass();
			}
		}

		Method method;
		try {
			method = MethodUtils.getMatchingAccessibleMethod(this.getClass(), 
					methodName, paramTypes);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new ServiceReflectionCallException(e);
		}
		if (!this.remoteMethods.contains(method)) {
			throw new ServiceReflectionCallException("No such method");
		}

		this.sender.sendNotification(recipients, 
				new RemoteCallRequest(getId(), methodName, args));
	}
	
	/**
	 * A convenient method for building the array of the recipients.
	 * 
	 * @param <T> type of the recipient info
	 * @param recipients recipient info
	 * @return the array of recipient info
	 */
	protected static <T> T[] buildRecipientList(T... recipients) {
		return recipients;
	}

}
