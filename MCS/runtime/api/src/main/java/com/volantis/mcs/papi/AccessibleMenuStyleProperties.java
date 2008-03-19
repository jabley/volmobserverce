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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi;

import com.volantis.styling.values.PropertyValues;

/**
 * Interface that allows implementors to return their menu style properties.
 * This is needed because the menu builder does not expose this information
 * and because the css cascade/inherit information is not available during
 * PAPI menu processing.
 */
public interface AccessibleMenuStyleProperties {

    /**
     * Get implementors menu style properties.
     *
     * @return The style properties defined for the menu implementing this
     */
    public PropertyValues getPropertyValues();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Mar-04	3641/3	claire	VBM:2004032602 Tidying menu types and styles in PAPI

 30-Mar-04	3641/1	claire	VBM:2004032602 Using menu types and styles in PAPI

 ===========================================================================
*/
