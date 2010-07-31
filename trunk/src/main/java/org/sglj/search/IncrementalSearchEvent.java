/*
 * IncrementalSearchEvent.java
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


import java.util.EventObject;

/**
 * The event which indicates that the state of
 * incremental search has changed. 
 * 
 * @see IncrementalFinder
 * @see IncrementalSearchModel
 * @author Leo Osvald
 * @version 1.0
 */
public class IncrementalSearchEvent<T> extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private IncrementalSearchModel<T> model;
	
	public IncrementalSearchEvent(Object o) {
		super(o);
	}
	
	public IncrementalSearchModel<T> getModel() {
		return model;
	}
	
	public void setModel(IncrementalSearchModel<T> model) {
		this.model = model;
	}
	
}
