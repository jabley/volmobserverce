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

import com.volantis.styling.compiler.SpecificityCalculator;
import com.volantis.styling.compiler.Specificity;

/**
 * Implementation of {@link SpecificityCalculator}.
 */
public class SpecificityCalculatorImpl
        implements SpecificityCalculator {

    /**
     * The base in which specificity calculations are made.
     */
    static final int BASE = 100;

    /**
     * The number of units representing a single Element Count.
     * This unit represents (d) in the 4 rules used to calculate specificity.
     */
    static final int ELEMENT_NAMES_AND_PSEUDO_ELEMENT_UNITS = 1;

    /**
     * The number of units representing a single Class Count.
     * This unit represents (c) in the 4 rules used to calculate specificity.
     */
    static final int OTHER_ATTRIBUTES_AND_PSEUDO_CLASS_UNITS =
            ELEMENT_NAMES_AND_PSEUDO_ELEMENT_UNITS * BASE;

    /**
     * The number of units representing a single ID Count.
     * This unit represents (b) in the 4 rules used to calculate specificity.
     */
    static final int ID_ATTRIBUTE_UNITS =
            OTHER_ATTRIBUTES_AND_PSEUDO_CLASS_UNITS * BASE;

    /**
     * The number of units representing a markup specified attribute.
     * This unit represents (a) in the 4 rules used to calculate specificity.
     */
    static final int STYLE_UNITS =
            ID_ATTRIBUTE_UNITS * BASE;

    /**
     * The specificity value.
     */
    protected int value;

    // Javadoc inherited.
    public void addElementSelector() {
        value += ELEMENT_NAMES_AND_PSEUDO_ELEMENT_UNITS;
    }

    // Javadoc inherited.
    public void addPseudoElementSelector() {
        value += ELEMENT_NAMES_AND_PSEUDO_ELEMENT_UNITS;
    }

    // Javadoc inherited.
    public void addClassSelector() {
        value += OTHER_ATTRIBUTES_AND_PSEUDO_CLASS_UNITS;
    }

    // Javadoc inherited.
    public void addPseudoClassSelector() {
        value += OTHER_ATTRIBUTES_AND_PSEUDO_CLASS_UNITS;
    }

    // Javadoc inherited.
    public void addAttributeSelector() {
        value += OTHER_ATTRIBUTES_AND_PSEUDO_CLASS_UNITS;
    }

    // Javadoc inherited.
    public void addIDSelector() {
        value += ID_ATTRIBUTE_UNITS;
    }

    // Javadoc inherited.
    public Specificity getSpecificity() {
        return new SpecificityImpl(value);
    }

    // Javadoc inherited.
    public void reset() {
        value = 0;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/2	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
