/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.sglj.util;

/**
 * Utility class for converting hex Strings to bytes and vice-versa. 
 * 
 * @author Leo Osvald
 *
 */
public class HexStringUtils {

	private static final String HEXES = "0123456789ABCDEF";

	public static String toHexString(byte[] bytes) {
		if (bytes == null)
			return null;
		
		final StringBuilder hex = new StringBuilder(2 * bytes.length);
		for (final byte b : bytes) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4))
			.append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}

	public static byte[] toByteArray(String hexString) {
		if (hexString == null)
			return null;

		byte[] b = new byte[hexString.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(hexString.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}
	
}
