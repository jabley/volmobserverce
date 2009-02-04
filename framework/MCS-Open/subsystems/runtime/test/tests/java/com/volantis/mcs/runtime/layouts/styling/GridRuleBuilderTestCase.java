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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.mcs.accessors.LayoutBuilder;
import com.volantis.mcs.layouts.Column;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.Row;
import com.volantis.mcs.layouts.RuntimeLayoutFactory;
import com.volantis.mcs.layouts.StyleableFormat;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.mcs.protocols.renderer.shared.layouts.SyntheticStyleableFormat;
import com.volantis.styling.Styles;

public class GridRuleBuilderTestCase
    extends FormatRuleBuilderTestAbstract {

    public void testStyling() throws Exception {

        LayoutBuilder builder = new LayoutBuilder(new RuntimeLayoutFactory());

        // Create the layout.
        builder.createLayout(LayoutType.CANVAS);

        // Push grid
        builder.pushFormat(FormatType.GRID.getTypeName(), 0);
        builder.setAttribute(Grid.COLUMNS_ATTRIBUTE, "3");
        builder.setAttribute(Grid.ROWS_ATTRIBUTE, "2");
        builder.setAttribute(FormatConstants.BACKGROUND_COLOUR_ATTRIBUTE, "red");
        builder.setAttribute(FormatConstants.BORDER_WIDTH_ATTRIBUTE, "2");
        builder.setAttribute(FormatConstants.CELL_PADDING_ATTRIBUTE, "4");
        builder.setAttribute(FormatConstants.CELL_SPACING_ATTRIBUTE, "6");
        builder.setAttribute(FormatConstants.WIDTH_ATTRIBUTE, "100");
        builder.setAttribute(FormatConstants.WIDTH_UNITS_ATTRIBUTE,
                             FormatConstants.WIDTH_UNITS_VALUE_PERCENT);
        builder.setAttribute(FormatConstants.HEIGHT_ATTRIBUTE, "50");
        builder.setAttribute(FormatConstants.HORIZONTAL_ALIGNMENT_ATTRIBUTE,
                             FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_RIGHT);
        builder.setAttribute(FormatConstants.VERTICAL_ALIGNMENT_ATTRIBUTE,
                             FormatConstants.VERTICAL_ALIGNMENT_VALUE_CENTER);
        builder.attributesRead();

        // Row 0
        builder.createSubComponent(Grid.ROW_FORMAT_TYPE, 0);
        builder.setAttribute(FormatConstants.HEIGHT_ATTRIBUTE, "10");
        builder.attributesRead();

        // Row 1
        builder.createSubComponent(Grid.ROW_FORMAT_TYPE, 1);
        builder.setAttribute(FormatConstants.HEIGHT_ATTRIBUTE, "20");
        builder.setAttribute(FormatConstants.HEIGHT_UNITS_ATTRIBUTE,
                             FormatConstants.HEIGHT_UNITS_VALUE_PERCENT);
        builder.attributesRead();

        // Column 0
        builder.createSubComponent(Grid.COLUMN_FORMAT_TYPE, 0);
        builder.setAttribute(FormatConstants.WIDTH_ATTRIBUTE, "15");
        builder.attributesRead();

        // Column 1
        builder.createSubComponent(Grid.COLUMN_FORMAT_TYPE, 1);
        builder.setAttribute(FormatConstants.WIDTH_ATTRIBUTE, "25");
        builder.setAttribute(FormatConstants.WIDTH_UNITS_ATTRIBUTE,
                             FormatConstants.WIDTH_UNITS_VALUE_PIXELS);
        builder.attributesRead();

        // Push panes.
        String[] horizontalAlign = new String[]{
            FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_LEFT,
            FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_CENTER,
            FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_RIGHT,
            FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_LEFT,
            FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_CENTER,
            FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_RIGHT,
        };
        String[] verticalAlign = new String[] {
            FormatConstants.VERTICAL_ALIGNMENT_VALUE_TOP,
            FormatConstants.VERTICAL_ALIGNMENT_VALUE_CENTER,
            FormatConstants.VERTICAL_ALIGNMENT_VALUE_BOTTOM,
            FormatConstants.VERTICAL_ALIGNMENT_VALUE_TOP,
            FormatConstants.VERTICAL_ALIGNMENT_VALUE_CENTER,
            FormatConstants.VERTICAL_ALIGNMENT_VALUE_BOTTOM,
        };
        for (int i = 0; i < 6; i += 1) {

            // Make an empty cell.
            if (i == 4) continue;

            builder.pushFormat(FormatType.PANE.getTypeName(), i);
            builder.setAttribute(FormatConstants.NAME_ATTRIBUTE, "Pane" + i);
            builder.setAttribute(FormatConstants.HORIZONTAL_ALIGNMENT_ATTRIBUTE,
                                 horizontalAlign[i]);
            builder.setAttribute(FormatConstants.VERTICAL_ALIGNMENT_ATTRIBUTE,
                                 verticalAlign[i]);
            builder.attributesRead();
             builder.popFormat();
        }

        // Pop grid
        Grid grid = (Grid) builder.popFormat();

        Layout layout = builder.getLayout();
        FormatStylingEngine formatStylingEngine =
                createFormatStylingEngine(layout);

        Styles actualStyles;

        actualStyles = formatStylingEngine.startStyleable(grid, null);

        assertEquals("Grid Styles",
                     "background-color: red; " +
                     "background-image: none; " +
                     "border: solid 2px; " +
                     "border-spacing: 6px 6px; " +
                     "height: 50px; " +
                     "padding: 0; " +
                     "vertical-align: middle; " +
                     "text-align: right; " +
                     "width: 100%; ",
                     actualStyles);

        String[] expectedColumnStyles = new String[]{
            "width: 15%",
            "width: 25px",
            "width: auto"
        };
        int columns = grid.getColumns();
        for (int c = 0; c < columns; c += 1) {
            Column column = grid.getColumn(c);

            actualStyles = formatStylingEngine.startStyleable(column, null);
            assertEquals("Column " + c + " Styles",
                         expectedColumnStyles[c],
                         actualStyles,
                         COLUMN_STYLING_PROPERTIES);
            formatStylingEngine.endStyleable(column);
        }

        String[] expectedRowStyles = new String[] {
            "height: 10px; " +
                "text-align: right; " +
                "vertical-align: baseline",

            "height: 20%; " +
                "text-align: right; " +
                "vertical-align: baseline"
        };

        String[][] expectedCellStyles = new String[][] {
            // Row 0
            new String[] {
                "text-align: left; vertical-align: top; padding: 4px",
                "text-align: center; vertical-align: middle; padding: 4px",
                "text-align: right; vertical-align: bottom; padding: 4px",
            },
            // Row 1
            new String[] {
                "text-align: left; vertical-align: top; padding: 4px",
                // This is inherited from the grid.
                "text-align: right; vertical-align: baseline; padding: 4px",
                "text-align: right; vertical-align: bottom; padding: 4px",
            }
        };

        int rows = grid.getRows();
        for (int r = 0; r < rows; r += 1) {
            Row row = grid.getRow(r);

            actualStyles = formatStylingEngine.startStyleable(row, null);
            assertEquals("Row " + r + " Styles",
                         expectedRowStyles[r],
                         actualStyles,
                         ROW_STYLING_PROPERTIES);

            for (int c = 0; c < columns; c += 1) {
                actualStyles = formatStylingEngine.startStyleable(
                        SyntheticStyleableFormat.GRID_CELL, null);

                assertEquals("Cell - Row " + r + " Column " + c + " Styles",
                             expectedCellStyles[r][c],
                             actualStyles,
                             CELL_STYLING_PROPERTIES);

                formatStylingEngine.endStyleable(
                        SyntheticStyleableFormat.GRID_CELL);
            }

            formatStylingEngine.endStyleable(row);
        }

        formatStylingEngine.endStyleable(grid);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10567/1	emma	VBM:2005112901 Forward port of bug fix: problems targetting styles by setting a class on the parent

 02-Dec-05	10544/1	emma	VBM:2005112901 Bug fix: problems targetting styles by setting a class on the parent

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/1	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 04-Nov-05	10046/3	geoff	VBM:2005102408 Post-review modifications

 01-Nov-05	10046/1	geoff	VBM:2005102408 Pane style class renders layout rather than theme bgcolor

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
