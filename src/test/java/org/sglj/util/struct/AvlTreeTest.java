/*
 * AvlTreeTest.java
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.sglj.util.struct.AvlTree.AvlNode;
import org.sglj.util.struct.AvlTree.InsertResult;
import org.sglj.util.struct.AvlTree.RemoveResult;


public class AvlTreeTest {
	static class Node extends AvlNode<Integer> {
		Integer key;
		int size;
		int heightMin;
		int heightMax;

		public Node(Integer key) {
			this.key = key;
			size = 1;
			heightMin = heightMax = 0;
		}

		@Override
		public Node mergeCodomains(AvlNode<Integer> a, AvlNode<Integer> b) {
			size = 1 + (a != null ? ((Node)a).size : 0) +
					(b != null ? ((Node)b).size : 0);
			heightMin = 1 + Math.min(a != null ? ((Node)a).heightMin : -1,
					b != null ? ((Node)b).heightMin : -1);
			heightMax = 1 + Math.max(a != null ? ((Node)a).heightMax : -1,
					b != null ? ((Node)b).heightMax : -1);
			return this;
		}

		@Override
		public Integer getKey() {
			return key;
		}

		@Override
		public String toString() {
			return "[" + key + "]";
		}

		Node l() {
			return (Node)left;
		}

		Node r() {
			return (Node)right;
		}
	}

	static class NonAugmentedAvlTreeTest {
	}

	static abstract class AbstractVerifiedAvlTree extends AvlTree<Integer> {
		public AbstractVerifiedAvlTree() {
			super(new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			});
		}
	}

	static class VerifiedAvlTree extends AbstractVerifiedAvlTree {
		@Override
		protected Node createNode(Integer key) {
			return new Node(key);
		}

		@Override
		public Node getRoot() {
			return (Node)super.getRoot();
		}

		@Override
		public boolean add(Integer e) {
			int oldHeight = getHeight();
			boolean ret = super.add(e);
			int heightDiff = getHeight() - oldHeight;
			if (heightDiff > 0)
				assertEquals(1, heightDiff);
			else if (heightDiff < -1)
				fail("Insertion cannot decrease height by >1");
			assertHeightInvariant();
			return ret;
		}

		@Override
		public boolean remove(Object o) {
			int oldHeight = getHeight();
			boolean ret = super.remove(o);
			int heightDiff = getHeight() - oldHeight;
			if (heightDiff < 0)
				assertEquals(-1, heightDiff);
			else if (heightDiff > 1)
				fail("Removal cannot increase height by >1");
			assertHeightInvariant();
			return ret;
		}

		int getHeight() {
			return getRoot() != null ? getRoot().heightMax : -1;
		}

		private void assertHeightInvariant() {
			Node root = getRoot();
			if (root == null)
				return;

			assertHeightInvariant(root);

			// http://en.wikipedia.org/wiki/AVL_tree#Comparison_to_other_structures
			assertTrue(Math.pow(2.0, (root.heightMax + 0.328) / 1.45) - 2 <
					root.size);
		}

		private static void assertHeightInvariant(Node node) {
			if (node == null)
				return;

			int heightDiff = Math.abs(
					(node.r() != null ? node.r().heightMax : 0) -
					(node.l() != null ? node.l().heightMax : 0));
			if (heightDiff != 0)
				assertEquals(1, heightDiff);
			assertHeightInvariant(node.l());
			assertHeightInvariant(node.r());
		}
	}

	static <E> String repr(AvlTree<E> t) {
		StringBuilder sb = new StringBuilder();
		repr(t.getRoot(), sb);
		return sb.toString();
	}

	private static void repr(AvlNode<?> node, StringBuilder sb) {
		sb.append('(');
		if (node != null) {
			sb.append(node);
			sb.append(" <");
			repr(node.left, sb);
			sb.append(" >");
			repr(node.right, sb);
		}
		sb.append(')');
	}

	static <E> void assertRepr(AvlTree<E> t, String expected) {
		assertEquals(expected, repr(t));
	}

	@Test
	public void testEmpty() {
		VerifiedAvlTree vat = new VerifiedAvlTree();
		assertTrue(vat.isEmpty());
		assertEquals(0, vat.size());
		assertEquals("()", repr(vat));

		Iterator<Integer> it = vat.iterator();
		assertFalse(it.hasNext());

		RemoveResult<Integer> rr = new RemoveResult<Integer>();
		rr.value = 17;
		vat.remove(42, rr);
		assertNull(rr.removed);
		assertEquals(0, rr.value);
	}

	@Test
	public void testInsertRemoveRoot() {
		VerifiedAvlTree vat = new VerifiedAvlTree();

		InsertResult<Integer> ir = new InsertResult<Integer>();
		ir.value = 17;
		vat.insert(42, ir);
		assertFalse(vat.isEmpty());
		assertEquals(1, vat.size());
		assertEquals((Integer)42, ir.inserted.getKey());
		Iterator<Integer> it = vat.iterator();
		assertTrue(it.hasNext());
		assertEquals((Integer)42, it.next());
		assertFalse(it.hasNext());
		assertRepr(vat, "([42] <() >())");

		RemoveResult<Integer> rr = new RemoveResult<Integer>();
		rr.value = 17;
		vat.remove(42, rr);
		assertNotNull(rr.removed);
		assertEquals((Integer)42, rr.removed.getKey());
		assertEquals(1, rr.value);

		vat.add(13);
		assertEquals(1, vat.size());
		assertEquals((Integer)13, vat.iterator().next());

		vat.remove(13);
		assertEquals(0, vat.size());
		assertFalse(vat.iterator().hasNext());
	}

	@Test
	public void testInsertNoRebalance() {
		VerifiedAvlTree vat = new VerifiedAvlTree();

		vat.add(50);
		vat.add(20);
		assertRepr(vat, "([50] <([20] <() >()) >())");
		assertEquals(2, vat.getRoot().size);
		assertEquals(0, vat.getRoot().heightMin);
		assertEquals(1, vat.getRoot().heightMax);

		vat.add(80);
		assertRepr(vat, "([50] <([20] <() >()) >([80] <() >()))");
		assertEquals(1, vat.getRoot().heightMin);
		assertEquals(1, vat.getRoot().heightMax);

		vat.add(25);
		assertRepr(vat, "([50] <([20] <() >([25] <() >())) >([80] <() >()))");
		assertEquals(2, vat.getRoot().heightMax);
		assertEquals(2, vat.getRoot().l().size);
	}

	@Test
	public void testRemoveNoRebalance() {
		VerifiedAvlTree vat = new VerifiedAvlTree();

		// remove a root with only the right child
		vat.add(40);
		vat.add(60);
		vat.remove(40);
		assertRepr(vat, "([60] <() >())");
		assertEquals(1, vat.getRoot().size);

		// remove a node with only the left child
		vat.add(80);
		vat.add(41);
		vat.add(30);
		assertEquals(2, vat.getRoot().heightMax);
		vat.remove(41);
		assertRepr(vat, "([60] <([30] <() >()) >([80] <() >()))");
		assertEquals(3, vat.getRoot().size);
		assertEquals(1, vat.getRoot().heightMin);
		assertEquals(1, vat.getRoot().heightMax);

		// remove a node with 2 children whose successor is right-left grandchild
		vat.add(20);
		vat.add(51);
		vat.add(90);
		vat.add(70);
		vat.add(31);
		assertRepr(vat, "([60]" +
				" <([30] <([20] <() >()) >([51] <([31] <() >()) >()))" +
				" >([80] <([70] <() >()) >([90] <() >())))");
		assertEquals(4, vat.getRoot().l().size);
		assertEquals(0, vat.getRoot().l().r().heightMin);
		vat.remove(30);
		assertRepr(vat, "([60]" +
				" <([31] <([20] <() >()) >([51] <() >()))" +
				" >([80] <([70] <() >()) >([90] <() >())))");
		assertEquals(1, vat.getRoot().l().r().size);
		assertEquals(1, vat.getRoot().l().heightMin);
		assertEquals(1, vat.getRoot().l().heightMax);
		assertEquals(1, vat.getRoot().r().heightMin);
		assertEquals(1, vat.getRoot().r().heightMax);
		assertEquals(3, vat.getRoot().l().size);
		assertEquals(7, vat.getRoot().size);

		// remove a leaf node which is a left child
		vat.remove(70);
		assertRepr(vat, "([60]" +
				" <([31] <([20] <() >()) >([51] <() >()))" +
				" >([80] <() >([90] <() >())))");
		assertEquals(0, vat.getRoot().r().heightMin);
		assertEquals(1, vat.getRoot().r().heightMax);
		assertEquals(2, vat.getRoot().r().size);

		// remove a node with 2 children whose successor is right child
		assertEquals(3, vat.getRoot().l().size);
		vat.remove(31);
		assertRepr(vat, "([60]" +
				" <([51] <([20] <() >()) >())" +
				" >([80] <() >([90] <() >())))");
		assertEquals(2, vat.getRoot().l().size);
		assertEquals(0, vat.getRoot().l().heightMin);
		assertEquals(1, vat.getRoot().l().heightMax);
		assertEquals(1, vat.getRoot().heightMin);

		// remove a node with only the right child
		assertEquals(1, vat.getRoot().r().r().size);
		vat.remove(80);
		assertRepr(vat, "([60]" +
				" <([51] <([20] <() >()) >())" +
				" >([90] <() >()))");
		assertEquals(1, vat.getRoot().r().size);
		assertEquals(0, vat.getRoot().r().heightMax);

		// remove a leaf node which is a right child
		vat.remove(51);
		assertRepr(vat, "([60] <([20] <() >()) >([90] <() >()))");
		assertEquals(1, vat.getRoot().heightMin);

		// remove the root with 2 children
		vat.remove(60);
		assertRepr(vat, "([90] <([20] <() >()) >())");
		assertEquals(0, vat.getRoot().heightMin);
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

	static List<int[]> permute(int maxN) {
		List<int[]> perms = new ArrayList<int[]>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= maxN; ++i) {
			sb.append((char)('0' + i));
			permute("", sb.toString(), perms);
		}
		return perms;
	}

	@Test
	public void testRotateLeft() {
		VerifiedAvlTree vat = new VerifiedAvlTree();
		for (int key : new int[]{1, 2, 3})
			vat.add(key);
		assertRepr(vat, "([2] <([1] <() >()) >([3] <() >()))");

		vat.add(4);
		vat.add(5);
		assertRepr(vat, "([2] <([1] <() >()) >([4] <([3] <() >()) >([5] <() >())))");

		vat.add(6);
		assertRepr(vat, "([4] <([2] <([1] <() >()) >([3] <() >())) >([5] <() >([6] <() >())))");
	}

	@Test
	public void testRotateRight() {
		VerifiedAvlTree vat = new VerifiedAvlTree();
		for (int key : new int[]{-1, -2, -3})
			vat.add(key);
		assertRepr(vat, "([-2] <([-3] <() >()) >([-1] <() >()))");

		vat.add(-4);
		vat.add(-5);
		assertRepr(vat, "([-2] <([-4] <([-5] <() >()) >([-3] <() >())) >([-1] <() >()))");

		vat.add(-6);
		assertRepr(vat, "([-4] <([-5] <([-6] <() >()) >()) >([-2] <([-3] <() >()) >([-1] <() >())))");
	}

	@Test
	public void testRotateLeftRight() {
		VerifiedAvlTree vat = new VerifiedAvlTree();
		for (int key : new int[]{2, 8, 5})
			vat.add(key);
		assertRepr(vat, "([5] <([2] <() >()) >([8] <() >()))");

		for (int key : new int[]{1, 4, 6, 9, 3})
			vat.add(key);
		assertRepr(vat, "([5] <([2] <([1] <() >()) >([4] <([3] <() >()) >())) >([8] <([6] <() >()) >([9] <() >())))");
		vat.remove(1);
		assertRepr(vat, "([5] <([3] <([2] <() >()) >([4] <() >())) >([8] <([6] <() >()) >([9] <() >())))");
	}

	@Test
	public void testRotateRightLeft() {
		VerifiedAvlTree vat = new VerifiedAvlTree();
		for (int key : new int[]{8, 4, 2})
			vat.add(key);
		assertRepr(vat, "([4] <([2] <() >()) >([8] <() >()))");

		vat.add(6);
		vat.add(3);
		vat.add(1);
		vat.add(9);
		vat.add(7);
		assertRepr(vat, "([4] <([2] <([1] <() >()) >([3] <() >())) >([8] <([6] <() >([7] <() >())) >([9] <() >())))");
		vat.remove(9);
		assertRepr(vat, "([4] <([2] <([1] <() >()) >([3] <() >())) >([7] <([6] <() >()) >([8] <() >())))");
	}

	@Test
	public void testInsertAllPermutations() {
		for (int[] keys : permute(7)) {
			VerifiedAvlTree vat = new VerifiedAvlTree();
			for (int key : keys)
				vat.add(key);

		}
	}

	@Test
	public void testRemoveAllPermutations() {
		for (int[] keys : permute(7)) {
			for (int toRemove : keys) {
				VerifiedAvlTree vat = new VerifiedAvlTree();
				for (int key : keys)
					vat.add(key);
				vat.remove(toRemove);
			}
		}
	}
}

