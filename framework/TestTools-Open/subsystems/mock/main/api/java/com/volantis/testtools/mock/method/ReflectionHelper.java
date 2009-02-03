/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.method;

import java.util.HashMap;
import java.util.Map;

public class ReflectionHelper {
    /**
     * Map from the primitive class to the autoboxing class.
     */
    private static final Map primitiveToAutoBox;

    static {
        primitiveToAutoBox = new HashMap();
        primitiveToAutoBox.put(Integer.TYPE, Integer.class);
        primitiveToAutoBox.put(Boolean.TYPE, Boolean.class);
        primitiveToAutoBox.put(Byte.TYPE, Byte.class);
        primitiveToAutoBox.put(Character.TYPE, Character.class);
        primitiveToAutoBox.put(Long.TYPE, Long.class);
        primitiveToAutoBox.put(Short.TYPE, Short.class);
        primitiveToAutoBox.put(Double.TYPE, Double.class);
        primitiveToAutoBox.put(Float.TYPE, Float.class);
    }

    /**
     * Determine whether the object is an instance of the specified class.
     *
     * <p>Supports auto boxing of primitive values.</p>
     *
     * @param clazz The class of which the object may be an instance.
     * @param object The object to test.
     *
     * @return True if the object is an instance of the class, or an instance
     * of the autoboxing class used to wrap values of the primitive type
     * specified by the class.
     */
    public static boolean isInstance(Class clazz, Object object) {
        if (clazz.isPrimitive()) {
            clazz = (Class) primitiveToAutoBox.get(clazz);
        }

        return clazz.isInstance(object);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 12-May-05	8208/1	pduffin	VBM:2005051208 Committing mock object framework changes

 ===========================================================================
*/
