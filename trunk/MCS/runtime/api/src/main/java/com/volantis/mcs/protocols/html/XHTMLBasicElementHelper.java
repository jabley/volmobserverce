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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/XHTMLBasicElementHelper.java,v 1.4 2003/01/03 09:20:33 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 11-Oct-02    Phil W-S        VBM:2002100804 - Add implementation of
 *                              getCells.
 * 02-Jan-03    Phil W-S        VBM:2002122401 - Add implementation of
 *                              isHeader, isFooter, isBody, getHeader,
 *                              getFooter and getBody.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.protocols.trans.ElementHelper;

/**
 * This is the XHTML Basic specific element helper. This follows the singleton design pattern.
 * @testcase test.com.volantis.mcs.protocols.html.TestXHTMLBasicElementHelper
 */
public final class XHTMLBasicElementHelper implements ElementHelper {
    /**
     * Table element name
     */
    private static final String TABLE = "table";

    /**
     * Table header element name - not strictly relevant to XHTMLBasic since
     * this element is not supported by this protocol
     */
    private static final String HEADER = "thead";

    /**
     * Table footer element name - not strictly relevant to XHTMLBasic since
     * this element is not supported by this protocol
     */
    private static final String FOOTER = "tfoot";

    /**
     * Table body element name - not strictly relevant to XHTMLBasic since
     * this element is not supported by this protocol
     */
    private static final String BODY = "tbody";

    /**
     * Table row element name
     */
    private static final String ROW = "tr";

    /**
     * Primary table cell element name
     */
    private static final String CELL = "td";

    /**
     * Secondary table cell element name
     */
    private static final String HEAD_CELL = "th";

    /**
     * Full set of table cell element names
     */
    private static final String[] CELLS = { CELL, HEAD_CELL };

    /**
     * Returns the singleton instance of this class. 
     */
    public static ElementHelper getInstance() {
        return instance;
    }

    public boolean isTable(Node node) {
        return (node instanceof Element) &&
                TABLE.equals(((Element)node).getName());
    }

    public boolean isHeader(Node node) {
        return (node instanceof Element) &&
                HEADER.equals(((Element)node).getName());
    }

    public boolean isFooter(Node node) {
        return (node instanceof Element) &&
                FOOTER.equals(((Element)node).getName());
    }

    public boolean isBody(Node node) {
        return (node instanceof Element) &&
                BODY.equals(((Element)node).getName());
    }

    public boolean isRow(Node node) {
        return (node instanceof Element) &&
                ROW.equals(((Element)node).getName());
    }

    public boolean isCell(Node node) {
        return (node instanceof Element) &&
                (CELL.equals(((Element)node).getName()) ||
                 HEAD_CELL.equals(((Element)node).getName()));
    }

    public String getTable() {
        return TABLE;
    }

    public String getRow() {
        return ROW;
    }

    public String getCell() {
        return CELL;
    }

    public String[] getCells() {
        return CELLS;
    }

    /**
     * This is protected to enforce the singleton pattern on this class.
     */
    private XHTMLBasicElementHelper() {
    }

    /**
     * The singleton instance of this class.
     * @supplierRole instance 
     */
    private static final ElementHelper instance = new XHTMLBasicElementHelper();
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
