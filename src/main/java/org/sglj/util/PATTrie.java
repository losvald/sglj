/*
 * PATTrie.java
 * 
 * Copyright (C) 2009 Leo Osvald <leo.osvald@gmail.com>
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

package org.sglj.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

/**
 * <p>Implementation of PAT trie.<br>
 * A description of this data structure can be found here:<br>
 * <pre>http://en.wikipedia.org/wiki/Suffix_tree</pre>
 * <br>
 * This implementation can be used as a multimap; it can associate multiple
 * different values with a key which is actually a node which correspond
 * to some string.</p>
 * <p>The implementation is quite efficient and all operations take
 * time proportional to the key length, which is some small constant.<br>
 * Insertion and removal is done in linear time which is proportional to
 * the size of the elements removed/inserted.
 * Prefix query takes linear time proportional to the size of the result set
 * matching that query.</p> 
 * 
 * @author Leo Osvald
 *
 * @param <E> type of the elements which are stored
 * 
 * @version 0.71
 */
public class PATTrie<E> {

	protected Node root;
	
	protected static final boolean REMOVE = false;
	protected static final boolean ADD = true;

	public PATTrie(final Collection<E> added, final Collection<E> removed) {
		root = new Node();
	}
	
	public PATTrie() {
		this(null, null);
	}
	
	private int getDifferenceIndex(String a, String b) {
		int diffInd = 0;
		while(diffInd < a.length() && diffInd < b.length() 
		&& a.charAt(diffInd) == b.charAt(diffInd)) 
			++diffInd;
		return diffInd;
	}
	
	private boolean insert(final String key, final E value, int ind, Node node) {
		if(ind >= key.length()) {
			if(node.addData(value)) {
				++node.prefixCount;
				return true;
			}
			return false;
		}
		
		String s = key.substring(ind);
		int splitInd = 1;
		
		Node subtree = node.getChild(key.charAt(ind));
		if(subtree == null) {
			subtree = new Node(node, s);
			splitInd = s.length();
		}
		else {
			//inace, pogledaj gdje treba rasdvojiti
			splitInd = getDifferenceIndex(s, subtree.edge);
			if(splitInd < subtree.edge.length()) {
				subtree = subtree.splitEdge(splitInd);
			}
		}
		
//		System.out.println("->" + subtree.edge.substring(0, splitInd)
//				+ "\t" + subtree.prefixCount);
		boolean ret = insert(key, value, ind+splitInd, subtree);
		
		if(ret) {
			++node.prefixCount;
		}
		
		return ret;
	}
	
	/**
	 * Associates specified value with the specified key, forming a new
	 * entry if it was not already associated.
	 * @param key key
	 * @param value value
	 * @return <code>true</code> if entry was inserted, 
	 * <code>false</code> otherwise.
	 */
	public boolean put(final String key, final E value) {
		return insert(key, value, 0, root);
	}
	
	private Node getNode(final String key, int ind, final Node node) {
		if(ind >= key.length())
			return node;
		
		Node subtree = node.getChild(key.charAt(ind));
		if(subtree == null) return null;
		
		String s = key.substring(ind);
		if(!s.startsWith(subtree.edge)) return null;
		
		return getNode(key, ind+subtree.edge.length(), subtree);
	}
	
	/**
	 * Returns collection of values associated with the specified key.<br>
	 * If there are no associated values, an immutable empty collection
	 * will be returned.<br>
	 * @param key key
	 * @return collection of values mapped by this key
	 */
	public Collection<E> getValues(final String key) {
		Node node = getNode(key, 0, root);
		if(node.isEmpty()) return Collections.emptyList();
		return new ArrayList<E>(node.data);
	}
	
	/**
	 * Checks whether the specified value is associated with
	 * the specified key.
	 * @param key key
	 * @param value value
	 * @return <code>true</code> if it is, <code>false</code> otherwise.
	 */
	public boolean contains(final String key, final E value) {
		return getNode(key, 0, root).containsData(value);
	}
	
