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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.renderer.layouts;

import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * Provides the API for performing rendering of specific
 * {@link com.volantis.mcs.layouts.Format Format} instances. Implementations of
 * this interface are expected to be stateless, at least within the scope of
 * a given selector.
 *
 * @mock.generate
 */
public interface FormatRenderer {
    /**
     * The specified format instance and any required nested format instances
     * are rendered to the page output by this method.
     *
     * @param context the context that provides access to the state data
     *                required for the rendering
     * @param instance  the format instance for which rendering is required
     * @throws RendererException if there is a problem rendering the format
     *                           instance
     */
    void render(final FormatRendererContext context, final FormatInstance instance)
            throws RendererException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Dec-04	6383/1	philws	VBM:2004120206 Provide base infrastructure for FormatRenderer mechanism

 ===========================================================================
*/
