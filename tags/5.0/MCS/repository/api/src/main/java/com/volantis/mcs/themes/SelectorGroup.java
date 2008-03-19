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

import com.volantis.mcs.model.validation.Validatable;

import java.util.List;

/**
 * A group of selectors that share a common set of style properties.
 *
 * <p>Rendered in CSS as <code>Sel1, Sel2, Sel3</code>.</p>
 */
public interface SelectorGroup extends Validatable {
    /**
     * Returns a list of Selector objects comprising this selector group.
     *
     * <p>Note that although it would be nice for this to use a typesafe
     * interface to the list, there are also benefits to making use of the
     * standard Collections API. At such time as we switch to JDK1.5 for
     * compilation, typed collections can be used.</p>
     *
     * @return The list of selectors associated with this selector group. If
     *         no list already exists, a new one should be created; null is
     *         not a valid return value for this method.
     */
    public List getSelectors();

    /**
     * Sets the list of Selector objects comprising this selector group.
     *
     * @param newSelectors The list of selectors associated with this selector
     *                     group.
     */
    public void setSelectors(List newSelectors);
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
