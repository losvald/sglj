/*
 * PlainMessageConsole.java
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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 
 * <p>Plain console with only one buffer which stores all messages,
 * regardless of their type.</p>
 * <p>This implementation is efficient and on average it takes constant time 
 * to insert a message, which is only affected by the length of the message, 
 * but not the buffer size.</p> 
 * <p>This implementation is <b>thread-safe</b></p>.
 * 
 * @author Leo Osvald
 * @version 0.952
 */
public class PlainMessageConsole implements MessageConsole {

	private final MessagePublisher msgPublisher;
	
	private final Vector<MessageConsoleView> views = new Vector<MessageConsoleView>();
	
	private MessageFilter msgFilter = new MessageFilter() {
		@Override
		public boolean accept(Message message) {
			return true;
		}
	};
	
	private final Vector<Message> buffer = new Vector<Message>();
	
	private StringBuffer currMessage = new StringBuffer();
	private boolean isMessagePending;
	
	private int messageCount = 0;
	private int charCount = 0;
	
	private int messageCapacity;
	private int charCapacity;
	
	private static final int DEFAULT_MESSAGE_CAPACITY = 100;
	
	public PlainMessageConsole(int typeCount, int messageCapacity) {
		this.messageCapacity = messageCapacity;
		msgPublisher = new DefaultMessagePublisher(typeCount);
		msgPublisher.addMessageHandler(this);
	}
	
	public PlainMessageConsole(int typeCount) {
		this(typeCount, DEFAULT_MESSAGE_CAPACITY);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMessageHandler(MessageHandler handler) {
		msgPublisher.addMessageHandler(handler);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean cancel(MessageType type) {
		return msgPublisher.cancel(type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		buffer.clear();
		messageCount = 0;
		informRefresh();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageConsole endMessage(MessageType type) {
		messageReceived(new Message(currMessage.toString(), type));
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMessageCapacity() {
		return messageCapacity;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMessageCount() {
		return buffer.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageFilter getMessageFilter() {
		return msgFilter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	synchronized public List<Message> getMessages() {
		//TODO ovo treba pametnije izvest mozda
		return new ArrayList<Message>(buffer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize() {
		return charCount;
	}
	
	/**
	 * Ova metoda sluzi samo za interno pozivanje.<br>
	 * Vanjsko pozivanje je zabranjeno jer poruka ne prolazi kroz filter.
	 */
	@Override
	synchronized public void messageReceived(Message message) {
		isMessagePending = false;
		if(!msgFilter.accept(message)) return ;
		
		buffer.add(message);
		messageCount++;
		charCount += message.getText().length();
		
		boolean wasOverflow = false;
		
		if(messageCapacity != 0 && messageCount >= 2*messageCapacity) {
			buffer.subList(0, messageCapacity).clear();
			messageCount -= messageCapacity;
			wasOverflow = true;
		}
		if(charCapacity != 0 && charCount > 2*charCapacity) {
			int cnt = 0;
			for(int i = 0; i < buffer.size()-1; ++i) {
				cnt += buffer.get(i).getText().length();
				if(cnt > charCapacity) {
					charCount -= charCapacity;
					buffer.subList(0, i+1).clear();
					wasOverflow = true;
					break;
				}
			}
		}
		
//		System.out.println("Total msgs: " + buffer.size() + "charCount: " + charCount);
		
		if(wasOverflow) informRefresh();
		else informReceived(message);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMessageFilter(MessageFilter filter) {
		this.msgFilter = filter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageConsole prepare(String chunk, MessageType type)
			throws IndexOutOfBoundsException {
		msgPublisher.prepare(chunk, type);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageConsole prepareAndPublish(String msg, MessageType type) {
		messageReceived(new Message(msg, type));
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	synchronized public MessageConsole print(String s) {
		if(!isMessagePending) beginMessage();
		currMessage.append(s);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	synchronized public boolean publish(MessageType type) {
		return msgPublisher.publish(type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeMessageHandler(MessageHandler handler) {
		msgPublisher.addMessageHandler(handler);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCharCapacity(int maximumChars) {
		this.charCapacity = maximumChars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMessageCapacity(int maximumMessages) {
		this.messageCapacity = maximumMessages;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMessageConsoleView(MessageConsoleView consoleView) {
		views.add(consoleView);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeMessageConsoleView(MessageConsoleView consoleView) {
		views.remove(consoleView);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getBufferSize(MessageType type) {
		return -1;
	}
	
	synchronized private void beginMessage() {
		isMessagePending = true;
		currMessage.delete(0, currMessage.length());
	}
	
	synchronized private void informRefresh() {
		for(int i = 0; i < views.size(); ++i)
			views.get(i).refresh(this);
	}
	
	synchronized private void informReceived(Message message) {
		for(int i = 0; i < views.size(); ++i)
			views.get(i).messageReceived(message);
	}

}
