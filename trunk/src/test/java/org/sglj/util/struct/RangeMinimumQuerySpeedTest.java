package org.sglj.util.struct;

import java.util.Random;

import org.junit.Test;
import org.sglj.util.struct.RangeMinimumQuery;


public class RangeMinimumQuerySpeedTest {

	static Random RANDOM = new Random();
	
	@Test
	public void testPreprocess1() {
		randomTest(1000, 0);
	}
	
	@Test
	public void testPreprocess2() {
		randomTest(10000, 0);
	}
	
	@Test
	public void testPreprocess3() {
		randomTest(100000, 0);
	}
	
	@Test
	public void testPreprocess4() {
		randomTest(200000, 0);
	}
	
	@Test
	public void testPreprocess5() {
		randomTest(400000, 0);
	}
	
	@Test
	public void testAll1() {
		randomTest(1000, 1000);
	}
	
	@Test
	public void testAll2() {
		randomTest(10000, 10000);
	}
	
	@Test
	public void testAll3() {
		randomTest(100000, 100000);
	}
	
	@Test
	public void testAll4() {
		randomTest(200000, 200000);
	}
	
	@Test
	public void testAll5() {
		randomTest(400000, 400000);
	}
	
	static void randomTest(final int n, int q) {
		Rmq rmq = new Rmq(n);
		
		for (int i = 0; i < n; ++i) {
			rmq.set(i, RANDOM.nextInt());
		}
		rmq.preprocess();
		while (q-- > 0) {
			int from = RANDOM.nextInt(n), to = RANDOM.nextInt(n + 1);
			if (from > to) { int t = from; from = to; to = t; }
			rmq.retrieveQuery(from, to);
		}
	}
	
	private static class Rmq extends RangeMinimumQuery<Integer, Integer> {
		
		public Rmq(int capacity) {
			super(capacity);
		}

		@Override
		public Integer mergeCodomains(Integer a, Integer b) {
			return Math.min(a, b);
		}

		@Override
		protected Integer createIdentityData(Integer element) {
			return element == null ? Integer.MAX_VALUE : element;
		}
		
	}
}
