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

package com.volantis.mcs.dom2theme.impl.optimizer;

import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.values.PropertyValues;
import com.volantis.mcs.themes.MutableStyleProperties;

/**
 * Base for all {@link ShorthandOptimizer}s that deal with properties that
 * can belong to a single shorthand, i.e. not border properties.
 */
public class AbstractShorthandOptimizer
        implements ShorthandOptimizer {

    /**
     * The analyzer for the shorthand.
     */
    private final BasicShorthandAnalyzer analyzer;

    /**
     * Initialise.
     *
     * @param analyzer The analyzer.
     */
    protected AbstractShorthandOptimizer(BasicShorthandAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    // Javadoc inherited.
    public void optimize(
            TargetEntity target, PropertyValues inputValues,
            MutableStyleProperties outputValues,
            DeviceValues deviceValues) {

        analyzer.analyze(target, inputValues, deviceValues);
        if (analyzer.canUseShorthand()) {
            analyzer.updateShorthand(outputValues);
        } else {
            // Update the individual properties.
            analyzer.updateProperties(outputValues);
        }
    }

    // Javadoc inherited.
    public void removeProperties(MutableStylePropertySet individualProperties) {
        analyzer.removeProperties(individualProperties);
    }
}
