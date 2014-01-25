/*
 * InfoBar.java
 * 
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
 * Copyright (C) 2010 Nikola Banić <nbanic@gmail.com>
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

package org.sglj.swing.util;

import javax.swing.JComponent;

/**
 * TODO
 * 
 * @author Leo Osvald
 * @author Nikola Banić
 */
public abstract class InfoBar extends JComponent {
	
	private static final long serialVersionUID = 1L;
	
	private final InfoBarHolder holder;
	private boolean shown;
	
	public InfoBar(InfoBarHolder infoBarHolder) {
		super();
		this.holder = infoBarHolder;
	}
	
	public InfoBarHolder getInfoBarHolder() {
		return holder;
	}
	
	public boolean showInfoBar() {
		createGUI();
		if(!holder.infoBarShowRequested(this)) {
			destroyGUI();
			return false;
		}
		shown = true;
		return true;
	}
	
	public boolean hideInfoBar() {
		destroyGUI();
		if(!holder.infoBarHideRequested(this)) {
			createGUI();
			return false;
		}
		shown = false;
		return true;
	}
	
	public boolean isShown() {
		return shown;
	}
	
	public abstract JComponent getComponent();
	
	protected abstract void createGUI();
	
	protected abstract void destroyGUI();
}
