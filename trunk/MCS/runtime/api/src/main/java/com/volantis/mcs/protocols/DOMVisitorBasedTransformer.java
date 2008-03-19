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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Document;

/**
 * An abstract DOMTransformer that provides a 'standard' implementation of
 * the transform() method and some static utility methods used by
 * DOMTransformers.
 */
public abstract class DOMVisitorBasedTransformer
        extends AbstractVisitorBasedDOMTransformer {

    // Javadoc inherited.
    public Document transform(DOMProtocol protocol, Document document) {

        logEntry(protocol, document);

        TransformingVisitor visitor = getDOMVisitor(protocol);
        if (visitor != null) {
            visitor.transform(document);
        }

        logExit(protocol, document);

        return document;
    }

    /**
     * Factory method to provide the DOMVisitor for this DOMTransformer.
     *
     * @param protocol The protocol for this DOMTransformer.
     * @return The right DOMVisitor for this DOMTransformer.
     */
    protected abstract TransformingVisitor getDOMVisitor(DOMProtocol protocol);
}
