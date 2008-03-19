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

public class ValueHandlerToPropertyAdapter
        extends AbstractPropertyHandler {

    private final StyleProperty property;

    private final ValueHandler valueHandler;

    private final PropertyUpdater significantUpdater;

    public ValueHandlerToPropertyAdapter(
            StyleProperty property, ValueHandler valueHandler) {
        this(property, valueHandler, NoopPropertyUpdater.getDefaultInstance());
    }

    public ValueHandlerToPropertyAdapter(
            StyleProperty property, ValueHandler valueHandler,
            PropertyUpdater propertyUpdater) {
        this.property = property;
        this.valueHandler = valueHandler;
        this.significantUpdater = propertyUpdater;
    }

    public boolean isSignificant(PropertyValues propertyValues) {
        StyleValue styleValue = propertyValues.getComputedValue(property);
        return valueHandler.isSignificant(styleValue);
    }

    public String getAsString(MutablePropertyValues propertyValues) {
        StyleValue styleValue = propertyValues.getComputedValue(property);
        String string = valueHandler.getAsString(styleValue);
        if (string != null) {
            significantUpdater.update(property, propertyValues);
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
