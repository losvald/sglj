/*
 * RandomPriorityScheduler.java
 * 
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
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
import java.util.Random;

/**
 * <p>Implementation of {@link PriorityScheduler} interface
 * which mimic the model of discrete random variable, whose
 * values are of type <T>.<br>
 * Priorities determine the probability of the certain outcome,
 * that is, the probability that a certain value will be chosen.
 * The greater the priority the greater the probability that
 * the certain value will be chosen (returned by {@link #get()} method).</p> 
 * <p>Method {@link #get()} is implemented efficiently and 
 * takes logarithmic time complexity, unlike other methods which
 * takes time linear to the number of outcomes ("tasks").<br>
 * </p>
 * 
 * @author Leo Osvald
 * @version 0.9
 * @param <T> type of outcome ("task")
 */
public class RandomPriorityScheduler<T> implements PriorityScheduler<T> {
	
	private final ArrayList<Integer> sum = new ArrayList<Integer>();
	private final ArrayList<T> tasks = new ArrayList<T>();
	private final Random random = new Random();
	
	/**
	 * Gets the outcome which occurred by calling this method.
	 * @return outcome
	 */
	@Override
	public T get() {
		if(sum.isEmpty())
			return null;
		int totalSum = sum.get(sum.size()-1);
		int target = random.nextInt(totalSum);
		int lo = 0, hi = sum.size()-1;
		while(lo < hi) {
			int mid = lo + (hi-lo-1)/2;
			if(target < sum.get(mid))
				hi = mid;
			else
				lo = mid+1;
		}
		return tasks.get(lo);
	}
	
	
	@Override
	public void add(T task, short priority) {
		int s = relativePriority(priority) 
		+ (!sum.isEmpty() ? sum.get(sum.size()-1) : 0);
		tasks.add(task);
		sum.add(s);
	}
	
	@Override
	public short getPriority(T task) {
		int ind = tasks.indexOf(task);
		if(ind != -1) {
			return absolutePriority(relativePriorityAt(ind));
		}
		return NORMAL_PRIORITY;
	}
	
	@Override
	public void remove(T task) {
		int ind = tasks.indexOf(task);
		if(ind != -1) {
			int d = relativePriorityAt(ind);
			sum.remove(ind);
			tasks.remove(ind);
			fix(ind, -d);
		}
	}
	@Override
	public void setPriority(T task, short priority) {
		int ind = tasks.indexOf(task);
		if(ind != -1) {
			fix(ind, relativePriority(priority) - relativePriorityAt(ind));
		}
	}
	
	/**
	 * Returns the probability that this outcome will occur.
	 * @param outcome outcome ("task")
	 * @return probability (real number from range [0, 1])
	 */
	public double getProbability(T outcome) {
		int ind = tasks.indexOf(outcome);
		if(ind == -1)
			return 0;
		return (double)relativePriorityAt(ind)/sum.get(sum.size()-1);
	}
	
	protected int relativePriority(short priority) {
		return (int)-priority - Short.MIN_VALUE;  
	}
	
	protected short absolutePriority(int relativePriority) {
		return (short) -(relativePriority + Short.MIN_VALUE);
	}
	
	private void fix(int from, int sumDiff) {
		for(int i = from; i < sum.size(); ++i)
			sum.set(i, sum.get(i)+sumDiff);
	}
	
	private int relativePriorityAt(int index) {
		return sum.get(index) 
		- (index > 0 ? sum.get(index-1) : 0);
	}
	
}
