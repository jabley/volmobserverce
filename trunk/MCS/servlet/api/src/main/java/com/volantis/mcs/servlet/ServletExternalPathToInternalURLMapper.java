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

package com.volantis.mcs.servlet;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.ExternalPathToInternalURLMapper;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.synergetics.log.LogDispatcher;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URI;
import java.util.Iterator;
import java.util.Set;

/**
 * Maps external paths to internal URLs within a servlet context.
 *
 * <p>On input an external path is relative to the context, e.g. if the context
 * path for the application is <code>/volantis</code> and the client has asked
 * for <code>http://host:8080/volantis/dir/fred.xdime</code> then the path
 * provided on input must be <code>/dir/fred.xdime</code>.</p>
 *
 * <p>On output the external path is also relative to the context, e.g. if in
 * the above example the internal URL was
 * <code>file:/tomcat/webapps/volantis/dir/fred.xdime</code> then that would be
 * mapped back to <code>/dir/fred.xdime</code>.</p>
 *
 * <p>This assumes that the application server consistently maps resource paths
 * to URLs within a webapp, i.e. it uses the same URL scheme for them all and
 * the URLs for two resources have the same relationship as the paths for
 * those resources. e.g. <code>/a/b.xdime</code> and <code>/c.xdime</code>
 * could map through to <code>file:/foo/a/b.xdime</code> and
 * <code>file:/foo/c.xdime</code>.</p>
 *
 * <p>It would be really nice if it was possible to get access to the context
 * path for the servlet from the {@link ServletContext} so that the external
 * path could be created relative to the host, i.e. by resolving it relative
 * to the context and prefixing it with the context. Unfortunately, the only
 * place it is possible to get access to the context path is from a
 * {@link HttpServletRequest} which is not available at the point that we want
 * to use this class. One suggestion was to get the last path component from
 * the URL to the root of the webapp. Unfortunately, that will not work if the
 * webapp is the ROOT, or if the context is encoded in the URL in some other
 * way. As it is we have to just make sure that anywhere that we need a host
 * relative URL in the outputted page that we prefix it with the context while
 * processing the page.</p> 
 */
