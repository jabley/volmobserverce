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
 * A simple value converter.
 */
public class SimpleValueConverter implements ValueConverter {

    /**
     * The set of supported types.
     */
    private final Set supportedTypes;

    /**
     * The optional allowableKeywords for keywords.
     */
    private final AllowableKeywords allowableKeywords;

    /**
     * Initialise.
     *
     * @param supportedTypes The set of supported types.
     * @param allowableKeywords         The optional allowableKeywords for keywords.
     */
    public SimpleValueConverter(
            Set supportedTypes, AllowableKeywords allowableKeywords) {
        this.supportedTypes = supportedTypes;
        this.allowableKeywords = allowableKeywords;
    }

    // Javadoc inherited.
    public StyleValue convert(
            ParserContext context, StyleValueIterator iterator) {

        return context.convertAndConsume(supportedTypes, allowableKeywords, iterator);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
