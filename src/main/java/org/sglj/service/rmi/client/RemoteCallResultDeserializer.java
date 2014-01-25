/*
 * RemoteCallResultDeserializer.java
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
import org.sglj.service.rmi.RemoteCallResult;

/**
 * Deserializer for {@link RemoteCallResult}.
 * Implementors should be able to deserialize this object from given data.
 * 
 * @author Leo Osvald
 * @version 1.0
 * 
 * @param <D> type of data from which the object should be serialized
 */
public interface RemoteCallResultDeserializer<D> {
	
	/**
	 * Deserializes a <tt>RemoteCallResult</tt> object from the specified data.
	 * @param data data from to be deserialized
	 * @param request that request which this result belongs to
	 * @return the result
	 */
	RemoteCallResult deserialize(D data, RemoteCallRequest request);
}

