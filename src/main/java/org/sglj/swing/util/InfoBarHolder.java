/*
 * InfoBarHolder.java
 * 
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
 * Copyright (C) 2010 Nikola Banić <nbanic@gmail.com>
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

package org.sglj.swing.util;

/**
 * All classes which can display {@link InfoBar} should implement
 * this interface.
 * 
 * @author Leo Osvald
 * @author Nikola Banić
 * 
 * @version 1.0
 */
public interface InfoBarHolder {
	
	
	/**
	 * This method is called from inside {@link InfoBar} to indicate
	 * that the specified info bar should be shown.
	 * @param infoBar info bar which should be shown
	 * @return <code>true</code> if the specified info bar is guaranteed 
	 * to be shown, <code>false</code> otherwise. 
	 */
	boolean infoBarShowRequested(InfoBar infoBar);
	
	
	/**
	 * This method is called from inside {@link InfoBar} to indicate
	 * that the specified info bar should be hidden.
	 * @param infoBar info bar
	 * @return <code>true</code> if the specified info bar is guaranteed 
	 * to be hidden, <code>false</code> otherwise. 
	 */
	boolean infoBarHideRequested(InfoBar infoBar);
}
