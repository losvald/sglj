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
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Dialog which resembles some kind of a setup.
 * It consists of one or more steps (see {@link SetupStep}).
 * 
 * @author Leo Osvald
 * @version 0.93
 */
public abstract class SetupDialog extends DynamicContentDialog {
	
//	protected JPanel navigationPanel;
//	private JButton backButton;
		
	private SetupStep currStep;
	
	private final ActionPreviousStep actionBack = createPreviousStepAction();
	private final ActionNextStep actionNext = createNextStepAction();
	private final ActionFinish actionFinish = createFinishAction();	
	
	private final Object[] options = {
			new JButton(actionBack),
			new JButton(actionNext),
			new JButton(actionFinish)
	};
	
	private static final long serialVersionUID = 1L;
	
	public SetupDialog(Frame parentFrame, String title) {
		super(parentFrame, title, false); //FIXME ne radi sa true
		setOptionPane(createOptionPane());
		
		update();
		
//		setSize(500,400);
		//pack();
	}
	
//	private JPanel createNavigationPanel() {
//		backButton = new JButton(actionBack);
//		nextOrFinishButton = nextButton = new JButton(actionNext);
//		finishButton = new JButton(actionFinish);
//		
//		Dimension nextDim = nextButton.getPreferredSize();
//		Dimension finDim = finishButton.getPreferredSize();
//		Dimension maxDim = new Dimension(Math.max(nextDim.width, finDim.width), 
//				Math.max(nextDim.height, finDim.height));
//		backButton.setPreferredSize(maxDim);
//		nextButton.setPreferredSize(maxDim);
//		finishButton.setPreferredSize(maxDim);
//		System.out.println("Next button dim: " + nextDim);
//		System.out.println("Fin  button dim: " + finDim);
//		
//		backButton.setVisible(false);
//		
//		navigationPanel = new JPanel();
//		navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
//		navigationPanel.add(Box.createGlue());
//		navigationPanel.add(backButton);
//		navigationPanel.add(nextOrFinishButton);
//		
//		return navigationPanel;
//	}
	
	/**
	 * This method should be called to refresh the state of
	 * Back/Next/Finish button.<br>
	 * Before this method returns, method {@link #onUpdate()}
	 * is called from inside this method.
	 */
	public void update() {
		//replace next with finish on last step
//		if(!hasNextStep()) {
//			if(nextOrFinishButton == nextButton) {
//				navigationPanel.remove(nextButton);
//				navigationPanel.add(finishButton);
//			}
//			nextOrFinishButton = finishButton;
//		}
//		else {
//			if(nextOrFinishButton == finishButton) {
//				navigationPanel.remove(finishButton);
//				navigationPanel.add(nextButton);
//			}
//			nextOrFinishButton = nextButton;
//		}
//		
//		System.out.println("Finish instead of next? " + (nextOrFinishButton == finishButton));
//		navigationPanel.validate();
		
		
		if(getCurrentStep() != null) {
			//disable previous step on first step
			actionBack.setEnabled(hasPreviousStep());
			actionNext.setEnabled(hasNextStep() && getCurrentStep().validate());
			actionFinish.setEnabled(canFinish());
		}
		else {
			actionBack.setEnabled(false);
			actionNext.setEnabled(false);
			actionFinish.setEnabled(false);
		}
		
		//update action
		onUpdate();
	}
	
	
	
	public SetupStep getCurrentStep() {
		return currStep;
	}
	
//	/**
//	 * Postavlja jeli "back" gumb postoji.
//	 */
//	public void setBackVisible(boolean visible) {
//		backButton.setVisible(visible);
//	}
	
	/**
	 * Proceeds to the next step of the setup.
	 * This method is automatically called when the user clicks
	 * on "Next" button.
	 */
	public abstract void nextStep();
	
	/**
	 * Goes back to the previous step of the setup.
	 * This method is automatically called when the user clicks
	 * on "Back" button.
	 */
	public abstract void previousStep();
	
	/**
	 * Checks whether there are more steps to advance to.
	 * @return <code>true</code> if this is not the last step, 
	 * <code>false</code> if this is the last step.
	 */
	public abstract boolean hasNextStep();
	
	/**
	 * Checks whether there are previous steps, or this is the first one.
	 * @return <code>true</code> if there are, <code>false</code> otherwise.
	 */
	public abstract boolean hasPreviousStep();
	
	/**
	 * Checks whether the setup can finish.
	 * This has a direct impact on the state of the
	 * "Finish" button. If the setup can finish, "Finish"
	 * button will be enabled (otherwise it will be disabled).
	 * @return <code>true</code> if setup can finish,
	 * <code>false</code> otherwise.
	 */
	public abstract boolean canFinish();
	
	/**
	 * Finishes the setup.
	 * This method is automatically called when the user clicks
	 * on "Finish" button.
	 */
	public abstract void finish();
	
	/**
	 * This method is automatically called from inside the 
	 * {@link #update()} method.
	 */
	protected abstract void onUpdate();
	
	/**
	 * This method is called whenever an attempt to close the dialog
	 * occurs or the user clicks "Cancel" button.
	 * All actions and behavior should be done here, such as
	 * asking for confirmation, saving state etc..
	 */
	protected abstract void onClose();	
	
	/**
	 * Sets the specified step as the current one,
	 * after calling {@link SetupStep#leave()} on the previous one.
	 * @param setupStep
	 */
	protected void setCurrentStep(SetupStep setupStep) {
		if(this.currStep == setupStep)
			return ;
		if(this.currStep != null)
			this.currStep.leave();
		this.currStep = setupStep;
		setupStep.load();
		setContent(setupStep.getPanel());
		update();
	}
	
	@Override
	protected JOptionPane createOptionPane() {
		return new JOptionPane(
				contentPane, 
				JOptionPane.PLAIN_MESSAGE, 
				JOptionPane.YES_NO_OPTION, 
				null, 
				options);
	}
	
	protected abstract ActionNextStep createNextStepAction();
	
	protected abstract ActionPreviousStep createPreviousStepAction();
	
	protected abstract ActionFinish createFinishAction();
	
	
	protected class ActionNextStep extends AbstractAction {		
		private static final long serialVersionUID = 1L;

		public ActionNextStep(String name, Icon icon,
				String shortDescription, int mnemonicKey) {
			super(name, icon);
			putValue(SHORT_DESCRIPTION, shortDescription);
	        putValue(MNEMONIC_KEY, mnemonicKey);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			nextStep();
		}
	}
	
	protected class ActionPreviousStep extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public ActionPreviousStep(String name, Icon icon,
				String shortDescription, int mnemonicKey) {
	        super(name, icon);
	        putValue(SHORT_DESCRIPTION, shortDescription);
	        putValue(MNEMONIC_KEY, mnemonicKey);
	    }
		
		@Override
		public void actionPerformed(ActionEvent e) {
			previousStep();
		}
	}
	
	protected class ActionFinish extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public ActionFinish(String name, Icon icon,
				String shortDescription, int mnemonicKey) { 
	        super(name, icon);
	        putValue(SHORT_DESCRIPTION, shortDescription);
	        putValue(MNEMONIC_KEY, mnemonicKey);
	    }
		
		@Override
		public void actionPerformed(ActionEvent e) {
			finish();
		}
		
	}

}
