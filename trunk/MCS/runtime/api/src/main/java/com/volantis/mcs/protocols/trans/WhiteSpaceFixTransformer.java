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
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMVisitorBasedTransformer;
import com.volantis.mcs.protocols.TransformingVisitor;

import java.util.Set;

/**
 * This class is responsible for adding space around styling elements and
 * anchor elements to work around the fact that some browsers do not
 * honour spacing properly.
 *
 * TODO: this should be moved from trans to a more appropriate package.
 */
public final class WhiteSpaceFixTransformer
        extends DOMVisitorBasedTransformer {

    /**
     * Contains the name of inline styling elements that do not honour
     * whitespace
     */
    private final Set inlineStyleElements;

    /**
     * Contains the name of link based elements that do not honour whitespace
     */
    private final Set inlineLinkElements;

    /**
     * Constructs a new <code>WhiteSpaceFixTransformer</code> instance
     * @param inlineStyleElements Contains the name of inline styling elements
     * that do not honour whitespace
     * @param inlineLinkElements Contains the name of link based elements that
     * do not honour whitespace
     */
    public WhiteSpaceFixTransformer(Set inlineStyleElements,
                                    Set inlineLinkElements) {
        this.inlineStyleElements = inlineStyleElements;
        this.inlineLinkElements = inlineLinkElements;

    }


    protected TransformingVisitor getDOMVisitor(DOMProtocol protocol) {
        return new WhitespaceFixingVisitor(inlineStyleElements,
                                           inlineLinkElements,
                                           protocol);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10675/3	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 02-Aug-05	9139/2	doug	VBM:2005071403 Finished off whitespace fixes

 22-Jul-05	9108/1	rgreenall	VBM:2005071403 Partial implementation.

 ===========================================================================
*/
