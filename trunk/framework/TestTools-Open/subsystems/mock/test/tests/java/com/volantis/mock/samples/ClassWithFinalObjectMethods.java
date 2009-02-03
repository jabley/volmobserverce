/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

/**
 * A class that has overridden some {@link Object} methods and made them final.
 *
 * @mock.generate
 */
public class ClassWithFinalObjectMethods {

    // Javadoc inherited.
    protected final java.lang.Object clone() {
        return null;
    }

    // Javadoc inherited.
    public final boolean equals(java.lang.Object obj) {
        return false;
    }

    // Javadoc inherited.
    public final int hashCode() {
        return -1;
    }

    // Javadoc inherited.
    public final java.lang.String toString() {
        return "";
    }
}
