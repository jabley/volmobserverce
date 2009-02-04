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

import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandAnalyzer;
import com.volantis.mcs.dom2theme.impl.optimizer.strategy.OptimizeStrategy;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.styling.properties.MutableStylePropertySet;

/**
 * A strategy that will handle the mcs-system-font being set.
 */
public class SystemFontStrategy
        implements OptimizeStrategy {

    // Javadoc inherited.
    public boolean alwaysUseIfBest() {
        return true;
    }

    // Javadoc inherited.
    public int cost(
            ShorthandAnalyzer shorthandAnalyzer, StyleValues inputValues,
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {

        FontAnalyzer analyzer = (FontAnalyzer) shorthandAnalyzer;

        if (analyzer.isSystemFontRequired()) {

            return analyzer.getShorthandCost(requiredForIndividual, 
                    requiredForShorthand);

        } else {
            return Integer.MAX_VALUE;
        }
    }

    // Javadoc inherited.
    public void update(
            ShorthandAnalyzer shorthandAnalyzer,
            MutableStyleProperties outputValues,
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {

        FontAnalyzer analyzer = (FontAnalyzer) shorthandAnalyzer;

        analyzer.updateShorthand(outputValues, requiredForShorthand, requiredForShorthand);
    }
}
