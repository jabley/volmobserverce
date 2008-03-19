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
package com.volantis.mcs.protocols.href;

import com.volantis.mcs.dom.WalkingDOMVisitor;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.WalkingDOMVisitorBasedTransformer;

import java.util.HashMap;

/**
 * Traverse a DOM tree processing any href attributes that are found.
 */
public class HrefTransformer
        extends WalkingDOMVisitorBasedTransformer {

    /**
     * Create a visitor suitable for transforming the href attributes for
     * the givne protocol.
     *
     * @param protocol protocol used to obtain the transformation rules.
     * @return a visitor loaded with the correct rule set for the given
     *         protocol
     */
    protected WalkingDOMVisitor getWalkingDOMVisitor(DOMProtocol protocol) {

        HashMap ruleSet =
                protocol.getProtocolConfiguration()
                        .getHrefTransformationRules();

        return new HrefVisitor(ruleSet);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9128/5	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 21-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
