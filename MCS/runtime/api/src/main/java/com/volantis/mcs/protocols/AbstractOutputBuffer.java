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
 * $Header: /src/voyager/com/volantis/mcs/protocols/AbstractOutputBuffer.java,v 1.3 2002/03/18 12:41:16 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Oct-01    Paul            VBM:2001111402 - Created
 * 28-Feb-02    Paul            VBM:2002022804 - Removed a lot of the code
 *                              due to changes in OutputBuffer.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 24-Apr-03    Steve           VBM:2003041606 - Created a dummy setTrim()
 *                              method that does nothing until it is overridden
 *                              Created a default isWhitespace method that can
 *                              be overriden from child classes. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

/**
 * This class implements functionality which is common across implementations
 * of OutputBuffer.
 */
public abstract class AbstractOutputBuffer
        implements com.volantis.mcs.protocols.OutputBuffer {

    /**
     * Whether or not the element allows mixed content
     */
    private boolean mixedContentElement = false;

    /**
     * Whether or not this is a block element
     */
    private boolean elementIsBlock = false;

    /**
     * Whether or not the element has pre-formatted whitespace
     */
    private boolean elementIsPreFormatted = false;

    public void initialise() {
    }

    /**
     * Sets whether this buffer should trim leading and trailing whitespace
     * characters. This flag must be set before any content is added to the
     * buffer. It defaults to false.
     */
    public void setTrim(boolean trim) {
    }


    /**
     * Returns true if this output buffer contains only whitespace
     *
     * @return true if the buffer contains only whitespace, false otherwise
     */
    public boolean isWhitespace() {
        return false;
    }

    /**
     * Write some literal text to the output buffer.
     *
     * @param text a character array holding the text to write
     * @param off  the index of the first character in the character array
     * @param len  the number of characters to write
     */
    public abstract void writeText(
            char[] text, int off, int len,
            boolean preEncoded);

    /**
     * Write some literal text to the output buffer.
     *
     * @param text the output text
     */
    public abstract void writeText(
            String text,
            boolean preEncoded);

    /**
     * Set whether or not the element that is writing to the buffer supports
     * mixed content.
     *
     * @param b true if the element supports mixed content, false otherwise
     */
    public void setElementHasMixedContent(boolean b) {
        mixedContentElement = b;
    }

    /**
     * Return whether or not the element that is writing to the output buffer
     * supports mixed content.
     *
     * @return true if the element supports mixed content, false otherwise
     */
    public boolean isMixedContentElement() {
        return mixedContentElement;
    }

    /**
     * Set whether or not the element that is writing to the buffer is a block element
     *
     * @param b true if the element is a block element, false otherwise
     */
    public void setElementIsBlock(boolean b) {
        elementIsBlock = b;
    }

    /**
     * Return whether or not the element that is writing to the buffer is a block
     * element.
     *
     * @return true if the element is a block element, false otherwise
     */
    public boolean isBlockElement() {
        return elementIsBlock;
    }

    /**
     * Set whether or not the element that is writing to the buffer is pre-formatted
     *
     * @param b true if the element is pre-formatted, false otherwise
     */
    public void setElementIsPreFormatted(boolean b) {
        elementIsPreFormatted = b;
    }

    /**
     * Return whether or not the element that is writing to the buffer is pre-formatted
     *
     * @return true if the element is pre-formatted, false otherwise
     */
    public boolean isPreFormatted() {
        return elementIsPreFormatted;
    }

    /**
     * Unsupported operation.
     */
    public void transferContentsFrom(OutputBuffer buffer) {
        throw new UnsupportedOperationException(
                "transferContentsFrom() not supported");
    }

    /**
     * Unsupported operation.
     */
    public String stringValue() {
        throw new UnsupportedOperationException("asString() not supported");
    }


    /**
     * Handle the whitespace state when opening an element
     */
    public abstract void handleOpenElementWhitespace();

    /**
     * Handle the whitespace state when closing an element
     */
    public abstract void handleCloseElementWhitespace();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10562/1	ibush	VBM:2005113001 Fix extra whitespace characters appearing

 01-Dec-05	10517/1	ibush	VBM:2005113001 Fix extra whitespace characters appearing

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Jul-04	4868/1	adrianj	VBM:2004070509 Implemented missing writeText(...) methods in StringOutputBuffer

 29-Apr-04	4013/2	pduffin	VBM:2004042210 Restructure menu item renderers

 ===========================================================================
*/
