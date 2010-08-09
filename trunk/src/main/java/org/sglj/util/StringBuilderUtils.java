package org.sglj.util;


/**
 * Utility methods for the {@link StringBuilder} class.
 * 
 * @author Leo Osvald
 * @version 0.1
 */
public abstract class StringBuilderUtils {

	/**
     * Tests if this string starts with the specified prefix.
     *
     * @param prefix the prefix.
     * @return  <code>true</code> if the character sequence represented by the
     *          argument is a prefix of the character sequence represented by
     *          this string; <code>false</code> otherwise.
     *          Note also that <code>true</code> will be returned if the
     *          argument is an empty string or is equal to this
     *          <code>StringBuilder</code> object as determined by 
     *          comparing the corresponding string obtained by
     *          calling the {@link StringBuilder#toString()} method.
     */
	public static boolean startsWith(StringBuilder sb, String prefix) {
		return startsWith(sb, prefix, 0);
	}
	
	/**
     * Tests if the substring of the specified <tt>StringBuilder</tt> 
     * beginning at the specified index starts with the specified prefix.
     *
     * @param sb
     * @param prefix the prefix.
     * @param offset where to begin looking in this string.
     * @return  <code>true</code> if the character sequence represented by the
     *          argument is a prefix of the substring of this object starting
     *          at index <code>offset</code>; <code>false</code> otherwise.
     *          The result is <code>false</code> if <code>toffset</code> is
     *          negative or greater than the length of this
     *          <code>StringBuilder</code> object; otherwise the result 
     *          is the same as the result of the expression
     *          <pre>
     *          sb.substring(offset).startsWith(prefix)
     *          </pre>
     */
	public static boolean startsWith(StringBuilder sb, 
			String prefix, int offset) {
		if (offset < 0 || sb.length() - offset < prefix.length())
			return false;
		
		int len = prefix.length();
		for (int i = 0; i < len; ++i) {
			if (sb.charAt(offset + i) != prefix.charAt(i))
				return false;
		}
		return true;
	}
	
	/**
     * Tests if this <tt>StringBuilder</tt> ends with the specified suffix.
     *
     * @param suffix the suffix.
     * @return  <code>true</code> if the character sequence represented by the
     *          argument is a suffix of the character sequence represented by
     *          this object; <code>false</code> otherwise. Note that the
     *          result will be <code>true</code> if the argument is the
     *          empty string or is equal to the specified
     *          <code>StringBuilder</code> object as determined by 
     *          comparing the corresponding string obtained by
     *          calling the {@link StringBuilder#toString()} method.
     */
	public static boolean endsWith(StringBuilder sb, String suffix) {
		return startsWith(sb, suffix, sb.length() - suffix.length());
	}
	
	/**
	 * Clears the specified <tt>StringBuilder</tt>. This method is equivalent
	 * to the following:
	 * <pre>
	 * sb.remove(0, sb.length());
	 * </pre>
	 * 
	 * @param sb the <tt>StringBuilder</tt> to be cleared
	 */
	public static void clear(StringBuilder sb) {
		sb.delete(0, sb.length());
	}
	
}
