/*
 * TimerUtils.java
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

import java.util.Timer;

/**
 * Utility class which boost functionality of the {@link Timer} class.
 * 
 * @author Leo Osvald
 * 
 */
public class TimerUtils {
	private TimerUtils() {
	}
	
	/**
	 * Checks whether new tasks can be scheduled, that is
	 * whether the time thread is still active.
	 * @param timer
	 * @return <code>true</code> if this is the case, <code>false</code> 
	 * otherwise.
	 */
	public static boolean mayNewTasksBeScheduled(Timer timer) {
		//quick and dirty way to check if timer can schedule new tasks
		try {
			timer.schedule(null, 0);
		} catch(IllegalStateException ex) {
			return false;
		} catch(NullPointerException ex) {
			return true;
		}
		return true;
	}
	
}
