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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.net.impl.url;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Default URL content that uses the standard {@link URLConnection} to retrieve
 * the content.
 */
public class DefaultContent
        extends AbstractURLContent {

    /**
     * The connection.
     */
    private final URLConnection connection;

    /**
     * The last modified time at content creation.
     */
    private final long lastModified;

    /**
     * Initialise.
     *
     * @param url     The URL whose content this represents.
     * @throws IOException If there was a problem getting the content.
     */
    public DefaultContent(final URL url)
            throws IOException {
        super(url);

        this.connection = url.openConnection();
        lastModified = connection.getLastModified();
    }

    // Javadoc inherited.
    public InputStream getInputStream() throws IOException {
        return connection.getInputStream();
    }

    // Javadoc inherited.
    public String getCharacterEncoding() {
        return null;
    }

    // Javadoc inherited.
    public boolean isFresh() {
        return lastModified == connection.getLastModified();
    }
}
