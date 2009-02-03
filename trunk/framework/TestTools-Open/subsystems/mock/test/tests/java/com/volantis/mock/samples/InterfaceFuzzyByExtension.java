/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

/**
 * @mock.generate base="InterfaceNotFuzzy"
 */
public interface InterfaceFuzzyByExtension
        extends InterfaceNotFuzzy {

    int foo(float a);
}
