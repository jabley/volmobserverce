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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * 30-May-03    Geoff           VBM:2003042918 - Implement dissection 
 *                              WBSAX serialiser.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.io;

import com.volantis.mcs.wbdom.VisitorAttributesIterator;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.wbdom.WBDOMAttribute;
import com.volantis.mcs.wbdom.WBDOMElement;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXTeeHandler;
import com.volantis.mcs.wbsax.io.XMLProducer;
import com.volantis.mcs.dom.output.SerialisationURLListener;

import java.io.CharArrayWriter;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * 
 * 
 * Also manaages the generation of URL attribute events if necessary. These
 * are used by the packager currently.
 */ 
public class WBSAXAttributesSerialiser 
        extends VisitorAttributesIterator {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(WBSAXAttributesSerialiser.class);

    private WBSAXContentHandler handler;
    private URLAttributeConfiguration urlAttributeConf; //optional
    private SerialisationURLListener urlListener; //optional
    private WBDOMElement element;
    
    private WBSAXAttributeValueSerialiser valueSerialiser;
    private WBSAXContentHandler teeHandler;
    private CharArrayWriter buffer;
    
    public WBSAXAttributesSerialiser(WBSAXContentHandler handler,
            WBSAXAttributeNameSerialiser nameVisitor, 
            WBSAXAttributeValueSerialiser valueVisitor,
            URLAttributeConfiguration urlAttributeConf,
            SerialisationURLListener urlListener) {
        super(nameVisitor, valueVisitor);
        this.handler = handler;
        this.valueSerialiser = valueVisitor;
        this.urlAttributeConf = urlAttributeConf;
        this.urlListener = urlListener;
        
        this.buffer = new CharArrayWriter();
        // This will only be used to copy attribute values.
        WBSAXContentHandler copyHandler = new XMLProducer(buffer, buffer);
        this.teeHandler = new WBSAXTeeHandler(handler, copyHandler);
    }

    public void use(WBDOMElement element) {
        this.element = element;
    }
    
    public void before() throws WBDOMException {
        try {
            handler.startAttributes();
        } catch (WBSAXException e) {
            throw new WBDOMException(e);
        }
    }

    public void next(WBDOMAttribute attribute) throws WBDOMException {
        // todo: avoid the debugging way of accessing element names -
        // see URLAttributeConfiguration.
        
        // If this is an attribute which contains a URL
        if (urlAttributeConf != null && urlListener != null &&  
                urlAttributeConf.isURLAttribute(element.getName(), 
                    attribute.getName())) {
            // Render the attribute value out to a string as well
            // as out normally.
            valueSerialiser.pushHandler(teeHandler);
            super.next(attribute);
            valueSerialiser.popHandler();
            // If we collected anything...
            if (buffer.size() > 0) {
                // Then send that value off as a URL event.
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "Generating URL Attribute Event for: " +
                            element.getName() + " " + attribute.getName() + 
                            "='" + buffer.toString() + "'");
                }
                urlListener.foundURL(buffer.toString());
                buffer.reset();
            }
        } else {
            // Just process the attribute normally.
            super.next(attribute);
        }
    }

    public void after() throws WBDOMException {
        try {
            handler.endAttributes();
        } catch (WBSAXException e) {
            throw new WBDOMException(e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/2	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
