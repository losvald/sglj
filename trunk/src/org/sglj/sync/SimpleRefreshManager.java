/*
 * SimpleRefreshManager.java
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
 * <p>Simple refresh manager which behaves in the following manner:
 * Each registered refreshable component for which auto-refresh
 * is enabled is kept refreshed so that no more than
 * {@link Refreshable#getMaxRefreshInterval()} milliseconds
 * elapse since last refresh. Generally, automatic refreshes occur
 * at fixed time intervals, but they are postoponed each time
 * a manual refresh occurs.<br>
 * For example, if some refreshable component has max refresh interval of 2 s,
 * each time {@link Refreshable#refresh()} method is called on it (including
 * automatic call from this manager), a new refresh will be scheduled 
 * automatically so that it occurs exactly 2 seconds later. In case that, 
 * in the meantime, manual refresh occurs, this refresh will be rescheduled
 * so that it occurs again 2 seconds later than last refresh occured.</p>
 * <p><b>Note</b>: this manager completely relies on the correct usage
 * of the following two methods (by some refreshable components): 
 * <ul>
 * <li> {@link RefreshListener#refreshStarted(RefreshEvent)} </li>
 * <li> {@link RefreshListener#refreshed(RefreshEvent)}</li>
 * </ul></p>
 * 
 * @author Leo Osvald
 * @version 0.81
 */
public class SimpleRefreshManager extends AbstractRefreshManager {

	/**
	 * Constructor.<br>Auto-refresh is on, by default.
	 */
	public SimpleRefreshManager() {
		this(true);
	}
	
	/**
	 * Constructor.
	 * @param autoRefresh initial autorefresh state
	 */
	public SimpleRefreshManager(boolean autoRefresh) {
		setAutoRefreshEnabled(autoRefresh);
	}
	
	@Override
	protected boolean decideToProceed(RefreshTimerTask task) {
		return !task.refreshable().isRefreshInProgress();
	}

	@Override
	protected RefreshTimerTask scheduleForAutoRefresh(Refreshable refreshable) {
		RefreshTimerTask timerTask = new RefreshTimerTask(refreshable);
		timer.schedule(timerTask, refreshable.getMaxRefreshInterval());
		return timerTask;
	}

	@Override
	public void maxRefreshIntervalChanged(RefreshEvent e) {
		//do nothing, just wait for next schedule
	}

	@Override
	public void minRefreshIntervalChanged(RefreshEvent e) {
		//do nothing, just wait for next schedule
	}

	@Override
	public void refreshStarted(RefreshEvent e) {
	}

	@Override
	public void refreshed(RefreshEvent e) {
		if(!isAutoRefreshEnabled())
			return;
		
		//just in case, see if refresh task is already scheduled
		//and try to cancel it
		RefreshTimerTask task = timerTasks.get(e.getRefreshable());
		if(task != null) {
			task.cancel();
		}
		if(!task.refreshable().isRefreshInProgress()) {
			synchronized (scheduleMutex) {
				timerTasks.put(task.refreshable(), 
						scheduleForAutoRefresh(task.refreshable()));
			}
		} else {
//			System.err.println("Refresh is already in progress"
//					+" so DIDN'T schedule!");
		}
	}
	
	@Override
	public void refreshCanceled(RefreshEvent e) {
	}

	@Override
	protected void refreshRejected(RefreshTimerTask task) {
	}
	
}
