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

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.spatial.CoordinateConverter;
import com.volantis.mcs.layouts.spatial.EndlessStringArray;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.SpatialFormatIteratorAttributes;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.LayoutModule;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.layouts.TableLayers;
import com.volantis.mcs.protocols.renderer.shared.layouts.SyntheticStyleableFormat;
import com.volantis.mcs.runtime.layouts.styling.FormatStylingEngine;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

public abstract class AbstractSpatialFormatIteratorHandler {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory
                    .createLogger(AbstractSpatialFormatIteratorHandler.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    AbstractSpatialFormatIteratorHandler.class);
    protected final FormatRendererContext context;
    private final FormatInstance formatInstance;
    protected final Styles formatStyles;
    private final CoordinateConverter converter;
    protected final SpatialFormatIterator spatial;
    protected final LayoutModule module;
    protected final RequiredSlices requiredSlices;
    protected final SpatialFormatIteratorAttributes spatialAttributes;
    protected final TableLayers spatialLayers;

    protected AbstractSpatialFormatIteratorHandler(
            final FormatRendererContext context,
            final FormatInstance formatInstance, Styles formatStyles,
            CoordinateConverter converter,
            RequiredSlicesCalculator calculator) {
        this.context = context;
        this.formatInstance = formatInstance;
        this.formatStyles = formatStyles;
        this.converter = converter;

        // Get the information about the required slices through the table,
        // where a slice is either a row of column.
        this.requiredSlices = calculator.getRequiredSlices();

        spatial = (SpatialFormatIterator) formatInstance.getFormat();
        checkValidSpatial(spatial);

        spatialAttributes = new SpatialFormatIteratorAttributes();
        spatialAttributes.setStyles(formatStyles);
        spatialAttributes.setFormat(spatial);

        module = context.getLayoutModule();

        spatialLayers = new TableLayers();
        setDisplayStyle(formatStyles, DisplayKeywords.TABLE);
        spatialLayers.setLayer(TableLayers.TABLE, formatStyles);
    }

    private Styles[] getColumnStyles(
            FormatStylingEngine formatStylingEngine, int columns,
            RequiredSlices requiredSlices,
            EndlessStringArray columnStyleClasses) {
        Styles[] columnStyles = new Styles[columns];

        for (int c = 0; c < columns; c += 1) {
            if (!requiredSlices.isSpatialColumnRequired(c)) {
                continue;
            }

            Styles styles = formatStylingEngine.startStyleable(
                    SyntheticStyleableFormat.SPATIAL_COLUMN,
                    columnStyleClasses.get(c));
            setDisplayStyle(styles, DisplayKeywords.TABLE_COLUMN);
            formatStylingEngine.endStyleable(
                    SyntheticStyleableFormat.SPATIAL_COLUMN);

            columnStyles[c] = styles;
        }

        return columnStyles;
    }

    protected Format getOnlyChildFormat(SpatialFormatIterator spatial) {
        int numChildren = spatial.getNumChildren();
        if (numChildren != 1) {
            throw new IllegalStateException(
                    exceptionLocalizer.format(
                            "render-spatial-iterator-multiple-children"));
        }

        return spatial.getChildAt(0);
    }

    protected abstract void checkValidSpatial(SpatialFormatIterator spatial);

    protected abstract void openSpatialRow(Styles rowStyles);

    protected abstract void closeSpatialRow();

    protected abstract void openSpatialCell(
            Styles cellStyles, int row, int column);

    protected abstract void closeSpatialCell();

    protected abstract void writeChild(
            NDimensionalIndex childIndex, int row, int column)
            throws RendererException;

