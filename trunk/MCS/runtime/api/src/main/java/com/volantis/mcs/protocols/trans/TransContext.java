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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/TransContext.java,v 1.2 2002/09/25 16:58:33 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;
import com.volantis.mcs.dom.Element;

/**
 * Instances of this class (or specializations) are used to hold contextual information associated with TransElements during hierarchy extraction (i.e. during the initial DOM hierarchy traversal). This contextual information includes:
 * <ol>
 * <li>the current row and column indices being processed within a given table</li>
 * <li>the current row's DOM element, if a row is currently being processed</li>
 * </ol>
 */
public class TransContext {

    /**
     * Initializes the context with the given table row DOM element. This may be null. 
     */
    public TransContext(Element row) {
        this.rowElement = row;
    }

    /**
     * Returns the table row DOM element associated with the context.
     */
    public Element getRowElement() {
        return rowElement;
    }

    /**
     * Allows the table row DOM element associated with the context to be
     * set or updated.
     */
    public void setRowElement(Element rowElement) {
        this.rowElement = rowElement;
    }

    /**
     * Returns the current row index.
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * Allows the current row index to be set or updated.
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    /**
     * Returns the current column index.
     */
    public int getColIndex() {
        return colIndex;
    }

    /**
     * Allows the current column index to be set or updated.
     */
    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    /**
     * The currently contained row index is incremented. 
     */
    public void incRowIndex() {
        rowIndex++;
    }

    /**
     * The currently contained column index is incremented. 
     */
    public void incColIndex() {
        colIndex++;
    }

    /**
     * Used during hierarchy initialization (i.e. during DOM traversal) to
     * track the table row DOM element that is currently being processed.
     * 
     * @supplierRole rowElement
     */
    private Element rowElement;

    /**
     * Used during hierarchy initialization (i.e. during DOM traversal) to
     * track the current row index within a table.
     */
    private int rowIndex = 0;

    /**
     * Used during hierarchy initialization (i.e. during DOM traversal) to
     * track the current column index within a table.
     */
    private int colIndex = 0;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
