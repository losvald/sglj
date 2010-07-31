/*
 * AbstractCacheManager.java
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

public abstract class AbstractCacheManager<I extends CacheInfo, C> 
implements CacheManager<I, C> {
		
	protected volatile long maxMemory;
	protected volatile long maxDisk;
	
	@Override
	public void setMaxDiskSize(long size) {
		this.maxDisk = size;
	}
	
	@Override
	public void setMaxMemoryCache(long size) {
		this.maxMemory = size;
	}
	
//	/**
//	 * Performs caching to RAM.
//	 * @param cacheInfo object that should be cached
//	 * @throws OutOfMemoryError if there is not enough free memory
//	 */
//	protected abstract void persistInMemory(CacheInfo<T> cacheInfo);
//	
//	/**
//	 * Performs caching to disk.
//	  * @param cacheInfo object that should be cached
//	 * @throws OutOfMemoryError if there is not enough free space on the disk
//	 */
//	protected abstract void persistOnDisk(CacheInfo<T> cacheInfo);
//	
//	/**
//	 * Decides where the specified object should be cached to.<br>
//	 * Usage:
//	 * <pre>
//	 * if(mask == {@link CacheManager#PERSIST_IN_MEMORY}) {
//	 *     //persist in memory
//	 *     ...
//	 * }
//	 * else {
//	 *     //persist in m
//	 * }
//	 * </pre>
//	 * @param cacheInfo object which should be cached
//	 * @return mask which contains
//	 */
//	protected abstract int determineWhereToPersist(CacheInfo<T> cacheInfo);
	
}
