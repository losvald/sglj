package org.sglj.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



/**
 * Utility class which provides methods for converting one basic type to 
 * another.<br>
 * There are two types of method for converting each type basic
 * Java type to another one.
 * The first variant return a wrapper class corresponding
 * to the desired primitive type. The <code>null</code> return value
 * indicates that the conversion fails.<br>
 * The second variant returns a primitive in either case. If the conversion
 * fails, the <tt>defaultValue</tt> is returned. The caller responsibility
 * is to choose such <tt>defaultValue</tt> that those two cases can be
 * distinguished.<br>
 * There are also generic methods which convert any of the basic types
 * to the specified type which is specified via parameter.
 * Two types of generic methods for each of the basic types exists; the 
 * first one converts any basic type to that type and is prefixed
 * with "to", and the second one converts that type to the specified type which
 * wrapper object is provided. 
 * 
 * For example, to convert a string to equivalent integer use:
 * <pre>
 * Integer i = TypeConversionUtils.stringToInteger("123");
 * </pre>
 * 
 * This class also provides a generic method to convert some basic type
 * to the primitive or its wrapper.
 * Here are the examples:
 * <pre>
 * Integer i = TypeConversionUtils.stringToBasicType("123", Integer.class);
 * </pre>
 * Note that there is no need for casting.<br>
 * Finally, here is the example how to convert a string to the corresponding
 * basic type equal to the type of the object <tt>o</tt>:
 * <pre>
 * Object converted = TypeConversionUtils.stringToBasicType(s, o.getClass());                
 * </pre>
 * <br>
 * Note that the type conversion methods which could be done by "upcasting", 
 * such as shortToLong are missing because that conversion is always safe
 * and is automatic in Java.<br>
 * <br>
 * All conversion methods which involve conversion from strings 
 * are done exactly by the way implemented in 
 * methods such as {@link Integer#parseInt(String)},
 * {@link Boolean#parseBoolean(String)}. Methods which would require
 * downcasting are implemented to succeed only if the argument is from
 * the range [MIN_VALUE, MAX_VALUE], except the boolean conversion which
 * is implemented like the one in the C programming language (all non-zero
 * values are <code>true</code>). 
 * 
 * @author Leo Osvald
 * @version 1.01
 */
public class TypeConversionUtils {
	
	private static final Set<Class<?>> BASIC_TYPES;
	private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER;
	private static final Map<Class<?>, Class<?>> WRAPPER_PRIMITIVE;
	
	static {
		Set<Class<?>> basicTypes = new HashSet<Class<?>>(9);
		basicTypes.add(Boolean.class);
		basicTypes.add(Byte.class);
		basicTypes.add(Character.class);
		basicTypes.add(Short.class);
		basicTypes.add(Integer.class);
		basicTypes.add(Long.class);
		basicTypes.add(Float.class);
		basicTypes.add(Double.class);
		basicTypes.add(String.class);
		BASIC_TYPES = Collections.unmodifiableSet(basicTypes);
		
		Map<Class<?>, Class<?>> wrapperPrimitive = new HashMap<Class<?>, Class<?>>(8);
		wrapperPrimitive.put(Boolean.class, boolean.class);
		wrapperPrimitive.put(Byte.class, byte.class);
		wrapperPrimitive.put(Character.class, char.class);
		wrapperPrimitive.put(Short.class, short.class);
		wrapperPrimitive.put(Integer.class, int.class);
		wrapperPrimitive.put(Long.class, long.class);
		wrapperPrimitive.put(Float.class, float.class);
		wrapperPrimitive.put(Double.class, double.class);
		WRAPPER_PRIMITIVE = Collections.unmodifiableMap(wrapperPrimitive);
		
		Map<Class<?>, Class<?>> primitiveWrapper = new HashMap<Class<?>, Class<?>>(8);
		primitiveWrapper.put(boolean.class, Boolean.class);
		primitiveWrapper.put(byte.class, Byte.class);
		primitiveWrapper.put(char.class, Character.class);
		primitiveWrapper.put(short.class, Short.class);
		primitiveWrapper.put(int.class, Integer.class);
		primitiveWrapper.put(long.class, Long.class);
		primitiveWrapper.put(float.class, Float.class);
		primitiveWrapper.put(double.class, Double.class);
		PRIMITIVE_WRAPPER = Collections.unmodifiableMap(primitiveWrapper);
	}
	
	private TypeConversionUtils() {
	}
	
	// boolean conversion methods
	
	public static boolean byteToBoolean(byte b) {
		return b != 0 ? true : false;
	}
	
	public static boolean charToBoolean(char c) {
		return c != 0 ? true : false;
	}
	
	public static boolean shortToBoolean(short h) {
		return h != 0 ? true : false;
	}
	
	public static boolean intToBoolean(int i) {
		return i != 0 ? true : false;
	}
	
	public static boolean longToBoolean(long l) {
		return l != 0 ? true : false;
	}
	
