/*
 * RefreshEvent.java
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

import java.util.EventObject;

public class RefreshEvent extends EventObject {
	
	private transient Refreshable refreshable;
	private static final long serialVersionUID = 1L;
	
	/**
	 * {@inheritDoc}
	 * @param source object which generated this event
	 */
	public RefreshEvent(Object source) {
		super(source);
	}
	
	/**
	 * Clones event.
	 * @param shallowCopy
	 * @param clone clone
	 */
	public RefreshEvent(RefreshEvent clone, boolean shallowCopy) {
		super(clone.getSource());
		setRefreshable(clone.getRefreshable());
	}

	public Refreshable getRefreshable() {
		return refreshable;
	}

	public void setRefreshable(Refreshable refreshable) {
		this.refreshable = refreshable;
	}
	
}
