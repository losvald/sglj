/*
 * RemoteCallExceptionThrower.java
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
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.sglj.service.rmi.RemoteCallException;


/**
 * The marker annotation used to indicate that a certain method
 * or constructor throws can throw a {@link RemoteCallException} with 
 * one of the specified error codes.
 * 
 * @author Leo Osvald
 * @version 1.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Inherited
@Documented
public @interface RemoteCallExceptionThrower {
	
	/** 
	 * Remote exceptions which might occur during the method.
	 * The codes corresponds to the ones obtained by 
	 * {@link RemoteCallException#getExceptionCode()}
	 * 
	 * @return the array of error codes of the remote exceptions
	 */
	byte[] value();
}
