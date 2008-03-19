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

package com.volantis.resource;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Accesses XML policies by resolving them against the URL for the policy root.
 */
public class XMLPoliciesResourceAccessor
        implements ResourceAccessor {

    /**
     * Used for logging.
     */
     private static final LogDispatcher logger =
             LocalizationFactory.createLogger(XMLPoliciesResourceAccessor.class);

    /**
     * The policy root.
     */
    private final URL root;

    /**
     * Initialise.
     *
     * @param root The policy root.
     */
    public XMLPoliciesResourceAccessor(URL root) {
        this.root = root;
    }

    // Javadoc inherited.
    public InputStream getResourceAsStream(String projectRelativePath) {

        try {
            URL url = new URL(root, projectRelativePath);
            return url.openStream();
        } catch (IOException e) {
            logger.error("Cannot find resource for path " + projectRelativePath, e);
            return null;
        }
    }
}
