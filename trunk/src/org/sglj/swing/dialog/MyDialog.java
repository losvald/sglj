/*
 * MyDialog.java
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

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * <p>Custom dialog; it provides control/validation, and access to
 * buttons of the dialog.
 * It also provides an ability to recursively add {@link KeyListener} 
 * listeners, so that each time a key is pressed, no matter which
 * subcomponent has the focus, an event is generated.
 * 
 * If an option is chosen or dialog is closed by clicking on "X", 
 * {@link #handleOption(Object)} method is called. All validation
 * should be done in that method.
 * Some of the options are supplied by setting option pane via
 * constructor or {@link #setOptionPane(JOptionPane)} method.
 * It is also possible to set a option pane of length 0, if there
 * should be no options (other than clicking on the "X", for example).
 * Buttons which corresponds to the options can be obtained via
 * {@link #getOptionButton(int)} method.<br>
 * To add {@link KeyListener} for the whole dialog (to all components,
 * recursively), there exists the 
 * {@link #MyDialog(Frame, JOptionPane, KeyListener)} constructor.</p>
 * 
 * <p>Dialog can be shown/hiddenDijalog by calling 
 * {@link #setVisible(boolean)} method. 
 * By default, the position of the dialog will be such that the center
 * of the dialog is as close as possible to the center of the parent frame. 
 * </p>
 * <p><b>Note:</b>Adding non-<code>null</code> {@link KeyListener} 
 * recursively may lead to "garbage" accumulation if new instances 
 * with the same {@link JOptionPane} are being creating. 
 * 
 * @see JDialog
 * @see JOptionPane
 * @see #handleOption(Object)
 * 
 * @author Leo Osvald
 * @version 0.63
 */
