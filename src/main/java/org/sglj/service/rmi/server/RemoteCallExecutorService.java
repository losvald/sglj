/*
 * RemoteCallExecutorService.java
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

import org.sglj.service.rmi.RemoteService;
import org.sglj.service.rmi.RemoteServiceManager;


/**
 * The service which executes remote calls.
 * 
 * @author Leo Osvald
 *
 * @param <T> type of the info about remote caller
 */
public class RemoteCallExecutorService<T extends RemoteCallerInfo>
extends AbstractRemoteCallExecutor<RemoteService, T> {
	private final RemoteServiceManager<RemoteServiceExecutor<RemoteService, T>> manager;
	
	@SuppressWarnings("unchecked")
	public RemoteCallExecutorService(
			RemoteServiceManager<? extends RemoteServiceExecutor<?, T>> serviceManager) {
		this.manager = (RemoteServiceManager<RemoteServiceExecutor<RemoteService, T>>) serviceManager;
	}
	
	public RemoteServiceExecutor<?, T> getRemoteServiceExecutor(byte serviceId) {
		return this.manager.getService(serviceId);
	}

	@Override
	public boolean canExecute(RemoteService service, Method method,
			boolean anonymously) {
		RemoteServiceExecutor<?, T> executor = getRemoteService(service.getId());
		return executor != null && (!anonymously ? executor.canExecute(method)
				: executor.canExecuteAnonymously(method));
	}

	@Override
	public T getCallerInfo(RemoteService service) {
		RemoteServiceExecutor<?, T> executor = getRemoteService(service.getId());
		return executor != null ? executor.getCallerInfo() : null;
	}


	@Override
	public void setCallerInfo(RemoteService service, T callerInfo) {
		RemoteServiceExecutor<?, T> executor = getRemoteService(service.getId());
		if (executor != null) {
			executor.setCallerInfo(callerInfo);
		}
	}
	
	@Override
	protected RemoteServiceExecutor<RemoteService, T> getRemoteService(byte serviceId) {
		return manager.getService(serviceId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Method> getCallableMethods(byte serviceId) {
		RemoteServiceExecutor<RemoteService, T> executor = getRemoteService(serviceId);
		if (executor == null)
			return Collections.EMPTY_LIST;
		return executor.getCallableMethods(serviceId);
	}
}
