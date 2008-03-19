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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/waptv5/WapTV5TransFactory.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Jan-03    Phil W-S        VBM:2002110402 - Created. Provides WAPTV5
 *                              protocol-specific DOM table optimization
 *                              support.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml.waptv5;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.html.RemoveTableRule;
import com.volantis.mcs.protocols.trans.AbridgedTransMapper;
import com.volantis.mcs.protocols.trans.ContainerActions;
import com.volantis.mcs.protocols.trans.ContainerValidator;
import com.volantis.mcs.protocols.trans.ElementHelper;
import com.volantis.mcs.protocols.trans.TransCell;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransVisitor;
import com.volantis.mcs.protocols.trans.TransformationConfiguration;

/**
 * Provides WAPTV5 protocol-specific DOM table optimization support.
 */
public class WapTV5TransFactory extends TransFactory {

    /**
     * The single ElementHelper instance used by this class.
     *
     * @supplierRole elementHelper
     * @link aggregation
     */
    private ElementHelper elementHelper;

    /**
     * The single ContainerValidator instance used by this class.
     */
    private ContainerValidator nestedEnabledContainerValidator;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param configuration protocol specific configuration to use when
     * transforming
     */
    public WapTV5TransFactory(TransformationConfiguration configuration) {
        super(configuration);
        initializeContainerValidator();
        initializeElementHelper();
    }

    // Javadoc inherited.
    public TransTable getTable(Element table, DOMProtocol protocol) {

        TransTable trans = new WapTV5TransTable(table, protocol);
        trans.setFactory(this);
        return trans;
    }

    // Javadoc inherited.
    public TransCell getCell(Element row, Element cell, int startRow,
            int startCol, DOMProtocol protocol) {
        // The TransCell class is abstract but is not missing any method
        // implementations. Use an anonymous derived class with no methods
        TransCell trans = new TransCell(row, cell, startRow, startCol,
                protocol) {
        };
        trans.setFactory(this);

        return trans;
    }

    // Javadoc inherited.
    protected AbridgedTransMapper getNestedDisabledMapper() {

        // WAPTV5 only works with nested tables for now as we have no mapper
        // available. The protocol has this value hardcoded to true to ensure
        // this is the case.
        throw new IllegalStateException("WAPTV5 must support nested tables;" +
                " no mapper available");
    }

    // Javadoc inherited.
    public ElementHelper getElementHelper() {
        return elementHelper;
    }

    // Javadoc inherited.
    public TransVisitor getVisitor(DOMProtocol protocol) {

        return new WapTV5TransVisitor(protocol, this);
    }

    // Javadoc inherited.
    protected ContainerValidator getNestedEnabledContainerValidator() {

        return nestedEnabledContainerValidator;
    }

    // Javadoc inherited.
    protected ContainerValidator getNestedDisabledContainerValidator() {

        // WAPTV5 only works with nested tables for now as we have no "nested
        // disabled" container validator available. The protocol has this value
        // hardcoded to true to ensure this is the case.
        throw new IllegalStateException("WAPTV5 must support nested tables;" +
                " no nested disabled container available");
    }

    // Javadoc inherited.
    public RemoveTableRule getRemoveTableRule() {

        // No mapper, so no need for a remove table rule.
        return null;
    }

    private void initializeContainerValidator() {
        // No table promotion is allowed and no remapping is needed. Always
        // retain tables nested within containers that aren't table cells
        nestedEnabledContainerValidator = new ContainerValidator() {
            public int getAction(
                    Element container,
                    Element table) {
                return ContainerActions.RETAIN;
            }
        };
    }

    private void initializeElementHelper() {
        // Simple tables only (no table sections) with one type of
        // cell.
        elementHelper = new ElementHelper() {
            protected final String table = "table";
            protected final String row = "tr";
            protected final String cell = "td";
            protected final String[] cells = {cell};

            public boolean isTable(Node node) {
                return (node instanceof Element) &&
                        table.equals(((Element) node).getName());
            }

            public boolean isHeader(Node node) {
                return false;
            }

            public boolean isFooter(Node node) {
                return false;
            }

            public boolean isBody(Node node) {
                return false;
            }

            public boolean isRow(Node node) {
                return (node instanceof Element) &&
                        row.equals(((Element) node).getName());
            }

            public boolean isCell(Node node) {
                return (node instanceof Element) &&
                        cell.equals(((Element) node).getName());
            }

            public String getTable() {
                return table;
            }

            public String getRow() {
                return row;
            }

            public String getCell() {
                return cell;
            }

            public String[] getCells() {
                return cells;
            }
        };
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Aug-05	9289/5	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
