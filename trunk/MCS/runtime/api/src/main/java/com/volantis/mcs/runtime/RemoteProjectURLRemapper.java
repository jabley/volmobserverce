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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import javax.servlet.FilterConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * A default implementation of the GenericURLRemapper abstract class.
 */
public class RemoteProjectURLRemapper
        extends GenericURLRemapper {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(
                    RemoteProjectURLRemapper.class);

    /**
     * Contains the properties which have been loaded in from the specified
     * project properties text file.
     */
    private Map projects;

    // Javadoc inherited.
    public void initialise(FilterConfig filterConfig) {
        try {
            // Give the remapper the location of the project properties file so
            // that it can populate the properties.
            String webappPath = filterConfig.getServletContext().getRealPath("/");
            if (webappPath != null) {
                final File file = new File(webappPath, PROPERTIES_FILE);
                FileInputStream fis = new FileInputStream(file);
                Properties properties = new Properties();
                properties.load(fis);
                projects = Collections.unmodifiableMap(properties);
            } else {
                // No location of the properties file could be found - perhaps
                // we're running as a WAR? Attempt to load as a resource.
                InputStream is = filterConfig.getServletContext().
                        getResourceAsStream("/" + PROPERTIES_FILE);
                if (is != null) {
                    Properties properties = new Properties();
                    properties.load(is);
                    projects = Collections.unmodifiableMap(properties);
                }
            }

            if (projects == null) {
                // Project properties couldn't be loaded - log this as an
                // error but continue anyway.
                LOGGER.error("properties-cannot-load");
            }
        } catch (IOException e) {
            // if there was a problem, just continue with an empty property
            // set, but log the problem
            LOGGER.error("properties-cannot-load");
        }
    }

    // Javadoc inherited.
    public Iterator pathPrefixIterator() {
        return projects.keySet().iterator();
    }

    // Javadoc inherited.
    public URL getRemoteSiteRootURL(String mcsProjectName) {

        URL remoteProjectRootURL = null;
        String locationOfRemoteProject = (String) projects.get(mcsProjectName);
        if (locationOfRemoteProject != null) {
            try {
                remoteProjectRootURL = new URL(locationOfRemoteProject);
            } catch(MalformedURLException e) {
                LOGGER.error("invalid-url");
            }
        }
        return remoteProjectRootURL;
    }
}
