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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/ElementHelper.java,v 1.5 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 11-Oct-02    Phil W-S        VBM:2002100804 - Update to allow support of
 *                              multiple cell tags. This adds getCells method
 *                              that returns a string array of the cell tags.
 * 02-Jan-03    Phil W-S        VBM:2002122401 - Update to allow support of
 *                              table sections, specifically header, footer and
 *                              body. This adds isHeader, isFooter, isBody,
 *                              getHeader, getFooter and getBody.
 * 08-Jan-03    Phil W-S        VBM:2002110402 - Javadoc update.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Node;

/**
 * Provides various utility functions such as allowing a check to be made to
 * see if the element given is a table, table section, row or cell element, or
 * returning an element name for the required type of element. This interface
 * exists because different protocols could use different element names.
 * <p/>
 * In addition some protocols may support various "flavours" of table, table
 * section row or cell element with different names.
 */
public interface ElementHelper {
    /**
     * Returns true iff the given node represents a table in the protocol. 
     *
     * @param node the node to be tested
     * @return true if the given node is a table element
     */
    boolean isTable(Node node);

    /**
     * Returns true iff the given node represents a table header in the
     * protocol.
     *
     * @param node the node to be tested
     * @return true if the given node is a table header element
     */
    boolean isHeader(Node node);

    /**
     * Returns true iff the given node represents a table footer in the
     * protocol.
     *
     * @param node the node to be tested
     * @return true if the given node is a table footer element
     */
    boolean isFooter(Node node);

    /**
     * Returns true iff the given node represents a table body in the
     * protocol.
     *
     * @param node the node to be tested
     * @return true if the given node is a table body element
     */
    boolean isBody(Node node);

    /**
     * Returns true iff the given node represents a table row in the protocol. 
     *
     * @param node the node to be tested
     * @return true if the given node is a table row element
     */
    boolean isRow(Node node);

    /**
     * Returns true iff the given node represents a table cell in the
     * protocol.
     *
     * @param node the node to be tested
     * @return true if the given node is a table cell element
     */
    boolean isCell(Node node);

    /**
     * Returns the string giving the primary element name for a table in the
     * protocol.
     *
     * @return a string giving the primary element name for a table element
     */
    String getTable();

    /**
     * Returns the string giving the primary element name for a table row in
     * the protocol.
     *
     * @return a string giving the primary element name for a table row element
     */
    String getRow();

    /**
     * Returns the string giving the primary element name for a table cell in
     * the protocol.
     *
     * @return a string giving the primary element name for a table cell
     *         element
     */
    String getCell();

    /**
     * Returns the strings giving all element names for table cells in
     * the protocol.
     *
     * @return a string array giving all element names for table cell elements
     */
    String[] getCells();
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
