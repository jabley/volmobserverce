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
 * $Header: /src/voyager/com/volantis/mcs/protocols/text/LogicalWhitespaceWriter.java,v 1.4 2003/01/29 14:30:57 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Nov-02    Geoff           VBM:2002103005 - Created when refactoring the
 *                              old SMSWriter into multiple classes.
 * 21-Nov-02    Geoff           VBM:2002103005 - Inherit debugging setting from
 *                              SMS for convenience.
 * 27-Nov-02    Geoff           VBM:2002103005 - Refactor ignore code away 
 *                              after creating TextOutputBuffer, throw
 *                              IOExceptions and extend FilterWriter since we
 *                              refactored stream error handling, improve 
 *                              debugging somewhat.
 * 12-Dec-02    Geoff           VBM:2002121023 - Fix bug in flushWhitespace() 
 *                              and make Debug, Newline and Space member names 
 *                              comply with coding standards.
 * 29-Jan-03    Adrian          VBM:2003012104 - Made constructor and methods 
 *                              writeSpace, writeLine, writeTrimmed public 
 *                              instead of package private. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.text;

import com.volantis.mcs.utilities.WhitespaceUtilities;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * A writer which allows explicit control over whitespace in a logical fashion.
 * <p>
 * It allows the client to add "logical" spaces and newlines which are written
 * into the output only as and if required. Specifically:
 * <ul>
 *  <li>Multiple spaces compress to one space.
 *  <li>Multiple lines compress to one line.
 *  <li>Leading and trailing spaces on lines are ignored.
 *  <li>Literal whitespace content is not detected (unlike 
 *      {@link NormaliseWhitespaceWriter}.  
 * </ul>
 */ 
public class LogicalWhitespaceWriter extends FilterWriter {

    /**
     * The canonical newline character used for whitespace. 
     */
    private final static Character NEWLINE = new Character('\n');
    
    /** 
     * The canonical space character used for whitespace. 
     */
    private final static Character SPACE = new Character(' '); 

    /**
     * Set this to true if you want to see in line debugging comments in the
     * output.
     */ 
    private static final boolean debug = SMS.debug;
    
    /**
     * A character which stores the last whitespace which was "queued" to be 
     * written by one of the printXxxx methods.
     */ 
    private Character whitespace;

    /**
     * A simple flag to remember if we have written anything to our output 
     * writer.<p> Useful for detecting and removing leading whitespace.
     */ 
    private boolean writtenTo;

    /**
     * Constructs a new <code>LogicalWhitespaceWriter</code>, which writes 
     * though to the quiet writer specified.
     *
     * @param out the writer to send our output to.
     */ 
    public LogicalWhitespaceWriter(Writer out) {
        super(out);
    }

    //
    // Core methods which implement Writer, and co-operate with our logical
    // whitespace control stuff.
    //
    
    /**
     * Write a character to the output, respecting contained whitespace, 
     * and ignoring any IOExceptions.
     * 
     * @param c     char to write.
     * @exception IOException
     */ 
    public void write(int c) throws IOException {
        flushWhitespace();
        super.write(c);
        writtenTo = true;
    }

    /**
     * Write a section of a character array to the output, respecting
     * contained whitespace, and ignoring any IOExceptions.
     * 
     * @param cbuf  buffer to write from
     * @param off   character offset to start from
     * @param len   number of characters to write
     * @exception IOException
     */
    public void write(char cbuf[], int off, int len) throws IOException {
        if (len > 0) {
            flushWhitespace();
            super.write(cbuf, off, len);
            writtenTo = true;
        }
    }

    /**
     * Write a section of a string to the output, respecting contained 
     * whitespace, and ignoring any IOExceptions.
     * 
     * @param str   string to write from
     * @param off   character offset to start from
     * @param len   number of characters to write
     * @exception IOException
     */
    public void write(String str, int off, int len) throws IOException {
        if (len > 0) {
            flushWhitespace();
            super.write(str, off, len);
            writtenTo = true;
        }
    }

    //
    // Non Writer write methods for logical whitespace control 
    //
    
    /**
     * Writes a section of a character array, with leading and trailing 
     * whitespace removed, and ignoring any IOExceptions.
     * 
     * @param cbuf  buffer to write from
     * @param off   character offset to start from
     * @param len   number of characters to write
     * @exception IOException
     */ 
    public void writeTrimmed(char cbuf[], int off, int len) throws IOException {
        // Figure out which whitespace can be missed from the start and end.
        int trimOff = WhitespaceUtilities.getFirstNonWhitespaceIndex(
                cbuf, off, len);
        int trimEnd = WhitespaceUtilities.getLastNonWhitespaceIndex(
                cbuf, off, len);
        int trimLen = trimEnd + 1 - trimOff;
        // Special case where buffer has a zero length
        if( trimOff == trimEnd ) {
           trimLen = 0;
        }
        // Write the remains of the buffer, excluding surrounding whitespace.
        write(cbuf, trimOff, trimLen);
    }
    
    /**
     * Prints a logical space to the output. This will mask any whitespace 
     * considered to be unnecessary for the purposes of SMS output.
     * 
     * @exception IOException 
     */ 
    public void writeSpace() throws IOException {
        // Queue a space, if we don't already have a newline. 
        if (whitespace == null) {
            whitespace = SPACE;
        }
    }

    /**
     * Prints a logical line to the output. This will mask any whitespace 
     * considered to be unnecessary for the purposes of SMS output. 
     * 
     * @exception IOException 
     */ 
    public void writeLine() throws IOException {
        // Queue a newline, overwriting any queued space.
        whitespace = NEWLINE;
    }
    
    /**
     * Called by the write methods to "lazy write" whitespace when we 
     * really have to.

     * @exception IOException
     */ 
    private void flushWhitespace() throws IOException {
        if (whitespace != null) {
            if (writtenTo) {
                if (debug) {
                    if (whitespace == SPACE) {
                        super.write("_");
                    } else if (whitespace == NEWLINE) {
                        super.write("\u00AC");
                        super.write(NEWLINE.charValue());
                    } else {
                        super.write("{" + whitespace.charValue() + "}");
                    }
                } else {
                    super.write(whitespace.charValue());
                }
            }
            whitespace = null;
        }
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Mar-04	3419/1	pduffin	VBM:2004031203 Fixed build problems with non ascii characters

 ===========================================================================
*/
