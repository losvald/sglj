package org.sglj.util.struct;

import java.util.Random;

import org.junit.Test;
import org.sglj.util.struct.ImmutableFenwickTree;

public class FenwickTreeSpeedTest {

	static Random RANDOM = new Random();

	@Test
	public void testLeft1() {
		randomTest(1000, 1000, false);
	}

	@Test
	public void testLeft2() {
		randomTest(10000, 10000, false);
	}

	@Test
	public void testLeft3() {
		randomTest(100000, 100000, false);
	}

	@Test
	public void testLeft4() {
		randomTest(200000, 200000, false);
	}

	@Test
	public void testLeft5() {
		randomTest(400000, 400000, false);
	}

	@Test
	public void testRight1() {
		randomTest(1000, 1000, true);
	}

	@Test
	public void testRight2() {
		randomTest(10000, 10000, true);
	}

	@Test
	public void testRight3() {
		randomTest(100000, 100000, true);
	}

	@Test
	public void testRight4() {
		randomTest(200000, 200000, true);
	}

	@Test
	public void testRight5() {
		randomTest(400000, 400000, true);
	}

	static void randomTest(final int n, int q, boolean rightToLeft) {
		Integer[] arr = new Integer[n];
		for (int i = 0; i < n; ++i) {
			arr[i] = RANDOM.nextInt();
		}
		FTMin ft = new FTMin(arr, rightToLeft);
		while (q-- > 0) {
			int to = RANDOM.nextInt(n + 1);
			ft.retrieveQuery(to);
		}
	}

	private static class FTMin extends ImmutableFenwickTree<Integer, Integer> {

		public FTMin(int size, boolean rightToLeft) {
			super(size, rightToLeft);
		}

		public FTMin(Integer[] elements, boolean rightToLeft) {
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
	}
}
