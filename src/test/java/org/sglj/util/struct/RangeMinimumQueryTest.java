package org.sglj.util.struct;

import java.util.Arrays;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;
import org.sglj.util.struct.RangeMinimumQuery;

public class RangeMinimumQueryTest {

	@Test
	public void test01() {
		//                              0  1  2  3  4  5  6  7  8  9
		RmqTester rmq = new RmqTester(new Integer[]{2, 4, 3, 1, 6, 7, 8, 9, 1, 7});
		System.out.println(rmq);
		for (int d = 1; d < rmq.size(); ++d) {
			for (int i = 0; i + d < rmq.size(); ++i) {
				System.out.print("min [" + i + ", " + (i + d) + "): ");
				System.out.println(rmq.retrieveQuery(i, i + d));
			}
		}
		System.out.println(rmq.retrieveQuery(2, 2));
	}
	
	@Test
	public void test02() {
		RmqTester rmq = new RmqTester(new Integer[]{2, 4, 1, 3});
		System.out.println(rmq);
		for (int d = 1; d < rmq.size(); ++d) {
			for (int i = 0; i + d < rmq.size(); ++i) {
				System.out.print("min [" + i + ", " + (i + d) + "): ");
				System.out.println(rmq.retrieveQuery(i, i + d));
			}
		}
		System.out.println(rmq.retrieveQuery(3, 3));
	}
	
	static Random RANDOM = new Random();
	
	@Test
	public void testRandom01() {
		for (int i = 1; i < 20; ++i)
			randomTest(i, 1000, 100);
	}
	
	@Test
	public void testRandom02() {
		randomTest(100, 100, 0x3f3f3f3f);
	}
	
	static void randomTest(final int n, int q, final int maxVal) {
		RmqTester rmq = new RmqTester(n);
		for (int i = 0; i < n; ++i) {
			rmq.set(i, RANDOM.nextInt(maxVal));
		}
		rmq.preprocess();
		System.out.println(rmq);
		while (q-- > 0) {
			int from = RANDOM.nextInt(n), to = RANDOM.nextInt(n + 1);
			if (from > to) { int t = from; from = to; to = t; }
			rmq.retrieveQuery(from, to);
		}
	}
	
	private static class RmqTester extends RangeMinimumQuery<Integer, Integer> {

		int bf[];
		
		public RmqTester(int capacity) {
			super(capacity);
			bf = new int[capacity];
			Arrays.fill(bf, Integer.MAX_VALUE);
		}
		
		public RmqTester(Integer[] arr) {
			super(arr);
			bf = new int[arr.length];
			for (int i = 0; i < arr.length; ++i)
				bf[i] = arr[i];
		}

		@Override
		public Integer mergeCodomains(Integer a, Integer b) {
			return Math.min(a, b);
		}

		@Override
		protected Integer createIdentityData(Integer element) {
			return element == null ? Integer.MAX_VALUE : element;
		}
		
		@Override
		public void set(int index, Integer element) {
			super.set(index, element);
			if (bf != null)
				bf[index] = element;
		}
		
		@Override
		public Integer retrieveQuery(int fromIndex, int toIndex) {
			Integer actual = super.retrieveQuery(fromIndex, toIndex);
			int expected = Integer.MAX_VALUE;
			for (int i = fromIndex; i < toIndex; ++i)
				expected = Math.min(expected, bf[i]);
			Assert.assertEquals(expected, (int)actual);
			return actual;
		}
	}
}
