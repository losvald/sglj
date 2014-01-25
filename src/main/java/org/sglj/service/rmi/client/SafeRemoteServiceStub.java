/*
 * SafeRemoteServiceStub.java
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

import org.sglj.service.rmi.RemoteService;
import org.sglj.service.rmi.ServiceReflectionCallException;
import org.sglj.service.rmi.annotations.RemoteMethod;
import org.sglj.service.rmi.annotations.RemoteMethod.RemoteMethodSide;


/**
 * An extension of the {@link RemoteServiceStub} class which provide
 * safe remote method calls.<br>
 * Unlike {@link RemoteServiceStub}, this implementation actually checks
 * whether the called methods exist and can be called. A method is callable
 * iff it is annotated with the {@link RemoteMethod} with
 * the {@link RemoteMethodSide} set to {@link RemoteMethodSide#CALLER}. 
 * If the method is not callable, the calls to methods 
 * {@link #callAsynchronously(String, Object...)} and 
 * {@link #callSynchronously(String, Object...)} will throw a
 * {@link ServiceReflectionCallException}. See
 * {@link AbstractSafeRemoteServiceCaller} and 
 * {@link RemoteServiceStub} for details.
 * 
 * @author Leo Osvald
 *
 * @param <T> type of the service stub that will be implemented
 * @see AbstractSafeRemoteServiceCaller
 * @see RemoteServiceStub
 */
public abstract class SafeRemoteServiceStub<T extends RemoteService>
extends RemoteServiceStub<T>
implements RemoteService {

	@SuppressWarnings("unchecked")
	public SafeRemoteServiceStub(Class<? extends T> remoteServiceStubClass,
			RemoteCallRequestSender requestSender) {
		super(new SafeCaller<T>(remoteServiceStubClass, 
				requestSender));
		((SafeCaller<T>) getCaller()).setRemoteService((T) this);
	}
	
	private static final class SafeCaller<T extends RemoteService>
	extends AbstractSafeRemoteServiceCaller<T> {
		private T remoteService;
		
		SafeCaller(Class<? extends T> remoteServiceStubClass, 
				RemoteCallRequestSender requestSender) {
			super(remoteServiceStubClass, requestSender);
		}

		@Override
		public T getService() {
			return (T) remoteService;
		}
		
		void setRemoteService(T remoteService) {
			this.remoteService = remoteService;
		}
		
	}

}
