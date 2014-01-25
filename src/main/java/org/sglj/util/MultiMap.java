/*
 * MultiMap.java
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

import java.util.Map;
import java.util.Set;

/**
 * Extension of interface {@link Map} which enable multiple values
 * to be mapped by the same key; this collection maps key to a set
 * of values.
 * 
 * @author Leo Osvald
 * @version 1.0
 * @param <K> type of key
 * @param <V> type of value
 */
public interface MultiMap<K, V> extends Map<K, V> {
	
	/**
	 * Returns set of all values associated with given key.<br>
	 * The returned set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa. If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own <tt>remove</tt> operation), the results of
     * the iteration are undefined. The set supports all operations.
	 * @param key key
	 * @return non-empty set if the key maps to at least one value,
	 * or <code>null</code> otherwise.
	 */
	Set<V> getAll(K key);
	
	/**
	 * Removes all values associated with given key.
	 * @param key key
	 * @return set of all values that were removed, or <code>null</code>
	 * if none were removed.
	 */
	Set<V> removeAll(K key);
	
	/**
	 * Returns the number of values associated with given key.
	 * @param key key
	 * @return number of values
	 */
	int getValueCount(K key);
	
	/**
	 * Checks whether specified key maps to specified value.
	 * @param key key
	 * @param value value
	 * @return <code>true</code> if such mapping exists, 
	 * <code>false</code> otherwise.
	 */
	boolean containsEntry(K key, V value);
	
	/**
	 * If there exists the mapping (entry) <code>key</code>-><code>value</code>
	 * exists, removes it.
	 * @param value value
	 * @return <code>true</code> if removal succeeded, 
	 * <code>false</code> otherwise.
	 */
	boolean removeEntry(K key, V value);
}
