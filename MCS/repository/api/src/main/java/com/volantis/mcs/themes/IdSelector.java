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
 * A selector that matches a specified ID.
 *
 * <p>Rendered in CSS as <code>a#identity</code>.
 *
 * @mock.generate base="Selector"
 */
public interface IdSelector extends Selector {
    /**
     * Gets the ID.
     *
     * @return The ID
     */
    public String getId();

    /**
     * Sets the ID.
     *
     * @param newId The ID
     */
    public void setId(String newId);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Fixed issue with build

 21-Jul-05	8713/2	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
