/*
 * AbstractStaticCollection.java
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

import java.util.List;

/**
 * Abstract implementation of the {@link StaticCollection} interface.
 * 
 * @author Leo Osvald
 * @version 0.8
 *
 * @param <E>
 */
public abstract class AbstractStaticCollection<E> 
implements StaticCollection<E> {

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		StaticIterator<E> i = iterator();
		while (i.hasNext()) {
		    E obj = i.next();
		    hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
		}
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
		    return true;
		if (!(o instanceof List))
		    return false;

		StaticIterator<E> it1 = iterator();
		StaticIterator<?> it2 = ((StaticCollection<?>) o).iterator();
		while(it1.hasNext() && it2.hasNext()) {
		    E o1 = it1.next();
		    Object o2 = it2.next();
		    if (!(o1 == null ? o2 == null : o1.equals(o2)))
		    	return false;
		}
		return !(it1.hasNext() || it2.hasNext());
	}
	
	@Override
	public String toString() {
		StaticIterator<E> it = iterator();
		if (!it.hasNext())
			return "[]";

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		while (true) {
			E e = it.next();
			sb.append(e == this ? "(this Collection)" : e);
			if (!it.hasNext())
				return sb.append(']').toString();
			sb.append(", ");
		}
	}

}
