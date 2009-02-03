package com.volantis.testtools.mock.value;

import com.volantis.testtools.mock.MockFactory;

import java.util.Arrays;

/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */


public class ValueHelper {

    public static ExpectedValue mapObjectToValue(Object expectedReturnValue) {
        if (expectedReturnValue instanceof ExpectedValue) {
            return (ExpectedValue) expectedReturnValue;
        } else {
            return MockFactory.getDefaultInstance().expectsEqual(expectedReturnValue);
        }
    }

    /**
     * Compare two objects for equality.
     *
     * <p>If the specified objects are arrays then the contents of the arrays
     * are compared deeply as well.</p>
     *
     * @param o1 The first object to compare.
     * @param o2 The second object to compare.
     *
     * @return True if the objects are equal, false otherwise. 
     */
    public static boolean deepEquals(Object o1, Object o2) {

        if (o1 == o2) {
            // They are either the same object, or are both null.
            return true;
        } else if (o1 == null || o2 == null) {
            // One of them is null so cannot be equal to the other.
            return false;
        }

        // At this point they are not the same and neither are null. Check
        // for arrays.
        Class c1 = o1.getClass();
        Class componentType1 = c1.getComponentType();
        Class c2 = o2.getClass();
        Class componentType2 = c2.getComponentType();

        if (componentType1 == null && componentType2 == null) {
            // Neither of them are arrays so just use normal equals.
            return o1.equals(o2);
        } else if (componentType1 != componentType2) {
            // Either one of them is an array but the other is not, or they are
            // of a different type so they can never be equal.
            return false;
        }

        // At this point they are both arrays of the same type so check them.
        if (componentType1 == boolean.class) {
            return Arrays.equals((boolean[]) o1, (boolean[]) o2);
        } else if (componentType1 == byte.class) {
            return Arrays.equals((byte[]) o1, (byte[]) o2);
        } else if (componentType1 == char.class) {
            return Arrays.equals((char[]) o1, (char[]) o2);
        } else if (componentType1 == double.class) {
            return Arrays.equals((double[]) o1, (double[]) o2);
        } else if (componentType1 == float.class) {
            return Arrays.equals((float[]) o1, (float[]) o2);
        } else if (componentType1 == int.class) {
            return Arrays.equals((int[]) o1, (int[]) o2);
        } else if (componentType1 == long.class) {
            return Arrays.equals((long[]) o1, (long[]) o2);
        } else if (componentType1 == short.class) {
            return Arrays.equals((short[]) o1, (short[]) o2);
        }

        // All the simple types have been checked so now check the object
        // array. As it could contain nested arrays it is not possible to use
        // the Arrays.equals(Object[],Object[]) as it only does a shallow
        // equals, relying on the implementation of equals.
        Object[] a1 = (Object[]) o1;
        Object[] a2 = (Object[]) o2;

        if (a1.length != a2.length) {
            // The arrays are of different lengths so cannot be equal.
            return false;
        }

        // Iterate over the arrays.
        for (int i = 0; i < a1.length; i++) {
            Object e1 = a1[i];
            Object e2 = a2[i];
            if (!deepEquals(e1, e2)) {
                // A nested element is not equal so the arrays cannot be equal.
                return false;
            }
        }

        return true;
    }

    /**
     * Append the string representation of an object to the buffer.
     *
     * <p>If the object is an array then output the items comma separated
     * delimited by [].</p>
     *
     * @param buffer The buffer to which the string representation is appended.
     * @param object The object whose string representation is required.
     */
    public static void deepAppend(StringBuffer buffer, Object object) {
        if (object == null) {
            buffer.append("null");
            return;
        }

        // At this point they are not the same and neither are null. Check
        // for arrays.
        Class clazz = object.getClass();
        Class componentType = clazz.getComponentType();

        if (componentType == null) {
            // Not an array so just use normal toString.
            buffer.append(object.toString());
            return;
        }

        String separator = "";
        buffer.append("[");

        // At this point they are both arrays of the same type so check them.
        if (componentType == boolean.class) {
            boolean[] array = (boolean[]) object;
            for (int i = 0; i < array.length; i++) {
                boolean e = array[i];
                buffer.append(separator).append(e);
                separator = ", ";
            }
        } else if (componentType == byte.class) {
            byte[] array = (byte[]) object;
            for (int i = 0; i < array.length; i++) {
                byte e = array[i];
                buffer.append(separator).append(e);
                separator = ", ";
            }
        } else if (componentType == char.class) {
            char[] array = (char[]) object;
            for (int i = 0; i < array.length; i++) {
                char e = array[i];
                buffer.append(separator).append(e);
                separator = ", ";
            }
        } else if (componentType == double.class) {
            double[] array = (double[]) object;
            for (int i = 0; i < array.length; i++) {
                double e = array[i];
                buffer.append(separator).append(e);
                separator = ", ";
            }
        } else if (componentType == float.class) {
            float[] array = (float[]) object;
            for (int i = 0; i < array.length; i++) {
                float e = array[i];
                buffer.append(separator).append(e);
                separator = ", ";
            }
        } else if (componentType == int.class) {
            int[] array = (int[]) object;
            for (int i = 0; i < array.length; i++) {
                int e = array[i];
                buffer.append(separator).append(e);
                separator = ", ";
            }
        } else if (componentType == long.class) {
            long[] array = (long[]) object;
            for (int i = 0; i < array.length; i++) {
                long e = array[i];
                buffer.append(separator).append(e);
                separator = ", ";
            }
        } else if (componentType == short.class) {
            short[] array = (short[]) object;
            for (int i = 0; i < array.length; i++) {
                short e = array[i];
                buffer.append(separator).append(e);
                separator = ", ";
            }
        } else {
        // Iterate over the array.
            Object[] array = (Object[]) object;
            for (int i = 0; i < array.length; i++) {
                Object e = array[i];
                buffer.append(separator);
                deepAppend(buffer, e);
                separator = ", ";
            }
        }
        buffer.append("]");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	8978/1	pduffin	VBM:2005070712 Further enhanced mock framework

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 ===========================================================================
*/
