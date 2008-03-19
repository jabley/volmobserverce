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

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.spatial.CoordinateConverter;
import com.volantis.mcs.protocols.SpatialFormatIteratorAttributes;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.layouts.spatial.AbstractSpatialFormatIteratorHandler;
import com.volantis.styling.Styles;

/**
 * This is the specialist format renderer that is used to render spatial
 * iterators with aligned content. These only apply to spatial iterators that
 * contain a Grid format.
 */
public class NestedSpatialFormatIteratorHandler
        extends AbstractSpatialFormatIteratorHandler {

    private final Format child;
    private final SpatialFormatIteratorAttributes rowAttributes;
    private final SpatialFormatIteratorAttributes cellAttributes;

    public NestedSpatialFormatIteratorHandler(
            final FormatRendererContext context,
            final FormatInstance instance, Styles formatStyles,
            CoordinateConverter converter) {
        super(context, instance, formatStyles, converter,
                new NestedRequiredSlicesCalculatorImpl(
                        instance, context, converter));

        child = getOnlyChildFormat(spatial);

        int columns = converter.getColumns();

        spatialAttributes.setColumns(columns);

        rowAttributes = new SpatialFormatIteratorAttributes();
        rowAttributes.setFormat(spatial);
        rowAttributes.setColumns(columns);

        cellAttributes = new SpatialFormatIteratorAttributes();
        cellAttributes.setFormat(spatial);
        cellAttributes.setColumns(columns);

    }

    protected void checkValidSpatial(SpatialFormatIterator spatial) {
    }

    protected void writeChild(NDimensionalIndex childIndex, int row, int column)
            throws RendererException {

        // Handle the spatial cell processing
        FormatInstance childInstance =
                context.getFormatInstance(
                        child, childIndex);

        context.renderFormat(childInstance);
    }

    protected void openSpatialRow(Styles rowStyles) {
        rowAttributes.setStyles(rowStyles);
        module.writeOpenSpatialFormatIteratorRow(rowAttributes);
    }

    protected void closeSpatialRow() {
        module.writeCloseSpatialFormatIteratorRow(rowAttributes);
    }

    protected void openSpatialCell(Styles cellStyles, int row, int column) {

        spatialLayers.mergeColumnStylesIntoCellStyles(
                cellStyles, row == 0, column == 0);

        cellAttributes.setStyles(cellStyles);
        module.writeOpenSpatialFormatIteratorChild(cellAttributes);
    }

    protected void closeSpatialCell() {
        module.writeCloseSpatialFormatIteratorChild(cellAttributes);
    }
}
