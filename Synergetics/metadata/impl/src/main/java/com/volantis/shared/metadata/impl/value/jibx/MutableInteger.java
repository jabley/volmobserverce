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
 * Mutable int implementation.
 */
public class MutableInteger extends MutableNumber {

    private int value;

    /**
     * Creates a mutable int with value set to 0.
     */
    public MutableInteger() {
        value = 0;
    }

    /**
     * Creates a mutable int value.
     *
     * @param value the initial value
     */
    public MutableInteger(final int value) {
        this.value = value;
    }

    /**
     * Sets the new value.
     *
     * @param value the value
     */
    public void setValue(final int value) {
        this.value = value;
    }

    // javadoc inherited
    public Number getNumber() {
        return new Integer(value);
    }
}
