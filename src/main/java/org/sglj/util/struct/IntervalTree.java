/*
 * IntervalTree.java
 * 
 * Copyright (C) 2014 Leo Osvald <leo.osvald@gmail.com>
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

import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;

public class IntervalTree<E, K extends Comparable<? super K>>
extends AvlTree<E> {

	protected final IntervalTraits<E, K> traits;

	public IntervalTree(final IntervalTraits<E, K> traits) {
		this.traits = traits;
		setComparator(traits.getOverlapComparator());
	}

	@SuppressWarnings("unchecked")
	public void findOverlapping(K point, Collection<E> result) {
		final E pointInterval = traits.pointInterval(point);
		final Comparator<Object> pointComparator = getComparator();
		Node<E, K> root = (Node<E, K>)getRoot();
		while (root != null) {
			int cmp = root.compareKey(pointInterval, pointComparator);
			NavigableSet<E> overlappingSet;
			if (cmp < 0) {
				overlappingSet = root.asc;
				root = (Node<E, K>)root.left;
			} else if (cmp > 0) {
				overlappingSet = root.desc;
				root = (Node<E, K>)root.right;
			} else { // optimize for full overlap
				if (root.asc.size() == 1)
					result.add(root.asc.first());
				else
					result.addAll(root.asc);
				break;
			}

			E last = overlappingSet.floor(pointInterval);
			if (last != null) {
				if (overlappingSet.first() == last) { // optimize for 1 overlap
					result.add(last);
				} else {
					overlappingSet = overlappingSet.headSet(pointInterval, true);
					result.addAll(overlappingSet);
				}
			}
		}
	}

	@Override
	protected AvlNode<E> createNode(E interval) {
		return new Node<E, K>(interval, traits);
	}

	@Override
	public boolean contains(Object o) {
		@SuppressWarnings("unchecked")
		Node<E, K> node = (Node<E, K>)Node.search(
				getRoot(), (E)o, getComparator());
		if (node == null)
			return false;

		return node.asc.contains(o);
	}

	protected static abstract class IntervalTraits<I, K extends Comparable<? super K>> {
		protected Comparator<I> overlapComparator;
		protected Comparator<I> ascComparator;
		protected Comparator<I> descComparator;

		public IntervalTraits() {
			this(new Comparator<I>() {
				@SuppressWarnings("unchecked")
				@Override
				public int compare(I o1, I o2) {
					return ((Comparable<I>)o1).compareTo(o2);
				}
			});
		}

		public IntervalTraits(final Comparator<I> idComparator) {
			overlapComparator = new Comparator<I>() {
				@Override
				public int compare(I o1, I o2) {
					int cmp = to(o1).compareTo(from(o2));
					if (cmp < 0)
						return cmp;
					cmp = from(o1).compareTo(to(o2));
					if (cmp > 0)
						return cmp;
					return 0;
				}
			};
			ascComparator = new Comparator<I>() {
				@Override
				public int compare(I o1, I o2) {
					int cmp = from(o1).compareTo(from(o2));
					if (cmp != 0)
						return cmp;
					cmp = to(o2).compareTo(to(o1));
					if (cmp != 0)
						return cmp;
					return idComparator.compare(o2, o1);
				}
			};
			descComparator = new Comparator<I>() {
				@Override
				public int compare(I o1, I o2) {
					int cmp = to(o2).compareTo(to(o1));
					if (cmp != 0)
						return cmp;
					cmp = from(o1).compareTo(from(o2));
					if (cmp != 0)
						return cmp;
					return idComparator.compare(o2, o1);
				}
			};
		}

		public abstract K from(I interval);
		public abstract K to(I interval);

		/**
		 * Creates a point interval <tt>[endpoint, endpoint]</tt> such
		 * that any smaller than any interval <tt>[endpoint, endpoint]</tt>
		 * with respect to the {@link Comparable#compareTo(Object)} method.
		 *
		 * @param endpoint
		 * @return
		 */
		public abstract I pointInterval(K endpoint);

		public Comparator<I> getOverlapComparator() {
			return overlapComparator;
		}

		public Comparator<I> getAscendingComparator() {
			return ascComparator;
		}

		public Comparator<I> getDescendingComparator() {
			return descComparator;
		}
	}

	protected static class Node<E, K extends Comparable<? super K>>
	extends AvlNode<E> {
		protected E point;
		protected final TreeSet<E> asc;
		protected final TreeSet<E> desc;

		public Node(E interval, IntervalTraits<E, K> traits) {
			this.point = traits.pointInterval(traits.from(interval));
			this.asc = new TreeSet<E>(traits.getAscendingComparator());
			this.desc = new TreeSet<E>(traits.getDescendingComparator());
		}

		@Override
		public AvlNode<E> mergeCodomains(AvlNode<E> a, AvlNode<E> b) {
			if (left != null) {
				@SuppressWarnings("unchecked")
				Node<E, K> l = (Node<E, K>)left;
				moveAll(l.desc, point, l.asc);
			}
			if (right != null) {
				@SuppressWarnings("unchecked")
				Node<E, K> r = (Node<E, K>)right;
				moveAll(r.asc, point, r.desc);
			}
			return this;
		}

		private void moveAll(TreeSet<E> moveSrc, E point, TreeSet<E> src) {
			E lastToMove = moveSrc.floor(point);
			if (lastToMove == null)
				return;

			// optimize for a single interval removal
			if (lastToMove == moveSrc.first()) {
				moveSrc.remove(lastToMove);
				src.remove(lastToMove);
				asc.add(lastToMove);
				desc.add(lastToMove);
				return;
			}

			NavigableSet<E> toMove = moveSrc.headSet(point, true);
			src.removeAll(toMove);
			asc.addAll(toMove);
			desc.addAll(toMove);
			toMove.clear();
		}

		@Override
		public void incrementMultiplicity(E key) {
			asc.add(key);
			desc.add(key);
		}

		@Override
		public boolean decrementMultiplicity(E key) {
			asc.remove(key);
			desc.remove(key);
			return desc.isEmpty();
		}

		@Override
		public E getKey() {
			return point;
		}

		@Override
		public String toString() {
			return "[" + point + ": " + asc + "]";
		}
	}
}