	private Node getNodeMatchingPrefix(final String prefix, int ind, Node node) {
		if(ind >= prefix.length())
			return node;
		
		Node subtree = node.getChild(prefix.charAt(ind));
		if(subtree == null) return null;
		
		String s = prefix.substring(ind);
		int diffInd = getDifferenceIndex(subtree.edge, s);
		//if one is not the prefix of another one, there are no results
		if(diffInd < s.length() && diffInd < subtree.edge.length()) return null;
		
//		System.out.println("->" + subtree.edge + "\t" + subtree.prefixCount);
		return getNodeMatchingPrefix(prefix, ind+diffInd, subtree);
	}
	
	private boolean findPrefix(final String prefix, int ind, Node node,
			final Collection<E> added) {
		if(ind >= prefix.length()) {
//			System.out.println("[FIND] found subtree: ");
			return updateCollection(added, node);
		}
		
		Node subtree = node.getChild(prefix.charAt(ind));
		if(subtree == null) return false;
		
		String s = prefix.substring(ind);
		int diffInd = getDifferenceIndex(subtree.edge, s);
		//if one is not the prefix of another one, there are no results
		if(diffInd < s.length() && diffInd < subtree.edge.length()) return false;
			
		return findPrefix(prefix, ind+diffInd, subtree, added);
	}
	
	/**
	 * Retrieves all values mapped by the keys whose prefix is the one
	 * specified.
	 * @param prefix key prefix
	 * @param result collection where retrieved values should be added
	 * @return collection to which retrieved values should be added
	 */
	public boolean findPrefix(final String prefix, Collection<E> result) {
		return findPrefix(prefix, 0, root, result);
	}
	
	private boolean remove(final String key, final E value, int ind, Node node) {
		if(ind >= key.length()) {
			if(node.removeData(value)) {
				if(node.isEmpty()) {
					if(node.hasSingleChild()) {
						node.getOnlyChild().mergeWithParent();
					}
					else if(node.isLeaf()) {
//						System.out.println("Removing subtree: " + node.edge);
						node.destroy();
					}
				}
				else --node.prefixCount;
				
				return true;
			}
			return false;
		}
		
		
		Node subtree = node.getChild(key.charAt(ind));
		if(subtree == null) return false;
		
		String s = key.substring(ind);
		int diffInd = getDifferenceIndex(subtree.edge, s);
		//if one is not the prefix of another one, there are no results
		if(diffInd < s.length() && diffInd < subtree.edge.length()) return false;
		
//		System.out.println("->" + subtree.edge + "\t" + subtree.prefixCount);
		boolean ret = remove(key, value, ind+diffInd, subtree);
		
		if(ret) {
//			System.out.println("UP: " + node.edge);
			--node.prefixCount;
			if(node != root && node.isEmpty() && node.hasSingleChild())
				node.getOnlyChild().mergeWithParent();
		}
		
		return ret;
	}
	
	/**
	 * Removes the specified value associated with the specified key
	 * (if this entry exists).
	 * @param key key
	 * @param value value
	 * @return <code>true</code> if value was removed, <code>false</code>
	 * otherwise.
	 */
	public boolean remove(final String key, final E value) {
		return remove(key, value, 0, root);
	}
	
	private void clear(final Node node) {
		if(node.next != null) {
			Collection<Node> children = node.next.values();
			for(Node subtree : children)
				clear(subtree);
		}
		
		node.purgeNext();
		node.purgeData();
	}
	
	/**
	 * Removes all key-value pairs from this collection.<br>
	 * In addition, resets search state regarding search by prefix
	 * (so that further calls to 
	 * {@link #continueFindPrefix(String, String, Collection, Collection)}
	 * works properly).
	 */
	public void clear() {
		clear(root);
		nodeCount = 0;
		root = new Node();
	}
	
	private boolean updateCollection(final Collection<E> addedOrRemoved, 
			final Node node) {
		
		if(node == null) return false;
		boolean ret = false;
		
		if(node.data != null) {
			if(addedOrRemoved != null)
				ret |= addedOrRemoved.addAll(node.data);
		}
		
		if(node.next != null)
			for(Node subtree : node.next.values())
				ret |= updateCollection(addedOrRemoved, subtree);
		
		return ret;
	}
	
