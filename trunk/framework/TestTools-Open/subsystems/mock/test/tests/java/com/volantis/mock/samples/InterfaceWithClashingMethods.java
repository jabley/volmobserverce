/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

/**
 * Makes sure that an interface with two methods with the same name and number
 * of parameters but different returns types is mockable.
 *
 * @mock.generate
 */
public interface InterfaceWithClashingMethods {

    void foo(java.lang.String bar);
    int foo(int bar);
}
