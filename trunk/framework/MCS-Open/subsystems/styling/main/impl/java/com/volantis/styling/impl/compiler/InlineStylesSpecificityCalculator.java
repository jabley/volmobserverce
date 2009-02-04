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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.styling.impl.compiler;

/**
 * An extension of the Specificity Calculator for handling Inline Sytles
 * specificity.
 */
public class InlineStylesSpecificityCalculator
        extends SpecificityCalculatorImpl {

    /**
     * Initialise the value to be equal to style units.
     *
     * This needs to be done as this calculator is only used for style attribute
     * values so needs marking with the correct specificity.
     */
    public InlineStylesSpecificityCalculator() {
        value = STYLE_UNITS;
    }

    /**
     * Inline styles are marked with the special specificity.
     */
    public void reset() {
        value = STYLE_UNITS;
    }
}
