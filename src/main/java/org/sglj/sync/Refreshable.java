/*
 * Refreshable.java
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

package org.sglj.sync;

/**
 * Interface which is implemented by components that can be refreshed.
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public interface Refreshable {
	
	/**
	 * Default min refresh time = 100 milliseconds.
	 */
	static final int DEFAULT_MIN_REFRESH_INTERVAL = 100;
	
	/**
	 * Default max refresh time = one minute.
	 */
	static final int DEFAULT_MAX_REFRESH_INTERVAL = 60*1000;
	
	/**
	 * Does the actual refresh.
	 */
	void refresh();
	
	/**
	 * Checks whether refresh is already in progress.
	 * @return <code>true</code> if it is, <code>false</code> otherwise.
	 */
	boolean isRefreshInProgress();
	
	/**
	 * Returns minimum time which must elapse since last refresh
	 * in order to let another refresh to occur (that is, 
	 * {@link #refresh()} method is called).
	 * @return interval in milliseconds
	 */
	int getMinRefreshInterval();
	
	/**
	 * Vraca maksimalno vrijeme u milisekundama koje
	 * se tolerira od zadnjeg vremena poziva metode
	 * {@link #refresh()}.
	 * @return interval u milisekundama
	 */
	/**
	 * Returns maximum time which can elapse since last refresh.
	 * @return interval in milliseconds
	 */
	int getMaxRefreshInterval();
	
	/**
	 * Registers new {@link RefreshListener} listener.
	 * @param l listener
	 */
	void addRefreshListener(RefreshListener l);
	
	/**
	 * Unregisters {@link RefreshListener} listener.
	 * @param l listener
	 */
	void removeRefreshListener(RefreshListener l);
}
