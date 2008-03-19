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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl.parsing;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.InvalidSelector;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.parsing.ObjectParser;

/**
 * General purpose parser for all selector types supported by a rule. In the
 * current implementation this is combined selectors or selector sequences.
 */
public class RuleSelectorParser implements ObjectParser {
    /**
     * Parser for selector sequences.
     */
    private static final ObjectParser SELECTOR_SEQUENCE_PARSER =
            new SelectorSequenceParser();

    /**
     * Parser for combined selector sequences.
     */
    private static final ObjectParser COMBINED_SELECTOR_PARSER =
            new CombinedSelectorParser();

    private static final CSSParserFactory CSS_PARSER_FACTORY =
            CSSParserFactory.getDefaultInstance();

    private final CSSParser parser;
    
    public RuleSelectorParser() {
        parser = CSS_PARSER_FACTORY.createLaxParser();
    }

    // Javadoc inherited
    public String objectToText(Object object) {
        String result = null;
        if (object instanceof SelectorSequence) {
            result =  SELECTOR_SEQUENCE_PARSER.objectToText(object);
        } else if (object instanceof CombinedSelector) {
            result = COMBINED_SELECTOR_PARSER.objectToText(object);
        } else if (object instanceof InvalidSelector) {
            result = ((InvalidSelector) object).getText();
        } else {
            throw new IllegalArgumentException();
        }
        return result;
    }

    // Javadoc inherited
    public Object textToObject(String text) {
        Selector selector = parser.parseSelector(text);
        return selector;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9886/1	adrianj	VBM:2005101811 New themes GUI

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
