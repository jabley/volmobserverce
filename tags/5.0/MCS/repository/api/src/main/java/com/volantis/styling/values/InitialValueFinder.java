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

package com.volantis.styling.values;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.styling.properties.PropertyDetails;
import com.volantis.styling.properties.StyleProperty;

/**
 * Calculate the initial value from the set of values for the given property
 * details.
 *
 * @mock.generate
 */
public class InitialValueFinder
    implements InitialValueSourceVisitor {

    /**
     * The values that may be checked for dependent initial values.
     */
    private StyleValues values;

    /**
     * The found initial value.
     */
    private StyleValue initialValue;

    /**
     * Get the initial value for the specified combination of values and
     * property details.
     *
     * @param values the fully populated set of style values
     * @param details the details of the property.
     * @return the calculated initial value, may be null
     */
    public StyleValue getInitialValue(
            StyleValues values, PropertyDetails details) {

        this.values = values;
        initialValue = null;
        InitialValueSource source = details.getInitialValueSource();
        if (source != null) {
            source.accept(this);
        }
        return initialValue;
    }

    // Javadoc inherited.
    public void visit(FixedInitialValue source) {
        initialValue = source.getValue();
    }

    // Javadoc inherited.
    public void visit(PropertyInitialValue source) {
        StyleProperty other = source.getProperty();
        initialValue = values.getStyleValue(other);
    }

    // Javadoc inherited.
    public void visit(ComputedInitialValue source) {
        StyleProperty dependentProperty = source.getProperty();
        final StyleValue sourceValue = values.getStyleValue(dependentProperty);
        initialValue = source.resolve(sourceValue);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 ===========================================================================
*/
