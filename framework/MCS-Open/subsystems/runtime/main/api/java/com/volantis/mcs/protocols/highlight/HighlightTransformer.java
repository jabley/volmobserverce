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

package com.volantis.mcs.protocols.highlight;

import com.volantis.mcs.dom.WalkingDOMVisitor;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.WalkingDOMVisitorBasedTransformer;

import java.util.HashMap;

/**
 * Traverse a DOM tree processing any mcs-effect-style style property that are found.
 */

public class HighlightTransformer extends WalkingDOMVisitorBasedTransformer {

    /**
     * Create a visitor suitable for transforming the mcs-effect-style for
     * the given protocol.
     *
     * @param protocol protocol used to obtain the transformation rules.
     * @return a visitor loaded with the correct rule set for the given
     *         protocol
     */
    
	protected WalkingDOMVisitor getWalkingDOMVisitor(DOMProtocol protocol) {
        HashMap ruleSet =
            protocol.getProtocolConfiguration().getHighlightTransformationRules();

        return new HighlightVisitor(ruleSet, protocol);		
	}
}
