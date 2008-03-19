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

package com.volantis.mcs.protocols.styles;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;

public class EdgePropertyHandler
        extends AbstractPropertyHandler {

    private final StyleProperty[] properties;
    private final ValueHandler valueHandler;
    private final PropertyUpdater significantUpdater;

    public EdgePropertyHandler(StyleProperty[] properties,
                               ValueHandler valueHandler,
                               PropertyUpdater significantUpdater) {
        this.properties = properties;
        this.valueHandler = valueHandler;
        this.significantUpdater = significantUpdater;
    }

    public boolean isSignificant(PropertyValues propertyValues) {
        for (int i = 0; i < properties.length; i++) {
            StyleProperty property = properties[i];
            StyleValue value = propertyValues.getComputedValue(property);
            if (valueHandler.isSignificant(value)) {
                return true;
            }
        }
        
        return false;
    }

    public String getAsString(MutablePropertyValues propertyValues) {
        String string = null;
        for (int i = 0; i < properties.length; i++) {
            StyleProperty property = properties[i];
            StyleValue value = propertyValues.getComputedValue(property);
            string = valueHandler.getAsString(value);
            if (string != null) {
                break;
            }
        }

        // If the value is significant then update the property.
        if (string != null) {
            for (int i = 0; i < properties.length; i++) {
                StyleProperty property = properties[i];
                significantUpdater.update(property, propertyValues);
            }
        }
        
        return string;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
