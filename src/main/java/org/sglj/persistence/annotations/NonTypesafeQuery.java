package org.sglj.persistence.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>The marker annotation for annotating string constants
 * which represents non type-safe SQL/JP-QL/HQL query.</p>
 * 
 * <p>
 * If used correctly, this annotation provide a means of finding all 
 * queries which refer to an entity and its attributes in a 
 * non type-safe way, without any overhead in the compiled code. <br>
 * This comes in handy when the type of an attribute has changed or when 
 * an attribute is removed from the entity. For example, 
 * all such interesting places can be easily found with the following
 * regular expression searches:
 * <ul>
 * <li>entity[\s]*=[\s]*Car[\s]*.[\s]*class[^)]*"engine"</li>
 * <li>"engine"[^)]*entity[\s]*=[\s]*Car[\s]*.[\s]*class</li>
 * </ul>
 * The regular expressions listed above will find all places where
 * the "engine" column of the <tt>Car.class</tt> class was used in a query.</p>
 * 
 * @author Leo Osvald
 * @version 1.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.LOCAL_VARIABLE, ElementType.FIELD})
public @interface NonTypesafeQuery {
	
	/**
	 * The list of non-type accesses.
	 * 
	 * @return the array of non type-safe accesses
	 * @see NonTypesafeAccess
	 */
	NonTypesafeAccess[] value();
}

