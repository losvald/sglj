/*
 * FIFOCacheManager.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Cache manager which provides FIFO insertion and removal of cached
 * objects. When there is not enough free memory/space, object
 * which was first cached is removed from cache. 
 * 
 * @author Leo Osvald
 *
 */
public class FIFOCacheManager<I extends CacheInfo, C> 
extends AbstractCacheManager<I, C> {
	
	protected Map<I, C> memory = Collections.synchronizedMap(new LinkedHashMap<I, C>());

	protected int memoryUsed;
	
	@Override
	public void add(I cacheInfo, C cacheable) {
		add(cacheInfo, cacheable, PERSIST_IN_MEMORY);
		//TODO
	}

	public void add(I cacheInfo, C cacheable, int whereToPersist) {
		if((whereToPersist & PERSIST_IN_MEMORY) != 0) {
			long size = cacheInfo.approximateSize();
			if(size <= maxMemory) {
				synchronized (memory) {
					ensureFreeSpace(size);
					if(memory.containsKey(cacheInfo))
						memory.remove(cacheInfo);
					memory.put(cacheInfo, cacheable);
					memoryUsed += cacheInfo.approximateSize();
				}
			}
		}
		//TODO
	}
	
	@Override
	public void clearDiskCache() {
		throw new UnsupportedOperationException("Not yet implemented");
		// TODO Auto-generated method stub
	}

	@Override
	public void clearMemoryCache() {
		memory.clear();
	}

	@Override
	public C get(CacheInfo cacheInfo) {
		return memory.get(cacheInfo);
	}

	@Override
	public boolean moveFromDiskToMemory(I cachedObject) {
		throw new UnsupportedOperationException("Not yet implemented");
		// TODO Auto-generated method stub
	}

	@Override
	public boolean moveFromMemoryToDisk(I cacheInfo) {
		throw new UnsupportedOperationException("Not yet implemented");
		// TODO Auto-generated method stub
	}

	@Override
	public boolean remove(I cacheInfo) {
		return memory.remove(cacheInfo) == null;
	}
	
	@Override
	public int getPersistenceMask(I cacheInfo) {
		int mask = 0;
		if(memory.containsKey(cacheInfo))
			mask |= PERSIST_IN_MEMORY;
		return mask;
	}

	@Override
	public long approximateDiskCacheSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long approximateMemoryCacheSize() {
		synchronized (memory) {
			return memoryUsed;
		}
	}
	
	protected void ensureFreeSpace(long ensuredFreeSpace) {
		synchronized (memory) {
			List<I> toRemove = new ArrayList<I>();
			for(I i : memory.keySet()) {
				if(memoryUsed + ensuredFreeSpace <= maxMemory)
					break;
				memoryUsed -= i.approximateSize();
				toRemove.add(i);
			}
			for(I i : toRemove)
				memory.remove(i);
		}
	}

}
