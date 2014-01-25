/*
 * HashMultiMap.java
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

package org.sglj.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of the {@link MultiMap} interface based
 * on {@link HashMap}. 
 * 
 * @author Leo Osvald
 *
 * @param <K>
 * @param <V>
 */
public class HashMultiMap<K, V> extends AbstractMultiMap<K, V, HashMap<K,Set<V>>> {
	
	private static final long serialVersionUID = 1L;
	
	public static final float DEFAULT_LOAD_FACTOR = 0.75f;
	
	@SuppressWarnings("unchecked")
	public HashMultiMap() {
		super(new HashMap());
	}
	
	public HashMultiMap(Map<? extends K, ? extends V> map) {
		this();
		this.putAll(map);
	}
	
	public HashMultiMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}
	
	@SuppressWarnings("unchecked")
	public HashMultiMap(int initialCapacity, float loadFactor) {
		super(new HashMap(initialCapacity, loadFactor));
	}
	
}
