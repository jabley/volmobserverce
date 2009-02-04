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
 * $Header: /src/voyager/com/volantis/mcs/gui/policyobject/PolicyObjectChooser.java,v 1.1 2002/05/23 14:16:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-May-03    Adrian          VBM:2003030509 - Created this to record a sax
 *                              event for later playback through a specified
 *                              ContentHandler.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.ab.editors.xml.schema.recorder;

import org.xml.sax.Locator;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/**
 * This class is used to playback an EndElement SAXEvent.
 */
class EndElementSAXEvent extends SAXEvent {

    /**
     * The namespaceURI parameter of the EndElement SAXEvent that we are
     * storing for later playback.
     */
    protected String namespaceURI;

    /**
     * The localName parameter of the EndElement SAXEvent that we are storing
     * for later playback.
     */
    protected String localName;

    /**
     * The qName parameter of the EndElement SAXEvent that we are storing
     * for later playback.
     */
    protected String qName;

    /**
     * Construct a new EndElementSAXEvent
     * @param locator
     */
    public EndElementSAXEvent(Locator locator, String namespaceURI,
                              String localName, String qName) {
        super(locator);
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.qName = qName;
    }

    /**
     * 'Playback' the EndElement SAXEvent by firing a
     * EndElement event in the specified {@link ContentHandler}
     * @param target The {@link ContentHandler} on which we want to fire the
     * EndElement event.
     * @param locator The {@link Locator} used to associate the SAX event with
     * a document location
     * @throws SAXException if one was thrown in firing the EndElement
     * event on the specified {@link ContentHandler}.
     */
    protected void playbackImpl(ContentHandler target, Locator locator)
            throws SAXException {
        target.endElement(namespaceURI, localName, qName);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
