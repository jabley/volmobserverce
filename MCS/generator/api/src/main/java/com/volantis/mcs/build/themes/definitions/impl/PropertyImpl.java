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

import com.volantis.mcs.build.themes.definitions.Property;
import com.volantis.mcs.build.themes.definitions.types.Type;
import com.volantis.mcs.build.themes.definitions.values.Value;
import com.volantis.mcs.build.themes.definitions.values.ValueSource;

/**
 * Representation of a theme property.
 */
public class PropertyImpl extends NamedImpl implements Property {
    /**
     * The initial value of the property.
     */
    private Value initialValue;

    /**
     * The type of the property.
     */
    private Type type;

    /**
     * The source of the initial value of the property.
     */
    private ValueSource initialValueSource;

    /**
     * Whether the property is inherited by default
     */
    private boolean inherited;

    // Javadoc inherited
    public void setInitialValue(Value initialValue) {
        this.initialValue = initialValue;
    }

    // Javadoc inherited
    public Value getInitialValue() {
        return initialValue;
    }

    public ValueSource getInitialValueSource() {
        return initialValueSource;
    }

    public void setInitialValueSource(ValueSource initialValueSource) {
        this.initialValueSource = initialValueSource;
    }

    // Javadoc inherited
    public boolean isInherited() {
        return inherited;
    }

    // Javadoc inherited
    public void setInherited(boolean inherit) {
        inherited = inherit;
    }

    // Javadoc inherited
    public Type getType() {
        return type;
    }

    // Javadoc inherited
    public void setType(Type type) {
        this.type = type;
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

 26-Oct-04	5968/1	adrianj	VBM:2004083105 Add inherited property to StylePropertyDetails

 21-Oct-04	5833/2	adrianj	VBM:2004082605 Fix initial values for StylePropertyDetails

 25-Mar-04	3550/1	pduffin	VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 ===========================================================================
*/
