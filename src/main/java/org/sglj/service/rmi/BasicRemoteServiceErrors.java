/*
 * BasicRemoteServiceErrors.java
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
 * Basic remote service error constants.
 * 
 * @author Leo Osvald
 *
 */
public abstract class BasicRemoteServiceErrors {
	public static final byte UNKNOWN_ERROR = 0x0;
	public static final byte PERMISSION_DENIED = 0x1;
	public static final byte INVALID_DATA_SENT = 0x2;
	public static final byte INVALID_DATA_RECEIVED = 0x3;
}
