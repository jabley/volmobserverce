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

package com.volantis.mcs.build.themes.definitions.impl;

import com.volantis.mcs.build.themes.definitions.Named;
import com.volantis.mcs.build.themes.definitions.values.ValueSource;

/**
 * Base class for those classes that implement the {@link Named} interface.
 */
public abstract class NamedImpl
    implements Named {

    /**
     * The name.
     */
    private String name;

    /**
     * Set the value of the name property.
     * @param name The new value of the name property.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get the value of the name property.
     * @return The value of the name property.
     */
    public String getName() {
        return name;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Mar-04	3550/1	pduffin	VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 ===========================================================================
*/
