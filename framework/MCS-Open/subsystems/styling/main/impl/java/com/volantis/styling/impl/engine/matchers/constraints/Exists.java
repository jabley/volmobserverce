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

package com.volantis.styling.impl.engine.matchers.constraints;

import com.volantis.styling.debug.DebugStylingWriter;

/**
 * Test whether the attribute exists, i.e. the value is not null.
 */
public class Exists
        implements ValueConstraint {

    // Javadoc inherited.
    public boolean satisfied(String value) {
        return value != null;
    }

    // Javadoc inherited.
    public void debug(DebugStylingWriter writer) {
    }

    public int hashCode() {
        return ValueConstraint.class.hashCode();
    }

    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == getClass();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
