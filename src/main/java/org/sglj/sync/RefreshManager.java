/*
 * RefreshManager.java
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

import java.util.Set;

/**
 * Manager which manages refreshment process.<br>
 * It is responsible for automatic refreshment, and keep tracks of refreshable
 * components (see {@link Refreshable}.
 * 
 * @see Refreshable
 * 
 * @author Leo Osvald
 * @version 1.0
 * 
 */
public interface RefreshManager {
	
	/**
	 * Registers new refreshable component, with its auto-refresh enabled
	 * or disabled.
	 * @param refreshable refreshable component
	 * @param autoRefresh <code>true</code> if auto-refresh for that
	 * component should be enabled, <code>false</code> otherwise.
	 */
	void addRefreshable(Refreshable refreshable, boolean autoRefresh);
	
	/**
	 * Unregisters existing refreshable component.
	 * @param refreshable refreshable component
	 * @return <code>true</code> if that component was actually registered
	 * and has been removed, <code>false</code> otherwise.
	 */
	boolean removeRefreshable(Refreshable refreshable);
	
	/**
	 * Enables/disables automatic refresh for specified refreshable component.
	 * @param refreshable refreshable component
	 * @param autoRefresh <code>true</code> if auto-refresh for that
	 * component should be enabled, <code>false</code> otherwise.
	 */
	void setAutoRefreshEnabled(Refreshable refreshable, boolean autoRefresh);
	
	/**
	 * Checks whether auto-refresh is enabled for specifed refreshable
	 * component.
	 * @param refreshable refreshable component
	 * @return <code>true</code> if auto-refresh is enabled, 
	 * <code>false</code> if it is disabled.
	 */
	boolean isAutoRefreshEnabled(Refreshable refreshable);
	
	/**
	 * Returns a set of refreshable components which are registered
	 * by this manager.
	 * Note: each time this method is called, a new copy is created
	 * and returned.
	 * @return set of refreshable components
	 */
	<T extends Refreshable> Set<T> getRefreshables();
	
	/**
	 * Checks whether global auto-refresh is enabled.
	 * If it is disabled, none of the refreshable components can be
	 * automatically refreshed. This does not affect "flags"
	 * for each refreshable component.
	 * @return <code>true</code> if global automatic refreshment is enabled,
	 * <code>false</code> otherwise.
	 */
	boolean isAutoRefreshEnabled();
	
	/**
	 * Enables or disables global automatic refreshment.<br>
	 * If it is enabled, each component for which auto-refresh is enabled
	 * will be kept refreshed.
	 * @param enabled <code>true</code> if automatic refreshment is enabled,
	 * <code>false</code> otherwise.
	 */
	void setAutoRefreshEnabled(boolean enabled);
}
