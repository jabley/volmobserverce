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

package com.volantis.mcs.protocols.renderer.layouts;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.properties.BackgroundColorKeywords;
import com.volantis.mcs.themes.properties.BackgroundImageKeywords;
import com.volantis.mcs.themes.properties.WidthKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Encapsulates the different layers used in resolving styles for table like
 * structures.
 *
 * <p>This includes the following layers.</p>
 *
 * <ol>
 * <li>Spatial Iterator</li>
 * <li>Spatial Iterator Column Group</li>
 * <li>Spatial Iterator Column</li>
 * <li>Spatial Iterator Row Group</li>
 * <li>Spatial Iterator Row</li>
 * <li>Spatial Iterator Cell</li>
 * </ol>
 */
public class TableLayers {

    private static final StyleProperty BACKGROUND_COLOR =
            StylePropertyDetails.BACKGROUND_COLOR;

    public static final int TABLE = 0;
    public static final int COLUMN_GROUP = 1;
    public static final int COLUMN = 2;
    public static final int ROW_GROUP = 3;
    public static final int ROW = 4;
    public static final int CELL = 5;

    private static final int MAX_LAYERS = CELL + 1;

    private final StyleValues[] layers;

    public TableLayers() {
        layers = new StyleValues[MAX_LAYERS];
    }

    public void setLayer(int layer, StyleValues values) {
        layers[layer] = values;
    }

    public StyleValues getLayer(int layer) {
        return layers[layer];
    }

    public void setLayer(int layer, Styles styles) {
        setLayer(layer,  styles.getPropertyValues());
    }

    public void mergeColumnStylesIntoCellStyles(
            Styles cellStyles, boolean firstRow, boolean firstColumn) {

        MutablePropertyValues cellValues = cellStyles.getPropertyValues();



        // If the cell is transparent then it will show through the row
        // background, or if that is also transparent the cell background. If
        // the row has a background image set then setting the cell's colour
        // to the column's color would obscure the row image. So in that case
        // don't set the cell background.
        //
        // todo Support columns in output markup so that these styles can be
        // todo added to the element. This code would then become protocol
        // todo dependent and would have to be moved into the protocols.
        if (layerIsTransparent(CELL)) {
            if (layerIsTransparent(ROW) && layerIsTransparent(ROW_GROUP)) {

                if (layerIsTransparent(COLUMN)) {
                    setBackgroundFromLayer(cellValues, COLUMN_GROUP);
                } else {
                    setBackgroundFromLayer(cellValues, COLUMN);
                }
            }
        }

        // Only set the width of the cell on the first row.
        if (firstRow) {

            // The column width sets the absolute minimum width for all cells
            // in that column. The actual minimum width for the column is
            // approximately the largest of the minimum widths of all the cells
            // in that column. So we only need to set the column width on the
            // cell if the cell is auto, or the column's width is larger than
            // the cell.

            StyleValue cellWidth = cellValues.getStyleValue(
                    StylePropertyDetails.WIDTH);
            if (cellWidth == WidthKeywords.AUTO) {

                StyleValues columnValues = getLayer(COLUMN);
                StyleValue columnWidth = columnValues.getStyleValue(
                        StylePropertyDetails.WIDTH);

                // The minimum width of the cell will be the largest of the
                // specified width and the MCW (the minimum content width
                // determined by the content). So setting the width of the cell to
                // the width of the column will have the same effect as it would
                // have on the column itself.
                cellValues.setComputedValue(StylePropertyDetails.WIDTH,
                        columnWidth);
            } else {
                // todo compare sizes, not sure what to do if one is length
                // todo and other is percentage.
            }
        }

        // If the visibility of the column is collapse then none of the cells
        // within that column are visible and no space is taken up by that
        // column. Unfortunately, that is not the same as setting visibility
        // on a cell which will hide it but leave it using the space so until
        // we support columns in the output markup we cannot support this.
    }

    private void setBackgroundFromLayer(
            MutablePropertyValues values, int layer) {
        StyleValues fromValues = getLayer(layer);
        StyleValue bgColor = fromValues.getStyleValue(BACKGROUND_COLOR);
        if (bgColor != BackgroundColorKeywords.TRANSPARENT) {
            values.setComputedValue(BACKGROUND_COLOR, bgColor);
        }
    }

    private boolean layerIsTransparent(int layer) {
        StyleValues values = getLayer(layer);
        StyleValue bgColor = values.getStyleValue(BACKGROUND_COLOR);
        StyleValue bgImage = values.getStyleValue(
                StylePropertyDetails.BACKGROUND_IMAGE);
        return (bgColor == BackgroundColorKeywords.TRANSPARENT
                && bgImage == BackgroundImageKeywords.NONE);
    }


}