	private boolean traverse(final Node lower, final Node higher, 
			final boolean addOrRemove, final Collection<E> addedOrRemoved) {
		boolean ret = false;
		for(Node curr = lower; curr != higher && curr != root; curr = curr.parent) {
			//add/remove all from other subtrees
			for(Node sibling : curr.parent.next.values()) {
				if(sibling != curr) {
//					int oldCnt = (result != null ? result.size() : 0);
					ret |= updateCollection(addedOrRemoved, sibling);					
//					int nowCnt = (result != null ? result.size() : 0);
//					System.out.println((addOrRemove == ADD ? "[ASCEND] added " 
//							: "[DESCEND] removed ")
//							+ (nowCnt-oldCnt) + "\t subtree: " + sibling.edge);
				}
			}
			if(curr.parent.data != null) {
				if(addedOrRemoved != null)
					ret |= addedOrRemoved.addAll(curr.parent.data);
			}
//			System.out.println((addOrRemove == ADD ? "-----UP-----" 
//					: "----DOWN----"));
		}
		
		return ret;
	}
	
	private boolean ascend(final Node from, final Node to, final Collection<E> added) {
		return traverse(from, to, ADD, added);
	}

	private boolean descend(final Node from, final Node to, final Collection<E> removed) {
		return traverse(to, from, REMOVE, removed);
	}
	
	/**
	 * 
	 * <p>Continues searching by prefix.<br>
	 * Collections <code>added</code> and <code>removed</code> are cleared 
	 * and one of the following occurs: 
	 * <ul>
	 * <li>a) if <code>currPrefix</code> matches more keys than 
	 * <code>lastPrefix</code>, values mapped by these matched keys
	 * are added to <code>added</code> collection</li>
	 * <li>b) if <code>currPrefix</code> matches less keys than 
	 * <code>lastPrefix</code>,  values mapped by these matched keys
	 * which <code>currPrefix</code> does not match
	 * (but <code>lastPrefix</code> did match) will be added to  
	 * <code>removed</code> collection
	 * <li>c) if the set of keys that match <code>currPrefix</code>
	 * and the set of keys that match <code>lastPrefix</code> are disjunctive
	 * (that is, no key is matched by both prefixes), 
	 * values mapped by newly matched keys are added to 
	 * <code>added</code> collection.
	 * </ul></p>
	 * <p>The time complexity of this operation is linear to
	 * the number of values added to <code>added</code> and removed
	 * from <code>removed</code> collection.</p>
	 * @param currPrefix current prefix that is searched by
	 * @param lastPrefix last prefix that was searched by
	 * @param added collection
	 * @param removed collection
	 * @return <code>true</code>if results are non-disjunctive (case a or b),
	 * <code>false</code> if this is not the case (which means
	 * that continuation of the search has not succeeded) (case c).
	 */
	public boolean continueFindPrefix(String currPrefix, final String lastPrefix,
			Collection<E> added, Collection<E> removed) {
		if(added != null) added.clear();
		if(removed != null) removed.clear();
		
		//rubni slucajevi
		if(currPrefix == null) currPrefix = "";
		
		Node currNode = getNodeMatchingPrefix(currPrefix, 0, root);
		if(currNode == null) return false;
		
		Node lastNode = getNodeMatchingPrefix(lastPrefix, 0, root);
		if(lastNode == null) {
//			System.out.println("[lastNode == null]");
			findPrefix(currPrefix, 0, root, added);
			return false;
		}
		
		//trivial cases (currNode != null && lastNode != null)
		
//		System.out.println("lastNode data: " + (lastNode == root ? "(root)" : 
//			(lastNode == null ? "(lastNode == null)" : lastNode.data)));
//		System.out.println("currNode data: " + (currNode == root ? "(root)" : 
//			(currNode == null ? "(currNode == null)" : currNode.data)));
		
		boolean isDescendant = currPrefix.startsWith(lastPrefix);
		boolean	isAncestor = lastPrefix.startsWith(currPrefix);
		
		//if current and last node represent the same subtree, no changes
		if(lastNode == currNode) 
			return true;
		
		boolean ret;
		
		//if they are disjunctive, return a new set
		if(!isDescendant && !isAncestor) {
			findPrefix(currPrefix, 0, root, added);
			ret = false;
		}
		//if this node is a descendant, descend and remove results
		//from other subtrees on the route
		else if(isDescendant) {
			System.out.println("DESCENDANT: " + currPrefix.substring(lastPrefix.length()));
			descend(lastNode, currNode, removed);
			ret = true;
		}
		//if this is an ancestor, ascend and add results from the subtrees
		//on the route
		else {
			System.out.println("ASCENDANT");
			ascend(lastNode, currNode, added);
			ret = true;
		}
		
		return ret;
	}
	
