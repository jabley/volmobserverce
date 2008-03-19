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
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.styling.properties.MutableStylePropertySet;

/**
 * There are many different ways of trying to make use of border shorthands and
 * they are represented by instances of this.
 */
public interface OptimizeStrategy {

    /**
     * Indicates whether there is any point in trying any of the other
     * strategies.
     *
     * @return True if this strategy will always win over the others and false otherwise.
     */
    boolean alwaysUseIfBest();

    /**
     * Calculate the cost of the strategy.
     *
     * <p>If this strategy is not usable then this will return
     * {@link Integer#MAX_VALUE}, otherwise it will calculate the cost of
     * this strategy and remove those properties that it handles from the sets
     * of required properties.</p>
     *
     * @param shorthandAnalyzer     The analyzer.
     * @param inputValues           The properties.
     * @param requiredForIndividual The set of required properties for
     *                              individual usage.
     * @param requiredForShorthand  The set of required properties for
     *                              shorthand usage.
     * @return The cost.
     */
    int cost(
            ShorthandAnalyzer shorthandAnalyzer, StyleValues inputValues,
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand);

    /**
     * Update the properties.
     *
     * <p>This is invoked for the strategy that was chosen to render the
     * properties and it must update any shorthand values used.</p>
     *
     * @param shorthandAnalyzer     The analyzer.
     * @param outputValues          The properties.
     * @param requiredForIndividual The set of required properties for
     *                              individual usage.
     * @param requiredForShorthand  The set of required properties for
     *                              shorthand usage.
     */
    void update(
            ShorthandAnalyzer shorthandAnalyzer,
            MutableStyleProperties outputValues,
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand);
}
