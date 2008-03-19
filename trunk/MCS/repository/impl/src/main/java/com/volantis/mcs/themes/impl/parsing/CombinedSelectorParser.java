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

import com.volantis.mcs.themes.CombinatorEnum;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.Subject;
import com.volantis.mcs.themes.parsing.ObjectParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Parser for converting combined selectors to text.
 */
public class CombinedSelectorParser implements ObjectParser {
    /**
     * Map associating the various supported combinators with their textual
     * equivalent from the CSS2/3 specs.
     */
    private static final Map COMBINATOR_REPRESENTATIONS = new HashMap();
    static {
        COMBINATOR_REPRESENTATIONS.put(CombinatorEnum.CHILD, " > ");
        COMBINATOR_REPRESENTATIONS.put(CombinatorEnum.DESCENDANT, " ");
        COMBINATOR_REPRESENTATIONS.put(CombinatorEnum.DIRECT_ADJACENT, " + ");
        COMBINATOR_REPRESENTATIONS.put(CombinatorEnum.INDIRECT_ADJACENT, " ~ ");
    }

    /**
     * A parser for selector sequences.
     */
    private static final ObjectParser SELECTOR_SEQUENCE_PARSER =
            new SelectorSequenceParser();

    // Javadoc inherited
    public String objectToText(Object object) {
        CombinedSelector selector = (CombinedSelector) object;
        StringBuffer output = new StringBuffer();
        output.append(SELECTOR_SEQUENCE_PARSER.objectToText(
                selector.getContext()));
        output.append(COMBINATOR_REPRESENTATIONS.get(selector.getCombinator()));
        Subject subject = selector.getSubject();
        if (subject instanceof SelectorSequence) {
            output.append(SELECTOR_SEQUENCE_PARSER.objectToText(subject));
        } else if (subject instanceof CombinedSelector) {
            output.append(objectToText(subject));
        }
        return output.toString();
    }

    // Javadoc inherited
    public Object textToObject(String text) {
        throw new UnsupportedOperationException();
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
