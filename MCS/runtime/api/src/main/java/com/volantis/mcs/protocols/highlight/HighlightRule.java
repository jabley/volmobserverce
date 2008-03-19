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

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ProtocolException;

/**
 * Rule for handling mcs-effect-style in XHTML 2 elements.
 */
public abstract class HighlightRule {

    /**
     * The transform method which processes the element.
     * 
     * @param element
     *            element to process
     * @param protocol
     *            DOMProtocol
     * @param document
     *            Document object
     * @throws ProtocolException
     */
    abstract void transform(Element element, DOMProtocol protocol,
            Document document) throws ProtocolException;

}
