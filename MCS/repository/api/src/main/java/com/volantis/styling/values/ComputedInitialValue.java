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

import com.volantis.styling.properties.StyleProperty;
import com.volantis.mcs.themes.StyleValue;

import java.util.Map;

/**
 * Calculate the appropriate initial StyleValue for this StyleProperty
 * given the value of another StyleProperty. 
 */
public class ComputedInitialValue extends PropertyInitialValue {

    private final Map ruleSet;

    public ComputedInitialValue(StyleProperty property, Map rules) {
        super(property);
        ruleSet = rules;
    }

    public void accept(InitialValueSourceVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Given the StyleValue of one StyleProperty find the appropriate initial
     * StyleValue of this StyleProperty.
     *
     * @param dependentStyleValue to be looked up
     * @return the correct style value for this property or null if not found
     */
    public StyleValue resolve(StyleValue dependentStyleValue) {
        return (StyleValue) ruleSet.get(dependentStyleValue);
    }

    // javadoc inherited
    public boolean isComputed() {
        return true;
    }

}
