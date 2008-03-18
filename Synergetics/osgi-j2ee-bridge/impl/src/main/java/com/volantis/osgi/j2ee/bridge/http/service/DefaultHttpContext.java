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

package com.volantis.osgi.j2ee.bridge.http.service;

import org.osgi.framework.Bundle;
import org.osgi.service.http.HttpContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

/**
 * Default implementation of {@link HttpContext}.
 *
 * <p>An instance of this is created each time a null HttpContext is passed to
 * an HttpService method.</p>
 */
public class DefaultHttpContext
        implements HttpContext {

    /**
     * The bundle for which this was created.
     */
    private final Bundle bundle;

    /**
     * Initialise.
     *
     * @param bundle The bundle for which this was created.
     */
    public DefaultHttpContext(Bundle bundle) {
        this.bundle = bundle;
    }

    // Javadoc inherited.
    public boolean handleSecurity(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse)
            throws IOException {
        
        return true;
    }

    // Javadoc inherited.
    public URL getResource(String name) {
        return bundle.getResource(name);
    }

    // Javadoc inherited.
    public String getMimeType(String name) {
        return null;
    }
}
