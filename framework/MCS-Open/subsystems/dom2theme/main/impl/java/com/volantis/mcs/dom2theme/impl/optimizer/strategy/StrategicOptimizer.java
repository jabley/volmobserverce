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

package com.volantis.mcs.dom2theme.impl.optimizer.strategy;

import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandAnalyzer;
import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.TargetEntity;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.styling.values.PropertyValues;

import java.util.List;

/**
 * Optimizes the sets of properties by choosing the best out of a number of
 * different strategies.
 */
public class StrategicOptimizer
        implements ShorthandOptimizer {

    /**
     * The analyzer for the properties.
     */
    private final ShorthandAnalyzer analyzer;

    /**
     * The different strategies to try for the properties.
     */
    private final OptimizeStrategy[] strategies;

    /**
     * The set of properties required for shorthand usage.
     */
    private final MutableStylePropertySet requiredForShorthand;

    /**
     * The set of properties required for individual usage.
     */
    private final MutableStylePropertySet requiredForIndividual;

    /**
     * Initialise.
     *
     */
    protected StrategicOptimizer(
            ShorthandAnalyzer analyzer,
            List strategies) {

        this.analyzer = analyzer;

        this.strategies = new OptimizeStrategy[strategies.size()];
        strategies.toArray(this.strategies);

        requiredForIndividual = new MutableStylePropertySetImpl();
        requiredForShorthand = new MutableStylePropertySetImpl();
    }

    // Javadoc inherited.
    public void optimize(
            TargetEntity target, PropertyValues inputValues,
            MutableStyleProperties outputValues,
            DeviceValues deviceValues) {

        // Analyse the border properties.
        analyzer.analyze(target, inputValues, deviceValues);

        if (!analyzer.allClearable()) {
            // Try all the strategies in turn checking to see what the cost is
            // of using that approach to handle the properties.
            int bestCost = Integer.MAX_VALUE;
            OptimizeStrategy bestStrategy = null;

            requiredForIndividual.clear();
            requiredForShorthand.clear();

            for (int i = 0; i < strategies.length; i++) {
                OptimizeStrategy strategy = strategies[i];

                // Initialise the set of required properties. There is no need
                // to clear the set as we are always adding the same set of
                // properties.
                analyzer.addRequired(requiredForIndividual,
                        requiredForShorthand);

                int cost = strategy.cost(analyzer, inputValues,
                        requiredForIndividual, requiredForShorthand);
                if (cost < bestCost) {
                    bestStrategy = strategy;
                    bestCost = cost;

                    // If the strategy cannot be beaten then don't check any
                    // more.
                    if (strategy.alwaysUseIfBest()) {
                        break;
                    }
                }
            }

            // Initialise the set of required properties as the strategies will
            // clear the bits for those properties that they handle so that
            // following strategies will know what is left.
            analyzer.addRequired(requiredForIndividual, requiredForShorthand);
            bestStrategy.update(analyzer, outputValues, requiredForIndividual,
                    requiredForShorthand);
        }
    }

    // Javadoc inherited.
    public void removeProperties(MutableStylePropertySet individualProperties) {
        analyzer.removeProperties(individualProperties);
    }
}