public class ServletExternalPathToInternalURLMapper
        implements ExternalPathToInternalURLMapper {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    ServletExternalPathToInternalURLMapper.class);

    /**
     * The underlying servlet context.
     */
    private final ServletContext servletContext;

    /**
     * The URL to the root of the servlet context.
     */
    private final URL rootURL;

    /**
     * The string representation of the root URL.
     *
     * <p>This must end with a /</p>
     */
    private final String rootURLAsString;

    /**
     * The position of the path of an internal URL that is relative to the
     * root URL. This is the position of the last / in the root URL so that
     * when an internal URL is mapped to an external path the path will start
     * with a /.
     */
    private final int startIndex;

    public ServletExternalPathToInternalURLMapper(
            ServletContext servletContext) {

        this.servletContext = servletContext;

        // Get the URL for the root of the servlet context as all paths will
        // be calculated relative to that. If it cannot be found then MCS
        // cannot start up.
        try {
            rootURL = getResourceURL("/");
        } catch (IOException e) {
            throw new ExtendedRuntimeException(e);
        }

        rootURLAsString = rootURL.toExternalForm();


        if (!rootURLAsString.endsWith("/")) {
            throw new IllegalStateException("Root URL '" + rootURLAsString +
                    "' does not end with a /");
        }
        startIndex = rootURLAsString.length() - 1;

    }

    /**
     * Get a URL that references the resource within the web application
     * represented by the servlet context.
     *
     * <p>This first looks to see whether the resource can be referenced as a
     * file as that is probably the simplest and most efficient way.</p>
     *
     * <p>If that does not work then it asks the servlet context to look for it
     * as a resource within the servlet. This could return a URL of potentially
     * any form, although likely ones are jndi:, file:, jar:.</p>
     *
     * <p>If that does not work then it assumes that the resource is a
     * directory and tries to find a resource underneath it that can then be
     * resolved.</p>
     *
     * @param resourcePath The path to the resource with the web application.
     * @return The URL to the resource, or null if the resource could not be
     *         found.
     * @throws java.net.MalformedURLException if there was a problem creating the URL.
     */
    private URL getResourceURL(final String resourcePath)
            throws IOException {

        URL url = null;

        // Try and look for the resource as a file as that should be easier
        // to access than other types of resources.
        String filePath = servletContext.getRealPath(resourcePath);
        if (filePath != null) {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new IllegalStateException("File '" +
                        file.getCanonicalPath() + "' does not exist");
            } else if (!file.isDirectory()) {
                throw new IllegalStateException("File '" +
                        file.getCanonicalPath() + "' is not a directory");
            }
            url = new URL(file.toURI().toString());
        }

        // If it could not be resolved as a URL then try it as a resource.
        if (url == null) {
            url = servletContext.getResource(resourcePath);
        }

        if (url == null) {
            // The container either can't locate the resource (e.g. it doesn't
            // actually exist) or doesn't support the Servlet API correctly or
            // the request hit a "welcome list" resource so the request isn't
            // fully qualified.

            // Try to locate a non-directory resource below the request's
            // resource path which can be used as a reasonable resource URL.
            // A "/" must be added to the resource path (if necessary) to
            // force the server to look for "nested" resources. Note that
            // the actual nested non-directory resource isn't important since
            // the system ID is only used to allow relative URLs to be
            // processed correctly.
            Set resourcePathsSet;
            String alternativeResourcePath = resourcePath;

            if (logger.isDebugEnabled()) {
                logger.debug("getResource(" + resourcePath +
                        ") returned null");
            }

            if (!alternativeResourcePath.endsWith("/")) {
                alternativeResourcePath = resourcePath + "/";
            }

            resourcePathsSet =
                    servletContext.getResourcePaths(alternativeResourcePath);

            alternativeResourcePath = null;

            if (resourcePathsSet != null && !resourcePathsSet.isEmpty()) {
                Iterator resourcePaths = resourcePathsSet.iterator();

                while (resourcePaths.hasNext() &&
                        (alternativeResourcePath == null)) {
                    String path = (String) resourcePaths.next();

                    if ((path != null) && !path.endsWith("/")) {
                        alternativeResourcePath = path;
                    }
                }
            }

            if (alternativeResourcePath != null) {
                url = servletContext.getResource(alternativeResourcePath);

                if ((url == null) && logger.isDebugEnabled()) {
                    logger.debug("getResource(" +
                            alternativeResourcePath +
                            ") returned null");
                }
            } else if (logger.isDebugEnabled()) {
                logger.debug("Could not find a non-directory resource " +
                        "path for " + resourcePath);
            }
        }
        URI tmpURI = null;
        URL tmpURL = url;
        try {
            tmpURI = new URI(url.toString());
            tmpURL = tmpURI.normalize().toURL();
        } catch (URISyntaxException use) {
            logger.error("url-normalization-failure", url.toString(), use);
        }
        
        return tmpURL;
    }

    // Javadoc inherited.
    public URL mapExternalPathToInternalURL(String path) {

        try {
            return new URL(rootURL, path);
        } catch (MalformedURLException e) {
            if (logger.isErrorEnabled()) {
                logger.error("unexpected-exception", e);
            }

            throw new IllegalStateException(
                    "Cannot map '" + path + "' to URL: " + e.getMessage());
        }
    }

    // Javadoc inherited.
    public String mapInternalURLToExternalPath(String urlAsString) {
        if (urlAsString.startsWith(rootURLAsString)) {
            return urlAsString.substring(startIndex);
        } else {
            throw new IllegalArgumentException("Cannot map '" + urlAsString +
                    "' to external path as it does not start with '" +
                    rootURLAsString + "'");
        }
    }
}
