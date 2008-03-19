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
package com.volantis.mcs.protocols;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.Styles;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Provides mechanisum for filtering the style properties of an MCSAttribute
 */
public class MCSAttributeStylePropertyFilter {

    /**
     * Remove the specified StyleProperties from the MCSAttribute if they are
     * equal to the default value
     * @param protocolConfiguration
     * @param attributes
     * @param styleProperties
     */
    public static void filterOutAttributeDefaultValues(
                                ProtocolConfiguration protocolConfiguration,
                                String elementType,
                                MCSAttributes attributes,
                                StyleProperty[] styleProperties) {

        Styles styles = attributes.getStyles();
        MutablePropertyValues propertyValues = styles.getPropertyValues();

        for (int i = 0; i < styleProperties.length; i++) {
            StyleProperty prop = styleProperties[i];

            StyleValue styleValue = propertyValues.getComputedValue(prop);

            if (protocolConfiguration.isElementAttributeDefaultStyle(
                                                            elementType,
                                                            prop,
                                                            styleValue)) {
                propertyValues.setComputedValue(prop, null);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 14-Sep-05	9472/3	ibush	VBM:2005090808 Add default styling for sub/sup elements

 09-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 ===========================================================================
*/
