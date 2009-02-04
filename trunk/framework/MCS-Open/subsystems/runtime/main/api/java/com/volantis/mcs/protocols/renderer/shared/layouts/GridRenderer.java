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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.layouts.Column;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.Row;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.GridRowAttributes;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.GridFormatModule;
import com.volantis.mcs.protocols.layouts.GridInstance;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.layouts.TableLayers;
import com.volantis.mcs.runtime.layouts.styling.FormatStylingEngine;
import com.volantis.styling.Styles;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;

/**
 * A format renderer that is used to render grids.
 */
public class GridRenderer
        extends AbstractFormatRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SegmentRenderer.class);

    // javadoc inherited
    public void render(
            final FormatRendererContext context,
            final FormatInstance instance)
            throws RendererException {

        if (!instance.isEmpty()) {

            GridInstance gridInstance = (GridInstance) instance;
            boolean[] requiredColumns = gridInstance.getRequiredColumns();
            int requiredColumnCount = 0;
            for (int i = 0; i < requiredColumns.length; i++) {
                boolean requiredColumn = requiredColumns[i];
                requiredColumnCount += requiredColumn ? 1 : 0;
            }
            writeGrid(context, gridInstance,
                    context.getLayoutModule(),
                    gridInstance.getRequiredRows(),
                    requiredColumns,
                    requiredColumnCount);
        }
    }

    public void writeGrid(
            FormatRendererContext context,
            GridInstance gridInstance, GridFormatModule module,
            boolean[] requiredRows, boolean[] requiredColumns, int columns)
            throws RendererException {

        Grid grid = (Grid) gridInstance.getFormat();

        // Initialise the attributes.
        GridAttributes attributes = gridInstance.getAttributes();

        TableLayers layers = new TableLayers();

        // Style the grid, and then style all of its columns.
        FormatStylingEngine formatStylingEngine =
                context.getFormatStylingEngine();
        Styles formatStyles = formatStylingEngine.startStyleable(
                grid, grid.getStyleClass());
        layers.setLayer(TableLayers.TABLE, formatStyles);

        attributes.setStyles(formatStyles);
        attributes.setLayers(layers);

        // Style the columns so that we can pick up the height and any
        // other properties associated with the column's style class
        // if any.

        // Style the group of columns. This is to simply group the grid columns
        // together within a single element to simplify the use of those
        // selectors that rely on the position of an element within its parent,
        // e.g. nth-child().
        Styles columnGroupStyles = formatStylingEngine.startStyleable(
                SyntheticStyleableFormat.GRID_COLUMNS,
                null);
        layers.setLayer(TableLayers.COLUMN_GROUP, columnGroupStyles);

        Styles[] columnStyles = getColumnStyles(grid, formatStylingEngine,
                requiredColumns);

        formatStylingEngine.endStyleable(
                SyntheticStyleableFormat.GRID_COLUMNS);

        if (logger.isDebugEnabled()) {
            logger.debug("Grid.writeOutput()");
        }

        attributes.setColumns(columns);

        // Should the order of the columns be reversed,
        // false = 0...n, true = n...0
        boolean reversed =
                DirectionHelper.isDirectionReversed(grid, formatStyles);

        // Open the grid, even if no rows are written
        attributes.setFormat(grid);
        module.writeOpenGrid(attributes);

        // Style the group of rows. See the comment in getColumnStyles().
        Styles bodyStyles = formatStylingEngine.startStyleable(
                SyntheticStyleableFormat.GRID_BODY, null);
        layers.setLayer(TableLayers.ROW_GROUP, bodyStyles);

        // Write out children
        for (int r = 0; r < grid.getRows(); r++) {

            // If the row is completely empty then ignore it.
            if (requiredRows[r]) {
                writeRow(r, context, reversed, columnStyles, gridInstance,
                        requiredColumns, module, columns, layers);
            } else if (logger.isDebugEnabled()) {
                logger.debug("Output not required for row " + r);
            }

        }

        formatStylingEngine.endStyleable(
                SyntheticStyleableFormat.GRID_BODY);

        module.writeCloseGrid(attributes);
        formatStylingEngine.endStyleable(grid);
    }

    /**
     * Build an array of column styles for later merging with cell styles.
     *
     * @param grid                for which the column styles will be obtained
     * @param formatStylingEngine used to generate the styles.
     * @return array of column styles, will not be null.
     */
    private Styles[] getColumnStyles(
            Grid grid,
            FormatStylingEngine formatStylingEngine,
            boolean[] requiredColumns) {

        int columns = grid.getColumns();

        Styles[] columnStyles = new Styles[columns];
        for (int c = 0; c < columns; c += 1) {
            if (!requiredColumns[c]) {
                continue;
            }

            Column column = grid.getColumn(c);
            columnStyles[c] = formatStylingEngine.startStyleable(
                    column, column.getStyleClass());
            formatStylingEngine.endStyleable(column);
        }

        return columnStyles;
    }

    /**
     * Write out a grid row in a pre-existing grid.
     *
     * @param rowPosition     of the current row (0..n)
     * @param context         the context that provides access to the state data
     *                        required for the rendering
     * @param rightToLeft     the order of the columns, true="right to left"
     * @param columnStyles    styles applicable to the whole column
     * @param gridInstance    being rendered.
     * @param requiredColumns
     * @param module
     * @param columns
     * @param layers
     * @throws IOException       if there was a problem while writing the
     *                           open/close row
     * @throws RendererException if there was a problem while rendering the
     *                           child instance
     */
    private void writeRow(
            int rowPosition, FormatRendererContext context,
            boolean rightToLeft, Styles[] columnStyles,
            GridInstance gridInstance, boolean[] requiredColumns,
            GridFormatModule module, int columns, TableLayers layers)
            throws RendererException {

        Grid grid = (Grid) gridInstance.getFormat();

        Row row = grid.getRow(rowPosition);

        FormatStylingEngine formatStylingEngine =
                context.getFormatStylingEngine();
        Styles rowStyles = formatStylingEngine.startStyleable(
                row, row.getStyleClass());
        layers.setLayer(TableLayers.ROW, rowStyles);

        GridRowAttributes rowAttributes = gridInstance.getRowAttributes();

        rowAttributes.resetAttributes();
        rowAttributes.setStyles(rowStyles);
        rowAttributes.setFormat(grid);
        rowAttributes.setColumns(columns);
        rowAttributes.setRow(rowPosition);
        rowAttributes.setLayers(layers);

        module.writeOpenGridRow(rowAttributes);

        writeSubRow(context, gridInstance, rightToLeft, rowPosition,
                columnStyles,
                requiredColumns, columns, module, layers);

        module.writeCloseGridRow(rowAttributes);

        formatStylingEngine.endStyleable(row);
    }

    /**
     * Write only the row elements, not the start/end row markup.
     *
     * @param context         the context that provides access to the state data
     *                        required for the rendering
     * @param gridInstance    being rendered.
     * @param rightToLeft     true if the row should be rendered right to left
     * @param rowPosition     of the current row (0..n)
     * @param columnStyles    styles applicable to the whole column
     * @param requiredColumns columns that need to be rendered
     * @param columns
     * @param module
     * @param layers
     * @throws IOException       if there was a problem while writing the
     *                           open/close child cell
     * @throws RendererException if there was a problem while rendering the
     *                           child instance
     */
    private void writeSubRow(
            FormatRendererContext context,
            GridInstance gridInstance, boolean rightToLeft, int rowPosition,
            Styles[] columnStyles, boolean[] requiredColumns, int columns,
            GridFormatModule module, TableLayers layers)
            throws RendererException {

        IndexIterator indexIterator;
        int maxGridColumns = gridInstance.getFormat().getColumns();
        if (rightToLeft) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "Rows being laid out in Right-To-Left format");

            }

            indexIterator = new BackwardIterator(maxGridColumns, 0);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "Rows being laid out in Left-To-Right format");

            }
            indexIterator = new ForwardIterator(0, maxGridColumns);
        }

        for (; indexIterator.hasNext();) {
            int c = indexIterator.next();

            if (!requiredColumns[c]) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "Output not required for column " + c);

                }

                continue;
            }

            writeChild(context, gridInstance, columnStyles, c,
                    rowPosition,
                    columns, module, layers);
        }
    }



    /**
     * Write out a single child element to pre-existing grid row.
     *
     * @param context      the context that provides access to the state data
     *                     required for the rendering
     * @param gridInstance being rendered, and which is the parent of this
     *                     child
     * @param columnStyles styles for the columns of this grid
     * @param column       number of child to be written
     * @param row          number of child to be written
     * @param columns
     * @param module
     * @param layers
     * @throws IOException       if there was a problem while writing the
     *                           open/close child to the LayoutModule
     * @throws RendererException if there was a problem while rendering the
     *                           child instance
     */
    private void writeChild(
            FormatRendererContext context,
            GridInstance gridInstance, Styles[] columnStyles, int column,
            int row, int columns, GridFormatModule module,
            TableLayers layers)
            throws RendererException {

        FormatStylingEngine formatStylingEngine =
                context.getFormatStylingEngine();

        layers.setLayer(TableLayers.COLUMN, columnStyles[column]);

        // Get the styles for the child.
        Styles childStyles = formatStylingEngine.startStyleable(
                SyntheticStyleableFormat.GRID_CELL, null);
        layers.setLayer(TableLayers.CELL, childStyles);

        layers.mergeColumnStylesIntoCellStyles(
                childStyles, row == 0, column == 0);

        Grid grid = (Grid) gridInstance.getFormat();

        GridChildAttributes childAttributes =
                gridInstance.getChildAttributes();

        childAttributes.resetAttributes();

        childAttributes.setRow(row);
        childAttributes.setColumn(column);
        childAttributes.setStyles(childStyles);
        childAttributes.setFormat(grid);
        childAttributes.setColumns(columns);
        childAttributes.setLayers(layers);

        // Write out the per-child preamble, even if the child
        // is null because otherwise the grid will be
        // misaligned.

        module.writeOpenGridChild(childAttributes);

        Format child = grid.getChildAt(row, column);

        if (child != null) {
            FormatInstance childInstance =
                    context.getFormatInstance(
                            child, gridInstance.getIndex());

            // Now render the child
            context.renderFormat(childInstance);
        }

        module.writeCloseGridChild(childAttributes);

        formatStylingEngine.endStyleable(
                SyntheticStyleableFormat.GRID_CELL);
    }

    private interface IndexIterator {

        boolean hasNext();

        int next();
    }

    private static class ForwardIterator
            implements IndexIterator {

        private int index;

        private int endExclusive;


        public ForwardIterator(int startInclusive, int endExclusive) {
            this.index = startInclusive;
            this.endExclusive = endExclusive;
        }

        public boolean hasNext() {
            return index < endExclusive;
        }

        public int next() {
            int next = index;
            index += 1;
            return next;
        }
    }

    private static class BackwardIterator
            implements IndexIterator {

        private int index;

        private int endInclusive;


        public BackwardIterator(int endExclusive, int startInclusive) {
            this.index = endExclusive - 1;
            this.endInclusive = startInclusive;
        }

        public boolean hasNext() {
            return index >= endInclusive;
        }

        public int next() {
            int next = index;
            index -= 1;
            return next;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/3	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 29-Nov-05	10465/1	geoff	VBM:2005112205 MCS35: Themes not overridng layout properties as expected at runtime

 04-Nov-05	10046/3	geoff	VBM:2005102408 Post-review modifications

 01-Nov-05	10046/1	geoff	VBM:2005102408 Pane style class renders layout rather than theme bgcolor

 07-Nov-05	10166/1	geoff	VBM:2005102408 Backwards port: Pane style class renders layout rather than theme bgcolor

 04-Nov-05	10046/3	geoff	VBM:2005102408 Post-review modifications

 01-Nov-05	10046/1	geoff	VBM:2005102408 Pane style class renders layout rather than theme bgcolor

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/3	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 06-Jan-05	6391/7	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 06-Jan-05	6391/5	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 21-Dec-04	6402/1	philws	VBM:2004120208 Added aligning spatial format iterator renderer

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/
