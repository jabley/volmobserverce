/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.metadata.impl.value.jibx;

/**
 * Mutable double implementation.
 */
public class MutableDouble extends MutableNumber {

    private double value;

    /**
     * Creates a mutable double with value set to 0.0.
     */
    public MutableDouble() {
        value = 0.0;
    }

    /**
     * Creates a mutable double value.
     *
     * @param value the initial value
     */
    public MutableDouble(final double value) {
        this.value = value;
    }

    /**
     * Sets the new value.
     *
     * @param value the value
     */
    public void setValue(final double value) {
        this.value = value;
    }

    // javadoc inherited
    public Number getNumber() {
        return new Double(value);
    }
}
