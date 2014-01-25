/*
 * MessagePublisher.java
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
 * Classes implementing this interface provide a way of publishing messages.
 * First, a message is constructed part by part using
 * {@link #prepare(String, MessageType)} method. Once the message is 
 * constructed, it can be published by calling {@link #publish(MessageType)}
 * method which then publishes the message.
 * The published message is then caught by one or more handlers (a class 
 * implementing {@link MessageHandler} interface).<br>
 * During preparation, but before being published,
 * a message can be at any time canceled, by calling
 * {@link #cancel(MessageType)} method, respectively. 
 * 
 * <p><b>Note</b>: This interface does not guarantee that all
 * classes implementing it are thread-safe.</p>
 * 
 * @see MessageHandler
 * @see MessageType
 *  
 * @author Leo Osvald
 * @version 1.0
 */
public interface MessagePublisher {
	/**
	 * Prepares text as a part of the message.
	 * Successive calls to this methods, constructs a message, chunk by chunk.
	 * Calling {@link #publish(MessageType)} method merges all prepared
	 * chunks for specified message type into a single message which is 
	 * then published.
	 * @param chunk part of the message
	 * @param type type of the message which this part belong to
	 * @return this message publisher
	 * @throws IndexOutOfBoundsException if the current message prepared
	 * for publishing would be too large to fit into a buffer.  
	 */
	MessagePublisher prepare(String chunk, MessageType type) 
	throws IndexOutOfBoundsException;
	
	/**
	 * Merges all prepared chunks of a message for given type and
	 * publishes it as a message of that type.
	 * Message is send to all registered message handlers.
	 * @param type message type
	 * @return <code>true</code> if message consists of at
	 * least one part. If there are no parts, nothing is published and
	 * <code>false</code> is returned. 
	 */
	boolean publish(MessageType type);
	
	/**
	 * Cancels the prepared message. If message of given type has not 
	 * yet been prepared (or has been already published), nothing happens.
	 * @param type message type which preparation should be canceled
	 * @return <code>true</code> if message of specified type was
	 * indeed canceled, <code>false</code> otherwise.
	 */
	boolean cancel(MessageType type);
	
	/**
	 * Prepares and publishes specified text as a message.
	 * This is the preferred way of publishing one-chunk messages
	 * in contrast to calling two methods like:
	 * <pre>
	 * {@link #prepare(String, MessageType)}.{@link #publish(MessageType)}
	 * </pre>
	 * as this eliminates the need of a buffer and is, thus, more efficient.
	 * @param msgText message text
	 * @param type message type
	 * @return this message publisher
	 */
	MessagePublisher prepareAndPublish(String msgText, MessageType type);
	
	/**
	 * Adds new message handler which "catches" published messages.
	 * @param handler message handler
	 */
	public void addMessageHandler(MessageHandler handler);
	
	/**
	 * Removes message handler.
	 * @param handler message handler
	 */
	public void removeMessageHandler(MessageHandler handler);
	
	/**
	 * Returns total size of the buffer.
	 * This corresponds to the maximum number of characters a message can 
	 * consist of (in order to be published).
	 * @return number of characters
	 */
	int getBufferSize(MessageType type);
}
