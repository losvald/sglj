package org.sglj.util.struct;

import java.util.Comparator;

import junit.framework.Assert;

import org.junit.Test;

public class MinimumQueueTest {

	@Test
	public void test01() {
		// 1 2 3 1 4 5 2 3 6
		MinimumQueue<Integer> q = createMax();
		Assert.assertEquals(null, q.query());
		
		q.add(1);
		System.out.println(q);
		Assert.assertEquals((Integer)1, q.query());
		
		q.add(2);
		System.out.println(q);
		Assert.assertEquals((Integer)2, q.query());
		
		q.add(3);
		System.out.println(q);
		Assert.assertEquals((Integer)3, q.query());
		
		q.remove();
		q.add(1);
		System.out.println(q);
		Assert.assertEquals((Integer)3, q.query());
		
		q.remove();
		q.add(4);
		System.out.println(q);
		Assert.assertEquals((Integer)4, q.query());
		
		q.remove();
		q.add(5);
		System.out.println(q);
		Assert.assertEquals((Integer)5, q.query());
		
		q.remove();
		q.add(2);
		System.out.println(q);
		Assert.assertEquals((Integer)5, q.query());
		
		System.out.println(q);
		q.remove();
		System.out.println(q);
		q.add(3);
		System.out.println(q);
		Assert.assertEquals((Integer)5, q.query());
		
		q.remove();
		q.add(6);
		System.out.println(q);
		Assert.assertEquals((Integer)6, q.query());
	}
	
	MinimumQueue<Integer> createMax(Integer... ints) {
		return new MinimumQueue<Integer>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2.compareTo(o1);
			}
		});
	}
	
	
}
