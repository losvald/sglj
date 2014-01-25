/*
 * DeterminateTask.java
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

package org.sglj.task;

/**
 * The task which has determinate (predictable) time of the execution,
 * or consists of known phases.
 * This type of task has its own progress, which is a real value
 * between 0 and 1 (for example, 0.42 means that 42% of the task completed).  
 * 
 * @author Leo Osvald
 * @version 1.2
 */
public interface DeterminateTask extends Task {
	/**
	 * Returns the progress of the task.
	 * @return real value from the interval <b>[0, 1]</b>
	 */
	double getProgress();
	
	/**
	 * Sets the progress of the task.
	 * @param value real value from interval [0, 1]
	 * @return <code>true</code> if the difference between
	 * the last value and the current one is fine enough
	 * to be registered. Sensitivity can be obtained
	 * by calling the {@link #getSensitivity()} method.
	 */
	boolean setProgress(double value);
	
	/**
	 * Returns the sensitivity of the progress.<br>
	 * The sensitivity is defined as the minimal difference in the 
	 * progress value which results in an event being fired to inform 
	 * {@link TaskProgressListener} listeners. 
	 * @return osjetljivost
	 */
	double getSensitivity();
	
	/**
	 * Sets the sensitivity of the progress.<br>
	 * The implementation must ensure that every difference
	 * of at least <code>value</code> results in the appropriate event
	 * being fired.
	 * @param value the desired sensitivity
	 */
	void setSensitivity(double value);
	
	/**
	 * Registers a new listener for changes in the progress.
	 * @param l listener
	 */
	void addTaskProgressListener(TaskProgressListener l);
	
	/**
	 * Unregisters an existing listenr which listens for changes in the 
	 * progress.
	 * @param l listener
	 */
	void removeTaskProgressListener(TaskProgressListener l);
	
	/**
	 * Registers a new {@link DeterminateTaskListener} listener.
	 * @param l listener
	 */
	void addDeterminedTaskListener(DeterminateTaskListener l);
	
	/**
	 * Unregisters an existing {@link DeterminateTaskListener} listener.
	 * @param l listener
	 */
	void removeDeterminedTaskListener(DeterminateTaskListener l);
	
}
