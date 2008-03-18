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

import com.volantis.xml.sax.recorder.SAXPlayer;
import com.volantis.xml.sax.recorder.SAXRecording;
import com.volantis.xml.sax.recorder.impl.attributes.AttributeContainerBuilder;
import com.volantis.xml.sax.recorder.impl.attributes.AttributesContainer;
import com.volantis.xml.sax.recorder.impl.attributes.AttributesWindow;
import com.volantis.xml.sax.recorder.impl.recording.events.SAXEventType;

import java.io.Serializable;

/**
 * A thread safe recording that is safe to cache.
 *
 * <p>This stores the recording in three parts, an array of unique strings used
 * for those events that take {@link String}s and also for attribute
 * strings, an array of characters used for content, an
 * {@link AttributesContainer} and last but not least an array of ints used to
 * record the main information about the event stream.</p>
 *
 * <p>Each event is stored as a sequence of ints in the {@link #intArray},
 * starting with the index of the event type followed by some event specific
 * information.</p>
 *
 * <p>This recording stores each event type as follows, where a value is
 * stored as a String what is actually stored is an index into the string
 * array:</p>
 *
 * <h3>setDocumentLocator</h3>
 *
 * <ol>
 * <li>Public ID - as string.</li>
 * <li>System ID - as string.</li>
 * </ol>
 *
 * <h3>startDocument</h3>
 *
 * <ol>
 * <li>Forward reference to matching endDocument event.</li>
 * </ol>
 *
 * <h3>endDocument</h3>
 *
 * <p>Nothing stored.</p>
 *
 * <h3>startElement</h3>
 *
 * <p><strong>Note:</strong> The startPrefixMapping events are not treated
 * separately, rather they are treated as an intergral part of the startElement
 * event (which is as it should be).</p>
 *
 * <ol>
 * <li>A count of the number of startPrefixMapping events.</li>
 * <li>For each startPrefixMapping event the following is stored:
 * <ol>
 * <li>Prefix - as string.</li>
 * <li>URI - as string.</li>
 * </ol>
 * </li>
 * <li>Namespace URI - as string.</li>
 * <li>Local Name - as string.</li>
 * <li>QName - as string.</li>
 * <li>Offset to start of attributes in the container.</li>
 * <li>Length of window within the attributes container.</li>
 * <li>Forward reference to matching endElement event.</li>
 * </ol>
 *
 * <h3>endElement</h3>
 *
 * <p><strong>Note:</strong> The endPrefixMapping events are not treated
 * separately, rather they are treated as an intergral part of the endElement
 * event (which is as it should be).</p>
 *
 * <ol>
 * <li>Namespace URI - as string.</li>
 * <li>Local Name - as string.</li>
 * <li>QName - as string.</li>
 * <li>A count of the number of endPrefixMapping events.
 * <li>For each endrefixMapping event the following is stored:
 * <ol>
 * <li>Prefix - as string.</li>
 * </ol>
 * </li>
 * </ol>
 *
 * <h3>characters</h3>
 *
 * <p><strong>Note:</strong> Consecutive characters event are coalesced into a
 * single event, this typically occurs if the input stream is being produced
 * by a parser that splits the content up into lines and generates an event for
 * each line.</p>
 *
 * <ol>
 * <li>Offset of start of characters in the characters array.</li>
 * <li>Length of characters.</li>
 * </ol>
 *
 * <h3>ignorableWhitespace</h3>
 *
 * <p><strong>Note:</strong> Consecutive ignorableWhitespace event are
 * coalesced into a single event, this typically occurs if the input stream is
 * being produced by a parser that splits the content up into lines and
 * generates an event for each line.</p>
 *
 * <ol>
 * <li>Offset of start of characters in the characters array.</li>
 * <li>Length of characters.</li>
 * </ol>
 *
 * <h3>processingInstruction</h3>
 *
 * <ol>
 * <li>Target - as string.</li>
 * <li>Data - as string.</li>
 * </ol>
 */
public class SAXRecordingImpl
        implements SAXRecording, Serializable {

    /**
     * Bit that indicates that the
     */
    public static final int RECORD_PER_EVENT_LOCATION = 1 << 0;

    /**
     * The array of strings.
     */
    private final String[] stringArray;

    /**
     * The array of ints.
     */
    private final int[] intArray;

    /**
     * The array of characters.
     */
    private final char[] characterArray;

    /**
     * The container for all the recorded attributes.
     */
    private final AttributesContainer attributeContainer;

    /**
     * A flag that indicates that the content is complex, i.e. contains markup
     * as well as character data.
     */
    private final boolean complex;

    /**
     * Initialise from an existing recording.
     *
     * <p>This copies only the significant parts of the contained arrays and
     * objects in order to reduce wastage. An alternative approach would be
     * to make this sensitive to whether this object and the other object are
     * read only or writeable and maximising the sharing of arrays. However,
     * as it is likely that the only time a recording will be copied in this
     * way is to create a read only recording from a writable recording this
     * implementation will suffice.</p>
     *
     * @param stringArray
     * @param intCount
     * @param intArray
     * @param characterCount
     * @param characterArray
     * @param attributeBuilder
     * @param complex
     */
    public SAXRecordingImpl(
            String[] stringArray,
            int intCount, int[] intArray,
            int characterCount, char[] characterArray,
            AttributeContainerBuilder attributeBuilder,
            boolean complex) {

        this.stringArray = stringArray;

        // Copy the ints.
        this.intArray = ArrayHelper.copyArray(intArray, intCount);

        // Copy the characters.
        this.characterArray =
                ArrayHelper.copyArray(characterArray, characterCount);

        this.attributeContainer = attributeBuilder.buildContainer();

        this.complex = complex;
    }

    // Javadoc inherited.
    public SAXPlayer createPlayer() {
        return new SAXPlayerImpl(this);
    }

    // Javadoc inherited.
    public boolean isComplex() {
        return complex;
    }

    /**
     * Create a window on the attributes.
     *
     * <p>This is provided solely for the use of {@link SAXPlayerImpl}.</p>
     *
     * @return The window on the attributes.
     */
    AttributesWindow createWindow() {
        return attributeContainer.createWindow();
    }

    /**
     * Get the event type.
     *
     * @param index The index within the int array for the event type.
     * @return The SAXEventType.
     */
    public SAXEventType getEventType(int index) {
        int eventTypeIndex = getInt(index);
        return SAXEventType.getEventType(eventTypeIndex);
    }

    /**
     * Get the int at the specified index in the int array.
     *
     * <p>This is provided solely for the use of {@link SAXPlayerImpl}.</p>
     *
     * @param index The index.
     * @return The value retrieved.
     */
    int getInt(int index) {
        return intArray[index];
    }

    /**
     * Get the string at the specified index in the string array.
     *
     * <p>This is provided solely for the use of {@link SAXPlayerImpl}.</p>
     *
     * @param index The index.
     * @return The value retrieved.
     */
    String getString(int index) {
        return stringArray[index];
    }

    /**
     * Get the character array.
     *
     * <p>This is provided solely for the use of {@link SAXPlayerImpl}.</p>
     *
     * <p><strong>This method provides a way for a client to modify the
     * contents of a read only recording as the returned character array is the
     * one that stores the character data for the recording. Therefore, it
     * should be used with great care.</strong></p>
     *
     * @return The character array.
     */
    char[] getCharacterArray() {
        return characterArray;
    }
}
