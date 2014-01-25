/*
 * DefaultMessagePublisher.java
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
import java.util.Vector;

/**
 * Thread-safe implementation of {@link MessagePublisher}.
 *
 *
 * @author Leo Osvald
 *
 */
public class DefaultMessagePublisher implements MessagePublisher {

	private final Vector<MessageHandler> handlers = new Vector<MessageHandler>();

	private final StringBuffer[] messages;

	public DefaultMessagePublisher(int typeCount) {
		messages = new StringBuffer[typeCount];
		for(int i = 0; i < messages.length; ++i) {
			messages[i] = new StringBuffer();
		}
	}

	@Override
	public boolean cancel(MessageType type) {
		synchronized (type) {
			return StringBufferUtils.clear(messages[type.ordinal()]);
		}
	}

	@Override
	public int getBufferSize(MessageType type) {
		//TODO
		return 0;
	}

	@Override
	public MessagePublisher prepare(String chunk, MessageType type)
			throws IndexOutOfBoundsException {
		synchronized (type) {
			messages[type.ordinal()].append(chunk);
		}
		return this;
	}

	@Override
	public MessagePublisher prepareAndPublish(String msg, MessageType type) {
		informHandlers(new Message(msg,
				type, Calendar.getInstance().getTimeInMillis()));
		return this;
	}

	@Override
	public boolean publish(MessageType type) {
		if(messages[type.ordinal()].length() == 0) return false;
		synchronized (type) {
			Message message = new Message(messages[type.ordinal()].toString(),
				type, Calendar.getInstance().getTimeInMillis());
			informHandlers(message);
			StringBufferUtils.clear(messages[type.ordinal()]);
		}
		return true;
	}

	@Override
	public void addMessageHandler(MessageHandler handler) {
		handlers.add(handler);
	}

	@Override
	public void removeMessageHandler(MessageHandler handler) {
		handlers.remove(handler);
	}

	private synchronized void informHandlers(Message msg) {
		for(int i = 0; i < handlers.size(); ++i)
			handlers.get(i).messageReceived(msg);
	}


	protected static class StringBufferUtils {
		public static boolean clear(StringBuffer buff) {
			int len = buff.length();
			buff.delete(0, len);
			return len > 0;
		}
	}

}
