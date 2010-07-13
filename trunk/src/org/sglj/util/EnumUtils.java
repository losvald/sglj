/*
 * EnumUtils.java
 * 
 * Copyright 2010 Leo Osvald <leo.osvald@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.
 *  
 * You may obtain a copy of the License at 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */

package org.sglj.util;

/**
 * Utility razred za enumeracije
 * 
 * @author Leo Osvald
 */
public final class EnumUtils {
	/**
	 * Pretvara oblik ime iz oblika
	 * "SOME_CONSTANT" u oblik
	 * "SomeConstant".
	 * @param enumeration enumeracija
	 * @return ime
	 */
	public static String toClassName(Enum<?> enumeration) {
		String name = enumeration.name();
		StringBuffer s = new StringBuffer(name.length());
		boolean lastUnderscore = true;
		for(int i = 0; i < name.length(); ++i) {
			if(name.charAt(i) == '_') {
				lastUnderscore = true;
			}
			else {
				s.append(lastUnderscore ? name.charAt(i)
						: Character.toLowerCase(name.charAt(i)));
				lastUnderscore = false;
			}
		}
		return s.toString();
	}
}