	/**
	 * Returns the number of values contained by this collection.
	 * This value is greater than or equal to the one 
	 * returned by {@link #nodeCount()} method, as several
	 * values can be mapped by the same key.
	 * @return total number of values
	 */
	public int size() {
		return root.prefixCount;
	}
	
	/**
	 * Checks whether this collection is empty, that is, whether
	 * it contains at least one key-value entry.
	 * @return <code>true</code> if it is empty, <code>false</code> otherwise.
	 */
	public boolean isEmpty() {
		return root.prefixCount == 0;
	}
	
	/**
	 * Returns the number of nodes in the trie.<br>
	 * This method is equivalent to the {@link #keyCount()} method.
	 * @return number of nodes in the trie
	 * @see Node
	 */
	public int nodeCount() {
		return nodeCount;
	}
	
	/**
	 * Returns the total number of keys.
	 * This method is equivalent to the {@link #nodeCount()} method.
	 * @return number of keys in this collection
	 */
	public int keyCount() {
		return nodeCount();
	}
	
	protected int nodeCount = 0;

	/**
	 * Node of the trie.
	 * 
	 * @author Leo Osvald
	 *
	 */
	private class Node {
		private Set<E> data;
		private TreeMap<Character, Node> next;
		private Node parent;
		private String edge;
		private int prefixCount;
		
		private static final int INIT_DATA_CAPACITY = 2;
		
		public Node() { }
		
		/**
		 * Creates node which is doubly-linked with its parent node.
		 * @param parent parent node
		 * @param edgeFromParent edge from parent node
		 */
		public Node(Node parent, String edgeFromParent) {
			++nodeCount;
			parent.link(edgeFromParent, this);
			prefixCount = 0;
		}
		
		/**
		 * Creates double link to its future child.
		 * @param edge edge which represent the link
		 * @param child child node which should be created and linked
		 */
		public void link(String edge, Node child) {
			if(next == null) next = new TreeMap<Character, Node>();
			next.put(edge.charAt(0), child);
			child.parent = this;
			child.edge = edge;
		}
		
		/**
		 * Destroys double link to its parent and links to its children
		 * (but not from its children).
		 */
		public void destroy() {
			// TODO maybe links from children to this node should be destroyed??
//			System.out.println("Destroy: " + edge);
			--nodeCount;
			if(this.parent != null) {
				this.parent.next.remove(edge.charAt(0));
				if(this.parent.next.isEmpty()) {
					this.parent.purgeNext();
				}
				this.parent = null;
			}
			edge = null;
			//
			//let GC deallocate memory
			purgeNext();
			purgeData();
		}
		
		/**
		 * Splits edge which connects this node to its parent, creating
		 * a new node in between, which is then returned.
		 * @param splitInd index at which the edge should be split
		 * @return node new node that was created
		 */
		public Node splitEdge(int splitInd) {
			//napravi link s parentom->splitNode
			parent.next.remove(edge.charAt(0));
			Node splitNode = new Node(parent, edge.substring(0, splitInd));
			//napravi link splitNode -> this
			splitNode.prefixCount = prefixCount;
			splitNode.link(edge.substring(splitInd), this);
			
//			System.out.println("Splitnode(" + splitNode.edge
//					+ ")\tchild(" + edge + ")");
			
			
			return splitNode;
		}
		
