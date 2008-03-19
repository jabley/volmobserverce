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
 * Representation of a pseudo element selector.
 *
 * <p>Examples in CSS would include:
 * <ul>
 * <li><code>:first-line</code></li>
 * <li><code>:mcs-shortcut:after</code></li>
 * </ul>
 * </p>
 *
 * @mock.generate base="Selector"
 */
public interface PseudoElementSelector extends Selector {
    /**
     * Returns the type of pseudo element selector, using a typesafe
     * enumeration.
     *
     * @return The type of pseudo element selector
     */
    public PseudoElementTypeEnum getPseudoElementType();

    /**
     * Returns the invalid pseudo element type (if the type is
     * {@link com.volantis.mcs.themes.PseudoElementTypeEnum#INVALID}).
     *
     * @return The invalid pseudo element type
     */
    public String getInvalidPseudoElement();

    /**
     * Sets the invalid pseudo element type (if the type is
     * {@link com.volantis.mcs.themes.PseudoElementTypeEnum#INVALID}).
     *
     * @param newInvalid The new invalid pseudo element type
     */
    public void setInvalidPseudoElement(String newInvalid);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Fixed issue with build

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
