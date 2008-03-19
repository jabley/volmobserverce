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
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.GridRowAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.layouts.GridFormatModule;
import com.volantis.mcs.protocols.renderer.layouts.TableLayers;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.properties.BackgroundColorKeywords;
import com.volantis.mcs.themes.properties.BackgroundImageKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;

public class AlignedGridFormatModule
        implements GridFormatModule {

    private final GridFormatModule delegate;
    private final RowOutputBuffers rowBuffers;
    private final OutputBufferStack outputBufferStack;
    private final int columns;
    private final TableLayers spatialLayers;
    private TableLayers gridLayers;

    public AlignedGridFormatModule(
            GridFormatModule delegate, RowOutputBuffers rowOutputBuffers,
            OutputBufferStack outputBufferStack, int columns,
            TableLayers layers) {
        this.delegate = delegate;
        this.rowBuffers = rowOutputBuffers;
        this.outputBufferStack = outputBufferStack;
        this.columns = columns;
        this.spatialLayers = layers;
    }

//    public void setSpatialCellStyles(Styles spatialCellStyles) {
//        this.spatialCellStyles = spatialCellStyles;
//    }

    public void writeOpenGrid(GridAttributes attributes) {
        // Don't write anything out for the grid.
        //
        // Save the grid's Styles away though so that they can be merged into
        // the cell styles.
        gridLayers = attributes.getLayers();
    }

    public void writeCloseGrid(GridAttributes attributes) {
        // Don't write anything out for the grid.
    }

    public void writeOpenGridRow(GridRowAttributes attributes) {
        // Don't write anything out for the grid row.

        // Push a buffer onto the stack into which the grid will write the
        // contents of this row.
        OutputBuffer rowBuffer = rowBuffers.getBuffer(attributes.getRow());
        outputBufferStack.pushOutputBuffer(rowBuffer);
    }

    public void writeCloseGridRow(GridRowAttributes attributes) {
        // Don't write anything out for the grid row.

        // Pop the buffer into which the grid wrote the contents of this row.
        OutputBuffer rowBuffer = rowBuffers.getBuffer(attributes.getRow());
        outputBufferStack.popOutputBuffer(rowBuffer);
    }

    public void writeOpenGridChild(GridChildAttributes attributes) {

        // high priority.
        // Spatial cells styles.
        // Grid styles.
        // Grid column styles (already handled).
        // Grid row styles.
        StyleValues[] layers = new StyleValues[]{
                gridLayers.getLayer(TableLayers.ROW),
                gridLayers.getLayer(TableLayers.TABLE),
                spatialLayers.getLayer(TableLayers.CELL),
        };

        Styles cellStyles = attributes.getStyles();

        // Styles need to be merged in following order from low priority to
        // high priority.
        // Spatial cells styles.
        // Grid styles.
        // Grid column styles (already handled).
        // Grid row styles.

        // todo add in row group / column group and col styles.
        MutablePropertyValues cellValues = cellStyles.getPropertyValues();
        StyleValue bgColor = cellValues.getStyleValue(
                StylePropertyDetails.BACKGROUND_COLOR);
        if (bgColor == BackgroundColorKeywords.TRANSPARENT) {

            for (int i = 0; i < layers.length; i++) {
                StyleValues layer = layers[i];

                StyleValue layerBgColor = layer.getStyleValue(
                        StylePropertyDetails.BACKGROUND_COLOR);
                StyleValue layerBgImage = layer.getStyleValue(
                        StylePropertyDetails.BACKGROUND_IMAGE);
                if (layerBgImage != BackgroundImageKeywords.NONE) {
                    // Don't set background color if the layer has an image
                    // otherwise it couldn't be seen.
                    break;
                } else
                if (layerBgColor != BackgroundColorKeywords.TRANSPARENT) {
                    cellValues.setComputedValue(
                            StylePropertyDetails.BACKGROUND_COLOR,
                            layerBgColor);
                    break;
                }
            }
        }

        // Make sure that the attributes have the correct number of columns
        // which is the total number that will be written out in each row.
        attributes.setColumns(columns);

        // Write the grid child.
        delegate.writeOpenGridChild(attributes);
    }

    public void writeCloseGridChild(GridChildAttributes attributes) {
        // Write the grid child.
        delegate.writeCloseGridChild(attributes);
    }

}
