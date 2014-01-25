/*
 * DefaultRemoteServiceManager.java
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

package org.sglj.service.rmi;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sglj.util.Pair;

/**
 * The implementation of the {@link RemoteServiceManager} interface
 * which uses <tt>HashMap</tt> to provide efficient retrieval of services
 * by id.<br>
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public class DefaultRemoteServiceManager<T extends RemoteService> 
extends AbstractRemoteServiceManager<T> {

	private final Map<Byte, T> serviceById;
	
	/**
	 * A map of all remote methods in the registered services.
	 * Each service has its own map which stores methods by
	 * the pair ("method name", "argument count").
	 */
	private final Map<Byte, Map<Pair<String, Integer>, Method>> 
	methodMapByService;
	
	/**
	 * This map cannot be initialized here (or in the constructor), since
	 * it must be initialized before the call to super constructor from
	 * which the {@link #registerRemoteMethod(byte, Method)} method
	 * will be called.
	 */
	private Map<Byte, Map<Pair<String, Integer>, Method>>
	preMethodMapByService;
	
	@SuppressWarnings("unchecked")
	public <T2 extends T> DefaultRemoteServiceManager(T2... remoteServices) {
		super(remoteServices);
		
		HashMap<Byte, T> map = new HashMap<Byte, T>();
		for (T service : availableServices()) {
			map.put(service.getId(), service);
		}
		this.serviceById = Collections.unmodifiableMap(map);
		
		// create method map from the map which (maybe) registered some services
		// it will be null if no remote services were registered
		methodMapByService = (preMethodMapByService == null 
				? Collections.EMPTY_MAP
						: Collections.unmodifiableMap(preMethodMapByService));
	}

	@Override
	public T getService(byte serviceId) {
		return this.serviceById.get(serviceId);
	}
	
	@Override
	protected void registerRemoteMethod(byte serviceId, Method remoteMethod) {
		if (preMethodMapByService == null) {
			preMethodMapByService = new HashMap<Byte, 
			Map<Pair<String,Integer>, Method>>();
		}
		Map<Pair<String,Integer>, Method> methodMap
		= preMethodMapByService.get(serviceId);
		if (methodMap == null) {
			methodMap = new HashMap<Pair<String,Integer>,Method>();
			preMethodMapByService.put(serviceId, methodMap);
		}
		methodMap.put(new Pair<String, Integer>(remoteMethod.getName(), 
				remoteMethod.getParameterTypes().length), remoteMethod);
	}
	
	@Override
	public Method getRemoteMethod(byte serviceId, String methodName,
			int parameterCount) {
		Map<Pair<String,Integer>, Method> methodMap = methodMapByService
		.get(serviceId);
		if (methodMap != null) {
			return methodMap.get(new Pair<String, Integer>(
					methodName, parameterCount));
		}
		return null;
	}

}
