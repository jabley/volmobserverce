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

package com.volantis.mcs.protocols.html;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransCell;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.properties.WidthKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * A class that can convert table and table cell widths from percentages to
 * pixels. It can also be used to convert widths specified via a width attribute
 * to inline CSS specified via a style attribute
 */
public final class TableWidthTransformer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(TableWidthTransformer.class);

    /**
     * Constant to indicate that percentage to pixel emulation is needed.
     */
    private static final String EMULATE_PERCENT_DEVICE_WIDTH =
        DevicePolicyConstants.PROTOCOL_CSS_EMULATE_WIDTH_PERCENTAGE_TABLE__DEVICE_WIDTH;

    /**
     * Used to indicate that table and td width styling will be specified
     * via an inline style attribute.
     */
    private static final String EMULATE_STYLE_ATTRIBUTE =
            DevicePolicyConstants.PROTOCOL_CSS_EMULATE_WIDTH_TABLE__STYLE_ATTRIBUTE;

    /**
     * True iff the table/td widths specified as percentages are to be converted
     * to pixes
     */
    private final boolean emulateWidthPercentages;

    /**
     * True iff width attributes are emulates using inline CSS via a style
     * attribute.
     */
    private final boolean emulateWidthUsingInlineStyle;

    /**
     * Constructor for TableWidthTransformer class
     * @param device the InternalDevice instance for the device making this
     * request.
     */
    public TableWidthTransformer(InternalDevice device) {
        // used to determine if we need to convert width percentage to
        // absolute pixels.
        String emulateWidthPercentPolicyValue = device.getPolicyValue(
            DevicePolicyConstants.PROTOCOL_CSS_EMULATE_WIDTH_PERCENTAGE_TABLE);

        // used to determine if we need to convert width attribute
        // values to inling style attributes.
        String emulateWidthPolicyValue = device.getPolicyValue(
            DevicePolicyConstants.PROTOCOL_CSS_EMULATE_WIDTH_TABLE);

        emulateWidthPercentages =
                EMULATE_PERCENT_DEVICE_WIDTH.equals(
                        emulateWidthPercentPolicyValue);

        emulateWidthUsingInlineStyle =
                EMULATE_STYLE_ATTRIBUTE.equals(emulateWidthPolicyValue);


    }

    /**
     * @param table The table whose percentage widths will be transformed to
     * pixel widths
     * @param useablePixels the horizontal space in pixels available on the
     * device.
     */
    public void transformPercentagesToPixels(TransTable table,
                                             int useablePixels) {

        if (emulateWidthPercentages || emulateWidthUsingInlineStyle) {

            Element tableElement = table.getElement();
            // calculate the width of the table in pixels
            int tableWidth = (useablePixels != -1) ?
                    calculatePixelWidth(tableElement, useablePixels)
                    : -1;

            processWidthAttribute(tableElement, tableWidth);

            // calculate the width in pixels of each column
            int[] columnWidths = calculateColumnWidths(table, tableWidth);

            // now that we have calculated the size of each column we can make a
            // second pass over the  TransTable to convert all percentages to
            // pixels
            for (int row = 0; row < table.getCellRows(); row++) {
                for (int col = 0; col < table.getCellCols(); col++) {
                	TransCell cell = table.getCell(row, col);
                	if (cell != null) {
                		Element element = cell.getElement();
                		processWidthAttribute(element, tableWidth);
                		// finally if the cell is represents a nested table then we
                		// need to process it
                		if (cell.getTable() != null) {
                			// calculation
                			transformPercentagesToPixels(cell.getTable(),
                					columnWidths[col]);
                		}
                	}
                }
            }
        }
    }

    /**
     * If the given element has a width attribute that is specified as
     * a percent then this method rewirtes it so that it is in pixels. If there
     * is no width attribute or it is not in pixels then this method does
     * nothing.
     * @param element the candidate element.
     * @param useableWidth the amount of pixels available.
     */
    private void processWidthAttribute(Element element,
                                       int useableWidth) {

        String width = element.getAttributeValue("width");

        if (emulateWidthPercentages && (width != null) && width.endsWith("%")) {
            // we need to convert the % to pixels
            if (useableWidth != -1) {
                int pixelWidth = convertWidthPercentageToPixels(width,
                        useableWidth);
                width = String.valueOf(pixelWidth);
                element.setAttribute("width", width);
            } else {
                width = null;
                element.removeAttribute("width");
            }
        }

        if (emulateWidthUsingInlineStyle && (width != null)) {
            element.removeAttribute("width");

            if (useableWidth != -1) {
                WidthAttribute widthAttr = new WidthAttribute(width);
                element.setAttribute("style", "width:" + widthAttr.toString());

                // we also need to add width value to element's Styles
                // collection
                StyleValue value = StyleValueFactory.getDefaultInstance()
                        .getLength(null, widthAttr.numValue, widthAttr.unit);
                element.getStyles().getPropertyValues().setComputedValue(
                        StylePropertyDetails.WIDTH, value);
            }
        }

        MutablePropertyValues propertyValues =
                element.getStyles().getPropertyValues();
        StyleValue widthValue =
                propertyValues.getStyleValue(StylePropertyDetails.WIDTH);

        if (widthValue != null) {
            if (emulateWidthPercentages && widthValue instanceof StylePercentage) {
                if (useableWidth != -1) {
                    StylePercentage percentage = (StylePercentage) widthValue;
                    double pixelWidth = useableWidth * percentage.getPercentage() / 100.0;
                    StyleLength length = StyleValueFactory.getDefaultInstance()
                            .getLength(null, pixelWidth, LengthUnit.PX);
                    propertyValues.setComputedValue(StylePropertyDetails.WIDTH, length);
                } else {
                    propertyValues.clearPropertyValue(StylePropertyDetails.WIDTH);
                }
            }
        }
    }

    /**
     * Struct holding information about width attibute (its numerical value and unit).
     */
    private static class WidthAttribute {
        final int numValue;
        final LengthUnit unit;
        final String unitSymbol;

        WidthAttribute(String attr) {
            int unitPos = 0;

            while (unitPos < attr.length()) {
                if (Character.isDigit(attr.charAt(unitPos))) {
                    unitPos++;
                } else {
                    break;
                }
            }

            if (unitPos < attr.length()) {
                numValue = Integer.parseInt(attr.substring(0, unitPos));
                unitSymbol = attr.substring(unitPos);
                unit = unitSymbol.equals("%") ? LengthUnit.PC : LengthUnit.getUnitByName(unitSymbol);
            } else {
                numValue = Integer.parseInt(attr);
                unitSymbol = "px";
                unit = LengthUnit.PX;
            }
        }

        public String toString() {
            return String.valueOf(numValue) + unitSymbol;
        }
    }

    /**
     * Attempts to calculate the width in pixels of the element that represents
     * either a table or table cell. If if a width is specified via a
     * percentage it is converted to an absolute pixel width based on the
     * useableWidth argument. IF the width cannot be determined -1 will be
     * returned
     * @param element the element
     * @param useableWidth the useable pixel width
     * @return the width in pixesl or -1 if the element did not have a width a
     * ttribute.
     */
    private int calculatePixelWidth(Element element, int useableWidth) {
        int result = -1;
        String width = element.getAttributeValue("width");
        if (width != null) {
            if (width.endsWith("%")) {
                // convert the %  to pixels based on the useableWidth
                result = convertWidthPercentageToPixels(width, useableWidth);
            } else if (width.endsWith("em") || width.endsWith("ex")) {
                logger.warn("table-width-convertion-unit-failure", width);
            } else {
                if (width.endsWith("px")) {
                    // strip of the px unit
                    width = width.substring(0, width.length() - 2);
                }

                try {
                    result = Integer.valueOf(width).intValue();
                } catch (NumberFormatException e) {
                    logger.warn("table-width-convertion-failure", width);
                }
            }
        }

        StyleValue widthValue = element.getStyles().getPropertyValues().getStyleValue(
                StylePropertyDetails.WIDTH);
        if (widthValue == WidthKeywords.AUTO) {
            
        } else if (widthValue instanceof StylePercentage) {
            StylePercentage percentage = (StylePercentage) widthValue;
            result = convertWidthPercentageToPixels(percentage.getPercentage(),
                    useableWidth);
        } else if (widthValue instanceof StyleLength) {
            StyleLength length = (StyleLength) widthValue;
            if (length.getUnit() == LengthUnit.PX) {
                result = (int) length.getNumber();
            }
        }
        return result;
    }

    /**
     * Calcultes the width of each column in a table
     * @param table the table.
     * @param useableWidth the width in pixels of the table.
     * @return the width in pixels of each column.
     */
    private int[] calculateColumnWidths(TransTable table,
                                         int useableWidth) {
        int columnCount = table.getCellCols();
        int[] columnSizes = new int[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnSizes[i] = (useableWidth != -1) ?
                    calculateColumnWidth(table, useableWidth, i)
                    : -1;
        }

        return columnSizes;
    }


    /**
     * Calcultes that width in pixels for a given coulumn in a table.
     * @param table the table
     * @param useableWidth the number of pixels of the table.
     * @param col the column
     * @return the width in pixels or -1 if it could not be determined.
     */
    private int calculateColumnWidth(TransTable table,
                                     int useableWidth,
                                     int col) {

        int maxWidth = -1;
        int rowCount = table.getRows();
        for (int row = 0; row < rowCount; row++) {
            int width = calculateCellWidth(table, useableWidth, row, col);
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }

    /**
     * Calculate the size of the given cell in pixels
     * @param table the table that contains the cell.
     * @param useableWidth the number of pixels.
     * @param row the row that the cell belongs to
     * @param col the column that the cell belongs to.
     * @return the size of the cell in pixels or -1 if it could nto be
     * determined.
     */
    private int calculateCellWidth(TransTable table,
                                   int useableWidth,
                                   int row,
                                   int col) {
    	TransCell cell = table.getCell(row, col);
    	int width;
    	if(cell == null){
    		width = -1;
    	} else {
    		width = calculatePixelWidth(cell.getElement(), useableWidth);
    		int colspan = cell.getColspan();
    		if (width != -1 && colspan > 1) {
    			width = width / colspan;
    		}        	
    	}
    	return width;
    }


    /**
     * Method that converts a width specified as a percentage to pixels.
     * @param width the width as a percentage
     * @param usableWidth the max number of pixels available.
     * @return the width as an int that specifies the width in pixels.
     */
    private int convertWidthPercentageToPixels(String width,
                                               int usableWidth) {
        final String percentageString =
                width.substring(0, width.length() - 1);
        double percentage = Double.parseDouble(percentageString);
        return convertWidthPercentageToPixels(percentage, usableWidth);
    }

    private int convertWidthPercentageToPixels(double percentage,
                                               int usableWidth) {
        return (int) (usableWidth * percentage) /100;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10799/2	geoff	VBM:2005081506 Port 2005071314 forward to MCS

 14-Dec-05	9622/2	geoff	VBM:2005081506 Port 2005071314 forward to MCS

 27-Sep-05	9622/1	doug	VBM:2005081817 Converted width percentages to absolute pixel value

 15-Sep-05	9518/2	doug	VBM:2005081817 Converted width percentages to absolute pixel value

 12-Sep-05	9441/3	doug	VBM:2005081817 Converted width percentages to absolute pixel value

 10-Aug-05	9199/1	geoff	VBM:2005071314 IBM Orange:Set the page width to be the usable width of device(pixels)

 ===========================================================================
*/
