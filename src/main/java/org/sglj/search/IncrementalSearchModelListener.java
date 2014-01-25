/*
 * IncrementalSearchModelListener.java
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

import java.util.EventListener;

/**
 * Listener which listens for {@link IncrementalSearchEvent} event.<br>
 * Corresponding model can be accessed by 
 * {@link IncrementalSearchEvent#getModel()} method.
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public interface IncrementalSearchModelListener<T> extends EventListener {
	
	/**
	 * Informs that the result set has changes (entries were added or removed).
	 */
	void resultsChanged(IncrementalSearchEvent<T> e);
	
	/**
	 * Informs that new entries have been added to the result set.
	 */
	void resultsAdded(IncrementalSearchEvent<T> e);
	
	/**
	 * Informs that some entries have been removed from the result set.
	 */
	void resultsRemoved(IncrementalSearchEvent<T> e);
	
	/**
	 * Informs that next entry from result set was selected.
	 */
	void nextResult(IncrementalSearchEvent<T> e);
	
	/**
	 * Informs that previous entry from result set was selected.
	 */
	void previosResult(IncrementalSearchEvent<T> e);
	
}
