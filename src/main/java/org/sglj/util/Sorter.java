/*
 * Sorter.java
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

import org.sglj.math.BinaryPredicate.IntBinaryPredicate;

/**
 * Utility class with various sorting algorithm on primitive types, with
 * custom comparators.
 *
 * @author Leo Osvald
 * @version 0.7
 */
public class Sorter {

	public static void countingSort(int[] a, int min, int max, int from, int to) {
		IntSorter.checkMinMax(min, max);
		ArrayUtils.rangeCheck(a, from, to);
		IntSorter.countingSort0(a, min, max, from, to);
	}

	public static void countingSort(int[] a, int from, int to) {
		ArrayUtils.rangeCheck(a, from, to);
		countingSort(a, from, to,
				ArrayUtils.min(a, from, to),
				ArrayUtils.max(a, from, to));
	}

	public static void heapSort(int[] a, int from, int to) {
		ArrayUtils.rangeCheck(a, from, to);
		IntSorter.heapSort(a, from, to);
	}

	public static void heapSort(int[] a, int from, int to,
			final IntBinaryPredicate cmp) {
		ArrayUtils.rangeCheck(a, from, to);
		IntSorter.heapSort(a, from, to, cmp);
	}

	static final int INSERTIONSORT_THRESHOLD = 8;

	static class TypeSorter {
//		static <T> void countingSort0(int[] a, T min, T max, int first, int last) {
//			int[] count = new int[max - min + 1];
//			for (int i = first; i < last; ++i)
//				++count[a[i] - min];
//			for (int x = max - min; x >= 0; --x) {
//				for (int i = count[x]; i > 0; --i)
//					a[--last] = x + min;
//			}
//		}

	}

	static class IntSorter {
		static void countingSort0(int[] a, int min, int max, int first, int last) {
			int[] count = new int[max - min + 1];
			for (int i = first; i < last; ++i)
				++count[a[i] - min];
			for (int x = max - min; x >= 0; --x) {
				for (int i = count[x]; i > 0; --i)
					a[--last] = x + min;
			}
		}

		static void mergeSort(int[] a, int from, int to) {
			int len = to - from;
			int[] tmp = new int[len];
			System.arraycopy(a, from, tmp, 0, len);
			mergeSort0(tmp, a, from, to, from);
		}

		private static void mergeSort0(int[] tmp, int[] sorted,
				int first, int last, int offset) {
			int len = last - first;

			// insertion sort on small arrays
			if (len < INSERTIONSORT_THRESHOLD) {
				insertionSort(sorted, first, last);
				return ;
			}

			int sortedFirst = first;
			int sortedLast = last;
			first -= offset;
			last -= offset;
			int mid = (first + last) >>> 1;
			mergeSort0(sorted, tmp, first, mid, -offset);
			mergeSort0(sorted, tmp, mid, last, -offset);

			if (tmp[mid - 1] < tmp[mid]) { // if already merged
				System.arraycopy(tmp, first, sorted, sortedFirst, len);
			} else {
				// merge sorted halves
				for (int p = first, q = mid, i = sortedFirst; i < sortedLast; ++i) {
					if (q >= last || p < mid && !(tmp[q] < tmp[p]))
						sorted[i] = tmp[p++];
					else
						sorted[i] = tmp[q++];
				}
			}
		}

		static void insertionSort(int[] a, int first, int last) {
			for (int cur = first; ++cur < last; ) {
				int key = a[cur];
				int i = cur;
				for (int val = a[i - 1]; key < val; val = a[--i - 1]) {
					a[i] = val;
					if (first == i - 1) {
						--i;
						break;
					}
				}
				a[i] = key;
			}
		}

		static void mergeSort(int[] a, int from, int to,
				final IntBinaryPredicate cmp) {
			int len = to - from;
			int[] tmp = new int[len];
			System.arraycopy(a, from, tmp, 0, len);
			mergeSort0(tmp, a, from, to, from, cmp);
		}

		private static void mergeSort0(int[] tmp, int[] sorted,
				int first, int last, int offset,
				final IntBinaryPredicate cmp) {
			int len = last - first;

			// insertion sort on small arrays
			if (len < INSERTIONSORT_THRESHOLD) {
				insertionSort(sorted, first, last, cmp);
				return ;
			}

			int sortedFirst = first;
			int sortedLast = last;
			first -= offset;
			last -= offset;
			int mid = (first + last) >>> 1;
			mergeSort0(sorted, tmp, first, mid, -offset, cmp);
			mergeSort0(sorted, tmp, mid, last, -offset, cmp);

			if (cmp.holds(tmp[mid - 1], tmp[mid])) { // if already merged
				System.arraycopy(tmp, first, sorted, sortedFirst, len);
			} else {
				// merge sorted halves
				for (int p = first, q = mid, i = sortedFirst; i < sortedLast; ++i) {
					if (q >= last || p < mid && !cmp.holds(tmp[q], tmp[p]))
						sorted[i] = tmp[p++];
					else
						sorted[i] = tmp[q++];
				}
			}
		}

		static void insertionSort(int[] a, int first, int last,
				final IntBinaryPredicate cmp) {
			for (int cur = first; ++cur < last; ) {
				int key = a[cur];
				int i = cur;
				for (int val = a[i - 1]; cmp.holds(key, val); val = a[--i - 1]) {
					a[i] = val;
					if (first == i - 1) {
						--i;
						break;
					}
				}
				a[i] = key;
			}
		}

		static void heapSort(int[] a, int first, int last) {
			int len = last - first;

			// max-heapify in O(last - first)
			for (int root = (len >>> 1) - 1; root >= 0; --root)
				downHeap(a, first, len, root);

			// sort by extraction of max
			while (len-- > 1) {
				--last;
				int tmp = a[last]; a[last] = a[first]; a[first] = tmp;
				downHeap(a, first, len, first);
			}
		}

		private static void downHeap(int[] a, int first, int len, int root) {
			int rootVal = a[root + first];
			int p = root;
			int halfLen = (len >>> 1);
			while (p < halfLen) {
				int child = (p << 1) + 1;
				int right = child + 1;
				int childVal = a[child + first];
				if (right < len && a[right + first] > childVal)
					childVal = a[(child = right) + first];
				if (rootVal >= childVal)
					break;
				a[p + first] = childVal;
				p = child;
			}
			a[p + first] = rootVal;
		}

		static void heapSort(int[] a, int first, int last,
				final IntBinaryPredicate cmp) {
			int len = last - first;

			// max-heapify in O(last - first)
			for (int root = (len >>> 1) - 1; root >= 0; --root)
				downHeap(a, first, len, root, cmp);

			// sort by extraction of max
			while (len-- > 1) {
				--last;
				int tmp = a[last]; a[last] = a[first]; a[first] = tmp;
				downHeap(a, first, len, first, cmp);
			}
		}

		private static void downHeap(int[] a, int first, int len, int root,
				final IntBinaryPredicate cmp) {
			int rootVal = a[root + first];
			int p = root;
			int halfLen = (len >>> 1);
			while (p < halfLen) {
				int child = (p << 1) + 1;
				int right = child + 1;
				int childVal = a[child + first];
				if (right < len && cmp.holds(childVal, a[right + first]))
					childVal = a[(child = right) + first];
				if (!cmp.holds(rootVal, childVal))
					break;
				a[p + first] = childVal;
				p = child;
			}
			a[p + first] = rootVal;
		}

		private static void checkMinMax(int min, int max) {
			if (min > max)
				throw new IllegalArgumentException();
		}
	}
}
