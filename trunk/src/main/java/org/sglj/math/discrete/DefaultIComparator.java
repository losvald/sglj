/*
 * DefaultIComparator.java
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
public class DefaultIComparator extends AbstractIComparator {

	@Override
	public boolean iLt(int a, int b) {
		return a < b;
	}

	@Override
	public boolean iGt(int a, int b) {
		return a > b;
	}

	@Override
	public boolean iLe(int a, int b) {
		return a <= b;
	}

	@Override
	public boolean iGe(int a, int b) {
		return a >= b;
	}

	@Override
	public boolean iEq(int a, int b) {
		return a == b;
	}

	@Override
	public boolean iNe(int a, int b) {
		return a != b;
	}

}
