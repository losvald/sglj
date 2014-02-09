/*
 * AvlTree.java
 *
 * Copyright (C) 2014 Leo Osvald <leo.osvald@gmail.com>
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

package org.sglj.util.struct;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.sglj.math.discrete.CodomainMergeable;
import org.sglj.util.ArrayStack;
import org.sglj.util.Stack;
import org.sglj.util.ref.IntReference;


public abstract class AvlTree<E> extends AbstractSet<E> {
	protected static final Comparator<Object> minComparator = new Comparator<Object>() {
		@SuppressWarnings("unchecked")
		@Override
		public int compare(Object o1, Object o2) {
			return ((AvlNode<Object>)o2).left == null ? 0 : -1;
		}
	};

	private Comparator<Object> comparator;

	private int size;
	private AvlNode<E> root;

	public AvlTree() {
		this(new Comparator<E>() {
			@SuppressWarnings("unchecked")
			@Override
			public int compare(E o1, E o2) {
				return ((Comparable<E>)o1).compareTo(o2);
			}
		});
	}

	public AvlTree(Comparator<? super E> comparator) {
		setComparator(comparator);
	}

	protected void setComparator(final Comparator<? super E> comparator) {
		this.comparator = new Comparator<Object>() {
			@SuppressWarnings("unchecked")
			@Override
			public int compare(Object o1, Object o2) {
				return comparator.compare((E)o1, ((AvlNode<E>)o2).getKey());
			}
		};
	}

	protected Comparator<Object> getComparator() {
		return comparator;
	}

	protected abstract AvlNode<E> createNode(E item);

	protected static class InsertResult<E> extends IntReference {
		public AvlNode<E> inserted;

		public InsertResult() {
			super(0);
		}
	}

	public void insert(E item, InsertResult<E> result) {
		root = insert(root, item, result);
		if (result.inserted != null)
			++size;
	}

	private AvlNode<E> insert(AvlNode<E> root, E key, InsertResult<E> result) {
		if (root == null) {
			// insert a new node
			result.value = 1;
			(result.inserted = createNode(key)).incrementMultiplicity(key);
			return result.inserted;
		}

		// Compare items and determine which direction to search
		int cmp = root.compareKey(key, comparator);
		if (cmp == 0) {
			// key already in tree at this node
			root.incrementMultiplicity(key);
			result.value = 0;
			result.inserted = null;
			return root;
		}

		// Insert into left/right subtree
		if (cmp < 0)
			root.left = insert(root.left, key, result);
		else
			root.right = insert(root.right, key, result);

		if (result.inserted != null) {
			// rebalance if needed -- height of current tree increases only if its
			// subtree height increases and the current tree needs no rotation.
			if (result.value != 0 && (root.myBal += cmp < 0 ? -1 : 1) != 0) {
				root = root.rebalance(result);
				result.value = 1 - result.value;
			} else
				result.value = 0;

			root.mergeCodomains(root.left, root.right);
		}

		return root;
	}

	public static class RemoveResult<K> extends IntReference {
		public AvlNode<K> removed;

		public RemoveResult() {
			super(0);
		}
	}

	public void remove(E key, RemoveResult<E> result) {
		result.removed = null;
		root = AvlNode.remove(root, key, comparator, result);
		if (result.removed != null)
			--size;
	}

	public AvlNode<E> getRoot() {
		return root;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<E> iterator() {
		return new AvlIterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		return AvlNode.search(root, (E)o, comparator) != null;
	}

	@Override
	public boolean add(E e) {
		InsertResult<E> result = new InsertResult<E>();
		insert(e, result);
		return result.inserted != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean modified = false;
		InsertResult<E> result = new InsertResult<E>();
		for (Object item : c) {
			insert((E)item, result);
			modified |= (result.inserted != null);
		}
		return modified;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		RemoveResult<E> result = new RemoveResult<E>();
		remove((E)o, result);
		return result.removed != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean removeAll(Collection<?> c) {
		int oldSize = size();
		RemoveResult<E> result = new RemoveResult<E>();
		for (Object item : c)
			remove((E)item, result);
		return size() != oldSize;
	}

	@Override
	public void clear() {
		size = 0;
		root = null;
	}

	protected static abstract class AvlNode<K> implements
	CodomainMergeable<AvlNode<K>> {
		protected AvlNode<K> left;
		protected AvlNode<K> right;

		private int myBal;

		protected AvlNode<K> rotateLeft(IntReference heightChange) {
			AvlNode<K> root = this;
			AvlNode<K> oldRoot = root;
			heightChange.value = (root.right.myBal == 0 ? 0 : 1);

			// perform rotation
			root = root.right;
			oldRoot.right = root.left;
			root.left = oldRoot;

			// update balances
			oldRoot.myBal = -(--root.myBal);

			oldRoot.mergeCodomains(oldRoot.left, oldRoot.right);
			root.mergeCodomains(root.left, root.right);

			return root;
		}

		protected AvlNode<K> rotateRight(IntReference heightChange) {
			AvlNode<K> root = this;
			AvlNode<K> oldRoot = root;
			heightChange.value = (root.left.myBal == 0 ? 0 : 1);

			// perform rotation
			root = root.left;
			oldRoot.left = root.right;
			root.right = oldRoot;

			// update balances
			oldRoot.myBal = -(++root.myBal);

			oldRoot.mergeCodomains(oldRoot.left, oldRoot.right);
			root.mergeCodomains(root.left, root.right);

			return root;
		}

		protected AvlNode<K> rotateLeftRight(IntReference heightChange) {
			AvlNode<K> root = this;
			AvlNode<K> oldRoot = root;
			AvlNode<K> oldOtherDirSubtree = root.right;

			// assign a new root
			root = oldRoot.right.left;

			// new root exchanges its left subtree for its grandparent
			oldRoot.right = root.left;
			root.left = oldRoot;

			// new root exchanges its right subtree for its parent
			oldOtherDirSubtree.left = root.right;
			root.right = oldOtherDirSubtree;

			heightChange.value = updateBalancesRotateTwice(root);

			oldRoot.mergeCodomains(oldRoot.left, oldRoot.right);
			AvlNode<K> rootNewChild = root.right;
			rootNewChild.mergeCodomains(rootNewChild.left, rootNewChild.right);
			root.mergeCodomains(root.left, root.right);

			return root;
		}

		protected AvlNode<K> rotateRightLeft(IntReference heightChange) {
			AvlNode<K> root = this;
			AvlNode<K> oldRoot = root;
			AvlNode<K> oldOtherDirSubtree = root.left;

			// assign a new root
			root = oldRoot.left.right;

			// the new root exchanges its right subtree for its grandparent
			oldRoot.left = root.right;
			root.right = oldRoot;

			// the new root exchanges its left subtree for its parent
			oldOtherDirSubtree.right = root.left;
			root.left = oldOtherDirSubtree;

			heightChange.value = updateBalancesRotateTwice(root);

			oldRoot.mergeCodomains(oldRoot.left, oldRoot.right);
			AvlNode<K> rootNewChild = root.left;
			rootNewChild.mergeCodomains(rootNewChild.left, rootNewChild.right);
			root.mergeCodomains(root.left, root.right);

			return root;
		}

		private static <K> int updateBalancesRotateTwice(AvlNode<K> root) {
			// update balances
			root.left.myBal = -Math.max(root.myBal, 0);
			root.right.myBal = -Math.min(root.myBal, 0);
			root.myBal = 0;

			// a double rotation always shortens the overall height of the tree
			return 1;
		}

		public AvlNode<K> rebalance(IntReference heightChange) {
			AvlNode<K> root = this;
			if (!isLeftBalanced()) {
				if (root.isRightHeavy()) {
					root = root.rotateRightLeft(heightChange);
				} else {
					root = root.rotateRight(heightChange);
				}
			} else if (!isRightBalanced()) {
				if (root.isLeftHeavy()) {
					root = root.rotateLeftRight(heightChange);
				} else {
					root = root.rotateLeft(heightChange);
				}
			} else
				heightChange.value = 0;

			return root;
		}

		public int compareKey(K target, Comparator<Object> comparator) {
			return comparator.compare(target, this);
		}

		public abstract K getKey();

		public boolean isLeftBalanced() {
			return myBal >= -1;
		}

		public boolean isRightBalanced() {
			return myBal <= 1;
		}

		public boolean isLeftHeavy() {
			return right.myBal == -1;
		}

		public boolean isRightHeavy() {
			return left.myBal == 1;
		}

		public static <K> AvlNode<K> search(AvlNode<K> root, K key,
				Comparator<Object> comparator) {
			while (root != null) {
				int cmp = root.compareKey(key, comparator);
				if (cmp == 0)
					break;
				else if (cmp < 0)
					root = root.left;
				else
					root = root.right;
			}
			return root;
		}

		public static <K> AvlNode<K> remove(AvlNode<K> root, K key,
				Comparator<Object> comparator, RemoveResult<K> result) {
			if (root == null) {
				// Key not found
				result.value = 0;
				return root;
			}

			// Compare items and determine which direction to search
			int decrease = 0;
			int cmp = root.compareKey(key, comparator);
			if (cmp < 0) {
				root.left = remove(root.left, key, comparator, result);
				decrease = -result.value;
			} else if (cmp > 0) {
				root.right = remove(root.right, key, comparator, result);
				decrease = result.value;
			} else  {   // Found key at this node
				if (result.removed == null && !root.decrementMultiplicity(key)) {
					result.value = 0;
					return root;
				}

				// At this point, we know "result" is zero and "root" points to
				// the node that we need to delete.  There are three cases:
				//
				//    1) The node is a leaf.  Remove it and return.
				//
				//    2) The node is a branch (has only 1 child). Make "root"
				//       (the pointer to this node) point to the child.
				//
				//    3) The node has two children. Swap items with root's 
				//       successor (the smallest item in its right subtree) and
				//       delete the successor from the root's right subtree.
				//       Reseat "decrease" if the subtree height decreased
				//       due to the deletion of the successor of "root".
				boolean existsLeft = (root.left != null);
				boolean existsRight = (root.right != null);
				if (!existsLeft || !existsRight) {
					AvlNode<K> toDelete = root;
					if (existsLeft)
						root = root.left;
					else if (existsRight)
						root = root.right;
					else { // We have a leaf -- remove it
						result.removed = toDelete;
						result.value = 1; // height changed from 1 to 0
						return null;
					}

					// We have one child -- only child becomes new root
					result.removed = toDelete;
					result.value = 1; // We just shortened the subtree
					// Null-out the subtree pointers not to delete recursively
					toDelete.left = toDelete.right = null;
					return root;
				} else {
					// We have two children -- find successor and replace our
					// current data item with that of the successor
					int heightChange = result.value;
					result.removed = root;
					AvlNode<K> right = remove(root.right, key, minComparator,
							result);
					AvlNode<K> succ = result.removed;
					succ.left = root.left;
					succ.right = right;
					succ.myBal = root.myBal; // XXX: double-check if correct
					decrease = result.value;
					result.value = heightChange;
					root = succ;
				}
			}

			// Rebalance if necessary -- the height of current tree changes if:
			// (1) a rotation that was performed changed the subtree height
			// (2) the decreased subtree height now matches the sibling's height
			// (now, the current has a zero balance when it previously did not).
			if (decrease != 0) {
				// update balance factor and rebalance if the height changed
				if ((root.myBal -= decrease) != 0) {
					root = root.rebalance(result);
				} else {
					result.value = 1;  // balanced because subtree decreased
				}
			} else {
				result.value = 0;
			}

			root.mergeCodomains(root.left, root.right);

			return root;
		}

		public void incrementMultiplicity(K key) {
		}

		public boolean decrementMultiplicity(K key) {
			return true;
		}
	}

	private class AvlIterator implements Iterator<E> {
		AvlNode<E> cur = root;
		boolean done = (root == null);
		Stack<AvlNode<E>> stack = new ArrayStack<AvlNode<E>>();

		@Override
		public boolean hasNext() {
			return !done;
		}

		@Override
		public E next() {
			while (cur != null) {
				stack.push(cur);
				cur = cur.left;
			}

			if (stack.isEmpty())
				throw new NoSuchElementException();

			AvlNode<E> prev = stack.pop();
			cur = prev.right;
			done = (cur == null && stack.isEmpty());
			return prev.getKey();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Not implemented");
		}
	}
}
