/*
 * RemoteServiceStub.java
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.sglj.service.rmi.BasicRemoteServiceErrors;
import org.sglj.service.rmi.RemoteCallException;
import org.sglj.service.rmi.RemoteCallResult;
import org.sglj.service.rmi.RemoteService;


/**
 * Base class for implementing stub services on the calling side (usually
 * client). Provides wrapper type-safe methods that return the method's
 * return type. If there is an exception on the executing side (usually server),
 * every method in the service will throw the {@link RemoteCallException}. 
 * 
 * @author Leo Osvald
 *
 * @param <T> type of the service stub that will be implemented
 */
public abstract class RemoteServiceStub<T extends RemoteService>
implements RemoteService {

	private final Caller caller;
	
	public RemoteServiceStub(RemoteCallRequestSender requestSender) {
		caller = new Caller(requestSender);
	}
	
	protected <R> R callSynchronously(String methodName, Object... args) 
	throws RemoteCallException {
		//we could call the callAsynchronously method here, but this is faster
		Future<R> returnValueFuture = new ReturnValueFuture<R>(caller
				.callServiceMethod(methodName, args));
		try {
			return returnValueFuture.get();
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof RemoteCallException) {
				throw (RemoteCallException) e;
			}
			throw new RemoteCallException(e, BasicRemoteServiceErrors.UNKNOWN_ERROR);
		}
	}
	
	protected <R> Future<R> callAsynchronously(String methodName, 
			Object... args) {
		return new ReturnValueFuture<R>(caller
				.callServiceMethod(methodName, args));
	}
	
	protected RemoteServiceCaller<T> getCaller() {
		return caller;
	}
	
	private class Caller extends AbstractRemoteServiceCaller<T> {

		@SuppressWarnings("unchecked")
		public Caller(RemoteCallRequestSender requestSender) {
			super((T) RemoteServiceStub.this, requestSender);
			// TODO Auto-generated constructor stub
		}

		@SuppressWarnings("unchecked")
		@Override
		public T getService() {
			return (T) RemoteServiceStub.this;
		}
		
	}
	
	
	private static class ReturnValueFuture<R> implements Future<R> {

		private final Future<RemoteCallResult> resultFuture;
		
		ReturnValueFuture(Future<RemoteCallResult> resultFuture) {
			this.resultFuture = resultFuture;
		}
		
		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return resultFuture.cancel(mayInterruptIfRunning);
		}

		@SuppressWarnings("unchecked")
		@Override
		public R get() throws InterruptedException, ExecutionException {
			try {
				RemoteCallResult res = resultFuture.get();
				if (res.isError())
					throw new RemoteCallException(res.getError());
				return (R) res.getReturnValue();
			} catch (ExecutionException e) {
				e.printStackTrace();
				throw new RemoteCallException(e, 
						BasicRemoteServiceErrors.UNKNOWN_ERROR);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public R get(long timeout, TimeUnit unit) throws InterruptedException,
				ExecutionException, TimeoutException {
			RemoteCallResult res = resultFuture.get(timeout, unit);
			if (res.isError())
				throw new RemoteCallException(res.getError());
			return (R) res.getReturnValue();
		}

		@Override
		public boolean isCancelled() {
			return resultFuture.isCancelled();
		}

		@Override
		public boolean isDone() {
			return resultFuture.isDone();
		}
	}
	

}
