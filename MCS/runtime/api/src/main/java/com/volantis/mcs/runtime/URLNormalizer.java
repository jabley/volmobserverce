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
package com.volantis.mcs.runtime;

/**
 * This class has been added to reduce the number of places where the same
 * chunks of code perform the same checks on Strings which represent URL
 * components.
 */
public class URLNormalizer {

    private static final String URL_SEPARATOR = "/";

    /**
     * Various classes (e.g. {@link com.volantis.mcs.utilities.MarinerURL})
     * fall down if a relative URL starts with a slash. This method ensures
     * that the resulting String does not start with a slash.
     *
     * @param url to be normalized
     * @return the normalized full URL
     */
    public static String convertHostRelativeToDocumentRelative(String url) {

        if (url.startsWith(URL_SEPARATOR)) {
            return url.substring(1);
        } else {
            return url;
        }
    }

    /**
     * Ensure that it has a trailing {@link #URL_SEPARATOR} unless it is
     * specifying a resource.
     *
     * @param url to be normalized
     * @return the normalized full URL
     */
    public static String normalizeFullURL(String url) {
        if (!url.endsWith(URL_SEPARATOR)) {
            return url + URL_SEPARATOR;
        } else {
            return url;
        }
    }

    /**
     * This directly contradicts the behaviour of {@link #convertHostRelativeToDocumentRelative},
     * so care should be taken to use the correct version. This should be used
     * in the case where a String indicates a URL that is relative to the webapp
     * context, and should therefore start with a forward slash.
     * <p/>
     * This method ensures that the resulting String starts with a slash. This
     * behaviour is required when passing a context relative path to servlet
     * classes.
     *
     * @param url to be normalized
     * @return the normalized context relative URL
     */
    public static String normalizeContextRelativeURL(String url) {

        if (url.startsWith(URL_SEPARATOR)) {
            return url;
        } else {
            return URL_SEPARATOR + url;
        }
    }

    /**
     * Convenience method for combining URL components which ensures that the
     * separator charactor '/' is not duplicated. Does not verify that the
     * resultant string is a valid URL.
     *
     * @param base      the first part of the URL component
     * @param ending    the second part of the URL component
     * @return String which is the base and ending combined. This is not
     * necessarily a valid URL.
     */
    public static String combineURLComponents(String base, String ending) {

        StringBuffer url = new StringBuffer(base);

        // Always add a slash to the end of the base if not present...
        if (!base.endsWith(URL_SEPARATOR)) {
            url.append(URL_SEPARATOR);
        }
        // ...and always remove it from the start of the second part if present.
        int beginIndex = 0;
        if (ending.startsWith(URL_SEPARATOR)) {
            beginIndex = 1;
        }

        return url.append(ending.substring(beginIndex)).toString();
    }
}
