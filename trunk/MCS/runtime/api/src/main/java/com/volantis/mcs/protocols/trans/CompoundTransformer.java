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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;

/**
 * A compound transformer that invokes one transformer then another (the
 * two transformers are passed in via the constructor).<p>
 *
 * Note that we can composite compound transformers. For example,
 *
 * <pre>
 * DOMTransformer one = new DOMTransformer();
 * DOMTransformer two = new DOMTransformer();
 * DOMTransformer three = new DOMTransformer();
 * DOMTransformer transformer = new CompoundTransformer(
 *  new CompoundTransformer(one, two), three));
 * </pre>
 */
public class CompoundTransformer implements DOMTransformer {

    /**
     * The first transformer.
     */
    private DOMTransformer firstTransformer;

    /**
     * The second transformer.
     */
    private DOMTransformer secondTransformer;

    /**
     * A compound transformer that will invoke the first transformer then
     * the second transformer. Neither transformers may be null.
     * @param firstTransformer the first transformer.
     * @param secondTransformer the second transformer.
     */
    public CompoundTransformer(DOMTransformer firstTransformer,
                               DOMTransformer secondTransformer) {
        if ((firstTransformer == null) || (secondTransformer == null)) {
            throw new IllegalArgumentException("Both transformer parameters " +
                    "may not be null: ");
        }
        this.firstTransformer = firstTransformer;
        this.secondTransformer = secondTransformer;
    }

    // javadoc inherited
    public Document transform(DOMProtocol protocol,
                              Document document) {
        firstTransformer.transform(protocol, document);
        secondTransformer.transform(protocol, document);
        return document;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 ===========================================================================
*/
