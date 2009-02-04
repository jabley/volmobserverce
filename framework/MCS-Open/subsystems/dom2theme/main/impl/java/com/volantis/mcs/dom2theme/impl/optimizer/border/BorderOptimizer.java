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

import com.volantis.mcs.dom2theme.impl.optimizer.PropertyClearerChecker;
import com.volantis.mcs.dom2theme.impl.optimizer.strategy.IndividualPropertyStrategy;
import com.volantis.mcs.dom2theme.impl.optimizer.strategy.OptimizeStrategy;
import com.volantis.mcs.dom2theme.impl.optimizer.strategy.SingleShorthandStrategy;
import com.volantis.mcs.dom2theme.impl.optimizer.strategy.StrategicOptimizer;
import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.mcs.themes.StyleShorthands;

import java.util.ArrayList;
import java.util.List;

/**
 * Optimizes the border related properties.
 */
public class BorderOptimizer
        extends StrategicOptimizer {

    /**
     * Build the list of strategies for use by the super class.
     *
     * @param supportedShorthands The set of supported shorthands.
     * @return The list of {@link OptimizeStrategy}s.
     */
    private static List buildStrategies(ShorthandSet supportedShorthands) {

        List strategies = new ArrayList();

        // The strategies to use are added in priority order.
        final OptimizeStrategy individualPropertyStrategy =
                new IndividualPropertyStrategy();
        if (supportedShorthands.contains(StyleShorthands.BORDER)) {
            strategies.add(new SingleShorthandStrategy(true));
        }

        if (supportedShorthands.contains(StyleShorthands.BORDER_COLOR) &&
                supportedShorthands.contains(StyleShorthands.BORDER_STYLE) &&
                supportedShorthands.contains(StyleShorthands.BORDER_WIDTH)) {
            strategies.add(
                    new CharacteristicStrategy(individualPropertyStrategy));
        }

        if (supportedShorthands.contains(StyleShorthands.BORDER_TOP) &&
                supportedShorthands.contains(StyleShorthands.BORDER_TOP) &&
                supportedShorthands.contains(StyleShorthands.BORDER_TOP) &&
                supportedShorthands.contains(StyleShorthands.BORDER_TOP)) {
            strategies.add(new EdgesStrategy(individualPropertyStrategy));
        }

        // Always add an optimizer for the individual properties.
        // todo CSS Version 1 does not support individual properties for style and color.
        strategies.add(individualPropertyStrategy);

        return strategies;
    }

    /**
     * Initialise.
     *
     * @param checker             The checker, used by the analyzer.
     * @param supportedShorthands The set of supported shorthands.
     */
    public BorderOptimizer(
            PropertyClearerChecker checker,
            ShorthandSet supportedShorthands) {
        super(new BorderAnalyzer(checker, supportedShorthands),
                buildStrategies(supportedShorthands));
    }
}
