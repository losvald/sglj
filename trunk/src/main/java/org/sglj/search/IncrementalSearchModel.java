/*
 * IncrementalSearchModel.java
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

package org.sglj.search;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.event.EventListenerList;

import org.sglj.util.OccurrenceMap;
import org.sglj.util.PATTrie;
import org.sglj.util.SetUtils;


/**
 * Model which provides incremental search.
 * 
 * @author Leo Osvald
 * @version 0.99
 *
 * @param <E> type of data which is incrementally searched
 */

public abstract class IncrementalSearchModel<E> implements SortedSet<E>,
Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected PATTrie<E> trie;
	protected TreeSet<E> addedResults;
	protected TreeSet<E> removedResults;
	
	protected Comparator<? super E> resultComparator;
	
	/** mapa trenutnih rezultata po broju keyworda koji se matchaju*/
	private OccurrenceMap<E, TreeMap<E, Integer> > resultMap;
	
	private String lastPrefix;
	
	private E currResult;
	
	private TreeSet<E> searchSpace;
	
	private Collection<E> addedList;
	private Collection<E> removedList;
	
	private IncrementalSearchEvent<E> event;
	
	private EventListenerList listeners = new EventListenerList();
	
	public IncrementalSearchModel(Comparator<E> resultComparator) {
		this.resultComparator = resultComparator;
		resultMap = new OccurrenceMap<E, TreeMap<E,Integer> >(
				new TreeMap<E, Integer>(resultComparator));
		
		searchSpace = new TreeSet<E>(resultComparator);
		
		addedResults = new TreeSet<E>(resultComparator);
		removedResults = new TreeSet<E>(resultComparator);
				
		addedList = new ArrayList<E>();
		removedList = new ArrayList<E>();
		
		trie = new PATTrie<E>(addedList, removedList);
		resetFind();
	}
	
	public Set<E> getSearchSpace() {
		return searchSpace;
	}
	
	private void startSession() {
		addedResults.clear();
		removedResults.clear();
		addedList.clear();
		removedList.clear();
	}
	
	private boolean sessionedPut(E clazz) {
		String[] criteria = searchBy(clazz);
		boolean hasChanged = false;
		for(int i = 0; i < criteria.length; ++i)
			if(trie.put(criteria[i], clazz)) {
				if(lastPrefix != null && criteria[i].startsWith(lastPrefix)) {
					if(addedList != null) addedList.add(clazz);
				}
				hasChanged = true;
			}
		if(hasChanged) searchSpace.add(clazz);
		return hasChanged;
	}
	
	private boolean sessionedRemove(E clazz) {
//		if(!searchSpace.contains(clazz)) return false;
		String[] criteria = searchBy(clazz);
		boolean hasChanged = false;
		for(int i = 0; i < criteria.length; ++i)
			if(trie.remove(criteria[i], clazz)) {
				if(lastPrefix != null && criteria[i].startsWith(lastPrefix)) {
					if(removedList != null) removedList.add(clazz);
				}
				hasChanged = true;
			}
		if(hasChanged) searchSpace.remove(clazz);
		return hasChanged;
	}
	
	public boolean myRemoveAll(Collection<E> searchSpace) {
		startSession();
		boolean hasChanged = false;
		List<E> list = new ArrayList<E>(searchSpace);
		for(E curr : list)
			hasChanged |= sessionedRemove(curr);
		
		if(!removedList.isEmpty()) {
			removedResults.addAll(removedList);
			for(E curr : removedList)
				resultMap.decrement(curr);
			fireResultsRemoved();
			fireResultsChanged();
		}
		return hasChanged;
	}
	
	public boolean myRetainAll(Collection<E> searchSpace) {
		Set<E> toRemove = SetUtils.difference(this.searchSpace, 
				searchSpace, new TreeSet<E>(comparator()));
		
		//izbrisi ono sto vise ne postoji u novom search spaceu
		return removeAll(toRemove);
	}
	
	public boolean containsKey(String key) {
		return trie.getValues(key) != null;
	}
	
	
	@Deprecated
	public TreeSet<E> find(E clazz) throws IllegalStateException {
		throw new IllegalStateException("find is depracated");
	}
	
	public boolean continueFind(String s) {
		startSession();
		boolean wasContinued = trie.continueFindPrefix(s, lastPrefix, 
				addedList, removedList);
		if(wasContinued) {
			if(!addedList.isEmpty()) {
				for(E curr : addedList) {
					if(resultMap.increment(curr))
						addedResults.add(curr);
				}
				fireResultsAdded();
				fireResultsChanged();
			}
			else if(!removedList.isEmpty()) {
				for(E curr : removedList) {
					if(resultMap.decrement(curr))
						removedResults.remove(curr);
				}
				fireResultsRemoved();
				fireResultsChanged();
			}	
			System.out.println("Continued - Added: " + addedResults.size()
					+ "Removed: " + removedResults.size());
		}
		else {
			resultMap.getMap().clear();
			if(!addedList.isEmpty()) {
				for(E curr : addedList)
					resultMap.increment(curr);
			}
			System.out.println("new find: " + resultMap.getMap().size());
			fireResultsChanged();
		}
		//zapamti zadnji prefix
		lastPrefix = s;
		
		return wasContinued;
	}
	
	private void resetSearch() {
		lastPrefix = "";
		startSession();
	}
	
	public void resetFind() {
		resultMap.getMap().clear();
		resetSearch();
		ArrayList<E> allResults = new ArrayList<E>();
		trie.findPrefix("", allResults);
		for(E curr : allResults)
			resultMap.increment(curr);
		
		rewind();
		fireResultsChanged();
	}
	
	public Set<E> getResultSet() {
		return resultMap.getMap().keySet();
	}
	
	public TreeSet<E> getAddedResults() {
		return addedResults;
	}
	
	public TreeSet<E> getRemovedResults() {
		return removedResults;
	}
	
	public boolean next() {
		currResult = resultMap.getMap().higherKey(currResult);
		if(currResult != null) return true;
		currResult = resultMap.getMap().firstKey();
		return false;
	}
	
	public boolean previous() {
		currResult = resultMap.getMap().lowerKey(currResult);
		if(currResult != null) return true;
		currResult = resultMap.getMap().lastKey();
		return false;
	}
	
	public E getCurrent() {
		return currResult;
	}
	
	public void rewind() {
		if(resultMap.getMap().isEmpty()) currResult = null;
		else currResult = resultMap.getMap().firstKey();
	}
	
	public abstract String[] searchBy(E clazz);
	
	public void setResultComparator(Comparator<? super E> resultComparator) {
		this.resultComparator = resultComparator;
		addedResults = SetUtils.changeComparator(addedResults, resultComparator);
		removedResults = SetUtils.changeComparator(removedResults, resultComparator);
		
		OccurrenceMap<E, TreeMap<E,Integer> > nextResultMap 
		= new OccurrenceMap<E, TreeMap<E, Integer> >(
				new TreeMap<E, Integer>(resultComparator));
		nextResultMap.getMap().putAll(resultMap.getMap());
		resultMap.getMap().clear();
		resultMap = nextResultMap;
		
		fireResultsChanged();
	}

	public void reverseResultOrder() {
		setResultComparator(SetUtils.reverseComparator(
				resultMap.getMap().comparator()));
	}
	
	public void addIncrementalFinderEventListener(IncrementalSearchModelListener<E> l) {
		listeners.add(IncrementalSearchModelListener.class, l);
	}
	
	@SuppressWarnings("unchecked")
	public void fireResultsChanged() {
//		System.out.println("Results changed - count: " + resultMap.getMap().size());
		Object[] listeners = this.listeners.getListenerList();
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==IncrementalSearchModelListener.class) {
	             if (event == null)
	                 event = new IncrementalSearchEvent<E>(this);
	             event.setModel(this);
	             ((IncrementalSearchModelListener<E>)listeners[i+1])
	             .resultsChanged(event);
	         }
	     }
	}
	
	@SuppressWarnings("unchecked")
	public void fireResultsAdded() {
//		System.out.println("New results added");
//		for(T curr : addedResults) {
//			if(curr instanceof ProjectAccess) {
//				ProjectAccess pa = (ProjectAccess) curr;
//				System.out.println(pa.getName() + "@" + pa.getOwner());
//			}
//		}
		Object[] listeners = this.listeners.getListenerList();
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==IncrementalSearchModelListener.class) {
	             if (event == null)
	                 event = new IncrementalSearchEvent<E>(this);
	             event.setModel(this);
	             ((IncrementalSearchModelListener<E>)listeners[i+1])
	             .resultsAdded(event);
	         }
	     }
	}
	
	@SuppressWarnings("unchecked")
	public void fireResultsRemoved() {
		Object[] listeners = this.listeners.getListenerList();
	    for (int i = listeners.length-2; i>=0; i-=2) {
	         if (listeners[i]==IncrementalSearchModelListener.class) {
	             if (event == null)
	                 event = new IncrementalSearchEvent<E>(this);
	             event.setModel(this);
	             ((IncrementalSearchModelListener<E>)listeners[i+1])
	             .resultsRemoved(event);
	         }
	     }
	}
	
	public List<E> findByKey(String s) {
		List<E> list = new ArrayList<E>();
		trie.findPrefix(s, list);
		Collections.sort(list, resultComparator);
		return list;
	}
	
	public boolean putWithKey(String key, E value) {
		return trie.put(key, value);
	}
	
	public int nodeCount() {
		return trie.nodeCount();
	}

	@Override
	public E first() {
		return searchSpace.first();
	}

	@Override
	public SortedSet<E> headSet(E toElement) {
		return searchSpace.headSet(toElement);
	}

	@Override
	public E last() {
		return searchSpace.last();
	}

	@Override
	public SortedSet<E> subSet(E fromElement, E toElement) {
		return searchSpace.subSet(fromElement, toElement);
	}

	@Override
	public SortedSet<E> tailSet(E fromElement) {
		return searchSpace.tailSet(fromElement);
	}

	@Override
	public boolean add(E clazz) {
		startSession();
		boolean hasChanged = sessionedPut(clazz);
		if(!addedList.isEmpty()) {
			addedResults.addAll(addedList);
			for(E curr : addedList)
				resultMap.increment(curr);
			fireResultsAdded();
			fireResultsChanged();
		}
		return hasChanged;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean hasChanged = false;
		startSession();
		for(E curr : c)
			hasChanged |= sessionedPut(curr);
		
		if(!addedList.isEmpty()) {
			addedResults.addAll(addedList);
			for(E curr : addedList)
				resultMap.increment(curr);
			fireResultsAdded();
			fireResultsChanged();
		}
		return hasChanged;
	}

	@Override
	public void clear() {
		trie.clear();
		searchSpace.clear();
		addedResults.clear();
		removedResults.clear();
		fireResultsChanged();
	}
	
	@Override
	public Comparator<? super E> comparator() {
		return resultComparator;
	}
	
	@Override
	public boolean contains(Object clazz) {
		return searchSpace.contains(clazz);
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		return searchSpace.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return trie.isEmpty();
	}
	
	@Override
	public Iterator<E> iterator() {
		return searchSpace.iterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object clazz) {
		if(!searchSpace.contains(clazz)) return false;
		startSession();
		boolean hasChanged = sessionedRemove((E)clazz);
		if(!removedList.isEmpty()) {
			removedResults.addAll(removedList);
			for(E curr : removedList)
				resultMap.decrement(curr);
			fireResultsRemoved();
			fireResultsChanged();
		}
		return hasChanged;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean removeAll(Collection<?> c) {
		return myRemoveAll((Collection<E>)c);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean retainAll(Collection<?> c) {
		return myRetainAll((Collection<E>)c);
	}

	@Override
	public int size() {
		return trie.size();
	}
	
	@Override
	public Object[] toArray() {
		return searchSpace.toArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		return (T[])searchSpace.toArray();
	}
	
}
