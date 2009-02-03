/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

import java.io.IOException;

/**
 * Makes sure that a class with many constructors is mockable.
 *
 * @mock.generate
 */
public class ClassWithCtors {

    public ClassWithCtors()
            throws IOException {
    }

    public ClassWithCtors(int a, java.lang.String b, boolean[] c)
            throws IOException, IllegalStateException {
    }
}
