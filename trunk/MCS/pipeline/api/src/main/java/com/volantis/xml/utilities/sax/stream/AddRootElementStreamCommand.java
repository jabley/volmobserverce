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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 20-May-2003  Sumit       VBM:2003050606 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.utilities.sax.stream;

import com.volantis.xml.utilities.sax.DocumentFragmentStreamConstants;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

/**
 * A command abstraction that handles reading and writing of data from the
 * open and close element arrays and the underlying stream when required.
 */
public abstract class AddRootElementStreamCommand
        implements DocumentFragmentStreamConstants {
    /**
     * Enumeration for current states that this stream reader is in
     */
    static final int DOING_OPEN_ELEMENT = 0;

    static final int DOING_THROUGH_READ = 1;

    static final int DOING_CLOSE_ELEMENT = 2;

    static final int EOF = 4;

    /**
     * The encoding to use when converting between Strings and byte arrays
     */
    private static final String ENCODING = "US-ASCII";

    /**
     * Flag to keep track of the current stream state
     */
    private int currentState = DOING_OPEN_ELEMENT;

    /**
     * Offset within our current buffer
     */
    private int bufferOffset;

    /**
     * The byte represenation form of the opening element.
     */
    byte[] openElement = OPEN_ELEMENT;

    /**
     * The byte represenation of the closing element.
     */
    byte[] closeElement = CLOSE_ELEMENT;

    /**
     * Underlying object that this stream must read from. Subclasses are
     * responsible for casting this to the right object
     */
    protected Object underlyingStream;

    public AddRootElementStreamCommand(Object underlying) {
        underlyingStream = underlying;
    }

    /**
     * Create the command. This constructor takes a underlying stream that
     * contains the fragment to wrap and a Map of prefix->url namespaces. All
     * entries in the map are (String)->(String). If the map is null or empty
     * then no namespaces will be added to the underlying fragment."" prefix is
     * the default namespace to use.
     *
     * @param underlying        the stream to delegate to.
     * @param fragmentNamespace the namespace of the "fragment" element that is
     *                          inserted. This is the prefix not the URI. The
     *                          prefix must exist in the namespaces map
     *                          provided.
     * @param namespaces        the namespaces to apply to the "fragment"
     *                          element that is used to wrap the contents of
     *                          the stream.
     */
    public AddRootElementStreamCommand(Object underlying,
                                       String fragmentNamespace,
                                       Map namespaces) {
        underlyingStream = underlying;
        StringBuffer sb = new StringBuffer();

        try {
            // add the "<" part of the text.
            sb.append("<");
            //add in the namespace if there is one
            if (fragmentNamespace != null && !"".equals(fragmentNamespace)) {
                sb.append(fragmentNamespace);
                sb.append(":");
            }
            // append the "fragment"
            sb.append(new String(OPEN_ELEMENT, 1,
                                 OPEN_ELEMENT.length - 2, ENCODING));

            if (namespaces != null && namespaces.size() > 0) {

                Iterator it = namespaces.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry e = (Map.Entry) it.next();
                    String prefix = (String) e.getKey();
                    sb.append(" ");
                    if ("".equals(prefix)) { // the default namespace
                        sb.append("xmlns=");
                        sb.append("\"");
                        sb.append(e.getValue());
                        sb.append("\"");
                    } else {
                        sb.append("xmlns:");
                        sb.append(prefix);
                        sb.append("=");
                        sb.append("\"");
                        sb.append(e.getValue());
                        sb.append("\"");
                    }
                }
            }

            sb.append(new String(OPEN_ELEMENT, OPEN_ELEMENT.length - 1, 1,
                                 ENCODING));
            openElement = sb.toString().getBytes(ENCODING);

            if (fragmentNamespace != null && !"".equals(fragmentNamespace)) {
                StringBuffer closebuffer = new StringBuffer();
                closebuffer.append("</");
                closebuffer.append(fragmentNamespace);
                closebuffer.append(":");
                closebuffer.append(new String(CLOSE_ELEMENT, 2, CLOSE_ELEMENT.length
                                                                - 2, ENCODING));
                closeElement = closebuffer.toString().getBytes(ENCODING);
            }

        } catch (UnsupportedEncodingException e) {
            // US-ASCII is mandatory on all VM's see ENCODING variable
        }

    }

    /**
     * This method reads from either the open or close element or the
     * underlying stream
     * @return the int read from any of the three sources of data
     * @throws IOException
     */
    int read() throws IOException {
        // Return -1 by default
        int retVal = -1;

        switch (currentState) {
        case DOING_OPEN_ELEMENT:
            if (bufferOffset >= openElement.length) {
                // We have reached the end of the openElement
                // array so start returning underlying stream data
                currentState = DOING_THROUGH_READ;
                bufferOffset = 0;
            } else {
                // continue to return openELement data
                retVal = openElement[bufferOffset++];
                break;
            }
        case DOING_THROUGH_READ:
            retVal = streamRead();
            if (retVal != -1) {
                break;
                // Underlying stream has hit eof so start
                // returning the close element data
            } else {
                currentState = DOING_CLOSE_ELEMENT;
            }
        case DOING_CLOSE_ELEMENT:
            if (bufferOffset >= closeElement.length) {
                // We have completed the close element so now we
                // are done. Return -1.
                currentState = EOF;
            } else {
                // Return close element data;
                retVal = closeElement[bufferOffset++];
            }
        }
        return retVal;
    }

    int available() throws IOException {
        int total = 0;
        // Calculate the number of available bytes depending on
        // where we are in the process of returning data

        switch (currentState) {
        case DOING_OPEN_ELEMENT:
            // Return chars left in openElement + underlying stream's
            // available count + close element
            total = openElement.length - bufferOffset;
            total += closeElement.length;
            total += streamAvailable();
            break;
        case DOING_THROUGH_READ:
            // Return chars left in closeElement + underlying stream's
            // available count
            total = closeElement.length - bufferOffset;
            total += streamAvailable();
            break;
        case DOING_CLOSE_ELEMENT:
            // Return chars left in close element data
            total = closeElement.length - bufferOffset;
        }
        return total;

    }

    int getCurrentState() {
        return currentState;
    }

    /**
     * Extenders must implement this method to read from the underlying stream
     * @return stream.read
     * @throws IOException
     */
    abstract int streamRead() throws IOException;

    /**
     * Extenders must implement this method to return available from the
     * underlying stream
     * @return stream.available
     * @throws IOException
     */
    abstract int streamAvailable() throws IOException;

    /**
     * Extenders must implement this method to reset the underlying stream
     * @throws IOException
     */
    abstract void reset() throws IOException;

    /**
     * Extenders must implement this method to close the underlying stream
     * @throws IOException
     */
    abstract void close() throws IOException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
