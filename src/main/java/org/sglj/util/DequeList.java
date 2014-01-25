/*
 * DequeList.java
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
 */

package org.sglj.util;

import java.util.Deque;
import java.util.List;

/**
 * Extension of {@link List} interface which
 * guarantees that all deque operations are implemented
 * efficiently.<br>
 * In general, implementations should ensure that no
 * <code>null</code> values can be inserted.
 * 
 * @author Leo Osvald
 * @version 1.0
 * @param <E> type of elements
 */
public interface DequeList<E> extends Deque<E>, List<E> {
}
