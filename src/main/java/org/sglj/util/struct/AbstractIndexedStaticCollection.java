/*
 * AbstractIndexedStaticCollection.java
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

import java.util.NoSuchElementException;

/**
 * Abstract implementation of the {@link IndexedStaticCollection} interface.
 * 
 * @author Leo Osvald
 *
 * @param <E>
 */
public abstract class AbstractIndexedStaticCollection<E>
extends AbstractStaticCollection<E>
implements IndexedStaticCollection<E> {

	private static class IndexedStaticIterator<E> 
	implements StaticIterator<E> {
		final IndexedStaticCollection<E> c;
		int pos;
		
		IndexedStaticIterator(IndexedStaticCollection<E> c, int index) {
			this.c = c;
			this.pos = index - 1;
		}

		@Override
		public boolean hasNext() {
			int indexCount = c.indexCount();
			if (pos + 1 >= indexCount)
				return false;
			
			// optimize for speed (most common case)
			if (c.get(pos + 1) != null)
				return true;
			
			for (int i = pos + 1; i < indexCount; ++i)
				if (c.get(i) != null)
					return true;
			
			return false;
		}

		@Override
		public E next() {
			if (pos + 1 >= c.indexCount())
				throw new NoSuchElementException();
			E e;
			do {
				e = c.get(++pos);
			} while (e == null);
			return e;
		}
		
	}
	
	@Override
	public StaticIterator<E> iterator() {
		return iterator(0);
	}
	
	@Override
	public StaticIterator<E> iterator(int index) {
		return new IndexedStaticIterator<E>(this, 0);
	}
	
	@Override
	public int indexCount() {
		return capacity();
	}

}
