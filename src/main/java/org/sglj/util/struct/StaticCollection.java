/*
 * StaticCollection.java
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

package org.sglj.util.struct;

import java.util.Collection;

/**
 * The root interface which represents a group of objects,
 * known as its elements. Unlike the {@link Collection} this interface
 * does not require insertion or deletion of elements. The mutability
 * of elements may or may not be p
 * 
 * @author Leo Osvald
 * @version 0.8
 * 
 * @param <E> the type of elements held in this collection
 */
public interface StaticCollection<E> {
	/**
	 * Returns the number of elements in this static collection.
	 * 
	 * @return the number of elements in this static collection
	 */
	int size();
	
	/**
	 * Returns the size of the underlying structure which holds elements.
	 * The capacity is guaranteed to be greater or equal to its size.
	 * 
	 * @return the capacity
	 */
	int capacity();
	
	/**
	 * Checks whether this static collection contains no elements.
	 * 
	 * @return <code>true</code> if this static collection contains no 
	 * elements.
	 */
	boolean isEmpty();
	
	/**
	 * Returns an iterator over the elements in this static collection.
	 * 
	 *@return an <tt>StaticIterator</tt> over the elements in this collection
	 */
	StaticIterator<E> iterator();
}
