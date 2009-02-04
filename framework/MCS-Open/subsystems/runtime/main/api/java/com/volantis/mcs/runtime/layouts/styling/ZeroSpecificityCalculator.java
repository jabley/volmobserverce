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

package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.styling.compiler.Specificity;
import com.volantis.styling.compiler.SpecificityCalculator;

/**
 * A {@link SpecificityCalculator} that always returns a specificity of the
 * lowest possible value.
 *
 * <p>This class always returns the same specificity object each time it is
 * called.</p>
 */
public class ZeroSpecificityCalculator
        implements SpecificityCalculator {

    /**
     * The specificity with the lowest possible value.
     */
    private Specificity specificity;

    /**
     * Initialise.
     *
     * @param calculator The normal calculator to use to create the
     * specificity that is returned by this class.
     */
    public ZeroSpecificityCalculator(SpecificityCalculator calculator) {
        calculator.reset();
        this.specificity = calculator.getSpecificity();
    }

    // Javadoc inherited.
    public void addAttributeSelector() {
    }

    // Javadoc inherited.
    public void addClassSelector() {
    }

    // Javadoc inherited.
    public void addElementSelector() {
    }

    // Javadoc inherited.
    public void addIDSelector() {
    }

    // Javadoc inherited.
    public void addPseudoClassSelector() {
    }

    // Javadoc inherited.
    public void addPseudoElementSelector() {
    }

    // Javadoc inherited.
    public Specificity getSpecificity() {
        return specificity;
    }

    // Javadoc inherited.
    public void reset() {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
