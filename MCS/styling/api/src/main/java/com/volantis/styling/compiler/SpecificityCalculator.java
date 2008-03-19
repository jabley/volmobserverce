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

package com.volantis.styling.compiler;


/**
 * Represents a CSS specificity calculation.
 *
 * The specificity for a <code>Matcher</code> is calculated according to the
 * <a href="http://www.w3.org/TR/CSS21/cascade.html#specificity">
 * 2.1 Specification</a>. It stores the specificity as an int in base 100.
 *
 * There are four rules used to calculate the specificity.
 *
 * A. # count 1 if the selector is a 'style' attribute rather than a selector,
 * 0 otherwise (= a) (In HTML, values of an element's "style" attribute are
 * style sheet rules. These rules have no selectors, so a=1, b=0, c=0, and
 * d=0.)
 * B. # count the number of ID attributes in the selector (= b)
 * C. # count the number of other attributes and pseudo-classes in the selector
 * (= c)
 * D. # count the number of element names and pseudo-elements in the selector
 * (= d)
 *
 * @mock.generate
 */
public interface SpecificityCalculator {

    /**
     * Add an element selector to the specificity.
     */
    void addElementSelector();

    /**
     * Add a pseudo element selector to the specificity.
     */
    void addPseudoElementSelector();

    /**
     * Add a class selector to the specificity.
     */
    void addClassSelector();

    /**
     * Add a pseudo class selector to the specificity.
     */
    void addPseudoClassSelector();

    /**
     * Add an attribute selector to the specificity.
     */
    void addAttributeSelector();

    /**
     * Add an id selector to the specificity.
     */
    void addIDSelector();

    /**
     * Get the specificity that has been calculated.
     *
     * @return The specificity.
     */
    Specificity getSpecificity();

    /**
     * Reset the calculator so that it can be used again.
     */
    void reset();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
