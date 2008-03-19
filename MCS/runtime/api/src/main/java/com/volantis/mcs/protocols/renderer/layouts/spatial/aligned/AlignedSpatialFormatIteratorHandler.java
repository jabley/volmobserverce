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

package com.volantis.mcs.protocols.renderer.layouts.spatial.aligned;

import com.volantis.mcs.context.OutputBufferStack;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.spatial.CoordinateConverter;
import com.volantis.mcs.protocols.SpatialFormatIteratorAttributes;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.GridInstance;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.layouts.spatial.AbstractSpatialFormatIteratorHandler;
import com.volantis.mcs.protocols.renderer.shared.layouts.GridRenderer;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;

/**
 * This is the specialist format renderer that is used to render spatial
 * iterators with aligned content. These only apply to spatial iterators that
 * contain a Grid format.
 */
public class AlignedSpatialFormatIteratorHandler
        extends AbstractSpatialFormatIteratorHandler {
    private final Grid child;

    private final SpatialFormatIteratorAttributes rowAttributes;

    /**
     * Grid renderer used to render the rows of the child grid.
     */
    private final GridRenderer gridRenderer = new GridRenderer();
    private final AlignedGridFormatModule alignedGridModule;
    private final int totalSubColumnsAcrossAllSpatialColumns;
    private final RowOutputBuffers rowBuffers;
    private final OutputBufferStack bufferStack;
    private Styles rowStyles;

    public AlignedSpatialFormatIteratorHandler(
            final FormatRendererContext context,
            final FormatInstance instance, Styles formatStyles,
            CoordinateConverter converter) {
        super(context, instance, formatStyles, converter,
                new AlignedRequiredSlicesCalculatorImpl(
                        instance, context, converter));

        // Retrieve the (must be) single SpatialIterator's child which must be
        // a grid.
        child = (Grid) getOnlyChildFormat(spatial);

        totalSubColumnsAcrossAllSpatialColumns =
                requiredSlices.getTotalSubColumnsAcrossAllSpatialColumns();
        //        assert totalSubColumnsAcrossAllSpatialColumns > 0;

        spatialAttributes.setColumns(totalSubColumnsAcrossAllSpatialColumns);

        rowAttributes = new SpatialFormatIteratorAttributes();
        rowAttributes.setFormat(spatial);
        rowAttributes.setColumns(totalSubColumnsAcrossAllSpatialColumns);

        bufferStack = context.getOutputBufferStack();

        // Rendering goes into a set of output buffers that
        // collect the cells from all grid instances in the current
        // spatial iteration row. There is one output buffer per
        // row of the nested grid (called a "sub-row"). A buffer
        // will be null if the sub-row is not required or at the
        // beginning of rendering the current spatial iteration
        // row.
        rowBuffers = new RowOutputBuffers(
                requiredSlices.getMaxSubRowsPerSpatialCell(), module);

        alignedGridModule = new AlignedGridFormatModule(
                module, rowBuffers, bufferStack,
                totalSubColumnsAcrossAllSpatialColumns,
                spatialLayers);

    }

    protected void checkValidSpatial(SpatialFormatIterator spatial) {
        if (!spatial.isContentAligned() ||
                (spatial.getNumChildren() != 1) ||
                !(spatial.getChildAt(0) instanceof Grid)) {
            throw new IllegalStateException(
                    "This format renderer should only be used for " +
                            "spatial iterators that are aligning content and " +
                            "where the child is a grid");
        }
    }

    protected void writeChild(NDimensionalIndex childIndex, int row, int column)
            throws RendererException {

        // Handle the spatial cell processing
        GridInstance gridInstance = (GridInstance)
                context.getFormatInstance(
                        child, childIndex);

        gridRenderer.writeGrid(context, gridInstance, alignedGridModule,
                requiredSlices.getSubRows(row),
                requiredSlices.getSubColumns(column),
                totalSubColumnsAcrossAllSpatialColumns);
    }

    protected void openSpatialRow(Styles rowStyles) {
        this.rowStyles = rowStyles;
    }

    protected void closeSpatialRow() {
        // Handle the spatial row end processing
        // If there is any output recorded in the grid (sub-)row
        // buffers then this needs to be integrated into the full
        // output at this point
        for (int i = 0; i < rowBuffers.size(); i++) {
            if (rowBuffers.bufferNotEmpty(i)) {

                // todo create row styles, must have minimum height of
                // todo all contained sub rows.
                Styles pseudoRowStyles = StylingFactory.getDefaultInstance()
                        .createInheritedStyles(formatStyles,
                                DisplayKeywords.TABLE_ROW);
                pseudoRowStyles.getPropertyValues().setComputedValue(
                        StylePropertyDetails.BACKGROUND_COLOR,
                        rowStyles.getPropertyValues().getStyleValue(
                                StylePropertyDetails.BACKGROUND_COLOR));
                rowAttributes.setStyles(pseudoRowStyles);

                // Open a new row, write the sub-row
                // content to it then close that row
                module.writeOpenSpatialFormatIteratorRow(rowAttributes);

                bufferStack.getCurrentOutputBuffer().
                        transferContentsFrom(rowBuffers.getBuffer(i));

                module.writeCloseSpatialFormatIteratorRow(rowAttributes);
            }
        }

        rowBuffers.reset();
    }

    protected void openSpatialCell(Styles cellStyles, int row, int column) {

        spatialLayers.mergeColumnStylesIntoCellStyles(
                cellStyles, row == 0, column == 0);
    }

    protected void closeSpatialCell() {
    }
}
