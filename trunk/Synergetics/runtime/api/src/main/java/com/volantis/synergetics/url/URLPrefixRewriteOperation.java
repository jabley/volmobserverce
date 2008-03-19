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
package com.volantis.synergetics.url;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This is a typesafe enumerator that provides types of URL prefix rewrite
 * operation and the rewrite operations themselves which are dependent on the
 * type of operation.
 */
public abstract class URLPrefixRewriteOperation {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(URLPrefixRewriteOperation.class);

    /**
     * A URLPrefixRewriteOperation that will add a prefix to a url. The
     * operation will make newURL the prefix of url and handle the separator
     * character as necessary ("/").
     */
    public static final URLPrefixRewriteOperation ADD_PREFIX =
        new URLPrefixRewriteOperation() {
            public String performOperationImpl(String currentURL,
                                               String newURL,
                                               String contextURL,
                                               String url) {
                if (newURL == null) {
                    throw new IllegalArgumentException("Cannot be null:" +
                                                       " newURL");
                }

                boolean separatorRequired = !newURL.endsWith("/") &&
                    !url.startsWith("/");

                int bufferLength = newURL.length() + url.length();
                if (separatorRequired) {
                    bufferLength++;
                }

                StringBuffer result = new StringBuffer(bufferLength);
                result.append(newURL);
                if (separatorRequired) {
                    result.append("/");
                }
                result.append(url);

                return result.toString();
            }

                public String toString() {
                    return "[" + getClass().getName() + ".ADD_PREFIX]";
                }
        };

    /**
     * A URLPrefixRewriteOperation that will remove a prefix from a url. The
     * operation will remove the currentURL from the url and return the result
     * if and only if the contextURL matches the currentURL and the url starts
     * with the currentURL. Otherwise the value of url is returned.
     */
    public static final URLPrefixRewriteOperation REMOVE_PREFIX =
        new URLPrefixRewriteOperation() {
            public String performOperationImpl(String currentURL,
                                               String newURL,
                                               String contextURL,
                                               String url) {
                if (currentURL == null) {
                    throw new IllegalArgumentException("Cannot be null" +
                                                       " currentURL");
                }

                String result = url;
                // if context URL is null then we must return the
                // original URL.
                if (contextURL != null) {
                    // ensure that if the currentURL ends in a separator then
                    // the contextURL ends with a separator and vice versa.
                    if (currentURL.endsWith("/") &&
                        !contextURL.endsWith("/")) {
                        // add a seperator to the contextURL
                        contextURL = contextURL + "/";
                    } else if (!currentURL.endsWith("/") &&
                        contextURL.endsWith("/")) {
                        // remove the separator from the contextURL
                        contextURL = contextURL.substring(0,
                                                          contextURL.length() -
                                                          1);
                    }

                    if (url.startsWith(currentURL) &&
                        currentURL.equals(contextURL)) {
                        // the result is the url minus the current url.
                        result = url.substring(currentURL.length());
                    }
                }
                return result;
            }

                public String toString() {
                    return "[" + getClass().getName() + ".REMOVE_PREFIX]";
                }

        };

    /**
     * A URLPrefixRewriteOperation that will replace a prefix on a url with
     * another. The operation will replace the currentURL part of url where
     * currentURL is the prefix of url with newURL. If currentURL is not the
     * prefix of url then value of url is returned. The separator character
     * ("/") is handled as necessary.
     */
    public static final URLPrefixRewriteOperation REPLACE_PREFIX =
        new URLPrefixRewriteOperation() {
            public String performOperationImpl(String currentURL,
                                               String newURL,
                                               String contextURL,
                                               String url) {
                if (currentURL == null) {
                    throw new IllegalArgumentException("Cannot be null" +
                                                       " currentURL");
                }
                if (newURL == null) {
                    throw new IllegalArgumentException("Cannot be null:" +
                                                       " newURL");
                }

                String result = url;
                if (url.startsWith(currentURL)) {
                    StringBuffer buffer = null;
                    boolean separatorRequired = currentURL.endsWith("/") &&
                        !newURL.endsWith("/");

                    final int currentURLLength = currentURL.length();
                    int bufferLength = url.length() - currentURLLength +
                        newURL.length();
                    if (separatorRequired) {
                        bufferLength++;
                    }

                    buffer = new StringBuffer(bufferLength);
                    buffer.append(newURL);
                    if (separatorRequired) {
                        buffer.append("/");
                    }
                    // If the buffer ends with a / and the url starting
                    // position starts with a /, then don't include the
                    // / in the substring operation therefore preventing
                    // two / characters appearing in the url.
                    if (buffer.charAt(buffer.length() - 1) == '/' &&
                        (currentURLLength < url.length()) &&
                        url.charAt(currentURLLength) == '/') {
                        buffer.append(url.substring(currentURLLength + 1));
                    } else {
                        buffer.append(url.substring(currentURLLength));
                    }

                    result = buffer.toString();
                }

                return result;
            }

                public String toString() {
                    return "[" + getClass().getName() + ".REPLACE_PREFIX]";
                }

        };

