/*
 * CacheInfo.java
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

package org.sglj.cache;

/**
 * Cache descriptor.
 * Serves as a key for mapping cacheable objects
 * (marked with {@link Cacheable} interface).
 * It is required that subclasses override the following methods:
 * -{@link #equals(Object)}
 * -{@link #hashCode()}
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public abstract class CacheInfo {
	/**
	 * Returns approximate amount of memory which cacheable object
	 * (which this info describes) uses.
	 * @return size in bytes
	 */
	protected abstract long approximateSize();
	
	/**
	 * Checks whether cacheable object has expired.
	 * @return <code>true</code> if it expired, <code>false</code> otherwise.
	 */
	protected abstract boolean isExpired();
	
	public abstract boolean equals(Object o);
	public abstract int hashCode();
}
