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
package com.volantis.shared.net.impl.url.file;

import com.volantis.shared.net.impl.url.AbstractURLContent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * This class is responsible for storing the content read from a
 * file and providing access to an {@link InputStream} for reading
 * this content.
 */
public class FileContent extends AbstractURLContent {

    /**
     * The input stream for the file to be read.
     */
    private InputStream inputStream = null;

    /**
     * The file that was read to obtain the contents stored within this class.
     */
    private final File readFrom;

    /**
     * The last modified time of the file at the creation of this content.
     */
    private final long lastModified;

    /**
     * Initialize the new instance using the given parameters.
     *
     * @param inputStream a file input stream.
     * @param readFrom the file that is to be read.
     */
    public FileContent(final InputStream inputStream, final File readFrom)
            throws MalformedURLException {
        super(readFrom.toURL());
        this.inputStream = inputStream;
        this.readFrom = readFrom;
        lastModified = readFrom.lastModified();
    }

    /**
     * Returns an {@link InputStream} from which the file content
     * stored in the class can be read.
     *
     * @return the contents stored as an {@link InputStream}.
     *
     * @throws IOException
     */
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    // javadoc inherited.
    public String getCharacterEncoding() throws IOException {
        return null;
    }

    // javadoc inherited
    public boolean isFresh() {
        return lastModified == readFrom.lastModified();
    }
}
