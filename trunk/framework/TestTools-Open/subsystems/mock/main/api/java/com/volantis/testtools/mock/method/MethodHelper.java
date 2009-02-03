/* ----------------------------------------------------------------------------
 * (c) Copyright Volantis Systems Ltd. 2007. All rights reserved
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.mock.method;

/**
 * A class to hold some common functions. This exists due to a refactoring that
 * moved the original classes that contained these functions into the
 * impl directory where they would not be visible.
 */
public class MethodHelper {

    /**
     * This class cannot be instantiated
     */
    private MethodHelper() {

    }

    /**
     * Get a shorter name. <p/> <p>This takes the string after the last '.'. If
     * there are none then it simply returns the supplied name.</p>
     *
     * @param name The name.
     * @return The short name.
     */
    public static String getShorterName(String name) {
        int index = name.lastIndexOf('.');
        if (index != -1) {
            name = name.substring(index + 1);
        }

        return name;
    }
}
