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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes;



/**
 * Representation of a pseudo class selector.
 *
 * <p>Examples in CSS would include:
 * <ul>
 * <li><code>:first-child</code></li>
 * <li><code>:visited</code></li>
 * <li><code>:hover</code></li>
 * </ul>
 * </p>
 *
 * @mock.generate base="Selector"
 */
public interface PseudoClassSelector extends Selector {

    /**
     * Returns the type of pseudo element selector, using a typesafe
     * enumeration.
     *
     * @return The type of pseudo element selector
     */
    public PseudoClassTypeEnum getPseudoClassType();

    /**
     * If the type of the pseudo-class is {@link com.volantis.mcs.themes.PseudoClassTypeEnum#INVALID}
     * then this property contains the invalid representation of this selector.
     *
     * @return The invalid string representation of this selector
     */
    public String getInvalidPseudoClass();

    /**
     * If the type of the pseudo-class is {@link com.volantis.mcs.themes.PseudoClassTypeEnum#INVALID}
     * then this property contains the invalid representation of this selector.
     *
     * @param newInvalid The invalid string representation of this selector
     */
    public void setInvalidPseudoClass(String newInvalid);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/3	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
