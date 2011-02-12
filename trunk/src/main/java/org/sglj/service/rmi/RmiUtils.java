/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sglj.service.rmi;

import java.lang.reflect.Method;
import java.util.Collection;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.reflect.MethodUtils;

public class RmiUtils {
	
	public static Method getMatchingAccessibleMethod(Class<?> cls,
            String methodName, Class<?>[] parameterTypes,
            Collection<Method> callableMethods) {
        try {
            return cls.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) { /* SWALLOW */
        }
        // search through all methods
        Method bestMatch = null;
        for (Method method : callableMethods) {
            if (method.getName().equals(methodName)) {
                // compare parameters
                if (ClassUtils.isAssignable(parameterTypes, method
                        .getParameterTypes(), true)) {
                    // get accessible version of method
                    Method accessibleMethod = MethodUtils.getAccessibleMethod(method);
                    if (accessibleMethod != null) {
                        if (bestMatch == null
                                || compareParameterTypes(
                                        accessibleMethod.getParameterTypes(),
                                        bestMatch.getParameterTypes(),
                                        parameterTypes) < 0) {
                            bestMatch = accessibleMethod;
                        }
                    }
                }
            }
        }
        return bestMatch;
    }
	
	@SuppressWarnings("rawtypes")
	static int compareParameterTypes(Class[] left, Class[] right, Class[] actual) {
        float leftCost = getTotalTransformationCost(actual, left);
        float rightCost = getTotalTransformationCost(actual, right);
        return leftCost < rightCost ? -1 : rightCost < leftCost ? 1 : 0;
    }
	
	/**
     * Returns the sum of the object transformation cost for each class in the source
     * argument list.
     * @param srcArgs The source arguments
     * @param destArgs The destination arguments
     * @return The total transformation cost
     */
    @SuppressWarnings("rawtypes")
	private static float getTotalTransformationCost(Class[] srcArgs,
            Class[] destArgs) {
        float totalCost = 0.0f;
        for (int i = 0; i < srcArgs.length; i++) {
            Class srcClass, destClass;
            srcClass = srcArgs[i];
            destClass = destArgs[i];
            totalCost += getObjectTransformationCost(srcClass, destClass);
        }
        return totalCost;
    }
    
    /**
     * Gets the number of steps required needed to turn the source class into the 
     * destination class. This represents the number of steps in the object hierarchy 
     * graph.
     * @param srcClass The source class
     * @param destClass The destination class
     * @return The cost of transforming an object
     */
    @SuppressWarnings("rawtypes")
	private static float getObjectTransformationCost(Class srcClass,
            Class destClass) {
        if (destClass.isPrimitive()) {
            return getPrimitivePromotionCost(srcClass, destClass);
        }
        float cost = 0.0f;
        while (destClass != null && !destClass.equals(srcClass)) {
            if (destClass.isInterface()
                    && ClassUtils.isAssignable(srcClass, destClass)) {
                // slight penalty for interface match.
                // we still want an exact match to override an interface match,
                // but
                // an interface match should override anything where we have to
                // get a superclass.
                cost += 0.25f;
                break;
            }
            cost++;
            destClass = destClass.getSuperclass();
        }
        /*
         * If the destination class is null, we've travelled all the way up to
         * an Object match. We'll penalize this by adding 1.5 to the cost.
         */
        if (destClass == null) {
            cost += 1.5f;
        }
        return cost;
    }
    
    /**
     * Get the number of steps required to promote a primitive number to another type.
     * @param srcClass the (primitive) source class
     * @param destClass the (primitive) destination class
     * @return The cost of promoting the primitive
     */
    @SuppressWarnings("rawtypes")
	private static float getPrimitivePromotionCost(final Class srcClass,
            final Class destClass) {
        float cost = 0.0f;
        Class cls = srcClass;
        if (!cls.isPrimitive()) {
            // slight unwrapping penalty
            cost += 0.1f;
            cls = ClassUtils.wrapperToPrimitive(cls);
        }
        for (int i = 0; cls != destClass && i < ORDERED_PRIMITIVE_TYPES.length; i++) {
            if (cls == ORDERED_PRIMITIVE_TYPES[i]) {
                cost += 0.1f;
                if (i < ORDERED_PRIMITIVE_TYPES.length - 1) {
                    cls = ORDERED_PRIMITIVE_TYPES[i + 1];
                }
            }
        }
        return cost;
    }
    
    /** Array of primitive number types ordered by "promotability" */
    @SuppressWarnings("rawtypes")
	private static final Class[] ORDERED_PRIMITIVE_TYPES = { Byte.TYPE,
            Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE,
            Double.TYPE };
}
