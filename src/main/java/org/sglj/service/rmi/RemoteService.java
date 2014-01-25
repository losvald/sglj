/*
 * RemoteService.java
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

/**
 * Marker interface which must be implemented by all remote services.
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public interface RemoteService {
	
	/**
	 * Returns the identifier of this service. Two remote services on the same
	 * host should have different identifiers.
	 * 
	 * @return the identifier of the remote service
	 */
	byte getId();
}
