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
package com.volantis.mcs.dom.output;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import com.volantis.mcs.dom.output.util.CircularCharacterBuffer;
import com.volantis.mcs.dom.output.util.CircularCharacterBufferFullException;
import com.volantis.mcs.dom.output.util.CharacterMatcher;

import java.io.IOException;
import java.io.Writer;

/**
 * This is a wrapper for a <code>Writer</code> which will not let lines be
 * greater than a certain number of characters.
 * <p>
 * A carriage return will be inserted at a suitable point, preferably replacing
 * a single whitespace character.  A carraige return may also be added
 * immediately before the '&lt;' character.
 * <p>
 *
 * <strong>This class should only be used when there is a line length limit
 * since the logic breaks down when the maxLineLength is 0. To prevent this
 * - providing the constructor with a maxLineLength of 0 will result in an
 * {@link IllegalArgumentException}.</strong>
 */
public class LineLengthRestrictingWriter extends Writer {

    /**
     * CharacterMatcher to be used for finding whitespace in
     * the character buffer.
     */
    private final static CharacterMatcher
            WHITESPACE_CHARACTER_MATCHER = new CharacterMatcher() {

        public boolean matches(char c) {
            return Character.isWhitespace(c);
        }
    };

    /**
     * CharacterMatcher used for finding '<' in the character buffer.
     */
    private final static CharacterMatcher
        CHEVRON_MATCHER = new CharacterMatcher() {

            public boolean matches(char c) {
                return c == '<';
            }
        };

    /**
     * Constant for the new line character.
     */
    private static final char NEW_LINE_CHARACTER = '\n';

    /**
     * Constant for the String representation of the new line character.
     */
    private static final String NEW_LINE = new String(
            new char[]{NEW_LINE_CHARACTER});

    /**
     * The logging object to use in this class for localised logging services.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(LineLengthRestrictingWriter.class);

    /**
     * The <code>Writer</code> which these wrapper methods are forwarded to.
     */
    private Writer out;

    /**
     * The maximum line length (in characters) permitted for the requesting
     * device.
     */
    private int maxLineLength;

    /**
     * Provides a buffer where characters written are stored prior to
     * {@link #maxLineLength} being reached.  When this buffer is
     * full (or the next item(s) written would fill or exceed the buffer)
     * a suitable break point will be located and the contents
     * upto this break point will be written to {@link #out}.
     */
    private CircularCharacterBuffer characterBuffer;

    /**
     * Initialises the new instance with the supplied parameters.
     *
     * @param out the delegate <code>Writer</code>
     * @param maxLineLength the maximum length of a line in characters can
     *        not be 0. See class note.
     */
    public LineLengthRestrictingWriter(Writer out, int maxLineLength) {
        if (maxLineLength == 0) {
            throw new IllegalArgumentException("can not be 0: maxLineLength");
        }
        this.out = out;
        this.maxLineLength = maxLineLength;
        characterBuffer = new CircularCharacterBuffer(maxLineLength);
    }

    // Javadoc inherited
    public void write(int c) throws IOException {

        if (characterBuffer.isFull()) {

            // The buffer is full, ie max line limit has been reached.
            if (Character.isWhitespace((char)c)) {
                // We want to discard this whitespace as it is a suitable
                // place to break the line.

                // write the characters stored in the buffer
                flushBufferAddingNewLine();
            } else {

                // The line length has been reached. We need to
                // find a suitable place in the line to break, i.e
                // whitespace (including newline) or '<'.

                boolean bufferCanBeSplitOnWhitespace = false;
                boolean bufferCanBeSplitOnOpenAngledBracket = false;

                // Try searching for whitespace and break at this
                // point if possible.
                int lastIndexOfWhiteSpace =
                        characterBuffer.lastIndexMatching(
                                WHITESPACE_CHARACTER_MATCHER);

                if (lastIndexOfWhiteSpace != -1) {
                    // we can split the buffer on whitespace

                    // write the characters in the buffer upto but
                    // not including the whitespace.
                    writeCharacters(lastIndexOfWhiteSpace, false);

                    // As this breakpoint is whitespace we can remove this
                    // character as it will be replaced by the newline
                    // character.
                    // This ensures that page weight is preserved.
                    characterBuffer.removeChars(lastIndexOfWhiteSpace + 1);
                    bufferCanBeSplitOnWhitespace = true;
                } else {
                    int lastIndexOfOpenAngledBracket =
                        characterBuffer.lastIndexMatching(
                                CHEVRON_MATCHER);
                    if (lastIndexOfOpenAngledBracket != -1) {
                        // we can split the buffer just before the '<'.
                        writeCharacters(lastIndexOfOpenAngledBracket, true);
                        bufferCanBeSplitOnOpenAngledBracket = true;
                    }
                }

                if (bufferCanBeSplitOnOpenAngledBracket ||
                       bufferCanBeSplitOnWhitespace) {

                    // The buffer should have been reduced in size so we can
                    // now add the supplied character to the buffer.
                    addCharacterToBuffer((char)c);
                } else {
                    // The characters in the buffer could not be split on
                    // a whitespace character or an '<'.
                    // This is highly unlikely
                    // but if it happens lets empty the buffer as
                    // writing out the contents of buffer followed by a
                    // new line may break the device.
                    char[] charsInBuffer = characterBuffer.emptyBuffer();
                    String contentWhichExceedsLineLength =
                            new String(charsInBuffer);

                    // MCSDO0003W Could not writeout line as maximum line
                    // length of {0} would be exceeded for line: ''{1}''
                    // while adding ''{2}''
                    logger.warn("max-line-length-exceeded", new Object[] {
                            String.valueOf(maxLineLength), out.toString(),
                            contentWhichExceedsLineLength });

                    // now have room to add the new character
                    addCharacterToBuffer((char)c);

                    //@todo The most likely sitiation for this case to occur
                    //@todo is very long href values in either the anchor or
                    //@todo image element.  Should try to remove such long
                    //@todo hrefs leaving just <a>something</a>.
                    //@todo leaving for now due to lack of time and the fact
                    //@todo that this is unlikely to occur.
                }
            }
        } else if (isLineSeperator((char)c)) {

            // A naturally occuring new line has been supplied. Here is
            // a good point to write out characters in the buffer.

            // Discarding the supplied new line character as one will be added
            // during the flush.
            flushBufferAddingNewLine();
        } else {
           addCharacterToBuffer((char)c);
        }
    }

