/*
 * AbstractIComparator.java
 *
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
 *
 * This file is part of YOUR PROGRAM NAME.
 * 
 * YOUR PROGRAM NAME is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * YOUR PROGRAM NAME is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with YOUR PROGRAM NAME. If not, see <http://www.gnu.org/licenses/>.
 */

package org.sglj.math.discrete;

/**
 * TODO
 * 
 * @author Leo Osvald
 *
 */
public abstract class AbstractIComparator implements IComparator {

	@Override
	public boolean lt(Integer a, Integer b) {
		return iLt(a, b);
	}

	@Override
	public boolean gt(Integer a, Integer b) {
		return iGt(a, b);
	}

	@Override
	public boolean le(Integer a, Integer b) {
		return iLe(a, b);
	}

	@Override
	public boolean ge(Integer a, Integer b) {
		return iGe(a, b);
	}

	@Override
	public boolean eq(Integer a, Integer b) {
		return iEq(a, b);
	}

	@Override
	public boolean ne(Integer a, Integer b) {
		return iNe(a, b);
	}

	@Override
	public int compare(Integer o1, Integer o2) {
		if (eq(o1, o2))
			return 0;
		return le(o1, o2) ? -1 : 1;
	}

}
