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

package com.volantis.mcs.themes.model;

import com.volantis.mcs.model.descriptor.AbstractPropertyAccessor;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.styling.properties.StyleProperty;

public class PropertyValueAccessor
        extends AbstractPropertyAccessor {

    private final StyleProperty property;

    public PropertyValueAccessor(StyleProperty property) {
        this.property = property;
    }

    public Object get(Object object) {
        MutableStyleProperties properties = (MutableStyleProperties) object;
        return properties.getPropertyValue(property);
    }

    public void set(Object object, Object value) {
        MutableStyleProperties properties = (MutableStyleProperties) object;
        if (value == null) {
            properties.clearPropertyValue(property);
        } else {
            properties.setPropertyValue((PropertyValue) value);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Dec-05	10512/1	pduffin	Quick commit for GUI fixes

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 31-Oct-05	9961/4	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
