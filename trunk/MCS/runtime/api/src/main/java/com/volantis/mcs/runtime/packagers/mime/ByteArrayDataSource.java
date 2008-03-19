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
package com.volantis.mcs.runtime.packagers.mime;

import javax.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Implementation of {@link DataSource} that uses an underlying byte array to
 * store the data.
 */
public class ByteArrayDataSource implements DataSource {
    /**
     * The data to be returned by this DataSource.
     */
    private byte[] data;

    /**
     * The content type for the data.
     */
    private String contentType;

    /**
     * Create a data source providing the specified array of bytes as data of a
     * specified type.
     *
     * @param data The data to make available through this DataSource
     * @param contentType The content type for this data
     */
    public ByteArrayDataSource(byte[] data, String contentType) {
        this.data = data;
        this.contentType = contentType;
    }

    // Javadoc inherited
    public String getContentType() {
        return contentType;
    }

    // Javadoc inherited
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(data);
    }

    // Javadoc inherited
    public String getName() {
        return "byte-array";
    }

    // Javadoc inherited
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("Not supported");
    }
}
