/*
 * SetupDialog.java
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


import java.awt.Frame;
import java.nio.channels.IllegalSelectorException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Setup dialog with fixed steps.<br>
 * Steps are set via {@link #setSteps(SetupStep...)} method.
 * 
 * @author Leo Osvald
 * @version 0.71
 */
public abstract class FixedStepSetupDialog extends SetupDialog {

	private static final long serialVersionUID = 1L;

	protected SetupStep[] steps;
	
	private int currStepIndex;
	
	private Map<SetupStep, Integer> stepIndex;
	
	public FixedStepSetupDialog(Frame parentFrame, String title) {
		super(parentFrame, title);
	}

	/**
	 * Returns the step at the specified index.
	 * @param index 0-based index
	 * @return the step
	 */
	public SetupStep getStep(int index) {
		return steps[index];
	}
	
	/**
	 * Returns steps of the dialog.
	 * @return array of steps
	 */
	public SetupStep[] getSteps() {
		return Arrays.copyOf(steps, steps.length);
	}
	
	/**
	 * Sets steps in the specified order.<br>
	 * In addition, the first step specified will be
	 * set as the current one.
	 * @param steps steps
	 */
	public void setSteps(SetupStep... steps) {
		this.steps = steps;
		stepIndex = new HashMap<SetupStep, Integer>(steps.length);
		for(int i = 0; i < this.steps.length; ++i) {
			//make each step belong to this setup dialog
			this.steps[i].setSetupDialog(this);
			//index them all, to know currIndex in case of a jump
			stepIndex.put(this.steps[i], i);
		}
		//set first step as current
		if(!this.stepIndex.isEmpty()) {
			try { //XXX this try-catch should be removed
				setCurrentStep(this.steps[0]);
			} catch(IllegalStateException bug) {
				System.err.println("BUG: FixedStepSetup ne radi kako spada!");
			}
		}
	}
	
	@Override
	public boolean hasNextStep() {
		return currStepIndex+1 < steps.length;
	}

	@Override
	public boolean hasPreviousStep() {
		return currStepIndex > 0;
	}

	@Override
	public void nextStep() {
		if(!hasNextStep())
			throw new IllegalSelectorException();
		setCurrentStep(steps[++currStepIndex]);
	}

	@Override
	public void previousStep() {
		if(!hasPreviousStep())
			throw new IllegalSelectorException();
		setCurrentStep(steps[--currStepIndex]);
	}
	
	/**
	 * {@inheritDoc
	 * }
	 * @throws {@link IllegalStateException} if the specified
	 * step does not belong to this setup dialog 
	 */
	@Override
	protected void setCurrentStep(SetupStep setupStep) 
	throws IllegalStateException {
		if(!stepIndex.containsKey(setupStep))
			throw new IllegalStateException("Tried to set non-existent setupStep in FixedStepSetupDialog");
		currStepIndex = stepIndex.get(setupStep);
		super.setCurrentStep(setupStep);
	}

}
