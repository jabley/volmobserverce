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

import com.volantis.mcs.themes.PseudoElementSelector;
import com.volantis.mcs.themes.PseudoElementTypeEnum;
import com.volantis.mcs.themes.parsing.ObjectParser;

/**
 * Parser for converting pseudo-elements to text.
 */
public class PseudoElementSelectorParser implements ObjectParser {
    // Javadoc inherited
    public String objectToText(Object object) {
        if (object instanceof PseudoElementSelector) {
            String retVal = null;
            PseudoElementSelector pseudoElement =
                    (PseudoElementSelector) object;
            if (PseudoElementTypeEnum.INVALID.equals(
                    pseudoElement.getPseudoElementType())) {
                retVal =  "::" + pseudoElement.getInvalidPseudoElement();
            } else {
                retVal = "::" + pseudoElement.getPseudoElementType().getType();
            }

            return retVal;
        } else {
            throw new IllegalArgumentException();
        }
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

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.
 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 14-Sep-05	9380/3	adrianj	VBM:2005082401 GUI support for nth-child

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 21-Jul-05	8713/2	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
