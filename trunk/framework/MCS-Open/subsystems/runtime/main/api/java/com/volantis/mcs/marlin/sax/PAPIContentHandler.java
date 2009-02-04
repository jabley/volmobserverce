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
 * $Header: /src/voyager/com/volantis/mcs/marlin/sax/PAPIContentHandler.java,v 1.1 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Apr-2003  Chris W         VBM:2003030404 - Created to handle normal PAPI
 *                              elements.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.marlin.sax;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;

/**
 * This is an implementation of MarlinContentHandler that deals with normal
 * PAPI elements.
 */
public class PAPIContentHandler extends AbstractPAPIContentHandler {
        
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(PAPIContentHandler.class);

    /**
     * Creates a new <code>PAPIContentHandler</code> instance.
     */
    public PAPIContentHandler() {
        this(null);
    }

    /**
     * Creates a new <code>PAPIContentHandler</code> instance.
     * @param flowControlManager a FlowControlManager that can be
     * used to suppress SAX events at the root of an <code>XMLPipeline</code>. 
     * If the source of the SAX events is not an <code>XMLPipeline</code>
     * null should be passed in
     */ 
    public PAPIContentHandler(FlowControlManager flowControlManager) {
        super(flowControlManager);        
    }
    
    /**
     * Creates a new <code>PAPIContentHandler</code> instance that takes its
     * attributes from the values passed in. This allows us to switch between
     * <code>AbstractPAPIContentHandler</code>s when we process nativemarkup.
     * @param marlinContext MarlinContentHandlerContext
     * @param context PAPIContentHandlerContext
     * @param elementStackEntry ElementStackEntry
     */
    public PAPIContentHandler(MarlinContentHandlerContext marlinContext,
                              PAPIContentHandlerContext context,
                              ElementStackEntry elementStackEntry) {
        this.marlinContext = marlinContext;
        this.context = context;
        this.elementStackEntry = elementStackEntry;
    }
    
    /**
     * Returns the appropriate MarlinElementHandler
     * @param localName A String containing the name of the current tag 
     * @return MarlinElementHandler
     */
    protected MarlinElementHandler getMarlinElementHandler(String localName) {
        return MarlinElementHandlerFactory.getMarlinElementHandler(localName);
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

 15-Aug-03	1111/1	chrisw	VBM:2003081306 Move fields in AbstractMarlinContentHandler to MarlinContentHandlerContext

 13-Aug-03	1048/2	doug	VBM:2003070904 Modified MarlinContentHandlers so that they can control the flow of pipeline SAX events

 22-Jul-03	833/1	adrian	VBM:2003071902 added marlin support for invocation elements

 ===========================================================================
*/
