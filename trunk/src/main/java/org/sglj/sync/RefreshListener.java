/*
 * RefreshListener.java
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

import java.util.EventListener;

/**
 * Listener which listens for {@link RefreshEvent} events.
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public interface RefreshListener extends EventListener {
	
	/**
	 * Informs that refresh has just started.
	 * @param e event
	 */
	void refreshStarted(RefreshEvent e);
	
	/**
	 * Informs that refresh is finished.
	 * @param e event
	 */
	void refreshed(RefreshEvent e);
	
	/**
	 * Informs that refresh was canceled.
	 * @param e event
	 */
	void refreshCanceled(RefreshEvent e);
	
	/**
	 * Informs that minimum refresh time has changed. 
	 * @param e event
	 */
	void minRefreshIntervalChanged(RefreshEvent e);
	
	/**
	 * Informs that maximum refresh time has changed. 
	 * @param e event
	 */
	void maxRefreshIntervalChanged(RefreshEvent e);
}
