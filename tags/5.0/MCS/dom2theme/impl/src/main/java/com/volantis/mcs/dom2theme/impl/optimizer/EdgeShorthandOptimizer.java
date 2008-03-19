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

package com.volantis.mcs.dom2theme.impl.optimizer;

import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.LengthUnit;

/**
 * Optimizes a group of properties that specify the styles for various edges
 * of a box.
 *
 * <p>e.g. margin, padding. Border edges are handled separately.</p>
 */
public class EdgeShorthandOptimizer
        extends AbstractShorthandOptimizer {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();
    
    /**
     * The default value to use if a required property has a value of ANY.
     */
    private static final StyleLength LENGTH_0PX =
        STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX);

    /**
     * Initialise.
     *
     * @param shorthand           The shorthand to analyse.
     * @param checker             The property status checker.
     * @param supportedShorthands The set of supported shorthands.
     */
    public EdgeShorthandOptimizer(
            StyleShorthand shorthand,
            PropertyClearerChecker checker,
            ShorthandSet supportedShorthands) {
        super(new EdgeCharacteristicAnalyzer(shorthand,
                checker, LENGTH_0PX, supportedShorthands));
    }
}
