/*
 * AbstractRemoteServiceCaller.java
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
import org.sglj.service.rmi.RemoteService;

/**
 * Abstract implementation of the {@link RemoteServiceCaller} which
 * delegates methods called via the {@link #callServiceMethod(String, Object...)}
 * to the corresponding {@link RemoteCallRequestSender}.
 * 
 * @author Leo Osvald
 *
 * @param <T>
 */
abstract class AbstractRemoteServiceCaller<T extends RemoteService>
implements RemoteServiceCaller<T> {
	
	private final RemoteCallRequestSender requestSender;
	
	public AbstractRemoteServiceCaller(RemoteCallRequestSender requestSender) {
		this.requestSender = requestSender;
	}
	
	@Override
	public Future<RemoteCallResult> callServiceMethod(String methodName,
			Object... args) {
		return getRequestSender().sendRemoteCallRequest(new RemoteCallRequest(
				getService().getId(), methodName, args));
	}
	
	@Override
	public RemoteCallRequestSender getRequestSender() {
		return requestSender;
	}
	
}
