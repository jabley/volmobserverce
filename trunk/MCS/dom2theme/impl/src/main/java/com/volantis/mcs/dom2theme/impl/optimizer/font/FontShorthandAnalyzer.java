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

package com.volantis.mcs.dom2theme.impl.optimizer.font;

import com.volantis.mcs.dom2theme.impl.optimizer.BasicShorthandAnalyzer;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertyClearerChecker;
import com.volantis.mcs.dom2theme.impl.optimizer.TargetEntity;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.FontShorthandValue;
import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.values.PropertyValues;

/**
 * A specialized analyzer for the font shorthand properties.
 */
public class FontShorthandAnalyzer
        extends BasicShorthandAnalyzer {

    /**
     * Initialise.
     *
     * @param checker             The checker.
     * @param supportedShorthands The set of supported shorthands.
     */
    public FontShorthandAnalyzer(
            PropertyClearerChecker checker,
            ShorthandSet supportedShorthands) {
        super(StyleShorthands.FONT, PropertyGroups.FONT_PROPERTIES,
                checker, supportedShorthands);
    }

    // Javadoc inherited.
    public void analyze(
            TargetEntity target, PropertyValues inputValues,
            DeviceValues deviceValues) {
        super.analyze(target, inputValues, deviceValues);

        // The family and size are always required for font shorthand.
        requiredForShorthand |= FontAnalyzer.FAMILY_BIT;
        requiredForShorthand |= FontAnalyzer.SIZE_BIT;
    }

    // Javadoc inherited.
    protected ShorthandValue createShorthandValue(
            StyleValue[] values, Priority priority) {

        // Create a specialized shorthand value.
        return new FontShorthandValue(shorthand, values, priority);
    }
}
