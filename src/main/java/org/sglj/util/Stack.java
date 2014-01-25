/*
 * Stack.java
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

import java.util.Collection;

/**
 * <p>This interface is the extension of the 
 * {@link Collection} interface, which introduces some methods
 * which are typical for a LIFO structure.<br>
 * All classes that provide efficient implementation of this
 * method and a LIFO functionality should implement this interface.<br>
 * In general, implementations should not permit insertion of
 * <code>null</code> values.</p>
 * <p>As opposed to the {@link java.util.Stack}, this collection
 * is <b>not</b> thread-safe.</p>
 * 
 * @author Leo Osvald
 * @version 1.0
 * @param <E>
 */
public interface Stack<E> extends Collection<E> {
	
	/**
	 * Pushes an element onto the stack.
	 * @param e the element which should be pushed onto the stack
	 */
	void push(E e);
	
	/**
	 * Returns the top element of the stack.
	 * @return element or <code>null</code> if the stack is empty.
	 */
	E top();
	
	/**
	 * Removes the top element of the stack and returns it.
	 * @return the popped element or <code>null</code> if the stack was empty.
	 */
	E pop();
}
