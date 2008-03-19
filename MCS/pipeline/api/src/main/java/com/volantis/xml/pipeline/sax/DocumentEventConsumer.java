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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

import org.xml.sax.SAXException;
import org.xml.sax.Locator;

/**
 * An XMLProcess that consumes nested startDocument() and endDocumentEvents().
 */
public class DocumentEventConsumer extends XMLProcessImpl {

    /**
     * Variable that is used to record the document event depth count
     */
    private int eventCount = 0;

    /**
     * Flag that is used to determine whether this process has been started
     */
    private boolean isStarted = false;

    // javadoc inherited
    public void startProcess() {
        isStarted = true;
        eventCount = 0;
    }

    // javadoc inherited
    public void stopProcess() {
        isStarted = false;
    }

    // Javadoc inherited
    public void startDocument() throws SAXException {
        if (!isStarted || eventCount == 0) {
            super.startDocument();
        }
        eventCount++;
    }

    // Javadoc inherited
    public void endDocument() throws SAXException {
        eventCount--;
        if (eventCount < 0) {
            Locator locator = getPipelineContext().getCurrentLocator();
            XMLStreamingException e = new XMLStreamingException(
                    "endDocument received without a preceeding startDocument",
                    locator);
            fatalError(e);
        }
        if (!isStarted || eventCount == 0) {
            super.endDocument();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 23-Jun-03	95/1	doug	VBM:2003061605 Document Event Filtering changes

 ===========================================================================
*/
