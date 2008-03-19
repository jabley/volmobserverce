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
 * Indicates that a selector can have an associated namespace prefix.
 *
 * <p>Rendered in CSS as <code>prefix|selector</code>
 */
public interface Namespaced {
    /**
     * Gets the namespace prefix for this selector.
     *
     * @return The namespace prefix for this selector.
     */
    public String getNamespacePrefix();

    /**
     * Sets the namespace for this selector.
     *
     * @param newNamespacePrefix The namespace prefix for this selector
     */
    public void setNamespacePrefix(String newNamespacePrefix);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Sep-05	9407/2	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/2	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
