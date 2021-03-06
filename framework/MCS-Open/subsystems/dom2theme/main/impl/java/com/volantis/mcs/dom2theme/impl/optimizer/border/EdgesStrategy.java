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

package com.volantis.mcs.dom2theme.impl.optimizer.border;

import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandAnalyzer;
import com.volantis.mcs.dom2theme.impl.optimizer.strategy.OptimizeStrategy;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.styling.properties.MutableStylePropertySet;

/**
 * A strategy that attempts to use the <code>border-top</code>,
 * <code>border-bottom</code>, <code>border-left</code>, and
 * <code>border-right</code> shorthands followed by individual properties
 * where necessary.
 */
public class EdgesStrategy
        implements OptimizeStrategy {

    /**
     * The individual property strategy.
     */
    private final OptimizeStrategy individualStrategy;

    /**
     * Initialise.
     *
     * @param individualStrategy The individual property strategy.
     */
    public EdgesStrategy(OptimizeStrategy individualStrategy) {
        this.individualStrategy = individualStrategy;
    }

    // Javadoc inherited.
    public boolean alwaysUseIfBest() {
        return false;
    }

    // Javadoc inherited.
    public int cost(
            ShorthandAnalyzer shorthandAnalyzer, StyleValues inputValues,
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {

        BorderAnalyzer analyzer = (BorderAnalyzer) shorthandAnalyzer;

        int cost = 0;

        for (int i = 0; i < PropertyGroups.EDGE_COUNT; i += 1) {
            ShorthandAnalyzer edgeAnalyzer = analyzer.getEdgeAnalyzer(i);

            if (edgeAnalyzer.canUseShorthand()) {
                cost += edgeAnalyzer.getShorthandCost(requiredForIndividual, requiredForShorthand);
            }
        }

        cost += individualStrategy.cost(analyzer, inputValues, requiredForIndividual, requiredForShorthand);

        return cost;
    }

    // Javadoc inherited.
    public void update(
            ShorthandAnalyzer shorthandAnalyzer,
            MutableStyleProperties outputValues,
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {

        BorderAnalyzer analyzer = (BorderAnalyzer) shorthandAnalyzer;

        for (int i = 0; i < PropertyGroups.EDGE_COUNT; i += 1) {
            ShorthandAnalyzer edgeAnalyzer = analyzer.getEdgeAnalyzer(i);

            if (edgeAnalyzer.canUseShorthand()) {
                edgeAnalyzer.updateShorthand(outputValues, requiredForIndividual, requiredForShorthand);
            }
        }

        individualStrategy.update(shorthandAnalyzer, outputValues, requiredForIndividual, requiredForShorthand);
    }
}
