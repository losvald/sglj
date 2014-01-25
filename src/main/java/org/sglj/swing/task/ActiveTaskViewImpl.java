/*
 * ActiveTaskViewImpl.java
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

package org.sglj.swing.task;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.sglj.swing.task.SingleTaskModel.SingleTaskModelListener;
import org.sglj.task.DeterminateTask;
import org.sglj.task.Task;
import org.sglj.task.TaskEvent;


/**
 * This implementation of interface {@link SingleTaskView} displays
 * active tasks such that only one is displayed at a time, but it
 * in a cyclic order. The change of currently displayed task
 * occurs at fixed time periods, and can be adjusted through
 * {@link #setDisplayDuration(int)} method.
 * 
 * @author Leo Osvald
 * @version 1.03
 */
public class ActiveTaskViewImpl extends JPanel 
implements SingleTaskView, SingleTaskModelListener {

	private SingleTaskModel model;
	private NameTextProvider nameTextCreator;
	private StatusTextProvider statusTextCreator;

	private Task activeTask;
	private Queue<WeakReference<Task>> displayCycle = new LinkedList<WeakReference<Task>>();	

	private Timer timer;

	private JLabel nameLabel = new JLabel();
	private JProgressBar progressBar;
	private JLabel statusLabel = new JLabel();

	private JPanel nameStatusPanel;
	private final JSeparator nameStatusSeparator = new JSeparator(SwingConstants.VERTICAL);
	private final Component nameStatusGlue = Box.createGlue();

	private boolean taskNameDisplayed = true;
	private boolean taskProgressDisplayed = true;
	private boolean taskStatusDisplayed = true;

	private int displayDuration = DEFAULT_DISPLAY_DURATION;

	/**
	 * Default width of the progress bar
	 */
	public static final int DEFAULT_PROGRESS_BAR_WIDTH = 50;

	/** minimum active task display duration in ms */
	public static final int MIN_DISPLAY_DURATION = 500;
	
	/** default active task display duration in ms */
	private static final int DEFAULT_DISPLAY_DURATION = 2000;
	
	private static final int PB_MAX_VAL = 1000;

	private static final long serialVersionUID = 1L;

	public ActiveTaskViewImpl(NameTextProvider nameTextCreator,
			StatusTextProvider statusTextCreator, int progressBarWidth) {
		setNameProvider(nameTextCreator);
		setStatusTextProvider(statusTextCreator);
	
		this.setLayout(new BorderLayout());
		
		FontMetrics fm = getFontMetrics(getFont());
		Dimension dim = new Dimension(0, fm.getHeight()+fm.getDescent());
		nameLabel.setMinimumSize(dim);
		nameLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		statusLabel.setMinimumSize(dim);
		statusLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);

		progressBar = new JProgressBar(0, PB_MAX_VAL);
		progressBar.setPreferredSize(new Dimension(progressBarWidth, 0));
		progressBar.setVisible(false);

		nameStatusPanel = new JPanel();
		nameStatusPanel.setLayout(new BoxLayout(nameStatusPanel, BoxLayout.X_AXIS));
		updateNameStatusPanel();

		this.add(nameStatusPanel, BorderLayout.CENTER);
		this.add(progressBar, BorderLayout.EAST);

		timer = new Timer(DEFAULT_DISPLAY_DURATION, new ActionListener() {
//			int cnt = 0;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!displayCycle.isEmpty()) {
//					System.out.println("Displaying next"+(cnt++));
					displayNext();
				}
			}
		});
		timer.setInitialDelay(0);

		//ensure that timer is not running when component is hidden, and vice-versa
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				timer.start();
			}
			@Override
			public void componentHidden(ComponentEvent e) {
				timer.stop();
			}
		});
	}

	public ActiveTaskViewImpl(NameTextProvider nameTextCreator,
			StatusTextProvider statusTextCreator) {
		this(nameTextCreator, statusTextCreator, DEFAULT_PROGRESS_BAR_WIDTH);
	}

	@Override
	public SingleTaskModel getModel() {
		return model;
	}

	@Override
	public void setModel(SingleTaskModel model) {
		timer.stop();
		displayCycle.clear();

		//disconnect this view from old model
		if(this.model != null)
			this.model.removeSingleTaskModelListener(this);
		//set new model
		this.model = model;
		model.addSingleTaskModelListener(this);

		if(model.getTask() != null && model.getTask().getState()
				== Task.TaskState.STARTED) {
			displayCycle.add(new WeakReference<Task>(model.getTask()));
//			System.out.println("Added to display cycle task: "+model.getTask().name());
//			System.out.println("Task by ref: "+displayCycle.peek().get().name());
		}
		else {
//			System.out.println("setModel: is task null? " + (model.getTask() == null));
		}

		timer.start();
	}

	@Override
	public void taskStateChanged(TaskEvent e) {
		Task task = e.getTask();
		if(task == null)
			return;

//		System.out.println("ActiveTaskViewImpl - STATE CHANGED");
//		System.out.println("New state is: "+task.getState().name());

		//if a task just started, add it to display cycle
		if(task.getState() == Task.TaskState.STARTED) {
			displayCycle.add(new WeakReference<Task>(task));
			//if there was no task in the display cycle, display it immediately
			if(displayCycle.size() == 1)
				timer.restart();
		}
		//if it is active and is not active, display next (it will be removed)
		else if(task == activeTask) {
//			System.out.println("COMPLETED OR CANCELLED");
			displayNext();
		}
	}

	@Override
	public void taskChanged(TaskEvent e) {
		Task task = e.getTask();
		if(task == activeTask
				|| activeTask == null && task != null)
			updateAll(task);
	}

	@Override
	public void taskUpdated(TaskEvent e) {
		Task task = e.getTask();
		if(task == null)
			return ;
		if(task == activeTask || activeTask == null) {
			if(task.getState() == Task.TaskState.STARTED) {
//				System.out.println("Updating ActiveTaskViewImpl: task is null?"
//						+ (task == null));
				updateAll(task);
			}
		}
	}

	@Override
	public void taskStatusChanged(TaskEvent e) {
		if(!taskStatusDisplayed)
			return ;
		Task task = e.getTask();
		if(task == null)
			return ;
		if(task == activeTask || activeTask == null)
			updateStatus(task);
	}

	public Task getDislayedTask() {
		return activeTask;
	}

	/**
	 * Sets active task display duration. This only matters when there are
	 * more than one active tasks; in that case, they are displayed
	 * one at a time, in cyclic order, and each one is displayed exactly
	 * <code>displayDuration</code> milliseconds.
	 * @param displayDuration time period in ms
	 * @throws IndexOutOfBoundsException if specified period is too short
	 * - see {@link #MIN_DISPLAY_DURATION}
	 */
	public void setDisplayDuration(int displayDuration)
	throws IndexOutOfBoundsException {
		//ensure that it meets restrictions
		if(displayDuration < MIN_DISPLAY_DURATION)
			throw new IndexOutOfBoundsException("Display duration too short.");
		//set the new display duration
		this.displayDuration  = displayDuration;
		timer.setDelay(displayDuration);
	}

	public int getDisplayDuration() {
		return displayDuration;
	}

	@Override
	public boolean isTaskNameDisplayed() {
		return taskNameDisplayed;
	}

	@Override
	public boolean isTaskProgressDisplayed() {
		return taskNameDisplayed;
	}

	@Override
	public boolean isTaskStatusDisplayed() {
		return taskStatusDisplayed;
	}

	@Override
	public void setTaskNameDisplayed(boolean enabled) {
		boolean hasChanged = this.taskNameDisplayed ^ enabled;
		this.taskNameDisplayed = enabled;
		if(hasChanged)
			updateNameStatusPanel();
	}

	@Override
	public void setTaskProgressDisplayed(boolean enabled) {
//		System.out.println("Set progress displayed("+enabled+")");
		boolean hasChanged = this.taskProgressDisplayed ^ enabled;
		this.taskProgressDisplayed = enabled;
		if(hasChanged)
			updateProgressDisplayed();
	}

	@Override
	public void setTaskStatusDisplayed(boolean enabled) {
		boolean hasChanged = this.taskStatusDisplayed ^ enabled;
		this.taskStatusDisplayed = enabled;
		if(hasChanged)
			updateNameStatusPanel();
	}

	@Override
	public NameTextProvider getNameTextProvider() {
		return nameTextCreator;
	}

	@Override
	public StatusTextProvider getStatusTextProvider() {
		return statusTextCreator;
	}

	@Override
	public void setStatusTextProvider(StatusTextProvider statusTextCreator) {
		this.statusTextCreator = statusTextCreator;
	}

	@Override
	public void setNameProvider(NameTextProvider nameTextCreator) {
		this.nameTextCreator = nameTextCreator;
	}
	
	private void updateNameStatusPanel() {
		nameStatusPanel.remove(nameLabel);
		nameStatusPanel.remove(nameStatusSeparator);
		nameStatusPanel.remove(statusLabel);
		nameStatusPanel.remove(nameStatusGlue);
		if(taskNameDisplayed) {
			nameStatusPanel.add(nameLabel);
			if(taskStatusDisplayed)
				nameStatusPanel.add(nameStatusSeparator);
		}
		if(taskStatusDisplayed)
			nameStatusPanel.add(statusLabel);

		nameStatusPanel.add(nameStatusGlue);
		this.validate();
	}

	private void updateProgressDisplayed() {
		this.remove(progressBar);
		if(taskProgressDisplayed) {
			this.add(progressBar, BorderLayout.EAST);
//			System.out.println("Added progressbar");
//			for(Component c : this.getComponents())
//				System.out.println(c);
		}
		this.validate();
	}

	void printTasks() {
		int size = displayCycle.size();
		System.out.println("---Printing "+size+" tasks");
		for(int i = 0; i < size; ++i) {
			WeakReference<Task> ref = displayCycle.poll();
			Task t = ref.get();
			System.out.printf("Task#%d: %s %d\n", i, (t != null ? t.name() : "-"),
					(ref.isEnqueued() ? 1 : 0));
			displayCycle.add(ref);
		}
		System.out.println();
	}

	private void displayNext() {
		//cycle until there are actually references to running tasks
		Task task = null;
		while(!displayCycle.isEmpty()) {
//			printTasks();
			WeakReference<Task> front = displayCycle.poll();
			if(!front.isEnqueued()) //if task is still alive 
				displayCycle.add(front); //rotate list
			//keep cycling if task is not alive
			if(displayCycle.peek().isEnqueued())
				continue;
			task = displayCycle.peek().get();
			if(task != null && task.getState() == Task.TaskState.STARTED)
				break;
			//if task is somehow null or is not started yet, remove it
			displayCycle.remove();
		}
		if(displayCycle.isEmpty())
			task = null;
		setActive(task);
	}

	private void setActive(Task task) {
//		System.out.println("SETTING ACTIVE: "+task);
		activeTask = task;
		updateAll(task);
	}

	private void updateProgress(Task task) {
		if(task == null) {
			progressBar.setVisible(false);
			return ;
		}
		else if(task == activeTask) {
			if(task instanceof DeterminateTask) {
				progressBar.setValue((int)(((DeterminateTask)task)
						.getProgress()*PB_MAX_VAL));
				progressBar.setIndeterminate(false);
				progressBar.setStringPainted(true);
			}
			else {
				progressBar.setString(null);
				progressBar.setIndeterminate(true);
				progressBar.setStringPainted(false);
			}
			progressBar.setVisible(true);
		}
	}

	private void updateName(Task task) {
		if(task != null)
			nameLabel.setText(task.name() != null ? task.name() : "");
		else
			nameLabel.setText(null);
	}

	private void updateStatus(Task task) {
		if(task == null)
			statusLabel.setText(null);
		else if(task.getStatus() != null) {
			String text = statusTextCreator.statusDescription(task);
			statusLabel.setText(text != null ? text : "");
		}
	}

	private void updateState(Task task) {
		if(task != null && task.getState() != Task.TaskState.NOT_STARTED) {
			progressBar.setValue(0);
			progressBar.setVisible(true);
		}
		else {
			progressBar.setVisible(false);
		}
	}

	private void updateAll(Task task) {
		updateState(task);
		if(taskNameDisplayed)
			updateName(task);
		if(taskStatusDisplayed)
			updateStatus(task);
		if(taskProgressDisplayed)
			updateProgress(task);
	}

}