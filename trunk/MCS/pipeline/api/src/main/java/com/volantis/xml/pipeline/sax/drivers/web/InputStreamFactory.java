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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.web;

import java.io.InputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * This factory returns an appropiate <code>InputStream</code> based on the
 * content encoding.
 */
public class InputStreamFactory {

    /**
     * Return an alternative InputStream for the given contentEncoding, or the
     * unmodified InputStream passed in.
     *
     * @param contentEncoding
     *               the content-encoding value.
     * @param stream the current InputStream.
     * @return an alternative InputStream for the given contentEncoding, or the
     *         unmodified InputStream passed in.
     * @throws IOException thrown if the a InputStream cannot be created.
     */
    public InputStream getInputStream(InputStream stream,
                                      String contentEncoding)
            throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("Argument not be null: stream");
        }
        InputStream result = stream;
        // For the gzip content-encoding we return an InputStream that is
        // wrapped by GZIPInputSream. This ensures the InputStream is
        // decompressed accordingly.
        if ("gzip".equalsIgnoreCase(contentEncoding)) {
            result = new GZIPInputStream(stream);
        }
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 23-Sep-04	888/1	byron	VBM:2004092003 DSB:HTTPS Requests not working

 ===========================================================================
*/
