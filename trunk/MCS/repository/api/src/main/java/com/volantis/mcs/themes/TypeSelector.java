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

import com.volantis.mcs.model.property.PropertyIdentifier;

import java.util.Arrays;
import java.util.List;

/**
 * A selector that matches a specified element type.
 *
 * <p>Rendered in CSS as <code>typeName</code>.</p>
 *
 * @mock.generate base="ElementSelector"
 */
public interface TypeSelector extends ElementSelector {

    /**
     * Used to identify this class when logging validation errors.
     */
    PropertyIdentifier TYPE_SELECTOR = new PropertyIdentifier(
            TypeSelector.class, "typeSelector");

    /**
     * A list of all valid types
     */
    List VALID_TYPES = Arrays.asList(ThemeConstants.TYPE_LIST);

    /**
     * Returns the type for this selector.
     *
     * @return The type for this selector
     */
    public String getType();

    /**
     * Sets the type for this selector.
     *
     * @param newType The type for this selector
     */
    public void setType(String newType);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9409/1	geoff	VBM:2005083007 Move over to using the new themes model.

 21-Jul-05	8713/2	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
