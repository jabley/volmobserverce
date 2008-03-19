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

package com.volantis.mcs.build.themes.definitions;

import com.volantis.mcs.build.themes.definitions.values.Value;
import com.volantis.mcs.build.themes.definitions.values.ValueSource;

/**
 * A representation of the meta information for a theme stylistic property.
 */
public interface Property extends Named, Typed {

    /**
     * Set the property's initial value.
     * @param initialValue The property's initial value.
     */
    public void setInitialValue(Value initialValue);

    /**
     * Get the property's initial value.
     * @return The property's initial value.
     */
    public Value getInitialValue();


    public ValueSource getInitialValueSource();

    public void setInitialValueSource(ValueSource initialValueSource);
    
    /**
     * Query whether the property is inherited by default.
     * @return True if the property is inherited by default
     */
    public boolean isInherited();

    /**
     * Specify whether the property is inherited by default.
     * @param inherit True if the property is inherited by default
     */
    public void setInherited(boolean inherit);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Oct-04	5968/1	adrianj	VBM:2004083105 Add inherited property to StylePropertyDetails

 21-Oct-04	5833/2	adrianj	VBM:2004082605 Fix initial values for StylePropertyDetails

 25-Mar-04	3550/1	pduffin	VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 ===========================================================================
*/
