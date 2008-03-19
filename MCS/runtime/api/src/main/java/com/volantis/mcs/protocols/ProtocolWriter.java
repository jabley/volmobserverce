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
 * $Header: /src/voyager/com/volantis/mcs/protocols/ProtocolWriter.java,v 1.4 2002/05/23 09:49:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Feb-02    Paul            VBM:2002022804 - Created to support the
 *                              PAPI element getDirectWriter method.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 28-Mar-02    Allan           VBM:2002022007 - Modified write() char[] 
 *                              methods to call new char[] versions of 
 *                              writeDirect().
 * 23-May-02    Paul            VBM:2002042202 - Use the Writer on the
 *                              current output buffer instead of some protocol
 *                              methods as it is more efficient.
 * 01-May-03    Steve           VBM:2003041606 - Derive drom OutputBufferWriter
 *                              so that element capabilities can be passed on.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import java.io.IOException;

/**
 * This class extends Writer in order to provide a mechanism for adding text
 * to the current dom output buffer.
 * <p>
 * This is very inefficient at the moment but it is hidden away so will
 * be easy to fix in future. The most efficient method of encoding is to
 * probably write a method which explicitly tests for the different
 * characters which need to be encoded.
 * </p>
 */
public final class ProtocolWriter
        extends OutputBufferWriter {

    /**
     * The VolantisProtocol object which determines how the characters should be
     * encoded.
     */
    private final VolantisProtocol protocol;

    /**
     * Determines if the writer should write out literally (for preencoded
     * output) or should encode the output.
     */
    private final boolean preEncoded;


    /**
     * Create a new <code>ProtocolWriter</code>.
     *
     * @param protocol The VolantisProtocol object which determines how the
     *                 characters should be encoded.
     */
    public ProtocolWriter(VolantisProtocol protocol) {
        this(protocol, false);
    }

    /**
     * Create a new <code>ProtocolWriter</code>.
     *
     * @param protocol   The VolantisProtocol object which determines how the
     *                   characters should be encoded.
     * @param preEncoded Indicates that this writer should not encode if true.
     */
    public ProtocolWriter(VolantisProtocol protocol, boolean preEncoded) {
        this.protocol = protocol;
        this.preEncoded = preEncoded;
    }


    // Javadoc inherited from super class.
    public void write(char[] cbuf, int off, int len) throws IOException {
        setOutputBuffer(
                protocol.getMarinerPageContext().getCurrentOutputBuffer());
        super.write(cbuf, off, len, preEncoded);
    }

    // Javadoc inherited from super class.
    public void write(String str) throws IOException {
        setOutputBuffer(
                protocol.getMarinerPageContext().getCurrentOutputBuffer());
        super.write(str, preEncoded);
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
