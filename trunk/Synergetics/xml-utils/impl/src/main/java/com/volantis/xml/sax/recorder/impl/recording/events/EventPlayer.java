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

package com.volantis.xml.sax.recorder.impl.recording.events;

import org.xml.sax.SAXException;

/**
 * Interface for event types to use to play events.
 */
public interface EventPlayer {

    /**
     * Generate a {@link org.xml.sax.ContentHandler#startDocument} event.
     *
     * @throws SAXException If there was a problem processing the event.
     */
    void startDocument()
            throws SAXException;

    /**
     * Generate an {@link org.xml.sax.ContentHandler#endDocument} event.
     *
     * <p>Also causes the player to stop.</p>
     *
     * @throws SAXException If there was a problem processing the event.
     */
    void endDocument()
            throws SAXException;

    /**
     * Generate a {@link org.xml.sax.ContentHandler#startElement} event.
     *
     * <p>This moves the attribute window to expose the next attribute range of
     * the specified length.</p>
     *
     * @throws SAXException If there was a problem processing the event.
     */
    void startElement()
            throws SAXException;

    /**
     * Generate an {@link org.xml.sax.ContentHandler#endElement} event.
     *
     * @throws SAXException If there was a problem processing the event.
     */
    void endElement()
            throws SAXException;

    /**
     * Generate a {@link org.xml.sax.ContentHandler#characters} event.
     *
     * <p>Moves the range of characters to cover the next range, whose length
     * is specified and generates an event for that range.</p>
     *
     * @throws SAXException If there was a problem processing the event.
     */
    void characters()
            throws SAXException;

    /**
     * Generate an {@link org.xml.sax.ContentHandler#ignorableWhitespace} event.
     *
     * <p>Moves the range of characters to cover the next range, whose length
     * is specified and generates an event for that range.</p>
     *
     * @throws SAXException If there was a problem processing the event.
     */
    void ignorableWhitespace()
            throws SAXException;

    /**
     * Generate a {@link org.xml.sax.ContentHandler#processingInstruction}
     * event.
     *
     * @throws SAXException If there was a problem processing the event.
     */
    void processingInstruction()
            throws SAXException;

    /**
     * The recording has started.
     */
    void startRecording();

    /**
     * The end of the recording has been reached.
     */
    void endRecording();
}
