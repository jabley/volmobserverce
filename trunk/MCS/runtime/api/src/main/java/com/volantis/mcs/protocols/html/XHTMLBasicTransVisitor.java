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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/XHTMLBasicTransVisitor.java,v 1.6 2003/01/16 11:29:17 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 24-Dec-02    Phil W-S        VBM:2002122402 - Remove promoteStore, change
 *                              promoteApply signature and processing.
 * 08-Jan-03    Phil W-S        VBM:2003010902 - Rename promoteApply to
 *                              promotePreserveStyle and make it invoke the
 *                              superclass method.
 * 09-Jan-03    Phil W-S        VBM:2003010906 - Ensure that style classes are
 *                              only merged if multiple style classes are
 *                              supported within the class attribute.  Affects
 *                              promotePreserveStyle.
 * 15-Jan-03    Phil W-S        VBM:2002110402 - Rework: add the single column
 *                              table removal processing (refactored from the
 *                              Netfront3 variant). This is the preprocess
 *                              method.
 * 03-Jun-03    Byron           VBM:2003042204 - Added handleStyleClass as a
 *                              result of refactoring.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.trans.AbridgedTransMapper;
import com.volantis.mcs.protocols.trans.TransCell;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransVisitor;

/**
 * The trans visitor for the XHTMLBasic unabridged transformer algorithm.
 */
public class XHTMLBasicTransVisitor extends TransVisitor {

    /**
     * The RemoveTableRule for this TransVisitor set to a default.
     */
    private final RemoveTableRule removeTableRule;

    /**
     * Initializes the instance from the given parameters.
     */
    public XHTMLBasicTransVisitor(DOMProtocol protocol, TransFactory factory) {
        super(protocol, factory);

        this.removeTableRule = factory.getRemoveTableRule();
    }

    /**
     * Get removeTableRule.
     * @return removeTableRule.
     */
    private RemoveTableRule getRemoveTableRule() {
        return removeTableRule;
    }

    protected void promotePreserveStyle(Element originalParent,
                                        Element table) {
        // Let the base class handle the non-class attributes
        super.promotePreserveStyle(originalParent, table);
    }

    /**
     * Extends the superclass method to allow "redundant" tables to be
     * removed.
     *
     * @param document the DOM to be processed
     * @see TransVisitor#preprocess(com.volantis.mcs.dom.Document document)
     * @see #removeRedundantTables
     */
    public void preprocess(Document document) {
        super.preprocess(document);

        removeRedundantTables();
    }

    /**
     * Ensures that candidate outer tables are "removed" from the DOM
     * and mapped to alternative markup. This implementation deems an
     * outer table a candidate for removal if it has a single column, if there
     * is a mapper defined by the factory.
     */
    private void removeRedundantTables() {
        AbridgedTransMapper mapper = getFactory().getMapper(protocol);

        if (mapper != null) {
            // Look for all top-level single column tables and remove those
            // that have nested table content. The array size may vary during
            // processing, never shrinking but potentially growing on each
            // iteration
            int i = 0;

            while (i < tables.size()) {
                TransTable table = (TransTable)tables.get(i);

                if (getRemoveTableRule().canRemoveTable(table)) {
                    int row;

                    // Re-map the top-level table
                    mapper.remap(table.getElement(),
                                 getFactory().getElementHelper(),
                                 true);

                    // "Promote" the nested tables, adding them to the end
                    // of the tables array so they can be processed in turn
                    // (they may be single column tables too)
                    for (row = 0; row < table.getRows(); row++) {
                        TransCell cell = table.getCell(row, 0);
                        TransTable nested = cell.getTable();

                        if ((nested != null) &&
                            (cell.getStartRow() == row)) {
                            nested.setParent(null);
                            tables.add(nested);
                            cell.setTable(null);
                        }
                    }

                    // Remove this table and release it. This will
                    // automatically move later tables down in index within
                    // the tables array so the iteration index does not need
                    // to be incremented
                    tables.remove(i);
                } else {
                    // Explicitly move on to the next table in the array
                    i++;
                }
            }
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9223/3	emma	VBM:2005080403 Remove style class from within protocols and transformers

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 14-Oct-03	1542/1	allan	VBM:2003101101 HTML_iMode single col table removal rules and pane style fixes - ported

 13-Oct-03	1540/1	allan	VBM:2003101101 Fix pane styles and single column table removal

 ===========================================================================
*/
