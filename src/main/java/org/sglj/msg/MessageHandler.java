/*
 * MessageHandler.java
 * 
 * Copyright (C) 2009 Leo Osvald <leo.osvald@gmail.com>
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

package org.sglj.msg;

/**
 * Message handler - provides a mechanism for receiving messages,
 * with filtering.
 * 
 * @author Leo Osvald
 * @version 0.8
 */
public interface MessageHandler {
	/**
	 * This method is usually called from a sender, 
	 * i.e. {@link MessagePublisher}.<br>
	 * If required, all filtering should be done here.
	 * @param message message that was received
	 */
	void messageReceived(Message message);
	
	/**
	 * Returns a message filter associated with this handler.
	 * @return filter message filter
	 */
	MessageFilter getMessageFilter();
	
	/**
	 * Sets filter for this handler.
	 * @param filter message filter
	 */
	void setMessageFilter(MessageFilter filter);
}
