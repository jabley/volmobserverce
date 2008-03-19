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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-May-03    Adrian          VBM:2003030509 - Created this a the baseclass
 *                              for those classes which record a sax event for
 *                              later playback through a specified
 *                              ContentHandler.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.ab.editors.xml.schema.recorder;

import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

import java.io.Serializable;

abstract class SAXEvent implements Serializable {

    /**
     * The columnNumber attribute of the Locator passed in on construction
     */
    private int columnNumber;

    /**
     * The lineNumber attribute of the Locator passed in on construction
     */
    private int lineNumber;

    /**
     * The publicID attribute of the Locator passed in on construction
     */
    private String publicID;

    /**
     * The systemID attribute of the Locator passed in on construction
     */
    private String systemID;

    /**
     * Copies out the important locator information into local member storage.
     */
    public SAXEvent(Locator locator) {
        if (locator != null) {
            columnNumber = locator.getColumnNumber();
            lineNumber = locator.getLineNumber();
            publicID = locator.getPublicId();
            systemID = locator.getSystemId();
        }
    }

    /**
     * 'Playback' of the SAXEvent by restoring the state of the specified
     * {@link LocatorImpl} and firing a SAX event in the specified
     * {@link ContentHandler}
     *
     * @param target - The {@link ContentHandler} on which we want to fire the
     * SAX event.
     * @param locator - The {@link LocatorImpl} which we wish to restore the
     * state of before replaying the event.
     *
     * @throws SAXException if one was thrown in firing the SAX event on the
     * specified {@link ContentHandler}.
     */
    public final void playback(ContentHandler target, LocatorImpl locator)
            throws SAXException {
        locator.setColumnNumber(columnNumber);
        locator.setLineNumber(lineNumber);
        locator.setPublicId(publicID);
        locator.setSystemId(systemID);

        playbackImpl(target, locator);
    }

    /**
     * Event specific 'Playback' of the SAXEvent by firing a SAX event in the
     * specified {@link ContentHandler}
     *
     * @param target The {@link ContentHandler} on which we want to fire the
     * SAX event.
     * @param locator The {@link Locator} used to associate the SAX event with
     * a document location
     *
     * @throws SAXException if one was thrown in firing the SAX event on the
     * specified {@link ContentHandler}.
     */
    protected abstract void playbackImpl(ContentHandler target,
                                         Locator locator) throws SAXException;
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
