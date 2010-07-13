/*
 * OccurrenceMap.java
 * 
 * Copyright (C) 2009 Leo Osvald <leo.osvald@gmail.com>
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

/**
 * <p>Map wrapper class which primary purpose is counting number of 
 * occurrences of objects of certain type.<br>
 * An underlying map can be specified in constructor, and its type
 * must be passed as template argument.<br>
 * The underlying map can be accessed through {@link #getMap()} method.
 * All changes made to the map reflects in the multimap and vice-versa.</p>
 * <p>Example:<br>
 * <pre>
 * OccurrenceMap&lt String, HashMap&lt String, Integer> &gt countMap = 
 * 	new OccurrenceMap&lt String, HashMap&lt String, Integer&gt &gt(
 * 		new HashMap&lt String, Integer&gt ());
 * 
 * System.out.println(countMap.increment("an")); //true
 * System.out.println(countMap.increment("an")); //false
 * System.out.println(countMap.increment("example")); //true
 * System.out.println(countMap.getCount("an")); //2
 * System.out.println(countMap.getMap().get("an")); //2
 * System.out.println(countMap.decrement("example")); //true
 * System.out.println(countMap.getCount("example")); //0
 * System.out.println(countMap.decrement("an")); //false
 * System.out.println(countMap.decrement("an")); //true
 * countMap.getMap().put("example", 5);
 * System.out.println(countMap.getCount("example")); //5
 * </pre>
 * </p>
 * 
 * 
 * @author Leo Osvald
 *
 * @param <K> type which is counted
 * @param <T> underlying map which is used
 * 
 * @version 0.9
 */
public class OccurrenceMap<K, T extends Map<K, Integer> > {
	
	private T map;
	
	/**
	 * Constructor.<br>
	 * If specified map is not empty, this multimap will adjust 
	 * its contents accordingly.
	 * 
	 * @param map underlying map which is backed by this multimap
	 */
	public OccurrenceMap(T map) {
		this.map = map;
	}
	
	@SuppressWarnings("unchecked")
	public OccurrenceMap() {
		this.map = (T)new HashMap<K, Integer>();
	}
	
	/**
	 * Returns the underlying map.
	 * The map is backed by this multimap and all changes made through
	 * it will be reflected in the multimap, and vice-versa.
	 * @return map
	 */
	public T getMap() {
		return map;
	}

	/**
	 * Increments the number of occurrences for the specified element/key.
	 * @param key object which is counted
	 * @return <code>true</code> if this is the first occurrence, that is,
	 * a new entry has been added to the underlying map, 
	 * <code>false</code> otherwise.
	 */
	public boolean increment(K key) {
		Integer val = map.get(key);
		if(val == null) {
			map.put(key, 1);
			return true;
		}
		map.put(key, val+1);
		return false;
	}
	
	/**
	 * Decrements the number of occurrences for the specified element/key.
	 * If this was the last occurrence for the specified element/key,
	 * the corresponding entry in the underlying map is removed
	 * and <code>true</code> is returned.
	 * @param key object which is counted
	 * @return <code>true</code> if entry was removed (or did not exist), 
	 * <code>false</code> otherwise.
	 */
	public boolean decrement(K key) {
		Integer val = map.get(key);
		if(val != null) {
			if(val == 1) {
				map.remove(key);
				return true;
			}
			map.put(key, val-1);
			return false;
		}
		return false;
	}
	
	/**
	 * Returns the number of occurrences for specified element/key.
	 * @param key object which is counted
	 * @return number of occurrences (0 if there are no occurrences)
	 */
	public int count(K key) {
		Integer val = map.get(key);
		if(val == null) return 0;
		return val;
	}
	
}
