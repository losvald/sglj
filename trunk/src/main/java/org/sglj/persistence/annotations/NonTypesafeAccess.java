package org.sglj.persistence.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The marker annotation used as inside the {@link NonTypesafeQuery}
 * to indicate that an entity and/or its attributes are accessed in a
 * non type-safe way.<br>
 * 
 * @author Leo Osvald
 * @version 1.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target({})
public @interface NonTypesafeAccess {
	
	/**
	 * The entity which is accessed in a non type-safe way.
	 * 
	 * @return the class of the entity
	 */
	Class<?> entity();
	
	/**
	 * Columns/attributes which are accessed in a non type-safe way.
	 * The name must match the name of the one defined
	 * by {@link javax.persistence.Column#name()}.
	 * 
	 * @return the array of attribute names
	 */
	String[] columns() default {};
}