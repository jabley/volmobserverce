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

import com.volantis.shared.net.impl.url.URLProtocolHandler;
import com.volantis.shared.net.impl.url.DefaultContent;
import com.volantis.shared.net.url.URLConfiguration;
import com.volantis.shared.net.url.URLContent;
import com.volantis.shared.time.Period;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;

/**
 * This class is responsible for providing a {@link URLProtocolHandler}
 * for reading files.
 */
public class FileProtocolHandler implements URLProtocolHandler {

    /**
     * Constant for localhost.
     */
    private static final String LOCAL_HOST = "localhost";

    /**
     * Connect to the given file based URL, retrieve the content and
     * encapsulate it
     * within a {@link URLContent} instance.
     *
     * @param url file based url representing the file to be read.
     * @param timeout the period of time after which reading will stop, if
     * not previously completed (will not be null).
     * @param configuration
     * @return
     * @throws IOException if an error occurs during the read.
     */
    public URLContent connect(URL url,
                              Period timeout,
                              URLConfiguration configuration)
            throws IOException {


        URLContent fileContents = null;


        // If the url is a host based file convert to ftp
        String host = url.getHost().toLowerCase();
        if (LOCAL_HOST.equals(host)) {
            String urlString = url.toString();
            String ftpURLString = urlString.replaceFirst("file", "ftp");
            URL ftpURL = new URL(ftpURLString);
            fileContents = new DefaultContent(ftpURL);
        } else {

            FileReader reader = new FileReader();
            try {
                fileContents = reader.read(new URI(url.toExternalForm()), timeout);
            } catch (URISyntaxException e) {
                //@todo decide whether to rethrow or log an error.
            }
        }
        return fileContents;
    }
}
