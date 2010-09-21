/*
 * RemoteCallRequestSender.java
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

import java.util.concurrent.Future;

import org.sglj.service.rmi.RemoteCallRequest;
import org.sglj.service.rmi.RemoteCallResult;


/**
 * Classes that implement this interface are responsible for sending
 * a {@link RemoteCallRequest} from the calling side 
 * through the network to the executing side.
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public interface RemoteCallRequestSender {
	
	/**
	 * Asynchronously sends a remote call request.
	 *  
	 * @param request request to be sent
	 * @return a {@link Future} object which contains the result of the
	 * remote call once the request is sent and the response is received
	 */
	Future<RemoteCallResult> sendRemoteCallRequest(RemoteCallRequest request);
}
