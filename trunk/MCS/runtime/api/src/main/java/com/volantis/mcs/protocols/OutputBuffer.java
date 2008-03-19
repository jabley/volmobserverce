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
 * $Header: /src/voyager/com/volantis/mcs/protocols/OutputBuffer.java,v 1.6 2002/03/18 12:41:17 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Oct-01    Paul            VBM:2001111402 - Created
 * 28-Feb-02    Paul            VBM:2002022804 - The original implementation
 *                              was not suitable for this (which was not
 *                              surprising as it was not being used much if
 *                              at all) so this has been modified to make it
 *                              suitable.
 * 08-Mar-02    Paul            VBM:2002030607 - Added getWriter method.
 * 13-Mar-02    Paul            VBM:2002031301 - Added getCurrentBuffer method
 *                              to return the current buffer associated with
 *                              this buffer.
 * 13-Mar-02    Paul            VBM:2002030104 - Removed deprecatedAdd method
 *                              as it is no longer needed.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 24-Apr-03    Steve           VBM:2003041606 - Added whitespace processing state 
 *                              constants
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import java.io.Writer;

/**
 * This interface defines the methods which should be implemented by a class
 * which can be used as the destination for the output from the PAPI.
 *
 * @mock.generate
 */
public interface OutputBuffer {

    // Whitespace processing constants

    /**
     * Keep all whitespace
     */
    public static final int WS_KEEP = 0;

    /**
     * Remove all whitespace start and end
     */
    public static final int WS_IGNORE = 1;

    /**
     * After the start of an element
     */
    public static final int WS_AFTER_START = 2;

    /**
     * Removing whitespace from between text
     */
    public static final int WS_PRUNING = 3;

    /**
     * Get the writer associated with this buffer.
     */
    public Writer getWriter();

    /**
     * Get the current OutputBuffer.
     */
    public OutputBuffer getCurrentBuffer();

    /**
     * Sets whether this buffer should trim leading and trailing whitespace
     * characters. This flag must be set before any content is added to the
     * buffer. It defaults to false.
     */
    public void setTrim(boolean trim);

    /**
     * Check whether this buffer is empty or not.
     *
     * @return True if this buffer is empty and false otherwise.
     */
    public boolean isEmpty();

    /**
     * Check to see whether this buffer is just white space or not.
     * <p/>
     * This is just a hack to work around a problem with white space which should
     * be sorted by
     * </p>
     *
     * @return True if this buffer contains just whitespace and false otherwise.
     */
    public boolean isWhitespace();

    /**
     * @deprecated This method should no longer be called.
     */
    public void initialise();

    /**
     * Write some literal text to the output buffer.
     *
     * @param text       a character array holding the text to write
     * @param off        the index of the first character in the character array
     * @param len        the number of characters to write
     * @param preEncoded Indicates the text has already been encoded.
     */
    public void writeText(char[] text, int off, int len, boolean preEncoded);

    /**
     * Write some non preencoded literal text to the output buffer.
     *
     * @param text a character array holding the text to write
     * @param off  the index of the first character in the character array
     * @param len  the number of characters to write
     */
    public void writeText(char[] text, int off, int len);

    /**
     * Write some literal text to the output buffer.
     *
     * @param text the output text
     */
    public void writeText(String text);

    /**
     * Write some non preformated literal text to the output buffer.
     *
     * @param text       the output text
     * @param preEncoded Indicates the text has already been encoded.
     */
    public void writeText(String text, boolean preEncoded);

    /**
     * Set whether or not the element that is writing to the buffer supports
     * mixed content.
     *
     * @param b true if the element supports mixed content, false otherwise
     */
    public void setElementHasMixedContent(boolean b);

    /**
     * Return whether or not the element that is writing to the output buffer
     * supports mixed content.
     *
     * @return true if the element supports mixed content, false otherwise
     */
    public boolean isMixedContentElement();

    /**
     * Set whether or not the element that is writing to the buffer is a block element
     *
     * @param b true if the element is a block element, false otherwise
     */
    public void setElementIsBlock(boolean b);

    /**
     * Return whether or not the element that is writing to the buffer is a block
     * element.
     *
     * @return true if the element is a block element, false otherwise
     */
    public boolean isBlockElement();

    /**
     * Set whether or not the element that is writing to the buffer is pre-formatted
     * in which case no whitespace processing is performed.
     *
     * @param b true if the element is a block element, false otherwise
     */
    public void setElementIsPreFormatted(boolean b);

    /**
     * Return whether or not the element that is writing to the buffer is pre-formatted
     * in which case no whitespace processing is performed.
     *
     * @return true if the element is a block element, false otherwise
     */
    public boolean isPreFormatted();

    /**
     * Transfer the contents from the specified buffer to this one.
     * <p/>
     * <p>After this method has been called the specified buffer is empty as
     * its contents have been transferred to this one.</p>
     *
     * @param buffer The buffer whose contents should be transferred to this
     *               one.
     */
    public void transferContentsFrom(OutputBuffer buffer);

    /**
     * Return the string value of the buffer contents.
     * <p/>
     * <p>The string value is the result of concetenating all the text
     * (as distinct from markup) within the buffer.</p>
     *
     * @return The string value of the contents of the buffer.
     */
    public String stringValue();

    public void handleOpenElementWhitespace();

    public void handleCloseElementWhitespace();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10562/1	ibush	VBM:2005113001 Fix extra whitespace characters appearing

 01-Dec-05	10517/1	ibush	VBM:2005113001 Fix extra whitespace characters appearing

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Apr-04	4013/3	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/
