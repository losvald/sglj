/*
 * RemoteCallerInfo.java
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

import org.sglj.service.rmi.RemoteService;
import org.sglj.service.rmi.annotations.RemoteMethod;


/**
 * Marker interface for the class which represents the info
 * about the caller of a remote method.
 * 
 * @author Leo Osvald
 * @version 1.0
 * 
 * @see RemoteMethod
 * @see RemoteService
 */
public interface RemoteCallerInfo {
	
	/**
	 * Checks whether the caller is authenticated.
	 * 
	 * @return <code>true</code> if the caller is authenticated,
	 * <code>false</code> otherwise.
	 */
	boolean isAuthenticated();
}
