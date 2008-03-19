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

import java.util.HashSet;
import java.util.Set;

import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.styling.properties.StyleProperty;

/**
 * Base for those parsers that parse corners.
 */
public abstract class AbstractPairParser extends AbstractSinglePropertyParser {

    /**
     * The converter for the pair of values.
     */
    private static final PairValueConverter CONVERTER;

    static {
        final Set supportedTypes = new HashSet();
        supportedTypes.add(StyleValueType.LENGTH);

        PairComponentValueHandler firstHandler =
                new SimplePairComponentValueHandler(supportedTypes, null, null);

        PairComponentValueHandler secondHandler =
                new SimplePairComponentValueHandler(supportedTypes, null, null) {
                    public StyleValue initial(StyleValue other) {
                        return other;
                    }
                };

        CONVERTER = new PairValueConverter(firstHandler, secondHandler);
    }
    
    /**
     * Initialise.
     *
     * @param property  The property to set.
     */
    public AbstractPairParser(StyleProperty property) {
        super(property, 2);
        // TODO Auto-generated constructor stub
    }

    public StyleValue convert(ParserContext context, StyleValueIterator iterator) {
        return CONVERTER.convert(context, iterator);
    }

}