    /**
     * Private accessor to prevent instantiation outside of this class.
     */
    private URLPrefixRewriteOperation() {
    }

    /**
     * Perform the URL prefix rewrite operation for this URLPrefixOperation.
     *
     * @param currentURL the current url that if not null is expected to be the
     *                   prefix on url
     * @param newURL     the new url that if not null is expected to become the
     *                   prefix that on url
     * @param contextURL the URL that constitutes that context of this
     *                   operation.
     * @param url        the url that whose prefix to rewrite. Cannot be null.
     * @return the rewritten url
     *
     * @throws IllegalArgumentException if url is null.
     */
    public String performOperation(String currentURL,
                                   String newURL,
                                   String contextURL,
                                   String url) {
        return optimizeURL(performOperationImpl(currentURL, newURL,
                                                contextURL,
                                                optimizeURL(url)));
    }

    /**
     * Have sub-classes do their own rewrite operation.
     *
     * @param currentURL the current url that if not null is expected to be the
     *                   prefix on url
     * @param newURL     the new url that if not null is expected to become the
     *                   prefix that on url
     * @param contextURL the URL that constitutes that context of this
     *                   operation.
     * @param url        the url that whose prefix to rewrite. Cannot be null.
     * @return the rewritten url
     */
    protected abstract String performOperationImpl(String currentURL,
                                                   String newURL,
                                                   String contextURL,
                                                   String url);

    /**
     * Optimise ".." parts of a url path. If it turns out that the ".."s in the
     * path produce an invalid url then the url the returned value will be
     * unchanged from the url passed in. "." characters in the path are also
     * optimized out.
     *
     * @param url the url whose ".." parts to optimize
     * @return the url with no ".." in the path unless the ".."s in the path
     *         made the url invalid in which case the original is returned.
     */
    private String optimizeURL(String url) {
        String result = url;

        // Using a URLIntrospector might be expensive so don't unless we
        // have some optimizing to do.
        int pathStart = url.indexOf('/');
        if (pathStart != -1 && url.indexOf('.', pathStart) != -1) {
            try {
                result = new URLIntrospector(url).getExternalForm();
            } catch (IllegalStateException e) {
                // URLIntrospector will throw an IllegalStateException if
                // ".." characters in the path make the url illegal. We
                // do not want to break the caller when this happens so
                // we catch the problem here, generate a log message and
                // pass through the invalid url unoptimized.
                logger.warn("unexpected-illegal-state-exception", e);
            }
        }

        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Feb-05	406/1	byron	VBM:2005021002 Remove extraneous / from url rewriting

 16-Feb-05	404/1	byron	VBM:2005021002 Remove extraneous / from url rewriting

 29-Nov-04	343/1	doug	VBM:2004111702 Refactored Logging framework

 01-Sep-04	254/2	doug	VBM:2004052801 Fixed problem with REMOVE_PREFIX URLPrefixRewriteOperation

 26-May-04	246/3	allan	VBM:2004052102 Optimize url before rewriting.

 26-May-04	246/1	allan	VBM:2004052102 Optimize url before rewriting.

 25-May-04	227/3	allan	VBM:2004052103 Provide a general url prefix rewriting facility

 ===========================================================================
*/
