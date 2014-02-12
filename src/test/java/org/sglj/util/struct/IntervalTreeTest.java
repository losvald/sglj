/*
 * IntervalTreeTest.java
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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;


public class IntervalTreeTest {
	static class Interval implements Comparable<Interval> {
		Integer id;
		Integer from, to;

		static int curId = 0;

		public Interval(Integer from, Integer to) {
			this(from, to, curId++);
		}

		public Interval(Integer from, Integer to, Integer id) {
			this.from = from;
			this.to = to;
			this.id = id;
		}

		@Override
		public int compareTo(Interval o) {
			return id.compareTo(o.id);
		}

		@Override
		public String toString() {
			return "(" + from + " " + to + " #" + id + ")";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Interval other = (Interval) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
	}

	static class VerifiedIntervalTree extends IntervalTree<Interval, Integer> {
		Set<Interval> intervals = new TreeSet<Interval>(traits.ascComparator);

		public VerifiedIntervalTree() {
			super(new IntervalTraits<Interval, Integer>() {
				@Override
				public Integer from(Interval interval) {
					return interval.from;
				}

				@Override
				public Integer to(Interval interval) {
					return interval.to;
				}

				@Override
				public Interval pointInterval(Integer endpoints) {
					return new Interval(endpoints, endpoints, Integer.MIN_VALUE);
				}
			});
		}

		private static String repr(AvlNode<?> node) {
			@SuppressWarnings("unchecked")
			Node<Interval, Integer> n = (Node<Interval, Integer>)node;
			return n.point.from + ": " + n.asc;
		}

		static void repr(AvlNode<?> node, StringBuilder sb) {
			sb.append('(');
			if (node != null) {
				sb.append(repr(node));
				sb.append(" <");
				repr(node.left, sb);
				sb.append(" >");
				repr(node.right, sb);
			}
			sb.append(')');
		}

		String repr() {
			StringBuilder sb = new StringBuilder();
			repr(getRoot(), sb);
			return sb.toString();
		}

		@Override
		public boolean add(Interval e) {
			intervals.add(e);
			return super.add(e);
		}

		@Override
		public boolean remove(Object o) {
			intervals.remove(o);
			return super.remove(o);
		}

		@Override
		public void findOverlapping(Integer point, Collection<Interval> result) {
			Set<Interval> expResult = new TreeSet<Interval>(result);
			for (Interval interval : intervals) {
				if (interval.from.compareTo(point) <= 0
						&& point.compareTo(interval.to) <= 0)
					expResult.add(interval);
			}

			super.findOverlapping(point, result);

			Set<Interval> actResult = new TreeSet<Interval>(result);
			assertEquals(expResult.toString(), actResult.toString());
		}

		Interval randomContained(Random random) {
			if (intervals.isEmpty())
				return null;

			int ord = random.nextInt(intervals.size());
			for (Interval interval : intervals)
				if (ord-- == 0)
					return interval;
			return null;
		}
	}

	static void assertRepr(VerifiedIntervalTree t, String expected) {
		assertEquals(expected, t.repr());
	}

	private static void permute(String prefix, String str, List<int[]> perms) {
	    int n = str.length();
	    if (n == 0) {
	    	int[] a = new int[prefix.length()];
	    	for (int i = 0; i < a.length; ++i)
	    		a[i] = prefix.charAt(i) - '0';
	    	perms.add(a);
	    }
	    else {
	        for (int i = 0; i < n; i++)
	            permute(prefix + str.charAt(i),
	            		str.substring(0, i) + str.substring(i+1, n),
	            		perms);
	    }
	}

	static List<int[]> permute(int n) {
		List<int[]> perms = new ArrayList<int[]>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; ++i) {
			sb.append((char)('0' + i));
			permute("", sb.toString(), perms);
		}
		return perms;
	}

	static final int[] permFrom = new int[]{0, 0, 0, 1, 1, 2};
	static final int[] permTo   = new int[]{1, 2, 3, 2, 3, 3};

	@Test
	public void testMoveAfterRotate() {
		VerifiedIntervalTree vit = new VerifiedIntervalTree();
		vit.add(new Interval(2, 3, 23));
		vit.add(new Interval(2, 7, 27));
		vit.add(new Interval(1, 1, 11));
		vit.add(new Interval(5, 6, 56));
		vit.add(new Interval(8, 9, 89));
		vit.add(new Interval(4, 4, 44));
		assertRepr(vit, "(2: [(2 7 #27), (2 3 #23)]"
				+ " <(1: [(1 1 #11)] <() >())"
				+ " >(5: [(5 6 #56)] <(4: [(4 4 #44)] <() >()) >(8: [(8 9 #89)] <() >())))");

		vit.remove(new Interval(1, 1, 11));
		assertRepr(vit, "(5: [(2 7 #27), (5 6 #56)]"
				+ " <(2: [(2 3 #23)] <() >(4: [(4 4 #44)] <() >()))"
				+ " >(8: [(8 9 #89)] <() >()))");
	}

	@Test
	public void testFindOverlappingAllPermutations() {
		for (int[] perm : permute(permFrom.length)) {
			VerifiedIntervalTree vit = new VerifiedIntervalTree();
			System.out.println(Arrays.toString(perm));
			for (int id = 0; id < perm.length; ++id) {
				Interval in = new Interval(permFrom[perm[id]], permTo[perm[id]],
						id);
				vit.add(in);
				System.out.println("+ " + in);
			}
			System.out.println(vit.repr());
			for (int point = 0; point < perm.length; ++point) {
				TreeSet<Interval> overlapping = new TreeSet<Interval>();
				System.out.print("> " + point);
				vit.findOverlapping(point, overlapping);
				//System.out.println(": " + overlapping);
			}
		}
	}

	@Test
	public void testSameIntervalsAllPermutations() {
		for (int[] perm : permute(4)) {
			for (int toRemove : perm) {
				VerifiedIntervalTree vit = new VerifiedIntervalTree();
				for (int id : perm)
					vit.add(new Interval(2, 2, id));
				vit.remove(new Interval(2, 2, toRemove));

				if (perm.length == 1) {
					assertRepr(vit, "()");
					continue;
				}

				StringBuilder sb = new StringBuilder("(2: [");
				boolean first = true;
				for (int id = perm.length - 1; id >= 0; --id)
					if (id != toRemove) {
						if (!first) sb.append(", ");
						sb.append("(2 2 #" + id + ")");
						first = false;
					}
				sb.append("] <() >())");
				assertEquals(sb.toString(), vit.repr());
			}
		}
	}

	static Interval randomInterval(Random r, int coordMax) {
		int a = r.nextInt(coordMax);
		int b = r.nextInt(coordMax);
		return a <= b ? new Interval(a, b) : new Interval(b, a);
	}

	@Test
	public void testRandomOverlapping() {
		final int sizeAvg = 100;
		final int coordMax = 50;
		final Random r = new Random(42);
		VerifiedIntervalTree vit = new VerifiedIntervalTree();
		Interval.curId = 0;
		for (int i = 0; i < sizeAvg; ++i) {
			vit.add(randomInterval(r, coordMax));
		}

		for (int itr = 0; itr < 1000; ++itr) {
			//System.out.println(vit.repr());
			boolean add;
			int sizeDiff = vit.intervals.size() - sizeAvg;
			if (sizeDiff > 10)
				add = false;
			else if (sizeDiff < -10)
				add = true;
			else
				add = r.nextBoolean();
			if (add) {
				Interval in = randomInterval(r, coordMax);
				//System.out.println("+ " + in);
				vit.add(in);
			} else {
				Interval in = vit.randomContained(r);
				//System.out.println("- " + in);
				vit.remove(in);
			}

			for (int i = 0; i < 5; ++i) {
				vit.findOverlapping(r.nextInt(coordMax), new TreeSet<Interval>());
			}
		}
	}
}
