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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.testtools.mock.libraries.org.osgi.service.http;

import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;

/**
 * Triggers auto generation of classes within <code>org.osgi.service.http</code>
 * and contained packages for which the source is not available.
 *
 * @mock.generate library="true"
 */
public class OSGiHttpServiceLibrary {

    /**
     * @mock.generate interface="true"
     */
    public HttpService httpService;

    /**
     * @mock.generate interface="true"
     */
    public HttpContext httpContext;
}
