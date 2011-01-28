/*
 * AbstractIAdditive.java
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
public abstract class AbstractIAdditive implements IAdditive {
	@Override
	public Integer add(Integer a, Integer b) {
		return iAdd(a, b);
	}

	@Override
	public Integer addIdentityElement() {
		return iAddIdentityElement();
	}

	@Override
	public Integer addInverse(Integer a) {
		return iAddInverse(a);
	}
}
