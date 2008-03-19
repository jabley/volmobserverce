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

package com.volantis.xml.sax.recorder.impl;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.sax.recorder.SAXRecorder;
import com.volantis.xml.sax.recorder.SAXRecorderConfiguration;
import com.volantis.xml.sax.recorder.SAXRecording;
import com.volantis.xml.sax.recorder.impl.attributes.AttributeContainerBuilder;
import com.volantis.xml.sax.recorder.impl.attributes.AttributeContainerBuilderImpl;
import com.volantis.xml.sax.recorder.impl.recording.ArrayHelper;
import com.volantis.xml.sax.recorder.impl.recording.PlaceHolder;
import com.volantis.xml.sax.recorder.impl.recording.PlaceHolderAccessor;
import com.volantis.xml.sax.recorder.impl.recording.PlaceHolderList;
import com.volantis.xml.sax.recorder.impl.recording.SAXRecordingImpl;
import com.volantis.xml.sax.recorder.impl.recording.StringTable;
import com.volantis.xml.sax.recorder.impl.recording.events.SAXEventType;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Default implementation of {@link SAXRecorder}.
 */
public class SAXRecorderImpl
        implements SAXRecorder, ContentHandler, PlaceHolderAccessor {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(SAXRecorderImpl.class);

    /**
     * Indicates whether ignorableWhitespace should be recorded.
     */
    private final boolean discardIgnorableWhitespace;

    /**
     * Indicates whether per event location information should be recorded.
     */
    private final boolean recordPerEventLocation;

    /**
     * The string table to use to ensure uniqueness of strings used within the
     * recording.
     */
    private final StringTable stringTable;

    /**
     * The builder for the attributes.
     */
    private final AttributeContainerBuilder attributeBuilder;

    /**
     * The stack of {@link PlaceHolderList}s that represent the forward
     * references to the position of the balancing event.
     *
     * <p>An entry is added into the stack for each nesting level, one for
     * the startDocument and one each for the startElement.</p>
     */
    private final Stack balancingEventPositionReferences;

    /**
     * A stack of the locations of prefix mappings. The location in the
     * intArray of a set of prefix mappings. The element at the specified
     * location is the number of prefix mappings that follow.
     */
    private final Stack prefixMappingLocations;

    /**
     * The recording that is returned to the user.
     */
    private SAXRecording recording;

    /**
     * The number of ints stored in {@link #intArray}.
     */
    private int intCount;

    /**
     * The array of ints.
     */
    private int[] intArray;

    /**
     * The number of characters stored in {@link #characterArray}.
     */
    private int characterCount;

    /**
     * The array of characters.
     */
    private char[] characterArray;

    /**
     * Indicates whether the recording is in progress. This is true from the
     * time this object is created until the {@link ContentHandler#endDocument}.
     * After that time it is false.
     */
    private boolean recordingInProgress;

    /**
     * The event type of the last event that was recorded, used to determine
     * whether or not two characters or ignorableWhitespace events have
     * happened consecutively and so can be coalesced.
     */
    private SAXEventType lastEventType;

    /**
     * The place holder for the length of the last character (characters or
     * ignorableWhitespace) event that occurred.
     *
     * <p>If another event is coalesced with this then this is used to update
     * the length to include the additional characters in the subsequent
     * event.</p>
     *
     * <p>This is reused and moved from one location to another.</p>
     */
    private final PlaceHolder lastCharacterEventLength;

    /**
     * A count of the number of prefix mapping events that precede a
     * startElement event.
     *
     * <p>This is used to
     */
    private int prefixMappingCount;

    /**
     * The place holder for updating the number of start prefix mapping
     * events that have occurred.
     *
     * <p>This is reused and moved from one location to another.</p>
     */
    private final PlaceHolder prefixMappingCountReference;

    /**
     * The locator for event information, may be null.
     */
    private Locator locator;

    /**
     * The nesting depth.
     *
     * <p>{@link #startDocument} and {@link #startElement} increment this,
     * {@link #endDocument} and {@link #endElement}.</p>
     */
    private int nestingDepth;

    /**
     * Keeps track of whether a {@link SAXEventType#START_RECORDING} event has
     * been generated.
     */
    private boolean startedRecording;

    /**
     * Keeps track of whether the content is complex, i.e. has seen elements.
     */
    private boolean complex;

    /**
     * Initialise.
     *
     * @param configuration The configuration.
     */
    public SAXRecorderImpl(SAXRecorderConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException(
                    "Configuration must not be null");
        }

        this.discardIgnorableWhitespace =
                configuration.getDiscardIgnorableWhitespace();
        this.recordPerEventLocation = configuration.getRecordPerEventLocation();

        this.stringTable = new StringTable();
        this.intArray = new int[32];
        this.characterArray = new char[1024];
        this.attributeBuilder = new AttributeContainerBuilderImpl(stringTable);
        this.balancingEventPositionReferences = new Stack();
        recordingInProgress = true;
        prefixMappingLocations = new Stack();

        lastCharacterEventLength = new PlaceHolder(this);
        prefixMappingCountReference = new PlaceHolder(this);
    }

    // Javadoc inherited.
    public ContentHandler getContentHandler() {
        return this;
    }

    // Javadoc inherited.
    public SAXRecording getRecording() {
        if (recording == null) {
            if (nestingDepth != 0) {
                throw new IllegalStateException(
                        "Recording still in progress");
            }

            addEvent(SAXEventType.END_RECORDING);

            String[] stringArray = stringTable.toArray();

            recording = new SAXRecordingImpl(stringArray,
                    intCount, intArray, characterCount, characterArray,
                    attributeBuilder, complex);
        }

        return recording;
    }

    /**
     * Add an event to the array of events, expanding it if necessary.
     *
     * @param eventType The event type to add.
     */
    private void addEvent(SAXEventType eventType) {
        int eventTypeIndex = eventType.getIndex();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Event type=" + eventType.toString());
        }
        addInt(eventTypeIndex);
        lastEventType = eventType;

        if (recordPerEventLocation && eventType.supportsLocation() &&
                locator != null) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("writing Location information");
            }

            addString(locator.getPublicId());
            addString(locator.getSystemId());
            addInt(locator.getLineNumber());
            addInt(locator.getColumnNumber());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("finished writing Location information");
            }

        }

        complex |= eventType.isComplex();
    }

    /**
     * Add a string.
     *
     * <p>Gets an index of the string in the string table and adds it to the
     * int array.</p>
     *
     * @param value The string to add.
     */
    private void addString(String value) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Adding String '" + value + "'");
        }

        int index = stringTable.getIndex(value);
        addInt(index);
    }

    /**
     * Add a sequence of characters to the recording.
     *
     * @param eventType  The event type of this event, used to determine whether
     *                   it can be coalesced with preceding event.
     * @param characters The characters array.
     * @param offset     The offset into the characters array.
     * @param length     The length of the range within the characters array.
     */
    private void addCharacters(
            final SAXEventType eventType,
            char[] characters, int offset, int length) {

        boolean coalesce = true;

        startRecording();

        // Coalesce adjacent character events together.
        if (lastEventType != eventType) {
            // Add the event type.
            addEvent(eventType);

            coalesce = false;
        }

        if (coalesce) {
            int lastLength = lastCharacterEventLength.getValue();
            int totalLength = lastLength + length;
            lastCharacterEventLength.setValue(totalLength);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("characters (coalesce)='" +
                             new String(characters,offset, length) + "'");
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("characters='" +
                             new String(characters, offset, length) + "'");
            }
            addInt(characterCount);

            addPlaceHolderValue(lastCharacterEventLength);

            lastCharacterEventLength.setValue(length);
        }

        ensureCharacterArrayCapacity(characterCount + length);
        System.arraycopy(characters, offset, characterArray, characterCount,
                length);
        characterCount += length;
    }

    /**
     * Ensure that the character array has the required capacity, expanding it
     * if it does not.
     *
     * @param requiredCapacity The required capacity.
     */
    private void ensureCharacterArrayCapacity(int requiredCapacity) {
        int capacity = characterArray.length;
        if (requiredCapacity > capacity) {
            while (capacity < requiredCapacity) {
                capacity <<= 1;
            }

            // Expand the character array.
            characterArray = ArrayHelper.expandArray(characterArray, capacity);
        }
    }

    /**
     * Add a place holder value to the recording.
     * @param placeHolder The place holder that will be moved to the current
     * position and can be used to update the value.
     */
    private void addPlaceHolderValue(final PlaceHolder placeHolder) {
        placeHolder.move();
        addInt(0);
    }

    /**
     * Add a place holder value to the recording.
     *
     * <p>This will add a value, create a {@link PlaceHolder} instance to
     * allow that value to be updated and then add it to the list.</p>
     *
     * @param list The list to which the newly create {@link PlaceHolder}
     * @param initialValue
     */
    private void addPlaceHolderValue(PlaceHolderList list, int initialValue) {
        // Create the place holder here as it automatically picks up the
        // current position which will be changed by the addInt() method that
        // follows.
        PlaceHolder placeHolder = new PlaceHolder(this);

        // Add a dummy value.
        addInt(initialValue);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Added placeholder");
        }

        list.addPlaceHolder(placeHolder);
    }

    /**
     * Add an int to the recording.
     *
     * @param value The value to add.
     */
    private void addInt(int value) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Adding int index at " + intCount + " value " + value);
        }
        ensureIntArrayCapacity(intCount + 1);
        intArray[intCount] = value;
        intCount += 1;
    }

    /**
     * Return the integer at the specified location
     *
     * @param location the location of the int to return
     * @return the integer at the specified location
     */
    private int getInt(int location) {
       return intArray[location];
    }

    /**
     * Ensure that the int array has the required capacity, expanding it
     * if it does not.
     *
     * @param requiredCapacity The required capacity.
     */
    private void ensureIntArrayCapacity(int requiredCapacity) {
        int capacity = intArray.length;
        if (requiredCapacity > capacity) {
            while (capacity < requiredCapacity) {
                capacity <<= 1;
            }

            // Expand the character array.
            intArray = ArrayHelper.expandArray(intArray, capacity);
        }
    }

    /**
     * Make sure that recording is still in progress.
     *
     * <p>Throws an {@link IllegalStateException} if recording has already
     * finished.</p>
     */
    private void ensureRecording() {
        if (!recordingInProgress) {
            throw new IllegalStateException(
                    "Event received after recording stopped");
        }
    }

    /**
     * Create a new {@link PlaceHolderList} and push it onto the stack of
     * references to balancing event positions.
     *
     * @return The newly created {@link PlaceHolderList}.
     */
    private PlaceHolderList pushBalancingEventPositionReferences() {
        PlaceHolderList referenceList = new PlaceHolderList();
        balancingEventPositionReferences.push(referenceList);
        return referenceList;
    }

    /**
     * Pop the current {@link PlaceHolderList} pff the stack of balancing
     * event positions.
     *
     * @return The popped {@link PlaceHolderList}.
     */
    private PlaceHolderList popBalancingEventPositionReferences() {
        return (PlaceHolderList) balancingEventPositionReferences.pop();
    }

    /**
     * Peek the current {@link PlaceHolderList} pff the stack of balancing
     * event positions.
     *
     * @return The peeked {@link PlaceHolderList}.
     */
    private PlaceHolderList peekEndElementReferences() {
        if (balancingEventPositionReferences.isEmpty()) {
            return null;
        } else {
            return (PlaceHolderList) balancingEventPositionReferences.peek();
        }
    }

    // Javadoc inherited.
    public void setPlaceHolderValue(int placeHolderIndex, int newValue) {
        intArray[placeHolderIndex] = newValue;
    }

    // Javadoc inherited.
    public int getPlaceHolderValue(int placeHolderIndex) {
        return intArray[placeHolderIndex];
    }

    // Javadoc inherited.
    public int getCurrentPosition() {
        return intCount;
    }

    /**
     * Records the event.
     */
    public void setDocumentLocator(Locator locator) {

        // Make sure that recording is happening.
        ensureRecording();

        this.locator = locator;
    }

    private void startRecording() {
        if (!startedRecording) {
            int locationOptions = 0;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Starting Recording");
            }

            if (locator != null && recordPerEventLocation) {
                locationOptions |= SAXRecordingImpl.RECORD_PER_EVENT_LOCATION;
            }
            addInt(locationOptions);
            startedRecording = true;

            addEvent(SAXEventType.START_RECORDING);

            String publicId;
            String systemId;
            if (locator == null) {
                publicId = null;
                systemId = null;
            } else {
                publicId = locator.getPublicId();
                systemId = locator.getSystemId();
            }

            addString(publicId);
            addString(systemId);
        }
    }

    /**
     * Records the event.
     */
    public void startDocument()
            throws SAXException {

        // Make sure that recording is happening.
        ensureRecording();

        startRecording();

        // Add the event type.
        addEvent(SAXEventType.START_DOCUMENT);

        // Add a list of forward references to the position of the balancing
        // event.
        pushBalancingEventPositionReferences();

        nestingDepth += 1;
    }

    /**
     * Records the event.
     */
    public void endDocument()
            throws SAXException {

        // Make sure that recording is happening.
        ensureRecording();

        // Add the event type.
        addEvent(SAXEventType.END_DOCUMENT);

        // Pop the list of references.
        popBalancingEventPositionReferences();

        // We are no longer recording.
        recordingInProgress = false;
        nestingDepth -= 1;
    }

    /**
     * Records the event.
     */
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {

        // Make sure that recording is happening.
        ensureRecording();

        // If this is the first startPrefixMapping event associated with the
        // startElement event then add the event type and a place holder
        // to the total number of prefix mapping events.
        if (prefixMappingCount == 0) {

            startRecording();

            // Add the event type.
            addEvent(SAXEventType.START_ELEMENT);

            // Add a forward reference to the number of prefix mapping events.
            addPlaceHolderValue(prefixMappingCountReference);
        }

        // Add the prefix and uri.
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("start prefix mapping '" + prefix +"' : '" + uri +"' count "+(prefixMappingCount+1));
        }
        addString(prefix);
        addString(uri);

        // Keep track of the number of prefix mappings.
        prefixMappingCount += 1;
    }

    /**
     * Records the event.
     */
    public void endPrefixMapping(String prefix)
            throws SAXException {
        // do nothing. The prefix mappings are automatically closed when the
        // element they were attached to closes.
        // This is done so the recorder is robust to mismatched namespace start
        // and end events (which happens quite a lot in the pipeline)
    }

    /**
     * Records the event.
     */
    public void startElement(
            String namespaceURI, String localName,
            String qName, Attributes attributes)
            throws SAXException {

        // Make sure that recording is happening.
        ensureRecording();

        if (prefixMappingCount == 0) {

            startRecording();

            // This element does not have any associated prefix mappings so
            // add the event type and 0 for the number of prefix mapping
            // events.
            addEvent(SAXEventType.START_ELEMENT);
            // Add a forward reference to the number of prefix mapping events
            // as if there were no prefix mappings the place holder will not
            // be set up
            addPlaceHolderValue(prefixMappingCountReference);
        } else {
            // Some startPrefixMapping events have already occurred so the
            // event type has already been added but the prefix mapping count
            // needs updating.
            prefixMappingCountReference.setValue(prefixMappingCount);
        }

        // Push the number of prefix mapping counts associated with this
        // element so that it can be picked up by the matching endElement
        // event.
        prefixMappingLocations.push(new Integer(prefixMappingCountReference.getIndex()));
        prefixMappingCount = 0;

        // Add the strings.
        addString(namespaceURI);
        addString(localName);
        addString(qName);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("added namespace, localname and qName");
        }

        // Add the offset to the start of the attributes in the container.
        addInt(attributeBuilder.getOffset());

        // Add the length.
        addInt(attributes.getLength());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("added attribute offset and number of attributes");
        }


        attributeBuilder.addAttributes(attributes);

        // Add a forward reference to the matching end element event.
        PlaceHolderList referenceList =
                pushBalancingEventPositionReferences();

        addPlaceHolderValue(referenceList, -1);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("   for forward reference");
        }

        nestingDepth += 1;
    }

    /**
     * Records the event.
     */
    public void endElement(
            String namespaceURI, String localName,
            String qName)
            throws SAXException {

        // Make sure that recording is happening.
        ensureRecording();

        if (prefixMappingCount != 0) {
            throw new IllegalStateException("Ill formed event stream, get endElement with no " +
                    "matching startElement");
        }

        // get the location of the start prefix mappings.
        int prefixMappingLocation = ((Integer) prefixMappingLocations.pop()).intValue();
        int prefixMappingCount = intArray[prefixMappingLocation++];

        // Resolve the forward reference to the start of this event.
        PlaceHolderList referenceList =
                popBalancingEventPositionReferences();
        referenceList.setValue(intCount);

        // Add the event type.
        addEvent(SAXEventType.END_ELEMENT);

        // Add the strings.
        addString(namespaceURI);
        addString(localName);
        addString(qName);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("added namespace, localname and qName");
        }

        // Add a reference to the containing endElement (or endDocument)
        // event if necessary.
        referenceList = peekEndElementReferences();
        if (referenceList == null) {
            addInt(-1);
        } else {
            addPlaceHolderValue(referenceList, -1);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("added reference to containing end element");
        }

        // Add the number of expected prefix mapping events after this one.
        addInt(prefixMappingCount);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("added prefix mapping count");
        }

        // copy the prefix mappings that where defined on the start element.
        // This effectively balances the prefix mappings.
       copyPrefixMappings(prefixMappingLocation, prefixMappingCount);

        nestingDepth -= 1;
    }

    /**
     * Copy the prefix mappings from the specified location to the current
     * location.
     *
     * @param location the location to copy the mappings from. This location
     * is actually the location containing the number of prefix mappings
     */
    private void copyPrefixMappings(int location, int prefixMappingCount) {
        // copy the locations. We do not need to actually copy the strings from
        // the stringTable. Only copy the references to them.
        for (int i=0; i<prefixMappingCount; i++) {
            // add the prefix
            addInt(getInt(location));
            location++;
            //step over the the url as we only need the prefixes for endPrefix
            // events
            location++;
        }
    }

    /**
     * Records the event.
     */
    public void characters(char ch[], int start, int length)
            throws SAXException {

        // Make sure that recording is happening.
        ensureRecording();

        // Add the characters.
        addCharacters(SAXEventType.CHARACTERS, ch, start, length);
    }

    /**
     * Records the event.
     */
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {

        // Make sure that recording is happening.
        ensureRecording();

        // Record the event unless it should be discarded.
        if (!discardIgnorableWhitespace) {
            // Add the recording.
            addCharacters(SAXEventType.IGNORABLE_WHITESPACE, ch, start, length);
        }
    }

    /**
     * Records the event.
     */
    public void processingInstruction(String target, String data)
            throws SAXException {

        // Make sure that recording is happening.
        ensureRecording();

        startRecording();

        // Add the event type.
        addEvent(SAXEventType.PROCESSING_INSTRUCTION);

        // Add the strings.
        addString(target);
        addString(data);
    }

    /**
     * Event recording not supported.
     */
    public void skippedEntity(String name)
            throws SAXException {

        throw new UnsupportedOperationException(
                "skippedEntity events cannot be recorded");
    }
}
