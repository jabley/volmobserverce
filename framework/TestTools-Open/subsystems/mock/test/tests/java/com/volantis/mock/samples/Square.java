/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

/**
 * Derived from class that is mocked.
 *
 * @mock.generate base="Shape"
 */
public class Square
        extends Shape {

    public void draw() {
        // Draw a square.
    }

    public void setLength(int l) {
        // Set the length.
    }
}
