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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.properties.StyleProperty;

/**
 * Parser for the mcs-marquee shorthand property.
 */
public class MarqueeParser extends AbstractShorthandPropertyParser {

    /**
     * Initialise.
     *
     * @param factory The factory to use to retrieve parsers for the component
     *                properties.
     */
    public MarqueeParser(PropertyParserFactory factory) {
        super(factory, new StyleProperty[]{
            StylePropertyDetails.MCS_MARQUEE_DIRECTION,
            StylePropertyDetails.MCS_MARQUEE_REPETITION,
            StylePropertyDetails.MCS_MARQUEE_SPEED,
            StylePropertyDetails.MCS_MARQUEE_STYLE,
        });
    }
}
