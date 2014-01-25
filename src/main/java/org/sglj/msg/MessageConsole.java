/*
 * MessageConsole.java
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

import java.util.List;


/**
 * 
 * <p>Interface that specifies behavior of the message console
 * - type of buffer which holds messages.</p>
 * <p>There are two ways to send a message to a message console:
 * <ul>
 * <li>Synchronously<br>
 * Messages can be sent in parts; a part of the message can be sent by 
 * calling method {@link #print(String, MessageType)}. To complete
 * the message, call {@link #endMessage(MessageType)} method; 
 * this will inform all message console views that a message is finished.
 * Example:
 * <pre>
 * console.print("Hello");
 * console.print(" world.");
 * console.endMessage({@link MessageType#MAIN});
 * </pre>
 * or, if message contains only one part, the
 * {@link #prepareAndPublish(String, MessageType)} method is preferred:
 * <pre>
 * console.prepareAndPublish("Hello world.", {@link MessageType#MAIN});
 * </pre>
 * </li>
 * <li>Asynchronously<br>
 * Messages are sent in parts; a part of the message can be sent by 
 * calling {@link #prepare(String, MessageType)} method. The difference
 * between this way and the previous is that parts are not immediately sent.
 * Instead, message of a certain type is constructed part by part, and
 * once the {@link #publish(MessageType)} method is called, 
 * all previously sent parts which belong to the message of that type
 * are be merged into a single which is then published.
 * Note that different types of messages can be prepared concurrently, 
 * as shown in the following example:
 * <pre>
 * console.prepare("Hello", {@link MessageType#MAIN});
 * console.prepare("This is my", {@link MessageType#ERROR});
 * console.prepare(" world.", {@link MessageType#MAIN});
 * console.publish({@link MessageType#MAIN});
 * console.prepare(" first application.", {@link MessageType#ERROR});
 * console.publish({@link MessageType#MAIN});</pre>
 * After execution of the code above, message console will contain the 
 * following two messages:
 * <pre>Hello world.</pre>
 * <pre>This is my first application.</pre>
 * </li>
 * </ul>
 * </p>
 * <p>Synchronous way provides read-time sending of messages to console,
 * which is preferred in situations where messages are send from only
 * one thread.<br>
 * However, if the messages are sent a console from multiple threads,
 * the, it is often desirable that a message is published as a whole
 * or not published at all, which favors sending messages in an asynchronous
 * way.</p>
 * <p><b>Note</b>: This interface does not guarantee that all
 * classes implementing it are thread-safe.</p>
 * 
 * @see MessageType
 * @see MessagePublisher
 * @see MessageHandler
 * 
 * @author Leo Osvald
 * @version 1.0
 */
public interface MessageConsole extends MessageHandler, MessagePublisher {
	/**
	 * Prints/sends a message to a console.<br>
	 * To mark a message and end it, the {@link #endMessage(MessageType)}
	 * method should be called.
	 * @param s one or more lines of text 
	 * @return {@link MessageConsole} which 
	 */
	MessageConsole print(String s);
	/**
	 * Finishes the message and publishes it as a specified type.
	 * This method must be called after one or more calls to
	 * {@link #print(String, MessageType)} method.
	 * @param type tip poruke
	 * @return
	 */
	MessageConsole endMessage(MessageType type);
	
	/** 
	 * Returns messages that this console contains, as a list, ordered
	 * by the time of arrival.
	 * @return list of messages
	 */
	List<Message> getMessages();
	
	/**
	 * Clears all messages from this console.
	 */
	void clear();
	
	/**
	 * Returns the number of messages that are currently stored
	 * by this console.
	 * @return number of messages that this console contains
	 */
	int getMessageCount();
	
	/**
	 * Returns maximum number of messages that this console
	 * can contain.
	 * @return maximum number of messages
	 */
	int getMessageCapacity();
	
	/**
	 * Sets maximum number of messages that this console
	 * can contain.
	 * @param maximumMessages number of messages
	 */
	void setMessageCapacity(int maximumMessages);
	
	/**
	 * Returns total number of characters that this console can store.
	 * @return number of characters (UTF-8)
	 */
	int getSize();
	
	/**
	 * Sets maximum number of characters that this console can contain.
	 * This indirectly affects the number of messages that this console
	 * can contain.
	 * @param maximumChars number of characters (UTF-8)
	 */
	void setCharCapacity(int maximumChars);
	
	/**
	 * Adds message console view for this console.
	 * @param consoleView view which displays contents of this console
	 */
	void addMessageConsoleView(MessageConsoleView consoleView);
	
	/**
	 * Removes message console view for this console.
	 * @param consoleView view which displays contents of this console
	 */
	void removeMessageConsoleView(MessageConsoleView consoleView);
}
