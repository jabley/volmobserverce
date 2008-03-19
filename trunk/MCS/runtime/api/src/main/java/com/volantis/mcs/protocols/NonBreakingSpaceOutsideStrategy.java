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
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;

/**
 * This class is responsible for adding
 * "&nbsp;" followed by a whitespace character before and after an element.
 */
public class NonBreakingSpaceOutsideStrategy
        extends AbstractWhiteSpaceFixStrategy {

    // javadoc inherited
    public void fixUpSpace(Element element, boolean isOpen) {
        Node node = (isOpen) ? element.getPrevious() : element.getNext();
        if (node instanceof Text) {
            Text text = (Text) node;
            if (isOpen) {
                // append the text to the previous element

                text.append(NON_BREAKING_SPACE);
                text.append(SINGLE_WHITESPACE);
            } else {
                // prepend the spaces
                prependText(text, NON_BREAKING_SPACE + SINGLE_WHITESPACE);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10803/2	pabbott	VBM:2005120901 Fix white space problem on LG-C1100

 08-Dec-05	10675/4	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 02-Aug-05	9139/4	doug	VBM:2005071403 Finished off whitespace fixes

 22-Jul-05	9108/1	rgreenall	VBM:2005071403 Partial implementation.

 ===========================================================================
*/