	public static boolean floatToBoolean(float f) {
		return f != 0 ? true : false;
	}
	
	public static boolean doubleToBoolean(double d) {
		return d != 0 ? true : false;
	}
	
	public static byte booleanToByte(boolean b) {
		return b ? (byte) 1 : 0;
	}
	
	public static char booleanToChar(boolean b) {
		return b ? (char) 1 : 0;
	}
	
	public static short booleanToShort(boolean b) {
		return b ? (short) 1 : 0;
	}
	
	public static int booleanToInt(boolean b) {
		return b ? 1 : 0;
	}
	
	public static long booleanToLong(boolean b) {
		return b ? 1L : 0L;
	}
	
	public static float booleanToFloat(boolean b) {
		return b ? 1f : 0f;
	}
	
	public static double booleanToDouble(boolean b) {
		return b ? 1. : 0.;
	}
	
	// standard type-to-type conversion (only downcasts)
	
	public static Boolean stringToBoolean(String s) {
		if (s == null)
			return null;
		
		try {
			return Boolean.valueOf(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Boolean toBoolean(Object o) {
		if (o == null)
			return null;
		
		if (o.getClass() == Boolean.class)
			return (Boolean) o;
		
		if (o.getClass() == Byte.class)
			return byteToBoolean(((Byte) o));
		if (o.getClass() == Character.class)
			return charToBoolean(((Character) o));
		if (o.getClass() == Short.class)
			return shortToBoolean(((Short) o));
		if (o.getClass() == Integer.class)
			return intToBoolean(((Integer) o));
		if (o.getClass() == Long.class)
			return longToBoolean(((Long) o));
		if (o.getClass() == Float.class)
			return floatToBoolean(((Float) o));
		if (o.getClass() == Double.class)
			return doubleToBoolean(((Double) o));
		if (o.getClass() == String.class)
			return stringToBoolean(((String) o));
		
		return null;
	}
	
	public static Byte charToByte(char c) {
		if (c >= Byte.MIN_VALUE &&  c <= Byte.MAX_VALUE) {
			return (byte) c;
		}
		return null;
	}
	
	public static Byte shortToByte(short h) {
		if (h >= Byte.MIN_VALUE &&  h <= Byte.MAX_VALUE) {
			return (byte) h;
		}
		return null;
	}
	
	public static Byte intToByte(int i) {
		if (i >= Byte.MIN_VALUE && i <= Byte.MAX_VALUE) {
			return (byte) i;
		}
		return null;
	}
	
	public static Byte longToByte(long l) {
		if (l >= Byte.MIN_VALUE && l <= Byte.MAX_VALUE) {
			return (byte) l;
		}
		return null;
	}
	
	public static Byte floatToByte(float f) {
		if (f >= Byte.MIN_VALUE && f <= Byte.MAX_VALUE) {
			return (byte) f;
		}
		return null;
	}
	
	public static Byte doubleToByte(double d) {
		if (d >= Byte.MIN_VALUE && d <= Byte.MAX_VALUE) {
			return (byte) d;
		}
		return null;
	}
	
	public static Byte stringToByte(String s) {
		try {
			return Byte.valueOf(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Byte toByte(Object o) {
		if (o == null)
			return null;

		if (o.getClass() == Byte.class)
			return (Byte) o;
		
		if (o.getClass() == Boolean.class)
			return booleanToByte((Boolean) o);
		if (o.getClass() == Character.class)
			return charToByte((Character) o);
		if (o.getClass() == Short.class)
			return shortToByte((Short) o);
		if (o.getClass() == Integer.class)
			return intToByte((Integer) o);
		if (o.getClass() == Long.class)
			return longToByte((Long) o);
		if (o.getClass() == Float.class)
			return floatToByte((Float) o);
		if (o.getClass() == Double.class)
			return doubleToByte((Double) o);
		if (o.getClass() == String.class)
			return stringToByte((String) o);
		
		return null;
	}
	
	public static Character shortToCharacter(short h) {
		if (h >= Character.MIN_VALUE && h <= Character.MAX_VALUE) {
			return (char) h;
		}
		return null;
	}
	
	public static Character intToCharacter(int i) {
		if (i >= Character.MIN_VALUE && i <= Character.MAX_VALUE) {
			return (char) i;
		}
		return null;
	}
	
	public static Character longToCharacter(long l) {
		if (l >= Character.MIN_VALUE && l <= Character.MAX_VALUE) {
			return (char) l;
		}
		return null;
	}
	
	public static Character floatToCharacter(float f) {
		if (f >= Character.MIN_VALUE && f <= Character.MAX_VALUE) {
			return (char) f;
		}
		return null;
	}
	
	public static Character doubleToCharacter(double d) {
		if (d >= Character.MIN_VALUE && d <= Character.MAX_VALUE) {
			return (char) d;
		}
		return null;
	}
	
	public static Character stringToCharacter(String s) {
		if (s == null || s.length() != 1)
			return null;
		try {
			return s.charAt(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public static Character toCharacter(Object o) {
		if (o == null)
			return null;
		
		if (o.getClass() == Character.class)
			return (Character) o;
		
		if (o.getClass() == Boolean.class)
			return booleanToChar((Boolean) o);
		if (o.getClass() == Byte.class)
			return (char) ((Byte) o).byteValue();
		if (o.getClass() == Short.class)
			return shortToCharacter((Short) o);
		if (o.getClass() == Integer.class)
			return intToCharacter((Integer) o);
		if (o.getClass() == Long.class)
			return longToCharacter((Long) o);
		if (o.getClass() == Float.class)
			return floatToCharacter((Float) o);
		if (o.getClass() == Double.class)
			return doubleToCharacter((Double) o);
		if (o.getClass() == String.class)
			return stringToCharacter((String) o);
		
		return null;
	}
	
	public static Short intToShort(int i) {
		if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE) {
			return (short) i;
		}
		return null;
	}
	
	public static Short longToShort(long l) {
		if (l >= Short.MIN_VALUE && l <= Short.MAX_VALUE) {
			return (short) l;
		}
		return null;
	}
	
	public static Short floatToShort(float f) {
		if (f >= Short.MIN_VALUE && f <= Short.MAX_VALUE) {
			return (short) f;
		}
		return null;
	}
	
	public static Short doubleToShort(double d) {
		if (d >= Short.MIN_VALUE && d <= Short.MAX_VALUE) {
			return (short) d;
		}
		return null;
	}
	
	public static Short stringToShort(String s) {
		try {
			return Short.valueOf(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Short toShort(Object o) {
		if (o == null)
			return null;

		if (o.getClass() == Short.class)
			return (Short) o;
		
		if (o.getClass() == Boolean.class)
			return booleanToShort((Boolean) o);
		if (o.getClass() == Byte.class)
			return (short) ((Byte) o).byteValue();
		if (o.getClass() == Character.class)
			return (short) ((Character) o).charValue();
		if (o.getClass() == Integer.class)
			return intToShort((Integer) o);
		if (o.getClass() == Long.class)
			return longToShort((Long) o);
		if (o.getClass() == Float.class)
			return floatToShort((Float) o);
		if (o.getClass() == Double.class)
			return doubleToShort((Double) o);
		if (o.getClass() == String.class)
			return stringToShort((String) o);
		
		return null;
	}
	
	public static Integer longToInteger(long l) {
		if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) {
			return (int) l;
		}
		return null;
	}
	
	public static Integer floatToInteger(float f) {
		if (f >= Integer.MIN_VALUE && f <= Integer.MAX_VALUE) {
			return (int) f;
		}
		return null;
	}
	
	public static Integer doubleToInteger(double d) {
		if (d >= Integer.MIN_VALUE && d <= Integer.MAX_VALUE) {
			return (int) d;
		}
		return null;
	}
	
	public static Integer stringToInteger(String s) {
		try {
			return Integer.valueOf(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Integer toInteger(Object o) {
		if (o == null)
			return null;

		if (o.getClass() == Integer.class)
			return (Integer) o;
		
		if (o.getClass() == Boolean.class)
			return booleanToInt((Boolean) o);
		if (o.getClass() == Byte.class)
			return (int) ((Byte) o).byteValue();
		if (o.getClass() == Character.class)
			return (int) ((Character) o).charValue();
		if (o.getClass() == Short.class)
			return (int) ((Short) o).shortValue();
		if (o.getClass() == Long.class)
			return longToInteger((Long) o);
		if (o.getClass() == Float.class)
			return floatToInteger((Float) o);
		if (o.getClass() == Double.class)
			return doubleToInteger((Double) o);
		if (o.getClass() == String.class)
			return stringToInteger((String) o);
		
		return null;
	}
	
	public static Long floatToLong(float f) {
		if (f >= Long.MIN_VALUE && f <= Long.MAX_VALUE) {
			return (long) f;
		}
		return null;
	}
	
	public static Long doubleToLong(double d) {
		if (d >= Long.MIN_VALUE && d <= Long.MAX_VALUE) {
			return (long) d;
		}
		return null;
	}
	
	public static Long stringToLong(String s) {
		try {
			return Long.valueOf(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Long toLong(Object o) {
		if (o == null)
			return null;
		
		if (o.getClass() == Long.class)
			return (Long) o;
		
		if (o.getClass() == Boolean.class)
			return booleanToLong((Boolean) o);
		if (o.getClass() == Byte.class)
			return (long) ((Byte) o).byteValue();
		if (o.getClass() == Character.class)
			return (long) ((Character) o).charValue();
		if (o.getClass() == Short.class)
			return (long) ((Short) o).shortValue();
		if (o.getClass() == Integer.class)
			return (long) ((Integer) o).intValue();
		if (o.getClass() == Float.class)
			return floatToLong((Float) o);
		if (o.getClass() == Double.class)
			return doubleToLong((Double) o);
		if (o.getClass() == String.class)
			return stringToLong((String) o);
		
		return null;
	}
	
	public static Float doubleToFloat(double d) {
		if (d >= Float.MIN_VALUE && d <= Float.MAX_VALUE) {
			return (float) d;
		}
		return null;
	}
	
	public static Float stringToFloat(String s) {
		if (s == null)
			return null;
		try {
			return Float.valueOf(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Float toFloat(Object o) {
		if (o == null)
			return null;
		
		if (o.getClass() == Float.class)
			return (Float) o;
		
		if (o.getClass() == Boolean.class)
			return booleanToFloat((Boolean) o);
		if (o.getClass() == Byte.class)
			return (float) ((Byte) o).byteValue();
		if (o.getClass() == Character.class)
			return (float) ((Character) o).charValue();
		if (o.getClass() == Short.class)
			return (float) ((Short) o).shortValue();
		if (o.getClass() == Integer.class)
			return (float) ((Integer) o).intValue();
		if (o.getClass() == Long.class)
			return (float) ((Long) o).longValue();
		if (o.getClass() == Double.class)
			return doubleToFloat((Double) o);
		if (o.getClass() == String.class)
			return stringToFloat((String) o);
		
		return null;
	}
	
	public static Double stringToDouble(String s) {
		if (s == null)
			return null;
		try {
			return Double.valueOf(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Double toDouble(Object o) {
		if (o == null)
			return null;
		
		if (o.getClass() == Double.class)
			return (Double) o;
		
		if (o.getClass() == Boolean.class)
			return booleanToDouble((Boolean) o);
		if (o.getClass() == Byte.class)
			return (double) ((Byte) o).byteValue();
		if (o.getClass() == Character.class)
			return (double) ((Character) o).charValue();
		if (o.getClass() == Short.class)
			return (double) ((Short) o).shortValue();
		if (o.getClass() == Integer.class)
			return (double) ((Integer) o).intValue();
		if (o.getClass() == Long.class)
			return (double) ((Long) o).longValue();
		if (o.getClass() == Float.class)
			return (double) ((Float) o).floatValue();
		if (o.getClass() == String.class)
			return stringToDouble((String) o);
		
		return null;
	}
	
	
	// methods which return primitives
	
	public static boolean stringToBoolean(String s, boolean defaultValue) {
		if (s == null)
			return defaultValue;
		
		try {
			return Boolean.valueOf(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public static boolean toBoolean(Object o, boolean defaultValue) {
		if (o == null)
			return defaultValue;
		
		if (o.getClass() == Boolean.class)
			return (Boolean) o;
		
		if (o.getClass() == Byte.class)
			return byteToBoolean(((Byte) o));
		if (o.getClass() == Character.class)
			return charToBoolean(((Character) o));
		if (o.getClass() == Short.class)
			return shortToBoolean(((Short) o));
		if (o.getClass() == Integer.class)
			return intToBoolean(((Integer) o));
		if (o.getClass() == Long.class)
			return longToBoolean(((Long) o));
		if (o.getClass() == Float.class)
			return floatToBoolean(((Float) o));
		if (o.getClass() == Double.class)
			return doubleToBoolean(((Double) o));
		if (o.getClass() == String.class)
			return stringToBoolean(((String) o), defaultValue);
		
		return defaultValue;
	}
	
	public static byte charToByte(char c, byte defaultValue) {
		if (c >= Byte.MIN_VALUE &&  c <= Byte.MAX_VALUE) {
			return (byte) c;
		}
		return defaultValue;
	}
	
	public static byte shortToByte(short h, byte defaultValue) {
		if (h >= Byte.MIN_VALUE &&  h <= Byte.MAX_VALUE) {
			return (byte) h;
		}
		return defaultValue;
	}
	
	public static byte intToByte(int i, byte defaultValue) {
		if (i >= Byte.MIN_VALUE && i <= Byte.MAX_VALUE) {
			return (byte) i;
		}
		return defaultValue;
	}
	
	public static byte longToByte(long l, byte defaultValue) {
		if (l >= Byte.MIN_VALUE && l <= Byte.MAX_VALUE) {
			return (byte) l;
		}
		return defaultValue;
	}
	
	public static byte floatToByte(float f, byte defaultValue) {
		if (f >= Byte.MIN_VALUE && f <= Byte.MAX_VALUE) {
			return (byte) f;
		}
		return defaultValue;
	}
	
	public static byte doubleToByte(double d, byte defaultValue) {
		if (d >= Byte.MIN_VALUE && d <= Byte.MAX_VALUE) {
			return (byte) d;
		}
		return defaultValue;
	}
	
	public static byte stringToByte(String s, byte defaultValue) {
		try {
			return Byte.valueOf(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public static byte toByte(Object o, byte defaultValue) {
		if (o == null)
			return defaultValue;

		if (o.getClass() == Byte.class)
			return (Byte) o;
		
		if (o.getClass() == Boolean.class)
			return booleanToByte((Boolean) o);
		if (o.getClass() == Character.class)
			return charToByte((Character) o, defaultValue);
		if (o.getClass() == Short.class)
			return shortToByte((Short) o, defaultValue);
		if (o.getClass() == Integer.class)
			return intToByte((Integer) o, defaultValue);
		if (o.getClass() == Long.class)
			return longToByte((Long) o, defaultValue);
		if (o.getClass() == Float.class)
			return floatToByte((Float) o, defaultValue);
		if (o.getClass() == Double.class)
			return doubleToByte((Double) o, defaultValue);
		if (o.getClass() == String.class)
			return stringToByte((String) o, defaultValue);
		
		return defaultValue;
	}
	
	public static char shortToChar(short h, char defaultValue) {
		if (h >= Character.MIN_VALUE && h <= Character.MAX_VALUE) {
			return (char) h;
		}
		return defaultValue;
	}
	
	public static char intToChar(int i, char defaultValue) {
		if (i >= Character.MIN_VALUE && i <= Character.MAX_VALUE) {
			return (char) i;
		}
		return defaultValue;
	}
	
	public static char longToChar(long l, char defaultValue) {
		if (l >= Character.MIN_VALUE && l <= Character.MAX_VALUE) {
			return (char) l;
		}
		return defaultValue;
	}
	
	public static char floatToChar(float f, char defaultValue) {
		if (f >= Character.MIN_VALUE && f <= Character.MAX_VALUE) {
			return (char) f;
		}
		return defaultValue;
	}
	
	public static char doubleToChar(double d, char defaultValue) {
		if (d >= Character.MIN_VALUE && d <= Character.MAX_VALUE) {
			return (char) d;
		}
		return defaultValue;
	}
	
	public static char stringToChar(String s, char defaultValue) {
		if (s == null || s.length() != 1)
			return defaultValue;
		try {
			return s.charAt(0);
		} catch (IndexOutOfBoundsException e) {
			return defaultValue;
		}
	}
	
	public static char toChar(Object o, char defaultValue) {
		if (o == null)
			return defaultValue;

		if (o.getClass() == Character.class)
			return (Character) o;
		
		if (o.getClass() == Boolean.class)
			return booleanToChar((Boolean) o);
		if (o.getClass() == Byte.class)
			return (char) ((Byte) o).byteValue();
		if (o.getClass() == Short.class)
			return shortToChar((Short) o, defaultValue);
		if (o.getClass() == Integer.class)
			return intToChar((Integer) o, defaultValue);
		if (o.getClass() == Long.class)
			return longToChar((Long) o, defaultValue);
		if (o.getClass() == Float.class)
			return floatToChar((Float) o, defaultValue);
		if (o.getClass() == Double.class)
			return doubleToChar((Double) o, defaultValue);
		if (o.getClass() == String.class)
			return stringToChar((String) o, defaultValue);
		
		return defaultValue;
	}
	
	public static short intToShort(int i, short defaultValue) {
		if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE) {
			return (short) i;
		}
		return defaultValue;
	}
	
	public static short longToShort(long l, short defaultValue) {
		if (l >= Short.MIN_VALUE && l <= Short.MAX_VALUE) {
			return (short) l;
		}
		return defaultValue;
	}
	
	public static short floatToShort(float f, short defaultValue) {
		if (f >= Short.MIN_VALUE && f <= Short.MAX_VALUE) {
			return (short) f;
		}
		return defaultValue;
	}
	
	public static short doubleToShort(double d, short defaultValue) {
		if (d >= Short.MIN_VALUE && d <= Short.MAX_VALUE) {
			return (short) d;
		}
		return defaultValue;
	}
	
	public static short stringToShort(String s, short defaultValue) {
		try {
			return Short.valueOf(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public static short toShort(Object o, short defaultValue) {
		if (o == null)
			return defaultValue;
		
		if (o.getClass() == Short.class)
			return (Short) o;
		
		if (o.getClass() == Boolean.class)
			return booleanToShort((Boolean) o);
		if (o.getClass() == Byte.class)
			return (short) ((Byte) o).byteValue();
		if (o.getClass() == Character.class)
			return (short) ((Character) o).charValue();
		if (o.getClass() == Integer.class)
			return intToShort((Integer) o, defaultValue);
		if (o.getClass() == Long.class)
			return longToShort((Long) o, defaultValue);
		if (o.getClass() == Float.class)
			return floatToShort((Float) o, defaultValue);
		if (o.getClass() == Double.class)
			return doubleToShort((Double) o, defaultValue);
		if (o.getClass() == String.class)
			return stringToShort((String) o, defaultValue);
		
		return defaultValue;
	}
	
	public static int longToInt(long l, int defaultValue) {
		if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) {
			return (int) l;
		}
		return defaultValue;
	}
	
	public static int floatToInt(float f, int defaultValue) {
		if (f >= Integer.MIN_VALUE && f <= Integer.MAX_VALUE) {
			return (int) f;
		}
		return defaultValue;
	}
	
	public static int doubleToInt(double d, int defaultValue) {
		if (d >= Integer.MIN_VALUE && d <= Integer.MAX_VALUE) {
			return (int) d;
		}
		return defaultValue;
	}
	
	public static int stringToInt(String s, int defaultValue) {
		try {
			return Integer.valueOf(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public static int toInt(Object o, int defaultValue) {
		if (o == null)
			return defaultValue;
		
		if (o.getClass() == Integer.class)
			return (Integer) o;
		
		if (o.getClass() == Boolean.class)
			return booleanToInt((Boolean) o);
		if (o.getClass() == Byte.class)
			return (int) ((Byte) o).byteValue();
		if (o.getClass() == Character.class)
			return (int) ((Character) o).charValue();
		if (o.getClass() == Short.class)
			return (int) ((Short) o).shortValue();
		if (o.getClass() == Long.class)
			return longToInt((Long) o, defaultValue);
		if (o.getClass() == Float.class)
			return floatToInt((Float) o, defaultValue);
		if (o.getClass() == Double.class)
			return doubleToInt((Double) o, defaultValue);
		if (o.getClass() == String.class)
			return stringToInt((String) o, defaultValue);
		
		return defaultValue;
	}
	
	public static long floatToLong(float f, long defaultValue) {
		if (f >= Long.MIN_VALUE && f <= Long.MAX_VALUE) {
			return (long) f;
		}
		return defaultValue;
	}
	
	public static long doubleToLong(double d, long defaultValue) {
		if (d >= Long.MIN_VALUE && d <= Long.MAX_VALUE) {
			return (long) d;
		}
		return defaultValue;
	}
	
	public static long stringToLong(String s, long defaultValue) {
		try {
			return Long.valueOf(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public static long toLong(Object o, long defaultValue) {
		if (o == null)
			return defaultValue;
		
		if (o.getClass() == Long.class)
			return (Long) o;
		
		if (o.getClass() == Boolean.class)
			return booleanToLong((Boolean) o);
		if (o.getClass() == Byte.class)
			return (long) ((Byte) o).byteValue();
		if (o.getClass() == Character.class)
			return (long) ((Character) o).charValue();
		if (o.getClass() == Short.class)
			return (long) ((Short) o).shortValue();
		if (o.getClass() == Integer.class)
			return (long) ((Integer) o).intValue();
		if (o.getClass() == Float.class)
			return floatToLong((Float) o, defaultValue);
		if (o.getClass() == Double.class)
			return doubleToLong((Double) o, defaultValue);
		if (o.getClass() == String.class)
			return stringToLong((String) o, defaultValue);
		
		return defaultValue;
	}
	
	public static float doubleToFloat(double d, float defaultValue) {
		if (d >= Float.MIN_VALUE && d <= Float.MAX_VALUE) {
			return (float) d;
		}
		return defaultValue;
	}
	
	public static float stringToFloat(String s, float defaultValue) {
		if (s == null)
			return defaultValue;
		try {
			return Float.valueOf(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public static float toFloat(Object o, float defaultValue) {
		if (o == null)
			return defaultValue;
		
		if (o.getClass() == Float.class)
			return (Float) o;
		
		if (o.getClass() == Boolean.class)
			return booleanToFloat((Boolean) o);
		if (o.getClass() == Byte.class)
			return (float) ((Byte) o).byteValue();
		if (o.getClass() == Character.class)
			return (float) ((Character) o).charValue();
		if (o.getClass() == Short.class)
			return (float) ((Short) o).shortValue();
		if (o.getClass() == Integer.class)
			return (float) ((Integer) o).intValue();
		if (o.getClass() == Long.class)
			return (float) ((Long) o).longValue();
		if (o.getClass() == Double.class)
			return doubleToFloat((Double) o, defaultValue);
		if (o.getClass() == String.class)
			return stringToFloat((String) o, defaultValue);
		
		return defaultValue;
	}
	
	public static double stringToDouble(String s, double defaultValue) {
		if (s == null)
			return defaultValue;
		try {
			return Double.valueOf(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public static double toDouble(Object o, double defaultValue) {
		if (o == null)
			return defaultValue;
		
		if (o.getClass() == Double.class)
			return (Double) o;
		
		if (o.getClass() == Boolean.class)
			return booleanToDouble((Boolean) o);
		if (o.getClass() == Byte.class)
			return (double) ((Byte) o).byteValue();
		if (o.getClass() == Character.class)
			return (double) ((Character) o).charValue();
		if (o.getClass() == Short.class)
			return (double) ((Short) o).shortValue();
		if (o.getClass() == Integer.class)
			return (double) ((Integer) o).intValue();
		if (o.getClass() == Long.class)
			return (double) ((Long) o).longValue();
		if (o.getClass() == Float.class)
			return (double) ((Float) o).floatValue();
		if (o.getClass() == String.class)
			return stringToDouble((String) o, defaultValue);
		
		return defaultValue;
	}
	
	
	// generic methods
	
	@SuppressWarnings("unchecked")
	public static <T> T booleanToBasicType(boolean b, Class<T> clazz) {
		if (clazz.equals(Boolean.class))
			return (T) Boolean.valueOf(b);
		if (clazz.equals(Byte.class))
			return (T) Byte.valueOf(booleanToByte(b));
		if (clazz.equals(Character.class))
			return (T) Character.valueOf(booleanToChar(b));
		if (clazz.equals(Short.class))
			return (T) Short.valueOf(booleanToShort(b));
		if (clazz.equals(Integer.class))
			return (T) Integer.valueOf(booleanToInt(b));
		if (clazz.equals(Long.class))
			return (T) Long.valueOf(booleanToLong(b));
		if (clazz.equals(Float.class))
			return (T) Float.valueOf(booleanToFloat(b));
		if (clazz.equals(Double.class))
			return (T) Double.valueOf(booleanToDouble(b));
		if (clazz.equals(String.class))
			return (T) String.valueOf(b);
			
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T byteToBasicType(byte b, Class<T> clazz) {
		if (clazz.equals(Boolean.class))
			return (T) Boolean.valueOf(byteToBoolean(b));
		if (clazz.equals(Byte.class))
			return (T) Byte.valueOf(b);
		if (clazz.equals(Character.class))
			return (T) Character.valueOf((char) b);
		if (clazz.equals(Short.class))
			return (T) Short.valueOf(b);
		if (clazz.equals(Integer.class))
			return (T) Integer.valueOf(b);
		if (clazz.equals(Long.class))
			return (T) Long.valueOf(b);
		if (clazz.equals(Float.class))
			return (T) Float.valueOf(b);
		if (clazz.equals(Double.class))
			return (T) Double.valueOf(b);
		if (clazz.equals(String.class))
			return (T) String.valueOf(b);
			
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T charToBasicType(char c, Class<T> clazz) {
		if (clazz.equals(Boolean.class))
			return (T) Boolean.valueOf(charToBoolean(c));
		if (clazz.equals(Byte.class))
			return (T) charToByte(c);
		if (clazz.equals(Character.class))
			return (T) Character.valueOf(c);
		if (clazz.equals(Short.class))
			return (T) Short.valueOf((short) c);
		if (clazz.equals(Integer.class))
			return (T) Integer.valueOf(c);
		if (clazz.equals(Long.class))
			return (T) Long.valueOf(c);
		if (clazz.equals(Float.class))
			return (T) Float.valueOf(c);
		if (clazz.equals(Double.class))
			return (T) Double.valueOf(c);
		if (clazz.equals(String.class))
			return (T) String.valueOf(c);
			
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T shortToBasicType(short h, Class<T> clazz) {
		if (clazz.equals(Boolean.class))
			return (T) Boolean.valueOf(shortToBoolean(h));
		if (clazz.equals(Byte.class))
			return (T) shortToByte(h);
		if (clazz.equals(Character.class))
			return (T) shortToCharacter(h);
		if (clazz.equals(Short.class))
			return (T) Short.valueOf(h);
		if (clazz.equals(Integer.class))
			return (T) Integer.valueOf(h);
		if (clazz.equals(Long.class))
			return (T) Long.valueOf(h);
		if (clazz.equals(Float.class))
			return (T) Float.valueOf(h);
		if (clazz.equals(Double.class))
			return (T) Double.valueOf(h);
		if (clazz.equals(String.class))
			return (T) String.valueOf(h);
			
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T intToBasicType(int i, Class<T> clazz) {
		if (clazz.equals(Boolean.class))
			return (T) Boolean.valueOf(intToBoolean(i));
		if (clazz.equals(Byte.class))
			return (T) intToByte(i);
		if (clazz.equals(Character.class))
			return (T) intToCharacter(i);
		if (clazz.equals(Short.class))
			return (T) intToShort(i);
		if (clazz.equals(Integer.class))
			return (T) Integer.valueOf(i);
		if (clazz.equals(Long.class))
			return (T) Long.valueOf(i);
		if (clazz.equals(Float.class))
			return (T) Float.valueOf(i);
		if (clazz.equals(Double.class))
			return (T) Double.valueOf(i);
			
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T longToBasicType(long l, Class<T> clazz) {
		if (clazz.equals(Boolean.class))
			return (T) Boolean.valueOf(longToBoolean(l));
		if (clazz.equals(Byte.class))
			return (T) longToByte(l);
		if (clazz.equals(Character.class))
			return (T) longToCharacter(l);
		if (clazz.equals(Short.class))
			return (T) longToShort(l);
		if (clazz.equals(Integer.class))
			return (T) longToInteger(l);
		if (clazz.equals(Long.class))
			return (T) Long.valueOf(l);
		if (clazz.equals(Float.class))
			return (T) Float.valueOf(l);
		if (clazz.equals(Double.class))
			return (T) Double.valueOf(l);
		if (clazz.equals(String.class))
			return (T) String.valueOf(l);
			
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T floatToBasicType(float f, Class<T> clazz) {
		if (clazz.equals(Boolean.class))
			return (T) Boolean.valueOf(floatToBoolean(f));
		if (clazz.equals(Byte.class))
			return (T) floatToByte(f);
		if (clazz.equals(Character.class))
			return (T) floatToCharacter(f);
		if (clazz.equals(Short.class))
			return (T) floatToShort(f);
		if (clazz.equals(Integer.class))
			return (T) floatToInteger(f);
		if (clazz.equals(Long.class))
			return (T) floatToLong(f);
		if (clazz.equals(Float.class))
			return (T) Float.valueOf(f);
		if (clazz.equals(Double.class))
			return (T) Double.valueOf(f);
		if (clazz.equals(String.class))
			return (T) String.valueOf(f);	
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T doubleToBasicType(double d, Class<T> clazz) {
		if (clazz.equals(Boolean.class))
			return (T) Boolean.valueOf(doubleToBoolean(d));
		if (clazz.equals(Byte.class))
			return (T) doubleToByte(d);
		if (clazz.equals(Character.class))
			return (T) doubleToCharacter(d);
		if (clazz.equals(Short.class))
			return (T) doubleToShort(d);
		if (clazz.equals(Integer.class))
			return (T) doubleToInteger(d);
		if (clazz.equals(Long.class))
			return (T) doubleToLong(d);
		if (clazz.equals(Float.class))
			return (T) doubleToFloat(d);
		if (clazz.equals(Double.class))
			return (T) Double.valueOf(d);
		if (clazz.equals(String.class))
			return (T) String.valueOf(d);
			
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T stringToBasicType(String s, Class<T> clazz) {
		if (s == null)
			return null;
		
		if (clazz.equals(Boolean.class))
			return (T) stringToBoolean(s);
		if (clazz.equals(Byte.class))
			return (T) stringToByte(s);
		if (clazz.equals(Character.class))
			return (T) stringToCharacter(s);
		if (clazz.equals(Short.class))
			return (T) stringToShort(s);
		if (clazz.equals(Integer.class))
			return (T) stringToInteger(s);
		if (clazz.equals(Long.class))
			return (T) stringToLong(s);
		if (clazz.equals(Float.class))
			return (T) stringToFloat(s);
		if (clazz.equals(Double.class))
			return (T) stringToDouble(s);
		if (clazz.equals(String.class))
			return (T) s;
			
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T toBasicType(Object o, Class<T> clazz) {
		if (o == null) // check now to avoid comparison
			return null;
		
		Class<?> fromType = o.getClass();
		
		// optimize for conversion to the same class
		if (fromType.equals(clazz)) {
			return BASIC_TYPES.contains(clazz) ? (T) o : null;
		}
		
		if (fromType.equals(Boolean.class))
			return booleanToBasicType((Boolean) o, clazz);
		if (fromType.equals(Byte.class))
			return byteToBasicType((Byte) o, clazz);
		if (fromType.equals(Character.class))
			return charToBasicType((Character) o, clazz);
		if (fromType.equals(Short.class))
			return shortToBasicType((Short) o, clazz);
		if (fromType.equals(Integer.class))
			return intToBasicType((Integer) o, clazz);
		if (fromType.equals(Long.class))
			return longToBasicType((Long) o, clazz);
		if (fromType.equals(Float.class))
			return floatToBasicType((Float) o, clazz);
		if (fromType.equals(Double.class))
			return doubleToBasicType((Double) o, clazz);
		if (fromType.equals(String.class))
			return stringToBasicType((String) o, clazz);
		
		return null;
	}
	
	public static final Object[] toBasicType(Object[] args, Class<?>[] types) 
	throws IllegalArgumentException {
		if (args == null || types == null || args.length != types.length)
			throw new IllegalArgumentException("Invalid arguments");
		
		Object[] ret = new Object[args.length];
		for (int i = 0; i < ret.length; ++i)
			ret[i] = toBasicType(args[i], types[i]);
		
		return ret;
	}
	
	// methods for class conversion

	public static Class<?> toPrimitiveClass(Class<?> clazz) {
		Class<?> c = WRAPPER_PRIMITIVE.get(clazz);
		return c != null ? c : clazz; 
	}
	
	public static Class<?> toPrimitiveClass(Class<?> clazz, Class<?> defaultClazz) {
		Class<?> c = WRAPPER_PRIMITIVE.get(clazz);
		return c != null ? c : defaultClazz;
	}
	
	public static Class<?> toPrimitiveWrapperClass(Class<?> clazz) {
		Class<?> c = PRIMITIVE_WRAPPER.get(clazz);
		return c != null ? c : clazz; 
	}
	
	public static Class<?> toPrimitiveWrapperClass(Class<?> clazz, Class<?> defaultClazz) {
		Class<?> c = PRIMITIVE_WRAPPER.get(clazz);
		return c != null ? c : defaultClazz; 
	}
}
