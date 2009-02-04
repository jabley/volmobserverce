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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A {@link Path} that is manipulated as string and uses URL to retrieve the
 * data.
 *
 * <p>This assumes that the tail end of the URL has a path like structure, i.e.
 * path components separated by /. e.g.</p>
 *
 * <pre>
 * file:/a/b/c/d.mimg
 * http://www.volantis.com/a/b/mimg
 * jndi:/localhost/volantis/welcome/welcome.mlyt
 * jar:file:/a/b/c!/com/volantis
 * </pre>
 *
 * <p>The URL passed in is the URL to a resource (policy, some thing else)
 * within a project. The first thing that is done is to get a URL to the
 * directory containing that resource. That is done by stripping off the last
 * / and everything after it. This will also remove any query parameters
 * and such like.</p>
 */
public class StringURLBasedPath
        implements Path {

    /**
     * The string representation of the URL.
     */
    private final String url;

    /**
     * The index with the string representation of the URL that represents the
     * start of the path.
     */
    private final int pathStart;

    /**
     * Initialise.
     *
     * @param url The string representation of the URL.
     */
    public StringURLBasedPath(String url) {
        this(url, calculatePathStart(url));
    }

    /**
     * Calculate the index of the start of the path in the URL.
     *
     * <p>This supports two styles of URLs, ones with hosts, like http:
     * and ones with nested paths, like jar:. In the former the start of the
     * path is the first / after the host, in the latter it is the first /
     * </p>
     *
     * @param url The string representation of the URL.
     * @return The index of the start of the path in the URL.
     */
    private static int calculatePathStart(String url) {
        int pathStart = 0;
        int hostStartIndex = getFirstEndIndex(url, "://");
        int jarStartIndex = getLastEndIndex(url, "!");
        int index = Math.max(hostStartIndex, jarStartIndex);
        if (index != -1) {
            // The path starts somewhere after the index.
            pathStart = index;

            index = url.indexOf('/', index);
            if (index != -1) {
                // The path starts at the index of the /
                pathStart = index;
            }
        }

        return pathStart;
    }

    /**
     * Get the index of the end of the first occurrence of the substring within
     * the string.
     *
     * @param string    The string to search.
     * @param substring The substring to search for.
     * @return The index, or -1 if the substring could not be found.
     */
    private static int getFirstEndIndex(String string, String substring) {
        int index = string.indexOf(substring);
        if (index == -1) {
            return -1;
        } else {
            return index + substring.length();
        }
    }

    /**
     * Get the index of the end of the last occurrence of the substring within
     * the string.
     *
     * @param string    The string to search.
     * @param substring The substring to search for.
     * @return The index, or -1 if the substring could not be found.
     */
    private static int getLastEndIndex(String string, String substring) {
        int index = string.lastIndexOf(substring);
        if (index == -1) {
            return -1;
        } else {
            return index + substring.length();
        }
    }

    /**
     * Initialise.
     *
     * @param url The string representation of the URL.
     */
    public StringURLBasedPath(String url, int pathStart) {
        this.url = url;
        this.pathStart = pathStart;
    }

    // Javadoc inherited.
    public Path getParentPath() {

        // Get the parent by stripping off the last / and all following
        // characters. This will also strip off any query parameters.
        String parent = stripLastPathComponent(url);
        if (parent == null) {
            return null;
        } else {
            return new StringURLBasedPath(parent, pathStart);
        }
    }

    // Javadoc inherited.
    public Path getChild(String name) {
        // Get the child by appending a separator and the child name to the
        // current string url and then create a new instance from that.
        return new StringURLBasedPath(url + "/" + name, pathStart);
    }

    // Javadoc inherited.
    public InputStream openStream()
            throws IOException {
        InputStream stream;
        try {
            stream = new URL(url).openStream();
        } catch (IOException e) {
            // Could not open URL.
            stream = null;
        }
        return stream;
    }

    // Javadoc inherited.
    public String toExternalForm() {
        return url;
    }

    /**
     * Strip the last path component (and preceding /) from the input URL.
     *
     * @param url The input URL.
     * @return The string url for the parent path, or null if it has reached
     *         the root.
     */
    private String stripLastPathComponent(String url) {
        // Get the index of the last /, if it comes before the start of the
        // path then this has reached the root of the path and therefore
        // must return null to indicate that.
        int index = url.lastIndexOf("/");
        if (index < pathStart) {
            url = null;
        } else {
            url = url.substring(0, index);
        }
        return url;
    }
}
