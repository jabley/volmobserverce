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

package com.volantis.xml.sax.recorder.impl.recording;

import com.volantis.xml.sax.recorder.FlowController;
import com.volantis.xml.sax.recorder.SAXPlayer;
import com.volantis.xml.sax.recorder.impl.attributes.AttributesWindow;
import com.volantis.xml.sax.recorder.impl.recording.events.EventPlayer;
import com.volantis.xml.sax.recorder.impl.recording.events.SAXEventType;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.LocatorImpl;

/**
 * Default implementation of {@link SAXPlayer}.
 *
 * <p>This holds all the state needed when playing a recording.</p>
 */
public class SAXPlayerImpl
        implements SAXPlayer, EventPlayer {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(SAXPlayerImpl.class);

    /**
     * The recording that is being played.
     */
    private final SAXRecordingImpl recording;

    /**
     * The locator to pass to the event handler.
     */
    private final LocatorImpl locator;

    /**
     * The event handler to which the events will be sent.
     */
    private ContentHandler handler;

    /**
     * Allows integration with a flow controller.
     */
    private FlowController flowController;

    /**
     * Indicates whether there is per event location information associated
     * with each event that supports it.
     */
    private boolean playPerEventLocation;

    /**
     * The window on the recording's attributes.
     */
    private final AttributesWindow window;

    /**
     * The index of the next int to get within the array of ints.
     */
    private int nextIntIndex;

    /**
     * Initialise.
     *
     * @param recording The recording that this player will play.
     */
    public SAXPlayerImpl(SAXRecordingImpl recording) {
        this.recording = recording;

        locator = new LocatorImpl();

        window = recording.createWindow();
    }

    // Javadoc inherited.
    public void setContentHandler(ContentHandler handler) {
        this.handler = handler;
    }

    // Javadoc inherited.
    public ContentHandler getContentHandler() {
        return handler;
    }

    public void setFlowController(FlowController flowController) {
        this.flowController = flowController;
    }

    public FlowController getFlowController() {
        return flowController;
    }

    // Javadoc inherited.
    public Locator getLocator() {
        return locator;
    }

    // Javadoc inherited.
    public void play()
            throws SAXException {

        playPerEventLocation = false;
        nextIntIndex = 0;

        int locationOptions = nextInt();
        if ((locationOptions & SAXRecordingImpl.RECORD_PER_EVENT_LOCATION)
                == SAXRecordingImpl.RECORD_PER_EVENT_LOCATION) {

            playPerEventLocation = true;
        }

        SAXEventType eventType;
        do {

            eventType = nextEventType();

            if (eventType.supportsLocation() && playPerEventLocation) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("reading location information");
                }
                locator.setPublicId(nextString());
                locator.setSystemId(nextString());
                locator.setLineNumber(nextInt());
                locator.setColumnNumber(nextInt());
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("finished reading location information");
                }
            }

            eventType.play(this);
        } while (eventType != SAXEventType.END_RECORDING);
    }

    /**
     * Get the next event type from the recording.
     *
     * @return The next event type.
     */
    private SAXEventType nextEventType() {
        SAXEventType eventType = recording.getEventType(nextIntIndex);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("intIndex="+nextIntIndex+" Event " + eventType.toString());
        }
        nextIntIndex += 1;
        return eventType;
    }

    /**
     * Get the next int from the recording.
     *
     * <p>This updates the index used to retrieve the int.</p>
     *
     * @return The value of the next int.
     */
    private int nextInt() {
        int value = recording.getInt(nextIntIndex);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("intIndex=" + nextIntIndex + " value=" + value);
        }
        nextIntIndex += 1;
        return value;
    }

    /**
     * Get the next string from the recording.
     *
     * <p>This updates the index used to retrieve the string.</p>
     *
     * @return The value of the next string.
     */
    private String nextString() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("nextString");
        }
        return recording.getString(nextInt());
    }

    /**
     * Get the next position of the balancing event from the recording.
     *
     * @return The next position of the balancing event.
     */
    private int nextEventPosition() {

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("nextEventPos");
        }

        return nextInt();
    }

    /**
     * Handle exiting events.
     *
     * @param balancedEventPosition The position of the event that ends the
     *                              current nesting level.
     */
    private void handleExiting(int balancedEventPosition) {
        if (flowController != null && flowController.exitCurrentLevel()) {
                // Move to the position of the balancing event.
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("handleExit -> " + balancedEventPosition);
                }
                nextIntIndex = balancedEventPosition;
        }
    }

    // Javadoc inherited.
    public void startDocument()
            throws SAXException {

        // Generate the event.
        handler.startDocument();
    }

    // Javadoc inherited.
    public void endDocument()
            throws SAXException {

        // Generate the event.
        handler.endDocument();
    }

    // Javadoc inherited.
    public void startElement() throws SAXException {

        // Generate the startPrefixMapping events associated with this event
        // if necessary.
        int prefixMappingCount = nextInt();
        if (prefixMappingCount > 0) {
            for (int i = 0; i < prefixMappingCount; i += 1) {

                String prefix = nextString();
                String uri = nextString();

                // Generate the event.
                handler.startPrefixMapping(prefix, uri);
            }
        }

        // Get the strings.
        String namespaceURI = nextString();
        String localName = nextString();
        String qName = nextString();

        // Get the attributes.
        int offset = nextInt();

        int length = nextInt();

        window.move(offset, length);

        // Remember the location of the matching end element event.
        int endElementEventPosition = nextInt();

        // Generate the event.
        handler.startElement(namespaceURI, localName, qName, window);

        handleExiting(endElementEventPosition);
    }

    // Javadoc inherited.
    public void endElement() throws SAXException {

        // Get the strings.
        String namespaceURI = nextString();
        String localName = nextString();
        String qName = nextString();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("read namespace, localname and qname");
        }

        // Remember the location of the balancing end element event.
        int containingEndElementPosition = nextEventPosition();

        // Generate the event.
        handler.endElement(namespaceURI, localName, qName);

        int prefixMappingCount = nextInt();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("number of prefix mappings is "+prefixMappingCount);
        }

        if (prefixMappingCount > 0) {
            for (int i = 0; i < prefixMappingCount; i += 1) {

                String prefix = nextString();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("read prefix mapping '" + prefix +"'");
                }

                // Generate the event.
                handler.endPrefixMapping(prefix);
            }
        }

        handleExiting(containingEndElementPosition);
    }

    // Javadoc inherited.
    public void characters()
            throws SAXException {

        int charactersOffset = nextInt();

        int length = nextInt();

        char[] characters = recording.getCharacterArray();
        handler.characters(characters, charactersOffset, length);
    }

    // Javadoc inherited.
    public void ignorableWhitespace()
            throws SAXException {

        int charactersOffset = nextInt();

        int length = nextInt();

        char[] characters = recording.getCharacterArray();
        handler.ignorableWhitespace(characters, charactersOffset, length);
    }

    // Javadoc inherited.
    public void processingInstruction() throws SAXException {

        String target = nextString();
        String data = nextString();

        // Generate the event.
        handler.processingInstruction(target, data);
    }

    // Javadoc inherited.
    public void startRecording() {

        String publicId = nextString();
        String systemId = nextString();

        locator.setPublicId(publicId);
        locator.setSystemId(systemId);

        handler.setDocumentLocator(locator);
    }

    // Javadoc inherited.
    public void endRecording() {
    }
}
