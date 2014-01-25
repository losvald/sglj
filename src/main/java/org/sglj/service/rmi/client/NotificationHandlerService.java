/*
 * NotificationHandlerService.java
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


import org.sglj.service.rmi.RemoteCallRequest;
import org.sglj.service.rmi.RemoteServiceManager;

/**
 * The service which handles any notification.
 * 
 * @author Leo Osvald
 *
 */
public class NotificationHandlerService {
	
	private final RemoteServiceManager<NotificationServiceExecutor<?>> manager;
	
	public NotificationHandlerService(
			RemoteServiceManager<NotificationServiceExecutor<?>> 
			notificationServiceManager) {
		this.manager = notificationServiceManager;
	}
	
	/**
	 * Handles the notification represented by the specified remote call
	 * request
	 * 
	 * @param request request which describes which notification method
	 * should be called.
	 */
	public void handleNotification(RemoteCallRequest request) {
		NotificationServiceExecutor<?> executor = this.manager.getService(
				request.getServiceId());
		if (executor == null) {
			throw new IllegalArgumentException(
					"Missing notification service with id: "
					+request.getServiceId());
		}
		executor.handleNotification(request.getMethodName(), 
				request.getArguments());
	}
}
