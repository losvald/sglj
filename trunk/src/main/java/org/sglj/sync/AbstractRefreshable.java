/*
 * AbstractRefreshable.java
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

import javax.swing.event.EventListenerList;

/**
 * Abstract implementation of the {@link Refreshable} interface.<br>
 * All methods are thread-safe.
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public abstract class AbstractRefreshable implements Refreshable {
	
	private volatile int minRefreshInterval;
	private volatile int maxRefreshInterval;
	private volatile boolean refreshInProgress;
	
	//access is thread-safe because event-dispatching is inside synchronized block
	private RefreshEvent event;
	private final EventListenerList listeners = new EventListenerList();

	/**
	 * Creates refreshable with specified minimum and maximum refresh interval.
	 * @param minRefreshInterval minimum refresh interval in milliseconds)
	 * @param maxRefreshInterval maximum refresh interval in milliseconds)
	 */
	public AbstractRefreshable(int minRefreshInterval, int maxRefreshInterval) {
		setMinRefreshInterval(minRefreshInterval);
		setMaxRefreshInterval(maxRefreshInterval);
	}
	
	/**
	 * Creates refreshable with minimum refresh interval equal to
	 * {@link Refreshable#DEFAULT_MIN_REFRESH_INTERVAL} and maximum refresh
	 * interval equal to {@link Refreshable#DEFAULT_MAX_REFRESH_INTERVAL}.
	 */
	protected AbstractRefreshable() {
		this(DEFAULT_MIN_REFRESH_INTERVAL, DEFAULT_MAX_REFRESH_INTERVAL);
	}
	
	@Override
	public int getMinRefreshInterval() {
		return minRefreshInterval;
	}
	
	@Override
	public int getMaxRefreshInterval() {
		return maxRefreshInterval;
	}
	
	@Override
	public boolean isRefreshInProgress() {
		return refreshInProgress;
	}

	@Override
	public void addRefreshListener(RefreshListener l) {
		listeners.add(RefreshListener.class, l);
	}
	
	@Override
	public void removeRefreshListener(RefreshListener l) {
		listeners.remove(RefreshListener.class, l);
	}
	
	protected void setMinRefreshInterval(int intervalInMillis) {
		//if value actually changed
		if(this.minRefreshInterval != intervalInMillis) {
			this.minRefreshInterval = intervalInMillis;
			fireMinRefreshIntervalChanged();
		}
	}
	
	protected void setMaxRefreshInterval(int intervalInMillis) {
		//if value actually changed
		if(this.maxRefreshInterval != intervalInMillis) {
			this.maxRefreshInterval = intervalInMillis;
			fireMaxRefreshIntervalChanged();
		}
	}
	
	/**
	 * Postavlja stanje osvjezavanja.<br>
	 * Automatski baca odgovarajuce eventove ako se stanje promijenilo.
	 * @param refreshInProgress <code>true</code> ako je refresh u tijeku,
	 * <code>false</code> inace.
	 */
	protected void setRefreshInProgress(boolean refreshInProgress) {
		boolean oldValue = this.refreshInProgress;
		if(oldValue != refreshInProgress) {
			this.refreshInProgress = refreshInProgress;
			if(refreshInProgress)
				fireRefreshStarted();
			else
				fireRefreshed();
		}
	}
	
	protected synchronized void fireRefreshStarted() {
		Object[] listeners;
		synchronized (this.listeners) {
			listeners = this.listeners.getListenerList();
			for (int i = listeners.length-2; i>=0; i-=2) {
				if (listeners[i]==RefreshListener.class) {
					createEventIfNeeded();
					((RefreshListener)listeners[i+1]).refreshStarted(event);
				}
			}
		}
	}
	
	protected synchronized void fireRefreshed() {
		Object[] listeners;
		synchronized (this.listeners) {
			listeners = this.listeners.getListenerList();
			for (int i = listeners.length-2; i>=0; i-=2) {
				if (listeners[i]==RefreshListener.class) {
					createEventIfNeeded();
					((RefreshListener)listeners[i+1]).refreshed(event);
				}
			}
		}
	}
	
	protected synchronized void fireCanceled() {
		Object[] listeners;
		synchronized (this.listeners) {
			listeners = this.listeners.getListenerList();
			for (int i = listeners.length-2; i>=0; i-=2) {
				if (listeners[i]==RefreshListener.class) {
					createEventIfNeeded();
					((RefreshListener)listeners[i+1]).refreshCanceled(event);
				}
			}
		}
	}
	
	protected synchronized void fireMinRefreshIntervalChanged() {
		Object[] listeners;
		synchronized (this.listeners) {
			listeners = this.listeners.getListenerList();
			for (int i = listeners.length-2; i>=0; i-=2) {
				if (listeners[i]==RefreshListener.class) {
					createEventIfNeeded();
					((RefreshListener)listeners[i+1])
					.minRefreshIntervalChanged(event);
				}
			}
		}
	}
	
	protected synchronized void fireMaxRefreshIntervalChanged() {
		Object[] listeners;
		synchronized (this.listeners) {
			listeners = this.listeners.getListenerList();
			for (int i = listeners.length-2; i>=0; i-=2) {
				if (listeners[i]==RefreshListener.class) {
					createEventIfNeeded();
					((RefreshListener)listeners[i+1])
					.maxRefreshIntervalChanged(event);
				}
			}
		}
	}
	
	private void createEventIfNeeded() {
		if (event == null)
            event = new RefreshEvent(this);
        event.setRefreshable(this);
	}

}
