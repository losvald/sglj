/*
 * UnaryPredicate.java
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

package org.sglj.math;

/**
 * A predicate composed of a single operand. The <tt>holds</tt> method
 * should check whether the unary relation holds.
 * 
 * @author Leo Osvald
 *
 */
public interface UnaryPredicate {

//	tmp_file="/tmp/tmp"; xsel > "$tmp_file"; types="Boolean Byte Char Short Long Float Double Type"; prim="boolean byte char short long float double T"; ucase_prim="BOOLEAN BYTE CHAR SHORT LONG FLOAT DOUBLE TYPE"; types_arr=($types); prim_arr=($prim); ucase_prim_arr=($ucase_prim); echo "" > "$tmp_file"2; for ((i=0;i<7;++i)); do t=${types_arr[i]}; p=${prim_arr[i]}; prim_file=/tmp/$p; cat "$tmp_file" | sed 's/int\( \|s\)/'$p'\1/g' | sed 's/a int/a '$p'/g' | sed 's/single int/single '$p'/g' | sed 's/Int/'$t'/g' | sed 's/INT/'${ucase_prim_arr[i]}'/g' | sed 's/TypeUnaryPredicate/&<T>/g' >> "$tmp_file"2; echo -e "\n\n" >> "$tmp_file"2; done; cat "$tmp_file"2 | xsel -b
	
	// BEGIN HAND-CODED
	
	/**
	 * A predicate of a single type.
	 * @see UnaryPredicate
	 */
	public interface TypeUnaryPredicate<T> {
		/**
		 * Checks whether this unary predicate holds.
		 * @param a type
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(T a);
	}
	
	// BEGIN XSEL
	
	/**
	 * A predicate of a single int.
	 * @see UnaryPredicate
	 */
	public interface IntUnaryPredicate {
		/**
		 * Checks whether this unary predicate holds.
		 * @param a int
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(int a);
	}
	
	// END XSEL
	
	// END HAND-CODED
	

	/**
	 * A predicate of a single boolean.
	 * @see UnaryPredicate
	 */
	public interface BooleanUnaryPredicate {
		/**
		 * Checks whether this unary predicate holds.
		 * @param a boolean
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(boolean a);
	}


	/**
	 * A predicate of a single byte.
	 * @see UnaryPredicate
	 */
	public interface ByteUnaryPredicate {
		/**
		 * Checks whether this unary predicate holds.
		 * @param a byte
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(byte a);
	}


	/**
	 * A predicate of a single char.
	 * @see UnaryPredicate
	 */
	public interface CharUnaryPredicate {
		/**
		 * Checks whether this unary predicate holds.
		 * @param a char
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(char a);
	}


	/**
	 * A predicate of a single short.
	 * @see UnaryPredicate
	 */
	public interface ShortUnaryPredicate {
		/**
		 * Checks whether this unary predicate holds.
		 * @param a short
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(short a);
	}


	/**
	 * A predicate of a single long.
	 * @see UnaryPredicate
	 */
	public interface LongUnaryPredicate {
		/**
		 * Checks whether this unary predicate holds.
		 * @param a long
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(long a);
	}


	/**
	 * A predicate of a single float.
	 * @see UnaryPredicate
	 */
	public interface FloatUnaryPredicate {
		/**
		 * Checks whether this unary predicate holds.
		 * @param a float
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(float a);
	}



	/**
	 * A predicate of a single double.
	 * @see UnaryPredicate
	 */
	public interface DoubleUnaryPredicate {
		/**
		 * Checks whether this unary predicate holds.
		 * @param a double
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(double a);
	}
	
}
