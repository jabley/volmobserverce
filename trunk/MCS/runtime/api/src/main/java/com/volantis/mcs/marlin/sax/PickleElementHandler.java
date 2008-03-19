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
/*
 * $Header: /src/voyager/com/volantis/mcs/marlin/sax/PickleElementHandler.java,v 1.1 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Apr-2003  Chris W         VBM:2003030404 - PickleElementHandler creates
 *                              PickleInlineElements and PickleInlineAttributes
 *                              to deal with native markup.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.marlin.sax;

import java.util.HashMap;
import java.util.Map;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.pickle.PickleInlineAttributes;
import com.volantis.mcs.pickle.impl.PickleInlineElementImpl;
import com.volantis.mcs.pickle.impl.PickleNativeElementImpl;
import com.volantis.mcs.pickle.impl.PickleInlineElementImpl;
import com.volantis.mcs.pickle.impl.PickleNativeElementImpl;

/**
 * PickleElementHandler implements MarlinElementHandler for Pickle elements.
 */
public class PickleElementHandler extends AbstractElementHandler {
    
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(PickleElementHandler.class);

    /**
     * The name of the pickle element
     */
    private String elementName;

    /**
     * Create a new <code>PickleElementHandler</code>.
     */
    public PickleElementHandler() {
    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.marlin.sax.MarlinElementHandler#createPAPIAttributes
     * (com.volantis.mcs.marlin.sax.PAPIContentHandlerContext)
     */
    public PAPIAttributes createPAPIAttributes(PAPIContentHandlerContext context) {        
        return new PickleInlineAttributes();
    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.marlin.sax.MarlinElementHandler#createPAPIElement
     * (com.volantis.mcs.marlin.sax.PAPIContentHandlerContext)
     */
    public PAPIElement createPAPIElement(PAPIContentHandlerContext context) { 
        PAPIElement element;
        
        if(context.getNativeMarkupDepth() == 0) {  
            element = new PickleInlineElementImpl();
        } else {
            element = new PickleNativeElementImpl();
        }
        return element;
    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.marlin.sax.MarlinElementHandler#initializePAPIAttributes
     * (com.volantis.mcs.marlin.sax.PAPIContentHandlerContext,
     * org.xml.sax.Attributes, com.volantis.mcs.papi.PAPIAttributes)
     */
    public void initializePAPIAttributes(PAPIContentHandlerContext context,
        Attributes saxAttributes, PAPIAttributes papiAttributes) 
        throws SAXException {
                
        PickleInlineAttributes attributes = 
            (PickleInlineAttributes)papiAttributes;
    
        attributes.setElementName(elementName);
        if (logger.isDebugEnabled()) {
            logger.debug("setElementName (" + elementName + ")");
        }        
        
        Map attrsMap = new HashMap();
        for (int i = 0; i < saxAttributes.getLength(); i++) {
            String name = saxAttributes.getLocalName(i);                    
            String value = saxAttributes.getValue(i);
            attrsMap.put(name, value);
            
            if (logger.isDebugEnabled ()) {
              logger.debug ("setAttributes (" + name + "=" + value + ")");
            }
        }
        attributes.setAttributes(attrsMap);
    }

    /* (non-Javadoc)
     * @see MarlinElementHandler#canContainCharacterData()
     */
    public boolean canContainCharacterData() {
        return true;
    }

    /**
     * Sets the pickle element's name. We have to do this here as the element's
     * name cannot be obtained from org.xml.sax.Attributes object used by
     * initializePAPIAttributes();
     * @param localName
     */
    void setElementName(String localName) {
        elementName = localName;     
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 15-Aug-03	1111/1	chrisw	VBM:2003081306 Move fields in AbstractMarlinContentHandler to MarlinContentHandlerContext

 23-Jun-03	459/3	mat	VBM:2003061910 Changed marlin-canvas-schema to 2003061910

 23-Jun-03	459/1	mat	VBM:2003061910 Change getContentWriter() to return correct nativeWriter for Native markup elements

 ===========================================================================
*/
