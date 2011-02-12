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
 * every method in the service will throw the {@link RemoteCallException}.<br>
 * Note that this abstract implementation, unlike 
 * {@link SafeRemoteServiceStub}, does not check whether
 * the method can actually be called, so it is in a certain way "unsafe".<br>
 * Here is the usage example:
 * 
<pre>
// common interface for the calling and executing side
public interface ExampleService extends RemoteService {
	int calcSum(int a, int b) throws RemoteCallException;
	String sayHello(String a) throws RemoteCallException;
}

...

public class ExampleServiceStub extends
RemoteServiceStub&lt;ExampleService&gt; implements ExampleService {

	public ExampleServiceStub(RemoteCallRequestSender requestSender) {
		super(requestSender);
	}

	&#064;RemoteMethod(RemoteMethodSide.CALLER)
	&#064;Override
	public int calcSum(int a, int b) throws RemoteCallException {
		return callSynchronously(&quot;calcSum&quot;, a, b);
	}

	&#064;RemoteMethod(RemoteMethodSide.CALLER)
	&#064;Override
	public String sayHello(String a) throws RemoteCallException {
		return callSynchronously(&quot;sayHello&quot;, a);
	}

	&#064;Override
	public byte getId() {
		return ExampleService.ID;
	}

}
</pre> 
 * 
 * @author Leo Osvald
 *
 * @param <T> type of the service stub that will be implemented
 */
public abstract class RemoteServiceStub<T extends RemoteService>
implements RemoteService {

	private final RemoteServiceCaller<T> caller;
	
	/**
	 * Creates a stub which delegate calls to the corresponding
	 * request sender in an unsafe way (without checking whether the
	 * methods can actually be called on the server or whether they exist
	 * at all).
	 * 
	 * @param requestSender the request sender to which method calls
	 * should be delegated
	 */
	public RemoteServiceStub(RemoteCallRequestSender requestSender) {
		caller = new Caller(requestSender);
	}
	
	protected RemoteServiceStub(RemoteServiceCaller<T> caller) {
		this.caller = caller;
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
	
	private final class Caller extends AbstractRemoteServiceCaller<T> {

		public Caller(RemoteCallRequestSender requestSender) {
			super(requestSender);
		}

		@SuppressWarnings("unchecked")
		@Override
		public T getService() {
			return (T) RemoteServiceStub.this;
		}
		
	}
	
	private static final class ReturnValueFuture<R> implements Future<R> {

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
					throw new RemoteCallException(res.getError().byteValue());
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
				throw new RemoteCallException(res.getError().byteValue());
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
