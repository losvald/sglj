/*
 * SetUtils.java
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

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Utility class with various methods regarding sets.
 * 
 * @author Leo Osvald
 *
 */
public class SetUtils {
	
	protected SetUtils() {
	}
	
	/**
	 * Sets contents of set <code>dest</code> to match the one from
	 * <code>src</code> collection (ignoring duplicates).<br>
	 * The equivalence of the assignment operator (=) for primitive types.
	 * @param <T>
	 * @param dest
	 * @param src
	 */
	public static <T> void copy(Set<? super T> dest, Collection<? extends T> src) {
		dest.retainAll(src);
		dest.addAll(src);
	}
	
	/**
	 * Moves all the contents from <code>src</code> collection to 
	 * <code>dest</code> set.
	 * @param <T>
	 * @param dest
	 * @param src
	 */
	public static <T> void move(Set<? super T> dest, Collection<? extends T> src) {
		dest.retainAll(src);
		dest.addAll(src);
		src.clear();
	}
	
	/**
	 * Moves all elements contained in <code>elements</code> collection
	 * from <code>src</code> set to <code>dest</code> set.
	 * @param <T> type
	 * @param dest set to which the elements should be moved
	 * @param src set from which the elements should be moved
	 * @param elements collection which contains elements which
	 * should be moved from one set to another.
	 */
	public static <T> void moveElements(Set<? super T> dest, Set<? extends T> src, 
			Collection<T> elements) {
		dest.addAll(elements);
		src.removeAll(elements);
	}
	
	/**
	 * Returns the reverse comparator.<br>
	 * That comparator's {@link Comparator#compare(Object, Object)} method
	 * will the same absolute value but with reversed sign.
	 * @param <T> type
	 * @param cmp comparator
	 * @return reverse comparator
	 */
	public static <T> Comparator<T> reverseComparator(final Comparator<T> cmp) {
		return new Comparator<T>() {
			@Override 
			public int compare(T o1, T o2) {
				return -cmp.compare(o1, o2);
			}
		};
	}
	
	/**
	 * Clears the initial set, and creates a new one with same contents
	 * and has the specified comparator.
	 * @param <T> type
	 * @param set set which is cleared and whose elements should contain
	 * the newly create one (which is returned)
	 * @param cmp comparator
	 * @return copy of the specified set with the specified comparator
	 */
	public static <T> TreeSet<T> changeComparator(TreeSet<T> set, 
			Comparator<? super T> cmp) {
		TreeSet<T> ret = new TreeSet<T>(cmp);
		ret.addAll(set);
		set.clear();
		return ret;
	}
	
	/**
	 * Returns the set difference of the two sets, A \ B.
	 * Collections <code>a</code> and <code>b</code> will remain unchanged,
	 * and <code>result</code> collection will contain the elements
	 * which belongs to the set A\B. Unique elements from collections 
	 * <code>a</code> and <code>b</code> form the corresponding sets A and B.
	 * @param <E> type of the elements in the two sets
	 * @param <T> return type (some set)
	 * @param a collection
	 * @param b collection
	 * @param result set set which should contain the result
	 * of the set difference
	 * @return result set - which was passed as the last argument
	 */
	public static <E, T extends Set<E>> T difference(Collection<E> a, 
			Collection<E> b, T result) {
		if(!result.isEmpty()) result.clear();
		result.addAll(a);
		result.removeAll(b);
		return result;
	}
	
	/**
	 * Returns the set intersection of the two sets, A &cap B.
	 * Collections <code>a</code> and <code>b</code> will remain unchanged,
	 * and <code>result</code> collection will contain the elements
	 * which belongs to the set A &cap B. Unique elements from collections 
	 * <code>a</code> and <code>b</code> form the corresponding sets A and B.
	 * @param <E> type of the elements in the two sets
	 * @param <T> return type (some set)
	 * @param a collection
	 * @param b collection
	 * @param result set set which should contain the result
	 * of the set intersection
	 * @return result set - which was passed as the last argument
	 */
	public static <E, T extends Set<E>> T intersection(Collection<E> a, Collection<E> b, T result) {
		if(!result.isEmpty()) result.clear();
		result.addAll(a);
		result.retainAll(b);
		return result;
	}
	
	/**
	 * Returns the set union of the two sets, A &cup B.
	 * Collections <code>a</code> and <code>b</code> will remain unchanged,
	 * and <code>result</code> collection will contain the elements
	 * which belongs to the set A &cup B. Unique elements from collections 
	 * <code>a</code> and <code>b</code> form the corresponding sets A and B.
	 * @param <E> type of the elements in the two sets
	 * @param <T> return type (some set)
	 * @param a collection
	 * @param b collection
	 * @param result set set which should contain the result
	 * of the set union
	 * @return result set - which was passed as the last argument
	 */
	public static <E, T extends Set<E>> T union(Collection<E> a, Collection<E> b, T result) {
		if(!result.isEmpty()) result.clear();
		result.addAll(a);
		result.addAll(b);
		return result;
	}
	
	/**
	 * Returns the set union of the two sets, A &Delta B.
	 * Collections <code>a</code> and <code>b</code> will remain unchanged,
	 * and <code>result</code> collection will contain the elements
	 * which belongs to the set A &Delta B. Unique elements from collections 
	 * <code>a</code> and <code>b</code> form the corresponding sets A and B.
	 * @param <E> type of the elements in the two sets
	 * @param <T> return type (some set)
	 * @param a collection
	 * @param b collection
	 * @param result set set which should contain the result
	 * of the set union
	 * @return result set - which was passed as the last argument
	 */
	public static <E, T extends Set<E>> T symmetricDifference(Set<E> a, Set<E> b, 
			T result) {
		if(!result.isEmpty()) result.clear();
		for(E curr : a)
			if(!b.contains(curr))
				result.add(curr);
		for(E curr : b)
			if(!a.contains(curr))
				result.add(curr);
		return result;
	}
	
}
