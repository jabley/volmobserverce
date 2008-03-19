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

import com.volantis.synergetics.io.IOUtils;
import org.osgi.framework.Bundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * A resource registration.
 */
public class ResourceRegistration
        extends Registration {

    /**
     * The name of the root directory where the resources can be found.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param bundle         The bundle.
     * @param servletContext The servlet context.
     * @param alias          The alias.
     * @param name           The name of the root directory where the resources
     *                       can be found.
     */
    public ResourceRegistration(
            Bundle bundle, InternalServletContext servletContext, String alias,
            String name) {
        super(bundle, servletContext, alias);
        if (name.equals("/")) {
            name = "";
        }
        this.name = name;

        // No additional initialization is necessary.
        initialised = true;
    }

    // Javadoc inherited.
    protected boolean doRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();
        if (!path.startsWith(alias)) {
            throw new ServletException("Path '" + path +
                    "' does not start with registered alias '" + alias + "'");
        }

        String internal;
        if (alias.equals("/")) {
            internal = name + path;
        } else {
            internal = name + path.substring(alias.length(), path.length());
        }

        URL resource = httpContext.getResource(internal);
        if (resource == null) {
            return false;
        }

        String mimeType = servletContext.getMimeType(internal);
        if (mimeType != null) {
            response.setContentType(mimeType);
        }

        // Copy the file from the input to the output.
        InputStream is = resource.openStream();
        OutputStream os = response.getOutputStream();
        IOUtils.copyAndClose(is, os);

        return true;
    }

    // Javadoc inherited.
    protected void doShutDown() {
    }
}
