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

import javax.servlet.FilterConfig;
import java.net.URL;
import java.util.Iterator;

/**
 * Maps between local paths within MCS and remote sites.
 *
 * <p>The RemappingFilter supports the notion of mounting remote sites at a
 * specific path on the current MCS host. e.g. it can mount the contents of
 * the hierarchy <code>http://host/path/to/site/</code> to a context relative
 * path of <code>xyz</code>. When a client makes a request to MCS for
 * <code>xyz/content.xdime</code> then the RemappingFilter makes a request for
 * <code>http://host/path/to/site/content.xdime</code>.</p>
 *
 * <p>It also supports rewriting URLs within the returned content to make them
 * reenter MCS. e.g. if the returned content contained an absolute URL to say
 * <code>http://host/path/to/site/blah.xdime</code> then it would rewrite that
 * URL to point to <code>/context/xyz/blah.xdime</code>. Any URL to any mounted
 * site will be rewritten irrespective of where the content originates. This
 * means that mounted sites can cross reference each other safely.</p>
 *
 * <p>The RemappingFilter assumes that at any one time it is possible to
 * enumerate all the mappings and that each mapping is represented as a pair
 * consisting of a prefix to the servlet path and a prefix for the remote URL.
 * For each URL it iterates over the mappings and if a URL starts with the
 * remote URL prefix then it is replaced with a host relative path containing
 * the context (if any) followed by the servlet path prefix.
 * e.g. assuming that the RemappingFilter was running in a web app with a
 * context path of <code>context</code> then it would map
 * <code>http://host/path/to/site/content.xdime</code> to
 * <code>/context/xyz/content.xdime</code>. </p>
 *
 * <p>
 * <strong>Warning: This abstract class is not part of the public API and is liable
 * to change as part of future enhancements to the RemappingFilter. The
 * name of this class is wrong as it has actually nothing to do with
 * projects, neither for that matter does the <code>project.properties</code>
 * which the default implementation of this uses. A single remote site that
 * is mounted can support an arbitrary number of projects.</strong>
 * </p>
 *
 * @volantis-api-exclude-from PublicAPI
 * @volantis-api-exclude-from ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class GenericURLRemapper {
       
    /**
     * Can't use System.getProperty("file.separator") because the URL separator
     * is always '/'
     */
    private static final char SEPARATOR = '/';
    
    /**
     * Name of the properties file which contains the project mappings. This
     * should be located in the root directory of the webapp in which it is
     * being run. This location will be passed in in the constructor.
     */
    protected static final String PROPERTIES_FILE = "projects.properties";
    
    /**
     * Initialise the remapper using the data from the filter configuration.
     *
     * @param filterConfig configuration of the filter in which this remapping
     *                     is being performed.
     */
    public abstract void initialise(FilterConfig filterConfig);

    /**
     * Returns an iterator over the path prefixes for all of the remote
     * sites that have been mounted in MCS (if any).
     *
     * @return an iterator over the path prefixes of all remote sites mounted
     *         in MCS.
     */
    public abstract Iterator pathPrefixIterator();

    /**
     * Returns the root URL for the remote site for a given path prefix.
     *
     * @param pathPrefix the path prefix that identifies the mount point.
     * @return the root URL for the remote site, or null if none has been
     *         mounted at that location.
     */
    public abstract URL getRemoteSiteRootURL(String pathPrefix);
    
    /**
     * Returns the name of the remote MCS project being requested.
     * 
     * @param servletPath the current servlet path
     * 
     * @return the name of the remote MCS project being requested.
     */ 
    public String getRemoteProjectName(String servletPath) {

        String remoteProjectName = null;

        // Get a list of all of the registered MCS projects.
        Iterator iterator = pathPrefixIterator();
        boolean identifiedProjectName = false;
        while (iterator.hasNext() && !identifiedProjectName) {
            String currentRemoteProjectName =
                    (String)iterator.next();
            if (servletPath.startsWith("/" + currentRemoteProjectName)) {
                remoteProjectName = currentRemoteProjectName;
                identifiedProjectName = true;
            }
        }
        return remoteProjectName;
    }
    
    public String remapURL(String url, String servletPath) {

        String matchedPathPrefix = null;

        for (Iterator i = pathPrefixIterator(); i.hasNext();) {
            String pathPrefix = (String) i.next();
            if ((servletPath.startsWith(pathPrefix) || 
                    (servletPath.charAt(0) == '/' && servletPath
                    .indexOf(pathPrefix) == 1))
                    && (matchedPathPrefix == null 
                            || pathPrefix.length() > matchedPathPrefix
                            .length())) {
                matchedPathPrefix = pathPrefix;
            }
        }

        final String remappedURL;
        // Check if we can remap the URL.
        if (matchedPathPrefix != null) {
            URL urlPrefix = getRemoteSiteRootURL(matchedPathPrefix);

            if (urlPrefix != null) {

                // Remove the context path from the front of the servlet path.
                if (servletPath.indexOf(SEPARATOR) != -1
                        && servletPath.indexOf(SEPARATOR, servletPath
                                .indexOf(SEPARATOR) + 1) != -1) {
                    servletPath = servletPath.substring(servletPath.indexOf(
                            SEPARATOR, servletPath.indexOf(SEPARATOR) + 1));
                }
                remappedURL = URLNormalizer.combineURLComponents(urlPrefix
                        .toExternalForm(), servletPath);
            } else {
                // If the remote project URL is invalid,
                // then return the unmapped URL
                remappedURL = url;
            }
        } else {
            remappedURL = url;
        }

        return remappedURL;
    }
}
