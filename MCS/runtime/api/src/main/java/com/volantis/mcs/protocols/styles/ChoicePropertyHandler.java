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

import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks a number of possible choices in order and the first one that has
 * a significant value is used.
 */
public class ChoicePropertyHandler
        extends AbstractPropertyHandler {

    private final List handlers;

    public ChoicePropertyHandler() {
        handlers = new ArrayList();
    }

    protected void addHandler(StyleProperty property, ValueHandler valueHandler) {
        addHandler(property, valueHandler,
                   NoopPropertyUpdater.getDefaultInstance());
    }

    private void addHandler(StyleProperty property, ValueHandler valueHandler,
                           PropertyUpdater propertyUpdater) {
        handlers.add(new ValueHandlerToPropertyAdapter(
                property, valueHandler, propertyUpdater));
    }

    public void addHandlers(StyleProperty[] properties, ValueHandler valueHandler) {
        addHandlers(properties, valueHandler,
                    NoopPropertyUpdater.getDefaultInstance());
    }

    private void addHandlers(StyleProperty[] properties,
                            ValueHandler valueHandler,
                           PropertyUpdater propertyUpdater) {
        for (int i = 0; i < properties.length; i++) {
            StyleProperty property = properties[i];
            addHandler(property,  valueHandler, propertyUpdater);
        }
    }

    public boolean isSignificant(PropertyValues propertyValues) {

        for (int i = 0; i < handlers.size(); i++) {
            PropertyHandler combinedValueHandler =
                    (PropertyHandler) handlers.get(i);
            if (combinedValueHandler.isSignificant(propertyValues)) {
                return true;
            }
        }

        return false;
    }

    public String getAsString(MutablePropertyValues propertyValues) {

        for (int i = 0; i < handlers.size(); i++) {
            PropertyHandler combinedValueHandler =
                    (PropertyHandler) handlers.get(i);
            String string = combinedValueHandler.getAsString(
                    propertyValues);
            if (string != null) {
                return string;
            }
        }

        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
