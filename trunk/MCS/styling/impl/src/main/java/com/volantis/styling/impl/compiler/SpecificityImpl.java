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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.impl.compiler;

import com.volantis.styling.compiler.Specificity;

/**
 * Represents the result of a CSS Specificity calculation.
 */
public class SpecificityImpl
        implements Specificity {

    /**
     * The specificity.
     */
    private final int value;

    /**
     * Package restricted initialiser to hide the fact that internally this
     * is treated as an int.
     *
     * @param value The specificity as an int.
     */
    SpecificityImpl(int value) {
        this.value = value;
    }

    // Javadoc inherited.
    public boolean equals(Object object) {
        if (!(object instanceof SpecificityImpl)) {
            return false;
        }

        SpecificityImpl other = (SpecificityImpl) object;

        return value == other.value;
    }

    // Javadoc inherited.
    public int hashCode() {
        return value;
    }

    // Javadoc inherited.
    public int compareTo(Object o) {
        SpecificityImpl other = (SpecificityImpl) o;
        return value - other.value;
    }

    public String toString() {
        int v = value;

        int d = v % SpecificityCalculatorImpl.BASE;
        v = v / SpecificityCalculatorImpl.BASE;
        int c = v % SpecificityCalculatorImpl.BASE;
        v = v / SpecificityCalculatorImpl.BASE;
        int b = v % SpecificityCalculatorImpl.BASE;
        v = v / SpecificityCalculatorImpl.BASE;
        int a = v % SpecificityCalculatorImpl.BASE;

        return "{" + a + ", " + b + ", " + c + ", " + d + "}";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
