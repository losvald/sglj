/*
 * RemoteCallRequestSerializer.java
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

/**
 * A serializer for {@link RemoteCallRequest}.
 * Implementors should be able to serialize this object to some data.
 * 
 * @author Leo Osvald
 * @version 1.0
 * 
 * @param <D> type of data to which the object should be serialized
 */
public interface RemoteCallRequestSerializer<D> {
	
	/**
	 * Serializes the specified <tt>RemoteCallRequest</tt> object.
	 * 
	 * @param remoteCallRequest remote call info that should be serialized
	 * @return serialized data
	 */
	D serialize(RemoteCallRequest remoteCallRequest);
}
	
