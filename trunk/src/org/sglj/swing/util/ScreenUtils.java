/*
 * ScreenUtils.java
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

package org.sglj.swing.util;

import java.awt.Dimension;
import java.awt.Toolkit;

public class ScreenUtils {
	private ScreenUtils() {
	}
	
	/**
	 * Returns the closest valid width.
	 * @param preferredWidth preferred width
	 * @return width
	 */
	public static int getValidWidth(int preferredWidth) {
		if(preferredWidth < 0)
			return 0;
		return Math.min(preferredWidth, Toolkit.getDefaultToolkit()
				.getScreenSize().width);
	}
	
	/**
	 * Returns the closest valid height.
	 * @param preferredHeight preferred height
	 * @return height
	 */
	public static int getValidHeight(int preferredHeight) {
		if(preferredHeight < 0)
			return 0;
		return Math.min(preferredHeight, Toolkit.getDefaultToolkit()
				.getScreenSize().height);
	}
	
	/**
	 * Return the closest valid dimension.
	 * @param preferredDimension preferred dimension (width, height)
	 * @return dimension
	 */
	public static Dimension getValidDimension(Dimension preferredDimension) {
		return new Dimension(getValidWidth(preferredDimension.width), 
				getValidHeight(preferredDimension.height));
	}
	
	/**
	 * Checks whether the specified dimension can be displayed
	 * on the screen.
	 * @param dimension dimension to be checked for validity 
	 * @return <code>true</code> if it is, <code>false</code> otherwise.
	 */
	public static boolean isValid(Dimension dimension) {
		return dimension.width >= 0 && dimension.height >= 0
		&& dimension.width < Toolkit.getDefaultToolkit()
		.getScreenSize().width
		&& dimension.height < Toolkit.getDefaultToolkit()
		.getScreenSize().height;
	}
	
}
