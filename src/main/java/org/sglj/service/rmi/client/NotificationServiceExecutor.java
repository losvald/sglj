/*
 * NotificationServiceExecutor.java
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

import org.sglj.service.rmi.NotificationService;
import org.sglj.service.rmi.ServiceReflectionCallException;

/**
 * The executor of the notification service method.
 * This interface should be implemented by all hosted notification services. 
 * 
 * @author Leo Osvald
 *
 * @param <T> notification service (usually an interface) 
 * which this executor can execute
 */
public interface NotificationServiceExecutor<T extends NotificationService>
extends NotificationHandler<T>, NotificationService {
	
	/**
	 * Returns the notification service for which this handler
	 * handles notifications.
	 * 
	 * @return the notification service
	 */
	T getNotificationService();
	
	/**
	 * Handles the notification. This method is a convenient wrapper
	 * for the {@link #handleNotification(NotificationService, 
	 * String, Object...)} method.
	 * 
	 * @param methodName the name of the method that should be invoked
	 * @param args the arguments to be passed to the method
	 * @throws ServiceReflectionCallException
	 */
	void handleNotification(String methodName, Object... args)
	throws ServiceReflectionCallException;
}
