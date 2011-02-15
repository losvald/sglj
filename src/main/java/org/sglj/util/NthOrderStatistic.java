/*
 * NthOrderStatistic.java
 *
 * Copyright (C) 2011 Leo Osvald <leo.osvald@gmail.com>
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

import java.util.Arrays;


/**
 * A set of methods for calculating nth smallest/largest element
 * in linear time (proportional to the range being searched).<br>
 *
 * @author Leo Osvald
 *
 */
public class NthOrderStatistic {

	public static int nthElement(int[] a, int n, int fromIndex, int toIndex) {
		rangeCheck(a, fromIndex, toIndex);
		nthElement0(a, n + fromIndex, fromIndex, toIndex);
		ArrayUtils.swap(a, fromIndex, n + fromIndex);
		return a[fromIndex + n];
	}

	public static int nthElement(int[] a, int n) {
		nthElement0(a, n, 0, a.length);
		ArrayUtils.swap(a, 0, n);
		return a[n];
	}

	private static int partition(int[] a, int fromIndex, int toIndex,
			int pivotIndex) {
		int pivotValue = a[pivotIndex];
		ArrayUtils.swap(a, pivotIndex, toIndex - 1);
		for (int i = fromIndex; i < toIndex; ++i) {
			if (a[i] < pivotValue)
				ArrayUtils.swap(a, i, fromIndex++);
		}
		ArrayUtils.swap(a, fromIndex, toIndex - 1);
		return fromIndex;
	}

	/*
	 * Median of medians algorithm to find nth element in O(N)
	 */
	private static void nthElement0(int[] a, int n, int fromIndex, int toIndex) {
		if (fromIndex < toIndex) {
			int len = toIndex - fromIndex;
			if (len < 0) {
				for (int current = fromIndex; ++current < toIndex; ) {
					int tmp = a[current];
					int i = current;
					for (int tmp1 = a[i - 1]; tmp < tmp1;
					tmp1 = a[--i - 1] ) {
						a[i] = tmp1;
						if (fromIndex == i - 1) {
							--i;
							break;
						}
					}
					a[i] = tmp;
				}
			    ArrayUtils.swap(a, n, fromIndex);
			} else {
				int lo = fromIndex;
				int groupCount = len / 5;
				for (int group = 0; group < groupCount; ++group, lo += 5) {
					int j = med5(a, lo, lo + 1, lo + 2, lo + 3, lo + 4);
					ArrayUtils.swap(a, j, fromIndex + group);
				}

				nthElement0(a, fromIndex + (groupCount >>> 1),
				fromIndex, fromIndex + groupCount);
				int pivotIndex = partition(a, fromIndex, toIndex, fromIndex);
				if (n < pivotIndex)
					nthElement0(a, n, fromIndex, pivotIndex);
				else if (n > pivotIndex) {
					nthElement0(a, n, pivotIndex + 1, toIndex);
					ArrayUtils.swap(a, pivotIndex + 1, fromIndex);
				} else {
					ArrayUtils.swap(a, pivotIndex, fromIndex);
				}
			}
			return ;
		}
		assert false : "Bug detected"; // impossible
		return ;
	}

	private static void rangeCheck(int[] a, int fromIndex, int toIndex) {
		if (fromIndex < 0 || toIndex > a.length)
			throw new IndexOutOfBoundsException();
		if (fromIndex > toIndex)
			throw new IllegalArgumentException();
	}

	public static int med5(int x[], int a, int b, int c, int d, int e) {
		return x[b] < x[a] ? x[d] < x[c] ? x[b] < x[d] ? x[a] < x[e] ? x[a] < x[d] ? x[e] < x[d] ? e : d
                : x[c] < x[a] ? c : a
        : x[e] < x[d] ? x[a] < x[d] ? a : d
                : x[c] < x[e] ? c : e
: x[c] < x[e] ? x[b] < x[c] ? x[a] < x[c] ? a : c
                : x[e] < x[b] ? e : b
        : x[b] < x[e] ? x[a] < x[e] ? a : e
                : x[c] < x[b] ? c : b
: x[b] < x[c] ? x[a] < x[e] ? x[a] < x[c] ? x[e] < x[c] ? e : c
                : x[d] < x[a] ? d : a
        : x[e] < x[c] ? x[a] < x[c] ? a : c
                : x[d] < x[e] ? d : e
: x[d] < x[e] ? x[b] < x[d] ? x[a] < x[d] ? a : d
                : x[e] < x[b] ? e : b
        : x[b] < x[e] ? x[a] < x[e] ? a : e
                : x[d] < x[b] ? d : b
: x[d] < x[c] ? x[a] < x[d] ? x[b] < x[e] ? x[b] < x[d] ? x[e] < x[d] ? e : d
                : x[c] < x[b] ? c : b
        : x[e] < x[d] ? x[b] < x[d] ? b : d
                : x[c] < x[e] ? c : e
: x[c] < x[e] ? x[a] < x[c] ? x[b] < x[c] ? b : c
                : x[e] < x[a] ? e : a
        : x[a] < x[e] ? x[b] < x[e] ? b : e
                : x[c] < x[a] ? c : a
: x[a] < x[c] ? x[b] < x[e] ? x[b] < x[c] ? x[e] < x[c] ? e : c
                : x[d] < x[b] ? d : b
        : x[e] < x[c] ? x[b] < x[c] ? b : c
                : x[d] < x[e] ? d : e
: x[d] < x[e] ? x[a] < x[d] ? x[b] < x[d] ? b : d
                : x[e] < x[a] ? e : a
        : x[a] < x[e] ? x[b] < x[e] ? b : e
                : x[d] < x[a] ? d : a;
		}

}
