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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.AllowableKeywords;

import java.util.Set;

/**
 * A handler for a component of a pair.
 */
class SimplePairComponentValueHandler
        implements PairComponentValueHandler {

    /**
     * The types supported by this component.
     */
    protected final Set supportedTypes;

    /**
     * The optional mapper for this component.
     */
    protected final AllowableKeywords allowableKeywords;

    /**
     * The initial value of the component.
     */
    private final StyleValue initialValue;

    /**
     * Initialise.
     *
     * @param supportedTypes The types supported by this component.
     * @param allowableKeywords         The optional mapper for this component.
     * @param initialValue   The initial value of the component.
     */
    public SimplePairComponentValueHandler(
            Set supportedTypes, AllowableKeywords allowableKeywords,
            StyleValue initialValue) {

        this.supportedTypes = supportedTypes;
        this.allowableKeywords = allowableKeywords;
        this.initialValue = initialValue;
    }

    // Javadoc inherited.
    public StyleValue convert(
            ParserContext context, StyleValueIterator iterator) {

        return context.convertAndConsume(supportedTypes, allowableKeywords, iterator);
    }

    // Javadoc inherited.
    public StyleValue initial(StyleValue other) {
        return initialValue;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/4	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
