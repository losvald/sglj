/*
 * CacheManager.java
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
 * Manager which caches objects of type C.
 * 
 * @author Leo Osvald
 * @param <I> type of info about cacheable object
 * @param <C> type of object which is cached
 */
public interface CacheManager<I extends CacheInfo, C> {
	
	static final int PERSIST_ON_DISK 	= 1<<0;
	static final int PERSIST_IN_MEMORY = 1<<1;
	
	/**
	 * Caches the object.<br>
	 * The decision where the object should be cached is up to
	 * this manager.
	 * @param cacheInfo info about object that should be cached
	 * @param cacheable object that should be cached
	 */
	void add(I cacheInfo, C cacheable);
	
	/**
	 * Caches the object to RAM or disk (or both).
	 * @param cacheInfo info about object that should be cached
	 * @param cacheable object that should be cached
	 * @param whereToCache mask which tells where to persist
	 * (i.e. {@link #PERSIST_IN_MEMORY} | {@link #PERSIST_ON_DISK} 
	 * will cache to both RAM and disk)
	 */
	void add(I cacheInfo, C cacheable, int whereToCache);
	
	/**
	 * Removes cache for object described by specified info.
	 * @param cacheInfo info about object that should be removed from cache
	 * @return <code>true</code> if object was removed from cache,
	 * <code>false</code> otherwise.
	 */
	boolean remove(I cacheInfo);
	
	/**
	 * Retrieves cache location (RAM or disk) for object described by 
	 * this info.<br>
	 * Usage:
	 * <pre>
	 * int mask = cacheManager.getPersistenceMask(cacheInfo);
	 * if (mask | cacheManager.PERSIST_IN_MEMORY) {
	 *   //we know that object is cached in RAM (memory)
	 *   ...
	 * }
	 * @param cacheInfo info about cached object
	 * (i.e. {@link #PERSIST_IN_MEMORY} | retValue} checks whether object
	 * is cached to RAM (memory))
	 * @return mask
	 */
	int getPersistenceMask(I cacheInfo);
	
	/**
	 * If object described by this info is cached, it is returned.
	 * @param opisnik info about cached object
	 * @return object that is cached, or <code>null</code> if that
	 * object is not cached.
	 */
	C get(CacheInfo cacheInfo);
	
	/**
	 * Removes all objects cached to memory (RAM).
	 */
	void clearMemoryCache();
	
	/**
	 * Removes all objects cached to disk.
	 */
	void clearDiskCache();

	/**
	 * Moves cache from memory (RAM) to disk.
	 * @param cacheInfo info about cached object
	 * @return <code>true</code> if moving succeeded, 
	 * <code>false</code> otherwise.
	 */
	boolean moveFromMemoryToDisk(I cacheInfo);
	
	/**
	 * Moves cache from disk to memory (RAM).
	 * @param cacheInfo info about cached object
	 * @return <code>true</code> if moving succeeded, 
	 * <code>false</code> otherwise.
	 */
	boolean moveFromDiskToMemory(I cacheInfo);
	
	/**
	 * Sets maximum allowed size of memory cache (RAM).<br>
	 * Note: This is not a guarantee that this amount of memory will
	 * not be exceeded, because size of the cached objects
	 * is only estimated by means of calling 
	 * {@link CacheInfo#approximateSize()} method.
	 * @param size in bytes
	 */
	void setMaxMemoryCache(long size);
	
	/**
	 * Sets maximum allowed size of disk cache.<br>
	 * Note: This is not a guarantee that this amount of memory will
	 * not be exceeded, because size of the cached objects
	 * is only estimated by means of calling 
	 * {@link CacheInfo#approximateSize()} method.
	 * @param size in bytes
	 */
	void setMaxDiskSize(long size);
	
	/**
	 * Returns approximate amount of memory used for caching objects 
	 * to memory (RAM).
	 * @return size in bytes
	 */
	long approximateMemoryCacheSize();
	
	/**
	 * Returns the amount of space used for caching object to disk.
	 * @return size in bytes
	 */
	long approximateDiskCacheSize();
}
