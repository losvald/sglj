package org.sglj.persistence.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The marker annotation for annotating methods, constructors, types
 * or packages. This annotation is used to indicate that there is
 * a non type-safe query inside the method, construct, type or package,
 * and is primarily used when a single {@link NonTypesafeAccess}
 * is not enough or impractical.
 * 
 * @author Leo Osvald
 * @version 1.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE,
	ElementType.PACKAGE})
public @interface NonTypesafeQueries {

	/**
	 * List of non type-safe queries which are used inside the 
	 * annotated method, constructor, type or package. 
	 * 
	 * @return the list of non type-safe queries
	 */
	NonTypesafeQuery[] value();
}
