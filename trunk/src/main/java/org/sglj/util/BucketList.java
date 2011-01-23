/*
 * BucketList.java
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
 */

package org.sglj.util;

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

/**
 * 
 * @author Leo Osvald
 * @version 0.02
 * 
 * @param <E> the type of elements held in this collection
 */
public class BucketList<E> extends AbstractList<E>
implements RandomAccess {

//	private static final int INITIAL_BUCKET_SIZE_LOG = 1;
	
	private int bucketSizeLog;
	private int bucketCount;
	
	private RandomAccessDeque<E>[] b;
	
	public BucketList() {
		this(16);
	}
	
	public BucketList(int capacity) {
		allocateBuckets(capacity);
	}
	
	@SuppressWarnings("unchecked")
	private void allocateBuckets(int numElements) {
		if (numElements == 0)
			throw new IllegalArgumentException();
		
		// determine bucket size
		// [1, 3) -> 1
		// [3, 9) -> 2
		// [9, 33) -> 3
		// [2^(n-1)+1, 2^(n+1)+1] -> n
		--numElements;
		bucketSizeLog = (log2(numElements) >>> 1);
		
		// determine the number of buckets
		b = new RandomAccessDeque[2 << bucketSizeLog];
		b[0] = new RandomAccessDeque<E>(1 << bucketSizeLog);
    }
	
	@SuppressWarnings("unchecked")
	private void expand() {
		RandomAccessDeque<E>[] newArr 
		= new RandomAccessDeque[bucketCount << 1];
		bucketCount >>>= 1;
		++bucketSizeLog;
		b[0].addAll(b[1]);
		b[1].clear();
		newArr[0] = b[0];
		for (int i = 1; i < bucketCount; ++i) {
			RandomAccessDeque<E> from = b[2*i];
			RandomAccessDeque<E> to = b[i];
			// move left
			to.addAll(from);
			from.clear();
			// move right
			from = b[2*i + 1];
			to.addAll(from);
			from.clear();
			
			newArr[i] = to;
		}
		
		b = newArr;
	}
	
	@SuppressWarnings("unchecked")
	private void shrink() {
		final int halfBucketSize = (1 << bucketSizeLog) >>> 1;
		RandomAccessDeque<E>[] newArr 
		= new RandomAccessDeque[b.length >>> 1];
		for (int i = 0; i < bucketCount; ++i) {
			RandomAccessDeque<E> from = b[i];
			// copy right half
			newArr[2*i + 1] = new RandomAccessDeque<E>(
					from.subList(halfBucketSize, from.size()));
			// clear right half and assign as left side
			while (from.size() > halfBucketSize)
				from.removeLast();
			newArr[2*i] = from;
		}
		
		b = newArr;
		bucketCount <<= 1;
		--bucketSizeLog;
	}
	
	public int size() {
		return bucketCount == 0 ? 0 : ((bucketCount - 1) << bucketSizeLog) 
				+ b[bucketCount - 1].size();
	}
	
	public E get(int index) {
		return b[index >>> bucketSizeLog]
		         .get(index & ((1 << bucketSizeLog) - 1));
	}
	
	@Override
	public void add(int index, E element) {
		int bucket = (index >>> bucketSizeLog);
		int bucketSize = (1 << bucketSizeLog);
		if (bucketCount == 0 || b[bucketCount - 1].size() == bucketSize) {
			if ((bucketSize << 1) == bucketCount) {
				expand();
				bucket = (index >>> bucketSizeLog);
				bucketSize = (1 << bucketSizeLog);
			}
			b[bucketCount++] = new RandomAccessDeque<E>(bucketSize);
		}
		
		for (int i = bucketCount - 1; i > bucket; --i)
			b[i].addFirst(b[i - 1].pollLast());
		b[bucket].add(index & (bucketSize - 1), element);
	}
	
	@Override
	public E remove(int index) {
		final int bucket = (index >>> bucketSizeLog);
		final int bucketSize = (1 << bucketSizeLog);
		
		E oldValue = b[bucket].remove(index & (bucketSize - 1));
		for (int i = bucket + 1; i < bucketCount; ++i)
			b[i - 1].addLast(b[i].pollFirst());
		
		if (b[bucketCount - 1].isEmpty()) { // XXX
			b[--bucketCount] = null;
			if (bucketCount == (bucketSize >>> 2))
				shrink();
		}
		
		return oldValue;
	}
	
	protected int getBufferCount() {
		return bucketCount;
	}
	
	protected int getBufferSize() {
		return 1 << bucketSizeLog;
	}
	
	private static int log2(int v) {
		int r = 0;
		do {
			++r;
		} while ((v >>>= 1) != 0);
		return r;
	}
	
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int ret = 0;
		for (int i = size() - 1; i >= 0; --i) {
			E e = get(i);
			ret = ret * PRIME + (e != null ? e.hashCode() : 0);
		}
		return ret;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
		    return true;
		if (!(obj instanceof List))
		    return false;
		
		@SuppressWarnings("unchecked")
		BucketList<E> other = (BucketList<E>) obj;
		for (int i = size() - 1; i >= 0; --i) {
			E e1 = get(i);
			E e2 = other.get(i);
			if ((e1 == null ^ e2 == null) || e1 != null && !e1.equals(e2))
				return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(
				"[bucketSize = " + (1<<bucketSizeLog)
				+ " , bucketCount = " + bucketCount + "]: ");
		sb.append('[');
		for (int i = 0; i < bucketCount; ++i) {
			if (i > 0) sb.append(" | ");
			int bucketSize = (i + 1 != bucketCount ? (1 << bucketSizeLog)
					: b[i].size());
			for (int j = 0; j < bucketSize; ++j) {
				if (j > 0) sb.append(' ');
				sb.append(b[i].get(j));
			}
		}
		sb.append(']');
		return sb.toString();
	}
	
}
