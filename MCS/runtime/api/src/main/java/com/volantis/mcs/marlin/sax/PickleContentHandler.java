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
 * $Header: /src/voyager/com/volantis/mcs/marlin/sax/PickleContentHandler.java,v 1.1 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Apr-2003  Chris W         VBM:2003030404 - Created to handle pickle
 *                              elements.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.marlin.sax;

/**
 * This class is an implementation of MarlinContentHandler that deals with 
 * pickle elements. We use pickle elements to handle the markup present inside
 * nativemarkup elements.
 */
public class PickleContentHandler extends AbstractPAPIContentHandler {

    /**
     * The PickleElementHandler that will be used by this class. The same
     * PickleElementHandler is used to reduce garbage.
     */
    private PickleElementHandler handler = new PickleElementHandler();;
    
    /**
     * Creates a new <code>PickleContentHandler</code> instance that takes its
     * attributes from the values passed in. This allows us to switch between
     * <code>AbstractPAPIContentHandler</code>s when we process nativemarkup.
     * @param marlinContext MarlinContentHandlerContext
     * @param context PAPIContentHandlerContext
     * @param elementStackEntry ElementStackEntry
     */
    public PickleContentHandler(MarlinContentHandlerContext marlinContext,
                                PAPIContentHandlerContext context,
                                ElementStackEntry elementStackEntry) {
        this.marlinContext = marlinContext;
        this.context = context;
        this.elementStackEntry = elementStackEntry;
    }

    
    /* (non-Javadoc)
     * @see com.volantis.mcs.marlin.sax.AbstractPAPIContentHandler#getMarlinElementHandler(java.lang.String)
     */
    protected MarlinElementHandler getMarlinElementHandler(String localName) {       
        handler.setElementName(localName);
        return handler;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Aug-03	1111/1	chrisw	VBM:2003081306 Move fields in AbstractMarlinContentHandler to MarlinContentHandlerContext

 13-Aug-03	1048/2	doug	VBM:2003070904 Modified MarlinContentHandlers so that they can control the flow of pipeline SAX events

 22-Jul-03	833/1	adrian	VBM:2003071902 added marlin support for invocation elements

 ===========================================================================
*/
