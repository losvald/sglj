/*
 * IntervalTreeSpeedTest.java
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.sglj.util.struct.AvlTree.AvlNode;
import org.sglj.util.struct.IntervalTree.IntervalTraits;
import org.sglj.util.struct.IntervalTree.Node;
import org.sglj.util.struct.IntervalTreeTest.Interval;

@RunWith(Suite.class)
@SuiteClasses({
	IntervalTreeSpeedTest.Find10mDisjointPointsITTest.class,
	IntervalTreeSpeedTest.Find10mDisjointPointsABFTest.class,
	IntervalTreeSpeedTest.Find1mDisjointPointsITTest.class,
	IntervalTreeSpeedTest.Find1mDisjointPointsABFTest.class,
	IntervalTreeSpeedTest.Find10mDisjointIntervalsITTest.class,
	IntervalTreeSpeedTest.Find10mDisjointIntervalsABFTest.class,
	IntervalTreeSpeedTest.Find1mDisjointIntervalsITTest.class,
	IntervalTreeSpeedTest.Find1mDisjointIntervalsABFTest.class,
	IntervalTreeSpeedTest.Find1mConstant2OverlappingITTest.class,
	IntervalTreeSpeedTest.Find1mConstant2OverlappingABFTest.class,
	IntervalTreeSpeedTest.Find1mConstant4OverlappingITTest.class,
	IntervalTreeSpeedTest.Find1mConstant4OverlappingABFTest.class,
	IntervalTreeSpeedTest.Find1mConstant8OverlappingITTest.class,
	IntervalTreeSpeedTest.Find1mConstant8OverlappingABFTest.class,
	IntervalTreeSpeedTest.Find100kConstant100OverlappingITTest.class,
	IntervalTreeSpeedTest.Find100kConstant100OverlappingABFTest.class,
	IntervalTreeSpeedTest.Find1mLogOverlappingITTest.class,
	IntervalTreeSpeedTest.Find1mLogOverlappingABFTest.class,
})
public class IntervalTreeSpeedTest {
	static final IntervalTraits<Interval, Integer> traits =
			new IntervalTraits<Interval, Integer>() {
		@Override
		public Integer from(Interval interval) {
			return interval.from;
		}

		@Override
		public Integer to(Interval interval) {
			return interval.to;
		}

		@Override
		public Interval pointInterval(Integer endpoint) {
			return new Interval(endpoint, endpoint, Integer.MIN_VALUE);
		}
	};

	static IntervalTree<Interval, Integer> create() {
		return new IntervalTree<Interval, Integer>(traits);
	}

	static abstract class Finder {
		int hash;
		List<Interval> result = new ArrayList<Interval>(); //{
//			public boolean add(Interval e) {
//				return false;
//			}
//			public boolean addAll(java.util.Collection<? extends Interval> c) {
//				for (Interval in : c) {
//				}
//				return false;
//			}
//		};
		ArrayList<Integer> overlappingPoints = new ArrayList<Integer>();
		ArrayList<Integer> nonOverlappingPoints = new ArrayList<Integer>();

		void find(Integer point) {
			result.clear();
			findOverlapping(point, result);
			hash += result.hashCode();
		}

		abstract void findOverlapping(Integer point, Collection<Interval> result);
		abstract void add(Interval in);
		abstract void remove(Interval in);
		abstract void clear();
		abstract String repr();

		void setDisjointPoints(int n, int coordMax, Random random) {
			if (coordMax < n)
				throw new IllegalArgumentException();

			clear();
			HashSet<Integer> hs = new HashSet<Integer>(n);
			do {
				Integer e = random.nextInt(coordMax);
				if (!hs.add(e))
					continue;

				overlappingPoints.add(e);
				add(new Interval(e, e, -2));
			} while (hs.size() < n);
			do {
				Integer e = random.nextInt(coordMax);
				if (hs.contains(e))
					continue;

				nonOverlappingPoints.add(e);
				hs.add(e);
			} while (nonOverlappingPoints.size() != overlappingPoints.size());
		}

		private void initNonOverlappingPoints(int n, int coordMax,
				Random random) {
			HashSet<Integer> hs = new HashSet<Integer>();
			nonOverlappingPoints.clear();
			do {
				Integer e = -random.nextInt(coordMax - 3) - 1;
				if (hs.contains(e))
					continue;

				nonOverlappingPoints.add(e);
				hs.add(e);
			} while (nonOverlappingPoints.size() != n);
		}

		void setLinearOverlapping(int n, int coordMax, Random random) {
			clear();
			Interval[] intervals = new Interval[n];
			for (int i = 0; i < n; ++i) {
				add(intervals[i]
						= IntervalTreeTest.randomInterval(random, coordMax));
			}
			HashSet<Integer> hs = new HashSet<Integer>(n);
			overlappingPoints.clear();
			do {
				Integer i = random.nextInt(n);
				int from = intervals[i].from;
				int to = intervals[i].to;
				int e = from + random.nextInt(to - from + 1);
				if (!hs.add(e))
					continue;

				overlappingPoints.add(e);
			} while (hs.size() < n);
			initNonOverlappingPoints(n, coordMax, random);
		}

		void setConstantOverlapping(int n, int k, int coordMax, Random random) {
			if (k < 1 || n < k)
				throw new IllegalArgumentException();

			clear();
			overlappingPoints.clear();
			List<TreeSet<Integer>> froms = new ArrayList<TreeSet<Integer>>(k);
			for (int i = 0; i < k; ++i) {
				froms.add(new TreeSet<Integer>());
				froms.get(froms.size() - 1).add(0);
				overlappingPoints.add(1 + random.nextInt(coordMax));
			}
			initNonOverlappingPoints(n, coordMax, random);

			n -= k;
			while (n != 0) {
				if (!froms.get(random.nextInt(k)).add(
						1 + random.nextInt(coordMax)))
					continue;
				--n;
			}
			for (int i = 0; i < k; ++i) {
				Integer to = coordMax;
				for (Integer from : froms.get(i).descendingSet()) {
					add(new Interval(from, to));
					to = from - 1;
				}
			}
		}

		void findOverlapping(int n, Random random) {
			findOverlapping(overlappingPoints, n, random);
		}

		void findNonOverlapping(int n, Random random) {
			findOverlapping(nonOverlappingPoints, n, random);
		}

		private void findOverlapping(ArrayList<Integer> points,
				int n, Random random) {
			while (n-- != 0)
				find(points.get(random.nextInt(points.size())));
		}

		@Override
		public String toString() {
			return "{>}: " + overlappingPoints + "\n" + repr();
		}
	}

	static abstract class BruteForceFinder extends Finder {
		Collection<Interval> intervals = init();

		abstract Collection<Interval> init();

		public void findOverlapping(Integer point, Collection<Interval> result) {
			for (Interval interval : intervals) {
				if (interval.from.compareTo(point) <= 0
						&& point.compareTo(interval.to) <= 0)
					result.add(interval);
			}
		}

		@Override
		void add(Interval in) {
			intervals.add(in);
		}

		@Override
		void remove(Interval in) {
			intervals.remove(in);
		}

		@Override
		void clear() {
			intervals.clear();
		}

		@Override
		String repr() {
			return intervals.toString();
		}
	}

	static class IntervalTreeFinder extends Finder {
		IntervalTree<Interval, Integer> t = create();

		@Override
		void findOverlapping(Integer point, Collection<Interval> result) {
			t.findOverlapping(point, result);
		}

		@Override
		void add(Interval in) {
			t.add(in);
		}

		@Override
		void remove(Interval in) {
			t.remove(in);
		}

		@Override
		void clear() {
			t.clear();
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

		@Override
		String repr() {
			StringBuilder sb = new StringBuilder();
			repr(t.getRoot(), sb);
			return sb.toString();
		}
	}

	static class ArrayBruteForceFinder extends BruteForceFinder {
		@Override
		Collection<Interval> init() {
			return new ArrayList<Interval>();
		}
	}

	public static abstract class TestBase {
		static final int TIMEOUT = 2000;

		private Method testMethod;
		private int attempts;

		abstract int getN();

		Finder createFinder() {
			return new IntervalTreeFinder();
		}

		@Before
		public void resetIntervalId() {
			Interval.curId = 0;
		}

		protected void profileLater(int attempts) {
			if (!isProfilingEnabled())
				return;

			try {
				testMethod = this.getClass().getMethod(
						Thread.currentThread().getStackTrace()[3]
								.getMethodName());
				Test annot = testMethod.getAnnotation(Test.class);
				if (annot == null || skipProfiling(annot))
					testMethod = null;
				else {
					this.attempts = attempts;
				}
			} catch (Exception e) {
			}
		}

		protected boolean skipProfiling(Test test) {
			return test.timeout() != 0
					&& !this.getClass().getName().endsWith("ITTest");
		}

		@After
		public void rerunProfiled() {
			if (!isProfilingEnabled() || testMethod == null)
				return;

			try {
				long minTime = Long.MAX_VALUE;
				for (int attempts = this.attempts; attempts-- > 0; ) {
					long time = System.nanoTime();
					testMethod.invoke(this);
					minTime = Math.min(System.nanoTime() - time, minTime);
				}
				System.err.printf("%s.%s\t%4.6f\n",
						this.getClass().getCanonicalName(),
						testMethod.getName(),
						(double)minTime / 1e6);
			} catch (Exception e) {
				Assert.fail("Failed to repeat test: " + testMethod);
			} finally {
				testMethod = null;
			}
		}

		protected boolean isProfilingEnabled() {
			return true;
		}
	}

	static void testDisjointPoints(int findCount, int size, int coordMax,
			Random random, Finder f) {
		f.setDisjointPoints(size, coordMax, random);
		f.findOverlapping(findCount, random);
	}

	static abstract class FindDisjointPointsTestBase extends TestBase {
		void test(int size, int attempts) {
			profileLater(attempts);
			testDisjointPoints(getN(), size, Integer.MAX_VALUE,
					new Random(42), createFinder());
		}
	}

	public static class Find10mDisjointPointsITTest extends
	FindDisjointPointsTestBase {
		@Override
		int getN() { return 10000000; }

		@Test
		public void test01() { test(1, 1); }

		@Test
		public void test02() { test(2, 1); }

		@Test
		public void test04() { test(4, 1); }

		@Test
		public void test08() { test(8, 1); }

		@Test
		public void test16() { test(16, 1); }
	}

	public static class Find10mDisjointPointsABFTest extends
	Find10mDisjointPointsITTest {
		@Override
		Finder createFinder() {
			return new ArrayBruteForceFinder();
		}
	}

	public static class Find1mDisjointPointsITTest extends
	FindDisjointPointsTestBase {
		static final int TIMEOUT = 3300;

		@Override
		int getN() { return 1000000; }

		@Test
		public void test000025() { test(25, 2); }

		@Test
		public void test000050() { test(50, 1); }

		@Test
		public void test000100() { test(100, 1); }

		@Test
		public void test000200() { test(200, 1); }

		@Test
		public void test000400() { test(400, 1); }

		@Test(timeout=TIMEOUT/2)
		public void test001000() { test(1000, 2); }

		@Test(timeout=TIMEOUT/2)
		public void test002000() { test(2000, 2); }

		@Test(timeout=TIMEOUT/2)
		public void test004000() { test(4000, 2); }

		@Test(timeout=TIMEOUT/2)
		public void test010000() { test(10000, 1); }

		@Test(timeout=TIMEOUT)
		public void test020000() { test(20000, 2); }

		@Test(timeout=TIMEOUT)
		public void test040000() { test(40000, 2); }

		@Test(timeout=TIMEOUT)
		public void test100000() { test(100000, 2); }
	}

	public static class Find1mDisjointPointsABFTest extends
	Find1mDisjointPointsITTest {
		@Override
		Finder createFinder() {
			return new ArrayBruteForceFinder();
		}
	}

	static void testConstantOverlap(int findCount, int size, int k,
			int coordMax, Random random, Finder f) {
		f.setConstantOverlapping(size, k, coordMax, random);
		f.findNonOverlapping(findCount, random);
		//System.out.println(f);
	}

	public static class Find10mDisjointIntervalsITTest extends Find10mDisjointPointsITTest {
		void test(int size, int attempts) {
			profileLater(attempts);
			testConstantOverlap(getN(), size, 1, Integer.MAX_VALUE,
					new Random(42), createFinder());
		}
	}

	public static class Find10mDisjointIntervalsABFTest extends Find10mDisjointIntervalsITTest {
		@Override
		Finder createFinder() {
			return new ArrayBruteForceFinder();
		}
	}

	public static class Find1mDisjointIntervalsITTest extends Find1mDisjointPointsITTest {
		void test(int size, int attempts) {
			profileLater(attempts);
			testConstantOverlap(getN(), size, 1, Integer.MAX_VALUE,
					new Random(42), createFinder());
		}
	}

	public static class Find1mDisjointIntervalsABFTest extends Find1mDisjointIntervalsITTest {
		@Override
		Finder createFinder() {
			return new ArrayBruteForceFinder();
		}
	}

	static abstract class FindConstantOverlappingTestBase extends TestBase {
		abstract int getK();

		void test(int size) {
			profileLater(1);
			testConstantOverlap(getN(), size, getK(),
					Integer.MAX_VALUE, new Random(42),
					createFinder());
		}
	}

	public static class Find1mConstant2OverlappingITTest extends
	FindConstantOverlappingTestBase {
		static final int TIMEOUT = 4500;

		@Override
		int getN() { return 1000000; }

		@Override
		int getK() { return 2; }

		@Test
		public void test000002() { test(2); }

		@Test
		public void test000003() { test(3); }

		@Test
		public void test000004() { test(4); }

		@Test
		public void test000005() { test(5); }

		@Test
		public void test000008() { test(8); }

		@Test
		public void test000010() { test(10); }

		@Test
		public void test000016() { test(16); }

		@Test
		public void test000020() { test(20); }

		@Test
		public void test000040() { test(40); }

		@Test
		public void test000080() { test(80); }

		@Test
		public void test000100() { test(100); }

		@Test
		public void test000200() { test(200); }

		@Test
		public void test000320() { test(320); }

		@Test
		public void test000400() { test(400); }

		@Test(timeout=TIMEOUT/2)
		public void test001000() { test(1000); }

		@Test(timeout=TIMEOUT/2)
		public void test002000() { test(2000); }

		@Test(timeout=TIMEOUT/2)
		public void test004000() { test(4000); }

		@Test(timeout=TIMEOUT)
		public void test010000() { test(10000); }

		@Test(timeout=TIMEOUT)
		public void test020000() { test(20000); }

		@Test(timeout=TIMEOUT)
		public void test040000() { test(40000); }

		@Test(timeout=TIMEOUT)
		public void test100000() { test(100000); }
	}

	public static class Find1mConstant2OverlappingABFTest extends
	Find1mConstant2OverlappingITTest {
		@Override
		Finder createFinder() {
			return new ArrayBruteForceFinder();
		}
	}

	public static class Find1mConstant4OverlappingITTest extends
	Find1mConstant2OverlappingITTest {
		@Override
		int getK() { return 4; }
	}

	public static class Find1mConstant4OverlappingABFTest extends
	Find1mConstant2OverlappingABFTest {
		@Override
		int getK() { return 4; }
	}

	public static class Find1mConstant8OverlappingITTest extends
	Find1mConstant4OverlappingITTest {
		@Override
		int getK() { return 8; }
	}

	public static class Find1mConstant8OverlappingABFTest extends
	Find1mConstant4OverlappingABFTest {
		@Override
		int getK() { return 8; }
	}

	public static class Find100kConstant100OverlappingITTest extends
	FindConstantOverlappingTestBase {
		static final int TIMEOUT = 2500;

		@Override
		int getN() { return 100000; }

		@Override
		int getK() { return 100; }

		@Test
		public void test000100() { test(100); }

		@Test
		public void test000200() { test(200); }

		@Test
		public void test000400() { test(400); }

		@Test
		public void test001000() { test(1000); }

		@Test
		public void test002000() { test(2000); }

		@Test
		public void test004000() { test(4000); }

		@Test
		public void test010000() { test(10000); }

		@Test
		public void test020000() { test(20000); }

		@Test
		public void test040000() { test(40000); }

		@Test
		public void test100000() { test(100000); }
	}

	public static class Find100kConstant100OverlappingABFTest extends
	Find100kConstant100OverlappingITTest {
		@Override
		Finder createFinder() {
			return new ArrayBruteForceFinder();
		}
	}

	static abstract class FindLogOverlappingTestBase extends TestBase {
		void test(int size) {
			profileLater(1);
			testConstantOverlap(getN(), size,
					32 - Integer.numberOfLeadingZeros(size),
					Integer.MAX_VALUE, new Random(42),
					createFinder());
		}
	}

	public static class Find1mLogOverlappingITTest extends
	FindLogOverlappingTestBase {
		static final int TIMEOUT = 6000;
		@Override
		int getN() { return 1000000; }

		@Test
		public void test000004() { test(4); }

		@Test
		public void test000010() { test(10); }

		@Test
		public void test000020() { test(20); }

		@Test
		public void test000040() { test(40); }

		@Test
		public void test000100() { test(100); }

		@Test
		public void test000200() { test(200); }

		@Test
		public void test000400() { test(400); }

		@Test(timeout=TIMEOUT/2)
		public void test001000() { test(1000); }

		@Test(timeout=TIMEOUT/2)
		public void test002000() { test(2000); }

		@Test(timeout=TIMEOUT/2)
		public void test004000() { test(4000); }

		@Test(timeout=TIMEOUT/2)
		public void test010000() { test(10000); }

		@Test(timeout=TIMEOUT)
		public void test020000() { test(20000); }

		@Test(timeout=TIMEOUT)
		public void test040000() { test(40000); }

		@Test(timeout=TIMEOUT)
		public void test100000() { test(100000); }

		@Test(timeout=TIMEOUT)
		public void test200000() { test(200000); }

		@Test(timeout=2*TIMEOUT)
		public void test400000() { test(400000); }
	}

	public static class Find1mLogOverlappingABFTest extends
	Find1mLogOverlappingITTest {
		@Override
		Finder createFinder() {
			return new ArrayBruteForceFinder();
		}
	}

	static void testLinearOverlap(int findCount, int size, int coordMax,
			Random random, Finder f) {
		f.setLinearOverlapping(size, coordMax, random);
		f.findOverlapping(findCount, random);
		//System.out.println(f);
	}

	static abstract class FindLinearOverlappingTestBase extends TestBase {
		void test(int size) {
			profileLater(1);
			testLinearOverlap(getN(), size, Integer.MAX_VALUE, new Random(42),
					createFinder());
		}
	}

	public static class Find10kLinearOverlappingITTest extends
	FindLinearOverlappingTestBase {
		@Override
		int getN() { return 10000; }

		@Test(timeout=TIMEOUT)
		public void test1() { test(1); }

		@Test(timeout=TIMEOUT)
		public void test10() { test(10); }

		@Test(timeout=TIMEOUT)
		public void test100() { test(100); }
	}

	public static class Find10kLinearOverlappingABFTest extends
	Find10kLinearOverlappingITTest {
		@Override
		Finder createFinder() {
			return new ArrayBruteForceFinder();
		}
	}
}

// Profile output beautify command:
// sed -r -e 's/k('$'\t'')/000\1/' -e 's/.test/'$'\t''/' -e 's/.*SpeedTest\.//'

