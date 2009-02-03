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

import org.osgi.service.http.HttpContext;

import javax.servlet.ServletContext;

/**
 * An internal interface implemented by the {@link ServletContext}
 * implementation created by this service.
 *
 * todo clean up unused servlet contexts properly. It has a use count to keep
 * track of the number of references to it and when it is finished with then
 * it should be removed.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface InternalServletContext
        extends ServletContext {

    /**
     * Increment the use count for the servlet context.
     */
    void incrementUseCount();

    /**
     * Decrement the use count on the servlet context.
     */
    boolean decrementUseCount();

    /**
     * Get the underlying HttpContext.
     *
     * @return The HttpContext.
     */
    HttpContext getHttpContext();
}
