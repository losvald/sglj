/*
 * AbstractNotificationHandler.java
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.reflect.MethodUtils;
import org.sglj.service.rmi.NotificationService;
import org.sglj.service.rmi.ServiceReflectionCallException;


/**
 * The abstract implementation of the {@link NotificationHandler} interface. 
 * 
 * @author Leo Osvald
 *
 * @param <T> type of the notification service which this handler handles
 */
public abstract class AbstractNotificationHandler<T extends NotificationService>
implements NotificationHandler<T> {
	
	@Override
	public void handleNotification(T service, String methodName, 
			Object... args) {
		Class<?>[] paramTypes = new Class<?>[args == null ? 0 : args.length];
		for(int i = 0; i < paramTypes.length; ++i)
			paramTypes[i] = args[i].getClass();
		
		try {
			Method m = MethodUtils.getMatchingAccessibleMethod(
					service.getClass(), methodName, paramTypes);
			if (!canExecute(service, m)) {
				throw new ServiceReflectionCallException(
						"Missing method \""+m
						+"\" in the notification service: "
						+service.getClass().getName());
			}
			m.invoke(service, args);
		} catch (InvocationTargetException e) {
			// if notification fails, let the application crash
			throw new NotificationException(e);
		} catch (Exception e) {
			throw new ServiceReflectionCallException(e);
		}
	}
	
	
	private static class NotificationException extends RuntimeException {
		
		private static final long serialVersionUID = 1L;

		/**
		 * Constructs a new exception with a wrapped throwable.
		 * 
		 * @param cause exception to be wrapped
		 */
		public NotificationException(Throwable cause) {
			super(cause);
		}
	}
}
