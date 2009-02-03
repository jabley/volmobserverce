/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

/**
 * @mock.generate
 */
public interface InterfaceWithCaseClash {

    void xyz(int[] a);

    int XYZ(int[] a);

    int XYz(float b);

}
