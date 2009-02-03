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
public class Circle
        extends Shape {

    public void draw() {
        // Draw a circle.
    }

    public void setRadius(int r) {
        // Set the radius.
    }
}
