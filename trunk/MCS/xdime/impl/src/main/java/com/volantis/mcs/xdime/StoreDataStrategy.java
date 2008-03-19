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
package com.volantis.mcs.xdime;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.protocols.DOMOutputBuffer;

/**
 * Concrete implementation of {@link DataHandlingStrategy} which stores any
 * data (character data or markup) encountered while processing.
 */
public class StoreDataStrategy implements DataHandlingStrategy {

    /**
     * The output buffer to which content will be directed.
     */
    private DOMOutputBuffer content = null;

    // Javadoc inherited.
    public void handleData(XDIMEContextInternal context) {
        MarinerPageContext pageContext =
                 ContextInternals.getMarinerPageContext(
                         context.getInitialRequestContext());
        // Create new output buffer to capture the content of the label element.
        content = (DOMOutputBuffer) pageContext.getProtocol().
            getOutputBufferFactory().createOutputBuffer();
        pageContext.pushOutputBuffer(content);
    }

    // Javadoc inherited.
    public String getCharacterData() {
        return content.getPCDATAValue();
    }

    // Javadoc inherited.
    public void stopHandlingData(XDIMEContextInternal context) {
        MarinerPageContext pageContext =
                 ContextInternals.getMarinerPageContext(
                         context.getInitialRequestContext());
        pageContext.popOutputBuffer(content);
    }
}