    // javadoc inherited
    public void render()
            throws RendererException {

        // Start styling
        FormatStylingEngine formatStylingEngine =
                context.getFormatStylingEngine();

        // Now render the spatial iterator and its grid children as an
        // integrated grid (and ensure the child children's content is
        // also rendered)

        EndlessStringArray rowStyleClasses = spatial.getRowStyleClasses();
        EndlessStringArray columnStyleClasses = spatial.getColumnStyleClasses();

        NDimensionalIndex childIndex =
                formatInstance.getIndex().addDimension();

        // Retrieve the number of rows and columns.
        int rows = converter.getRows();
        //        assert rows > 0;
        int columns = converter.getColumns();
        //        assert columns > 0;

        // Open the spatial format iterator.
        module.writeOpenSpatialFormatIterator(spatialAttributes);

        // Get the styles for the spatial iterator columns. Do these up front
        // as they will be merged into the styles for the cells later on.

        // Style the group of columns. This simply groups the spatial columns
        // together within a single element to simplify the use of those
        // selectors that rely on the position of an element within its parent,
        // e.g. nth-child().
        Styles groupStyles = formatStylingEngine.startStyleable(
                SyntheticStyleableFormat.SPATIAL_COLUMNS,
                null);
        setDisplayStyle(groupStyles, DisplayKeywords.TABLE_ROW_GROUP);
        spatialLayers.setLayer(TableLayers.COLUMN_GROUP, groupStyles);

        Styles[] columnStyles = getColumnStyles(
                formatStylingEngine, columns, requiredSlices,
                columnStyleClasses);

        formatStylingEngine.endStyleable(
                SyntheticStyleableFormat.SPATIAL_COLUMNS);

        // Style the group of rows. See the comment in getColumnStyles().
        Styles spatialBodyStyles = formatStylingEngine.startStyleable(
                SyntheticStyleableFormat.SPATIAL_BODY,
                null);
        setDisplayStyle(spatialBodyStyles, DisplayKeywords.TABLE_ROW_GROUP);
        spatialLayers.setLayer(TableLayers.ROW_GROUP, spatialBodyStyles);

        for (int row = 0; row < rows; row++) {
            if (!requiredSlices.isSpatialRowRequired(row)) {
                continue;
            }

            Styles rowStyles = formatStylingEngine.startStyleable(
                    SyntheticStyleableFormat.SPATIAL_ROW,
                    rowStyleClasses.get(row));

            // Make sure that the row has the correct display.
            setDisplayStyle(rowStyles, DisplayKeywords.TABLE_ROW);
            spatialLayers.setLayer(TableLayers.ROW, rowStyles);

            openSpatialRow(rowStyles);

            for (int column = 0; column < columns; column++) {
                if (!requiredSlices.isSpatialColumnRequired(column)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("No spatial cell required at [" +
                                column + "," + row + "]");
                    }
                    continue;
                }

                spatialLayers.setLayer(TableLayers.COLUMN,
                        columnStyles[column]);

                Styles cellStyles = formatStylingEngine.startStyleable(
                        SyntheticStyleableFormat.SPATIAL_CELL, null);

                // Make sure that the cell has the correct display.
                setDisplayStyle(cellStyles, DisplayKeywords.TABLE_CELL);

                spatialLayers.setLayer(TableLayers.CELL, cellStyles);

                openSpatialCell(cellStyles, row, column);

                // Map the 2D (row, col) coordinates down onto the
                // 1D sequence of instances.
                int position = converter.getPosition(column, row);

                // Set and store the new child index.
                childIndex = childIndex.setCurrentFormatIndex(position);
                context.setCurrentFormatIndex(childIndex);

                writeChild(childIndex, row, column);

                closeSpatialCell();

                formatStylingEngine.endStyleable(
                        SyntheticStyleableFormat.SPATIAL_CELL);
            }

            closeSpatialRow();

            formatStylingEngine.endStyleable(
                    SyntheticStyleableFormat.SPATIAL_ROW);
        }

        formatStylingEngine.endStyleable(
                SyntheticStyleableFormat.SPATIAL_BODY);

        // Handle the spatial processing postamble
        module.writeCloseSpatialFormatIterator(spatialAttributes);

        // Reset the context's understanding of the current index
        context.setCurrentFormatIndex(
                formatInstance.getIndex());
    }

    private void setDisplayStyle(Styles styles, StyleKeyword display) {
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.DISPLAY,
                display);
    }
}
