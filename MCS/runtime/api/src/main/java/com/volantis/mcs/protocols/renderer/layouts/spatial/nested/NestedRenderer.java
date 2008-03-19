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

package com.volantis.mcs.protocols.renderer.layouts.spatial.nested;

import com.volantis.mcs.layouts.spatial.CoordinateConverter;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.layouts.spatial.AbstractSpatialRenderer;
import com.volantis.styling.Styles;

public class NestedRenderer
        extends AbstractSpatialRenderer {

    protected void render(
            FormatRendererContext context, FormatInstance instance,
            Styles formatStyles, CoordinateConverter converter)
            throws RendererException {

        NestedSpatialFormatIteratorHandler handler =
                new NestedSpatialFormatIteratorHandler(
                        context, instance, formatStyles, converter);
        handler.render();
    }
}
