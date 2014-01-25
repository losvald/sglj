/*
 * EnumArrayMap.java
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

import java.util.Arrays;

/**
 * Fixed size array-based map for enumerations with small number
 * of values.
 * 
 * @author Leo Osvald
 * @version 0.6
 *
 * @param <K> key type
 * @param <V> value type
 */
public class EnumArrayMap<K extends Enum<K>, V> {
	
	private final V[] arr;
	private final boolean[] containsFlag; 
	
	/**
	 * Constructs a new array-based map with capacity equal to <code>valueCount</code>.
	 * @param valueCount number of values in the enumeration (can be obtained
	 * by calling .values().length mathod)
	 */
	
	@SuppressWarnings("unchecked")
	public EnumArrayMap(int valueCount) {
		arr = (V[]) new Object[valueCount];
		containsFlag = new boolean[valueCount];
	}
	
	public V get(K key) {
		return arr[key.ordinal()];
	}
	
	public V put(K key, V value) {
		V oldValue = get(key);
		arr[key.ordinal()] = value;
		containsFlag[key.ordinal()] = true;
		return oldValue;
	}
	
	public boolean containsKey(K key) {
		return containsFlag[key.ordinal()];
	}
	
	public void clear() {
		Arrays.fill(arr, null);
		Arrays.fill(containsFlag, false);
	}
	
	public V remove(K key) {
		if(key == null)
			return null;
		
		int ord = key.ordinal();
		V oldValue = arr[ord];
		arr[ord] = null;
		containsFlag[ord] = false;
		return oldValue;
	}
	
}
