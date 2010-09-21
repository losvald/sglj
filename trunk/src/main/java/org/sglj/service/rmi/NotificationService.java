/*
 * NotificationService.java
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

package org.sglj.service.rmi;

/**
 * Marker interface for the remote notification services.
 * Must be implemented by both implementations of remote notification services
 * and stubs.<br>
 * All implemented methods annotated with the {@link RemoteService} annotation
 * must have a <code>void</code> return type.
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public interface NotificationService extends RemoteService {
}
