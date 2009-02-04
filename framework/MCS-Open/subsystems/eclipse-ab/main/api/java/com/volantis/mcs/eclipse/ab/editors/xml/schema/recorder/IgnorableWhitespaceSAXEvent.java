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

/**
 * This class is used to playback a IgnorableWhitespace SAXEvent.
 */
class IgnorableWhitespaceSAXEvent extends SAXEvent {

    /**
     * The start index parameter of the IgnorableWhitespace SAXEvent that we
     * are storing for later playback.
     */
    protected int start;

    /**
     * The length parameter of the IgnorableWhitespace SAXEvent that we are
     * storing for later playback.
     */
    protected int length;

    /**
     * The char array parameter of the IgnorableWhitespace SAXEvent that we are
     * storing for later playback.
     */
    protected char[] characters;

    /**
     * Construct a new IgnorableWhitespaceSAXEvent
     * @param locator
     */
    public IgnorableWhitespaceSAXEvent(Locator locator, char[] chars, int start,
                                       int length) {
        super(locator);
        this.start = start;
        this.length = length;
        // As arrays are mutable we copy the content to a new array.
        characters = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            characters[i] = aChar;
        }
    }

    /**
     * 'Playback' the Characters SAXEvent by firing a IgnorableWhitespace event
     * in the specified {@link ContentHandler}
     * @param target The {@link ContentHandler} on which we want to fire the
     * IgnorableWhitespace event.
     * @param locator The {@link Locator} used to associate the SAX event with
     * a document location
     * @throws SAXException if one was thrown in firing the IgnorableWhitespace
     * event on the specified {@link ContentHandler}.
     */
    protected void playbackImpl(ContentHandler target, Locator locator)
            throws SAXException {
        target.ignorableWhitespace(characters, start, length);
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
