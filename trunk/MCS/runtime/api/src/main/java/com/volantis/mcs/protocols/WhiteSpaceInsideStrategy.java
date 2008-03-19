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
 * This class is responsible for adding a single white space inside an
 * element.
 */
public class WhiteSpaceInsideStrategy extends AbstractWhiteSpaceFixStrategy {

    // javadoc inherited
    public void fixUpSpace(Element element, boolean isOpen) {
        Node node = isOpen ? element.getHead() : element.getTail();
        if (node instanceof Text) {
            Text text = (Text) node;
            if (isOpen) {
                prependText(text, SINGLE_WHITESPACE);                
            } else {
                // append the whitespace to the node
                text.append(SINGLE_WHITESPACE);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10675/2	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 02-Aug-05	9139/1	doug	VBM:2005071403 Finished off whitespace fixes

 22-Jul-05	9108/3	rgreenall	VBM:2005071403 Partial implementation.

 22-Jul-05	9108/1	rgreenall	VBM:2005071403 Partial implementation.

 ===========================================================================
*/
