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

package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.SimpleHTTPMessageEntities;

/**
 * A factory for creating web driver specific specializations of cookies,
 * parameters and headers.
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public class WebRequestEntityFactory {

    /**
     * The default instance.
     */
    private static final WebRequestEntityFactory DEFAULT_INSTANCE =
            new WebRequestEntityFactory();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static WebRequestEntityFactory getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Create a {@link WebRequestCookie}.
     *
     * @return The newly created {@link WebRequestCookie}.
     */
    public WebRequestCookie createCookie() {
        return new WebRequestCookie();
    }

    /**
     * Create a {@link WebRequestHeader}.
     *
     * @return The newly created {@link WebRequestHeader}.
     */
    public WebRequestHeader createHeader() {
        return new WebRequestHeader();
    }

    /**
     * Create a {@link WebRequestParameter}.
     *
     * @return The newly created {@link WebRequestParameter}.
     */
    public WebRequestParameter createParameter() {
        return new WebRequestParameter();
    }

    /**
     * Create a {@link HTTPMessageEntities}.
     *
     * @return The newly created {@link HTTPMessageEntities}.
     */
    public HTTPMessageEntities createHTTPMessageEntities() {
        return new SimpleHTTPMessageEntities();
    }
}
