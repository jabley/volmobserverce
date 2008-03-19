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
package com.volantis.mcs.migrate.impl.framework.io;

import com.volantis.synergetics.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An object which accepts a byte array and then fully writes that byte array
 * into an output stream.
 * <p>
 * This is the logical inverse of {@link java.io.ByteArrayOutputStream}.
 */
public class OutputStreamByteArray {

    /**
     * The buffer/stream that we read from.
     */
    private ByteArrayInputStream stream;

    /**
     * Initialise.
     *
     * @param buffer the buffer that we read from.
     */
    public OutputStreamByteArray(byte[] buffer) {

        this.stream = new ByteArrayInputStream(buffer);
    }

    /**
     * Write the contents of the byte array provided during construction to
     * the output stream provided.
     *
     * @param output the output stream to write the byte array contents to.
     * @throws IOException if there was an I/O error during the write.
     */
    public void writeTo(OutputStream output) throws IOException {

        IOUtils.copyAndClose(stream, output);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Jul-05	9033/1	allan	VBM:2005071312 Move IOUtils.java that is in cornerstone into Synergetics

 18-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