public abstract class MyDialog extends JDialog implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	
	private final Frame invokerFrame;
	private JOptionPane optionPane;
	
	private final KeyListener dialogKeyListener;
	private final RecursiveContainerListener recursiveContainerListener
	= new RecursiveContainerListener();
	
	private List<JButton> optionButtons;
	
	
	public MyDialog(Frame frame, JOptionPane optionPane, KeyListener dialogKeyListener) {
		super(frame);
		
		this.invokerFrame = frame;
		this.dialogKeyListener = dialogKeyListener;
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setOptionPane(optionPane);
	}
	
	public MyDialog(Frame frame, JOptionPane optionPane) {
		this(frame, optionPane, null);
	}
	
	public MyDialog(Frame frame, KeyListener dialogKeyListener) {
		this(frame, null, dialogKeyListener);
	}
	
	public MyDialog(Frame frame) {
		this(frame, (KeyListener)null);
	}

	public void setOptionPane(JOptionPane optionPane) {
		if(optionPane == null) return ; //ako je null, ignoriraj
		//izbrisi sve vezano uz stari optionPane
		if(this.optionPane != null) {
			getContentPane().remove(this.optionPane);
			this.optionPane.removePropertyChangeListener(this);
		}
		
		//postavi novi
		this.optionPane = optionPane;
		getContentPane().addContainerListener(recursiveContainerListener);
		getContentPane().add(optionPane);
		
		if(optionPane.getOptions() != null) {
			optionButtons = new ArrayList<JButton>();
			searchForOptionButtons(this);
		}
		
		addWindowListener(new WindowAdapter() {
	        	@Override
	        	public void windowClosing(java.awt.event.WindowEvent e) {
	        		MyDialog.this.optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
	        	};
		});
		
		optionPane.addPropertyChangeListener(this);
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(!isVisible()) return ;
		if(evt.getSource() != optionPane) return ;
		String property = evt.getPropertyName();
		if(!JOptionPane.VALUE_PROPERTY.equals(property) 
				&& !JOptionPane.INPUT_VALUE_PROPERTY.equals(property))
			return ;
		
		Object value = optionPane.getValue();
		if(value == JOptionPane.UNINITIALIZED_VALUE) return ;
		
		//obavezno resetiraj
		optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
		handleOption(value);
	}
	
	private void recursivelyAddKeyListener(Component comp) {
		System.out.println("Added component: " + comp);
		comp.addKeyListener(dialogKeyListener);
		if(comp instanceof Container) {
			Container cont = (Container) comp;
			addContainerListener(recursiveContainerListener);
			Component[] children = cont.getComponents();
			for(int i = 0; i < children.length; ++i)
				recursivelyAddKeyListener(children[i]);
		}
	}
	
	private void recursivelyRemoveKeyListener(Component comp) {
		System.out.println("Removed component: " + comp);
		comp.removeKeyListener(dialogKeyListener);
		if(comp instanceof Container) {
			Container cont = (Container) comp;
			removeContainerListener(recursiveContainerListener);
			Component[] children = cont.getComponents();
			for(int i = 0; i < children.length; ++i)
				recursivelyRemoveKeyListener(children[i]);
		}
	}
	
	private class RecursiveContainerListener extends ContainerAdapter {
		@Override
		public void componentAdded(ContainerEvent e) {
			if(dialogKeyListener != null)
				recursivelyAddKeyListener(e.getChild());
		}
		@Override
		public void componentRemoved(ContainerEvent e) {
			if(dialogKeyListener != null)
				recursivelyRemoveKeyListener(e.getChild());
		}
	}
	
	@Override
	public void dispose() {
		if(dialogKeyListener != null)
			recursivelyRemoveKeyListener(this);
		super.dispose();
	}
	
	@Override
	public void setVisible(boolean b) {
		moveToCenter();
		super.setVisible(b);
	}
	
	private void moveToCenter() {
		final double eps = 1e-9;
		double screenWidth = Toolkit.getDefaultToolkit().getScreenSize()
		.getWidth();
		double screenHeigth = Toolkit.getDefaultToolkit().getScreenSize()
		.getHeight();
		if(invokerFrame != null) { //if invoker frame actually exists
			int x = (2*invokerFrame.getX() + invokerFrame.getWidth()
					- getSize().width)/2;
			int y = (2*invokerFrame.getY() + invokerFrame.getHeight()
					- getSize().height)/2;
			//ako bi bio potupno izvan ekrena, vrati ga na rub
			if(x < 0) x = 0;
			else if(x >= screenWidth)
				x = (int) (screenWidth - getWidth() + eps);
			if(y < 0) y = 0;
			else if(y >= screenHeigth)
				y = (int) (screenHeigth - getHeight() + eps);
			//postavi po mogucnosti u sredinu tj. na rub ako bi bio izvan
			setLocation(x, y);
		} else { //otherwise, move it to center
			int x = (int) (screenWidth - getWidth() + eps)/2;
			int y = (int) (screenHeigth - getHeight()+eps)/2;
			setLocation(x, y);
		}
	}
	
	private void searchForOptionButtons(Component comp) {
		if(comp instanceof JButton) {
			JButton button = (JButton) comp;
			if(button.getName() != null && button.getName().equals("OptionPane.button")) {
	//			System.out.println("Found button: (name = " + button.getName() 
		//			+ ", text = " + button.getText());
				optionButtons.add(button);
			}
		}
		if(comp instanceof Container) {
			Container cont = (Container) comp;
			Component[] children = cont.getComponents();
			for(int i = 0; i < children.length; ++i)
				searchForOptionButtons(children[i]);
		}
	}
	
	/**
	 * This method must handle all possible options.<br>
	 * If a dialog is closed by clicking on "X", <code>option</code> will
	 * be equal to {@link JOptionPane#CLOSED_OPTION}.
	 * @param option option chosen by the user
	 * @see JOptionPane
	 */
	protected abstract void handleOption(Object option);
	
	/**
	 * Returns the button which represents the <code>n</code>-th option.
	 * @param n 0-based index
	 * @return the corresponding button
	 */
	protected JButton getOptionButton(int n) {
		return optionButtons.get(n);
	}
	
	/**
	 * Returns all buttons which represent an option.
	 * @return the list of option buttons
	 */
	protected List<JButton> getOptionButtons() {
		return optionButtons;
	}
	
	/**
	 * This method is called when the user tries to close the dialog
	 * (or choose Cancel option if it exists).
	 * All validation and control should be done here, i.e.
	 * ask for confirmation, save state etc..
	 */
	protected abstract void onClose();
	
	/**
	 * Default action for closing dialog.
	 * The {@link MyDialog#onClose()} is called when this action is
	 * performed.
	 * 
	 * @author Leo Osvald
	 *
	 */
	protected class ActionClose extends AbstractAction {
		private static final long serialVersionUID = -3704794791524647249L;

		public ActionClose() {
			super("closeDialog", null);
		}
		
		public ActionClose(String name, Icon icon) {
			super(name, icon);
		}
		
		public ActionClose(String name, Icon icon,
				String shortDescription, int mnemonicKey) {
			super(name, icon);
			putValue(SHORT_DESCRIPTION, shortDescription);
	        putValue(MNEMONIC_KEY, mnemonicKey);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			onClose();
		}
	}
	
}
