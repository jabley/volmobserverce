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
 * An enumeration of all the supported SAX event type objects.
 *
 * <p>All derived classes will be used in multiple recordings in multiple
 * threads simultaneously and therefore must not contain any recording or
 * thread specific state.</p>
 *
 * <p><strong>Note:</strong> There is no separate type for the
 * startPrefixMapping and endPrefixMapping events as they are assumed to be
 * part of the startElement and endElement events respectively.</p>
 */
public abstract class SAXEventType {

    /**
     * The array of events.
     */
    private static final SAXEventType[] EVENT_TYPE_ARRAY = new SAXEventType[9];

    /**
     * The count of the number of event types.
     */
    private static int EVENT_TYPE_COUNT = 0;

    /**
     * Plays back
     * {@link org.xml.sax.ContentHandler#startDocument} events.
     */
    public static final SAXEventType START_DOCUMENT
            = new SAXEventType("START-DOCUMENT", false) {

                // Javadoc inherited.
                public void play(EventPlayer player)
                        throws SAXException {

                    // Generate the event.
                    player.startDocument();
                }
            };

    /**
     * Plays back
     * {@link org.xml.sax.ContentHandler#endDocument} events.
     */
    public static final SAXEventType END_DOCUMENT
            = new SAXEventType("END-DOCUMENT", false) {

                // Javadoc inherited.
                public void play(EventPlayer player) throws SAXException {
                    player.endDocument();
                }
            };

    /**
     * Plays back
     * {@link org.xml.sax.ContentHandler#startElement} events.
     */
    public static final SAXEventType START_ELEMENT
            = new SAXEventType("START-ELEMENT", true) {

                // Javadoc inherited.
                public void play(EventPlayer player)
                        throws SAXException {

                    // Generate the event.
                    player.startElement();
                }
            };

    /**
     * Plays back
     * {@link org.xml.sax.ContentHandler#endElement} events.
     */
    public static final SAXEventType END_ELEMENT
            = new SAXEventType("END-ELEMENT", true) {

                // Javadoc inherited.
                public void play(EventPlayer player)
                        throws SAXException {

                    // Generate the event.
                    player.endElement();
                }
            };

    /**
     * Plays back
     * {@link org.xml.sax.ContentHandler#characters} events.
     */
    public static final SAXEventType CHARACTERS
            = new SAXEventType("CHARACTERS", false) {

                // Javadoc inherited.
                public void play(EventPlayer player)
                        throws SAXException {

                    // Generate the event.
                    player.characters();
                }
            };

    /**
     * Plays back
     * {@link org.xml.sax.ContentHandler#ignorableWhitespace} events.
     */
    public static final SAXEventType IGNORABLE_WHITESPACE
            = new SAXEventType("IGNORABLE-WHITESPACE", false) {

                // Javadoc inherited.
                public void play(EventPlayer player)
                        throws SAXException {

                    // Generate the event.
                    player.ignorableWhitespace();
                }
            };

    /**
     * Plays back
     * {@link org.xml.sax.ContentHandler#processingInstruction} events.
     */
    public static final SAXEventType PROCESSING_INSTRUCTION
            = new SAXEventType("PROCESSING-INSTRUCTION", true) {

                // Javadoc inherited.
                public void play(EventPlayer player)
                        throws SAXException {

                    // Generate the event.
                    player.processingInstruction();
                }
            };

    /**
     * A special event that marks the start of the recording.
     */
    public static final SAXEventType START_RECORDING
            = new SAXEventType("START-RECORDING", false) {

                // Javadoc inherited.
                public void play(EventPlayer player) {

                    // Generate the event.
                    player.startRecording();
                }

                // Javadoc inherited.
                public boolean supportsLocation() {
                    return false;
                }
            };

    /**
     * A special event that marks the end of the recording.
     */
    public static final SAXEventType END_RECORDING
            = new SAXEventType("END-RECORDING", false) {

                // Javadoc inherited.
                public void play(EventPlayer player) {

                    // Generate the event.
                    player.endRecording();
                }

                // Javadoc inherited.
                public boolean supportsLocation() {
                    return false;
                }
            };

    // Make sure that the array is exactly the correct length.
    static {
        if (EVENT_TYPE_COUNT != EVENT_TYPE_ARRAY.length) {
            throw new IllegalStateException(
                    "Mismatch between number of events and length of array");
        }
    }

    /**
     * Get the event type with the specified index.
     *
     * @param index The index.
     * @return The event type.
     */
    public static SAXEventType getEventType(int index) {
        return EVENT_TYPE_ARRAY[index];
    }

    /**
     * The name of the event.
     */
    private final String name;

    /**
     * The index of the event in the array.
     */
    private final int index;

    /**
     * Indicates whether the event represents simple or complex content.
     */
    private final boolean complex;

    /**
     * Initialise.
     *
     * @param name The name of the event type.
     */
    private SAXEventType(String name, boolean complex) {
        this.name = name;
        index = EVENT_TYPE_COUNT;
        EVENT_TYPE_ARRAY[index] = this;
        EVENT_TYPE_COUNT += 1;
        this.complex = complex;
    }

    /**
     * Get the index of the event type.
     *
     * @return The index.
     */
    public int getIndex() {
        return index;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }

    /**
     * Plays the event through the specified player.
     *
     * @param player The player through which the events should be played.
     * @throws SAXException If the events caused an error in the player or
     *                      further down stream.
     */
    public abstract void play(EventPlayer player)
            throws SAXException;

    /**
     * Indicates whether the event type supports location information.
     *
     * @return True if the event type supports location information, false
     * otherwise.
     */
    public boolean supportsLocation() {
        return true;
    }

    public boolean isComplex() {
        return complex;
    }
}
