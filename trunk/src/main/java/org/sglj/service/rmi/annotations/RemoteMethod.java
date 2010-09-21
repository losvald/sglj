/*
 * RemoteMethod.java
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

package org.sglj.service.rmi.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.sglj.service.rmi.RemoteService;


/**
 * <p>The annotation used to annotate a method that should be treated
 * as a remote method which belongs to some remote service
 * (see {@link RemoteService}). This prevents unauthorized access 
 * to non-annotated methods like private methods of classes that
 * implement <tt>RemoteService</tt> interface.<br>
 * By default, remote method can be called only by authenticated users.
 * To override this behavior and enable public access, set 
 * the {@link #publicAccess()} value to <code>true</code>.</p>
 * 
 * @author Leo Osvald
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RemoteMethod {
	
	/**
	 * Returns the side of the remote method. It can be either
	 * {@link RemoteMethodSide#CALLER} or {@link RemoteMethodSide#EXECUTOR}.
	 * @return
	 */
	RemoteMethodSide value();
	
	/**
	 * Returns the access type. If the value is set to true,
	 * @return <code>true</code> if the method does not require
	 * the caller to be authenticated, <code>false</code> otherwise.
	 */
	boolean anonymousAccess() default false;
	
	/**
	 * The side on which the method is implemented. Can be:
	 * <ul>
	 * <li>{@link #CALLER} - if this is only a stub implementation
	 * which transmits the request through the network to the
	 * executing side (where the method is actually executed and
	 * by which the result is returned)</li>
	 * <li>{@link #EXECUTOR} - if this is the real implementation
	 * which can either directly or undirectly (via method calls)
	 * implement the behavior specified by the corresponding
	 * remote service method.</li>
	 * </ul>
	 * 
	 * @author Leo Osvald
	 *
	 */
	public enum RemoteMethodSide {
		CALLER,
		EXECUTOR;
	}
}
