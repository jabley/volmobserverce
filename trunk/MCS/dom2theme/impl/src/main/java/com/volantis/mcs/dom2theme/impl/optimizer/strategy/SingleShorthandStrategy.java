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

package com.volantis.mcs.dom2theme.impl.optimizer.strategy;

import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandAnalyzer;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.styling.properties.MutableStylePropertySet;

/**
 * A strategy that attempts to use a single shorthand.
 */
public class SingleShorthandStrategy
        implements OptimizeStrategy {

    /**
     * Indicates whether this strategy should always be used if it is the best
     * one.
     */
    private final boolean alwaysUseIfBest;

    /**
     * Initialise.
     *
     * @param alwaysUseIfBest Indicates whether this strategy should always be
     *                        used if it is the best one.
     */
    public SingleShorthandStrategy(boolean alwaysUseIfBest) {
        this.alwaysUseIfBest = alwaysUseIfBest;
    }

    // Javadoc inherited.
    public boolean alwaysUseIfBest() {
        return alwaysUseIfBest;
    }

    // Javadoc inherited.
    public int cost(
            ShorthandAnalyzer shorthandAnalyzer, StyleValues inputValues,
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {
        if (shorthandAnalyzer.canUseShorthand()) {
            return shorthandAnalyzer.getShorthandCost(requiredForShorthand,
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
        shorthandAnalyzer.updateShorthand(outputValues,
                requiredForIndividual,
                requiredForShorthand);
    }
}
