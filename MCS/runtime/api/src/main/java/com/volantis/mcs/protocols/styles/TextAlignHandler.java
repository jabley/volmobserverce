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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.styles;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;

/**
 * Text align handler which delegates to the supplied handler if the text align
 * property value is not {@link TextAlignKeywords#_INTERNAL_DEFERRED_INHERIT}.
 * <p/>
 * NB: This is required to fix
 * http://mantis.uk.volantis.com:8080/mantis/Mantis_View.jsp?mantisid=2006091803
 * Ideally the way that style attributes are rendered out should be revisited.
 */
public class TextAlignHandler extends ValueHandlerToPropertyAdapter {

    /**
     * Initialise.
     *
     * @param valueHandler The handler for the value.
     * @param updater      The updater for the property.
     */ 
    public TextAlignHandler(
            ValueHandler valueHandler,
            PropertyUpdater updater) {
        super(StylePropertyDetails.TEXT_ALIGN, valueHandler, updater);
    }

    // Javadoc inherited.
    public String getAsString(MutablePropertyValues propertyValues) {
        String returnValue = null;
        final StyleValue value = propertyValues.getComputedValue(
                StylePropertyDetails.TEXT_ALIGN);
        if (value != TextAlignKeywords._INTERNAL_DEFERRED_INHERIT) {
            returnValue = super.getAsString(propertyValues);
        }
        return returnValue;
    }

    // Javadoc inherited.
    public String getAsString(Styles styles) {
        return getAsString(styles.getPropertyValues());
    }

    // Javadoc inherited.
    public boolean isSignificant(PropertyValues propertyValues) {
        boolean isSignificant = false;
        final StyleValue value = propertyValues.getComputedValue(
                StylePropertyDetails.TEXT_ALIGN);
        if (value != TextAlignKeywords._INTERNAL_DEFERRED_INHERIT) {
            isSignificant = super.isSignificant(propertyValues);
        }
        return isSignificant;
    }

    // Javadoc inherited.
    public boolean isSignificant(Styles styles) {
        return isSignificant(styles.getPropertyValues());
    }
}
