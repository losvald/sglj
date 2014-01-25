/*
 * IndexedStaticCollection.java
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


/**
 * A static collection which is indexed with indices from range
 * [0, {@link #size()}-1].
 * 
 * @author Leo Osvald
 * @version 0.8
 *
 * @param <E>
 */
public interface IndexedStaticCollection<E> extends StaticCollection<E> {
	
	/**
	 * Retrieves the element indexed by the specified index.
	 * 
	 * @param index index of the element to be retrieved
	 * @return
	 * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= indexCount()</tt>)
	 */
	E get(int index);
	
	/**
	 * Replaces the element indexed by the specified index.
	 * 
	 * @param index index of the element to replace
	 * @param element element to be replaced with
	 * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= indexCount()</tt>)
	 */
	void set(int index, E element);
	
	/**
	 * Returns the number of indices. This is the greatest allowed index + 1.
	 * (because indices range from 0 to {@link #indexCount()} - 1) 
	 * 
	 * @return the number of indices
	 */
	int indexCount();
	
	/**
	 * Returns the iterator which iterates from the specified index.
	 * (if the index is 0 this method call is equivalent to 
	 * the {@link #iterator()} method).
	 * 
	 * @param index starting index for iterating
	 * @return iterator which iterates from the specified index
	 */
	StaticIterator<E> iterator(int index);
}
