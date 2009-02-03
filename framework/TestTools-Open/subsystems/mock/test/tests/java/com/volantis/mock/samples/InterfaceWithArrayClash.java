/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

/**
 * @mock.generate
 */
public interface InterfaceWithArrayClash {

    void abc(int[] a);

    int abc(float[] a);
}
