/*
 * AbstractRemoteServiceManager.java
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.sglj.service.rmi.annotations.RemoteMethod;
import org.sglj.util.Pair;


/**
 * The abstract implementation of the {@link RemoteServiceManager} interface.<br>
 * 
 * Services to be registered are provided via constructor as variable
 * argument list.
 * 
 * 
 * @author Leo Osvald
 * @version 1.0
 */
abstract class AbstractRemoteServiceManager<T extends RemoteService> 
implements RemoteServiceManager<T> {

	/**
	 * Unmodifiable list of remote services
	 */
	private final List<T> services;
	
	/**
	 * 
	 * @param remoteServices services to be registered
	 * @throws IllegalArgumentException if the contract defined in 
	 * the {@link RemoteServiceManager} interface is broken.
	 */
	public <T2 extends T> AbstractRemoteServiceManager(T2... remoteServices) 
	throws IllegalArgumentException {
		Validate.noNullElements(remoteServices, "Remote service cannot be null");
		
		Set<T> uniqueServices = new HashSet<T>(
				remoteServices.length);
		for (T remoteService : remoteServices) {
			uniqueServices.add(remoteService);
		}
		
		// ensure that there are no duplicate services
		Validate.isTrue(uniqueServices.size() == remoteServices.length,
				"Duplicate services detected");
		
		// ensure that services have distinct identifiers
		Set<Byte> ids = new HashSet<Byte>(uniqueServices.size());
		for (RemoteService service : remoteServices) {
			ids.add(service.getId());
		}
		Validate.isTrue(uniqueServices.size() == ids.size(), 
				"Services with identical id detected");
		ids.clear(); // free memory before allocating new
		
		services = Collections.unmodifiableList(new ArrayList<T>(
				uniqueServices));
		
		// retrieve all remote methods and store them in a map by name
		for (RemoteService service : services) {
			Method[] methods = service.getClass().getMethods();
			Set<Pair<String, Integer>> set = new HashSet<Pair<String,Integer>>();
			for (Method m : methods) {
				if (m.isAnnotationPresent(RemoteMethod.class)) {
					int paramCount = m.getParameterTypes().length;
					if (!set.add(new Pair<String, Integer>(m.getName(), 
							paramCount))) {
						throw new IllegalArgumentException(
								"Two methods with the same name \""+m.getName()
								+ "\" and with " + paramCount 
								+ " parameters detected in service"
								+ service);
					}
					registerRemoteMethod(service.getId(), m);
				}
			}
		}
	}

	@Override
	public List<T> availableServices() {
		return services;
	}
	
	@Override
	public boolean existsRemoteMethod(byte serviceId, String methodName,
			int parameterCount) {
		return getRemoteMethod(serviceId, methodName, parameterCount)
		!= null;
	}
	
	/**
	 * A method called by the constructor to "permanently" register a 
	 * remote method in the specified service.<br>
	 * The method is guaranteed to be remote so there is no need to
	 * check again whether it is annotated by the 
	 * {@link RemoteMethod} annotation. The implementor also does not
	 * need to check whether two methods with the same name and the same
	 * number of the arguments belong to the same service - that check
	 * is already implemented by the {@link AbstractRemoteServiceManager}
	 * class.
	 * 
	 * @param serviceId identifier of the service that this method belongs to
	 * @param remoteMethod the method that should registered
	 */
	protected abstract void registerRemoteMethod(byte serviceId,
			Method remoteMethod);
	
}
