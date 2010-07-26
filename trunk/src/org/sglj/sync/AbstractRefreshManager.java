/*
 * AbstractRefreshManager.java
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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Abstract implementation of {@link RefreshManager} interface.<br>
 * All implemented methods are thread-safe.
 * 
 * <br>
 * Note: To avoid nondeterministic behavior caused by multiple threads,
 * it is mandatory that all {@link Refreshable} implementations have
 * their methods {@link #equals(Object)} and {@link #hashCode()} 
 * implemented in a thread-safe manner. 
 * 
 * @author Leo Osvald
 * @version 0.91
 */
public abstract class AbstractRefreshManager implements RefreshManager,
RefreshListener {

	/**
	 * Map which contains all refreshables and their timers which
	 * are responsible for automatic refresh.<br>
	 * Refreshables that are not being automatically refreshed have
	 * <code>null</code> value associated in this map.<br>
	 * This map is thread-safe.<br>
	 * <b>Note</b>: For iterating through this map, external synchronization
	 * is required.
	 */
	protected final Map<Refreshable, RefreshTimerTask> timerTasks;
	
	/**
	 * Timer which is responsible for scheduling refresh tasks.
	 */
	protected final RefreshTimer timer;

	/**
	 * Mutex which serves for synchronization purposes regarding
	 * scheduling for refresh.<br>
	 * The following methods should generally be called from a 
	 * synchronized block (on this mutex):
	 * <ul>
	 * <li>{@link #scheduleForAutoRefresh(Refreshable)}</li>
	 * <li>{@link #initAutoRefresh()}</li>
	 * <li>{@link #decideToProceed(RefreshTimerTask)}</li>
	 * <li>{@link #refreshRejected(RefreshTimerTask)}</li> 
	 * </ul>
	 * </pre>
	 */
	protected final Object scheduleMutex = new Object();
	
	private volatile boolean enabled;
	
	private final Set<Refreshable> autoRefreshSet;

	public AbstractRefreshManager() {
		timer = new RefreshTimer();
		timerTasks = Collections.synchronizedMap(
				new HashMap<Refreshable, RefreshTimerTask>());
		autoRefreshSet = new HashSet<Refreshable>();
	}
	
	@Override
	public void addRefreshable(Refreshable refreshable, boolean autoRefresh) {
		if(!timerTasks.containsKey(refreshable)) {
			setAutoRefreshEnabled(refreshable, autoRefresh);
		}
	}

	@Override
	public void setAutoRefreshEnabled(Refreshable refreshable,
			boolean autoRefresh) {
		boolean changed = true;
		synchronized (timerTasks) {
			//if this refreshable exists and hasn't changed its state
			//we have nothing to change
			if(timerTasks.containsKey(refreshable)
					&& (timerTasks.get(refreshable) != null) == autoRefresh) 
				changed = false;
		}
		if(changed) {
			if(autoRefresh) {
				refreshable.addRefreshListener(this);
				if(isAutoRefreshEnabled()) {
					synchronized (scheduleMutex) {
						timerTasks.put(refreshable, scheduleForAutoRefresh(refreshable));
					}
				}
				synchronized (autoRefreshSet) {
					autoRefreshSet.add(refreshable);
				}
			}
			else {
				TimerTask timerTask = timerTasks.get(refreshable);
				timerTasks.put(refreshable, null);
				refreshable.removeRefreshListener(this);
				if(isAutoRefreshEnabled()) {
					if(timerTask != null) {
						timerTask.cancel();
					}
				}
				synchronized (autoRefreshSet) {
					autoRefreshSet.remove(refreshable);
				}
			}
		}
	}
	
	@Override
	public boolean isAutoRefreshEnabled(Refreshable refreshable) {
		return timerTasks.get(refreshable) != null;
	}

	@Override
	public boolean removeRefreshable(Refreshable refreshable) {
		RefreshTimerTask timerTask = timerTasks.get(refreshable);
		if(timerTask != null) {
			timerTasks.remove(timerTask.refreshable);
			timerTask.cancel();
			if(isAutoRefreshEnabled(timerTask.refreshable))
				timerTask.refreshable.removeRefreshListener(this);
			return true;
		}
		synchronized (autoRefreshSet) {
			autoRefreshSet.remove(refreshable);
		}
		return false;
	}

	@Override
	public void setAutoRefreshEnabled(boolean enabled) {
		if(this.enabled != enabled) {
			this.enabled = enabled;
			initAutoRefresh();
		}
	}
	
	@Override
	public boolean isAutoRefreshEnabled() {
		return enabled;
	}
	
	@Override
	public Set<Refreshable> getRefreshables() {
		Set<Refreshable> ret = new HashSet<Refreshable>();
		synchronized (timerTasks) {
			ret.addAll(timerTasks.keySet());
			return ret;
		}
	}
	
	/**
	 * This method is called when refresh task is run from inside
	 * the {@link RefreshTimerTask#run()} method.<br>
	 * This is the right place for eventual decision whether refresh task
	 * should proceed with refresh or not.
	 * @param task refresh task
	 * @return <code>true</code> if refresh task should proceed, 
	 * <code>false</code> otherwise.
	 */
	protected abstract boolean decideToProceed(RefreshTimerTask task);
	
	/**
	 * This method is called when refresh task is rejected
	 * (i.e. the same refreshable has just been refreshed).
	 * @param task
	 */
	protected abstract void refreshRejected(RefreshTimerTask task);

	/**
	 * This method should do the scheduling of the specified refreshable.
	 * @param refreshable refreshable
	 * @return refresh task which was scheduled or <code>null</code> if no
	 * refresh task was scheduled.
	 */
	protected abstract RefreshTimerTask scheduleForAutoRefresh(Refreshable refreshable);

	/**
	 * Initializes autorefresh.<br>
	 * This method is called automatically whenever automatic refresh
	 * is enabled or disabled (if state actually changed).
	 */
	protected void initAutoRefresh() {
		synchronized (scheduleMutex) {
			Object[] refs;
			synchronized (autoRefreshSet) {
				//copy tasks to avoid ConcurrentModificationException
				//in scheduleForAutoRefresh
				refs = autoRefreshSet.toArray();
			}
			
			if(enabled) {
				//if this timer is unable to schedule new tasks, create a new one
//				Object[] tasks;
//				synchronized (timerTasks) { //must lock here
//					//copy tasks to avoid ConcurrentModificationException
//					//in scheduleForAutoRefresh
//					tasks = timerTasks.values().toArray();
//				}
//				for(Object task : tasks)
//					if(task != null) {
//						Refreshable refreshable = ((RefreshTimerTask)task).refreshable();
//						timerTasks.put(refreshable, scheduleForAutoRefresh(refreshable));
//						if(Environment.isDevelopment())
//							System.out.println("Enabling: "+refreshable);
//					}
				for(Object o : refs) {
					Refreshable refreshable = (Refreshable) o;
					timerTasks.put(refreshable, scheduleForAutoRefresh(refreshable));
//					System.out.println("Enabling: "+refreshable);
				}
			}
			else {
				for(Object o : refs) {
					Refreshable refreshable = (Refreshable) o;
					RefreshTimerTask task = timerTasks.get(refreshable);
//					System.out.println("Disabling: "+refreshable);
					if(task != null)
						task.cancel();
				}
			}
		}
	}
	
	/**
	 * <p>Refresh task.<br>
	 * Its {@link #run()} method behaves like this:
	 * First,
	 * {@link AbstractRefreshManager#decideToProceed(RefreshTimerTask)}
	 * method is called. If call returns <code>true</code>, 
	 * {@link Refreshable#refresh()} method is called on corresponding
	 * refreshable component. Otherwise, 
	 * {@link AbstractRefreshManager#refreshRejected(RefreshTimerTask)}
	 * method is called.</p>
	 * <p>Methods {@link AbstractRefreshManager#decideToProceed(RefreshTimerTask)}
	 * and {@link AbstractRefreshManager#refreshRejected(RefreshTimerTask)}
	 * are synchronized on the same object, so that decision whether some
	 * future task should proceed with refresh can depend on the one currently
	 * rejected. 
	 * 
	 * @author Leo Osvald
	 *
	 */
	protected class RefreshTimerTask extends TimerTask {
		private final Refreshable refreshable;

		public RefreshTimerTask(Refreshable refreshable) {
			this.refreshable = refreshable;
		}

		@Override
		public void run() {
			try {
//				System.out.println("Started running task: " +refreshable);
				boolean proceed;
				synchronized (scheduleMutex) {
					proceed = decideToProceed(this);
				}
				if(proceed) {
					refreshable.refresh();
				}
				else {
					synchronized (scheduleMutex) {					
						refreshRejected(this);
					}
				}
//				System.out.println("Ended running task: "+refreshable);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((refreshable == null) ? 0 : refreshable.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof RefreshTimerTask))
				return false;
			RefreshTimerTask other = (RefreshTimerTask) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (refreshable == null) {
				if (other.refreshable != null)
					return false;
			} else if (!refreshable.equals(other.refreshable))
				return false;
			return true;
		}

		public Refreshable refreshable() {
			return refreshable;
		}

		private AbstractRefreshManager getOuterType() {
			return AbstractRefreshManager.this;
		}

	}

	protected static class RefreshTimer extends Timer {
		public RefreshTimer() {
			super(true);
		}
		
		/**
		 * Does nothing.
		 */
		@Override
		public void cancel() {
		}
		
	}
	
//	/**
//	 * This method is called automatically from
//	 * {@link RefreshTimerTask#run()} method, immediately before
//	 * call to refresh method on the task if manager decided to proceed with
//	 * its refresh.
//	 * @param task refresh task
//	 */
//	protected abstract void autoRefreshStarted(RefreshTimerTask task);
//
//	/**
//	 * This method is called right after {@link Refreshable#refresh()} 
//	 * methods ends.
//	 * @param task
//	 */
//	protected abstract void autoRefreshDone(RefreshTimerTask task);
	
}
