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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/TransVisitor.java,v 1.16 2003/04/07 09:34:37 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 11-Oct-02    Phil W-S        VBM:2002100804 - Ensure that trans elements
 *                              have their parentage defined and added stub
 *                              container action handling for the
 *                              INVERSE_REMAP action (full solution
 *                              will be implemented in VBM 2002100201).
 * 15-Oct-02    Phil W-S        VBM:2002100201 - Implementation of the
 *                              INVERSE_REMAP action processing to allow
 *                              nested tables to be retained at the expense
 *                              of containing ones in certain restricted
 *                              circumstances.
 * 28-Oct-02    Phil W-S        VBM:2002100201 - Rework to ensure that other
 *                              tables within the top-level table are
 *                              retained for processing purposes when the
 *                              top-level table is INVERSE_REMAPped away.
 * 15-Nov-02    Steve           VBM:2002111207 - Problem in processInverseRemaps
 *                              where if there are two dissecting panes in a
 *                              region the first pane will remove the enclosing
 *                              table, then the second pane will throw a
 *                              null pointer exception as it cannot find its
 *                              parent. Fixed by checking if the parent table
 *                              is null before processing.
 * 11-Dec-02    Phil W-S        VBM:2002120212 - Ensure that un-remapped tables
 *                              are correctly retained after an inverse remap.
 *                              Affects processInverseRemap and adds
 *                              retainTablesWithinInverseRemap.
 * 24-Dec-02    Phil W-S        VBM:2002122402 - Remove promoteStore, change
 *                              promoteApply signature and update promote.
 * 02-Jan-03    Phil W-S        VBM:2002122401 - Update to handle table
 *                              sections. Adds the visitHeader, visitFooter,
 *                              visitBody and visitTableChildren methods and
 *                              updates visit(Element, Object) and visitTable.
 * 03-Jan-03    Phil W-S        VBM:2002122403 - Add a new table retention hook
 *                              method, retain, invoked from the visitTable
 *                              method. Also split out TransTable creation to
 *                              a new createTransTable method to avoid garbage
 *                              generation.
 * 07-Jan-03    Phil W-S        VBM:2003010712 - Update retain to utilize the
 *                              transTable's optimization facilities.
 * 08-Jan-03    Phil W-S        VBM:2003010902 - Rename promoteApply to
 *                              promotePreserveStyle. Update implementation to
 *                              use the new getPromotePreserveStyleAttributes
 *                              method to specifically preserve a given set
 *                              of attributes. The attributes to preserve
 *                              must be defined by specializations. Updated
 *                              createTransTable.
 * 09-Jan-03    Phil W-S        VBM:2003010906 - Update normalize to ensure
 *                              that all normalization tables are marked to
 *                              always be optimized away.
 * 21-Mar-03    Adrian          VBM:2003031701 - Added new method 
 *                              getFirstNonWhitespaceChild which is used in the
 *                              normalise method to ensure that we do not add 
 *                              whitespace text nodes into new table cells when
 *                              wrapping tables and their siblings. Also added 
 *                              log4j Category object to log messages from this
 *                              class. 
 * 24-Mar-03    Adrian          VBM:2003031701 - Updated method
 *                              getFirstNonWhitespaceChild to remove break 
 *                              statements. 
 * 04-Apr-03    Adrian          VBM:2003031701 - undid last two changes. 
 *                              reimplemented fix by adding method 
 *                              removeWhitespaceChildren. This is called in 
 *                              visitTable so normalize is not called if the 
 *                              table has only whitespace text nodes as 
 *                              siblings. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesMerger;
import com.volantis.styling.StylingFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Vector;
import java.util.List;
import java.util.ArrayList;

