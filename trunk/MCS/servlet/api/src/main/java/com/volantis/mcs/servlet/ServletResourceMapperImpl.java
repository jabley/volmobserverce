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
import com.volantis.synergetics.log.LogDispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import java.util.Set;
import java.util.Iterator;

/**
 */
public class ServletResourceMapperImpl
        implements ServletResourceMapper {

    /**
     * Used for logging.
     */
     private static final LogDispatcher logger =
             LocalizationFactory.createLogger(ServletResourceMapperImpl.class);

    private final ServletContext servletContext;

    public ServletResourceMapperImpl(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public String getSystemIDForRequest(HttpServletRequest request)
            throws MalformedURLException {

        // The system ID is a URL of some form that is representative of the
        // current request. Using the external request URL may cause problems
        // (such as servlet re-entrancy problems) on certain containers and
        // therefore should only be used as a last resort.
        //
        // The first way to generate a URL is to resolve the path for the
        // current request relative to the servlet, to obtain this path as a
        // resource URL and use the resource URL's external form as the system
        // ID. However, the container may return a null URL if the resource is
        // not accessible.
        final String servletPath = request.getServletPath();
        final String pathInfo = request.getPathInfo();
        StringBuffer resourcePathBuffer = new StringBuffer(servletPath);
        final String contextRelativePath;

        if (pathInfo != null) {
            resourcePathBuffer.append(pathInfo);
        }

        contextRelativePath = resourcePathBuffer.toString();

        String id = null;

        URL url = getResourceURL(contextRelativePath);
        if (url == null) {
            // This is "our last, best hope"; we're going to have to use the
            // external form of the request's URL. This is dodgy, for the
            // reasons outlined above, so we output a debug warning (this
            // usage is only problematic on certain containers, so we don't
            // want a warning always output, but providing this warning in
            // debug will potentially help track down this sort of issue).
            final String requestURL = request.getRequestURL().toString();

            if (logger.isDebugEnabled()) {
                logger.debug("Attempting to use request URL (" +
                             requestURL + ")");
            }

            // Perform a quick check to see if this looks like a plausible
            // URL or not (to avoid having a MalformedURLException thrown
            // for every request through WebSphere - see the comment within
            // the catch block below for details - as exceptions are expensive
            // in performance terms).
            if (requestURL.indexOf(':') == -1) {
                // The URL looks likely to be malformed (no scheme)
                logger.error("underlying-server-request-url-broken",
                             requestURL);

                url = servletContext.getResource("/");
            } else {
                try {
                    url = new URL(requestURL);

                    if (logger.isDebugEnabled()) {
                        logger.debug("WARNING: using request URL (may cause " +
                                     "issues on some containers)");
                    }
                } catch (MalformedURLException e) {
                    // This should never, ever be thrown (see the Servlet 2.3 API
                    // for {@link HttpServletRequest#getRequestURL()}. Sadly
                    // WebSphere 5.1 (and earlier/later? and possibly other
                    // servers?) is broken and generates duff output from this
                    // method.
                    logger.error("underlying-server-request-url-broken",
                                 requestURL,
                                 e);

                    // Fall back to what the MCSFilter used to do, even though
                    // this is likely not to work anyway (this whole system ID
                    // resolution mechanism was introduced to fix relative URL
                    // handling in XDIME/DCI in the first place)
                    url = servletContext.getResource("/");
                }
            }
        }

        if (url != null) {
            // The system ID is the external form of the URL, which may be some
            // form of internal URL or may be a fully fledged, externally callable
            // URL (it doesn't really matter which as long as the container can
            // cope).
            id = url.toExternalForm();
        } else {
            logger.warn("system-id-cannot-be-determined");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Using system ID " + id +
                         " for resource path " + contextRelativePath);
        }

        return id;
    }

    // Javadoc inherited.
    public String getLocalURL(String contextRelativePath)
            throws MalformedURLException {
        URL url = getResourceURL(contextRelativePath);
        return url == null ? null : url.toExternalForm();
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
     * found.
     * @throws MalformedURLException if there was a problem creating the URL.
     */
    private URL getResourceURL(final String resourcePath)
            throws MalformedURLException {

        URL url = null;

        // Try and look for the resource as a file as that should be easier
        // to access than other types of resources.
        String filePath = servletContext.getRealPath(resourcePath);
        if (filePath != null) {
            File file = new File(filePath);
            url = file.toURL();
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

            resourcePathsSet = servletContext.getResourcePaths(alternativeResourcePath);

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
        return url;
    }
}
