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

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.styles.PositivePixelLengthChecker;
import com.volantis.mcs.protocols.styles.PropertyChecker;
import com.volantis.mcs.protocols.styles.ValueChecker;
import com.volantis.mcs.protocols.styles.ValueCheckerImpl;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.Styles;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;

/**
 * Checks to see whether a pane needs to be rendered as a table.
 */
public final class XHTMLFullPaneTableChecker {

    private PropertyChecker widthChecker;
    private final ValueCheckerImpl borderWidthChecker;
    private final ValueCheckerImpl paddingChecker;
    private final ValueChecker borderSpacingChecker;

    public XHTMLFullPaneTableChecker() {

        ValueCheckerImpl lengthGreaterThanZero = new PositivePixelLengthChecker();

//        widthChecker = new PixelLengthOrPercentageGreaterThanZero();

        // A border width is significant if its pixel length is greater than 0.
        borderWidthChecker = lengthGreaterThanZero;

        // A border spacing is significant is its pixel length is greater than 0.
        borderSpacingChecker = lengthGreaterThanZero;

        // Padding is significant if it's pixel length is greater than 0.
        paddingChecker = lengthGreaterThanZero;
    }

    public boolean checkTable(Styles styles) {

        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue value;

        value = propertyValues.getComputedValue(StylePropertyDetails.WIDTH);
        boolean hasWidth = widthChecker.isSignificant(styles);

        boolean hasBorderWidth = checkEdges(
                propertyValues, PropertyGroups.BORDER_WIDTH_PROPERTIES,
                borderWidthChecker);

        boolean hasCellPadding = checkEdges(
                propertyValues, PropertyGroups.PADDING_PROPERTIES,
                paddingChecker);

        value = propertyValues.getComputedValue(
                StylePropertyDetails.BORDER_SPACING);
        boolean hasCellSpacing = borderSpacingChecker.isSignificant(value);

        return hasWidth || hasBorderWidth || hasCellPadding || hasCellSpacing;
    }

    private boolean checkEdges(
            PropertyValues propertyValues,
            StyleProperty[] properties,
            ValueChecker checker) {

        for (int i = 0; i < properties.length; i++) {
            StyleProperty property = properties[i];
            StyleValue value = propertyValues.getComputedValue(property);
            if (checker.isSignificant(value)) {
                return true;
            }
        }

        return false;
    }

    public void setWidthChecker(PropertyChecker widthChecker) {
        this.widthChecker = widthChecker;
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
