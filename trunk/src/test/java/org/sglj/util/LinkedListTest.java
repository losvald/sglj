package org.sglj.util;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

public class LinkedListTest {

	@Test
	public void testConcat01() {
		checkConcatenation(create(1, 2), create(3, 4, 5));
		checkConcatenation(create(1, 2, 3, 4), create(3, 4));
	}
	
	@Test
	public void testConcat02() {
		checkConcatenation(create(1), create(2, 3, 4));
		checkConcatenation(create(1, 2, 3), create(4));
		checkConcatenation(create(1, 2), create(3));
		checkConcatenation(create(1), create(3, 4));
		checkConcatenation(create(1), create(2));
	}
	
	@Test
	public void testConcat03() {
		checkConcatenation(create(1), create());
		checkConcatenation(create(), create(1));
		checkConcatenation(create(), create());
	}
	
	static void checkConcatenation(LinkedList<Integer> l1, 
			LinkedList<Integer> l2) {
		System.out.println("----------");
		int size1 = l1.size;
		int size2 = l2.size;
		java.util.LinkedList<Integer> expected 
		= new java.util.LinkedList<Integer>(l1);
		expected.addAll(l2);
		System.out.println(l1);
		System.out.println("+");
		System.out.println(l2);
		System.out.println("=");
		l1.concatenate(l2);
		System.out.println(l1);
		Assert.assertEquals(size1 + size2, l1.size());
		Assert.assertTrue(l2.isEmpty());
		Assert.assertEquals(l2.size(), 0);
		for (int i = 0; i < expected.size(); ++i)
			Assert.assertEquals(expected.get(i), l1.get(i));
	}
	
	static LinkedList<Integer> create(Integer... arr) {
		return new LinkedList<Integer>(Arrays.asList(arr));
	}
}
