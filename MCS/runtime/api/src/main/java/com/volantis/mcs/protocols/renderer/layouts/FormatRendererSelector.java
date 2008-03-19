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

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * Provides the API used to select a {@link FormatRenderer} appropriate for
 * rendering the instances of the specified {@link Format}. The returned
 * format renderers are assumed to be essentially stateless, at least within
 * the given selector instance's scope.
 *
 * @mock.generate
 */
public interface FormatRendererSelector {
    /**
      * Returns an appropriate FormatRenderer implementation instance.
      *
      * @param format the format for which a renderer is required
      * @return the appropriate renderer implementation instance
      * @throws RendererException if there is a problem selecting an
      *                           appropriate renderer instance
      */
    FormatRenderer selectFormatRenderer(final Format format)
            throws RendererException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/3	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 06-Dec-04	6383/1	philws	VBM:2004120206 Provide base infrastructure for FormatRenderer mechanism

 ===========================================================================
*/
