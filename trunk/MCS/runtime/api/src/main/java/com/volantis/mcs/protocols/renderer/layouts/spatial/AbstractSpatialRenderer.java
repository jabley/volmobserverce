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

package com.volantis.mcs.protocols.renderer.layouts.spatial;

import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.spatial.CoordinateConverter;
import com.volantis.mcs.layouts.spatial.CoordinateConverterChooser;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.shared.layouts.DirectionHelper;
import com.volantis.mcs.protocols.renderer.shared.layouts.IteratedFormatInstanceCounter;
import com.volantis.mcs.runtime.layouts.styling.FormatStylingEngine;
import com.volantis.styling.Styles;

public abstract class AbstractSpatialRenderer
        implements FormatRenderer {

    public void render(
            final FormatRendererContext context,
            final FormatInstance instance)
            throws RendererException {

        if ((instance == null) || instance.isEmpty()) {
            return;
        }

        SpatialFormatIterator spatial = (SpatialFormatIterator)
                instance.getFormat();

        // Start styling
        FormatStylingEngine formatStylingEngine =
                context.getFormatStylingEngine();
        Styles formatStyles = formatStylingEngine.startStyleable(
                spatial, spatial.getStyleClass());

        CoordinateConverter converter = chooseCoordinateConverter(
                context, instance, spatial, formatStyles);

        render(context, instance, formatStyles, converter);

        formatStylingEngine.endStyleable(spatial);
    }

    /**
     * Chooses the CoordinateConverter to use.
     *
     * @param context        The context.
     * @param formatInstance The format instance.
     * @param spatial        The spatial format iterator
     * @return the coordinate converter.
     */
    private CoordinateConverter chooseCoordinateConverter(
            final FormatRendererContext context,
            final FormatInstance formatInstance,
            final SpatialFormatIterator spatial,
            final Styles formatStyles) {

        // Iniitalise the CoordinateConverterChooser so that it can
        // be used to determine the correct CoordinateConverter
        NDimensionalIndex childIndex =
                formatInstance.getIndex().addDimension();
        CoordinateConverterChooser chooser =
                spatial.getCoordinateConverterChooser();
        IteratedFormatInstanceCounter instanceCounter =
                context.getInstanceCounter();

        int cells = instanceCounter.getMaxInstances(spatial, childIndex);

        // Determine the column direction.
        boolean reversed =
                DirectionHelper.isDirectionReversed(spatial, formatStyles);

        return chooser.chooseCoordinateConverter(cells, reversed);
    }

    protected abstract void render(
            FormatRendererContext context, FormatInstance instance,
            Styles formatStyles, CoordinateConverter converter)
            throws RendererException;
}
