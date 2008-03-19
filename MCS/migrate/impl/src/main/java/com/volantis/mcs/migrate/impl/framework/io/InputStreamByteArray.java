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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An object which fully reads an input stream and allows access to it's
 * content as a byte array.
 * <p>
 * This is the logical inverse of {@link java.io.ByteArrayInputStream}.
 */
public class InputStreamByteArray {

    /**
     * The buffer we read into.
     */
    private byte[] buffer;

    /**
     * Initialise.
     *
     * @param input the input stream we fully read.
     * @throws IOException if there was an I/O error during the read.
     */
    public InputStreamByteArray(InputStream input) throws IOException {

        // Read the whole input into the buffer up front.
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        IOUtils.copyAndClose(input, baos);
        buffer = baos.toByteArray();
    }

    /**
     * Return the byte array that we read the input stream content into.
     *
     * @return the byte array containing the input stream content.
     */
    public byte[] getByteArray() {

        return buffer;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Jul-05	9033/1	allan	VBM:2005071312 Move IOUtils.java that is in cornerstone into Synergetics

 18-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
