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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/TransCell.java,v 1.3 2003/01/09 11:41:36 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 09-Jan-03    Phil W-S        VBM:2003010902 - Added protocol
 *                              parameter to constructor. Remove the protocol
 *                              parameter from the process method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;

/**
 * Represents a cell found within a table in a DOM hierarchy. References to the original table row and cell DOM elements are maintained to allow specializations to more easily transform the DOM.
 * 
 * DOM pre-processing is actioned by the preprocess method in conjunction with the TransTable class.
 * 
 * DOM transformation is actioned by the process and refactor methods in conjunction with the TransTable class.
 */
public abstract class TransCell extends TransElement {
    /**
     * Initializes the instance from the given parameters. 
     */
    protected TransCell(Element row,
                        Element cell,
                        int startRow,
                        int startCol,
                        DOMProtocol protocol) {
        super(protocol);

        this.row = row;
        this.cell = cell;
        this.startRow = startRow;
        this.startCol = startCol;
    }

    /**
     * Allows the number of rows to be spanned to be updated. 
     */
    public void setRequiredRows(int requiredRows) {
        this.requiredRows = requiredRows;
    }

    /**
     * Allows the number of columns to be spanned to be updated. 
     */
    public void setRequiredCols(int requiredCols) {
        this.requiredCols = requiredCols;
    }

    /**
     * This is invoked during pre-processing of the DOM hierarchy to allow any nested tables to be pre-processed. 
     */
    public void preprocess() {
        if (table != null) {
            table.preprocess();
        }
    }

    public void process() {
        if (table != null) {
            table.process();
        }
    }

    /**
     * If the cell has a nested table within it, the nested table is instructed to re-work its DOM elements to make them immediate cell (and row) children of the given outer table. If there is no nested table, the current cell's row and col spans are simply updated as required.
     */
    public void refactor(DOMProtocol protocol, TransTable outerTable) {
        if (table != null) {
            // Refactor away the nested table
            table.refactor(protocol, outerTable, row, cell, requiredRows, requiredCols);

            // Now ditch the original "outer" cell as it is no longer needed
            cell.remove();
        } else {
            // Simply update the spans as needed
            if (requiredRows > 1) {
                setRowspan(requiredRows);
            }

            if (requiredCols > 1) {
                setColspan(requiredCols);
            }
        }
    }

    /**
     * Returns the original row index, in the containing table, in which the cell's top-left corner was found. 
     */
    public int getStartRow() {
        return startRow;
    }

    /**
     * Returns the original column index, in the containing table, in which the cell's top-left corner was found. 
     */
    public int getStartCol() {
        return startCol;
    }

    /**
     * Returns the table cell DOM element represented by an instance of this class. 
     */
    public Element getElement() {
        return cell;
    }

    /**
     * Returns the nested trans table instance, if one exists, or null otherwise. 
     */
    public TransTable getTable() {
        return table;
    }

    /**
     * Allows the nested trans table association to be set or updated. 
     */
    public void setTable(TransTable table) {
        this.table = table;
    }

    /**
     * Returns the rowspan attribute's value for the associated table cell DOM element. 
     */
    public int getRowspan() {
        return getRowspan(cell);
    }

    /**
     * Returns the colspan attribute's value for the associated table cell DOM element. 
     */
    public int getColspan() {
        return getColspan(cell);
    }

    /**
     * Sets the rowspan attribute for the associated table cell DOM element to the given value. 
     */
    private void setRowspan(int rowspan) {
        setRowspan(cell, rowspan);
    }

    /**
     * Sets the colspan attribute for the associated table cell DOM element to the given value. 
     */
    private void setColspan(int colspan) {
        setColspan(cell, colspan);
    }

    /**
     * If there is a nested table, this cell's depth is set to the table's depth value. 
     */
    public void trackDepth() {
        if (table != null) {
            setDepth(table.getDepth());
        }
    }

    /**
     * Returns the given DOM element's rowspan attribute's value. 
     */
    public static int getRowspan(Element cell) {
        return getSpan(cell, "rowspan");
    }

    /**
     * Returns the given DOM element's colspan attribute's value. 
     */
    public static int getColspan(Element cell) {
        return getSpan(cell, "colspan");
    }

    /**
     * Sets the given DOM element's rowspan attribute to the given value. 
     */
    public static void setRowspan(Element cell, int rowspan) {
        setSpan(cell, "rowspan", rowspan);
    }

    /**
     * Sets the given DOM element's colspan attribute to the given value. 
     */
    public static void setColspan(Element cell, int colspan) {
        setSpan(cell, "colspan", colspan);
    }

    /**
     * Returns the given cell's named attribute's value as an int, or 1 if the attribute doesn't exist or cannot be converted into an integer. 
     */
    private static int getSpan(Element cell, String name) {
        int result = 1;

        String span = cell.getAttributeValue(name);

        if (span != null) {
            try {
                result = Integer.parseInt(span);

                // Remove redundant span attributes to reduce page weight
                if (result == 1) {
                    cell.removeAttribute(name);
                }
            } catch (NumberFormatException e) {
                // Ignore number format issue.
            }
        }

        return result;
    }

    /**
     * Sets the given cell's named attribute to the given value. If the value is 1, the attribute is removed (it is assumed that this is the default value). 
     */
    private static void setSpan(Element cell, String name, int span) {
        if (span == 1) {
            cell.removeAttribute(name);
        } else {
            String spanAsString = Integer.toString(span);
    
            cell.setAttribute(name, spanAsString);
        }
    }

    /**
     * A cell normally spans 1 or more rows described by the rowspan attribute. However, in transforming a DOM, a cell may have to span additional "sub-rows" described by nested tables. This attribute holds the number of rows actually required, after transformation, in this cell's row. 
     */
    private int requiredRows = 1;

    /**
     * A cell normally spans 1 or more columns described by the colspan attribute. However, in transforming a DOM, a cell may have to span additional "sub-columns" described by nested tables. This attribute holds the number of columns actually required, after transformation, in this cell's column. 
     */
    private int requiredCols = 1;

    /**
     * The table row DOM element associated with this cell.
     * @supplierRole row
     */
    private Element row;

    /**
     * The table cell DOM element associated with this cell.
     * @supplierRole cell
     */
    private Element cell;

    /**
     * Indicates the row containing the top-left corner of the cell (useful if the cell has a rowspan).
     */
    private int startRow;

    /**
     * Indicates the column containing the top-left corner of the cell (useful if the cell has a colspan).
     */
    private int startCol;

    /**
     * If the cell contains nested tables, these are wrappered by a TransTable instance held in this association.
     * @supplierCardinality 0..1
     * @supplierRole table 
     */
    private TransTable table;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 ===========================================================================
*/
