/*
 * NumberUtils.java
 *
 * Copyright (C) 2010 Leo Osvald <leo.osvald@gmail.com>
 *
 * This file is part of YOUR PROGRAM NAME.
 * 
 * YOUR PROGRAM NAME is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * YOUR PROGRAM NAME is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with YOUR PROGRAM NAME. If not, see <http://www.gnu.org/licenses/>.
 */

package org.sglj.math;

import java.math.BigDecimal;

/**
 * TODO
 * 
 * @author Leo Osvald
 *
 */
public class NumberUtils {
	
	private NumberUtils() {
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Number> T negate(Class<T> clazz, T x) {
		if (clazz == Double.class) {
			return (T) Double.valueOf(-x.doubleValue());
		}
		if (clazz == Float.class)
			return (T) Float.valueOf(-x.floatValue());
		if (clazz == Integer.class)
			return (T) Integer.valueOf(-x.intValue());
		if (clazz == BigDecimal.class)
			return (T) ((BigDecimal) x).negate();
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Number> T sum(Class<T> clazz,
			T a, T b) {
		if (clazz == Double.class)
			return (T) Double.valueOf(a.doubleValue() + b.doubleValue());
		if (clazz == Float.class)
			return (T) Float.valueOf(a.floatValue() + b.floatValue());
		if (clazz == Integer.class)
			return (T) Integer.valueOf(a.intValue() + b.intValue());
		if (clazz == BigDecimal.class)
			return (T) ((BigDecimal) a).add((BigDecimal) b);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Number> T sum(Class<T> clazz,
			T a, T b, T c, T... arr) {
		if (clazz == Double.class) {
			double r = a.doubleValue() + b.doubleValue()
			+ c.doubleValue();
			for (T x : arr)
				r += x.doubleValue();
			return (T) Double.valueOf(r);
		}
		if (clazz == Float.class) {
			float r = a.floatValue() + b.floatValue()
					+ c.floatValue();
			for (T x : arr)
				r += x.floatValue();
			return (T) Float.valueOf(r);	
		}
		if (clazz == Integer.class) {
			int r = a.intValue() + b.intValue()
					+ c.intValue();
			for (T x : arr)
				r += x.intValue();
			return (T) Integer.valueOf(r);
		}
		if (clazz == BigDecimal.class) {
			BigDecimal r = ((BigDecimal) a).add((BigDecimal) b)
			.add((BigDecimal) c);
			for (T x : arr) {
				r = r.add((BigDecimal) x);
			}
			return (T) r;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Number> T sub(Class<T> clazz,
			T a, T b) {
		if (clazz == Double.class)
			return (T) Double.valueOf(a.doubleValue() - b.doubleValue());
		if (clazz == Float.class)
			return (T) Float.valueOf(a.floatValue() - b.floatValue());
		if (clazz == Integer.class)
			return (T) Integer.valueOf(a.intValue() - b.intValue());
		if (clazz == BigDecimal.class)
			return (T) ((BigDecimal) a).subtract((BigDecimal) b);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Number> T sub(Class<T> clazz,
			T a, T b, T c, T... arr) {
		if (clazz == Double.class) {
			double r = a.doubleValue() + b.doubleValue()
			+ c.doubleValue();
			for (T x : arr)
				r += x.doubleValue();
			return (T) Double.valueOf(r);
		}
		if (clazz == Float.class) {
			float r = a.floatValue() - b.floatValue()
					- c.floatValue();
			for (T x : arr)
				r -= x.floatValue();
			return (T) Float.valueOf(r);	
		}
		if (clazz == Integer.class) {
			int r = a.intValue() - b.intValue()
					- c.intValue();
			for (T x : arr)
				r -= x.intValue();
			return (T) Integer.valueOf(r);
		}
		if (clazz == BigDecimal.class) {
			BigDecimal r = ((BigDecimal) a).subtract((BigDecimal) b)
			.subtract((BigDecimal) c);
			for (T x : arr) {
				r = r.subtract((BigDecimal) x);
			}
			return (T) r;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Number> T mult(Class<T> clazz,
			T a, T b) {
		if (clazz == Double.class)
			return (T) Double.valueOf(a.doubleValue() * b.doubleValue());
		if (clazz == Float.class)
			return (T) Float.valueOf(a.floatValue() * b.floatValue());
		if (clazz == Integer.class)
			return (T) Integer.valueOf(a.intValue() * b.intValue());
		if (clazz == BigDecimal.class)
			return (T) ((BigDecimal) a).multiply((BigDecimal) b);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Number> T mult(Class<T> clazz,
			T a, T b, T c, T... arr) {
		if (clazz == Double.class) {
			double r = a.doubleValue() * b.doubleValue()
			* c.doubleValue();
			for (T x : arr)
				r *= x.doubleValue();
			return (T) Double.valueOf(r);
		}
		if (clazz == Float.class) {
			float r = a.floatValue() * b.floatValue()
					* c.floatValue();
			for (T x : arr)
				r *= x.floatValue();
			return (T) Float.valueOf(r);	
		}
		if (clazz == Integer.class) {
			int r = a.intValue() * b.intValue()
					* c.intValue();
			for (T x : arr)
				r *= x.intValue();
			return (T) Integer.valueOf(r);
		}
		if (clazz == BigDecimal.class) {
			BigDecimal r = ((BigDecimal) a).multiply((BigDecimal) b)
			.multiply((BigDecimal) c);
			for (T x : arr) {
				r = r.multiply((BigDecimal) x);
			}
			return (T) r;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Number> T div(Class<T> clazz,
			T a, T b) {
		if (clazz == Double.class)
			return (T) Double.valueOf(a.doubleValue() / b.doubleValue());
		if (clazz == Float.class)
			return (T) Float.valueOf(a.floatValue() / b.floatValue());
		if (clazz == Integer.class)
			return (T) Integer.valueOf(a.intValue() / b.intValue());
		if (clazz == BigDecimal.class)
			return (T) ((BigDecimal) a).divide((BigDecimal) b);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Number> T div(Class<T> clazz,
			T a, T b, T c, T... arr) {
		if (clazz == Double.class) {
			double r = a.doubleValue() / b.doubleValue()
			/ c.doubleValue();
			for (T x : arr)
				r /= x.doubleValue();
			return (T) Double.valueOf(r);
		}
		if (clazz == Float.class) {
			float r = a.floatValue() / b.floatValue()
					/ c.floatValue();
			for (T x : arr)
				r /= x.floatValue();
			return (T) Float.valueOf(r);	
		}
		if (clazz == Integer.class) {
			int r = a.intValue() / b.intValue()
					/ c.intValue();
			for (T x : arr)
				r /= x.intValue();
			return (T) Integer.valueOf(r);
		}
		if (clazz == BigDecimal.class) {
			BigDecimal r = ((BigDecimal) a).divide((BigDecimal) b)
			.divide((BigDecimal) c);
			for (T x : arr) {
				r = r.divide((BigDecimal) x);
			}
			return (T) r;
		}
		return null;
	}
	
}
