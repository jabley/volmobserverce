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

package com.volantis.mcs.protocols.html.xhtmlfull;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.NodeIteratee;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.styles.PropertyUpdater;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.styling.Styles;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

public class CellPaddingAnalyser
        extends RecursingDOMVisitor
        implements NodeIteratee {

    private static final int TABLE_DEPTH = 0;
    private static final int ROW_DEPTH = 1;
    private static final int CELL_DEPTH = 2;

    private int total;
    private int count;

    private int depth;
    private PropertyUpdater propertyUpdater;

    public String calculateCellPadding(
            Element table, PropertyUpdater propertyUpdater) {
        String name = table.getName();
        if (!"table".equals(name)) {
            throw new IllegalArgumentException(
                    "Element " + name + " is not a table");
        }

        depth = TABLE_DEPTH;
        total = 0;
        count = 0;
        this.propertyUpdater = propertyUpdater;
        table.forEachChild(this);

        return count > 0 ? String.valueOf(total / count) : "0";
    }

    public void visit(Element element) {
        String name = element.getName();
        depth += 1;
        if (depth == ROW_DEPTH) {
            if (!"tr".equals(name)) {
                throw new IllegalStateException(
                        "Element " + name + " is not a tr");
            }
            element.forEachChild(this);
        } else if (depth == CELL_DEPTH) {
            if (!"td".equals(name)) {
                throw new IllegalStateException(
                        "Element " + name + " is not a tr");
            }

            Styles styles = element.getStyles();
            if (styles != null) {
                MutablePropertyValues propertyValues = styles.getPropertyValues();
                for (int i = 0; i < PropertyGroups.PADDING_PROPERTIES.length; i++) {
                    StyleProperty property = PropertyGroups.PADDING_PROPERTIES[i];
                    StyleValue value = propertyValues.getComputedValue(property);
                    propertyUpdater.update(property, propertyValues);
                    int length = getPixelLength(value);
                    if (length >= 0) {
                        total += length;
                        count += 1;
                    }
                }
            }

        } else {
            throw new IllegalStateException("Depth " + depth + " unknown");
        }
        depth -= 1;
    }

    private int getPixelLength(StyleValue value) {
        if (value instanceof StyleLength) {
            StyleLength length = (StyleLength) value;
            LengthUnit units = length.getUnit();
            if (units == LengthUnit.PX) {
                int number = (int) length.getNumber();
                if (number > 0) {
                    return number;
                }
            }
        }

        return -1;
    }

    public void visit(Text text) {
        throw new IllegalStateException("This should never happen");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