/**
 * Instances of this class allow a DOM to be pre-processed to provide a
 * simple-to-analyse structure of tables found within the DOM, then processed
 * to transform the DOM.
 * <p/>
 * The simple-to-analyse structure is created from TransTable and TransCell
 * (specialization) instances.
 * <p/>
 * The DOM is "normalized" as part of this pre-processing to ensure that nested
 * tables are always immediate and single children of table cells. This can
 * mean:
 * <p/>
 * <ol> <li>additional tables are introduced to wrap tables that have
 * siblings</li> <li>tables nested within a cell but with non-cell block DOM
 * elements standing between them may have to be "promoted" out of their
 * containing block.</li> </ol>
 * <p/>
 * Sometimes it is not possible to promote a table out of a block element,
 * depending on the type of the block element and the container validator in
 * use. In these situations several things can happen:
 * <p/>
 * <ol> <li>the table can be retained, but will be treated in the output as if
 * it were another top-level table</li> <li>the table must be removed by
 * re-mapping its elements to non-table ones</li> <li>the table can be
 * retained, becoming a top-level table, and all "containing" tables must be
 * removed by re-mapping their elements to non-table ones</li> </ol>
 * <p/>
 * How re-mapping is achieved is dictated by the AbridgedTransMapper in use.
 * <p/>
 * Note that specializations can override the retain method to explicitly
 * indicate that a given nested table should be retained (and therefore be
 * treated as a top-level one). The default implementation defers processing
 * to the TransTable in question.
 * <p/>
 * The processing of the DOM utilizes the DOM and Trans Element structures to
 * perform a transformation of the DOM, removing nested tables where possible
 * following various rules embodied within supporting classes.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public abstract class TransVisitor
        extends RecursingDOMVisitor
        implements ContainerActions {


    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(TransVisitor.class);

    /**
     * Provides functionality used in the processing of inverse remap
     * entries. These are required when a ContainerAction of INVERSE_REMAP
     * is encountered.
     */
    protected final class InverseRemap {
        /**
         * The DOM element that caused the INVERSE_REMAP action to be
         * generated.
         */
        protected final Element element;

        /**
         * The trans cell (directly or indirectly) containing the element.
         */
        protected final TransCell cell;

        /**
         * Initializes the instance using the supplied parameters.
         *
         * @param element the DOM element that caused the INVERSE_REMAP
         *        action for which the entry is being created
         * @param cell the trans cell that contains the element that
         *        caused the INVERSE_REMAP action to be generated
         */
        public InverseRemap(Element element,
                            TransCell cell) {
            this.element = element;
            this.cell = cell;
        }

        /**
         * Return the DOM element that caused the entry to be created
         *
         * @return the DOM element association
         */
        public Element getElement() {
            return element;
        }

        /**
         * Returns true iff the INVERSE_REMAP pre-conditions are met. This
         * default implementation has the pre-condition that all containing
         * tables are single-columned.
         *
         * @return true iff the INVERSE_REMAP pre-conditions are met for this
         *         entry's element
         */
        public boolean matchesPreconditions() {
            boolean result = true;
            TransElement currentElement = cell;
            TransTable table = null;

            while (result && (currentElement != null)) {
                if (currentElement instanceof TransTable) {
                    table = (TransTable)currentElement;

                    result = (table.getCols() == 1);
                }

                currentElement = currentElement.getParent();
            }

            return result;
        }

        /**
         * If the pre-conditions have been met this method should be
         * called to actually do the required inverse re-mapping. The
         * top-level containing table is returned.
         *
         * @return the top-level containing trans table
         */
        public TransTable remap() {
            TransTable table = null;
            TransElement currentElement = cell;
            TransFactory factory = getFactory();
            ElementHelper helper = factory.getElementHelper();
            AbridgedTransMapper mapper = factory.getMapper(protocol);

            while (currentElement != null) {
                if (currentElement instanceof TransTable) {
                    table = (TransTable)currentElement;

                    mapper.remap(table.getElement(),
                                 helper,
                                 true);
                }

                currentElement = currentElement.getParent();
            }

            return table;
        }
    }

    /**
     * Holds the set of top-level tables found while analysing the DOM (i.e.
     * during execution of the preprocess method). This vector may be empty
     * if no tables were found.
     */
    protected final List tables = new ArrayList();

    /**
     * Holds the set of InverseRemap entries found while analysing the DOM
     * (i.e. during execution of the preprocess method). This vector may be
     * empty if no INVERSE_REMAP actions were generated.
     */
    private final Vector inverseRemap = new Vector();

    /**
     * The protocol instance for which the transformation is being performed.
     */
    protected DOMProtocol protocol;

    /**
     * The factory used to obtain various supporting class instances.
     */
    private TransFactory factory;

    /**
     * The containing element.
     *
     * <p>Typically changed before visiting children and restored
     * afterwards.</p>
     */
    private TransElement container;

    /**
     * Initializes the instance from the given parameters.
     */
    protected TransVisitor(DOMProtocol protocol, TransFactory factory) {
        this.protocol = protocol;
        this.factory = factory;
    }

    /**
     * This method is the entry point for preprocessing a DOM to:
     * <ol>
     * <li>normalize the DOM</li>
     * <li>identify and extract the set of top-level tables within the DOM</li>
     * </ol>
     * The result is maintained within the TransVisitor's state.
     *
     * @param document the DOM to be preprocessed
     */
    public void preprocess(Document document) {
        document.forEachChild(this);
        processInverseRemaps();
    }

    /**
     * This method, called by preprocess, is used to complete any additional
     * inverse remapping pre-processing steps.
     */
    private void processInverseRemaps() {
        // The following loop intentionally checks against the vector size
        // each time as the vector may grow during this loop processing
        for (int i = 0; i < inverseRemap.size(); i++) {
            InverseRemap entry = (InverseRemap)inverseRemap.get(i);

            if (entry != null) {
                Element element = entry.getElement();

                if (entry.matchesPreconditions()) {
                    TransTable table = entry.remap();
                    Object status = element.getObject();

                    // If the defunct table has already been removed by a
                    // sibling, then don't try and remove it again.
                    if (table != null) {
                        ElementHelper helper = getFactory().getElementHelper();

                        // Remove the now defunct table from the tables set
                        tables.remove(table);

                        // "Promote" the un-remapped nested tables for later
                        // processing
                        retainTablesWithinInverseRemap(table, helper);
                    }

                    // Reset the visit mark and finish processing this
                    // part of the DOM. This may cause new top-level tables
                    // to be added to the tables set and new entries to be
                    // added to the inverseRemap set
                    element.setObject(null);
                    revisitElement(element);
                    element.setObject(status);
                } else {
                    // The entry cannot be inverse remapped so perform a
                    // standard remap of the entry's element. No other
                    // processing is needed
                    remap(element);
                    element.setObject(VisitorStatus.getDefaultInstance());
                }
            }

            // Effectively release the item from the entry set
            inverseRemap.set(i, null);
        }
    }

    /**
     * Add the remaining un-remapped nested tables to the tables array for
     * later processing. These tables may be nested several levels down in
     * the hierarchy which means that this method must operate recursively.
     *
     * @param table a table that has been remapped
     * @param helper the element helper that allows a remapping test to be
     *        made
     */
    private void retainTablesWithinInverseRemap(TransTable table,
                                                  ElementHelper helper) {
        int row;
        int col;

        // "Promote" the un-remapped nested tables, adding them to the end of
        // the tables array so they can be processed later. If a remapped table
        // is found, this needs to be searched, recursively, for un-remapped
        // nested tables within it
        for (row = 0; row < table.getRows(); row++) {
            for (col = 0; col < table.getCols(); col++) {
                TransCell cell = table.getCell(row, col);
                TransTable nested = cell.getTable();

                if (nested != null) {
                    if (helper.isTable(nested.getElement())) {
                        // We only want to take note of the table once even if
                        // it spans multiple cells
                        if ((cell.getStartRow() == row) &&
                            (cell.getStartCol() == col)) {
                            // Disconnect the table from the defunct (remapped)
                            // table's hierarchy and add it to the set of
                            // "top level" tables to be processed later
                            nested.setParent(null);
                            tables.add(nested);
                        }

                        // Complete the disconnection
                        cell.setTable(null);
                    } else {
                        // The table has been remapped so search within it
                        // for other tables that must be retained
                        retainTablesWithinInverseRemap(nested, helper);
                    }
                }
            }
        }
    }

    /**
     * This method is the entry point for actually transforming the DOM,
     * amongst other things removing the nested tables that have been
     * identified during the preprocess stage (held as state within the
     * instance).
     */
    public void process() {
        // Pre-process the tables to update the required rows and columns
        // and final row and column counts
        int size = tables.size();

        for (int i = 0; i < size; i++) {
            TransTable table = (TransTable)tables.get(i);

            if (table != null) {
                table.preprocess();
            }
        }

        // Now actually remove the nested tables, merging them into the
        // outer-most table(s)
        for (int i = 0; i < size; i++) {
            TransTable table = (TransTable)tables.get(i);

            if (table != null) {
                table.process();
            }
        }
    }


    /**
     * Utilized by the pre-processing stage, this method is invoked during
     * the first pass of the DOM for each element found in the DOM. Tables
     * and cells are identified and referenced within a parallel structure
     * of TransTables and TransCells.
     *
     * @param element the DOM element being visited
     * @return true if the visitor pattern should terminate
     */
    public void visit(Element element) {

        ElementHelper helper = getFactory().getElementHelper();
        Object elementObject = element.getObject();

        // This condition is here to allow parts of the hierarchy to
        // avoid being processed twice. This is to do with the fact that
        // normalization can cause the tree structure to be modified
        // part way through a traversal.
        if (!(elementObject instanceof VisitorStatus)) {
            if (helper.isTable(element)) {
                visitTable(element);
            } else if (helper.isHeader(element)) {
                visitHeader(element);
            } else if (helper.isFooter(element)) {
                visitFooter(element);
            } else if (helper.isBody(element)) {
                visitBody(element);
            } else if (helper.isRow(element)) {
                visitRow(element);
            } else if (helper.isCell(element)) {
                visitCell(element);
            } else {
                visitChildren(element, container);
            }
        }
    }

    /**
     * Visit the children of the specified element with the specified new
     * container.
     *
     * <p>Changes the {@link #container} field to be the specified container
     * while visiting the children and restores it afterwards.</p>
     *
     * @param element The element whose children will be visited.
     * @param newContainer The container for those children.
     */
    private void visitChildren(
            Element element, TransElement newContainer) {
        TransElement savedContainer = container;
        this.container = newContainer;
        element.forEachChild(this);
        this.container = savedContainer;
    }

    private void visitElement(Element element, TransElement newContainer) {
        TransElement savedContainer = container;
        container = newContainer;
        element.accept(this);
        container = savedContainer;
    }

    protected void revisitElement(Element element) {
        visitElement(element, null);
    }

    /**
     * Returns the factory instance being used.
     *
     * @return the factory in use
     */
    protected TransFactory getFactory() {
        return factory;
    }

    /**
     * Returns the set of top-level tables found during pre-processing.
     *
     * @return the list of TransTable elements identified during the
     *         preprocess method invocation
     */
    public List getTables() {
        return tables;
    }

    /**
     * Called when a table DOM element is visited during the pre-processing
     * DOM traversal.
     *
     * @param table the DOM table element being visited
     */
    private void visitTable(Element table) {
        TransTable transTable = null;

        if (container == null || !(container instanceof TransCell)) {
            // The new table is a top-level one so create the TransTable
            // to represent it and add that to the result set
            transTable = createTransTable(table);

            tables.add(transTable);

            visitTableChildren(transTable);
        } else {
            // The new table is embedded within a cell. Check to see if it
            // is an immediate (and only) child of the given cell. If it is
            // add it to the cell otherwise normalize the DOM
            TransCell transCell = (TransCell) container;
            Element cell = transCell.getElement();
            Element parent = table.getParent();
            
            // Ordinarily we want to normalize a DOM where a table has siblings
            // However where those siblings are whitespace nodes we see results
            // 
            if ((parent.getHead() != table) || (parent.getTail() != table)) {
                removeWhitespaceChildren(parent);
            }
        
            if ((parent == cell) &&
                (cell.getHead() == table) &&
                (cell.getTail() == table)) {
                // The table is an immediate only child of the cell, so create
                // the TransTable to represent it and set up the depth values
                transTable = createTransTable(table);

                transTable.setDepth(transCell.getDepth() + 1);

                visitTableChildren(transTable);

                // Check to see how the table should be treated
                if (retain(transTable, transCell)) {
                    // The new table must be retained, so treat it as a
                    // top-level one and add it to the result set
                    tables.add(transTable);
                } else {
                    // The table is to be treated as a candidate for
                    // nested table removal
                    transTable.setParent(transCell);
                    transCell.setTable(transTable);
                }
            } else {
                normalize(table, transCell);
            }
        }

        // Track the depth of all contained cells if a transTable was
        // created
        if (transTable != null) {
            transTable.trackDepth();
        }
    }
    
    /**
     * Check if the {@link Node} is a {@link Text} which contains only
     * whitespace characters.
     * @param node The {@link Node} to test is whitespace only.
     * @return true if the specified {@link Node} is a {@link Text} which
     * contains only whitespace.
     */
    protected boolean isWhitespaceTextNode(Node node) {
        boolean result = false;
        if ((node instanceof Text) && ((Text) node).isWhitespace()) {
            result = true;
        }
        return result;
    }

    /**
     * Remove any {@link Text} which contain only whitespace characters from
     * the children of the specified parent {@link Element}.
     * 
     * @param parent The {@link Element} whose children we wish to purge of
     * {@link Text} which contain only whitespace characters
     */
    protected void removeWhitespaceChildren(Element parent) {
        Node node = parent.getHead();
        while (node != null) {
            // Store a reference to the next sibling to assign to node at the
            // end of the loop as the current node may be discarded.
            Node next = node.getNext();

            if (isWhitespaceTextNode(node)) {
                // First we will remove it from the parent.
                node.remove();
            }
            
            // Assign the next value that we retrieved earlier.
            node = next;
        }
    }

    /**
     * Support method used to obtain a new transTable to represent a given DOM
     * table element with the TransContext initialized.
     *
     * @param table the DOM table element to be represented by the new
     *              TransTable instance
     * @return a TransTable instance initialized to represent the given DOM
     *         table element
     */
    private TransTable createTransTable(Element table) {
        TransTable transTable = getFactory().getTable(table, protocol);
        TransContext context = transTable.getContext();

        context.setRowElement(null);
        context.setRowIndex(0);
        context.setColIndex(0);

        return transTable;
    }

    /**
     * Called during table visitation, when normalization is not being
     * performed (i.e. when normalization is not required or has been
     * completed).
     *
     * @param transTable the trans table being visited
     * @return true if the visitor pattern should terminate
     */
    private void visitTableChildren(TransTable transTable) {
        Element table = transTable.getElement();
        visitChildren(table, transTable);

        if (!(table.getObject() instanceof VisitorStatus)) {
            // Make sure that this table won't be re-processed again
            table.setObject(VisitorStatus.getDefaultInstance());
        }

        if (!isSkippingRemainder()) {
            // Complete processing of the table's footer, if any. This
            // processing was postponed in visitFooter to ensure that
            // rows would be processed in the correct (visual) order
            // @todo is this really needed? Is it not just needed in processing?
            Element footer = transTable.getFooter();

            if (footer != null) {
                visitChildren(footer, transTable);
            }
        }
    }

    /**
     * Called when a table header DOM element is visited during the
     * pre-processing DOM traversal.
     *
     * @param header the DOM table header element being visited
     * @return true if the visitor pattern should terminate
     */
    private void visitHeader(Element header) {

        if (container instanceof TransTable) {
            TransTable transTable = (TransTable) container;

            transTable.setHeader(header);

            visitChildren(header, container);
        }
    }

    /**
     * Called when a table footer DOM element is visited during the
     * pre-processing DOM traversal.
     *
     * @param footer the DOM table footer element being visited
     */
    private void visitFooter(Element footer) {

        if (container instanceof TransTable) {
            TransTable transTable = (TransTable) container;

            transTable.setFooter(footer);

            // Specifically do not visit the footer's children yet. This will
            // be handled by the visitTableChildren method to ensure that
            // the footer's content is actioned after any body content
        }
    }

    /**
     * Called when a table body DOM element is visited during the
     * pre-processing DOM traversal.
     *
     * @param body the DOM table body element being visited
     */
    private void visitBody(Element body) {

        if (container instanceof TransTable) {
            TransTable transTable = (TransTable) container;

            transTable.setBody(body);

            visitChildren(body, transTable);
        }
    }

    /**
     * Called when a row DOM element is visited during the pre-processing DOM
     * traversal.
     *
     * @param row the DOM table row element being visited
     * @return true if the visitor pattern should terminate
     */
    private void visitRow(Element row) {
        // When visiting full HTML document, the 'object' argument will never be null.
        // But, when visiting XDIME Widget Response document, the <tr>
        // element may appear without enclosing table. To be more exact,
        // it may appear within <response:tbody>. In that case, the object 
        // argument is null.
        // Following 'if' clause fixes the problem by terminating visitor pattern.
        // Proper solution is probably more difficult.
        if (container == null) {
            skipRemainder();
            return;
        }
        
        TransTable table = (TransTable) container;
        TransContext context = table.getContext();

        context.setRowElement(row);
        context.setColIndex(0);

        visitChildren(row, table);

        context.incRowIndex();
        context.setRowElement(null);

        // Make sure this row doesn't get re-processed again
        row.setObject(VisitorStatus.getDefaultInstance());
    }

    /**
     * Called when a cell DOM element is visited during the pre-processing
     * DOM traversal.
     *
     * @param cell the DOM table cell element being visited
     * @return true if the visitor pattern should terminate
     */
    private void visitCell(Element cell) {
        TransTable table = (TransTable) container;
        TransContext context = table.getContext();
        TransCell newElement = table.addCell(context.getRowElement(),
                                             cell,
                                             context.getRowIndex(),
                                             context.getColIndex());

        newElement.setDepth(table.getDepth());

        visitChildren(cell, newElement);

        // Track the actual depth of any contained table
        newElement.trackDepth();

        context.incColIndex();

        // Make sure this cell doesn't get processed again
        cell.setObject(VisitorStatus.getDefaultInstance());
    }

    /**
     * Invoked against a newly created TransTable, when a table is found nested
     * within another table, this method should return true iff the given
     * TransTable should be retained (and therefore be treated as a top-level
     * one).
     *
     * @param transTable the newly created TransTable that represents a nested
     *                   table
     * @param transCell  the TransCell representing the cell in which the
     *                   nested table has been found
     * @return true iff the TransTable should be retained and therefore treated
     *         as a top-level table
     */
    private boolean retain(TransTable transTable, TransCell transCell) {
        boolean result = false;

        if (transTable != null) {
            result = !transTable.canOptimize(transCell);
        }

        return result;
    }
            
    /**
     * Invoked against a table DOM element when a table is found nested within
     * another table but not as an immediate single child of a table cell DOM
     * element. Depending on the ContainerValidator action defined for the
     * containing element the DOM is either updated with new or promoted
     * tables or the table and its content is re-mapped to non-table markup
     * or the table is left as-is as a nested (but notionally top-level) table.
     *
     * @param table the DOM table element found somewhere beneath a cell
     *        but separated from the cell by other markup, or as a sibling
     *        to other markup within the cell
     * @param cell the TransCell representing the table cell element that is
     *        the "ancestor" of the DOM table element
     * @todo provide error condition in the default branch of the switch
     */
    protected void normalize(Element table, TransCell cell) {
        Element newTable = table;
        Element parent = table.getParent();
        ElementHelper elementHelper = getFactory().getElementHelper();
        boolean cont = true;

        // If parent element is locked, a table can not be normalized,
        // since it would touch a locked element.
        // In that case, simply re-visit this element, 
        // as if it was totally stand-alone and mark it as visited.
        if (protocol.isElementLocked(parent)) {
            revisitElement(table);
            table.setObject(VisitorStatus.getDefaultInstance());
            return;
        }
        
        // Check to see if the table has siblings to take account of. The
        // normalization here is to create a new single column table that
        // encloses all children of the container and makes each direct
        // child table a separate row/cell of its own.

        // Ordinarily we want to normalize a DOM where a table has siblings
        // However where those siblings are whitespace nodes we see results
        //
        if ((parent.getHead() != table) || (parent.getTail() != table)) {
            removeWhitespaceChildren(parent);
        }

        if ((parent.getHead() != table) || (parent.getTail() != table)) {
            Node child = parent.getHead();
            boolean previousNotTable = false;

            StylingFactory factory = StylingFactory.getDefaultInstance();
            Styles parentStyles = parent.getStyles();
            Styles tableStyles;
            if (parentStyles == null) {
               // This may occur if we are dealing with custom markup
                tableStyles = factory.createStyles (null);
                if (logger.isDebugEnabled ()) {
                    logger.debug("Encountered null styles on parent element "
                            + parent.getName ());
                }
            } else {
                tableStyles = factory.createInheritedStyles(parentStyles,
                        DisplayKeywords.TABLE);
            }
            newTable = allocateElement();
            newTable.setName(elementHelper.getTable());
            newTable.setStyles(tableStyles);
            newTable.setAttribute(OptimizationConstants.OPTIMIZATION_ATTRIBUTE,
                                  OptimizationConstants.OPTIMIZE_ALWAYS);

            while (child != null) {
                // Allocate a new row/cell within the new table to receive
                // the current td's first available child

                Styles rowStyles = factory.createInheritedStyles(tableStyles,
                        DisplayKeywords.TABLE_ROW);
                Element newRow = allocateElement();
                newRow.setName(elementHelper.getRow());
                newRow.setStyles(rowStyles);

                Styles cellStyles = factory.createInheritedStyles(rowStyles,
                        DisplayKeywords.TABLE_CELL);
                Element newCell = allocateElement();
                newCell.setName(elementHelper.getCell());
                newCell.setStyles(cellStyles);

                newTable.addTail(newRow);
                newRow.addTail(newCell);

                // Remove the child from its old parent then add it into the
                // new cell
                child.remove();
                newCell.addTail(child);
                previousNotTable = !elementHelper.isTable(child);
                child = parent.getHead();

                while ((child != null) &&
                       previousNotTable &&
                       !elementHelper.isTable(child)) {
                    // Concatenate all sequential non-table children together
                    // within the same row/cell
                    child.remove();
                    newCell.addTail(child);
                    child = parent.getHead();
                }
            }

            // The parent is now childless. Add the new table in as its
            // only child
            parent.addTail(newTable);
        }

        // Look to see if the table is not a child of a table cell. If it isn't
        // some form of further normalization is needed.
        if (!elementHelper.isCell(parent)) {
            // Firstly check the container validator action to see if anything
            // should/can be done.
            final int action = getFactory().getContainerValidator(protocol).
                getAction(parent, newTable);

            switch (action) {
            case RETAIN:
                // Simply re-visit this element, as if it were totally
                // stand-alone. Mark it as visited
                revisitElement(newTable);
                newTable.setObject(VisitorStatus.getDefaultInstance());
                cont = false;
                break;

            case REMAP:
                // The table cannot be retained or promoted so it must be
                // removed. This is achieved by re-mapping its elements to
                // non-table element "equivalents". Mark it as visited
                remap(newTable);
                newTable.setObject(VisitorStatus.getDefaultInstance());
                cont = false;
                break;

            case INVERSE_REMAP:
                // The table cannot be retained or promoted but its containing
                // tables may be removable to achieve resolution of the
                // nesting problem. Mark it as visited
                addInverseRemap(newTable, cell);
                newTable.setObject(VisitorStatus.getDefaultInstance());
                cont = false;
                break;

            case PROMOTE:
                promote(newTable);
                break;

            default:
                // @todo error
            }

            // Now iterate up the hierarchy
            if (cont) {
                normalize(newTable, cell);
            }
        } else if (parent != cell.getElement()) {
            // The table has been found within a cell but this cell is not
            // the latest cell found during processing. This means that the
            // table must have been re-located due to some normalization
            // processing. Simply ignore it for now (it will be processed
            // in due course).
        } else {
            // Complete the normal visiting of the new table and mark it such
            // it won't get re-visited by the main visitor sequence
            visitElement(newTable, cell);
            newTable.setObject(VisitorStatus.getDefaultInstance());
        }
    }

    /**
     * A helper method used to allocate a new element. This utilizes the
     * protocol's DOM pool, if available.
     *
     * @return a newly allocated DOM element
     */
    private Element allocateElement() {
        DOMFactory factory = protocol.getDOMFactory();
        return factory.createElement();
    }

    /**
     * Used by normalization to promote a table up out of a containing element
     * that it is allowed to be promoted out of. This is a template method to
     * allow additional special processing of attributes. It invokes
     * promoteApply after promoting the table. The original parent of the
     * table is passed to promoteApply in case attributes from that parent
     * are needed.
     *
     * @param table the table to be promoted out of its container
     */
    private void promote(Element table) {
        // If the parent has an ID, record it and remove it in case the parent
        // actually has to be split (we don't want a duplicated ID attribute)
        Element parent = table.getParent();
        String parentId = parent.getAttributeValue("id");

        parent.removeAttribute("id");

        final boolean discard = table.promote();

        promotePreserveStyle(parent, table);

        // Re-attach the ID attribute to the parent, if it still exists
        if (!discard && parentId != null) {
            parent.setAttribute("id", parentId);
        }
    }

    /**
     * Part of the promote template method used to apply special attribute
     * values from the original parent element on the table element. It also
     * merges the Styles (style information calculated from CSS) of the
     * original parent and the table to be promoted; the table's values 'win'
     * if any property is defined by both.
     *
     * @param originalParent the parent of the table before promotion was
     *        performed
     * @param table the DOM element that is being promoted
     */
    protected void promotePreserveStyle(Element originalParent,
                                        Element table) {
        // This basic version saves the nominated attributes (if any)
        String[] attributes = getPromotePreserveStyleAttributes();

        if (attributes != null) {
            String parentAttr;
            String tableAttr;

            for (int i = 0; i < attributes.length; i++) {
                tableAttr = table.getAttributeValue(attributes[i]);

                if (tableAttr == null) {
                    parentAttr = originalParent.getAttributeValue(attributes[i]);

                    if (parentAttr != null) {
                        table.setAttribute(attributes[i],
                                           parentAttr);
                    }
                }
            }
        }

        // preserve any originalParent Styles on the table too...
        final Styles originalParentStyles = originalParent.getStyles();
        if (originalParentStyles != null) {
            StylesMerger merger =
                    StylingFactory.getDefaultInstance().getStylesMerger();
            final Styles oldTableStyles = table.getStyles();
            final Styles tableStyles =
                    merger.merge(oldTableStyles, originalParentStyles);

            // Make sure that the table gets the correct display.
            tableStyles.getPropertyValues().setComputedValue(
                    StylePropertyDetails.DISPLAY, DisplayKeywords.TABLE);
            table.setStyles(tableStyles);
        }
    }

    /**
     * This supporting method allows easier customization of which attributes
     * to preserve.
     *
     * @return an array of attribute names
     */
    protected String[] getPromotePreserveStyleAttributes() {
        return null;
    }

    /**
     * Invoked when a table is found that cannot be retained and cannot be
     * promoted (i.e. when a container action of REMAP is detected) during
     * the normalization process.
     *
     * @param table the DOM table element that has caused the container
     *        action to be generated and that requires the action to be
     *        applied
     */
    private void remap(Element table) {
        getFactory().getMapper(protocol).remap(table,
                getFactory().getElementHelper());
    }

    /**
     * Invoked when a table is found that cannot be retained and cannot be
     * promoted but where an attempt can be made to preserve the nested
     * table at the expense of the containing table(s) (i.e. when a container
     * action of INVERSE_REMAP is detected) during the normalization process.
     *
     * @param table the DOM table element that has caused the container
     *        action to be generated and that requires the action to be
     *        applied
     * @param cell the trans cell element within which the nested table that
     *        caused the INVERSE_REMAP action has been found
     */
    private void addInverseRemap(Element table,
                                   TransCell cell) {
        inverseRemap.add(new InverseRemap(table, cell));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9223/1	emma	VBM:2005080403 Remove style class from within protocols and transformers

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