		/**
		 * Merges this node with its parent. 
		 */
		public void mergeWithParent() {
//			System.out.println("Merge(" + parent.edge + "+" + edge + ")");
			addData(parent.data);
			//prespoji edgeve (izbrisi parenta i spoji ovog na grandparenta)
			Node grandParent = parent.parent;
			String edgeToParent = parent.edge;
			parent.destroy();
			parent = null; //TODO UNTESTED
			grandParent.link(edgeToParent + edge, this);
		}
		
		/**
		 * Checks whether the node contains values.
		 * @return <code>true</code> if it contains at least one value, 
		 * <code>false</code> otherwise.
		 */
		public boolean isEmpty() {
			return data == null || data.isEmpty();
		}
		
		/**
		 * Checks whether the node is a leaf node.
		 * @return <code>true</code>if it is leaf but not the root,  
		 * <code>false</code> otherwise.
		 */
		public boolean isLeaf() {
			return this != root && (next == null || next.isEmpty());
		}
		
		/**
		 * Checks whether the node has only one child.
		 * @return <code>true</code> if this is the case, 
		 * <code>false</code> otherwise.
		 */
		public boolean hasSingleChild() {
			return next != null && next.size() == 1;
		}
		
		/**
		 * Returns the only child of the node.
		 * @return node child node, or  
		 * <code>null</code> if the node has no children.
		 */
		public Node getOnlyChild() {
			return next.firstEntry().getValue();
		}
		
		public Node getChild(Character c) {
			//TODO Character -> String
			return next == null ? null : next.get(c);
		}
		
		/**
		 * Adds value to the node, if it is not already
		 * contained in the node.
		 * @param data value that should be added
		 * @return <code>true</code> if the value is added,
		 * <code>false</code> otherwise.
		 */
		public boolean addData(E data) {
			if(data == null) return false;
			createDataIfNeeded();
			return this.data.add(data);
		}
		
		/**
		 * Adds all values from the specified collection.
		 * @param data values to add
		 * @return <code>true</code> if at least one value was added,
		 * <code>false</code> if none was added (in other words, if all
		 * were already contained).
		 */
		public boolean addData(Collection<? extends E> data) {
			if(data == null) return false;
			createDataIfNeeded();
			return this.data.addAll(data);
		}
		
		/**
		 * Removes the specified value (if it exists).
		 * @param data the value that should be removed
		 * @return <code>true</code> if the value was removed,
		 * <code>false</code> otherwise.
		 */
		public boolean removeData(Object data) {
			if(this.data == null) return false;
			boolean ret = this.data.remove(data);
			if(this.data.isEmpty()) this.data = null;
			return ret;
		}
		
		/**
		 * Removes all values that are contained in the specified collection.
		 * @param data values to be removed
		 * @return <code>true</code> if at least one value was removed,
		 * <code>false</code> otherwise.
		 */
		@SuppressWarnings("unused")
		public boolean removeAllData(Collection<?> data) {
			if(this.data == null) return false;
			boolean ret = this.data.removeAll(data);
			if(this.data.isEmpty()) this.data = null;
			return ret;
		}
		
		/**
		 * Checks whether the specified value is contained.
		 * @param data value
		 * @return <code>true</code> if it is,
		 * <code>false</code> otherwise.
		 */
		public boolean containsData(Object data) {
			if(isEmpty()) return false;
			return this.data.contains(data);
		}		
		
		/**
		 * Initialization of collection which holds values.
		 */
		public void createDataIfNeeded() {
			if(this.data == null) this.data = new HashSet<E>(INIT_DATA_CAPACITY);
		}
		
		/**
		 * Destruction of the collection which holds values - saves memory. 
		 */
		public void purgeData() {
			if(this.data != null) {
				this.data.clear();
				this.data = null;
			}
		}
		
		/**
		 * Removes all links to children (but not from children).
		 */
		public void purgeNext() {
			if(this.next != null) {
				this.next.clear();
				this.next = null;
			}
		}
		
	}
}
