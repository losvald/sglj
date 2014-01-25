package org.sglj.util.struct;

import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;
import org.sglj.util.struct.FenwickTree;
import org.sglj.util.struct.ImmutableFenwickTree;

public class FenwickTreeTest {

	static Random RANDOM = new Random();
	
	static class FTSumTester extends FenwickTree<Integer, Integer> {

		boolean parentInitialized = true;
		
		public FTSumTester(int size, boolean rightToLeft) {
			super(size, rightToLeft);
		}
		
		public FTSumTester(Integer[] elements, boolean rightToLeft) {
			super(elements, rightToLeft);
		}

		@Override
		public Integer mergeCodomains(Integer a, Integer b) {
			return a + b;
		}

		@Override
		public Integer inheritSubCodomain(Integer domain, Integer subdomain) {
			return domain - subdomain;
		}

		@Override
		protected Integer createData(Integer element) {
			return element != null ? element : 0;
		}
		
		@Override
		public Integer retrieveQuery(int index) {
			int actual = super.retrieveQuery(index);
			if (parentInitialized) {
				int expected = 0;
				if (!isRightToLeft()) {
					for (int i = 0; i < index; ++i)
						expected += get(i);
				} else {
					for (int i = index; i < indexCount(); ++i)
						expected += get(i); 
				}
				Assert.assertEquals(expected, actual);
			}
			return actual;
		}

	}
	
	@Test
	public void testSum01() {
		FTSumTester ft = new FTSumTester(
				//            0  1  2  3  4  5  6  7  8  9
				new Integer[]{2, 4, 3, 1, 6, 7, 8, 9, 1, 7}, false);
		
		System.out.println(ft);
		for (int i = 0; i < ft.size(); ++i) {
			System.out.print("sum [0, " + i + "): ");
			System.out.println(ft.retrieveQuery(i));
		}
		
		ft.set(4, 2);
		System.out.println(ft);
		for (int i = 0; i < ft.size(); ++i) {
			System.out.print("sum [0, " + i + "): ");
			System.out.println(ft.retrieveQuery(i));
		}
		
		ft.set(1, 4);
		System.out.println(ft);
		for (int i = 0; i < ft.size(); ++i) {
			System.out.print("sum [0, " + i + "): ");
			System.out.println(ft.retrieveQuery(i));
		}
	}
	
	@Test
	public void testRandomSum01() {
		for (int i = 1; i < 20; ++i) {
			randomMinTest(i, 1000, 100, false);
		}
	}

	@Test
	public void testRandomSum02() {
		randomMinTest(100, 100, 0x3f3f3f3f, true);
		randomMinTest(100, 100, 0x3f3f3f3f, false);
	}
	
	static void randomSumTest(final int n, int q, final int maxVal,
			boolean rightToLeft) {
		Integer[] arr = new Integer[n];
		for (int i = 0; i < n; ++i) {
			arr[i] = RANDOM.nextInt(maxVal);
		}
		FTSumTester ft = new FTSumTester(arr, rightToLeft);
		System.out.println(ft);
		while (q-- > 0) {
			if (RANDOM.nextBoolean()) {
				int to = RANDOM.nextInt(n + 1);
				ft.retrieveQuery(to);
			} else {
				ft.set(RANDOM.nextInt(n), RANDOM.nextInt());
			}
		}
	}
	
	
	static class FTMinTester extends ImmutableFenwickTree<Integer, Integer> {
		
		boolean parentInitialized = true;
		
		public FTMinTester(Integer[] elements, boolean rightToLeft) {
			super(elements, rightToLeft);
		}

		@Override
		public Integer mergeCodomains(Integer a, Integer b) {
			return Math.min(a, b);
		}

		@Override
		protected Integer createData(Integer element) {
			return element != null ? element : Integer.MAX_VALUE;
		}
		
		@Override
		public Integer retrieveQuery(int index) {
			int actual = super.retrieveQuery(index);
			if (parentInitialized) {
				int expected = Integer.MAX_VALUE;
				if (!isRightToLeft()) {
					for (int i = 0; i < index; ++i)
						expected = Math.min(expected, get(i));
				} else {
					for (int i = index; i < indexCount(); ++i)
						expected = Math.min(expected, get(i));
				}
				Assert.assertEquals(expected, actual);
			}
			return actual;
		}

	}
	
	@Test
	public void testMin01() {
		FTMinTester ft = new FTMinTester(
				//            0  1  2  3  4  5  6  7  8  9
				new Integer[]{2, 4, 3, 1, 6, 7, 8, 9, 1, 7}, false);
		System.out.println(ft);
		for (int i = 0; i < ft.size(); ++i) {
			System.out.print("min [0, " + i + "): ");
				System.out.println(ft.retrieveQuery(i));
		}
		System.out.println(ft.retrieveQuery(2));
		
		try {
			ft.set(0, 5);
			Assert.fail();
		} catch (UnsupportedOperationException e) {
		}
	}
	
	@Test
	public void testMin02() {
		FTMinTester ft = new FTMinTester(
				//            0  1  2  3  4  5  6  7
				new Integer[]{9, 7, 8, 5, 3, 3, 5, 1}, false);
		System.out.println(ft);
		for (int i = 0; i < ft.size(); ++i) {
			System.out.print("min [0, " + i + "): ");
				System.out.println(ft.retrieveQuery(i));
		}
	}
	
	@Test
	public void testRandomMin01() {
		for (int i = 1; i < 20; ++i) {
			randomMinTest(i, 1000, 100, false);
		}
	}

	@Test
	public void testRandomMin02() {
		randomMinTest(100, 100, 0x3f3f3f3f, true);
		randomMinTest(100, 100, 0x3f3f3f3f, false);
	}

	static void randomMinTest(final int n, int q, final int maxVal,
			boolean rightToLeft) {
		Integer[] arr = new Integer[n];
		for (int i = 0; i < n; ++i) {
			arr[i] = RANDOM.nextInt(maxVal);
		}
		FTMinTester ft = new FTMinTester(arr, rightToLeft);
		System.out.println(ft);
		while (q-- > 0) {
			int to = RANDOM.nextInt(n + 1);
			ft.retrieveQuery(to);
		}
	}
}
