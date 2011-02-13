/*
 * BinaryPredicate.java
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
 * A predicate composed of two operands. The <tt>holds</tt> method
 * should check whether the binary relation holds.
 * 
 * @author Leo Osvald
 *
 */
public interface BinaryPredicate {

//	tmp_file="/tmp/tmp"; xsel > "$tmp_file"; types="Boolean Byte Char Short Long Float Double Type"; prim="boolean byte char short long float double T"; ucase_prim="BOOLEAN BYTE CHAR SHORT LONG FLOAT DOUBLE TYPE"; types_arr=($types); prim_arr=($prim); ucase_prim_arr=($ucase_prim); echo "" > "$tmp_file"2; for ((i=0;i<7;++i)); do t=${types_arr[i]}; p=${prim_arr[i]}; prim_file=/tmp/$p; cat "$tmp_file" | sed 's/int\( \|s\)/'$p'\1/g' | sed 's/Int/'$t'/g' | sed 's/INT/'${ucase_prim_arr[i]}'/g' | sed 's/TypeBinaryPredicate/&<T>/g' >> "$tmp_file"2; echo -e "\n\n" >> "$tmp_file"2; done; cat "$tmp_file"2 | xsel -b
	
	// BEGIN HAND-CODED
	
	/**
	 * A predicate of two types.
	 * @see BinaryPredicate
	 */
	public interface TypeBinaryPredicate<T> {
		/**
		 * Checks whether this binary predicate holds.
		 * @param a first int
		 * @param b second int
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(T a, T b);
	}
	
	// BEGIN XSEL
	
	/**
	 * A predicate of two ints.
	 * @see BinaryPredicate
	 */
	public interface IntBinaryPredicate {
		/**
		 * Checks whether this binary predicate holds.
		 * @param a first int
		 * @param b second int
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(int a, int b);
	}
	
	// END XSEL
	
	// END HAND-CODED
	
	/**
	 * A predicate of two booleans.
	 * @see BinaryPredicate
	 */
	public interface BooleanBinaryPredicate {
		/**
		 * Checks whether this binary predicate holds.
		 * @param a first int
		 * @param b second int
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(boolean a, boolean b);
	}
	
	
	/**
	 * A predicate of two bytes.
	 * @see BinaryPredicate
	 */
	public interface ByteBinaryPredicate {
		/**
		 * Checks whether this binary predicate holds.
		 * @param a first int
		 * @param b second int
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(byte a, byte b);
	}
	
	
	/**
	 * A predicate of two chars.
	 * @see BinaryPredicate
	 */
	public interface CharBinaryPredicate {
		/**
		 * Checks whether this binary predicate holds.
		 * @param a first int
		 * @param b second int
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(char a, char b);
	}
	
	
	/**
	 * A predicate of two shorts.
	 * @see BinaryPredicate
	 */
	public interface ShortBinaryPredicate {
		/**
		 * Checks whether this binary predicate holds.
		 * @param a first int
		 * @param b second int
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(short a, short b);
	}
	
	
	/**
	 * A predicate of two longs.
	 * @see BinaryPredicate
	 */
	public interface LongBinaryPredicate {
		/**
		 * Checks whether this binary predicate holds.
		 * @param a first int
		 * @param b second int
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(long a, long b);
	}
	
	
	/**
	 * A predicate of two floats.
	 * @see BinaryPredicate
	 */
	public interface FloatBinaryPredicate {
		/**
		 * Checks whether this binary predicate holds.
		 * @param a first int
		 * @param b second int
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(float a, float b);
	}
	
	
	/**
	 * A predicate of two doubles.
	 * @see BinaryPredicate
	 */
	public interface DoubleBinaryPredicate {
		/**
		 * Checks whether this binary predicate holds.
		 * @param a first int
		 * @param b second int
		 * @return <code>true</code> if the predicate holds, <code>false</code>
		 * otherwise.
		 */
		boolean holds(double a, double b);
	}
	
}
