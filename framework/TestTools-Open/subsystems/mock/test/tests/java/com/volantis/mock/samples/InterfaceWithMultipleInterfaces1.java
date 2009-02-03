/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

/**
 * An interface that extends multiple interfaces.
 *
 * @mock.generate base="Interface2" interfaces="Interface3,Interface1"
 */
public interface InterfaceWithMultipleInterfaces1
        extends Interface1, Interface2, Interface3 {
}
