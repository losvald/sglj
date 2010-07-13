/*
 * SetupStep.java
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

package org.sglj.swing.dialog;


import javax.swing.JPanel;

import org.sglj.swing.util.FormData;

/**
 * Class which represent one step of the setup dialog (see 
 * {@link SetupDialog}).
 * It contains a panel which contents is automatically stored
 * using {@link FormData} class, and provides a series of method
 * which describes the state of the step.
 * 
 * @see SetupDialog
 * @author Leo Osvald
 * @version 0.6
 */
public abstract class SetupStep {
	private static final long serialVersionUID = 1L;
	
	private SetupDialog setupDialog;
	
	private JPanel panel;
	
	private FormData defaultData = new FormData(); //zasad nepotrebno
	private FormData currData = new FormData();
	
	private boolean completed;
	private boolean initialized;
	private boolean loaded;
	
	
	public SetupStep(SetupDialog setupDialog) {
		this.setupDialog = setupDialog;
	}
	
	public SetupDialog getSetupDialog() {
		return setupDialog;
	}
	
	public void setSetupDialog(SetupDialog setupDialog) {
		this.setupDialog = setupDialog;
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public void setPanel(JPanel panel) {
		//TODO
		this.panel = panel;
		getSetupDialog().validate();
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	/**
	 * First, if the step is not initialized, the {@link #initialize()} method
	 * is called. Then, the method {@link #onLoad()} is called. Finally,
	 * a state of the step is loaded, using {@link FormData}.
	 * @throws IllegalStateException if the step is already
	 * loaded
	 */
	public void load() throws IllegalStateException {
		if(loaded)
			throw new IllegalStateException(
					"Trying to load component which is already loaded!");
		
		if(!initialized) {
			initialize();
			initialized = true;
			//save initial state of the panel
			defaultData.storeAll(panel, true);
		}
		
		onLoad();
		if(initialized)
			currData.loadAll(panel, true);
		loaded = true;
	}
	
	/**
	 * Saves the state of the step.
	 */
	private void saveState() {
		currData.clear();
		currData.storeAll(panel, true);
	}
	
	/**
	 * <p>Finishes the step. The the following methods are called
	 * from inside this method (in order): 
	 * {@link #onCompleted()}, {@link #leave()}</p>
	 * <p>After this method returns, all calls to {@link #isCompleted()}
	 * will return <code>true</code>.</p>
	 * pa {@link #leave()}.
	 * Takodjer, daljnji pozivi metode {@link #isCompleted()}
	 * ce vracati <code>true</code>.
	 */
	public void complete() {
		onCompleted();
		completed = true; //XXX what if onCompleted is a callback ???
		leave();
	}
	
	/**
	 * Saves the state of the step and calls {@link #onLeave()} method.
	 * @throws IllegalStateException if the step is not loaded
	 */
	public void leave() throws IllegalStateException {
		if(!loaded)
			throw new IllegalStateException(
					"Trying to leave component which is not loaded!");
		
		saveState();
		onLeave();
		loaded = false;
		panel = null;
	}
	
	/**
	 * This method is called when the step becomes active for the first time,
	 * but can be explicitly called to reinitialize the step
	 * (Note: in that case, if {@link #load()} method is called prior to this,
	 * calls to {@link #isInitialized()} method will return <code>false</code>).
	 * <br>
	 * It is recommended to initialize the panel here, as well as
	 * all important data.
	 */
	protected abstract void initialize();
	
	/**
	 * This method will be called each time the step becomes active.<br>
	 * All creation of visible components which belong to this
	 * step should be done here, in order to preserve memory.
	 * Setting the initial text of text fields, 
	 * selection of buttons and similar is not necessary, as this is
	 * automatically done via {@link FormData} class (for all named components). 
	 * <br>
	 * This is the right place to set a panel using {@link #setPanel(JPanel)}
	 * method. 
	 */
	protected abstract void onLoad();
	
	/**
	 * This method is called right before the step finished,
	 * from inside the method {@link #complete()}.
	 */
	protected abstract void onCompleted();
	
	/**
	 * It is advisable to do the deallocation of the memory here,
	 * like nullifying created components to let GC free memory.
	 * In short, the opposite of the {@link #onLeave()} method.
	 */
	protected abstract void onLeave();
		
	/**
	 * This methods should check whether all conditions are met
	 * which prevent the step from being finished. For example,
	 * here can be checked whether the user has filled in
	 * all required fields etc..
	 * 
	 * @return <code>true</code> if all conditions are met and this
	 * step can be finished.
	 */
	public abstract boolean validate();
}
