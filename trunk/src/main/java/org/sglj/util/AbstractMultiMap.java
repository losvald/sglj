/*
 * AbstractMultiMap.java
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Abstract implementation of the {@link MultiMap} interface.<br>
 * Multiple values associated to the same key are stored using
 * the {@link HashSet} class. Therefore, values are compared
 * using their {@link #hashCode()} and {@link #equals(Object)} method
 * which means that no two distinct values can be mapped to the same key.<br>
 * 
 * @param <K> type of the key
 * @param <V> type of values stored by the map
 * @param <M> type of the underlying map which this multimap uses
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public class AbstractMultiMap<K, V, M extends Map<K, Set<V>>> 
implements MultiMap<K, V>, Serializable {

	private static final long serialVersionUID = 1285863430479515409L;
	
	protected static final int DEFAULT_INNER_SET_INITIAL_CAPACITY = 4;
	
	private int size;
	private final Map<K, Set<V>> map;
	
	public AbstractMultiMap(M map) {
		this.map = map;
		for(Set<V> s : map.values()) {
			size += s.size();
		}
	}

	@Override
	public Set<V> getAll(K key) {
		return map.get(key);
	}

	@Override
	public Set<V> removeAll(K key) {
		if(map.containsKey(key))
			map.get(key).clear();
		return map.remove(key);
	}
	
	public int getValueCount(K key) {
		if(!map.containsKey(key))
			return 0;
		return map.get(key).size();
	}

	public boolean containsEntry(K key, V value) {
		return containsKey(key) && getAll(key).contains(value);
	}
	
	public boolean removeEntry(K key, V value) {
		if(!containsEntry(key, value))
			return false;
		getAll(key).remove(value);
		return true;
	}
	
	@Override
	public void clear() {
		map.clear();
		size = 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		for(Set<V> s : map.values())
			if(s.contains(value))
				return true;
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	throws IllegalStateException {
		throw new IllegalStateException("Operation not yet implemented!");
	}

	@Override
	public V get(Object key) {
		if(map.containsKey(key))
			return firstVal(map.get(key));
		return null;
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public V put(K key, V value) {
		if(map.containsKey(key)) {
			map.get(key).add(value);
			return value;
		} else {
			map.put(key, createInnerSet(value, initialSetCapacity()));
			return null;
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for(Entry<? extends K, ? extends V> e : m.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	@Override
	public V remove(Object key) {
		if(map.containsKey(key)) {
			V someVal = firstVal(map.get(key));
			if(map.get(key).remove(someVal)) {
				if(map.get(key).isEmpty())
					map.remove(key);
				return someVal;
			}
			return null;
		}
		return null;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Collection<V> values() {
		ArrayList<V> vals = new ArrayList<V>();
		for(Set<V> s : map.values()) {
			vals.addAll(s);
		}
		return vals;
	}
	
	protected Set<V> createInnerSet(V value, int initialCapacity) {
		return new InnerSet(value, initialCapacity);
	}
	
	protected int initialSetCapacity() {
		return DEFAULT_INNER_SET_INITIAL_CAPACITY;
	}
	
	protected V firstVal(Set<V> set) {
		for(V val : set)
			return val;
		return null;
	}
	
	@Override
	public String toString() {
		if(map.isEmpty())
			return "{}";
		StringBuffer sb = new StringBuffer("{");
		boolean first = true;
		for(Entry<K, Set<V>> e : map.entrySet()) {
			if(!e.getValue().isEmpty()) {
				if(!first) {
					sb.append(", ");
				} else {
					first = false;
				}
				sb.append(e.getKey()).append("=>").append(e.getValue());
			}
		}
		sb.append("}");
		return sb.toString();
	}
	
	private class InnerSet extends HashSet<V> {

		private static final long serialVersionUID = -7694980022010327893L;
		
		public InnerSet(V value, int initialCapacity) {
			super(initialCapacity);
			add(value);
		}
		
		public boolean add(V e) {
			if(super.add(e)) {
				++size;
				return true;
			}
			return false;
		}
		
		@Override
		public boolean addAll(Collection<? extends V> c) {
			int oldSize = super.size();
			boolean changed = super.addAll(c);
			size += super.size() - oldSize;
			return changed;
		}
		
		@Override
		public boolean remove(Object o) {
			if(super.remove(o)) {
				--size;
				return true;
			}
			return false;
		}
		
		@Override
		public boolean removeAll(Collection<?> c) {
			int oldSize = super.size();
			boolean changed = super.removeAll(c);
			size += super.size() - oldSize;
			return changed;
		}
		
		@Override
		public boolean retainAll(Collection<?> c) {
			int oldSize = super.size();
			boolean changed = super.retainAll(c);
			size += super.size() - oldSize;
			return changed;
		}
		
		@Override
		public void clear() {
			super.clear();
			size = 0;
		}
	}

}
