/*
 * NotificationHandler.java
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

import org.sglj.service.rmi.NotificationService;
import org.sglj.service.rmi.ServiceReflectionCallException;


/**
 * The handler which handles notification for the specified notification
 * service. Notification handling is done by calling a 
 * the appropriate method from the notification service.
 * 
 * @author Leo Osvald
 *
 * @param <T> type of the notification service
 */
public interface NotificationHandler<T extends NotificationService>
extends NotificationService {
	
	/**
	 * Handles the notification for the specified service.
	 * 
	 * @param service the implementation class for the remote service which
	 * method should be invoked
	 * @param methodName the name of the method that should be invoked
	 * @param args the arguments to be passed to the method
	 * @throws ServiceReflectionCallException
	 */
	void handleNotification(T service, String methodName, Object... args)
	throws ServiceReflectionCallException;
	
	/**
	 * Checks whether the given remote method can be executed.
	 * If this method returns <code>false</code> trying to execute
	 * it with this executor is guaranteed to fail.
	 * 
	 * @param service the implementation class for the remote service which
	 * method should be invoked
	 * @param method method which should be checked 
	 */
	boolean canExecute(T service, Method method);
}