    /**
     * Writes the characters in the {@link #characterBuffer} from the start
     * of the buffer to the specified index (exclusive) followed by a new
     * line.
     *
     * @param index the index (exclusive) at which writing of characters from
     * the buffer will stop.
     *
     * @param logPageWeightIncreaseWarning indicates whether or not a
     * warning should be logged indicating that page weight is increasing.
     * This should be set to true if we are not splitting the buffer on
     * whitespace. No warning is required when splitting the buffer on
     * whitespace as the whitespace can be safely removed in favour of the
     * new line character.
     *
     * @throws IOException if an error occurs during the write.
     */
    private void writeCharacters(int index,
                                 boolean logPageWeightIncreaseWarning)
        throws IOException {

        char[] charsToWrite = characterBuffer.
        removeChars(index);
        out.write(charsToWrite);

        if (logPageWeightIncreaseWarning) {
            // log a warning indicating that page weight has been
            // increased.

            // MCSD00004W=Adding a new line character
            // therefore page weight increasing.
            logger.warn(
                    "adding-new-line-character-page-weight-increasing");
        }
        // write the line break
        out.write(NEW_LINE);
    }

    /**
     * Adds the supplied character to the buffer - {@link #characterBuffer}.
     *
     * @param character the character to be added to the buffer.
     */
    private void addCharacterToBuffer(char character) {
        try {
            characterBuffer.add(character);
        } catch (CircularCharacterBufferFullException e) {

            // This should never happen as calling code is responsible
            // for checking that there is room in the buffer. Log an
            // error for completeness.

            // MCSD00002E=Unable to add {0} to the character buffer
            logger.error("cannot-add-to-character-buffer",
                         new Object[]{String.valueOf(character)});
        }
    }

    /**
     * Returns true if the supplied character is a {@link #NEW_LINE}.
     *
     * @param character the character to be tested.
     *
     * @return true if the supplied character is a {@link #NEW_LINE};
     * otherwise false.
     */
    private boolean isLineSeperator(char character) {
        return NEW_LINE_CHARACTER == character;
    }

    // Javadoc inherited
    public void write(char cbuf[]) throws IOException {
        write(cbuf, 0, cbuf.length);
    }

    // Javadoc inherited
    public void write(char cbuf[], int off, int len)
            throws IOException {

        // write the characters in the supplied array one by one.
        for (int currentIndex = off; currentIndex < off + len; currentIndex++) {
            write(cbuf[currentIndex]);
        }
    }

    // Javadoc inherited
    public void write(String str) throws IOException {
        write(str, 0, str.length());
    }

    // Javadoc inherited
    public void write(String str, int off, int len) throws IOException {
        write(str.toCharArray(), off, len);
    }

    // Javadoc inherited
    public void close() throws IOException {
        flush();
        out.close();
    }

    // Javadoc inherited
    public void flush() throws IOException {

        char[] remainingCharacters =
                characterBuffer.emptyBuffer();

        // Write out characters that remain in the buffer.
        out.write(remainingCharacters);

        //out.write(NEW_LINE);
        out.flush();
    }

    // Javadoc inherited
    public String toString() {
        return out.toString();
    }

    /**
     * Flush the contents of the character and add a new line character.
     */
    private void flushBufferAddingNewLine() throws IOException {
        flush();
        out.write(NEW_LINE);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 11-Oct-05	9769/1	rgreenall	VBM:2005101101 Fixed incorrect implementation of write(char[] buf, int off, int length)

 03-Oct-05	9681/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 08-Aug-05	9205/1	rgreenall	VBM:2005062107 Forward port of VBM:2005062107

 04-Jul-05	8894/7	tom	VBM:2005062107 Added Extra Tests for witheld whitespace

 01-Jul-05	8894/5	tom	VBM:2005062107 Added line break to lines which are too big for device

 01-Jul-05	8894/3	tom	VBM:2005062107 Added line break to lines which are too big for device

 01-Jul-05	8894/1	tom	VBM:2005062107 Added line break to lines which are too big for device

 ===========================================================================
*/

