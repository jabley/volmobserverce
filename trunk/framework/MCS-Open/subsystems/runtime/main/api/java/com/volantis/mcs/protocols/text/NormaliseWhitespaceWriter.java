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
 * $Header: /src/voyager/com/volantis/mcs/protocols/text/NormaliseWhitespaceWriter.java,v 1.4 2003/01/29 14:30:57 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Nov-02    Geoff           VBM:2002103005 - Created when refactoring the
 *                              old SMSWriter into multiple classes.
 * 22-Nov-02    Geoff           VBM:2002103005 - Fix bug with blocks starting 
 *                              or finishing with whitespace in write([]).
 * 27-Nov-02    Geoff           VBM:2002103005 - Throw IOExceptions and extend 
 *                              FilterWriter since we refactored stream error 
 *                              handling, improve debugging somewhat.
 * 12-Dec-02    Geoff           VBM:2002121023 - Fix bug in write() methods 
 *                              and make Debug member name comply with coding 
 *                              standards.
 * 29-Jan-03    Adrian          VBM:2003012104 - Made constructor public 
 *                              instead of package private. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.text;

import com.volantis.synergetics.cornerstone.utilities.WhitespaceUtilities;

import java.io.FilterWriter;
import java.io.IOException;

/**
 * A writer which "normalises" literal whitespace characters present in the 
 * data passed to it to be written.
 * <p>
 * Normalisation is defined as removing all newlines, and reducing interword 
 * spacing to one space character.
 *
 * @see SMS 
 * @see LogicalWhitespaceWriter
 */
public final class NormaliseWhitespaceWriter extends FilterWriter {

    /**
     * Set this to true if you want to see in line debugging comments in the
     * output.
     */ 
    private static final boolean debug = SMS.debug;

    /**
     * The logical whitespace writer we write our output to.
     */ 
    private final LogicalWhitespaceWriter logical;
    
    /**
     * Constructs a new <code>NormaliseWhitespaceWriter</code>, which writes 
     * though to the writer specified.
     * @param out the writer to send our output to.
     */ 
    public NormaliseWhitespaceWriter(LogicalWhitespaceWriter out) {
        super(out);
        logical = out;
    }

    /**
     * Writes a char to the output, normalising any unneccessary whitespace.
     * 
     * @exception IOException
     */ 
    public void write(int c) throws IOException {
        if (debug) { 
            logical.write("[");
        }
        if (Character.isWhitespace((char)c)) {
            logical.writeSpace();
        } else {
            logical.write((char)c);
        }
        if (debug) { 
            logical.write("]");
        }
    }
   
    /**
     * Writes a char[] to the output, normalising any unneccessary whitespace.
     *
     * @exception IOException
     */ 
    public void write(char cbuf[], int off, int len) throws IOException {
        // NOTE: this code is identical to the string version below.
        // If you modify this, modify it as well!
        
        // Quote the entire block for debugging.
        if (debug) { 
            logical.write("[");
        }
        int end = off + len;
        if ((off | len | (cbuf.length - (len + off)) | end) < 0)
            throw new IndexOutOfBoundsException();
        int wordOff;
        int wordEnd = off;
        // While there is another word left in the buf,
        while ((wordOff = WhitespaceUtilities.getFirstNonWhitespaceIndex(
                cbuf, wordEnd, len - (wordEnd - off))) < end) {
            // If this is the first word,
            if (wordEnd == off) {
                // if we skipped some whitespace
                if (wordOff > off) {
                    // add a space in before the first word
                    logical.writeSpace();
                }
            } else {
                // Print a space between words.
                logical.writeSpace();
            }
            // Search for the end of the word.
            wordEnd = WhitespaceUtilities.getFirstWhitespaceIndex(
                    cbuf, wordOff, len - (wordOff - off));
            // Print logical the word.
            logical.write(cbuf, wordOff, wordEnd - wordOff);
        }
        // If we skipped whitespace (after last word, or if no words)
        if (wordEnd < end) {
            // Add a space 
            logical.writeSpace();
        }
        // End the quote for the entire block.
        if (debug) {
            logical.write("]");
        }
    }

    /**
     * Writes a String to the output, normalising any unneccessary whitespace.
     * 
     * @exception IOException
     */ 
    public void write(String str, int off, int len) throws IOException {
        // NOTE: this code is identical to the array version above.
        // If you modify this, modify it as well!

        // Quote the entire block for debugging.
        if (debug) { 
            logical.write("[");
        }
        int end = off + len;
        if ((off | len | (str.length() - (len + off)) | end) < 0)
            throw new IndexOutOfBoundsException();
        int wordOff;
        int wordEnd = off;
        // While there is another word left in the buf,
        while ((wordOff = WhitespaceUtilities.getFirstNonWhitespaceIndex(
                str, wordEnd, len - (wordEnd - off))) < end) {
            // If this is the first word,
            if (wordEnd == off) {
                // if we skipped some whitespace
                if (wordOff > off) {
                    // add a space in before the first word
                    logical.writeSpace();
                }
            } else {
                // Print a space between words.
                logical.writeSpace();
            }
            // Search for the end of the word.
            wordEnd = WhitespaceUtilities.getFirstWhitespaceIndex(
                    str, wordOff, len - (wordOff - off));
            // Print logical the word.
            logical.write(str, wordOff, wordEnd - wordOff);
        }
        // If we skipped whitespace (after last word, or if no words)
        if (wordEnd < end) {
            // Add a space 
            logical.writeSpace();
        }
        // End the quote for the entire block.
        if (debug) {
            logical.write("]");
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

 ===========================================================================
*/
