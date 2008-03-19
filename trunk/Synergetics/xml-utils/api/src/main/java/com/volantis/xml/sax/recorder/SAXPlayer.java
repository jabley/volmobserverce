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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.sax.recorder;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Plays back events contained within a {@link SAXRecording}.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface SAXPlayer {

    /**
     * Set the SAX event handler to which the recorded events should be played
     * back to.
     *
     * <p>This must be set before {@link #play} is called.</p>
     *
     * @param handler The SAX event handler.
     */
    void setContentHandler(ContentHandler handler);

    /**
     * Get the SAX event handler.
     *
     * @return The SAX event handler.
     */
    ContentHandler getContentHandler();

    /**
     * Set the flow controller to use.
     *
     * @param flowController The flow controller.
     */
    void setFlowController(FlowController flowController);

    /**
     * Get the flow controller.
     *
     * @return The flow controller.
     */
    FlowController getFlowController();

    /**
     * Get the locator that will be updated by this player.
     *
     * @return The locator.
     */
    Locator getLocator();

    /**
     * Play the events from the recording for which this player was created.
     *
     * <p>This can be called any number of times, in sequence by the same
     * thread and each time it is called it will play the exact same sequence
     * of events.</p>
     *
     * @throws SAXException If there was a problem handling any of the SAX
     *                      events.
     */
    void play() throws SAXException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Apr-05	1262/1	pduffin	VBM:2005041105 Added support for preparsing the pipeline template

 ===========================================================================
*/
