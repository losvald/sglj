/*
 * Message.java
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

import java.util.Calendar;

/**
 * Message. Each message has its contents (text), type, and timestamp.
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public class Message {
	
	private String text;
	private MessageType type;
	private long timestamp;
	
	/**
	 * Constructor.<br>
	 * Constructs message with specified text, type and timestamp.
	 * @param text message text
	 * @param type message type
	 */
	public Message(String text, MessageType type, long timestamp) {
		this.text = text;
		this.type = type;
		this.timestamp = timestamp;
	}
	
	/**
	 * Constructor.<br>
	 * Creates message with specified text and of specified type,
	 * whose <code>timestamp</code> is current time.
	 * @param text message text
	 * @param type message type
	 */
	public Message(String text, MessageType type) {
		this(text, type, Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * Returns message text.
	 * @return string containing message text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets message text.
	 * @param text text of the actual message.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns message type.
	 * @param type message type
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * Sets message type.
	 * @param type message type
	 */
	public void setType(MessageType type) {
		this.type = type;
	}

	/**
	 * Returns timestamp of the message. This is usually a time when the message
	 * was created or last updated.
	 * @return time in milliseconds
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets timestamp of the message.
	 * @param timestamp in milliseconds
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
