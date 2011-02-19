/*
 * UnionFindDisjointSet.java
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

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class UnionFindDisjointSet<E> extends AbstractSet<E>
implements DisjointSet<E> {

	private final Map<E, Node<E>> nodeMap;
	private final Map<Node<E>, UnionFindPartition<E>> partitionMap;
	
	private int size;
	
	transient volatile int modCount;
	
	private static final class Node<E> {
		E value;
		int height;
		int size;
		Node<E> parent;
		Node(E value, Node<E> parent) {
			this.value = value;
			this.parent = parent;
			this.size = 1;
		}
	}
	
	private static class Itr<E> implements Iterator<E> {

		final UnionFindDisjointSet<E> uf;
		int expectedModCount;
		final Iterator<Node<E>> it;
		Node<E> lastNode;
		
		public Itr(UnionFindDisjointSet<E> uf) {
			it = uf.nodeMap.values().iterator();
			this.uf = uf;
			this.expectedModCount = uf.modCount;
		}
		
		@Override
		public boolean hasNext() {
			return !it.hasNext();
		}

		@Override
		public E next() {
			lastNode = it.next();
			return lastNode.value;
		}

		@Override
		public void remove() {
			if (lastNode != null) {
				uf.removeNode(lastNode);
				++expectedModCount;
				uf.partitionMap.remove(lastNode);
				lastNode = null;
				it.remove();
			}
			throw new IllegalStateException();
		}
	}
	
	public static final class UnionFindPartition<E> extends AbstractSet<E>
	implements Partition<E> {

		Node<E> root;
		final UnionFindDisjointSet<E> uf;
		
		private static final class PartitionIterator<E> extends Itr<E> {
			final UnionFindPartition<E> partition;
			int pos;
			
			public PartitionIterator(UnionFindPartition<E> partition) {
				super(partition.uf);
				this.partition = partition;
			}
			
			@Override
			public boolean hasNext() {
				return pos < partition.root.size;
			}

			@Override
			public E next() {
				Node<E> lastNodeRoot;
				do {
					lastNode = it.next(); 
					lastNodeRoot = uf.relinkInsidePartition(lastNode);
				} while (lastNodeRoot != partition.root);
				++pos;
				return lastNode.value;
			}
		}
		
		public UnionFindPartition(UnionFindDisjointSet<E> owner, Node<E> root) {
			this.root = root;
			this.uf = owner;
		}
		
		@Override
		public UnionFindDisjointSet<E> getOwner() {
			return uf;
		}
		
		@Override
		public Iterator<E> iterator() {
			return new PartitionIterator<E>(this);
		}

		@Override
		public int size() {
			return root.size;
		}

		@Override
		public boolean contains(Object o) {
			if (!uf.nodeMap.containsKey(o))
				return false;
			return uf.relinkInsidePartition(uf.nodeMap.get(o)) == root;
		}
		
		@Override
		public boolean containsAll(Collection<?> c) {
			for (Object o : c)
				if (!contains(o))
					return false;
			return true;
		}
		
		@Override
		public boolean add(E e) {
			if (isEmpty()) {
				if (uf.add(e)) {
					root = uf.nodeMap.get(e);
					return true;
				}
			} else if (uf.add(e)) {
				uf.union(root.value, e);
				root = uf.relinkInsidePartition(root);
				return true;
			}
			return false;
		}
		
		@Override
		public boolean addAll(Collection<? extends E> c) {
			// TODO optimize
			boolean added = false;
			for (E e : c)
				added |= add(e);
			return added;
		}
		
		@Override
		public boolean remove(Object o) {
			if (!uf.nodeMap.containsKey(o))
				return false;
			
			Node<E> node = uf.nodeMap.get(o);
			Node<E> rootOfO = uf.relinkInsidePartition(node);
			if (rootOfO == root) {// if object belongs to this partition
				root = uf.removeNode(node);
				uf.nodeMap.remove(node.value);
				uf.partitionMap.remove(node);
				return true;
			}
			return false;
		}
		
		@Override
		public boolean removeAll(Collection<?> c) {
			Collection<Node<E>> toRemove = new ArrayList<Node<E>>(c.size());
			for (Object o : c) {
				if (!uf.nodeMap.containsKey(o))
					continue;
				
				Node<E> node = uf.nodeMap.get(o);
				Node<E> rootOfO = uf.relinkInsidePartition(node);
				if (rootOfO == root) {
					uf.nodeMap.remove(o);
					toRemove.add(node);
				}
			}
			if (!toRemove.isEmpty()) {
				root = uf.removeNodesInsidePartition(toRemove);
				uf.partitionMap.keySet().removeAll(toRemove);
			}
			return false;
		}

	}
	
	public UnionFindDisjointSet(int capacity) {
		this.nodeMap = new HashMap<E, Node<E>>(capacity);
		this.partitionMap = new HashMap<Node<E>, UnionFindPartition<E>>();
	}
	
	public UnionFindDisjointSet() {
		this(16);
	}
	
	public UnionFindDisjointSet(Collection<? extends E> c) {
		this(c.size());
		addAll(c);
	}
	
	@Override
	public boolean union(E a, E b) {
		if (a.equals(b))
			return false;
		
		Node<E> nodeA = nodeMap.get(a), nodeB = nodeMap.get(b);
		if (nodeA == null || nodeB == null)
			throw new NoSuchElementException();
		
		Node<E> rootA = relinkInsidePartition(nodeA);
		Node<E> rootB = relinkInsidePartition(nodeB);
		
		if (rootA.height < rootB.height) {
			rootA.parent = rootB;
			rootA.size += rootB.size;
		} else {
			rootB.parent = rootA;
			rootB.size += rootA.size;
			if (rootA.height == rootB.height)
				++rootA.height;
		}
		return true;
	}
	
	@Override
	public UnionFindPartition<E> find(E element) {
		if (!nodeMap.containsKey(element))
			return new UnionFindPartition<E>(this, null);
		
		Node<E> node = nodeMap.get(element);
		return partitionMap.get(relinkInsidePartition(node));
	}

	@Override
	public Iterator<E> iterator() {
		return new Itr<E>(this);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean contains(Object o) {
		return nodeMap.containsKey(o);
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		return nodeMap.keySet().containsAll(c);
	}
	
	@Override
	public boolean add(E e) {
		Node<E> node = new Node<E>(e, null);
		if (!nodeMap.containsKey(e)) {
			nodeMap.put(e, node);
			partitionMap.put(node, new UnionFindPartition<E>(this, node));
			++size;
			return true;
		}
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean ret = false;
		for (E e : c)
			ret |= add(e);
		return ret;
	}
	
	@Override
	public boolean remove(Object o) {
		if (!nodeMap.containsKey(o))
			return false;
		
		Node<E> node = nodeMap.get(o);
		removeNode(node);
		nodeMap.remove(o);
		partitionMap.remove(node);
		return true;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		if (c.isEmpty())
			return false;
		
		Collection<Node<E>> toRemove = new ArrayList<Node<E>>(c.size());
		for (Object o : c) {
			if (!nodeMap.containsKey(o))
				continue;

			nodeMap.remove(o);
			toRemove.add(nodeMap.get(o));
		}
		removeNodesInsidePartition(toRemove);
		return partitionMap.keySet().removeAll(toRemove);
	}
	
	@Override
	public String toString() {
		// XXX optimize for speed (this is O(P * N))
		if (size == 0)
			return "[]";
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Object o : partitionMap.values().toArray()) {
			@SuppressWarnings("unchecked")
			UnionFindPartition<E> partition = (UnionFindPartition<E>) o;
			if (first) { first = false; } else { sb.append(", "); }
			sb.append(partition);
		}
		return sb.append(']').toString();
	}
	
	private void relinkInsidePartition(Node<E> node, Node<E> root) {
		while (node != root) {
			Node<E> child = node;
			node = node.parent;
			child.parent = root;
		}
	}
	
	private Node<E> relinkInsidePartition(Node<E> node) {
		Node<E> root = node;
		while (root.parent != null)
			root = root.parent;
		relinkInsidePartition(node, root);
		return root;
	}
	
	private Node<E> removeNode(Node<E> toRemove) {
		Node<E> newRoot;
		if (toRemove.parent == null) { // root removal
			newRoot = null;
			// find new root among children with the least height
			for (Node<E> node : nodeMap.values()) {
				// if child of this the root and has less height
				if (node.parent == toRemove && (newRoot == null
						|| node.height < newRoot.height)) {
					newRoot = node;
				}
			}
			if (newRoot != null) {
				Node<E> oldRoot = newRoot.parent;
				newRoot.parent = null;
				newRoot.size = oldRoot.size - 1;
				newRoot.height = oldRoot.height;
				// iterate through all to ensure fail-fast iterator
				for (Node<E> node : nodeMap.values()) {
					if (node.parent == toRemove && node != newRoot) {
						node.parent = newRoot;
					}
				}
			}
		} else {
			newRoot = toRemove;
			while (newRoot.parent != null)
				newRoot = newRoot.parent;
			--newRoot.size;
			for (Node<E> node : nodeMap.values()) {
				if (node.parent == toRemove) {
					node.parent = newRoot;
				}
			}
		}
		--size;
		++modCount;
		return newRoot;
	}
	
	private Node<E> removeNodesInsidePartition(Collection<Node<E>> toRemove) {
		// XXX optimize for speed (this is O(P * N))
		Node<E> newRoot = null;
		for (Node<E> node : toRemove)
			newRoot = removeNode(node);
		return newRoot;
	}

}
