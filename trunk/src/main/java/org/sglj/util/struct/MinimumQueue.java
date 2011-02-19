/*
 * MinimumQueue.java
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;

/**
 * A {@link Queue} which supports querying for the least/greatest
 * element in constant time, through the {@link #query()} method. The queue 
 * to be used for queue operations is specified via constructor.<br>
 * This implementation does not support the following operations:
 * <ul>
 * <li>{@link #remove(Object)}</li>
 * <li>{@link #removeAll(Collection)}</li>
 * <li>{@link #retainAll(Collection)}</li>
 * </ul>
 * 
 * The primary reason why the above operations are not supported is
 * to guarantee constant time querying, regardless of the usage, while
 * also avoiding unnecessary overhead and checking.
 * 
 * @author Leo Osvald
 *
 * @param <E>
 */
public class MinimumQueue<E> implements Queue<E>, GlobalQueryable<E>, 
Cloneable, Serializable {

	private static final long serialVersionUID = -6888414204844941257L;
	
	private final Queue<E> queue;
	private final Comparator<? super E> cmp;

	private transient final Deque<E> minQueue;
	
	// constructors for comparator-based ordering

	/**
	 * Constructs a new minimum queue which uses the specified
	 * comparator for element comparison, delegates queue operations
	 * to the specified queue and uses the specified deque for internal
	 * operations.
	 * 
	 * @param queue the underlying queue
	 * @param minDeque a deque to be used for internal purposes
	 * @param comparator the comparator to be used
	 */
	public MinimumQueue(Queue<E> queue, Deque<E> minDeque,
			Comparator<E> comparator) {
		this.queue = queue;
		this.cmp = comparator;
		this.minQueue = minDeque;
	}
	
	/**
	 * Constructs a new minimum queue which uses the specified
	 * comparator for element comparison, delegates queue operations
	 * to the specified queue and uses a new instance of
	 * {@link ArrayDeque} of initial capacity
	 * equal to <tt>initialCapacity</tt> for internal operations.
	 * 
	 * @param queue the underlying queue
	 * @param initialCapacity initial capacity of the internal deque
	 * @param comparator the comparator to be used
	 */
	public MinimumQueue(Queue<E> queue, int initialCapacity,
			Comparator<E> comparator) {
		this(queue, new ArrayDeque<E>(initialCapacity), comparator);
	}
	
	/**
	 * Constructs a new minimum queue which uses the specified
	 * comparator for element comparison and delegates queue operations
	 * to the specified queue. A new instance of {@link ArrayDeque}
	 * is used for internal operations.
	 * 
	 * @param queue the underlying queue
	 * @param comparator the comparator to be used
	 */
	public MinimumQueue(Queue<E> queue, Comparator<E> comparator) {
		this(queue, new ArrayDeque<E>(), comparator);
	}
	
	/**
	 * Constructs a new minimum queue which uses an instance
	 * of {@link ArrayDeque} for queue operations and the specified
	 * comparator for element comparison. A new instance of {@link ArrayDeque}
	 * is used for internal operations. Both {@link ArrayDeque} have
	 * the initial capacity equal to <tt>initialCapacity</tt>.
	 * 
	 * @param initialCapacity initial capacity of the internal deque
	 * @param comparator the comparator to be used
	 */
	public MinimumQueue(int initialCapacity, Comparator<E> comparator) {
		this(new ArrayDeque<E>(initialCapacity),
				new ArrayDeque<E>(initialCapacity),
				comparator);
	}
	
	/**
	 * Constructs a new minimum queue which uses an instance
	 * of {@link ArrayDeque} for queue operations and the specified
	 * comparator for element comparison. A new instance of {@link ArrayDeque}
	 * is used for internal operations.
	 * 
	 * @param comparator the comparator to be used
	 */
	public MinimumQueue(Comparator<E> comparator) {
		this(new ArrayDeque<E>(), new ArrayDeque<E>(), comparator);
	}
	
	// constructors for natural ordering
	
	/**
	 * Constructs a new minimum queue which uses elements' natural ordering
	 * for element comparison, delegates queue operations
	 * to the specified queue and uses the specified deque for internal
	 * operations.
	 * 
	 * @param queue the underlying queue
	 * @param minDeque a deque to be used for internal purposes
	 */
	public MinimumQueue(Queue<E> queue, Deque<E> minDeque) {
		this(queue, minDeque, null);
	}
	
	/**
	 * Constructs a new minimum queue which uses elements' natural ordering
	 * for element comparison, delegates queue operations
	 * to the specified queue and uses a new instance of
	 * {@link ArrayDeque} of initial capacity
	 * equal to <tt>initialCapacity</tt> for internal operations.
	 * 
	 * @param queue the underlying queue
	 * @param initialCapacity initial capacity of the internal deque
	 */
	public MinimumQueue(Queue<E> queue, int initialCapacity) {
		this(queue, new ArrayDeque<E>(initialCapacity), null);
	}
	
	/**
	 * Constructs a new minimum queue uses elements' natural ordering
	 * for element comparison and delegates queue operations
	 * to the specified queue. A new instance of {@link ArrayDeque}
	 * is used for internal operations.
	 * 
	 * @param queue the underlying queue
	 */
	public MinimumQueue(Queue<E> queue) {
		this(queue, new ArrayDeque<E>(), null);
	}
	
	/**
	 * Constructs a new minimum queue which uses an instance
	 * of {@link ArrayDeque} for queue operations and elements' natural
	 * ordering for element comparison. A new instance of {@link ArrayDeque}
	 * is used for internal operations. Both {@link ArrayDeque} have
	 * the initial capacity equal to <tt>initialCapacity</tt>.
	 * 
	 * @param initialCapacity initial capacity of the internal deque
	 */
	public MinimumQueue(int initialCapacity) {
		this(new ArrayDeque<E>(initialCapacity),
				new ArrayDeque<E>(initialCapacity),
				null);
	}
	
	/**
	 * Constructs a new minimum queue which uses an instance
	 * of {@link ArrayDeque} for queue operations and elements' natural
	 * ordering for element comparison. A new instance of {@link ArrayDeque}
	 * is used for internal operations.
	 */
	public MinimumQueue() {
		this(new ArrayDeque<E>(), new ArrayDeque<E>(), null);
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return queue.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return queue.iterator();
	}

	@Override
	public Object[] toArray() {
		return queue.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return queue.toArray(a);
	}

	@Override
	public final boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return queue.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean ret = queue.addAll(c);
		int oldSize = size();
		if (oldSize + c.size() == size()) {
			if (cmp != null)
				fixAfterPushAllComparator(c);
			else
				fixAfterPushAllComparable(c);
			return ret;
		}
		throw new IllegalStateException("Underlying queue must add all");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		queue.clear();
		minQueue.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean add(E e) {
		if (queue.add(e)) {
			if (cmp != null)
				fixAfterPushComparator(e);
			else
				fixAfterPushComparable((Comparable<? super E>) e);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean offer(E e) {
		if (queue.offer(e)) {
			if (cmp != null)
				fixAfterPushComparator(e);
			else
				fixAfterPushComparable((Comparable<? super E>) e);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E remove() {
		E ret = queue.remove();
		if (ret != null) {
			if (cmp != null)
				fixAfterPopComparator(ret);
			else
				fixAfterPopComparable((Comparable<? super E>)ret);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E poll() {
		E ret = queue.poll();
		if (ret != null) {
			if (cmp != null)
				fixAfterPopComparator(ret);
			else
				fixAfterPopComparable((Comparable<? super E>)ret);
		}
		return ret;
	}

	@Override
	public E element() {
		return queue.element();
	}

	@Override
	public E peek() {
		return queue.peek();
	}
	
	@Override
	public int hashCode() {
		return queue.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return queue.equals(obj);
	}
	
	@Override
	public String toString() {
		return queue.toString() + " | " + minQueue.toString();
	}
	
	/**
	 * Returns the least element in the queue according to the
	 * {@link Comparator} if other than <code>null</code> or
	 * according to the elemenets' natural ordering if the comparator
	 * is <code>null</code>.
	 * @return the least element in the queue
	 */
	@Override
	public E query() {
		return minQueue.isEmpty() ? null : minQueue.peek();
	}
	
	public Comparator<? super E> comparator() {
		return cmp;
	}
	
	/**
	 * Returns <code>null</code>. This is only used when querying
	 * an empty queue with the {@link #query()} method.
	 * 
	 * @return <code>null</code>
	 */
	@Override
	public E createQueryData() {
		return null;
	}
	
	private void fixAfterPushComparator(E e) {
		while (!minQueue.isEmpty() && cmp.compare(e, minQueue.peekLast()) < 0)
			minQueue.pollLast();
		minQueue.addLast(e);
		assert minQueue.isEmpty() == queue.isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	private void fixAfterPushComparable(Comparable<? super E> e) {
		while (!minQueue.isEmpty() && e.compareTo(minQueue.peekLast()) < 0)
			minQueue.pollLast();
		minQueue.addLast((E) e);
		assert minQueue.isEmpty() == queue.isEmpty();
	}
	
	private void fixAfterPushAllComparator(Collection<? extends E> c) {
		if (c.isEmpty())
			return ;
		
		// find the minimum of the added elements and call fixAfterPush(min)
		Iterator<? extends E> it = c.iterator();
		E min = it.next();
		while (it.hasNext()) {
			E e = it.next();
			if (cmp.compare(e, min) < 0)
				min = e;
		}
		fixAfterPushComparator(min);
		assert minQueue.isEmpty() == queue.isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	private void fixAfterPushAllComparable(
			Collection<? extends E> c) {
		if (c.isEmpty())
			return ;
		
		// find the minimum of the added elements and call fixAfterPush(min)
		Iterator<? extends E> it = c.iterator();
		E min = it.next();
		while (it.hasNext()) {
			Comparable<? super E> e = (Comparable<? super E>) it.next();
			if (e.compareTo(min) < 0)
				min = (E) e;
		}
		fixAfterPushComparable((Comparable<? super E>) min);
		assert minQueue.isEmpty() == queue.isEmpty();
	}
	
	
	private void fixAfterPopComparator(E e) {
		if (!minQueue.isEmpty() 
				&& cmp.compare(minQueue.peek(), e) == 0)
			minQueue.remove();
		assert minQueue.isEmpty() == queue.isEmpty();
	}
	
	private void fixAfterPopComparable(Comparable<? super E> e) {
		if (!minQueue.isEmpty() 
				&& e.compareTo(minQueue.peek()) == 0)
			minQueue.remove();
		assert minQueue.isEmpty() == queue.isEmpty();
	}
	
	// serialization stuff
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws IOException,
            ClassNotFoundException {
    	stream.defaultReadObject();
    	if (cmp != null)
    		fixAfterPushAllComparator(this);
    	else
    		fixAfterPushAllComparable(this);
    }
	
}
