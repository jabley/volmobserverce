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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.web;

import java.util.List;
import java.util.ArrayList;

/**
 * A type safe enumeration representing an identifier of http version.
 */
public class HTTPVersion {

    /**
     * HTTP Version 1.0.
     */
    public static final HTTPVersion HTTP_1_0 = new HTTPVersion("1.0");

    /**
     * HTTPVersion 1.1.
     */
    public static final HTTPVersion HTTP_1_1 = new HTTPVersion("1.1");

    private static final String VERSION_PREFIX = "HTTP/";

    /**
     * The name of this instance of HTTPVersion.
     */
    private String name;

    /**
     * A container for all the available HTTPVersions.
     */
    private static List versions;

    static {
        // Initialize versions.
        versions = new ArrayList();
        versions.add(HTTP_1_0);
        versions.add(HTTP_1_1);
    }

    /**
     * The private constructor.
     */
    private HTTPVersion(String name) {
        this.name = name;
    }

    /**
     * Get the name of this version.
     * @return The name of this version e.g. 1.0, 1.1 etc.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name with the "HTTP/" prefix.
     *
     * @return the name with the "HTTP/" prefix
     */
    public String getFullName() {
        return VERSION_PREFIX + name;
    }

    /**
     * Provide the HTTPVersion for a specified version name.
     * @param name The name of the required HTTPVersion. Must not be null.
     * can either be of the form "1.0" or of the form "HTTP/1.0"
     * @return The HTTPVersion with the same name as that provided.
     * @throws IllegalArgumentException if the <code>name</code> paramater is
     * not a recognised HTTP version.
     */
    public static HTTPVersion httpVersion(String name) {
        HTTPVersion version = null;

        int length = 0;
        // if name starts with the HTTP prefix
        if (name.startsWith(VERSION_PREFIX)) {
            length = VERSION_PREFIX.length();
        }

        for (int i = 0; i < versions.size() && version == null; i++) {
            HTTPVersion currentVersion = (HTTPVersion)versions.get(i);
            String currentName = currentVersion.getName();

            if (name.length() == (currentName.length() + length)
                    && name.startsWith(currentName, length)) {
                version = currentVersion;
            }
        }
        if (version == null) {
            throw new IllegalArgumentException("HTTP version " + name +
                                               " is not recognised");
        }

        return version;
    }
}
