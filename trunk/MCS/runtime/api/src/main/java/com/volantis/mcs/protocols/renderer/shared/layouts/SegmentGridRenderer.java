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
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.Row;
import com.volantis.mcs.layouts.SegmentGrid;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.SegmentGridAttributes;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.LayoutModule;
import com.volantis.mcs.protocols.layouts.SegmentGridInstance;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;

/**
 * A format renderer that is used to render segment grids.
 */
public class SegmentGridRenderer
        extends AbstractFormatRenderer {
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SegmentGridRenderer.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                        SegmentGridRenderer.class);

    // javadoc inherited
    public void render(final FormatRendererContext context, final FormatInstance instance)
            throws RendererException {
        try {
            if (!instance.isEmpty()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("SegmentGridRenderer.render");
                }

                SegmentGrid grid = (SegmentGrid)instance.getFormat();

                SegmentGridInstance segmentGridContext =
                        (SegmentGridInstance) instance;

                // Initialise the attributes.
                SegmentGridAttributes attributes =
                        segmentGridContext.getAttributes();

                String value;
                int ivalue;

                value = (String) grid.getAttribute(
                        FormatConstants.BORDER_WIDTH_ATTRIBUTE);
                try {
                    ivalue = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    ivalue = 0;
                }
                attributes.setBorderWidth(ivalue);

                value = (String) grid.getAttribute(
                        FormatConstants.BORDER_COLOUR_ATTRIBUTE);
                attributes.setBorderColor(value);

                value = (String) grid.getAttribute(
                        FormatConstants.FRAME_BORDER_ATTRIBUTE);
                attributes.setFrameBorder("true".equalsIgnoreCase(value));

                value = (String) grid.getAttribute(
                        FormatConstants.FRAME_SPACING_ATTRIBUTE);
                try {
                    ivalue = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    ivalue = 0;
                }
                attributes.setFrameSpacing(ivalue);

                // Make column width array
                int columns = grid.getColumns();

                int columnWidths [] = new int[columns];
                String columnWidthUnits [] = new String[columns];
                for (int i = 0; i < columnWidths.length; i += 1) {
                    Column column = grid.getColumn(i);
                    String width = column.getWidth();
                    if (width != null) {
                        columnWidths[i] = Integer.parseInt(width);
                    } else {
                        columnWidths[i] = -1;
                    }
                    columnWidthUnits[i] = column.getWidthUnits();
                }
                if (columnWidths.length > 0) {
                    attributes.setColumnWidths(columnWidths);
                    attributes.setColumnWidthUnits(columnWidthUnits);
                }

                // Make row height array
                int rows = grid.getRows();
                int rowHeights [] = new int[rows];
                String rowHeightUnits [] = new String[rows];
                for (int i = 0; i < rowHeights.length; i += 1) {
                    Row row = grid.getRow(i);
                    String height = row.getHeight();
                    if (height != null) {
                        rowHeights[i] = Integer.parseInt(height);
                    } else {
                        rowHeights[i] = -1;
                    }
                    rowHeightUnits[i] = row.getHeightUnits();
                }
                if (rowHeights.length > 0) {
                    attributes.setRowHeights(rowHeights);
                    attributes.setRowHeightUnits(rowHeightUnits);
                }

                // Get the module.
                LayoutModule module = context.getLayoutModule();

                module.writeOpenSegmentGrid(attributes);

                // Write out children
                int index;
                for (int r = 0; r < rows; r += 1) {
                    for (int c = 0; c < columns; c += 1) {
                        index = r * columns + c;
                        Format child = grid.getChildAt(index);
                        if (child != null) {
                            FormatInstance childInstance =
                                    context.getFormatInstance(child,
                                            instance.getIndex());
                            context.renderFormat(childInstance);
                        } else {
                            if (logger.isDebugEnabled()) {
                                logger.debug("SegmentGridRenderer.render: " +
                                        "child r" + r + ", c" + c +
                                        " is null");
                            }
                        }
                    }
                }

                module.writeCloseSegmentGrid(attributes);
            }
        } catch (IOException e) {
            throw new RendererException(
                    exceptionLocalizer.format("renderer-error",
                            instance.getFormat()), e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/5	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 21-Dec-04	6402/1	philws	VBM:2004120208 Added aligning spatial format iterator renderer

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/
