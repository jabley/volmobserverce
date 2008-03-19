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

package com.volantis.mcs.runtime.project;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class FileBasedPath
        implements Path {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(FileBasedPath.class);


    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(FileBasedPath.class);

    /**
     * The file.
     */
    private final File file;

    /**
     * Initialise.
     *
     * @param url The string representation of the URL.
     */
    public FileBasedPath(String url) {
        if (!url.startsWith("file:/")) {
            throw new IllegalArgumentException("URL '" + url +
                    "' must start with 'file:/' but does not.");
        }

        try {
            // we need to decode this url in order to create a file
            // representation. For example if the file url contains a directory
            // that has a space it will be encoded.
            String decodedURL = URLDecoder.decode(url, "UTF-8");
            file = new File(decodedURL.substring(5));
        } catch (UnsupportedEncodingException e) {
            logger.error("url-decoding-error", new String[] {url, "UTF-8"}, e);
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "url-decoding-error",
                    new String[] {url, "UTF-8"}));
        }
    }

    private FileBasedPath(File file) {
        this.file = file;
    }

    public Path getParentPath() {
        File parent = file.getParentFile();
        if (parent == null) {
            return null;
        } else {
            return new FileBasedPath(parent);
        }
    }

    public Path getChild(String name) {
        return new FileBasedPath(new File(file, name));
    }

    public InputStream openStream() throws IOException {
        InputStream stream = null;
        if (file.exists() && file.canRead()) {
            stream = new FileInputStream(file);
        }
        return stream;
    }

    public String toExternalForm()
            throws IOException {

        return file.toURI().toString();
    }

    public boolean isRemote() {
        // File based paths are never remote.
        return false;
    }
}
